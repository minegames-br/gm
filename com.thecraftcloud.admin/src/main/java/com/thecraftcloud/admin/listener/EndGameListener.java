package com.thecraftcloud.admin.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.thecraftcloud.admin.service.AdminService;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.minigame.event.EndGameEvent;
import com.thecraftcloud.minigame.service.ConfigService;

public class EndGameListener implements Listener {
	
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onEndGame(final EndGameEvent event) {
    	Bukkit.getConsoleSender().sendMessage(Utils.color("&3Game: " + event.getGame().getName() + " has ended."));
    	final AdminService service = new AdminService();
    	final ConfigService cService = ConfigService.getInstance();
    	Bukkit.getScheduler().runTaskAsynchronously(event.getGame(), new Runnable() {
    		public void run() {
    	    	service.notifyGameOver( event.getGame() );
    		}
    	});
    }

}
