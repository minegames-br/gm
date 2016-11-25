package com.thecraftcloud.plugin.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class InteractSignListener implements Listener, PluginMessageListener {

	private JavaPlugin plugin;
	private com.thecraftcloud.core.bungee.BungeeUtils bUtils;

	public InteractSignListener(JavaPlugin plugin) {
		super();
		this.plugin = plugin;
	}

	@EventHandler
	public void onInteractSign(PlayerInteractEvent event) {
		
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if (block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST) {
			Sign sign = (Sign) block.getState();
			String server = sign.getLine(1);
			bUtils.sendToServer(player, server);
			
		
			
		}
	}

	@Override
	public void onPluginMessageReceived(String arg0, Player arg1, byte[] arg2) {
		// TODO Auto-generated method stub

	}

}
