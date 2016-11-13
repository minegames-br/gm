package com.thecraftcloud.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.domain.GameConfigInstance;
import com.thecraftcloud.core.domain.GameConfigType;
import com.thecraftcloud.core.domain.Local;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.export.ExportBlock;
import com.thecraftcloud.core.logging.MGLogger;
import com.thecraftcloud.core.multiverse.MultiVerseWrapper;
import com.thecraftcloud.core.util.BlockManipulationUtil;
import com.thecraftcloud.core.util.LocationUtil;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.domain.GamePlayer;
import com.thecraftcloud.domain.MyCloudCraftGame;
import com.thecraftcloud.plugin.command.JoinGameCommand;
import com.thecraftcloud.plugin.command.LeaveGameCommand;
import com.thecraftcloud.plugin.command.StartGameCommand;
import com.thecraftcloud.plugin.command.TriggerFireworkCommand;
import com.thecraftcloud.plugin.listener.PlayerDeath;
import com.thecraftcloud.plugin.listener.PlayerQuit;
import com.thecraftcloud.plugin.listener.ServerListener;
import com.thecraftcloud.plugin.task.BuildArenaTask;
import com.thecraftcloud.plugin.task.EndGameTask;
import com.thecraftcloud.plugin.task.LevelUpTask;
import com.thecraftcloud.plugin.task.StartCoundDownTask;
import com.thecraftcloud.plugin.task.StartGameTask;

public abstract class TheCraftCloudMiniGameAbstract extends JavaPlugin {
	
	protected CopyOnWriteArraySet<GamePlayer> playerList = new CopyOnWriteArraySet<GamePlayer>();
	protected CopyOnWriteArraySet<GamePlayer> livePlayers = new CopyOnWriteArraySet<GamePlayer>();
	protected CopyOnWriteArraySet<String> playerNames = new CopyOnWriteArraySet<String>();
	protected Game game;
	protected Arena arena;
	protected World world;

	private Scoreboard scoreboard;
	private TheCraftCloudDelegate delegate =TheCraftCloudDelegate.getInstance();
	private List<GameConfigInstance> gameConfigInstanceList;
	private List<GameArenaConfig> gameArenaConfigList;
	private TheCraftCloudDelegate gameManagerDelegate = TheCraftCloudDelegate.getInstance();
	private TheCraftCloudPlugin mgplugin;
	
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
	private boolean arenaReady;

	@Override
	public void onEnable() {
		this.mgplugin = (TheCraftCloudPlugin)Bukkit.getPluginManager().getPlugin(TheCraftCloudPlugin.THE_CRAFT_CLOUD_PLUGIN);
		
		ServerInstance server = null;
		try{
			server = delegate.findServerInstance(mgplugin.getServer_uuid());
			Local l = server.getLobby();
			String worldName = server.getWorld();
			this.lobby = new Location(Bukkit.getWorld(worldName), l.getX(), l.getY(), l.getZ()); 
		}catch(Exception e) {
			e.printStackTrace();
		}

		getCommand("jogar").setExecutor(new JoinGameCommand(this));

		registerListeners();
		
		// inicializar em que mundo o jogador est�. S� deve ter um.
		// n�o deixar mudar dia/noite
		// n�o deixar fazer spawn de mobs automatico
		// deixar o hor�rio de dia
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
		Arena arena = mgplugin.getArena();
		Game game = mgplugin.getGame();
		this.game = game;
		this.arena = arena;
		
		if(arena == null || arena.getArea() == null || arena.getArea().getPointA() == null || arena.getArea().getPointB() == null) {
			Bukkit.getLogger().info("Game is not correctly configured yet. Try /mg setup");
		} else {
			File dir = mgplugin.getDataFolder();
			File schematicFile = delegate.downloadArenaSchematic(arena.getArena_uuid(), dir.getAbsolutePath());
			List<ExportBlock> arenaBlocks = new BlockManipulationUtil().loadSchematic(this, schematicFile, this.world);
			this.startArenaBuild(arena.getName(), arenaBlocks);
		}
	}
	
