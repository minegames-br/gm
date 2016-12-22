package com.thecraftcloud.splegg.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.thecraftcloud.minigame.domain.MyCloudCraftGame;
import com.thecraftcloud.minigame.service.EntityService;
import com.thecraftcloud.splegg.GameController;
import com.thecraftcloud.splegg.service.SpleggPlayerService;

public class PlayerDeath implements Listener {

	private GameController controller;
	private SpleggPlayerService playerService;
	private EntityService entityService;

	public PlayerDeath(GameController controller) {
		this.controller = controller;
		this.playerService = new SpleggPlayerService(controller);
		this.entityService = new EntityService(controller);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDeath(EntityDeathEvent event) {
		
		MyCloudCraftGame game = this.controller.getConfigService().getMyCloudCraftGame();

		if (!game.isStarted()) {
			return;
		}


		event.getDrops().clear();
		event.setDroppedExp(0);

		if (event instanceof PlayerDeathEvent) {
			PlayerDeathEvent playerDeathEvent = (PlayerDeathEvent) event;
			Player dead = (Player) playerDeathEvent.getEntity();
			this.playerService.killPlayer(dead);
			dead.setHealth(20);
			playerService.spawnDeadPlayer(dead);
		} else {
			Entity entity = event.getEntity();
			if (entity instanceof Entity) {
				Entity z = (Entity) entity;
				if (((LivingEntity) z).getKiller() == null) {
				} else {
				}
				this.entityService.killEntity(z);
			}
		}
	}

}