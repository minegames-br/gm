package com.thecraftcloud.admin.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
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
    	Bukkit.getConsoleSender().sendMessage(Utils.color("&2Player: " + event.getGamePlayer().getPlayer().getName() + " has left " + event.getGame().getName() + "."));
    	final AdminService service = new AdminService(plugin);
    	//if(plugin.isLocal()) {
    		World world = Bukkit.getWorld("world");
   			event.getGamePlayer().getPlayer().teleport(new Location(world, 0, 5, 0));
    	//}
    	service.notifyPlayerLeft( event.getGamePlayer(), event.getGame() );
    }

}
