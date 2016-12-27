package com.thecraftcloud.core.multiverse;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.plugin.java.JavaPlugin;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.util.zip.ExtractZipContents;

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

	public World addWorld(JavaPlugin plugin, Arena arena) {
		File dir = Bukkit.getWorldContainer();
		
		//File schematicFile = delegate.downloadArenaSchematic(arena.getArena_uuid(), dir.getAbsolutePath());
		File arenaWorldFile = new File(dir, arena.getName() );
		
		MultiverseCore mvplugin = (MultiverseCore)Bukkit.getPluginManager().getPlugin("Multiverse-Core");
		String worldPath = dir.getAbsolutePath() + "/" + arena.getName();
				
		File zipFile = new File( dir, arena.getName() + ".zip");
		ExtractZipContents.unzip(zipFile);
		Bukkit.getLogger().info("world path: " + worldPath);
		mvplugin.getCore().getMVWorldManager().addWorld(arena.getName(), Environment.NORMAL, new Integer( arena.getName().hashCode() ).toString(), WorldType.NORMAL, new Boolean(false), null, false);
		return Bukkit.getWorld(arena.getName());
	}

	public void unloadWorld(World world) {
		MultiverseCore mvplugin = (MultiverseCore)Bukkit.getPluginManager().getPlugin("Multiverse-Core");
		mvplugin.getCore().getMVWorldManager().unloadWorld(world.getName());
		
	}

	public void deleteWorld(World world) {
		MultiverseCore mvplugin = (MultiverseCore)Bukkit.getPluginManager().getPlugin("Multiverse-Core");
		mvplugin.getCore().getMVWorldManager().deleteWorld(world.getName());
		
		//Apagar o diretório
		try {
			FileUtils.deleteDirectory(world.getWorldFolder());
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		
	}

	public void unloadWorld(String name) {

		MultiverseCore mvplugin = (MultiverseCore)Bukkit.getPluginManager().getPlugin("Multiverse-Core");
		mvplugin.getCore().getMVWorldManager().unloadWorld(name);
		
	}

}
