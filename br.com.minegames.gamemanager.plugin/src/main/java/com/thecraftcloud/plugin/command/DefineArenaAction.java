package com.thecraftcloud.plugin.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.command.CommandAction;
import com.thecraftcloud.core.domain.Area3D;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.GameWorld;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.plugin.TheCraftCloudPlugin;

public class DefineArenaAction extends CommandAction {

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

		TheCraftCloudPlugin p = (TheCraftCloudPlugin)plugin;
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
			player.sendMessage("You have selected Arena: " + arena.getName() );
		}catch(Exception e) {
			e.printStackTrace();
			player.sendMessage("Try again. You have " + p.getArenas().size() + " arenas that matched your query.");
		}
		
	}
}
