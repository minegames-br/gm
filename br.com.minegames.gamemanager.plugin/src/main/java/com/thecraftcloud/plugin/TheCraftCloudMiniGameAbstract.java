package com.thecraftcloud.plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.client.exception.InvalidRegistrationException;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.domain.GameConfigInstance;
import com.thecraftcloud.core.domain.GameInstance;
import com.thecraftcloud.core.domain.Local;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.export.ExportBlock;
import com.thecraftcloud.core.json.JSONParser;
import com.thecraftcloud.core.util.LocationUtil;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.core.util.zip.ExtractZipContents;
import com.thecraftcloud.domain.EntityPlayer;
import com.thecraftcloud.domain.GamePlayer;
import com.thecraftcloud.domain.MyCloudCraftGame;
import com.thecraftcloud.plugin.command.JoinGameCommand;
import com.thecraftcloud.plugin.command.LeaveGameCommand;
import com.thecraftcloud.plugin.command.StartGameCommand;
import com.thecraftcloud.plugin.command.TriggerFireworkCommand;
import com.thecraftcloud.plugin.listener.PlayerQuit;
import com.thecraftcloud.plugin.listener.ServerListener;
import com.thecraftcloud.plugin.service.ConfigService;
import com.thecraftcloud.plugin.service.GameInstanceService;
import com.thecraftcloud.plugin.service.PlayerService;
import com.thecraftcloud.plugin.task.BuildArenaTask;
import com.thecraftcloud.plugin.task.EndGameTask;
import com.thecraftcloud.plugin.task.LevelUpTask;
import com.thecraftcloud.plugin.task.StartCoundDownTask;
import com.thecraftcloud.plugin.task.StartGameTask;

public abstract class TheCraftCloudMiniGameAbstract extends JavaPlugin {
	
	protected CopyOnWriteArraySet<GamePlayer> playerList = new CopyOnWriteArraySet<GamePlayer>();
	protected CopyOnWriteArraySet<GamePlayer> livePlayers = new CopyOnWriteArraySet<GamePlayer>();
	protected CopyOnWriteArraySet<String> playerNames = new CopyOnWriteArraySet<String>();
	protected CopyOnWriteArraySet<EntityPlayer> livingEntities = new CopyOnWriteArraySet<EntityPlayer>();
	protected Game game;
	protected Arena arena;
	protected World world;

	private Scoreboard scoreboard;
	private TheCraftCloudDelegate delegate =TheCraftCloudDelegate.getInstance();
	private List<GameConfigInstance> gameConfigInstanceList;
	private HashMap<Player, Local> mapPlayerLocal = new HashMap<Player,Local>();
	private List<GameArenaConfig> gameArenaConfigList;
	private TheCraftCloudDelegate gameManagerDelegate = TheCraftCloudDelegate.getInstance();
	private TheCraftCloudPlugin mgplugin;

	protected Integer maxPlayers;
	protected Integer minPlayers;
	protected Local lobbyLocal;
	

	protected List<ExportBlock> arenaBlocks;
	protected int indexBlock = 0;
	
	protected BuildArenaTask buildArenaTask;
	protected List<Integer> threadIds;
	private Runnable endGameTask;
	private int endGameThreadID;
	protected int countDown;
	private GamePlayer winner;
	private StartGameTask startGameTask;
	private int startGameThreadID;
	private int startCountDownThreadID;
	private Runnable startCountDownTask;

	private long gameStartTime;
	
	private Runnable levelUpTask;
	private int levelUpThreadID;
	protected MyCloudCraftGame myCloudCraftGame;
	protected Location lobby;
	private boolean arenaReady = true;

	protected LocationUtil locationUtil = new LocationUtil();
	protected PlayerService playerService = new PlayerService(this);
	protected GameInstanceService giService = new GameInstanceService(this);
	protected ConfigService configService;
	
	protected GameInstance gameInstance;

