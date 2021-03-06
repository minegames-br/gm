package com.thecraftcloud.gamesetup.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.Area3D;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.gamesetup.TheCraftCloudGameSetupPlugin;

public class DefineAreaAction extends TheCraftCloudCommandAction {

	public DefineAreaAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	public void execute() {
		Bukkit.getLogger().info("Executando commando Define Area " + this.commandSender + " "
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
		Arena arena = p.getArena();
		if(arena == null) {
			if(player != null) {
				player.sendMessage("Find available arenas: /mg listarenas [name]");
				player.sendMessage("Please, set the active Arena first: /mg setarena <#>");
				return;
			}
		}
		
		if(arguments.length < 2) {
			player.sendMessage("Please, send a name for the area. /mg addarea <name> [category]");
			return;
		}
		String areaName = arguments[1];
		
		ServerInstance server = p.getServerInstance();
		Area3D area = p.getSelection();
		area.setName(areaName);
		if(arguments.length == 3) {
			area.setType(arguments[2]);			
		}
		area = delegate.addArea3D(area);
		arena.setArea(area);

		arena = delegate.updateArena(arena);
	}
}
