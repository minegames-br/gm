package com.thecraftcloud.gamesetup.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.core.command.CommandAction;

public abstract class TheCraftCloudCommandAction extends CommandAction {

	public TheCraftCloudCommandAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2,
			String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}


}
