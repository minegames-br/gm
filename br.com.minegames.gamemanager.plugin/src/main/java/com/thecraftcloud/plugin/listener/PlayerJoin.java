package com.thecraftcloud.plugin.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.plugin.service.PlayerManager;

public class PlayerJoin implements Listener {

	private TheCraftCloudMiniGameAbstract controller;

    public PlayerJoin(TheCraftCloudMiniGameAbstract controller) {
		super();
		this.controller = controller;
	}

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        controller.teleportPlayersBackToLobby(player);
    }
    
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.sendMessage("Olá " + player.getName() + "! Bem-Vindo ao incrível mundo da Minegames!");
		PlayerManager.getInstance().addPlayerToList(player);

	}

}