package com.thecraftcloud.tnt_tag.listener;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitScheduler;

import com.thecraftcloud.minigame.domain.MyCloudCraftGame;
import com.thecraftcloud.tnt_tag.GameController;
import com.thecraftcloud.tnt_tag.service.TntTagPlayerService;

import net.minecraft.server.v1_10_R1.Material;

public class PlayerDrop implements Listener {

	private GameController controller;
	private TntTagPlayerService playerService;
	private HashMap<String, Long> cooldowns = new HashMap<String, Long>();

	public PlayerDrop(GameController controller) {
		this.controller = controller;
		this.playerService = new TntTagPlayerService(controller);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDeath(final PlayerMoveEvent event) {

		MyCloudCraftGame game = this.controller.getConfigService().getMyCloudCraftGame();

		if (!game.isStarted())
			return;

		final Player player = event.getPlayer();

		if (event.getTo().getBlock().isLiquid()
				|| player.getLocation().getY() < this.controller.getArea().getPointB().getY()) {

			BukkitScheduler scheduler = this.controller.getServer().getScheduler();
			scheduler.scheduleSyncDelayedTask(this.controller, new Runnable() {

				@Override
				public void run() {

					int cooldownTime = 3;

					if (cooldowns.containsKey(player.getName())) {
						Long secondsLeft = ((cooldowns.get(player.getName()) / 1000) + cooldownTime)
								- (System.currentTimeMillis() / 1000);
						if (secondsLeft > 0) {
							event.setCancelled(true);
							return;
						}
					}

					cooldowns.put(player.getName(), System.currentTimeMillis());

					playerService.killPlayer(player);

				}
			}, 20L);

		}

	}

}