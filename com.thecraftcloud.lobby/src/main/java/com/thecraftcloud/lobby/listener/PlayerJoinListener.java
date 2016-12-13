package com.thecraftcloud.lobby.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.lobby.TheCraftCloudLobbyPlugin;
import com.thecraftcloud.lobby.service.PlayerService;

public class PlayerJoinListener  implements Listener {
	
	private TheCraftCloudLobbyPlugin plugin;

	public PlayerJoinListener(TheCraftCloudLobbyPlugin plugin) {
		this.plugin = plugin;
	}
	
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
    	Player player = event.getPlayer();
    	Bukkit.getConsoleSender().sendMessage(Utils.color("&3Player: " + player.getName() + " has joined the server."));
    	PlayerService playerService = new PlayerService(this.plugin);
    	playerService.joinServer(player);
    }


}
