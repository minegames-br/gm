package br.com.minegames.gamemanager.plugin.task;

import org.bukkit.scheduler.BukkitRunnable;

import br.com.minegames.gamemanager.domain.MyCloudCraftGame;
import br.com.minegames.gamemanager.plugin.Constants;
import br.com.minegames.gamemanager.plugin.MyCloudCraftPlugin;

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
