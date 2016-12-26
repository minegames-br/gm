package com.thecraftcloud.admin.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.thecraftcloud.admin.TheCraftCloudAdmin;
import com.thecraftcloud.admin.service.PermissionService;
import com.thecraftcloud.admin.service.PlayerService;
import com.thecraftcloud.core.util.Utils;

public class PlayerJoinListener  implements Listener {
	
	private TheCraftCloudAdmin plugin;

	public PlayerJoinListener(TheCraftCloudAdmin plugin) {
		this.plugin = plugin;
	}
	
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
    	Player player = event.getPlayer();
    	Bukkit.getConsoleSender().sendMessage(Utils.color("&3Player: " + player.getName() + " has joined the server."));
    	PlayerService playerService = new PlayerService(this.plugin);
    	playerService.joinServer(player);
    	
    	PermissionService permService = new PermissionService();
    	permService.setPermissions(player);
    }


}
