package br.com.minegames.gamemanager.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.minegames.gamemanager.plugin.command.MineGamesCommand;

public class MineGamesPlugin extends JavaPlugin {

    private YamlConfiguration configFile;
	private File file = new File(getDataFolder(), "minegames.yml");
	
	@Override
	public void onEnable() {
		getCommand("mg").setExecutor(new MineGamesCommand(this));
	    if(!file.exists()){
	        file.getParentFile().mkdirs();
	        try {
				file.createNewFile();
				this.configFile = YamlConfiguration.loadConfiguration(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    } else {
			this.configFile = YamlConfiguration.loadConfiguration(file);
	    }
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
