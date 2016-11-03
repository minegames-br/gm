package br.com.minegames.gamemanager.plugin;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import br.com.minegames.core.domain.Arena;
import br.com.minegames.core.domain.Game;
import br.com.minegames.core.domain.GameArenaConfig;
import br.com.minegames.core.domain.GameConfigInstance;
import br.com.minegames.core.domain.GameConfigType;
import br.com.minegames.core.domain.Local;
import br.com.minegames.core.export.ExportBlock;
import br.com.minegames.core.logging.MGLogger;
import br.com.minegames.core.multiverse.MultiVerseWrapper;
import br.com.minegames.core.util.Utils;
import br.com.minegames.gamemanager.client.GameManagerDelegate;
import br.com.minegames.gamemanager.domain.GamePlayer;
import br.com.minegames.gamemanager.domain.MyCloudCraftGame;
import br.com.minegames.gamemanager.plugin.command.JoinGameCommand;
import br.com.minegames.gamemanager.plugin.command.LeaveGameCommand;
import br.com.minegames.gamemanager.plugin.command.StartGameCommand;
import br.com.minegames.gamemanager.plugin.command.TriggerFireworkCommand;
import br.com.minegames.gamemanager.plugin.listener.PlayerDeath;
import br.com.minegames.gamemanager.plugin.listener.PlayerQuit;
import br.com.minegames.gamemanager.plugin.listener.ServerListener;
import br.com.minegames.gamemanager.plugin.task.BuildArenaTask;
import br.com.minegames.gamemanager.plugin.task.EndGameTask;
import br.com.minegames.gamemanager.plugin.task.LevelUpTask;
import br.com.minegames.gamemanager.plugin.task.StartCoundDownTask;
import br.com.minegames.gamemanager.plugin.task.StartGameTask;

public abstract class MyCloudCraftPlugin extends JavaPlugin {
	
	protected CopyOnWriteArraySet<GamePlayer> playerList = new CopyOnWriteArraySet<GamePlayer>();
	protected CopyOnWriteArraySet<GamePlayer> livePlayers = new CopyOnWriteArraySet<GamePlayer>();
	protected CopyOnWriteArraySet<String> playerNames = new CopyOnWriteArraySet<String>();
	protected Game game;
	protected Arena arena;
	protected World world;

	private Scoreboard scoreboard;
	private GameManagerDelegate delegate =GameManagerDelegate.getInstance();
	private List<GameConfigInstance> gameConfigInstanceList;
	private List<GameArenaConfig> gameArenaConfigList;
	private GameManagerDelegate gameManagerDelegate = GameManagerDelegate.getInstance();
	private MineGamesPlugin mgplugin;
	
	protected List<ExportBlock> arenaBlocks;
	protected int indexBlock = 0;
	
	protected BuildArenaTask buildArenaTask;
	protected List<Integer> threadIds;
	private Runnable endGameTask;
	private int endGameThreadID;
	private int countDown;
	private GamePlayer winner;
	private Runnable startGameTask;
	private int startGameThreadID;
	private int startCountDownThreadID;
	private Runnable startCountDownTask;

	private long gameStartTime;
	
	private Runnable levelUpTask;
	private int levelUpThreadID;
	private MyCloudCraftGame myCloudCraftGame;

	@Override
	public void onEnable() {
		this.mgplugin = (MineGamesPlugin)Bukkit.getPluginManager().getPlugin("MGPlugin");
		getCommand("jogar").setExecutor(new JoinGameCommand(this));

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

	}
	
	public void init(Game game, Arena arena) {
		this.game = game;
		this.arena = arena;
		this.endGameTask = new EndGameTask(this);
		this.levelUpTask = new LevelUpTask(this);
		this.myCloudCraftGame = new MyCloudCraftGame();

		this.gameConfigInstanceList = delegate.findAllGameConfigInstanceByGameUUID(this.game.getGame_uuid().toString());
		this.gameArenaConfigList = delegate.findAllGameConfigArenaByGameArena(this.game.getGame_uuid().toString(), this.arena.getArena_uuid().toString());
		
		// Agendar as threads que v�o detectar se o jogo pode comecar
		this.startCountDownTask = new StartCoundDownTask(this);
		this.startGameTask = new StartGameTask(this);

		BukkitScheduler scheduler = getServer().getScheduler();
		this.startGameThreadID = scheduler.scheduleSyncRepeatingTask(this, this.startGameTask, 0L, 20L);
		this.startCountDownThreadID = scheduler.scheduleSyncRepeatingTask(this, this.startCountDownTask, 0L, 25L);

		this.countDown = (Integer)getGameConfigInstance(Constants.START_COUNTDOWN);
		MGLogger.info(Constants.START_COUNTDOWN + " " + this.countDown );
		if(this.countDown == 0) {
			this.countDown = 10;
		}

	}

