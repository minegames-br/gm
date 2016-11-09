package com.thecraftcloud.plugin.command;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.export.ExportBlock;
import com.thecraftcloud.core.logging.MGLogger;
import com.thecraftcloud.core.util.BlockManipulationUtil;
import com.thecraftcloud.plugin.TheCraftCloudPlugin;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;

public class JoinGameCommand implements CommandExecutor {

	private TheCraftCloudMiniGameAbstract controller;

    public JoinGameCommand(TheCraftCloudMiniGameAbstract plugin) {
		super();
		this.controller = plugin;
	}

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
    	
    	MGLogger.debug("JoinGameCommand onCommand - " + commandSender.getName() + " " + command.getName() + " " + label + " " + args);
    	
        if (!(commandSender instanceof Player)) {
        	MGLogger.debug(commandSender + " - commando somente para players");
            return false;
        }
        
        Player player = (Player) commandSender;
        if(controller.getMyCloudCraftGame().isWaitingPlayers() && controller.getLivePlayers() != null && controller.getLivePlayers().size() > 0) {
        	if(args != null || args.length > 0) {
        		player.sendMessage("Uma arena está ativa. Vou te mandar pra lá.");
        	}
            controller.addPlayer(player);
        } else {
	        
    		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();
    		TheCraftCloudPlugin mgplugin = (TheCraftCloudPlugin)Bukkit.getPluginManager().getPlugin(TheCraftCloudPlugin.THE_CRAFT_CLOUD_PLUGIN);
    		
    		if(!mgplugin.isGameReady() || !controller.isGameReady() ) {
    			player.sendMessage("Game is not yet ready. Run the check list to find out what is missing /tcc checklist");
    			return false;
    		}
    		
    		Arena arena = mgplugin.getArena();
    		Game game = mgplugin.getGame();
    		
    		if(arena == null || arena.getArea() == null || arena.getArea().getPointA() == null || arena.getArea().getPointB() == null) {
    			player.sendMessage("Game is not correctly configured yet. Have you selected the game and arena? If so, try /tcc setup");
    			return false;
    		}
    		
    		controller.init( player.getWorld(), mgplugin.getServerInstance().getLobby() ); //setGameArenaConfigList( delegate.getInstance().findAllGameConfigArenaByGameArena(game.getGame_uuid().toString(), arena.getArena_uuid().toString() ) );
            controller.addPlayer(player);
        }
        
        return true;
    }

}