package com.thecraftcloud.splegg.listener;

import org.bukkit.Material;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.splegg.GameController;
import com.thecraftcloud.splegg.service.SpleggPlayerService;

public class ThrowEgg implements Listener {

	private GameController controller;
	private ConfigService configService = ConfigService.getInstance();
	private SpleggPlayerService spleggPlayerService;

	public ThrowEgg(GameController controller) {
		super();
		this.controller = controller;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {

		if (!configService.getMyCloudCraftGame().isStarted()) {
			return;
		}

		Player player = event.getPlayer();
		Action action = event.getAction();

		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
			if (player.getInventory().getItemInMainHand().getType() == Material.IRON_SPADE) {

				Egg egg = player.launchProjectile(Egg.class);

				double pitch = ((player.getLocation().getPitch() + 90) * Math.PI) / 180;
				double yaw = ((player.getLocation().getYaw() + 90) * Math.PI) / 180;

				double x = Math.sin(pitch) * Math.cos(yaw);
				double y = Math.sin(pitch) * Math.sin(yaw);
				double z = Math.cos(pitch);

				Vector velocity = new Vector(x, z, y).multiply(2);

				egg.setVelocity(velocity);

			}
		}
	}

}
