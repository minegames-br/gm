package com.thecraftcloud.plugin.task;

import com.thecraftcloud.domain.MyCloudCraftGame;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.plugin.service.ConfigService;

public class StartGameTask implements Runnable {
	
	private TheCraftCloudMiniGameAbstract controller;
	private ConfigService configService = ConfigService.getInstance();
	
	public StartGameTask(TheCraftCloudMiniGameAbstract controller) {
		this.controller = controller;
	}
	
    @Override
    public void run() {

    	MyCloudCraftGame game = controller.getMyCloudCraftGame();
    	
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
