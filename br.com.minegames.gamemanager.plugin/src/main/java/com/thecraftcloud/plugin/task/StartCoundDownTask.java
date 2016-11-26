package com.thecraftcloud.plugin.task;

import org.bukkit.scheduler.BukkitRunnable;

import com.thecraftcloud.domain.MyCloudCraftGame;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.plugin.service.ConfigService;

public class StartCoundDownTask extends BukkitRunnable {
	
	private TheCraftCloudMiniGameAbstract controller;
	private ConfigService configService = ConfigService.getInstance();
	
	public StartCoundDownTask(TheCraftCloudMiniGameAbstract controller) {
		this.controller = controller;
	}
	
    @Override
    public void run() {

    	MyCloudCraftGame game = controller.getMyCloudCraftGame();
    	
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
