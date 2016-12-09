package com.thecraftcloud.gungame;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;

import com.thecraftcloud.core.logging.MGLogger;
import com.thecraftcloud.core.util.BlockManipulationUtil;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.gungame.domain.GunGame;
import com.thecraftcloud.gungame.domain.GunGamePlayer;
import com.thecraftcloud.gungame.listener.PlayerDeath;
import com.thecraftcloud.gungame.service.GunGameConfigService;
import com.thecraftcloud.gungame.service.GunGamePlayerService;
import com.thecraftcloud.gungame.task.SpawnBonusItemTask;
import com.thecraftcloud.minigame.TheCraftCloudConfig;
import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.domain.GamePlayer;
import com.thecraftcloud.minigame.domain.MyCloudCraftGame;

/**
 * Created by joaoemilio@gmail.com on Nov 26, 2016
 */
public class GameController extends TheCraftCloudMiniGameAbstract {
	private Integer gameDuration;
	private Integer spawnBonusItemThreadID;
	private SpawnBonusItemTask spawnBonusItemTask;
	private GunGamePlayerService gunGamePlayerService = new GunGamePlayerService(this);
	private GunGameConfigService gunGameConfigService = GunGameConfigService.getInstance();

	@Override
	public void onEnable() {
		super.onEnable();

		spawnBonusItemTask = new SpawnBonusItemTask(this);
	}

	@Override
	public void startGameEngine() {
		super.startGameEngine();

		gunGamePlayerService.setupPlayersToStartGame();

		// Enviar jogadores para a Arena
		gunGamePlayerService.teleportPlayersToArena();

		// cria lista de itens
		this.gunGameConfigService.createItemList();

		// cria lista para sortear os itens
		this.gunGameConfigService.createPrizeList(this.getLivePlayers());

		// Iniciar threads do jogo
		BukkitScheduler scheduler = getServer().getScheduler();
		this.spawnBonusItemThreadID = scheduler.scheduleSyncRepeatingTask(this, this.spawnBonusItemTask, 200L, 250L);

		// varrer o mapa para encontrar Chests
		List<Chest> chestList = new BlockManipulationUtil().getArenaChests(
				Bukkit.getWorld(this.configService.getArena().getName()), this.configService.getArena());
		this.gunGameConfigService.setChestList(chestList);
	}

	@Override
	public void init() {
		super.init();

		// Carregar configuracoes especificas do Gun Game
		GunGameConfigService.getInstance().loadConfig();
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

		// Terminar threads do jogo
		Bukkit.getScheduler().cancelTask(this.spawnBonusItemThreadID);

		for (GamePlayer gp : livePlayers) {
			Player player = gp.getPlayer();
			player.getInventory().clear();
			player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
			player.sendMessage("Você fez " + gp.getPoint() + " pontos.");
		}

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
		return new GunGamePlayer();
	}

	@Override
	protected void registerListeners() {
		super.registerListeners();
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerDeath(this), this);
	}

	@Override
	public MyCloudCraftGame createMyCloudCraftGame() {
		return new GunGame();
	}

}
