package com.thecraftcloud.splegg.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.BlockIterator;

import com.thecraftcloud.minigame.domain.MyCloudCraftGame;
import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.splegg.GameController;

public class ProjectileHit implements Listener {

	private GameController controller;
	private ConfigService configService = ConfigService.getInstance();

	public ProjectileHit(GameController controller) {
		super();
		this.controller = controller;
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
			Block hit = null;

			while (bi.hasNext()) {
				hit = bi.next();
				if (!hit.getType().equals(Material.AIR) && !hit.getType().equals(Material.TNT)) {
					egg.remove();
					hit.setType(Material.AIR);
				} else if (hit.getType().equals(Material.TNT)) {
					egg.remove();
					hit.setType(Material.AIR);
					player.getWorld().createExplosion(hit.getLocation(), 2.0F);
				}
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		event.setDamage(0.0);
		event.setCancelled(true);
	}

}
