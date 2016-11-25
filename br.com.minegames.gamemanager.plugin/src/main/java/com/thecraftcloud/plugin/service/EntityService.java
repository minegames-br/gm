package com.thecraftcloud.plugin.service;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

import com.thecraftcloud.domain.EntityPlayer;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;

public class EntityService {
	
	private TheCraftCloudMiniGameAbstract controller;
	private PlayerService playerService;
	
	public EntityService(TheCraftCloudMiniGameAbstract controller) {
		this.controller = controller;
		this.playerService = new PlayerService(controller);
	}
	
	public EntityPlayer findEntityTargetByZombie(Zombie zombie) {
		boolean foundTarget = false;
		EntityPlayer et = null;
		for (EntityPlayer z : controller.getLivingEntities()) {
			if (z.getLivingEntity().equals(zombie)) {
				foundTarget = true;
				et = z;
			}
		}
		if (!foundTarget) {
		}
		return et;
	}

	public EntityPlayer findEntityPlayer(Entity entity) {
		boolean foundTarget = false;
		EntityPlayer et = null;
		for (EntityPlayer z : controller.getLivingEntities()) {
			if (z.getLivingEntity().equals(entity)) {
				foundTarget = true;
				et = z;
			}
		}
		if (!foundTarget) {
		}
		return et;
	}

	public void killEntityPlayers() {
		for (EntityPlayer eTarget : controller.getLivingEntities()) {
			if (eTarget instanceof EntityPlayer) {
				this.killEntity(((EntityPlayer) eTarget).getLivingEntity()); // this.killZombie...getZombie
			}
		}
	}

	public void killEntity(Entity z) {
		EntityPlayer et = (EntityPlayer) findEntityPlayer(z);
		Location loc = z.getLocation();
		if (et != null) {
			if (et.getKiller() != null) {
				Player player = et.getKiller();
				this.playerService.givePoints(player, et.getKillPoints());
				controller.getLivingEntities().remove(et);
			} else {
				controller.getLivingEntities().remove(et);
			}
		}
	}

}
