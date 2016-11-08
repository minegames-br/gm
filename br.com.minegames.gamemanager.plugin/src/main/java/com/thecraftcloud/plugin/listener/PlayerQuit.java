package com.thecraftcloud.plugin.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.domain.MyCloudCraftGame;
import com.thecraftcloud.plugin.MyCloudCraftPlugin;

public class PlayerQuit implements Listener {

	private MyCloudCraftPlugin controller;

    public PlayerQuit(MyCloudCraftPlugin controller) {
		super();
		this.controller = controller;
	}

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();
        
        MyCloudCraftGame game = controller.getMyCloudCraftGame();
        
        if( !game.isOver() ) {
        	controller.removeLivePlayer(player);
        }
        
        //limpar o inventário do jogador
        player.getInventory().clear();
        
        controller.teleportPlayersBackToLobby(player);
    }

}