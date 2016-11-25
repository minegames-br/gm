package com.thecraftcloud.plugin.service;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;

public class TheCraftCloudService {

	protected TheCraftCloudDelegate delegate;
	protected TheCraftCloudMiniGameAbstract miniGame;
	protected ConfigService configService = ConfigService.getInstance();
	
	public TheCraftCloudService(TheCraftCloudMiniGameAbstract miniGame) {
		this.delegate = TheCraftCloudDelegate.getInstance();
		this.miniGame = miniGame;
	}
	
}
