package com.thecraftcloud.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class GameManagerClientPlugin extends JavaPlugin {

    private YamlConfiguration configFile;
	private File file = new File(getDataFolder(), "minegames-config.yml");
	public static final String MINEGAMES_GAMEMANAGER_URL = "minegames.gamemanager.url";
	
	private static String minegamesGameManagerUrl;
	
	@Override
	public void onEnable() {
		Bukkit.getLogger().info("GameManagerClientPlugin - onEnable");

	    if(!file.exists()){
	        file.getParentFile().mkdirs();
	        try {
				file.createNewFile();
				this.configFile = YamlConfiguration.loadConfiguration(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        Bukkit.getLogger().info("Config file loaded: " + this.file.getAbsolutePath() );
	    } else {
			this.configFile = YamlConfiguration.loadConfiguration(file);
	        Bukkit.getLogger().info("Config file loaded: " + this.file.getAbsolutePath() );
	    }

		this.minegamesGameManagerUrl = this.configFile.getString(this.MINEGAMES_GAMEMANAGER_URL);
        Bukkit.getLogger().info("URL: " + this.minegamesGameManagerUrl );
	}

	public static String getMinegamesGameManagerUrl() {
		return minegamesGameManagerUrl;
	}

	public static void setMinegamesGameManagerUrl(String _minegamesGameManagerUrl) {
		minegamesGameManagerUrl = _minegamesGameManagerUrl;
	}
	
	public YamlConfiguration getConfigFile() {
		try {
			this.configFile.load(this.file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.configFile;
	}

	public void setConfig(String path, String value) {
		configFile.set(path, value);
		try {
			configFile.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
