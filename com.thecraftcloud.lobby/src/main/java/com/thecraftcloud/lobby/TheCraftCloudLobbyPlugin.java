package com.thecraftcloud.lobby;


import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.lobby.listener.InteractNpcListener;
import com.thecraftcloud.lobby.listener.InteractSignListener;
import com.thecraftcloud.lobby.listener.PlayerFlightEventListener;
import com.thecraftcloud.lobby.listener.PlayerJoinListener;
import com.thecraftcloud.lobby.listener.ServerListener;

public class TheCraftCloudLobbyPlugin extends JavaPlugin {

	@Override
	public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(Utils.color("&6Enabling TheCraftCloudLobbyPlugin"));

        this.registerListeners();
        
        Bukkit.getConsoleSender().sendMessage(Utils.color("&6TheCraftCloudLobbyPlugin Enabled Successfully"));
	}
	
	protected void registerListeners() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new InteractSignListener(this), this );
		pm.registerEvents(new InteractNpcListener(this), this);
		pm.registerEvents(new PlayerFlightEventListener(this), this );
		pm.registerEvents(new ServerListener(this), this );
		pm.registerEvents(new PlayerJoinListener(this), this );
	}
	
	
}
