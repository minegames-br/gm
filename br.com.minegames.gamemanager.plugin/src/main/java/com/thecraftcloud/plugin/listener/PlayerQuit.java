package com.thecraftcloud.plugin.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.thecraftcloud.domain.MyCloudCraftGame;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;

public class PlayerQuit implements Listener {

	private TheCraftCloudMiniGameAbstract controller;

    public PlayerQuit(TheCraftCloudMiniGameAbstract controller) {
		super();
		this.controller = controller;
	}

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();
        
        MyCloudCraftGame game = controller.getMyCloudCraftGame();
        
        if(game != null && !game.isOver() ) {
        	controller.removeLivePlayer(player);
        }
        
        //limpar o inventário do jogador
        player.getInventory().clear();
        
        controller.teleportPlayersBackToLobby(player);
    }

}