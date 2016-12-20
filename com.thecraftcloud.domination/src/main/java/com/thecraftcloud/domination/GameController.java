package com.thecraftcloud.domination;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.plugin.PluginManager;

import com.snowgears.arathibasin.ArathiBasin;
import com.thecraftcloud.core.logging.MGLogger;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.domination.domain.Domination;
import com.thecraftcloud.domination.domain.DominationPlayer;
import com.thecraftcloud.domination.service.DominationConfigService;
import com.thecraftcloud.domination.service.DominationPlayerService;
import com.thecraftcloud.minigame.TheCraftCloudConfig;
import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.domain.GamePlayer;
import com.thecraftcloud.minigame.domain.MyCloudCraftGame;

/**
 * Created by joaoemilio@gmail.com on Dec 20, 2016
 */
public class GameController extends TheCraftCloudMiniGameAbstract {
	private Integer gameDuration;
	private DominationPlayerService dominationPlayerService = new DominationPlayerService(this);
	private DominationConfigService dominationConfigService = DominationConfigService.getInstance();

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void startGameEngine() {
		configService.getMyCloudCraftGame().setGameStartTime(System.currentTimeMillis());
		super.startGameEngine();
		ArathiBasin abPlugin = (ArathiBasin)Bukkit.getPluginManager().getPlugin("ArathiBasin");
		for(GamePlayer gp: this.getLivePlayers()) {
			abPlugin.getArathiGame().addPlayer(gp.getPlayer(), null);
		}
		abPlugin.getArathiGame().startGame();
	}

	@Override
	public void init() {
		super.init();

		// Carregar configuracoes especificas do Game
		DominationConfigService.getInstance().loadConfig();
	}

	@Override
	public boolean shouldEndGame() {
		// Terminar o jogo caso não tenha mais jogadores
		if (this.getLivePlayers().size() == 0 && this.configService.getMyCloudCraftGame().isStarted()) {
			Bukkit.getConsoleSender().sendMessage(Utils.color("&6EndGameTask - No more players"));
			return true;
		}

		// Terminar o jogo caso tenha alcançado o limite de tempo
		long currentTime = System.currentTimeMillis();
		long duration = (currentTime - this.configService.getMyCloudCraftGame().getGameStartTime()) / 1000;
		this.gameDuration = (Integer) this.configService
				.getGameConfigInstance(TheCraftCloudConfig.GAME_DURATION_IN_SECONDS);

		if (duration >= this.gameDuration) {
			Bukkit.getConsoleSender()
					.sendMessage(Utils.color("&6EndGameTask - TimeOver: " + duration + " > " + this.gameDuration));
			return true;
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

	}

	@Override
	public boolean isLastLevel() {
		return false;
	}

	@Override
	public void levelUp() {
		return;
	}

	@Override
	public GamePlayer createGamePlayer() {
		return new DominationPlayer();
	}

	@Override
	protected void registerListeners() {
		super.registerListeners();
		PluginManager pm = Bukkit.getPluginManager();
	}

	@Override
	public MyCloudCraftGame createMyCloudCraftGame() {
		return new Domination();
	}

}
