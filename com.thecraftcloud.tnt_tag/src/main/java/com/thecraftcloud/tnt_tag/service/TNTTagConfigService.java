package com.thecraftcloud.tnt_tag.service;

import java.util.concurrent.CopyOnWriteArraySet;

import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.tnt_tag.GameController;
import com.thecraftcloud.tnt_tag.TNTTagConfig;
import com.thecraftcloud.tnt_tag.domain.TNT;
import com.thecraftcloud.tnt_tag.domain.TNTTagPlayer;

public class TNTTagConfigService {

	private GameController controller;
	private TNTService tntService;
	private TNTTagPlayer tntPlayer;
	private TNT tnt;
	
	private TNTTagConfig tntTagConfig = TNTTagConfig.getInstance();
	private ConfigService configService = ConfigService.getInstance();
	
	private static TNTTagConfigService me;
	
	private static Integer tntTimerInSeconds;

	public static TNTTagConfigService getInstance() {
		if (me == null) {
			me = new TNTTagConfigService();
		}
		return me;
	}

	private TNTTagConfigService() {
		this.configService = ConfigService.getInstance();
		this.tntTagConfig = new TNTTagConfig();
		this.tntService = new TNTService(this.controller);
		this.tntPlayer = new TNTTagPlayer(this.controller);
		this.tnt = new TNT();
	}

	public void loadConfig() {
		this.configService.getArenaWorld().setPVP(false);

		Integer tntTimerInSeconds = (Integer) this.configService.getGameConfigInstance(TNTTagConfig.TNT_EXPLODE_TIMER);

		setTntTimerInSeconds(tntTimerInSeconds);

		tnt.setHasTntInGame(false);
		
		// setar spawn points
		CopyOnWriteArraySet<GameArenaConfig> gacSpawnPoints = this.configService
				.getGameArenaConfigByGroup(TNTTagConfig.PLAYER_SPAWN);

	}
	
	public Integer getTntTimerInSeconds() {
		Integer time = (Integer) this.configService.getGameConfigInstance(TNTTagConfig.TNT_EXPLODE_TIMER);
		this.tntTimerInSeconds = time;
		return tntTimerInSeconds;
	}

	public void setTntTimerInSeconds(Integer tntTimerInSeconds) {
		this.tntTimerInSeconds = tntTimerInSeconds;
	}
	
	public void resetTime() {
		Integer tntTimerInSeconds = (Integer) this.configService.getGameConfigInstance(TNTTagConfig.TNT_EXPLODE_TIMER);
		setTntTimerInSeconds(tntTimerInSeconds);
	}

}
