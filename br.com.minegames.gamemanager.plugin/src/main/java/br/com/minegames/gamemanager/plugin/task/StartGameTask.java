package br.com.minegames.gamemanager.plugin.task;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.minegames.core.util.Utils;
import br.com.minegames.gamemanager.domain.MyCloudCraftGame;
import br.com.minegames.gamemanager.plugin.Constants;
import br.com.minegames.gamemanager.plugin.MyCloudCraftPlugin;

public class StartGameTask extends BukkitRunnable {
	
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
            Bukkit.getConsoleSender().sendMessage(Utils.color("&6StartGameTask - Max number of players achieved. Starting game."));
            controller.startGameEngine();
    	} else if ( (controller.getLivePlayers().size() >= minPlayers)
    			&& controller.getCountDown() == 0 && game.isWaitingPlayers() ) {
            Bukkit.getConsoleSender().sendMessage(Utils.color("&6StartGameTask - Min number of players achieved. Countdown 0. Starting game."));
            controller.startGameEngine();
    	}
    	
    	
    }

}
