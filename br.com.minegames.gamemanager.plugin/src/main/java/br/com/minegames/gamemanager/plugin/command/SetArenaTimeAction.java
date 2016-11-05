package br.com.minegames.gamemanager.plugin.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.minegames.core.command.CommandAction;
import br.com.minegames.core.domain.Arena;
import br.com.minegames.gamemanager.client.GameManagerDelegate;
import br.com.minegames.gamemanager.plugin.MineGamesPlugin;

public class SetArenaTimeAction extends CommandAction {

	public SetArenaTimeAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	public void execute() {
		Bukkit.getLogger().info("Executando commando Set Lobby " + this.commandSender + " "
				+ "\n" + this.command 
				+ "\n" + this.arg2
				+ "\n" + this.arguments);
		
		Player player = null; 
		if( this.commandSender instanceof Player ) {
			player = (Player)commandSender;
		}

		MineGamesPlugin p = (MineGamesPlugin)plugin;
		if(p.isServerRegistered()) {
			if(player != null) {
				player.sendMessage("Please, register server first. /mg register <name>");
				return;
			}
		}
		
		GameManagerDelegate delegate = GameManagerDelegate.getInstance();

		try{
			p.getArena().setTime(Integer.parseInt(arguments[2]));
		}catch(Exception e) {
			e.printStackTrace();
			player.sendMessage("Invalid time. Try /mg settime 10000 or /mg settime 13000" );
			return;
		}
		
		Arena arena = delegate.updateArena(p.getArena());
		p.setArena(arena);
		
		player.sendMessage("Time Set for the Arena: " + arena.getName() );
	}
}
