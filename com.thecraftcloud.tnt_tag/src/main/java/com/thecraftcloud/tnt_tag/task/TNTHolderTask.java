package com.thecraftcloud.tnt_tag.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.minigame.domain.GamePlayer;
import com.thecraftcloud.minigame.domain.MyCloudCraftGame;
import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.tnt_tag.GameController;
import com.thecraftcloud.tnt_tag.TNTTagConfig;
import com.thecraftcloud.tnt_tag.domain.TNT;
import com.thecraftcloud.tnt_tag.service.TNTService;
import com.thecraftcloud.tnt_tag.service.TNTTagPlayerService;

public class TNTHolderTask implements Runnable {

	private ConfigService configService = ConfigService.getInstance();
	private TNTService tntService;
	private GameController controller;
	private TNT tnt;

	private List<Player> players;

	public TNTHolderTask(GameController controller) {
		this.controller = controller;
		this.tntService = new TNTService(controller);
		this.tnt = new TNT();
	}

	@Override
	public void run() {

		MyCloudCraftGame game = configService.getMyCloudCraftGame();
		if (!game.isStarted())
			return;

		if (tnt.hasTntInGame() || this.controller.getLivePlayers().size() == 1)
			return;

		if (controller.isGameInBreak())
			return;

		players = new ArrayList<Player>();

		for (GamePlayer gp : controller.getLivePlayers()) {
			players.add(gp.getPlayer());
		}

		Random r = new Random();
		int randomPlayer = r.nextInt(players.size());

		tnt.setTntHolder(players.get(randomPlayer));

		Integer time = (int) (System.currentTimeMillis() / 1000);
		tntService.setTntStartTimeInSeconds(time);
	}

}
