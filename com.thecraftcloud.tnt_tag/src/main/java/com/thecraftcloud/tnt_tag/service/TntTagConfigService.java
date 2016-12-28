package com.thecraftcloud.tnt_tag.service;

import java.util.concurrent.CopyOnWriteArraySet;

import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.tnt_tag.GameController;
import com.thecraftcloud.tnt_tag.TntTagConfig;

public class TntTagConfigService {
	private GameController controller;
	private ConfigService configService;
	private static TntTagConfigService me;

	public static TntTagConfigService getInstance() {
		if (me == null) {
			me = new TntTagConfigService();
		}
		return me;
	}

	private TntTagConfigService() {
		this.configService = ConfigService.getInstance();
	}

	public void loadConfig() {

		// Deixa PVP inativo
		this.configService.getArenaWorld().setPVP(true);

		// setar spawn points
		CopyOnWriteArraySet<GameArenaConfig> gacSpawnPoints = this.configService
				.getGameArenaConfigByGroup(TntTagConfig.PLAYER_SPAWN);
		this.configService.getConfig().setSpawnPoints(gacSpawnPoints);

	}

}
