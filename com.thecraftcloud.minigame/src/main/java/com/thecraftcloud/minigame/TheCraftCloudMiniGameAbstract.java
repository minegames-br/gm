package com.thecraftcloud.minigame;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

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

import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.domain.GameConfigInstance;
import com.thecraftcloud.core.domain.GameInstance;
import com.thecraftcloud.core.domain.Local;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.export.ExportBlock;
import com.thecraftcloud.core.util.LocationUtil;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.minigame.command.JoinGameCommand;
import com.thecraftcloud.minigame.command.LeaveGameCommand;
import com.thecraftcloud.minigame.domain.EntityPlayer;
import com.thecraftcloud.minigame.domain.GamePlayer;
import com.thecraftcloud.minigame.domain.MyCloudCraftGame;
import com.thecraftcloud.minigame.listener.PlayerQuit;
import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.minigame.service.PlayerService;
import com.thecraftcloud.minigame.task.EndGameTask;
import com.thecraftcloud.minigame.task.LevelUpTask;
import com.thecraftcloud.minigame.task.StartCoundDownTask;
import com.thecraftcloud.minigame.task.StartGameTask;


public abstract class TheCraftCloudMiniGameAbstract extends JavaPlugin {
	
	protected CopyOnWriteArraySet<GamePlayer> playerList = new CopyOnWriteArraySet<GamePlayer>();
	protected CopyOnWriteArraySet<GamePlayer> livePlayers = new CopyOnWriteArraySet<GamePlayer>();
	protected CopyOnWriteArraySet<String> playerNames = new CopyOnWriteArraySet<String>();
	protected CopyOnWriteArraySet<EntityPlayer> livingEntities = new CopyOnWriteArraySet<EntityPlayer>();

	private Scoreboard scoreboard;

	protected Integer maxPlayers;
	protected Integer minPlayers;

	private int endGameThreadID;
	private Runnable endGameTask;


	private int startGameThreadID;
	private StartGameTask startGameTask;

	protected int countDown;
	private int startCountDownThreadID;
	private Runnable startCountDownTask;

	private long gameStartTime;
	
	private int levelUpThreadID;
	private Runnable levelUpTask;

	protected LocationUtil locationUtil = new LocationUtil();
	protected PlayerService playerService = new PlayerService(this);
	protected ConfigService configService;
	
	@Override
	public void onEnable() {
		this.configService = ConfigService.getInstance();
		
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

	}
	
	public void init() {
		BukkitScheduler scheduler = getServer().getScheduler();

		this.endGameTask = new EndGameTask(this);
		this.levelUpTask = new LevelUpTask(this);

		// Agendar as threads que vão detectar se o jogo pode comecar
		this.startCountDownTask = new StartCoundDownTask(this);
		this.startGameTask = new StartGameTask(this);

		this.startGameThreadID = scheduler.scheduleSyncRepeatingTask(this, this.startGameTask, 0L, 20L);
		this.startCountDownThreadID = scheduler.scheduleSyncRepeatingTask(this, this.startCountDownTask, 0L, 25L);

		this.configService.setStartCountDown();
	}

	public void startGameEngine() {
		
		BukkitScheduler scheduler = getServer().getScheduler();
		
		// Terminar threads de preparacao do jogo
		scheduler.cancelTask(startCountDownThreadID);
		scheduler.cancelTask(startGameThreadID);

		this.endGameThreadID = scheduler.scheduleSyncRepeatingTask(this, this.endGameTask, 0L, 50L);
		this.levelUpThreadID = scheduler.scheduleSyncRepeatingTask(this, this.levelUpTask, 0L, 50L);
		this.start();
		this.configService.getMyCloudCraftGame().start();
		
		//mudar horário da arena
		Integer time = this.configService.getArena().getTime();
		if( time != null) {
			this.configService.getWorld().setTime(time);
		}
	}

	public void endGame() {
		Bukkit.getScheduler().cancelTask(this.endGameThreadID);
		Bukkit.getScheduler().cancelTask(this.levelUpThreadID);

		// remover as bossbars
		removeBossBars();

		// limpar inventario do jogador
		clearPlayersInventory();

	}
	
	public CopyOnWriteArraySet<GamePlayer> getLivePlayers() {
		return this.livePlayers;
	}


	public abstract boolean shouldEndGame();

	public abstract boolean isLastLevel();

	public abstract void levelUp();

	public void startCoundDown() {
		this.countDown = (Integer)configService.getGameConfigInstance(TheCraftCloudConfig.START_COUNTDOWN);
		this.configService.getMyCloudCraftGame().startCountDown();
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
			}
			removeBossBar(gp);
			livePlayers.remove(gp);
		}

		if (livePlayers.size() == 0) {
			this.configService.getMyCloudCraftGame().endGame();
			this.endGame();
		}
	}
	
	protected void registerListeners() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerQuit(this), this);
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

	public void addEntityPlayer(EntityPlayer entityPlayer) {
		livingEntities.add(entityPlayer);
	}

	public CopyOnWriteArraySet<EntityPlayer> getLivingEntities() {
		return this.livingEntities;
	}

	public ConfigService getConfigService() {
		return this.configService;
	}

	public abstract MyCloudCraftGame createMyCloudCraftGame();
}
