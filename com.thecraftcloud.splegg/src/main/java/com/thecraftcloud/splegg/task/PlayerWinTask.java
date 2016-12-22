package com.thecraftcloud.splegg.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.core.util.title.TitleUtil;
import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.domain.GamePlayer;
import com.thecraftcloud.minigame.domain.MyCloudCraftGame;
import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.splegg.GameController;
import com.thecraftcloud.splegg.service.SpleggPlayerService;

public class PlayerWinTask implements Runnable {

	private TheCraftCloudMiniGameAbstract controller;
	private SpleggPlayerService spleggPlayerService;
	private ConfigService configService = ConfigService.getInstance();

	public PlayerWinTask(GameController game) {
		this.controller = game;
	}

	@Override
	public void run() {

		MyCloudCraftGame game = configService.getMyCloudCraftGame();

		if (!game.isStarted()) {
			return;
		}

		if (this.controller.getLivePlayers().size() == 1) {
			Player winner = null;
			for (GamePlayer gp : this.controller.getLivePlayers()) {
				winner = gp.getPlayer();
			}
			showPlayerWinner(winner);
		}

	}

	public void showPlayerWinner(Player winner) {

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.getWorld().equals(configService.getArenaWorld())) {
				TitleUtil.sendTitle(player.getPlayer(), 1, 70, 10, " " + winner.getName(), " venceu o jogo!");
			}
		}
		this.controller.endGameDelay();
	}

}
