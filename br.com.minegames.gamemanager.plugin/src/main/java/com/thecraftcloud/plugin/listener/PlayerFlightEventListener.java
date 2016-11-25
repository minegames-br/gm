package com.thecraftcloud.plugin.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerFlightEventListener implements Listener {

	public PlayerFlightEventListener(JavaPlugin plugin) {
		super();
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if ((player.getGameMode() != GameMode.CREATIVE) && (!player.isFlying())) {
			player.setAllowFlight(true);
		}
	}

}
