package com.thecraftcloud.domination.domain;

import com.thecraftcloud.minigame.domain.GameState;
import com.thecraftcloud.minigame.domain.Level;
import com.thecraftcloud.minigame.domain.MyCloudCraftGame;

public class Domination extends MyCloudCraftGame {

	public Domination() {

		//mudar o state do jogo para esperar jogadores entrarem
		this.state = GameState.WAITING;
		this.level = new Level();
		
	}



}