	public void startGameEngine() {
		BukkitScheduler scheduler = getServer().getScheduler();
		// Terminar threads de preparacao do jogo
		scheduler.cancelTask(startCountDownThreadID);
		scheduler.cancelTask(startGameThreadID);

		this.endGameThreadID = scheduler.scheduleSyncRepeatingTask(this, this.endGameTask, 0L, 50L);
		this.levelUpThreadID = scheduler.scheduleSyncRepeatingTask(this, this.levelUpTask, 0L, 50L);
	}

	public void endGame() {
		Bukkit.getScheduler().cancelTask(this.endGameThreadID);
		Bukkit.getScheduler().cancelTask(this.levelUpThreadID);

		// remover as bossbars
		removeBossBars();
		// mandar os jogadores de volta para o lobby
		// teleportPlayersBackToLobby();

		// limpar inventario do jogador
		clearPlayersInventory();

		if (!this.myCloudCraftGame.isShuttingDown()) {
			// limpar a arena e reiniciar o plugin
			restart();
		}

	}
	
	
	protected void restart() {
		// zerar lista de players
		this.playerList.clear();
		this.livePlayers.clear();
		this.playerNames.clear();

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

		this.winner = null;

		this.countDown = (Integer)getGameConfigInstance(Constants.START_COUNTDOWN);
		MGLogger.info(Constants.START_COUNTDOWN + " " + this.countDown );
		if(this.countDown == 0) {
			this.countDown = 10;
		}
		BukkitScheduler scheduler = getServer().getScheduler();
		this.startGameThreadID = scheduler.scheduleSyncRepeatingTask(this, this.startGameTask, 0L, 20L);
		this.startCountDownThreadID = scheduler.scheduleSyncRepeatingTask(this, this.startCountDownTask, 0L, 25L);
		
	}

	public CopyOnWriteArraySet<GamePlayer> getLivePlayers() {
		return this.livePlayers;
	}

	public void startArenaBuild(List<ExportBlock> arenaBlocks) {
		
        //criar mundo void para tentar ver se a recriacao da arena fica pronta mais rapidamente
		String worldName = "" + System.currentTimeMillis();
		Bukkit.getLogger().info("cloneWorld void para " + worldName);
		MultiVerseWrapper.cloneWorld(this, "voidworld", worldName );

		Bukkit.getLogger().info("bukkit geWorld");
		this.world = Bukkit.getWorld(worldName);
		
		Bukkit.getLogger().info("bukkit getworld - " + world);
		
		this.setArenaBlocks(arenaBlocks);

		Bukkit.getConsoleSender().sendMessage("&6startArenaBuild: " + arenaBlocks.size() + " blocks");
		this.indexBlock = 0;
		BukkitScheduler scheduler = Bukkit.getScheduler();
		this.buildArenaTask = new BuildArenaTask(this);
		this.threadIds = new CopyOnWriteArrayList<Integer>();
		for(int i = 0; i < 5; i++) {
			Integer _buildArenaThreadID = scheduler.scheduleSyncRepeatingTask(this, this.buildArenaTask, 5L, 200L);
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
				}if(gac.getGameConfig().getConfigType() == GameConfigType.LOCAL) {
					result = gac.getLocalValue();
				}if(gac.getGameConfig().getConfigType() == GameConfigType.AREA3D) {
					
					result = gac.getAreaValue();
				}
			}
		}
		
		return result;
	}

	public abstract boolean shouldEndGame();

	public MyCloudCraftGame getMyCloudCraftGame() {
		return this.myCloudCraftGame;
	}

	public boolean isLastLevel() {
		return false;
	}

	public void levelUp() {
		
	}

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
			Bukkit.getScheduler().cancelTask(startCountDownThreadID);
		}
	}

	public int getCountDown() {
		// TODO Auto-generated method stub
		return countDown;
	}

	protected void start() {
		this.myCloudCraftGame = new MyCloudCraftGame();
		
		MGLogger.info("setting debug level");
		Bukkit.getLogger().setLevel(Level.FINEST);

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

		this.countDown = (Integer)getGameConfigInstance(Constants.START_COUNTDOWN);
		MGLogger.info(Constants.START_COUNTDOWN + " " + this.countDown );
		if(this.countDown == 0) {
			this.countDown = 10;
		}
		
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
		
		Local lobbyLocal = (Local)getGameArenaConfig(Constants.LOBBY_LOCATION);
		MGLogger.info("teleportPlayersBackToLobby - lobby local: " + lobbyLocal);
		Location l = Utils.toLocation(player.getWorld(), lobbyLocal);
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
			gp.getBaseBar().removeAll();
		}
	}

	private void removeBossBar(Player player) {
		GamePlayer gp = findGamePlayerByPlayer(player);
		gp.getBaseBar().removeAll();
	}

	private void removeBossBar(GamePlayer gp) {
		gp.getBaseBar().removeAll();
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
			GamePlayer gp = new GamePlayer();
			gp.setPlayer(player);
			playerList.add(gp);
			livePlayers.add(gp);
			player.sendMessage(Utils.color("&aBem vindo, Arqueiro!"));
			playerNames.add(player.getName());
		} else {
			MGLogger.debug("Jogador j� est� na lista");
		}
	}

	public abstract void killPlayer(Player player);

	public abstract void killEntity(Entity z);

}