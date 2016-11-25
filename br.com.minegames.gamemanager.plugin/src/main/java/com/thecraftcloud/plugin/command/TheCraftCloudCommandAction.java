package com.thecraftcloud.plugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.core.command.CommandAction;
import com.thecraftcloud.plugin.TheCraftCloudConfig;
import com.thecraftcloud.plugin.service.ConfigService;

public abstract class TheCraftCloudCommandAction extends CommandAction {

	protected ConfigService configService = ConfigService.getInstance();
	protected TheCraftCloudConfig config = configService.getConfig();

	public TheCraftCloudCommandAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2,
			String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}


}
