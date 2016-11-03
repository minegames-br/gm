package br.com.minegames.gamemanager.plugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.minegames.core.logging.MGLogger;
import br.com.minegames.gamemanager.plugin.MyCloudCraftPlugin;

public class StartGameCommand implements CommandExecutor {

	private MyCloudCraftPlugin game;

    public StartGameCommand(MyCloudCraftPlugin plugin) {
		super();
		this.game = plugin;
	}

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
    	
    	MGLogger.debug(commandSender.getName() + " " + command.getName() + " " + label + " " + args);
    	
        if (!(commandSender instanceof Player)) {
        	MGLogger.debug(commandSender + " - commando somente para players");
            return false;
        }

        game.startGameEngine();
        
        return true;
    }

}