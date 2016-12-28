package com.thecraftcloud.splegg.listener;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.thecraftcloud.minigame.domain.MyCloudCraftGame;
import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.splegg.GameController;
import com.thecraftcloud.splegg.service.SpleggPlayerService;

public class ThrowEgg implements Listener {

	private GameController controller;
	private ConfigService configService = ConfigService.getInstance();
	private SpleggPlayerService spleggPlayerService;
	private HashMap<String, Long> cooldowns = new HashMap<String, Long>();

	public ThrowEgg(GameController controller) {
		super();
		this.controller = controller;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {

		if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;

		if (!(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.IRON_SPADE))
			return;

		/*
		 * 
		 * double cooldownTime = 0.5;
		 * 
		 * if(cooldowns.containsKey(event.getPlayer().getName())) { double
		 * secondsLeft =
		 * ((cooldowns.get(event.getPlayer().getName())/1000)+cooldownTime) -
		 * (System.currentTimeMillis()/1000); if(secondsLeft > 0) {
		 * event.setCancelled(true); return; } }
		 * 
		 * cooldowns.put(event.getPlayer().getName(),
		 * System.currentTimeMillis());
		 */

		Player player = event.getPlayer();

		throwEgg(player);
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {

		MyCloudCraftGame game = configService.getMyCloudCraftGame();

		if (!game.isStarted()) {
			return;
		}

		if (event.getEntity() instanceof Egg) {
			Egg egg = (Egg) event.getEntity();
			Player player = (Player) egg.getShooter();
			BlockIterator bi = new BlockIterator(player.getWorld(), egg.getLocation().toVector(),
					egg.getVelocity().normalize(), 0, 2);

			while (bi.hasNext()) {
				final Block hit = bi.next();
				if (!hit.getType().equals(Material.AIR) && !hit.getType().equals(Material.TNT)) {
					breakBlock(egg, hit);

				} else if (hit.getType().equals(Material.TNT)) {
					breakBlock(egg, hit);
					player.getWorld().createExplosion(hit.getLocation(), 2.0F);
				}
			}
		}
	}

	private void throwEgg(final Player player) {
		BukkitScheduler scheduler = this.controller.getServer().getScheduler();
		scheduler.scheduleSyncDelayedTask(this.controller, new Runnable() {
			@Override
			public void run() {
				Egg egg = player.launchProjectile(Egg.class);

				double pitch = ((player.getLocation().getPitch() + 90) * Math.PI) / 180;
				double yaw = ((player.getLocation().getYaw() + 90) * Math.PI) / 180;

				double x = Math.sin(pitch) * Math.cos(yaw);
				double y = Math.sin(pitch) * Math.sin(yaw);
				double z = Math.cos(pitch);

				Vector velocity = new Vector(x, z, y).multiply(2);
				egg.setVelocity(velocity);

			}
		}, 2L);

	}

	private void breakBlock(Egg egg, final Block block) {
		egg.remove();
		block.setType(Material.AIR);
	}
}
