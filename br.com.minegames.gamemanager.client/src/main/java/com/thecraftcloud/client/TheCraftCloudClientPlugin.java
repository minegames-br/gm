package com.thecraftcloud.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class TheCraftCloudClientPlugin extends JavaPlugin {

    private YamlConfiguration configFile;
	public static final String TCC_GAMEMANAGER_URL = "thecraftcloud.gamemanager.url";
	
	private static String minegamesGameManagerUrl;
	
	@Override
	public void onEnable() {
		this.loadConfiguration();
		Bukkit.getLogger().info("GameManagerClientPlugin - onEnable");

		this.minegamesGameManagerUrl = this.getConfig().getString(this.TCC_GAMEMANAGER_URL);
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
	
	public void loadConfiguration(){
	     this.getConfig().options().copyDefaults(true);
	     this.saveConfig();
	}

	public Properties readServerProperties() {
		FileInputStream in = null;
	    Properties properties = new Properties();
		try {
		    // You can read files using FileInputStream or FileReader.
		    File dir = Bukkit.getWorldContainer();
		    File file = new File(dir, "server.properties");
		    in = new FileInputStream( file );
		    properties.load(in);
		} catch (FileNotFoundException ex) {
	    	ex.printStackTrace();
		} catch (IOException ex) {
	    	ex.printStackTrace();
		} finally {
		    try {
		        if (in != null) in.close();
		    } catch (IOException ex) {
		    	ex.printStackTrace();
		    }
		}
		return properties;
	}

	
}
