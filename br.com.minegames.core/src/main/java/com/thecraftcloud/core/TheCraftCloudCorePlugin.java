package com.thecraftcloud.core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.core.util.Utils;

public class TheCraftCloudCorePlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage(Utils.color("&5[TheCraftCloudCorePlugin] Enabling TheCraftCloudCorePlugin"));
	}
	
	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(Utils.color("&5[TheCraftCloudCorePlugin] Disabling TheCraftCloudCorePlugin"));
	}
}
