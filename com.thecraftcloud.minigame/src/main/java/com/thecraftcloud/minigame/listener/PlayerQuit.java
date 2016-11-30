package com.thecraftcloud.minigame.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.domain.MyCloudCraftGame;
import com.thecraftcloud.minigame.service.ConfigService;

public class PlayerQuit implements Listener {

	private TheCraftCloudMiniGameAbstract controller;
	private ConfigService configService = ConfigService.getInstance();

    public PlayerQuit(TheCraftCloudMiniGameAbstract controller) {
		super();
		this.controller = controller;
	}

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();
        
		Bukkit.broadcastMessage(ChatColor.GOLD + " " + player.getName() + " saiu do jogo." );

        
        MyCloudCraftGame game = this.configService.getMyCloudCraftGame();
        
        if(game != null && !game.isOver() ) {
        	controller.removeLivePlayer(player);
        }
        
        //limpar o inventário do jogador
        player.getInventory().clear();
    }
    
}