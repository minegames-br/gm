package com.thecraftcloud.admin.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.thecraftcloud.admin.TheCraftCloudAdmin;
import com.thecraftcloud.admin.service.AdminService;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.minigame.event.StartGameEvent;
import com.thecraftcloud.minigame.service.ConfigService;

public class StartGameListener implements Listener {
	
	private TheCraftCloudAdmin plugin;
	
	public StartGameListener(TheCraftCloudAdmin plugin) {
		this.plugin = plugin;
	}
	
	private TheCraftCloudAdmin plugin;
	
	public StartGameListener(TheCraftCloudAdmin plugin) {
		this.plugin = plugin;
	}
	
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onStartGame(final StartGameEvent event) {
    	Bukkit.getConsoleSender().sendMessage(Utils.color("&2Game: " + event.getGame().getName() + " has started."));
    	final AdminService service = new AdminService(plugin);
    	final ConfigService cService = ConfigService.getInstance();
    	Bukkit.getScheduler().runTaskAsynchronously(event.getGame(), new Runnable() {
    		public void run() {
    	    	service.notifyGameStart( event.getGame(), plugin.getServerInstance() );
    		}
    	});
    }

}
