package com.thecraftcloud.tnt_tag.domain;

import com.thecraftcloud.minigame.domain.GameState;
import com.thecraftcloud.minigame.domain.Level;
import com.thecraftcloud.minigame.domain.MyCloudCraftGame;

public class TntTag extends MyCloudCraftGame {

	public TntTag() {

		//mudar o state do jogo para esperar jogadores entrarem
		this.state = GameState.WAITING;
		this.level = new Level();
		
	}



}
