package br.com.minegames.gamemanager.plugin.task;

import br.com.minegames.gamemanager.domain.MyCloudCraftGame;
import br.com.minegames.gamemanager.plugin.Constants;
import br.com.minegames.gamemanager.plugin.MyCloudCraftPlugin;

public class StartGameTask implements Runnable {
	
	private MyCloudCraftPlugin controller;
	
	public StartGameTask(MyCloudCraftPlugin controller) {
		this.controller = controller;
	}
	
    @Override
    public void run() {

    	MyCloudCraftGame game = controller.getMyCloudCraftGame();
    	
    	int minPlayers = (Integer)this.controller.getGameConfigInstance(Constants.MIN_PLAYERS);
    	int maxPlayers = (Integer)this.controller.getGameConfigInstance(Constants.MAX_PLAYERS);
    	
    	if( controller.getLivePlayers().size() == maxPlayers && game.isWaitingPlayers()) {
            controller.startCoundDown();;
    	} else if ( (controller.getLivePlayers().size() >= minPlayers)
    			&& controller.getCountDown() == 0 && game.isWaitingPlayers() ) {
            controller.startCoundDown();
    	}
    	
    	
    }

}
