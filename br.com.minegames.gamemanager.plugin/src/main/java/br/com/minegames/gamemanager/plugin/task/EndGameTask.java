package br.com.minegames.gamemanager.plugin.task;

import org.bukkit.scheduler.BukkitRunnable;

import br.com.minegames.gamemanager.plugin.MyCloudCraftPlugin;

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
