package com.thecraftcloud.minigame.task;

import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.service.ConfigService;

public class SpawnFireworksTask implements Runnable {

	private TheCraftCloudMiniGameAbstract controller;
	private ConfigService configService = ConfigService.getInstance();
	
	public SpawnFireworksTask(TheCraftCloudMiniGameAbstract controller) {
		this.controller = controller;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
