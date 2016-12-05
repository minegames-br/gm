package com.thecraftcloud.admin.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.thecraftcloud.admin.service.AdminService;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.minigame.event.PlayerJoinGameEvent;
import com.thecraftcloud.minigame.service.ConfigService;

public class PlayerJoinGameListener implements Listener {
	
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerJoinGame(final PlayerJoinGameEvent event) {
    	String name = event.getGamePlayer().getPlayer().getName();
    	Bukkit.getConsoleSender().sendMessage(Utils.color("&2Player: " + name + " has joined " + event.getGame().getName() + "."));
    	final AdminService service = new AdminService();
    	Bukkit.getScheduler().runTaskAsynchronously(event.getGame(), new Runnable() {
    		public void run() {
    	    	service.notifyPlayerJoin( event.getGamePlayer(), event.getGame() );
    		}
    	});
    }

}
