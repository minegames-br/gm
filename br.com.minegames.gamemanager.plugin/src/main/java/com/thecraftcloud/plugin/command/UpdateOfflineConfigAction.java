package com.thecraftcloud.plugin.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.client.exception.InvalidRegistrationException;
import com.thecraftcloud.plugin.TheCraftCloudPlugin;

public class UpdateOfflineConfigAction extends TheCraftCloudCommandAction {

	public UpdateOfflineConfigAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	@Override
	public void execute() {
		Bukkit.getLogger().info("Executando commando Update Offline Config " + this.commandSender + " "
				+ "\n" + this.command 
				+ "\n" + this.arg2
				+ "\n" + this.arguments);
		
		Player player = null; 
		if( this.commandSender instanceof Player ) {
			player = (Player)commandSender;
		}
		
		TheCraftCloudPlugin p = (TheCraftCloudPlugin)plugin;
		
		try {
			this.configService.loadTheCraftCloudData(plugin, true);
		} catch (InvalidRegistrationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
