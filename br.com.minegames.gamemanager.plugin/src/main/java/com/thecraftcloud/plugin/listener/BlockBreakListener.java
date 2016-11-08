package com.thecraftcloud.plugin.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.thecraftcloud.plugin.MineGamesPlugin;

public class BlockBreakListener implements Listener {

	private MineGamesPlugin plugin;
	
	public BlockBreakListener(MineGamesPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
		if(plugin.getSetupArena()) {
		    event.setCancelled(true);
		}
    }

}
