package com.thecraftcloud.admin.service;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;

public class PluginService {
	
	public void disableTheCraftCloudMiniGames() {
		Plugin[] plugins = Bukkit.getPluginManager().getPlugins();

		boolean success = false;
		for(Plugin plugin: plugins) {
			if(! (plugin instanceof JavaPlugin) ) {
				continue;
			}
			try{
				JavaPlugin javaPlugin = (JavaPlugin)plugin;
				if(javaPlugin instanceof TheCraftCloudMiniGameAbstract) {
					TheCraftCloudMiniGameAbstract miniGame = (TheCraftCloudMiniGameAbstract)javaPlugin;
					Bukkit.getPluginManager().disablePlugin(miniGame);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}


}
