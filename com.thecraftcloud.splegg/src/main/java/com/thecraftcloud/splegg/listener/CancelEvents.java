package com.thecraftcloud.splegg.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.thecraftcloud.minigame.domain.MyCloudCraftGame;
import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.splegg.GameController;

public class CancelEvents implements Listener {

	private GameController controller;
	private ConfigService configService = ConfigService.getInstance();
	MyCloudCraftGame game = configService.getMyCloudCraftGame();

	public CancelEvents(GameController controller) {
		super();
		this.controller = controller;
	}

	@EventHandler
	public void onHungry(FoodLevelChangeEvent event) {

		if (!game.isStarted()) {
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onThunderChange (ThunderChangeEvent event) {
		if (!game.isStarted()) {
			return;
		}
		event.setCancelled(true);
	}

	@EventHandler
	public void onDropItem(PlayerDropItemEvent event) {
		if (!game.isStarted()) {
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPickUpItem(PlayerPickupItemEvent event) {
		if (!game.isStarted()) {
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		event.setDamage(0.0);
		event.setCancelled(true);
	}

	@EventHandler
	public void onFallDamage(EntityDamageEvent event) {

		if (!game.isStarted()) {
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
