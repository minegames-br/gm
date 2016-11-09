package com.thecraftcloud.plugin.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.command.CommandAction;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.plugin.TheCraftCloudPlugin;

public class ListArenasAction extends CommandAction {

	public ListArenasAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	public void execute() {
		Bukkit.getLogger().info("Executando commando List Arenas " + this.commandSender + " "
				+ "\n" + this.command 
				+ "\n" + this.arg2
				+ "\n" + this.arguments);
		
		Player player = null; 
		if( this.commandSender instanceof Player ) {
			player = (Player)commandSender;
		}else{
			return;
		}

		String filterName = "";
		if(arguments.length > 0) {
			filterName = arguments[0];
		}
		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();
		TheCraftCloudPlugin p = (TheCraftCloudPlugin)plugin;
		List<Arena> arenas = delegate.findArenas(filterName);
		p.setArenas(arenas);
		int i = 0;
		for(Arena arena: arenas) {
			player.sendMessage((i++) + " - " + arena.getName() );
		}
	}
}
