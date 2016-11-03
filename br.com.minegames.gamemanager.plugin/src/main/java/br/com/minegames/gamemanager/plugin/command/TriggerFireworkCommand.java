package br.com.minegames.gamemanager.plugin.command;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.minegames.core.logging.MGLogger;
import br.com.minegames.core.util.Utils;
import br.com.minegames.core.worldedit.WorldEditWrapper;
import br.com.minegames.gamemanager.plugin.MyCloudCraftPlugin;

public class TriggerFireworkCommand implements CommandExecutor {

	private MyCloudCraftPlugin game;

    public TriggerFireworkCommand(MyCloudCraftPlugin plugin) {
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