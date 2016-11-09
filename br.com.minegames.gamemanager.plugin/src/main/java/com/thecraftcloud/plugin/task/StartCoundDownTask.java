package com.thecraftcloud.plugin.task;

import org.bukkit.scheduler.BukkitRunnable;

import com.thecraftcloud.domain.MyCloudCraftGame;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;

public class StartCoundDownTask extends BukkitRunnable {
	
	private TheCraftCloudMiniGameAbstract controller;
	
	public StartCoundDownTask(TheCraftCloudMiniGameAbstract controller) {
		this.controller = controller;
	}
	
    @Override
    public void run() {

    	MyCloudCraftGame game = controller.getMyCloudCraftGame();
    	
    	int configValue = (Integer)this.controller.getMinPlayers();
    	if( !game.isStarting() ) {
        	if( controller.getLivePlayers().size() >= configValue && !game.isStarting() ) {
        		controller.startCoundDown();
        	}
    	} else {
    		controller.proceedCountdown();
    	}
    }

}
