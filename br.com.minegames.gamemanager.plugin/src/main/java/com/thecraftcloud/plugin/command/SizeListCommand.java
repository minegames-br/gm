package com.thecraftcloud.plugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.plugin.service.PlayerManager;

public class SizeListCommand implements CommandExecutor {
	
	private JavaPlugin plugin;
	
	public SizeListCommand(JavaPlugin plugin) {
		super();
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		Player player = (Player) sender;
		PlayerManager playerManager = PlayerManager.getInstance();
		if (CommandLabel.equalsIgnoreCase("playerlist")) {
			player.sendMessage("Current list size: " + playerManager.getPlayerList().size() + " player(s) online in the server");
		}
		return false;
		
	}

}
