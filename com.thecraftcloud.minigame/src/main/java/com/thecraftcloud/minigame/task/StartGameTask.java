package com.thecraftcloud.minigame.task;

import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.domain.MyCloudCraftGame;
import com.thecraftcloud.minigame.service.ConfigService;

public class StartGameTask implements Runnable {
	
	private TheCraftCloudMiniGameAbstract controller;
	private ConfigService configService = ConfigService.getInstance();
	
	public StartGameTask(TheCraftCloudMiniGameAbstract controller) {
		this.controller = controller;
	}
	
    @Override
    public void run() {

    	MyCloudCraftGame game = this.configService.getMyCloudCraftGame();
    	
    	int minPlayers = (Integer)this.configService.getMinPlayers();
    	int maxPlayers = (Integer)this.configService.getMaxPlayers();
    	
    	if( controller.getLivePlayers().size() == maxPlayers && game.isWaitingPlayers()) {
            controller.startCoundDown();;
    	} else if ( (controller.getLivePlayers().size() >= minPlayers)
    			&& this.configService.getStartCountDown() == 0 && game.isWaitingPlayers() ) {
            controller.startCoundDown();
    	}
    	
    	
    }

}
