package com.thecraftcloud.plugin.task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.domain.ServerStatus;
import com.thecraftcloud.domain.GameState;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.plugin.TheCraftCloudPlugin;
import com.thecraftcloud.plugin.service.ConfigService;

public class TheCraftCloudAdminTask implements Runnable {
	
	private TheCraftCloudPlugin controller;
	private ConfigService configService = ConfigService.getInstance();
	
	public TheCraftCloudAdminTask(TheCraftCloudPlugin controller) {
		this.controller = controller;
	}
	
    @Override
    public void run() {
    	ServerInstance server = configService.getServerInstance();
    	TheCraftCloudMiniGameAbstract miniGame = getMiniGame();
    	TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();

    	if( miniGame == null) {
    		server.setStatus(ServerStatus.NO_MINIGAME);
    	} else {
        	GameState state = miniGame.getMyCloudCraftGame().getState();
        	if(state.equals(GameState.WAITING)) {
            	server.setStatus(ServerStatus.ONLINE);
        	} else if(state.equals(GameState.RUNNING) || state.equals(GameState.STARTING)) {
        		server.setStatus(ServerStatus.INGAME);
        	} else if(state.equals(GameState.GAMEOVER)) {
        		server.setStatus(ServerStatus.ONLINE);
        		//deixar o jogo esperando jogadores
        		miniGame.init(server.getLobby());
        	}
    	}
    	delegate.updateServer(server);
    	
    }

    private TheCraftCloudMiniGameAbstract getMiniGame() {
    	TheCraftCloudMiniGameAbstract miniGame = null;
		Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
		for(Plugin plugin: plugins) {
			if(! (plugin instanceof JavaPlugin) ) {
				continue;
			}
			JavaPlugin javaPlugin = (JavaPlugin)plugin;
			if(javaPlugin instanceof TheCraftCloudMiniGameAbstract) {
				miniGame = (TheCraftCloudMiniGameAbstract)javaPlugin;
			}
		}
		return miniGame;
    }
}
