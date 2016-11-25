package com.thecraftcloud.plugin.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.core.util.Utils;

public class BlockInteractListener implements Listener {

	private JavaPlugin plugin;

	public BlockInteractListener(JavaPlugin plugin) {
		super();
		this.plugin = plugin;
	}

	@EventHandler
	public void onInteractBlock(BlockBreakEvent event) {
		Bukkit.getConsoleSender().sendMessage(Utils.color("OnInteractBlock"));
		event.setCancelled(true);
	}
}