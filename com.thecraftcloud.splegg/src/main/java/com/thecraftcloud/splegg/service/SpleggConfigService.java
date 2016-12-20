package com.thecraftcloud.splegg.service;

import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.splegg.GameController;

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
		// this.delegate = TheCraftCloudDelegate.getInstance();
	}

	public void loadConfig() {

		// Deixa PVP ativo
		this.configService.getArenaWorld().setPVP(false);

		/*
		 * // setar spawn points CopyOnWriteArraySet<GameArenaConfig>
		 * gacSpawnPoints = this.configService
		 * .getGameArenaConfigByGroup(GunGameConfig.PLAYER_SPAWN);
		 * this.configService.getConfig().setSpawnPoints(gacSpawnPoints);
		 * 
		 */
	}


}
