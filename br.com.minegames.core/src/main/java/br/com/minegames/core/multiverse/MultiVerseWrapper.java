package br.com.minegames.core.multiverse;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import com.onarandombox.MultiverseCore.MultiverseCore;

public class MultiVerseWrapper {
	
	public static World cloneWorld(JavaPlugin plugin, String oldName, String newName) {
		MultiverseCore mvc = (MultiverseCore)Bukkit.getPluginManager().getPlugin("Multiverse-Core");
		mvc.cloneWorld(oldName, newName, "VoidWorld");
		mvc.getMVWorldManager().loadWorld( newName );
		return Bukkit.getWorld( newName );
	}

}
