package com.thecraftcloud.admin.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.thecraftcloud.admin.service.AdminService;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.minigame.event.PlayerLeftGameEvent;

public class PlayerLeftGameListener implements Listener {
	
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerJoinGame(final PlayerLeftGameEvent event) {
    	Bukkit.getConsoleSender().sendMessage(Utils.color("&2Player: " + event.getGamePlayer().getName() + " has left " + event.getGame().getName() + "."));
    	final AdminService service = new AdminService();
    	Bukkit.getScheduler().runTaskAsynchronously(event.getGame(), new Runnable() {
    		public void run() {
    	    	service.notifyPlayerLeft( event.getGamePlayer(), event.getGame() );
    	    	AdminService adminService = new AdminService();
    	    	adminService.sendPlayerToLobby( event.getGame(), event.getGamePlayer() );
    		}
    	});
    }

}
