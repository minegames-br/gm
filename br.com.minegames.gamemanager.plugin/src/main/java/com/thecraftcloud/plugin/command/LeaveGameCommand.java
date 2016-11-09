package com.thecraftcloud.plugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.thecraftcloud.core.logging.MGLogger;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;

public class LeaveGameCommand implements CommandExecutor {

	private TheCraftCloudMiniGameAbstract controller;

    public LeaveGameCommand(TheCraftCloudMiniGameAbstract plugin) {
		super();
		this.controller = plugin;
	}

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
        	MGLogger.debug(commandSender + " - commando somente para players");
            return false;
        }

        Player player = (Player) commandSender;
        controller.removeLivePlayer(player);
        
        return true;
    }

}