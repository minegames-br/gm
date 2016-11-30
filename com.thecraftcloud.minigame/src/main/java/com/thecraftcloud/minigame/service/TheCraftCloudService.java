package com.thecraftcloud.minigame.service;

import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;

public class TheCraftCloudService {

	protected TheCraftCloudMiniGameAbstract miniGame;
	protected ConfigService configService;
	
	public TheCraftCloudService(TheCraftCloudMiniGameAbstract miniGame) {
		this.miniGame = miniGame;
		this.configService = miniGame.getConfigService();
	}
	
}
