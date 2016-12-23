package com.thecraftcloud.gamesetup.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.gamesetup.TheCraftCloudGameSetupPlugin;

public class PlayerChangeWorldListener implements Listener {

	private TheCraftCloudGameSetupPlugin controller;
	
    public PlayerChangeWorldListener(TheCraftCloudGameSetupPlugin plugin) {
		super();
		this.controller = plugin;
	}


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChangeWorld(PlayerChangedWorldEvent event) {
    	Bukkit.getConsoleSender().sendMessage(Utils.color("&5onChangeWorld" + event.getFrom().getName() + " " + event.getPlayer().getWorld().getName() ));
    	Player player = event.getPlayer();
    	player.setGameMode(GameMode.CREATIVE);
    	player.setFlying(true);
    	
    }
    
}