package com.thecraftcloud.gamesetup.command;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.multiverse.MultiVerseWrapper;
import com.thecraftcloud.core.util.zip.ExtractZipContents;
import com.thecraftcloud.gamesetup.TheCraftCloudGameSetupPlugin;

public class DefineArenaAction extends TheCraftCloudCommandAction {

	public DefineArenaAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	public void execute() {
		Bukkit.getLogger().info("Executando commando Set Arena " + this.commandSender + " "
				+ "\n" + this.command 
				+ "\n" + this.arg2
				+ "\n" + this.arguments);
		
		Player player = null; 
		if( this.commandSender instanceof Player ) {
			player = (Player)commandSender;
		}

		TheCraftCloudGameSetupPlugin p = (TheCraftCloudGameSetupPlugin)plugin;
		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();
		if(p.isServerRegistered()) {
			if(player != null) {
				player.sendMessage("Please, register server first. /mg register <name>");
				return;
			}
		}
		
		if(p.getArenas() == null || p.getArenas().size() == 0) {
			if(player != null) {
				player.sendMessage("Find available arenas: /mg listarenas [name]");
				return;
			}
		}
		
		String sIndexArena = arguments[1];
		player.sendMessage("You have chosen: " + sIndexArena);
		try{
			Integer indexArena = Integer.parseInt(sIndexArena);
			if((indexArena-1) > p.getArenas().size()) {
				player.sendMessage("You have " + p.getArenas().size() + " arenas that matched your query.");
				return;
			}
			Arena arena = p.getArenas().get(indexArena);
			p.setArena(arena);
			player.sendMessage("Downloading arena: " + arena.getName() );
			
			File dir = Bukkit.getWorldContainer();
			
			File zipFile = delegate.downloadArenaWorld(arena, dir );
			player.sendMessage("Preparing arena..." );
			ExtractZipContents.unzip(zipFile);
			//WorldCreator wc = new WorldCreator( dir.getAbsolutePath() + "/" + arena.getName() );
			//wc.createWorld();
			MultiVerseWrapper mvw = new MultiVerseWrapper();
			mvw.addWorld(p, arena);
			player.sendMessage("Arena: " + arena.getName() + " is ready.");
		}catch(Exception e) {
			e.printStackTrace();
			player.sendMessage("Try again. You have " + p.getArenas().size() + " arenas that matched your query.");
		}
		
	}
}
