package br.com.minegames.gamemanager.plugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.minegames.core.logging.MGLogger;
import br.com.minegames.gamemanager.plugin.MyCloudCraftPlugin;

public class LeaveGameCommand implements CommandExecutor {

	private MyCloudCraftPlugin controller;

    public LeaveGameCommand(MyCloudCraftPlugin plugin) {
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