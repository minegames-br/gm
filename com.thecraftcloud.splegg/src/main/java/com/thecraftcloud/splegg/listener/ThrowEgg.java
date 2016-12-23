package com.thecraftcloud.splegg.listener;

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

	public ThrowEgg(GameController controller) {
		super();
		this.controller = controller;
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {

		MyCloudCraftGame game = configService.getMyCloudCraftGame();

		if (!game.isStarted())
			return;

		if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;

		if (!(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.IRON_SPADE))
			return;

		Player player = event.getPlayer();
		Action action = event.getAction();

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
			// Block hit = null;

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

				Vector velocity = new Vector(x, z, y).multiply(1);
				egg.setVelocity(velocity);
				egg.setGravity(false);
			}
		}, 5L);
		
	}

	private void breakBlock(Egg egg, final Block block) {
		egg.remove();
		BukkitScheduler scheduler = this.controller.getServer().getScheduler();
		scheduler.scheduleSyncDelayedTask(this.controller, new Runnable() {
			@Override
			public void run() {
				block.setType(Material.AIR);
			}
		}, 1L);
	}
}
