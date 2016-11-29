package com.thecraftcloud.gamesetup.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.thecraftcloud.gamesetup.TheCraftCloudGameSetupPlugin;

public class BlockBreakListener implements Listener {

	private TheCraftCloudGameSetupPlugin plugin;
	
	public BlockBreakListener(TheCraftCloudGameSetupPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
		if(plugin.getSetupArena()) {
		    event.setCancelled(true);
		}
    }

}
