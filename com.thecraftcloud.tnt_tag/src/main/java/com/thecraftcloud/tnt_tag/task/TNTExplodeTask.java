package com.thecraftcloud.tnt_tag.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.minigame.domain.GamePlayer;
import com.thecraftcloud.minigame.domain.MyCloudCraftGame;
import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.tnt_tag.GameController;
import com.thecraftcloud.tnt_tag.TNTTagConfig;
import com.thecraftcloud.tnt_tag.domain.TNT;
import com.thecraftcloud.tnt_tag.service.TNTTagPlayerService;

public class TNTExplodeTask implements Runnable {

	private ConfigService configService = ConfigService.getInstance();
	private TNT tnt;
	private TNTTagConfig tntTagConfig;
	private TNTTagPlayerService tntTagPlayerService;
	private GameController controller;

	public TNTExplodeTask(GameController controller) {
		this.controller = controller;
		this.tnt = new TNT();
		this.tntTagPlayerService = new TNTTagPlayerService(controller);
	}

	@Override
	public void run() {
		
		Bukkit.getConsoleSender().sendMessage(Utils.color("&6 TNT EXPLODE TASK"));

		MyCloudCraftGame game = configService.getMyCloudCraftGame();
		if (!game.isStarted())
			return;

		
		if (!tnt.getHasTntInGame()) {
			Bukkit.getConsoleSender().sendMessage(Utils.color("&6 NÃO TEM BOMBA!!!"));
			return;
		}
			
		if (controller.shouldExplodeTnt()) {

			Bukkit.getConsoleSender().sendMessage(Utils.color("&6 EXPLODIR BOMBA!!"));

			String playerName = tnt.getTntHolder().getName();

			for (GamePlayer gp : controller.getLivePlayers()) {
				Player player = gp.getPlayer();
				if (player.getName().equals(playerName)) {
					tntTagPlayerService.killPlayer(player);
					tnt.setHasTntInGame(false);
				}
			}

		}

	}

}
