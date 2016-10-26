package br.com.minegames.gamemanager.plugin.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import br.com.minegames.core.domain.Local;
import br.com.minegames.gamemanager.plugin.MineGamesPlugin;

public class PlayerOnClick implements Listener {

	private MineGamesPlugin controller;
	
    public PlayerOnClick(MineGamesPlugin plugin) {
		super();
		this.controller = plugin;
	}


    @EventHandler
    public void onClick(PlayerInteractEvent event) {

    	Bukkit.getLogger().info("PlayerInteractEvent - mg gamemanager - Interact" );
    	Bukkit.getLogger().info("event.getAction() : " + event.getAction());
    	
    	Bukkit.getLogger().info("What is in hand?" + event.getPlayer().getInventory().getItemInMainHand());
    	
    	if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
    		controller.setSelectionPointA(event.getClickedBlock().getLocation());
    		Local l = controller.getSelection().getPointA();
    		event.getPlayer().sendMessage("Area 3D PointA selected " + l.getX() + "," + l.getY() + "," + l.getZ() );
    	}
        
    	if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
    		controller.setSelectionPointB(event.getClickedBlock().getLocation());
    		Local l = controller.getSelection().getPointB();
    		event.getPlayer().sendMessage("Area 3D PointB selected " + l.getX() + "," + l.getY() + "," + l.getZ() );
    	}
        
    }

}