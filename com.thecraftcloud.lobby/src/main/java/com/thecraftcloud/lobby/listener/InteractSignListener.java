package com.thecraftcloud.lobby.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.thecraftcloud.core.bungee.BungeeUtils;
import com.thecraftcloud.lobby.service.GameService;

public class InteractSignListener implements Listener, PluginMessageListener {

	private JavaPlugin plugin;
	private GameService gameService = GameService.getInstance();

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
			String gameName = sign.getLine(1);
			gameService.playGame(player, gameName);
			BungeeUtils bungeeUtils = new BungeeUtils();
			bungeeUtils.setup(plugin);
			bungeeUtils.sendToServer(player, "gungame");

		}
	}

	@Override
	public void onPluginMessageReceived(String arg0, Player arg1, byte[] arg2) {
	}

}
