package com.thecraftcloud.tnt_tag;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import com.thecraftcloud.tnt_tag.domain.TntTag;
import com.thecraftcloud.tnt_tag.domain.TntTagPlayer;
import com.thecraftcloud.tnt_tag.listener.CancelEvents;
import com.thecraftcloud.tnt_tag.listener.PlayerDrop;
import com.thecraftcloud.tnt_tag.service.TntTagConfigService;
import com.thecraftcloud.tnt_tag.service.TntTagPlayerService;
import com.thecraftcloud.tnt_tag.task.PlayerWinTask;

/**
 * Created by renatocsare@gmail.com on Dez 28, 2016
 */
public class GameController extends TheCraftCloudMiniGameAbstract {
	private Integer gameDuration;
	private TntTagPlayerService spleggPlayerService = new TntTagPlayerService(this);
	private TntTagConfigService spleggConfigService = TntTagConfigService.getInstance();

	private Runnable playerWinTask;
	private int playerWinTaskThreadID;
	protected Runnable destroyArenaBlocksTask;
	protected int destroyArenaBlocksTaskThreadID;
	private boolean autoDestruction;

	
	
	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void startGameEngine() {
		super.startGameEngine();
	
		spleggPlayerService.setupPlayersToStartGame();

		// Enviar jogadores para a Arena
		spleggPlayerService.teleportPlayersToArena();

		// registar listeners especificos
		this.registerListeners();

		// Iniciar threads do jogo
		BukkitScheduler scheduler = getServer().getScheduler();
		this.playerWinTaskThreadID = scheduler.scheduleSyncRepeatingTask(this, this.playerWinTask, 0L, 20L);
		
		autoDestruction = false;
	}

	@Override
	public void init() {
		super.init();

		// inicializar variaveis de instancia
		this.playerWinTask = new PlayerWinTask(this);

		// Carregar configuracoes especificas do Splegg
		TntTagConfigService.getInstance().loadConfig();
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
			
			if(!autoDestruction) {
				autoDestructionAlert();
			}

			// chama uma task para derrubar toda a arena
			BukkitScheduler scheduler = getServer().getScheduler();
			this.destroyArenaBlocksTaskThreadID = scheduler.scheduleSyncRepeatingTask(this, this.destroyArenaBlocksTask,
					0L, 10L);
		}
		return false;
	}

	private void autoDestructionAlert() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.getWorld().equals(configService.getArenaWorld())) {
				TitleUtil.sendTitle(player.getPlayer(), 1, 90, 10, ChatColor.RED + "Autodestruição!",
						ChatColor.RED + "salve-se quem puder...");
				autoDestruction = true;
			}
		}
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
		Bukkit.getScheduler().cancelTask(this.destroyArenaBlocksTaskThreadID);


		for (GamePlayer gp : livePlayers) {
			Player player = gp.getPlayer();
			player.getInventory().clear();
			player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
			player.sendMessage("Você fez " + gp.getPoint() + " pontos.");
		}

	}

	@Override
	public PlayerService createPlayerService() {
		return new TntTagPlayerService(this);
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
		pm.registerEvents(new PlayerDrop(this), this);
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

	public TntTagConfigService getSpleggConfigService() {
		return spleggConfigService;
	}

	public void setSpleggConfigService(TntTagConfigService spleggConfigService) {
		this.spleggConfigService = spleggConfigService;
	}
}
