package com.thecraftcloud.admin.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.thecraftcloud.admin.TheCraftCloudAdmin;
import com.thecraftcloud.admin.service.PlayerService;
import com.thecraftcloud.core.util.Utils;

public class PlayerQuitListener  implements Listener {
	
	private TheCraftCloudAdmin plugin;

	public PlayerQuitListener(TheCraftCloudAdmin plugin) {
		this.plugin = plugin;
	}
	
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
    	Player player = event.getPlayer();
    	Bukkit.getConsoleSender().sendMessage(Utils.color("&3Player: " + player.getName() + " has left the server."));
    	
    	//Retirei pq o onJoin acontece antes no outro servidor que o onQuit no original. Isso deixava null no server do player
    	//PlayerService playerService = new PlayerService(this.plugin);
    	//playerService.quitServer(player);
    }


}
