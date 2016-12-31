package com.thecraftcloud.tnt_tag.domain;

import com.thecraftcloud.minigame.domain.GameState;
import com.thecraftcloud.minigame.domain.Level;
import com.thecraftcloud.minigame.domain.MyCloudCraftGame;

public class TNTTag extends MyCloudCraftGame {

	public TNTTag() {

		//mudar o state do jogo para esperar jogadores entrarem
		this.state = GameState.WAITING;
		this.level = new Level();
		
	}



}