	@Override
	public void onEnable() {
		this.mgplugin = (TheCraftCloudPlugin)Bukkit.getPluginManager().getPlugin(TheCraftCloudPlugin.THE_CRAFT_CLOUD_PLUGIN);
		this.configService = ConfigService.getInstance();
		try {
			this.configService.loadTheCraftCloudData(mgplugin, false);
		} catch (InvalidRegistrationException e1) {
			e1.printStackTrace();
			return;
		}
		
		ServerInstance server = null;
		try{
			server = this.getConfigService().getServerInstance();
			Bukkit.getConsoleSender().sendMessage("[TheCraftCloud] getConfigService instanceof: " + this.getConfigService().getClass() );
			if(server == null) {
				Bukkit.getConsoleSender().sendMessage("[TheCraftCloud] Cannot load The Craft Cloud configuration. Please register server first.");
				return;
			}
			Local l = server.getLobby();
			String worldName = server.getWorld();
			this.lobby = new Location(Bukkit.getWorld(worldName), l.getX(), l.getY(), l.getZ()); 
		}catch(Exception e) {
			e.printStackTrace();
		}

		Bukkit.getLogger().info( this.getCommand("jogar") + "");
		this.getCommand("jogar").setExecutor(new JoinGameCommand(this));
		
		registerListeners();
		
		// inicializar em que mundo o jogador está. Só deve ter um.
		// não deixar mudar dia/noite
		// não deixar fazer spawn de mobs automatico
		// deixar o horário de dia
		for(World world: Bukkit.getWorlds() ) {
			world.setTime(1000);
			world.setSpawnFlags(false, false);
			world.setGameRuleValue("doMobSpawning", "false");
			world.setGameRuleValue("doDaylightCycle", "false");
		}

		getCommand("iniciar").setExecutor(new StartGameCommand(this));
		getCommand("sair").setExecutor(new LeaveGameCommand(this));
		getCommand("fwk").setExecutor(new TriggerFireworkCommand(this));

		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();
		TheCraftCloudPlugin mgplugin = (TheCraftCloudPlugin)Bukkit.getPluginManager().getPlugin(TheCraftCloudPlugin.THE_CRAFT_CLOUD_PLUGIN);
		Arena arena = configService.getArena();
		Game game = configService.getGame();
		this.game = game;
		this.arena = arena;
		
		if(arena == null || arena.getArea() == null || arena.getArea().getPointA() == null || arena.getArea().getPointB() == null) {
			Bukkit.getLogger().info("Game is not correctly configured yet. Try /mg setup");
		} else {
			this.startArenaBuild(arena.getName());
		}
	}
	
	public void init(Local _lobby) {
		World world = Bukkit.getWorld(this.arena.getName());
		init(world, _lobby);
	}
	
	public void init(World _world, Local _lobby) {
		BukkitScheduler scheduler = getServer().getScheduler();

		Arena _arena = configService.getArena();
		Game _game = configService.getGame();
		this.game = _game;
		this.arena = _arena;
		
		this.world = _world;
		this.lobby = Utils.toLocation(_world, _lobby);

		this.endGameTask = new EndGameTask(this);
		this.levelUpTask = new LevelUpTask(this);
		this.myCloudCraftGame = new MyCloudCraftGame();

		//Criar GameInstance com datas vazias e state WAITING
		scheduler.runTaskAsynchronously(this, new Runnable() {
			public void run() {
				gameInstance = giService.startGameInstance(configService.getServerInstance(), game, arena);
			}
		});
		
		
		try {
			Bukkit.getLogger().info("Loading offline config");
			this.updateOfflineConfig(false);
		} catch (InvalidRegistrationException e) {
			e.printStackTrace();
		}
		
		// Agendar as threads que vão detectar se o jogo pode comecar
		this.startCountDownTask = new StartCoundDownTask(this);
		this.startGameTask = new StartGameTask(this);

		this.startGameThreadID = scheduler.scheduleSyncRepeatingTask(this, this.startGameTask, 0L, 20L);
		this.startCountDownThreadID = scheduler.scheduleSyncRepeatingTask(this, this.startCountDownTask, 0L, 25L);

		this.configService.setStartCountDown();
	}

