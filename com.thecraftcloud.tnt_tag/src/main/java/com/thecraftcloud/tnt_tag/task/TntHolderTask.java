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
import com.thecraftcloud.tnt_tag.TntTagConfig;
import com.thecraftcloud.tnt_tag.domain.TNT;
import com.thecraftcloud.tnt_tag.service.TNTService;
import com.thecraftcloud.tnt_tag.service.TntTagPlayerService;

public class TntHolderTask implements Runnable {
	
	private ConfigService configService = ConfigService.getInstance();
	private TntTagConfig tntTagConfig = TntTagConfig.getInstance();
	private TNTService tntService;
	private GameController controller;
	private TNT tnt;
	private TntTagPlayerService tntTagPlayerService;

	private List<Player> players;
	
	public TntHolderTask(GameController controller) {
		this.controller = controller;
		this.tntService = new TNTService(controller);
		this.tnt = new TNT();
	}

	@Override
	public void run() {
		
		MyCloudCraftGame game = configService.getMyCloudCraftGame();
		if (!game.isStarted()) return;
		
		if (tnt.getHasTntInGame()) return;
		
		players = new ArrayList<Player>();
		
		for(GamePlayer gp : controller.getLivePlayers()) {
			players.add(gp.getPlayer());
		}
		
		Random r = new Random();
		int randomPlayer = r.nextInt(players.size());
		
		Bukkit.getConsoleSender().sendMessage(Utils.color("&6 PLAYER SORTEADO: " + players.get(randomPlayer).getName()));

		tnt.setTntHolder(players.get(randomPlayer));
		
		Integer time = (int) (System.currentTimeMillis() / 1000);
		tntService.setTntStartTimeInSeconds(time);
	}

}
