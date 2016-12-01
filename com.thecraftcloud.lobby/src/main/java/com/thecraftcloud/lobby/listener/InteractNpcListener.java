package com.thecraftcloud.lobby.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.thecraftcloud.core.bungee.BungeeUtils;
import com.thecraftcloud.lobby.TheCraftCloudLobbyPlugin;
import com.thecraftcloud.lobby.service.GameService;

public class InteractNpcListener implements Listener, PluginMessageListener {
	
	private JavaPlugin plugin;
	private GameService gameService = GameService.getInstance();
	
	public InteractNpcListener (TheCraftCloudLobbyPlugin plugin) {
		super();
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onInteractNpc(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		String gameName = event.getRightClicked().getName().toLowerCase();
		
		//Falta mandar o player para algum lugar
		
		/*
		gameService.playGame(player, gameName);
		BungeeUtils bungeeUtils = new BungeeUtils();
		bungeeUtils.setup(plugin);
		bungeeUtils.sendToServer(player, gameName);
		*/
		
	}

	@Override
	public void onPluginMessageReceived(String arg0, Player arg1, byte[] arg2) {
		// TODO Auto-generated method stub
		
	}
	
	

}
