package com.thecraftcloud.lobby;


import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.core.util.Utils;

public class TheCraftCloudLobbyPlugin extends JavaPlugin {

	@Override
	public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(Utils.color("&6Enabling TheCraftCloudLobbyPlugin"));
		
        Bukkit.getConsoleSender().sendMessage(Utils.color("&6TheCraftCloudLobbyPlugin Enabled Successfully"));
	}
	
}
