package com.thecraftcloud.minigame.task;

import org.bukkit.scheduler.BukkitRunnable;

import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;

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
