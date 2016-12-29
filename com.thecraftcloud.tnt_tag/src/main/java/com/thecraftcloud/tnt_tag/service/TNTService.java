package com.thecraftcloud.tnt_tag.service;

import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.tnt_tag.GameController;

public class TNTService {
	
	private ConfigService configService = ConfigService.getInstance();
	private GameController controller;

	private static Integer tntStartTimeInSeconds;
	
	public TNTService(GameController controller) {
		this.controller = controller;
	}
	
	public Integer getTntDuration() {
		Integer currentTime = (int) (System.currentTimeMillis() / 1000);
		Integer duration = currentTime - this.getTntStartTimeInSeconds();
		return duration;
	}
	
	//setar o tempo em que a bomba foi colocada na mão de algum jogador
	
	public void setTntStartTimeInSeconds(Integer time) {
		TNTService.tntStartTimeInSeconds = time;
	}
	
	public Integer getTntStartTimeInSeconds() {
		return tntStartTimeInSeconds;
	}
	
	
	

}
