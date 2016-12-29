package com.thecraftcloud.splegg.domain;

import com.thecraftcloud.minigame.domain.GameState;
import com.thecraftcloud.minigame.domain.Level;
import com.thecraftcloud.minigame.domain.MyCloudCraftGame;

public class Splegg extends MyCloudCraftGame {
	
	private Long TntStartTime;

	public Splegg() {

		//mudar o state do jogo para esperar jogadores entrarem
		this.state = GameState.WAITING;
		this.level = new Level();
		
	}

	public Long getTntStartTime() {
		return TntStartTime;
	}

	public void setTntStartTime(Long tntStartTime) {
		TntStartTime = tntStartTime;
	}



}
