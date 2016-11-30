package com.thecraftcloud.client;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class TheCraftCloudClientPlugin extends JavaPlugin {

    private YamlConfiguration configFile;
	public static final String MINEGAMES_GAMEMANAGER_URL = "thecraftcloud.gamemanager.url";
	
	private static String minegamesGameManagerUrl;
	
	@Override
	public void onEnable() {
		Bukkit.getLogger().info("GameManagerClientPlugin - onEnable");

		this.minegamesGameManagerUrl = this.getConfig().getString(this.MINEGAMES_GAMEMANAGER_URL);
        Bukkit.getLogger().info("URL: " + this.minegamesGameManagerUrl );
	}

	public static String getMinegamesGameManagerUrl() {
		return minegamesGameManagerUrl;
	}

	public static void setMinegamesGameManagerUrl(String _minegamesGameManagerUrl) {
		minegamesGameManagerUrl = _minegamesGameManagerUrl;
	}
	
	public void setConfig(String path, String value) {
		this.getConfig().set(path, value);
	}
	
	
}
