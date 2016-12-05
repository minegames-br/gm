package com.thecraftcloud.minigame.task;

import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;

public class EndGameTask implements Runnable {
	
	private TheCraftCloudMiniGameAbstract controller;
	
	public EndGameTask(TheCraftCloudMiniGameAbstract controller) {
		this.controller = controller;
	}
	
    @Override
    public void run() {

    	if( controller.shouldEndGame() ) {
            controller.endGame();
    	}
    }

}
