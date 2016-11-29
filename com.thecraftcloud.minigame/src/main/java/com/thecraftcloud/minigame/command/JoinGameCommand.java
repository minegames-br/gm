package com.thecraftcloud.minigame.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.Local;
import com.thecraftcloud.core.logging.MGLogger;
import com.thecraftcloud.minigame.TheCraftCloudConfig;
import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.service.ConfigService;

public class JoinGameCommand implements CommandExecutor {

	private TheCraftCloudMiniGameAbstract controller;
	private ConfigService configService = ConfigService.getInstance();
	private TheCraftCloudConfig config = configService.getConfig();

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
        Bukkit.getLogger().info( "Controler " + controller );
        Bukkit.getLogger().info( "Game " + this.configService.getMyCloudCraftGame() );
        Bukkit.getLogger().info( "Live players" + controller.getLivePlayers() );
        if(this.configService.getMyCloudCraftGame().isWaitingPlayers() && controller.getLivePlayers() != null && controller.getLivePlayers().size() > 0) {
            controller.addPlayer(player);
        } else {
	        
    		Arena arena = this.config.getArena();
    		Game game = this.config.getGame();
    		
    		if(arena == null || arena.getArea() == null || arena.getArea().getPointA() == null || arena.getArea().getPointB() == null) {
    			player.sendMessage("Game is not correctly configured yet. Have you selected the game and arena? If so, try /tcc setup");
    			return false;
    		}
    		
    		//quando o jogo acabar, mandar o jogador de volta pra onde estava
    		Local l = new Local();
    		Location loc = player.getLocation();
    		l.setX(loc.getBlockX());
    		l.setY(loc.getBlockY());
    		l.setZ(loc.getBlockZ());
    		l.setName( player.getWorld().getName() );
    		controller.init( ); //setGameArenaConfigList( delegate.getInstance().findAllGameConfigArenaByGameArena(game.getGame_uuid().toString(), arena.getArena_uuid().toString() ) );
            controller.addPlayer(player);
        }
        
        return true;
    }

}