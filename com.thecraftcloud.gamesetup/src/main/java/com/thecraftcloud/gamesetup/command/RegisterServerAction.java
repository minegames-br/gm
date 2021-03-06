package com.thecraftcloud.gamesetup.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.gamesetup.TheCraftCloudGameSetupPlugin;

public class RegisterServerAction extends TheCraftCloudCommandAction {

	
	public RegisterServerAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	public void execute() {
		Bukkit.getLogger().info("Executando commando Register Server " + this.commandSender + " "
				+ "\n" + this.command 
				+ "\n" + this.arg2
				+ "\n" + this.arguments);
		
		Player player = null; 
		if( this.commandSender instanceof Player ) {
			player = (Player)commandSender;
		}

		TheCraftCloudGameSetupPlugin p = (TheCraftCloudGameSetupPlugin)this.plugin;
		String value = p.getServer_uuid();
		if(p.isServerRegistered()) {
			if(player != null) {
				player.sendMessage("Server already registered: " + value);
			}else{
				Bukkit.getLogger().info("Server already registered: " + value);
			}
			return;
		}
		
		if(arguments.length != 2) {
			player.sendMessage("syntax: /tcc register <servername>");
			return;
		}

		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();
		ServerInstance server = delegate.registerServer(arguments[1]);
		p.setServer(server);
		value = server.getServer_uuid().toString();
		
		if(player!= null) {
			player.sendMessage("Server registered successfully: "+ value );
		}else{
			Bukkit.getLogger().info("Server registered successfully: " + value );
		}
	}
	
}
