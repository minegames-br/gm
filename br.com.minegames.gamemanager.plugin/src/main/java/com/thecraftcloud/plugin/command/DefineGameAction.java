package com.thecraftcloud.plugin.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.command.CommandAction;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.plugin.TheCraftCloudPlugin;

public class DefineGameAction extends CommandAction {

	public DefineGameAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	public void execute() {
		Bukkit.getLogger().info("Executando commando Define Game " + this.commandSender + " "
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
				player.sendMessage("Please, register server first. /tcc register <name>");
				return;
			}
		}
		
		if(arguments.length < 2) {
			player.sendMessage("Please, send game #. List games /tcc listgames then /tcc setgame <#>");
			return;
		}
		int index = Integer.parseInt(arguments[1]);
		Game game = p.getGames().get(index);
		p.setGame(game);
		
		player.sendMessage("You have chosen game: " + game.getName() );
	}
}
