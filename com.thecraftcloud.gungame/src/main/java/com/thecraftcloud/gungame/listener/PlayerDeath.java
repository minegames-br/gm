package com.thecraftcloud.gungame.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.thecraftcloud.gungame.GameController;
import com.thecraftcloud.gungame.service.GunGamePlayerService;
import com.thecraftcloud.plugin.service.EntityService;

public class PlayerDeath implements Listener {

    private GameController controller;
	private GunGamePlayerService playerService;
	private EntityService entityService;

    public PlayerDeath(GameController controller) {
        this.controller = controller;
        this.playerService = new GunGamePlayerService(controller);
        this.entityService = new EntityService(controller);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
    	
        event.getDrops().clear();
        event.setDroppedExp(0);
        
		if (event instanceof PlayerDeathEvent) {
			PlayerDeathEvent playerDeathEvent = (PlayerDeathEvent)event;
			Player dead = (Player)playerDeathEvent.getEntity();
			this.playerService.killPlayer(dead);
		} else {
			Entity entity = event.getEntity();
			if(entity instanceof Entity) {
				Entity z = (Entity)entity;
				if(((LivingEntity) z).getKiller() == null) {
				} else {
				}
				this.entityService.killEntity(z);
			}
		}
    }

}