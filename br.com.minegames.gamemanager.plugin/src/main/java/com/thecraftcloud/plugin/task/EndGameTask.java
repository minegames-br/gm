package com.thecraftcloud.plugin.task;

import org.bukkit.scheduler.BukkitRunnable;

import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;

public class EndGameTask extends BukkitRunnable {
	
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
