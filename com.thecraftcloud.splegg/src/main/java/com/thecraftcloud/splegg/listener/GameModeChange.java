package com.thecraftcloud.splegg.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.splegg.GameController;

public class GameModeChange implements Listener {

	private GameController controller;
	private ConfigService configService = ConfigService.getInstance();

	public GameModeChange(GameController controller) {
		super();
		this.controller = controller;
	}

	@EventHandler
	public void onGameModeChange(PlayerGameModeChangeEvent e) {

		if (!configService.getMyCloudCraftGame().isStarted()) {
			return;
		}

		final Player p = e.getPlayer();

		if (e.getNewGameMode() == GameMode.ADVENTURE) {
			p.setAllowFlight(true);
			p.setFlying(true);
			for (Player pla : Bukkit.getOnlinePlayers()) {
				pla.hidePlayer(e.getPlayer());
			}

			e.getPlayer().sendMessage(ChatColor.GREEN + "Você está como espectador!");
		}
	}

	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if ((player.getGameMode().equals(GameMode.ADVENTURE)
				|| (player.getGameMode() != GameMode.CREATIVE) && (!player.isFlying()))) {
			player.setAllowFlight(true);
		}
	}


}
