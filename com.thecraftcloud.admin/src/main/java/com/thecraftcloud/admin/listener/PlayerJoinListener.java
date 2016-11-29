package com.thecraftcloud.admin.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.thecraftcloud.admin.service.PlayerService;
import com.thecraftcloud.core.util.Utils;

public class PlayerJoinListener  implements Listener {
	
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
    	Player player = event.getPlayer();
    	Bukkit.getConsoleSender().sendMessage(Utils.color("&3Player: " + player.getName() + " has joined the server."));
    	PlayerService playerService = new PlayerService();
    	playerService.joinServer(player);
    }


}
