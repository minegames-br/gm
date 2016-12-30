package com.thecraftcloud.tnt_tag;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;

import com.thecraftcloud.core.logging.MGLogger;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.minigame.TheCraftCloudConfig;
import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.domain.GamePlayer;
import com.thecraftcloud.minigame.domain.MyCloudCraftGame;
import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.minigame.service.PlayerService;
import com.thecraftcloud.tnt_tag.domain.TNT;
import com.thecraftcloud.tnt_tag.domain.TntTag;
import com.thecraftcloud.tnt_tag.domain.TntTagPlayer;
import com.thecraftcloud.tnt_tag.listener.CancelEvents;
import com.thecraftcloud.tnt_tag.listener.TNTExplosionListener;
import com.thecraftcloud.tnt_tag.service.TNTService;
import com.thecraftcloud.tnt_tag.service.TNTTagConfigService;
import com.thecraftcloud.tnt_tag.service.TNTTagPlayerService;
import com.thecraftcloud.tnt_tag.task.PlayerWinTask;
import com.thecraftcloud.tnt_tag.task.TNTExplodeTask;
import com.thecraftcloud.tnt_tag.task.TNTHolderTask;

/**
 * Created by renatocsare@gmail.com on Dez 28, 2016
 */
public class GameController extends TheCraftCloudMiniGameAbstract {

	private Integer gameDuration;
	private Integer tntTimer;

	private TNTTagPlayerService tntTagPlayerService = new TNTTagPlayerService(this);
	private TNTTagConfigService tntTagConfigService = TNTTagConfigService.getInstance();
	private TNTService tntService;

	private TNTTagConfig tntTagConfig = TNTTagConfig.getInstance();

	//private TNT tnt;

	private Runnable tntExplodeTask;
	private int tntExplodeTaskThreadID;
	private Runnable tntHolderTask;
	private int tntHolderTaskThreadID;

	private Runnable playerWinTask;
	private int playerWinTaskThreadID;

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

		tntTagPlayerService.setupPlayersToStartGame();

		// Enviar jogadores para a Arena
		tntTagPlayerService.teleportPlayersToArena();

		// registar listeners especificos
		this.registerListeners();

		// Iniciar threads do jogo
		BukkitScheduler scheduler = getServer().getScheduler();
		this.playerWinTaskThreadID = scheduler.scheduleSyncRepeatingTask(this, this.playerWinTask, 0L, 20L);
		this.tntExplodeTaskThreadID = scheduler.scheduleSyncRepeatingTask(this, this.tntExplodeTask, 20L, 20L);
		this.tntHolderTaskThreadID = scheduler.scheduleSyncRepeatingTask(this, this.tntHolderTask, 0L, 20L);

		// Bukkit.getConsoleSender().sendMessage(Utils.color("&6 GET TNT
		// DURATION" + tntService.getTntDuration()));

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
		Bukkit.getConsoleSender().sendMessage(Utils.color("&6 SHOULD EXPLODE TNT"));
		Bukkit.getConsoleSender()
				.sendMessage(Utils.color("&6 SHOULD EXPLODE TNT - GET TNT DURATION: " + tntService.getTntDuration()));
		Bukkit.getConsoleSender().sendMessage(Utils.color(
				"&6 SHOULD EXPLODE TNT - GET TNT TIMER IN SECONDS: " + tntTagConfigService.getTntTimerInSeconds()));
		// se a duração da bomba for maior que o tempo limite
		if (tntService.getTntDuration() > tntTagConfigService.getTntTimerInSeconds()) {
			Bukkit.getConsoleSender().sendMessage(Utils.color("&6 TEMPO ESGOTADOOOOO"));
			return true;
		}

		return false;
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

	/**
	 * Quando esse método executar, o jogo terá terminado com um vencedor e/ou o
	 * tempo terá acabado.
	 */
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
		return new TntTagPlayer();
	}

	@Override
	protected void registerListeners() {
		super.registerListeners();
		PluginManager pm = Bukkit.getPluginManager();
		//pm.registerEvents(new TntExplosionListener(this), this);
		pm.registerEvents(new CancelEvents(this), this);
	}

	@Override
	public MyCloudCraftGame createMyCloudCraftGame() {
		return new TntTag();
	}

	@Override
	public boolean isLastLevel() {
		// TODO Auto-generated method stub
		return false;
	}

}
