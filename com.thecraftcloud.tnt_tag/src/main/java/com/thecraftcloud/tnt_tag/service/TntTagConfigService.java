package com.thecraftcloud.tnt_tag.service;

import java.util.concurrent.CopyOnWriteArraySet;

import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.tnt_tag.GameController;
import com.thecraftcloud.tnt_tag.TntTagConfig;
import com.thecraftcloud.tnt_tag.domain.TNT;

public class TntTagConfigService {

	private GameController controller;
	private TNTService tntService;
	private TNT tnt;
	
	private TntTagConfig tntTagConfig = TntTagConfig.getInstance();
	private ConfigService configService = ConfigService.getInstance();
	
	private static TntTagConfigService me;
	
	private static Integer tntTimerInSeconds;

	public static TntTagConfigService getInstance() {
		if (me == null) {
			me = new TntTagConfigService();
		}
		return me;
	}

	private TntTagConfigService() {
		this.configService = ConfigService.getInstance();
		this.tntTagConfig = new TntTagConfig();
		this.tntService = new TNTService(this.controller);
		this.tnt = new TNT();
	}

	public void loadConfig() {

		// Deixa PVP ativo (false)
		this.configService.getArenaWorld().setPVP(false);

		Integer tntTimerInSeconds = (Integer) this.configService.getGameConfigInstance(TntTagConfig.TNT_EXPLODE_TIMER);

		this.setTntTimerInSeconds(tntTimerInSeconds);

		tnt.setHasTntInGame(false);

		// setar spawn points
		CopyOnWriteArraySet<GameArenaConfig> gacSpawnPoints = this.configService
				.getGameArenaConfigByGroup(TntTagConfig.PLAYER_SPAWN);

	}
	
	public Integer getTntTimerInSeconds() {
		Integer time = (Integer) this.configService.getGameConfigInstance(TntTagConfig.TNT_EXPLODE_TIMER);
		this.tntTimerInSeconds = time;
		return tntTimerInSeconds;
	}

	public void setTntTimerInSeconds(Integer tntTimerInSeconds) {
		this.tntTimerInSeconds = tntTimerInSeconds;
	}

}
