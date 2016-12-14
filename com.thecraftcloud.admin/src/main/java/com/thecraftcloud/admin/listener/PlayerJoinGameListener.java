package com.thecraftcloud.admin.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.thecraftcloud.admin.TheCraftCloudAdmin;
import com.thecraftcloud.admin.service.AdminService;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.minigame.event.PlayerJoinGameEvent;

public class PlayerJoinGameListener implements Listener {
	
	private TheCraftCloudAdmin plugin;
	
    public PlayerJoinGameListener(TheCraftCloudAdmin plugin) {
		this.plugin = plugin;
	}	
	
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerJoinGame(final PlayerJoinGameEvent event) {
    	String name = event.getGamePlayer().getPlayer().getName();
    	Bukkit.getConsoleSender().sendMessage(Utils.color("&2Player: " + name + " has joined " + event.getGame().getName() + "."));
    	final AdminService service = new AdminService(this.plugin);
    	Bukkit.getScheduler().runTaskAsynchronously(event.getGame(), new Runnable() {
    		public void run() {
    	    	service.notifyPlayerJoin( event.getGamePlayer(), event.getGame() );
    		}
    	});
    }

}
