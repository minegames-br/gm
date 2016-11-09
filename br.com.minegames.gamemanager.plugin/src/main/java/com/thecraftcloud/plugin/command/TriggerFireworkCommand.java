package com.thecraftcloud.plugin.command;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.thecraftcloud.core.logging.MGLogger;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.core.worldedit.WorldEditWrapper;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;

public class TriggerFireworkCommand implements CommandExecutor {

	private TheCraftCloudMiniGameAbstract game;

    public TriggerFireworkCommand(TheCraftCloudMiniGameAbstract plugin) {
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

        Player player = (Player) commandSender;
        
        File file = new File("c:/Temp/arena2_fire.schematic");
        WorldEditWrapper.loadSchematic(player.getWorld(), file, player.getLocation());
        
        
        Utils.shootFirework(player);
        
        return true;
    }

}