	protected void loadLobby() {
		this.mgplugin = (TheCraftCloudPlugin)Bukkit.getPluginManager().getPlugin(TheCraftCloudPlugin.THE_CRAFT_CLOUD_PLUGIN);
		
		try{
			ServerInstance server = configService.getServerInstance();
			Local l = server.getLobby();
			String worldName = server.getWorld();
			this.world = Bukkit.getWorld(worldName);
			this.lobby = new Location(this.world, l.getX(), l.getY(), l.getZ());
			Bukkit.getLogger().info("lobby: " + this.world.getName() + l);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void updateOfflineConfig(Boolean force) throws InvalidRegistrationException {
		try{
	    	File configDir = getDataFolder();
	
			String json = null;
	        ObjectMapper mapper = new ObjectMapper();
	
			File gameConfigListJsonFile = new File( configDir, "gameConfigList" + ".json");
			if( gameConfigListJsonFile.exists() && !force ) {
				Bukkit.getLogger().info("Game Config List Json exists");
				json = FileUtils.readFileToString(gameConfigListJsonFile);
				try {
					this.gameConfigInstanceList = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, GameConfigInstance.class));
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Bukkit.getLogger().info("GameConfigList size: " + this.gameConfigInstanceList.size() );
			} else {
				this.gameConfigInstanceList = delegate.findAllGameConfigInstanceByGameUUID(this.game.getGame_uuid().toString());
				json = JSONParser.getInstance().toJSONString(this.gameConfigInstanceList);
				FileUtils.writeStringToFile(gameConfigListJsonFile, json);
			}
			
			File gameArenaConfigListJsonFile = new File( configDir, "gameArenaConfigList" + ".json");
			if( gameArenaConfigListJsonFile.exists() && !force ) {
				Bukkit.getLogger().info("Game Arena Config List Json exists");
				json = FileUtils.readFileToString(gameArenaConfigListJsonFile);
				try {
					this.gameArenaConfigList = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, GameArenaConfig.class));
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Bukkit.getLogger().info("GameArenaConfigList size: " + this.gameArenaConfigList.size() );
			} else {
				this.gameArenaConfigList = delegate.findAllGameConfigArenaByGameArena(this.game.getGame_uuid().toString(), this.arena.getArena_uuid().toString());
				json = JSONParser.getInstance().toJSONString(this.gameArenaConfigList);
				FileUtils.writeStringToFile(gameArenaConfigListJsonFile, json);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new InvalidRegistrationException("Server not registered or Game/Arena config is invalid.");
		}
		Bukkit.getLogger().info("GameConfigList size: " + this.gameConfigInstanceList.size() );
		Bukkit.getLogger().info("GameArenaConfigList size: " + this.gameArenaConfigList.size() );

	}

	
	public void startGameEngine() {
		Arena arena = configService.getArena();
		Game game = configService.getGame();
		this.game = game;
		this.arena = arena;
		
		BukkitScheduler scheduler = getServer().getScheduler();
		
		if(this.lobby == null) {
			loadLobby();
		}
		
		// Terminar threads de preparacao do jogo
		scheduler.cancelTask(startCountDownThreadID);
		scheduler.cancelTask(startGameThreadID);

		this.endGameThreadID = scheduler.scheduleSyncRepeatingTask(this, this.endGameTask, 0L, 50L);
		this.levelUpThreadID = scheduler.scheduleSyncRepeatingTask(this, this.levelUpTask, 0L, 50L);
		this.start();
		this.myCloudCraftGame.start();
		
		//mudar horário da arena
		Integer time = this.arena.getTime();
		if( time != null) {
			this.world.setTime(time);
		}
	}

	public void endGame() {
		Bukkit.getScheduler().cancelTask(this.endGameThreadID);
		Bukkit.getScheduler().cancelTask(this.levelUpThreadID);

		// remover as bossbars
		removeBossBars();

		// limpar inventario do jogador
		clearPlayersInventory();

		startArenaBuild(this.arena.getName());
	}
	
	public CopyOnWriteArraySet<GamePlayer> getLivePlayers() {
		return this.livePlayers;
	}

	public void startArenaBuild(String arenaName) {
		File dir = this.getServer().getWorldContainer();
		//File schematicFile = delegate.downloadArenaSchematic(arena.getArena_uuid(), dir.getAbsolutePath());
		File arenaWorldFile = new File(dir, arena.getName());
		
		MultiverseCore mvplugin = (MultiverseCore)Bukkit.getPluginManager().getPlugin("Multiverse-Core");
		String worldPath = dir.getAbsolutePath() + "/" + arena.getName();
				
		if(!arenaWorldFile.exists()) {
			File zipFile = delegate.downloadArenaWorld(arena, dir);
			ExtractZipContents.unzip(zipFile);
			Bukkit.getLogger().info("world path: " + worldPath);
			mvplugin.getCore().getMVWorldManager().addWorld(arena.getName(), Environment.NORMAL, new Integer( arena.getName().hashCode() ).toString(), WorldType.NORMAL, new Boolean(false), null, false);
		} else {
			//Remover o mundo do multi verse para recarregar
			mvplugin.getMVWorldManager().unloadWorld(arena.getName());
			
			//Apagar o diretório
			try {
				FileUtils.deleteDirectory(arenaWorldFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//Descompactar arena
			File zipFile = new File( dir, arena.getName() + ".zip");
			ExtractZipContents.unzip(zipFile);
			
			//Carregar o mundo no multiverse
			Bukkit.getLogger().info("world path: " + worldPath);
			mvplugin.getCore().getMVWorldManager().addWorld(arena.getName(), Environment.NORMAL, new Integer( arena.getName().hashCode() ).toString(), WorldType.NORMAL, new Boolean(false), null, false);
		}
		this.world = Bukkit.getWorld(this.arena.getName());
	}
	
	public void setArenaBlocks(List<ExportBlock> arenaBlocks) {
		this.arenaBlocks = arenaBlocks;
	}

	public void completeBuildArenaTask() {
		BukkitScheduler scheduler = Bukkit.getScheduler();
		for(int i = 0; i < threadIds.size(); i++) {
			scheduler.cancelTask( threadIds.get(i) );
		}
		threadIds.clear();
		this.arenaReady = true;
	}

	public ExportBlock getNextBlock() {
		if(indexBlock < arenaBlocks.size()) {
			ExportBlock block = arenaBlocks.get(indexBlock);
			indexBlock++;
			return block;
		} else {
			return null;
		}
	}

	public World getWorld() {
		return this.world;
	}

	public List<GameConfigInstance> getGameConfigInstanceList() {
		return gameConfigInstanceList;
	}

	public void setGameConfigInstanceList(List<GameConfigInstance> gameConfigInstanceList) {
		this.gameConfigInstanceList = gameConfigInstanceList;
	}

	public List<GameArenaConfig> getGameArenaConfigList() {
		return gameArenaConfigList;
	}

	public void setGameArenaConfigList(List<GameArenaConfig> gameArenaConfigList) {
		this.gameArenaConfigList = gameArenaConfigList;
	}


	public CopyOnWriteArraySet<GameArenaConfig> getGameArenaConfigByGroup(String group) {
		
		CopyOnWriteArraySet<GameArenaConfig> gacs = new CopyOnWriteArraySet<GameArenaConfig>();
		for(GameArenaConfig gac: this.gameArenaConfigList) {
			if(gac.getGameConfig().getGroupName().equalsIgnoreCase(group)){
				gacs.add(gac);
			}
		}
		
		return gacs;
	}


	public abstract boolean shouldEndGame();

	public MyCloudCraftGame getMyCloudCraftGame() {
		return this.myCloudCraftGame;
	}

	public abstract boolean isLastLevel();

	public abstract void levelUp();

	public GamePlayer getWinner() {
		return winner;
	}

	public void setWinner(GamePlayer winner) {
		this.winner = winner;
	}

	public void startCoundDown() {
		this.countDown = (Integer)configService.getGameConfigInstance(TheCraftCloudConfig.START_COUNTDOWN);
		this.myCloudCraftGame.startCountDown();
	}

	public void proceedCountdown() {
		Bukkit.getConsoleSender().sendMessage(Utils.color("&5[TheCraftCloudMiniGameAbstract] - proceedCountdown: " + this.countDown));
		if (this.countDown != 0) {
			this.countDown--;
			Bukkit.broadcastMessage(Utils.color("&6O jogo vai começar em " + this.countDown + " ..."));
		} else {
			Bukkit.broadcastMessage(Utils.color("&6O Boa sorte! O Jogo Começou."));
			this.startGameEngine();
			Bukkit.getScheduler().cancelTask(startCountDownThreadID);
		}
	}

	protected void start() {
		
		// Remover qualquer entidade que tenha ficado no mapa
		for(World world: Bukkit.getWorlds() ) {
			world.setTime(1000);
			world.setSpawnFlags(false, false);
			world.setGameRuleValue("doMobSpawning", "false");
			world.setGameRuleValue("doDaylightCycle", "false");
			for (Entity entity : world.getEntities()) {
				if (!(entity instanceof Player) && entity instanceof LivingEntity) {
					entity.remove();
				}
			}
		}

		this.configService.setStartCountDown();
		
	}

	public void removeLivePlayer(Player player) {
		GamePlayer gp = this.playerService.findGamePlayerByPlayer(player);

		if (gp != null) {
			if (player != null) {
				player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
				player.getInventory().clear();
				
				Local l = (Local)mapPlayerLocal.get(player);
				Location loc = locationUtil.toLocation(Bukkit.getWorld(l.getName() ), l );
				player.teleport(loc);
			}
			removeBossBar(gp);
			livePlayers.remove(gp);
		}

		if (livePlayers.size() == 0) {
			this.myCloudCraftGame.endGame();
			this.endGame();
		}
	}
	
	protected void registerListeners() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerQuit(this), this);
		pm.registerEvents(new ServerListener(this), this);
	}
	
	public void teleportPlayersBackToLobby(Player player) {
		Local local = mapPlayerLocal.get(player);
		Location l = locationUtil.toLocation(Bukkit.getWorld(local.getName()), local);
		player.teleport(l);
	}
	
	private void removeBossBars() {
		for (GamePlayer gp : livePlayers) {
			if(gp.getBaseBar() != null) {
				gp.getBaseBar().removeAll();
			}
		}
	}

	private void removeBossBar(Player player) {
		GamePlayer gp = this.playerService.findGamePlayerByPlayer(player);
		if(gp.getBaseBar() != null) {
			gp.getBaseBar().removeAll();
		}
	}

	private void removeBossBar(GamePlayer gp) {
		if(gp.getBaseBar() != null) {
			gp.getBaseBar().removeAll();
		}
	}

	private void clearPlayersInventory() {
		for (GamePlayer gp : livePlayers) {
			Player player = gp.getPlayer();
			this.clearPlayerInventory(player);
		}
	}

	private void clearPlayerInventory(Player player) {
		PlayerInventory inventory = player.getInventory();

		inventory.clear();
		inventory.setArmorContents(null);
	}

	public void addPlayer(Player player) {
		if (this.playerService.findGamePlayerByPlayer(player) == null) {
			GamePlayer gp = createGamePlayer();
			gp.setPlayer(player);
			playerList.add(gp);
			livePlayers.add(gp);
			//player.sendMessage(Utils.color("&aBem vindo, Arqueiro!"));
			playerNames.add(player.getName());
		} else {
		}
	}
	
	public abstract GamePlayer createGamePlayer();

	public boolean isGameReady() {
		return this.arenaReady;
	}

	public void addPlayer(Player player, Local l) {
		this.mapPlayerLocal.put(player, l);
		this.addPlayer(player);
	}

	public void addEntityPlayer(EntityPlayer entityPlayer) {
		livingEntities.add(entityPlayer);
	}

	public CopyOnWriteArraySet<EntityPlayer> getLivingEntities() {
		return this.livingEntities;
	}

	public ConfigService getConfigService() {
		return this.configService;
	}

	public GameInstance getGameInstance() {
		return gameInstance;
	}

	public void setGameInstance(GameInstance gameInstance) {
		this.gameInstance = gameInstance;
	}

	
}
