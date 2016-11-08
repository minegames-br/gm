package com.thecraftcloud.plugin.task;

import org.bukkit.scheduler.BukkitRunnable;

import com.thecraftcloud.domain.MyCloudCraftGame;
import com.thecraftcloud.plugin.Constants;
import com.thecraftcloud.plugin.MyCloudCraftPlugin;

public class StartCoundDownTask extends BukkitRunnable {
	
	private MyCloudCraftPlugin controller;
	
	public StartCoundDownTask(MyCloudCraftPlugin controller) {
		this.controller = controller;
	}
	
    @Override
    public void run() {

    	MyCloudCraftGame game = controller.getMyCloudCraftGame();
    	
    	int configValue = (Integer)this.controller.getGameConfigInstance(Constants.MIN_PLAYERS);
    	if( !game.isStarting() ) {
        	if( controller.getLivePlayers().size() >= configValue && !game.isStarting() ) {
        		controller.startCoundDown();
        	}
    	} else {
    		controller.proceedCountdown();
    	}
    }

}
