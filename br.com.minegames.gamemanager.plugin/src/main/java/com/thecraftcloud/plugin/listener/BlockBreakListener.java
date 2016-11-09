package com.thecraftcloud.plugin.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.thecraftcloud.plugin.TheCraftCloudPlugin;

public class BlockBreakListener implements Listener {

	private TheCraftCloudPlugin plugin;
	
	public BlockBreakListener(TheCraftCloudPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
		if(plugin.getSetupArena()) {
		    event.setCancelled(true);
		}
    }

}
