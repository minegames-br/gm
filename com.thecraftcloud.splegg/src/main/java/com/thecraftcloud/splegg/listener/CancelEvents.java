package com.thecraftcloud.splegg.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.splegg.GameController;

public class CancelEvents implements Listener {

	private GameController controller;
	private ConfigService configService = ConfigService.getInstance();

	public CancelEvents(GameController controller) {
		super();
		this.controller = controller;
	}

	@EventHandler
	public void onHungry(FoodLevelChangeEvent event) {
		if (!configService.getMyCloudCraftGame().isStarted()) {
			return;
		}
		event.setCancelled(true);
	}

	@EventHandler
	public void onFallDamage(EntityDamageEvent event) {
		if (!configService.getMyCloudCraftGame().isStarted()) {
			return;
		}
		
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) event.getEntity();
		if (event.getCause() == DamageCause.FALL) {
			event.setCancelled(true);

		}
	}
}
