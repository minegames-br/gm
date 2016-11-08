package com.thecraftcloud.core.npcs;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Main {
	
	public static void init(Plugin plugin) {
		Bukkit.getPluginManager().registerEvents(new JoinListener(), plugin);

	}
}