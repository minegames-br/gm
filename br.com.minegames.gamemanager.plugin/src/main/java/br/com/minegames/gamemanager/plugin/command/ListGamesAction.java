package br.com.minegames.gamemanager.plugin.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.minegames.core.command.CommandAction;
import br.com.minegames.core.domain.Area3D;
import br.com.minegames.core.domain.Game;
import br.com.minegames.gamemanager.client.GameManagerDelegate;
import br.com.minegames.gamemanager.plugin.MineGamesPlugin;

public class ListGamesAction extends CommandAction {

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
		GameManagerDelegate delegate = GameManagerDelegate.getInstance();
		MineGamesPlugin p = (MineGamesPlugin)plugin;
		List<Game> games = delegate.findGames();
		p.setGames(games);
		int i = 0;
		for(Game game: games) {
			player.sendMessage((i++) + " - " + game.getName() );
		}
	}
}
