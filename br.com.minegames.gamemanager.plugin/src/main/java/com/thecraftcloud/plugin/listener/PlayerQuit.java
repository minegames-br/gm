package com.thecraftcloud.plugin.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.thecraftcloud.domain.MyCloudCraftGame;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.plugin.service.PlayerManager;

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
        
		Bukkit.broadcastMessage(ChatColor.GOLD + " " + player.getName() + " saiu do jogo." );

        
        MyCloudCraftGame game = controller.getMyCloudCraftGame();
        
        if(game != null && !game.isOver() ) {
        	controller.removeLivePlayer(player);
        }
        
        //limpar o invent�rio do jogador
        player.getInventory().clear();
        
        controller.teleportPlayersBackToLobby(player);
    }
    
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		Bukkit.broadcastMessage(ChatColor.GOLD + " " + player.getName() + " saiu do jogo." );
		PlayerManager.getInstance().removePlayer(player);
	}


}