package com.thecraftcloud.plugin.task;

import com.thecraftcloud.domain.MyCloudCraftGame;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;

public class StartGameTask implements Runnable {
	
	private TheCraftCloudMiniGameAbstract controller;
	
	public StartGameTask(TheCraftCloudMiniGameAbstract controller) {
		this.controller = controller;
	}
	
    @Override
    public void run() {

    	MyCloudCraftGame game = controller.getMyCloudCraftGame();
    	
    	int minPlayers = (Integer)this.controller.getMinPlayers();
    	int maxPlayers = (Integer)this.controller.getMaxPlayers();
    	
    	if( controller.getLivePlayers().size() == maxPlayers && game.isWaitingPlayers()) {
            controller.startCoundDown();;
    	} else if ( (controller.getLivePlayers().size() >= minPlayers)
    			&& controller.getStartCountDown() == 0 && game.isWaitingPlayers() ) {
            controller.startCoundDown();
    	}
    	
    	
    }

}
