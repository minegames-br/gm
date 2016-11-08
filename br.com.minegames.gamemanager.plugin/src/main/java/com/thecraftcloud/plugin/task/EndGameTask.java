package com.thecraftcloud.plugin.task;

import org.bukkit.scheduler.BukkitRunnable;

import com.thecraftcloud.plugin.MyCloudCraftPlugin;

public class EndGameTask extends BukkitRunnable {
	
	private MyCloudCraftPlugin controller;
	
	public EndGameTask(MyCloudCraftPlugin controller) {
		this.controller = controller;
	}
	
    @Override
    public void run() {

    	if( controller.shouldEndGame() ) {
            controller.endGame();
    	}
    }

}