	public void init(World _world, Local _lobby) {
		Arena arena = mgplugin.getArena();
		Game game = mgplugin.getGame();
		this.game = game;
		this.arena = arena;
		
		this.world = _world;
		this.lobby = Utils.toLocation(_world, _lobby);

		this.endGameTask = new EndGameTask(this);
		this.levelUpTask = new LevelUpTask(this);
		this.myCloudCraftGame = new MyCloudCraftGame();
		
		this.gameConfigInstanceList = delegate.findAllGameConfigInstanceByGameUUID(this.mgplugin.getGame().getGame_uuid().toString());
		this.gameArenaConfigList = delegate.findAllGameConfigArenaByGameArena(this.mgplugin.getGame().getGame_uuid().toString(), this.mgplugin.getArena().getArena_uuid().toString());
		
		// Agendar as threads que v�o detectar se o jogo pode comecar
		this.startCountDownTask = new StartCoundDownTask(this);
		this.startGameTask = new StartGameTask(this);

		BukkitScheduler scheduler = getServer().getScheduler();
		this.startGameThreadID = scheduler.scheduleSyncRepeatingTask(this, this.startGameTask, 0L, 20L);
		this.startCountDownThreadID = scheduler.scheduleSyncRepeatingTask(this, this.startCountDownTask, 0L, 25L);

		this.setStartCountDown();
	}

