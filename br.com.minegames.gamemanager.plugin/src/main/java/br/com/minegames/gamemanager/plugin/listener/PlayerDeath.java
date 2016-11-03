package br.com.minegames.gamemanager.plugin.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import br.com.minegames.core.logging.MGLogger;
import br.com.minegames.gamemanager.plugin.MyCloudCraftPlugin;

public class PlayerDeath implements Listener {

    private MyCloudCraftPlugin controller;

    public PlayerDeath(MyCloudCraftPlugin controller) {
        this.controller = controller;
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
    	
        event.getDrops().clear();
        event.setDroppedExp(0);
        
		if (event instanceof PlayerDeathEvent) {
			PlayerDeathEvent playerDeathEvent = (PlayerDeathEvent)event;
			Player dead = (Player)playerDeathEvent.getEntity();
			MGLogger.debug("player: " + dead.getName() + " died.");
			controller.killPlayer(dead);
		} else {
			Entity entity = event.getEntity();
			if(entity instanceof Entity) {
				Entity z = (Entity)entity;
				if(((LivingEntity) z).getKiller() == null) {
					MGLogger.debug( "Killer est� null" );
				} else {
					MGLogger.debug(((LivingEntity) z).getKiller() + " " + ((LivingEntity) z).getKiller().getName() );
				}
				controller.killEntity(z);
			}
		}
    }

}