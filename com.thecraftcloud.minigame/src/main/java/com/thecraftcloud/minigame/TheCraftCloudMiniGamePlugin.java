package com.thecraftcloud.minigame;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.minigame.command.LeaveGameCommand;

public class TheCraftCloudMiniGamePlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage(Utils.color("&5[TheCraftCloudMiniGamePlugin] Enabling TheCraftCloudMiniGamePlugin"));		
	}
	
	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(Utils.color("&5[TheCraftCloudMiniGamePlugin] Disabling TheCraftCloudMiniGamePlugin"));
	}
	
}
