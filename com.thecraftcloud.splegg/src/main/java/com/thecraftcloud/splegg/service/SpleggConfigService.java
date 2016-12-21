package com.thecraftcloud.splegg.service;

import java.util.concurrent.CopyOnWriteArraySet;

import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.splegg.GameController;
import com.thecraftcloud.splegg.SpleggConfig;

public class SpleggConfigService {
	private GameController controller;
	private ConfigService configService;
	private static SpleggConfigService me;

	public static SpleggConfigService getInstance() {
		if (me == null) {
			me = new SpleggConfigService();
		}
		return me;
	}

	private SpleggConfigService() {
		this.configService = ConfigService.getInstance();
	}

	public void loadConfig() {

		// Deixa PVP inativo
		this.configService.getArenaWorld().setPVP(true);

		// setar spawn points
		CopyOnWriteArraySet<GameArenaConfig> gacSpawnPoints = this.configService
				.getGameArenaConfigByGroup(SpleggConfig.PLAYER_SPAWN);
		this.configService.getConfig().setSpawnPoints(gacSpawnPoints);

	}

}
