package com.thecraftcloud.tnt_tag;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;

import com.thecraftcloud.core.logging.MGLogger;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.core.util.title.TitleUtil;
import com.thecraftcloud.minigame.TheCraftCloudConfig;
import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.domain.GamePlayer;
import com.thecraftcloud.minigame.domain.MyCloudCraftGame;
import com.thecraftcloud.minigame.service.PlayerService;
import com.thecraftcloud.tnt_tag.domain.TNTTag;
import com.thecraftcloud.tnt_tag.domain.TNTTagPlayer;
import com.thecraftcloud.tnt_tag.listener.CancelEvents;
import com.thecraftcloud.tnt_tag.listener.EntityHitEvent;
import com.thecraftcloud.tnt_tag.service.TNTService;
import com.thecraftcloud.tnt_tag.service.TNTTagConfigService;
import com.thecraftcloud.tnt_tag.service.TNTTagPlayerService;
import com.thecraftcloud.tnt_tag.task.PlayerWinTask;
import com.thecraftcloud.tnt_tag.task.TNTExplodeTask;
import com.thecraftcloud.tnt_tag.task.TNTHolderTask;

import net.md_5.bungee.api.ChatColor;

/**
 * Created by renatocsare@gmail.com on Dez 28, 2016
 */
public class GameController extends TheCraftCloudMiniGameAbstract {

	private TNTTagPlayerService tntPlayerService = new TNTTagPlayerService(this);
	private TNTTagConfigService tntConfigService = TNTTagConfigService.getInstance();
	private TNTService tntService;
	
	private Runnable tntExplodeTask;
	private int tntExplodeTaskThreadID;
	private Runnable tntHolderTask;
	private int tntHolderTaskThreadID;
	private Runnable playerWinTask;
	private int playerWinTaskThreadID;
	
	private Integer gameDuration;
	private boolean gameInBreak;

	public GameController() {
		this.tntService = new TNTService(this);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void startGameEngine() {
		super.startGameEngine();

		tntPlayerService.setupPlayersToStartGame();

		// Enviar jogadores para a Arena
		tntPlayerService.teleportPlayersToArena();

		// registar listeners especificos
		this.registerListeners();

		// Iniciar threads do jogo
		BukkitScheduler scheduler = getServer().getScheduler();
		this.playerWinTaskThreadID = scheduler.scheduleSyncRepeatingTask(this, this.playerWinTask, 0L, 20L);
		this.tntExplodeTaskThreadID = scheduler.scheduleSyncRepeatingTask(this, this.tntExplodeTask, 20L, 20L);
		this.tntHolderTaskThreadID = scheduler.scheduleSyncRepeatingTask(this, this.tntHolderTask, 0L, 20L);
	}

	@Override
	public void init() {
		super.init();

		// inicializar variaveis de instancia
		this.playerWinTask = new PlayerWinTask(this);
		this.tntExplodeTask = new TNTExplodeTask(this);
		this.tntHolderTask = new TNTHolderTask(this);

		// Carregar configuracoes especificas do TNT Tag
		TNTTagConfigService.getInstance().loadConfig();
	}

	@Override
	public void startThreads(BukkitScheduler scheduler) {
		this.updateScoreBoardThreadID = scheduler.scheduleSyncRepeatingTask(this, this.updateScoreBoardTask, 20L, 20L);
	}

	public boolean shouldExplodeTnt() {
		if (tntService.getTntDuration() > tntConfigService.getTntTimerInSeconds()) {
			return true;
		}
		return false;
	}
	
	public void gameBreak() {
		
		setGameInBreak(true);
		
		for (GamePlayer gp : this.getLivePlayers()) {
			Player player = gp.getPlayer();
			player.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.WHITE + " continua no jogo!");
		}
		BukkitScheduler scheduler = this.getServer().getScheduler();
		scheduler.scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				setGameInBreak(false);
				TNTTagConfigService.getInstance().resetTime();
				
			}
		}, 120L);
	}

	@Override
	public boolean shouldEndGame() {
		// Terminar o jogo caso não tenha mais jogadores
		if (this.getLivePlayers().size() == 0 && this.configService.getMyCloudCraftGame().isStarted()) {
			Bukkit.getConsoleSender().sendMessage(Utils.color("&6EndGameTask - No more players"));
			return true;
		}

		long currentTime = System.currentTimeMillis();
		long duration = (currentTime - this.configService.getMyCloudCraftGame().getGameStartTime()) / 1000;
		this.gameDuration = (Integer) this.configService
				.getGameConfigInstance(TheCraftCloudConfig.GAME_DURATION_IN_SECONDS);

		if (duration >= this.gameDuration && this.getLivePlayers().size() > 1) {
			//
		}
		return false;
	}

	@Override
	public void endGame() {
		super.endGame();
		if (this.configService.getMyCloudCraftGame().isStarted()) {
			this.configService.getMyCloudCraftGame().endGame();
		}
		MGLogger.info("Game.endGame");

		// Terminar threads do jogo
		Bukkit.getScheduler().cancelTask(this.playerWinTaskThreadID);
		Bukkit.getScheduler().cancelTask(this.tntExplodeTaskThreadID);
		Bukkit.getScheduler().cancelTask(this.tntHolderTaskThreadID);

		for (GamePlayer gp : livePlayers) {
			Player player = gp.getPlayer();
			player.getInventory().clear();
			player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
			player.sendMessage("Você fez " + gp.getPoint() + " pontos.");
		}

	}

	@Override
	public PlayerService createPlayerService() {
		return new TNTTagPlayerService(this);
	}

	@Override
	public void levelUp() {
		return;
	}

	@Override
	public GamePlayer createGamePlayer() {
		return new TNTTagPlayer();
	}

	@Override
	protected void registerListeners() {
		super.registerListeners();
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new EntityHitEvent(this), this);
		pm.registerEvents(new CancelEvents(this), this);
	}

	@Override
	public MyCloudCraftGame createMyCloudCraftGame() {
		return new TNTTag();
	}

	@Override
	public boolean isLastLevel() {
		return false;
	}

	public boolean isGameInBreak() {
		return gameInBreak;
	}

	public void setGameInBreak(boolean gameInBreak) {
		this.gameInBreak = gameInBreak;
	}

}
