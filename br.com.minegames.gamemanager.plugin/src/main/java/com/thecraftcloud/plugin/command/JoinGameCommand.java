package com.thecraftcloud.plugin.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.Local;
import com.thecraftcloud.core.logging.MGLogger;
import com.thecraftcloud.plugin.TheCraftCloudConfig;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.plugin.TheCraftCloudPlugin;
import com.thecraftcloud.plugin.service.ConfigService;

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
        Bukkit.getLogger().info( "Game " + controller.getMyCloudCraftGame() );
        Bukkit.getLogger().info( "Live players" + controller.getLivePlayers() );
        if(controller.getMyCloudCraftGame().isWaitingPlayers() && controller.getLivePlayers() != null && controller.getLivePlayers().size() > 0) {
            controller.addPlayer(player);
        } else {
	        
    		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();
    		TheCraftCloudPlugin mgplugin = (TheCraftCloudPlugin)Bukkit.getPluginManager().getPlugin(TheCraftCloudPlugin.THE_CRAFT_CLOUD_PLUGIN);
    		
    		if(!mgplugin.isGameReady() || !controller.isGameReady() ) {
    			player.sendMessage("Game is not yet ready. Run the check list to find out what is missing /tcc checklist");
    			return false;
    		}
    		
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
    		controller.init( Bukkit.getWorld(arena.getName()), this.config.getServerInstance().getLobby() ); //setGameArenaConfigList( delegate.getInstance().findAllGameConfigArenaByGameArena(game.getGame_uuid().toString(), arena.getArena_uuid().toString() ) );
            controller.addPlayer(player, l);
        }
        
        return true;
    }

}