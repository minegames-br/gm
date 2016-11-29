package com.thecraftcloud.gungame.domain;

import com.thecraftcloud.minigame.domain.GameState;
import com.thecraftcloud.minigame.domain.Level;
import com.thecraftcloud.minigame.domain.MyCloudCraftGame;

public class GunGame extends MyCloudCraftGame {

	public GunGame() {

		//mudar o state do jogo para esperar jogadores entrarem
		this.state = GameState.WAITING;
		this.level = new Level();
		
	}



}
