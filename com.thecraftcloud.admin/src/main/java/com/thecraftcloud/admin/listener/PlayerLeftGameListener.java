package com.thecraftcloud.admin.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.thecraftcloud.admin.TheCraftCloudAdmin;
import com.thecraftcloud.admin.service.AdminService;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.minigame.event.PlayerLeftGameEvent;

public class PlayerLeftGameListener implements Listener {
	
	private TheCraftCloudAdmin plugin;

	public PlayerLeftGameListener(TheCraftCloudAdmin plugin) {
		this.plugin = plugin;
	}
	
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerJoinGame(final PlayerLeftGameEvent event) {
    	Bukkit.getConsoleSender().sendMessage(Utils.color("&2Player: " + event.getGamePlayer().getName() + " has left " + event.getGame().getName() + "."));
    	final AdminService service = new AdminService(plugin);
    	Bukkit.getScheduler().runTaskAsynchronously(event.getGame(), new Runnable() {
    		public void run() {
    	    	service.notifyPlayerLeft( event.getGamePlayer(), event.getGame() );
<<<<<<< HEAD
    	    	AdminService adminService = new AdminService();
=======
    	    	AdminService adminService = new AdminService(plugin);
>>>>>>> branch 'master' of https://github.com/minegames-br/gm.git
    			Bukkit.getConsoleSender().sendMessage(Utils.color("&6 adminService.sendPlayerToLobby" ));
    	    	adminService.sendPlayerToLobby( event.getGame(), event.getGamePlayer() );
    		}
    	});
    }

}
