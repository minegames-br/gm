package com.thecraftcloud.gungame.domain;

import com.thecraftcloud.domain.GameState;
import com.thecraftcloud.domain.Level;
import com.thecraftcloud.domain.MyCloudCraftGame;

public class GunGame extends MyCloudCraftGame {

	public GunGame() {

		//mudar o state do jogo para esperar jogadores entrarem
		this.state = GameState.WAITING;
		this.level = new Level();
		
	}



}
