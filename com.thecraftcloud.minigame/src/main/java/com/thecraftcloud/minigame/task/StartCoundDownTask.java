package com.thecraftcloud.minigame.task;

import org.bukkit.scheduler.BukkitRunnable;

import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.domain.MyCloudCraftGame;
import com.thecraftcloud.minigame.service.ConfigService;

public class StartCoundDownTask extends BukkitRunnable {
	
	private TheCraftCloudMiniGameAbstract controller;
	private ConfigService configService = ConfigService.getInstance();
	
	public StartCoundDownTask(TheCraftCloudMiniGameAbstract controller) {
		this.controller = controller;
	}
	
    @Override
    public void run() {

    	MyCloudCraftGame game = this.configService.getMyCloudCraftGame();
    	
    	int configValue = (Integer)this.configService.getMinPlayers();
    	if( !game.isStarting() ) {
        	if( controller.getLivePlayers().size() >= configValue && !game.isStarting() ) {
        		controller.startCoundDown();
        	}
    	} else {
    		controller.proceedCountdown();
    	}
    }

}
