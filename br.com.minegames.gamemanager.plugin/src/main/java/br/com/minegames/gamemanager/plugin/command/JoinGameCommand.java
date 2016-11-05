package br.com.minegames.gamemanager.plugin.command;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.minegames.core.domain.Arena;
import br.com.minegames.core.domain.Game;
import br.com.minegames.core.export.ExportBlock;
import br.com.minegames.core.logging.MGLogger;
import br.com.minegames.core.util.BlockManipulationUtil;
import br.com.minegames.gamemanager.client.GameManagerDelegate;
import br.com.minegames.gamemanager.plugin.MineGamesPlugin;
import br.com.minegames.gamemanager.plugin.MyCloudCraftPlugin;

public class JoinGameCommand implements CommandExecutor {

	private MyCloudCraftPlugin controller;

    public JoinGameCommand(MyCloudCraftPlugin plugin) {
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
        if(controller.getLivePlayers() != null && controller.getLivePlayers().size() > 0) {
        	if(args != null || args.length > 0) {
        		player.sendMessage("Uma arena está ativa. Vou te mandar pra lá.");
        	}
            controller.addPlayer(player);
        } else {
	        
    		GameManagerDelegate delegate = GameManagerDelegate.getInstance();
    		MineGamesPlugin mgplugin = (MineGamesPlugin)Bukkit.getPluginManager().getPlugin("MGPlugin");
    		Arena arena = mgplugin.getArena();
    		Game game = mgplugin.getGame();
    		
    		if(arena.getArea() == null || arena.getArea().getPointA() == null || arena.getArea().getPointB() == null) {
    			player.sendMessage("Game is not correctly configured yet. Try /mg setup");
    			return false;
    		}
    		
    		controller.init(); //setGameArenaConfigList( delegate.getInstance().findAllGameConfigArenaByGameArena(game.getGame_uuid().toString(), arena.getArena_uuid().toString() ) );
            controller.addPlayer(player);
        }
        
        return true;
    }

}