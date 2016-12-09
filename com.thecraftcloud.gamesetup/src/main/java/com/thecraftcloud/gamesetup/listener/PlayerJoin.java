package com.thecraftcloud.gamesetup.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.thecraftcloud.gamesetup.TheCraftCloudGameSetupPlugin;

public class PlayerJoin implements Listener {

	private TheCraftCloudGameSetupPlugin controller;
	
    public PlayerJoin(TheCraftCloudGameSetupPlugin plugin) {
		super();
		this.controller = plugin;
	}


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

    	Player player = event.getPlayer();
    	player.teleport( controller.getCockpitLocation() ) ;
    	player.setGameMode(GameMode.CREATIVE);
    	
    }
    
}