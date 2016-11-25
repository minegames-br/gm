package com.thecraftcloud.plugin.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.plugin.TheCraftCloudPlugin;

public class ListGamesAction extends TheCraftCloudCommandAction {

	public ListGamesAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	public void execute() {
		Bukkit.getLogger().info("Executando commando List Games " + this.commandSender + " "
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
		if(arguments.length == 2) {
			filterName = arguments[1];
		}
		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();
		TheCraftCloudPlugin p = (TheCraftCloudPlugin)plugin;
		List<Game> games = delegate.findGames();
		p.setGames(games);
		int i = 0;
		for(Game game: games) {
			player.sendMessage((i++) + " - " + game.getName() );
		}
	}
}
