package br.com.minegames.gamemanager.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.minegames.core.domain.Area3D;
import br.com.minegames.core.domain.Arena;
import br.com.minegames.core.domain.Local;
import br.com.minegames.gamemanager.plugin.command.MineGamesCommand;
import br.com.minegames.gamemanager.plugin.listener.PlayerOnClick;

public class MineGamesPlugin extends JavaPlugin {

    private YamlConfiguration configFile;
	private File file = new File(getDataFolder(), "minegames.yml");
	
	private Area3D selection;
	private String server_uuid;
	private Arena arena;
	
	private List<Arena> arenas;
	
	public List<Arena> getArenas() {
		return arenas;
	}

	public void setArenas(List<Arena> arenas) {
		this.arenas = arenas;
	}

	public void setArena(Arena arena) {
		this.arena = arena;
	}

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

	    registerListeners();
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
	
	private void registerListeners() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerOnClick(this), this);
	}

	public Area3D getSelection() {
		return selection;
	}

	public void setSelection(Area3D selection) {
		this.selection = selection;
	}
	
	public void setSelectionPointA(Location l) {
		Local local = new Local(l.getBlockX(), l.getBlockY(), l.getBlockZ());
		selection.setPointA(local);
	}

	public void setSelectionPointB(Location l) {
		Local local = new Local(l.getBlockX(), l.getBlockY(), l.getBlockZ());
		selection.setPointB(local);
	}

	public String getServer_uuid() {
		return this.server_uuid;
	}

	public boolean isServerRegistered() {
		YamlConfiguration config = this.getConfigFile();
		String value = config.getString("minegames.server.uuid");
		Bukkit.getLogger().info("minegames.server.uuid: " + value);
		if(value != null && !value.trim().equals("")) {
			return false;
		}
		
		return false;
	}

	public Arena getArena() {
		return this.arena;
	}
}