	protected void loadLobby() {
		this.mgplugin = (TheCraftCloudPlugin)Bukkit.getPluginManager().getPlugin(TheCraftCloudPlugin.THE_CRAFT_CLOUD_PLUGIN);
		
		try{
			ServerInstance server = delegate.findServerInstance(mgplugin.getServer_uuid());
			Local l = server.getLobby();
			String worldName = server.getWorld();
			this.world = Bukkit.getWorld(worldName);
			this.lobby = new Location(this.world, l.getX(), l.getY(), l.getZ());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void startGameEngine() {
		Arena arena = mgplugin.getArena();
		Game game = mgplugin.getGame();
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
		
		//mudar hor�rio da arena
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
		// mandar os jogadores de volta para o lobby
		teleportPlayersBackToLobby();

		// limpar inventario do jogador
		clearPlayersInventory();

        File dir = mgplugin.getDataFolder();
    	File schematicFile = delegate.downloadArenaSchematic(arena.getArena_uuid(), dir.getAbsolutePath());
    	List<ExportBlock> arenaBlocks = new BlockManipulationUtil().loadSchematic(this, schematicFile, this.world);
		this.arenaBlocks = arenaBlocks;
		LocationUtil locationUtil = new LocationUtil();
		Location pointA = locationUtil.toLocation(this.getWorld(), arena.getArea().getPointA());
		Location pointB = locationUtil.toLocation(this.getWorld(), arena.getArea().getPointB());
		List<Block> list = new BlockManipulationUtil().blocksFromTwoPoints(pointA, pointB);
		List<ExportBlock> blocks = new ArrayList<ExportBlock>();
		for(Block block: list) {
			ExportBlock eb = new ExportBlock();
			eb.setX(block.getX());
			eb.setY(block.getY());
			eb.setZ(block.getZ());
			eb.setMaterial(Material.AIR);
			blocks.add(eb);
		}
		arenaBlocks.addAll(blocks);
		startArenaBuild(this.arena.getName(), this.arenaBlocks);

	}
	
	
	protected void teleportPlayersBackToLobby() {
		for(GamePlayer gp: this.livePlayers) {
			Player p = gp.getPlayer();
			p.teleport(lobby);
		}
	}

	public CopyOnWriteArraySet<GamePlayer> getLivePlayers() {
		return this.livePlayers;
	}

	public void startArenaBuild(String arenaName, List<ExportBlock> arenaBlocks) {
		this.arenaReady = false;
		Bukkit.getLogger().info("startArenaBuild - " + arenaName);
		this.world = Bukkit.getWorld(arenaName);
		if(this.world == null) {
	        //criar mundo void para tentar ver se a recriacao da arena fica pronta mais rapidamente
			this.world = new MultiVerseWrapper().createWorldVoid( arenaName );
			Bukkit.getLogger().info("startArenaBuild - mundo criado: " + arenaName);
		} else {
			Bukkit.getLogger().info("startArenaBuild - mundo existente: " + arenaName);
		}
		
		this.setArenaBlocks(arenaBlocks);

		this.indexBlock = 0;
		BukkitScheduler scheduler = Bukkit.getScheduler();
		this.buildArenaTask = new BuildArenaTask(this);
		this.threadIds = new CopyOnWriteArrayList<Integer>();
		for(int i = 0; i < 1; i++) {
			Integer _buildArenaThreadID = scheduler.scheduleSyncRepeatingTask(this, this.buildArenaTask, 5L, 10L);
			this.threadIds.add(_buildArenaThreadID);
		}
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


	public Object getGameConfigInstance(String name) {
		Object result = null;
		for(GameConfigInstance gci: this.gameConfigInstanceList) {
			if(gci.getGameConfig().getName().equals(name)) {
				if(gci.getGameConfig().getConfigType() == GameConfigType.INT) {
					result = gci.getIntValue();
				}if(gci.getGameConfig().getConfigType() == GameConfigType.LOCAL) {
					result = gci.getLocal();
				}if(gci.getGameConfig().getConfigType() == GameConfigType.AREA3D) {
					result = gci.getArea();
				}
			}
		}
		
		return result;
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


	public Object getGameArenaConfig(String name) {
		Object result = null;
		for(GameArenaConfig gac: this.gameArenaConfigList) {
			if(gac.getGameConfig().getName().equals(name)) {
				if(gac.getGameConfig().getConfigType() == GameConfigType.INT) {
					result = gac.getIntValue();
					break;
				}if(gac.getGameConfig().getConfigType() == GameConfigType.LOCAL) {
					result = gac.getLocalValue();
					break;
				}if(gac.getGameConfig().getConfigType() == GameConfigType.AREA3D) {
					result = gac.getAreaValue();
					break;
				}
			}
		}
		
		return result;
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
		this.myCloudCraftGame.startCountDown();
	}

	public void proceedCountdown() {
		
		if (this.countDown != 0) {
			this.countDown--;
			Bukkit.broadcastMessage(Utils.color("&6O jogo vai come�ar em " + this.countDown + " ..."));
		} else {
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

		this.setStartCountDown();
		
	}

	public void removeLivePlayer(Player player) {
		GamePlayer gp = findGamePlayerByPlayer(player);

		if (gp != null) {
			if (player != null) {
				player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
				player.getInventory().clear();
				teleportPlayersBackToLobby(player);
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
		pm.registerEvents(new PlayerDeath(this), this);
		pm.registerEvents(new ServerListener(this), this);
	}
	
	public void teleportPlayersBackToLobby(Player player) {
		
		Location l = Utils.toLocation(this.getWorld(), this.getLobby());
		player.teleport(l);
	}
	
	public GamePlayer findGamePlayerByPlayer(Player player) {
		for (GamePlayer gp : playerList) {
			if (gp.getPlayer().equals(player)) {
				return gp;
			}
		}
		return null;
	}

	private void removeBossBars() {
		for (GamePlayer gp : livePlayers) {
			if(gp.getBaseBar() != null) {
				gp.getBaseBar().removeAll();
			}
		}
	}

	private void removeBossBar(Player player) {
		GamePlayer gp = findGamePlayerByPlayer(player);
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
		if (findGamePlayerByPlayer(player) == null) {
			GamePlayer gp = createGamePlayer();
			gp.setPlayer(player);
			playerList.add(gp);
			livePlayers.add(gp);
			player.sendMessage(Utils.color("&aBem vindo, Arqueiro!"));
			playerNames.add(player.getName());
		} else {
		}
	}
	
	public abstract GamePlayer createGamePlayer();

	public abstract void killPlayer(Player player);

	public abstract void killEntity(Entity z);
	
	public abstract Integer getStartCountDown();

	public abstract void setStartCountDown();

	public abstract Local getLobby();

	public abstract void setLobby();

	public abstract Integer getMinPlayers();

	public abstract Integer getMaxPlayers();

	public boolean isGameReady() {
		return this.arenaReady;
	}

}
