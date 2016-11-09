package com.thecraftcloud.plugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.thecraftcloud.core.logging.MGLogger;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;

public class StartGameCommand implements CommandExecutor {

	private TheCraftCloudMiniGameAbstract game;

    public StartGameCommand(TheCraftCloudMiniGameAbstract plugin) {
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

        game.startCoundDown();
        
        return true;
    }

}