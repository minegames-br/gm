package com.thecraftcloud.gamesetup.listener;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Lever;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Stairs;

import com.thecraftcloud.core.domain.Local;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.gamesetup.TheCraftCloudGameSetupPlugin;

public class PlayerOnClick implements Listener {

	private TheCraftCloudGameSetupPlugin controller;
	
    public PlayerOnClick(TheCraftCloudGameSetupPlugin plugin) {
		super();
		this.controller = plugin;
	}


    @EventHandler
    public void onClick(PlayerInteractEvent event) {

    	Bukkit.getConsoleSender().sendMessage("&6onClick");
    	
    	if(controller.getSetupArena()) {
        	Bukkit.getConsoleSender().sendMessage("&6onClick - getSetupArena()");
    		controller.onPlayerClickSetupArena(event);
    		return;
    	} 
    	
    	Bukkit.getConsoleSender().sendMessage("&6onClick - ifs");
    	
    	if(event.getAction() == Action.LEFT_CLICK_BLOCK ) {
    		Block block = event.getClickedBlock();
    		//blockDetails(block, event.getPlayer());
    		controller.setSelectionPointA(event.getClickedBlock().getLocation());
    		Local l = controller.getSelection().getPointA();
    	}
        
    	if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
    		Block block = event.getClickedBlock();
    		//blockDetails(block, event.getPlayer());
    		controller.setSelectionPointB(event.getClickedBlock().getLocation());
    		Local l = controller.getSelection().getPointB();
    		MaterialData data = block.getState().getData();

			if(block.getType() == Material.LEVER) {
	    		updateHologram( block, (Lever)block.getState().getData(), event.getPlayer() );
	    		block.getState().update();
    		}
			
			if(block.getType() == Material.SPRUCE_DOOR) {
				this.controller.saveGameConfig();
			}
    	}
    	
    }
    
    private void updateHologram(Block block, Lever lever, Player player) {
    	
    	Bukkit.getConsoleSender().sendMessage("&6updateHologram");
    	
		if(this.controller.getConfigValue() == null) {
			this.controller.setConfigValue(new Integer(0) );
		}

    	Bukkit.getConsoleSender().sendMessage( Utils.color("&6updateHologram - getConfigValue: " + this.controller.getConfigValue() ) );

		Integer configValue = Integer.parseInt(this.controller.getConfigValue().toString());
		// adicionar 1 int a configuracao atual
		if(this.sameLocation( block.getLocation(), controller.getBlockUp() ) ) {
			configValue++;
			this.controller.setConfigValue(configValue);
			this.controller.updateConfigHologram(player);
		} else if (this.sameLocation( block.getLocation(), controller.getBlockDown())) {
			if(configValue > 0) {
				configValue --;
			}
			this.controller.setConfigValue(configValue);
			this.controller.updateConfigHologram(player);
		}
		
		//mudar de configuração
		if( this.sameLocation( block.getLocation(), controller.getBlockNextConfig() ) ) {
			this.controller.nextConfig(player);
		} else if ( this.sameLocation( block.getLocation(), controller.getBlockPreviousConfig() ) ) {
			this.controller.previousConfig(player);
		}
		
		// iniciar configuracao da arena
		if( this.sameLocation( block.getLocation(), controller.getBlockStartArenaSetup() ) ) {
			this.controller.startArenaSetupTask();
		}

		// cancelar configuracao da arena
		if( this.sameLocation( block.getLocation(), controller.getBlockCancelSetup() )) {
			this.controller.cancelArenaSetupTask();
		}

		//mudar horario da arena
		if( this.sameLocation( block.getLocation(), controller.getBlockChangeTime() ) ) {
			this.controller.switchArenaTime();
		}

	}


	private void blockDetails(Block block, Player player) {
    	
    	if(block.getType() == Material.QUARTZ_STAIRS) {
    		player.sendMessage("data " + block.getData());
    		player.sendMessage("data " + block.getTypeId() );
    		player.sendMessage("state data " + block.getState().getData());
			Stairs stairs = (Stairs) block.getState().getData();
    		player.sendMessage("facing " + stairs.getFacing() + " inverted " + stairs.isInverted() );
    		

    	}
    	
		try{
			Field field = block.getState().getData().getClass().getDeclaredField("BlockFace");
			BlockFace face = (BlockFace)field.get(BlockFace.class);
			player.sendMessage("face " + face.name() );
		} catch(Exception e) {
			
		}
		
		try{
			Field field = block.getState().getData().getClass().getDeclaredField("facing");
			BlockFace face = (BlockFace)field.get(BlockFace.class);
			player.sendMessage("facing " + face.name() );
		} catch(Exception e) {
			
		}
		
		try{
			Field[] fields = block.getState().getData().getClass().getDeclaredFields();
			player.sendMessage("fields " + fields.length );
			for(Field field: fields) {
				player.sendMessage("field: " + field.getName() + " " + field.getType());
				Object obj = null;
				player.sendMessage(field.get(obj).toString());
			}
		} catch(Exception e) {
			
		}
		
    	
    }
	
	private boolean sameLocation( Location l1, Location l2 ) {
		if(l1.getBlockX() != l2.getBlockX()) return false;
		if(l1.getBlockY() != l2.getBlockY()) return false;
		if(l1.getBlockZ() != l2.getBlockZ()) return false;
		return true;
	}

}