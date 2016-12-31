package com.thecraftcloud.tnt_tag.task;

import org.bukkit.entity.Player;

import com.thecraftcloud.minigame.domain.GamePlayer;
import com.thecraftcloud.minigame.domain.MyCloudCraftGame;
import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.tnt_tag.GameController;
import com.thecraftcloud.tnt_tag.domain.TNT;
import com.thecraftcloud.tnt_tag.service.TNTTagPlayerService;

import net.md_5.bungee.api.ChatColor;

public class TNTExplodeTask implements Runnable {

	private ConfigService configService = ConfigService.getInstance();
	private TNT tnt;
	private TNTTagPlayerService tntPlayerService;
	private GameController controller;

	public TNTExplodeTask(GameController controller) {
		this.controller = controller;
		this.tnt = new TNT();
		this.tntPlayerService = new TNTTagPlayerService(controller);
	}

	@Override
	public void run() {

		MyCloudCraftGame game = configService.getMyCloudCraftGame();
		if (!game.isStarted())
			return;

		if (!tnt.hasTntInGame())
			return;

		if (controller.isGameInBreak())
			return;

		if (controller.shouldExplodeTnt()) {
			
			String playerName = tnt.getTntHolder().getName();

			for (GamePlayer gp : controller.getLivePlayers()) {
				Player player = gp.getPlayer();
				player.sendMessage(ChatColor.GRAY + "[TNT_TAG] " + ChatColor.WHITE + tnt.getTntHolder().getName() + ChatColor.RED + " explodiu!" );
				if (player.getName().equals(playerName)) {
					tntPlayerService.killPlayer(player);
					tnt.setHasTntInGame(false);
				}
			}
			
			controller.gameBreak();

		}

	}

}
