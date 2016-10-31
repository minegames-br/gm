package br.com.minegames.gamemanager.plugin.listener;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
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
    	
    	if(controller.getSetupArena()) {
    		controller.onPlayerClickSetupArena(event);
    		return;
    	} 
    	
    	if(event.getAction() == Action.LEFT_CLICK_BLOCK ) {
    		Block block = event.getClickedBlock();
    		blockDetails(block, event.getPlayer());
    		controller.setSelectionPointA(event.getClickedBlock().getLocation());
    		Local l = controller.getSelection().getPointA();
    		event.getPlayer().sendMessage("Area 3D PointA selected " + l.getX() + "," + l.getY() + "," + l.getZ() + " block: " + block.getState().getData() );
    	}
        
    	if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
    		Block block = event.getClickedBlock();
    		blockDetails(block, event.getPlayer());
    		controller.setSelectionPointB(event.getClickedBlock().getLocation());
    		Local l = controller.getSelection().getPointB();
    		event.getPlayer().sendMessage("Area 3D PointB selected " + l.getX() + "," + l.getY() + "," + l.getZ() + " block: " + block.getState().getData() );
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
		if(this.controller.getConfigValue() == null) {
			this.controller.setConfigValue(new Integer(0) );
		}
		
		Integer configValue = Integer.parseInt(this.controller.getConfigValue().toString());
		if(block.getLocation().getBlockZ() == 400) {
			configValue++;
			this.controller.setConfigValue(configValue);
			this.controller.updateConfigHologram(player);
		} else if (block.getLocation().getBlockZ() == 403) {
			if(configValue > 0) {
				configValue --;
			}
			this.controller.setConfigValue(configValue);
			this.controller.updateConfigHologram(player);
		}
		
		//mudar de configuração
		if(block.getLocation().getBlockZ() == 399) {
			this.controller.nextConfig(player);
		} else if (block.getLocation().getBlockZ() == 404) {
			this.controller.previousConfig(player);
		}
		
		if(block.getLocation().getBlockZ() == 397) {
			this.controller.startArenaSetupTask();
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

}