package br.com.minegames.gamemanager.plugin.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.minegames.core.util.Utils;
import br.com.minegames.gamemanager.domain.MyCloudCraftGame;
import br.com.minegames.gamemanager.plugin.MyCloudCraftPlugin;

public class PlayerQuit implements Listener {

	private MyCloudCraftPlugin controller;

    public PlayerQuit(MyCloudCraftPlugin controller) {
		super();
		this.controller = controller;
	}

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Bukkit.getConsoleSender().sendMessage(Utils.color("&6PlayerQuit.onQuit"));
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