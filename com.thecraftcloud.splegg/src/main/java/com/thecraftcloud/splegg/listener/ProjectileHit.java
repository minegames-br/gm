package com.thecraftcloud.splegg.listener;

import org.bukkit.event.Listener;

import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.splegg.GameController;

public class ProjectileHit implements Listener {

	private GameController controller;
	private ConfigService configService = ConfigService.getInstance();
	private ThrowEgg eggList;

	public ProjectileHit(GameController controller) {
		super();
		this.controller = controller;
	}

	
}
