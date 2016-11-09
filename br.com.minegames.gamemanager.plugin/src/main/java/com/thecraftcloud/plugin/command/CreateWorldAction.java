package com.thecraftcloud.plugin.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.core.command.CommandAction;
import com.thecraftcloud.plugin.TheCraftCloudPlugin;

public class CreateWorldAction extends CommandAction {

	public CreateWorldAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	@Override
	public void execute() {
		Bukkit.getLogger().info("Executando commando Create World " + this.commandSender + " "
				+ "\n" + this.command 
				+ "\n" + this.arg2
				+ "\n" + this.arguments);
		
		Player player = null; 
		if( this.commandSender instanceof Player ) {
			player = (Player)commandSender;
		}
		
		TheCraftCloudPlugin p = (TheCraftCloudPlugin)plugin;
		long time = System.currentTimeMillis();

		WorldCreator wc = new WorldCreator("palace");
		World world = Bukkit.createWorld(wc);
		player.teleport(new Location(world, 0, 0, 0));
	}

}
