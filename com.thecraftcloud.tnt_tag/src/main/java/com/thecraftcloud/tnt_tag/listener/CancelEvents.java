package com.thecraftcloud.tnt_tag.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.thecraftcloud.minigame.domain.MyCloudCraftGame;
import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.tnt_tag.GameController;

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
	
	public void onRightClick(PlayerInteractEvent event) {
		if (!game.isStarted()) {
			return;
		}

		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			event.setCancelled(true);		
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (!game.isStarted()) {
			return;
		}
		event.setCancelled(true);
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (!game.isStarted()) {
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onExplosion(BlockExplodeEvent event) {
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
