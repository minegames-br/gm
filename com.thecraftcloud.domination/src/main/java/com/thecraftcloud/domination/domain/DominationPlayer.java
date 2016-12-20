package com.thecraftcloud.domination.domain;

import com.thecraftcloud.minigame.domain.GamePlayer;

public class DominationPlayer extends GamePlayer {

	private Integer level = 1;

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	
	public Integer decreaseLevel() {
		return this.level--;
	}
	
}
