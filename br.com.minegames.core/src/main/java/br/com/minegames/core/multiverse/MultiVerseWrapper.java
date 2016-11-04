package br.com.minegames.core.multiverse;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.plugin.java.JavaPlugin;

import com.onarandombox.MultiverseCore.MultiverseCore;

public class MultiVerseWrapper {
	
	public World cloneWorld(JavaPlugin plugin, String oldName, String newName) {
		MultiverseCore mvc = (MultiverseCore)Bukkit.getPluginManager().getPlugin("Multiverse-Core");
		Bukkit.getLogger().info("MV Clone World - " + "old: " + oldName + " new: " + newName); 
		mvc.cloneWorld(oldName, newName, "VoidWorld");
		mvc.getMVWorldManager().loadWorld( newName );
		return Bukkit.getWorld( newName );
	}
	
	public World createWorldVoid(String worldName) {
		MultiverseCore mvc = (MultiverseCore)Bukkit.getPluginManager().getPlugin("Multiverse-Core");
		
		mvc.getMVWorldManager().addWorld(worldName, World.Environment.NORMAL, worldName,  WorldType.NORMAL, false, "VoidWorld");
		mvc.getMVWorldManager().loadWorld( worldName );
		return Bukkit.getWorld( worldName );
	}

}
