package com.thecraftcloud.minigame.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

<<<<<<< HEAD
import com.thecraftcloud.core.bungee.BungeeUtils;
import com.thecraftcloud.core.logging.MGLogger;
=======
>>>>>>> branch 'master' of https://github.com/minegames-br/gm.git
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;

public class LeaveGameCommand implements CommandExecutor {

	private TheCraftCloudMiniGameAbstract controller;

    public LeaveGameCommand(TheCraftCloudMiniGameAbstract plugin) {
		super();
		this.controller = plugin;
	}

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
<<<<<<< HEAD
        if (!(commandSender instanceof Player)) {
        	MGLogger.debug(commandSender + " - commando somente para players");
            return false;
        }

=======
>>>>>>> branch 'master' of https://github.com/minegames-br/gm.git
        Bukkit.getConsoleSender().sendMessage(Utils.color("&6comando sair sendo executado"));
        
        Player player = (Player) commandSender;
        Bukkit.getConsoleSender().sendMessage(Utils.color("&6remove live player: " + player.getName() ));
        controller.removeLivePlayer(player);
        
        return true;
    }

}