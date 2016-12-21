package com.thecraftcloud.splegg.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.BlockIterator;

import com.thecraftcloud.splegg.GameController;

public class ProjectileHit implements Listener {

	private GameController controller;

	public ProjectileHit(GameController controller) {
		super();
		this.controller = controller;
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		if (event.getEntity() instanceof Egg) {
			Egg egg = (Egg) event.getEntity();
			Player shooter = (Player) egg.getShooter();
			BlockIterator bi = new BlockIterator(shooter.getWorld(), egg.getLocation().toVector(),
					egg.getVelocity().normalize(), 0, 2);
			Block hit = null;

			while (bi.hasNext()) {
				hit = bi.next();
				if (!hit.getType().equals(Material.AIR)) {
					egg.remove();
					hit.setType(Material.AIR);

				}
			}

		}
	}

}
