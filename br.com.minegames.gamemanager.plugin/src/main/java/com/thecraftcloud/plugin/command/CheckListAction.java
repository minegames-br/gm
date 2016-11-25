package com.thecraftcloud.plugin.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.plugin.TheCraftCloudPlugin;

public class CheckListAction extends TheCraftCloudCommandAction {

	public CheckListAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	public void execute() {
		Bukkit.getLogger().info("Executando commando Checklist " + this.commandSender + " "
				+ "\n" + this.command 
				+ "\n" + this.arg2
				+ "\n" + this.arguments);
		
		Player player = null; 
		if( this.commandSender instanceof Player ) {
			player = (Player)commandSender;
		}

		TheCraftCloudPlugin p = (TheCraftCloudPlugin)plugin;
		if(p.isServerRegistered()) {
			player.sendMessage("Please, register server first. /mg register <name>");
		}

		
		if(this.config.getGame() == null) {
			player.sendMessage("Game is not selected");
		}
		
		if(this.config.getArena() == null) {
			player.sendMessage("Arena is not selected");
		}
		
		if(this.config.getArena().getArea() == null) {
			player.sendMessage("Arena Area3D is not set up");
		}
		
		if(this.config.getArena().getArea().getPointA() == null) {
			player.sendMessage("Arena Area3D/PointA is not set up");
		}
		
		if(this.config.getArena().getArea().getPointB() == null) {
			player.sendMessage("Arena Area3D/PointB is not set up");
		}
	}
}
