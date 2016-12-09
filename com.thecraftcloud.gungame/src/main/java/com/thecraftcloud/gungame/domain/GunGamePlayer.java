package com.thecraftcloud.gungame.domain;

import com.thecraftcloud.minigame.domain.GamePlayer;

public class GunGamePlayer extends GamePlayer {

	private Integer level = 1;

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	
}
