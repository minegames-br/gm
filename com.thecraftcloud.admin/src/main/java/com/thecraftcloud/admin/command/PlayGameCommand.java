package com.thecraftcloud.admin.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.thecraftcloud.admin.TheCraftCloudAdmin;
import com.thecraftcloud.admin.action.Action;
import com.thecraftcloud.admin.action.JoinGameAction;
import com.thecraftcloud.admin.action.PrepareGameAction;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameWorld;
import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;

public class PlayGameCommand implements CommandExecutor {

	private TheCraftCloudAdmin controller;
	private TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();

    public PlayGameCommand(TheCraftCloudAdmin plugin) {
		super();
		this.controller = plugin;
	}

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
    	
    	String gameName = args[0];
    	String arenaName = args[1];
    	
		ServerInstance server = delegate.findServerByName("localhost-joao");
		System.out.println(server.getHostname() + " " + server.getIp_address() + "  "  + server.getAdminPort());
		Game game = delegate.findGameByName(gameName);
		Arena arena = delegate.findArenaByName(arenaName);
		GameWorld gameWorld = delegate.findGameWorldByName( arenaName );
		
		World world = Bukkit.getWorld("world");
		for(Player player:Bukkit.getOnlinePlayers()) {
			player.teleport(new Location( world, 0, 65 ,0 ));
		}

		ActionDTO dto = new ActionDTO();
		dto.setArena(arena);
		dto.setGame(game);
		dto.setServer(server);
		dto.setGameWorld(gameWorld);
		dto.setName(ActionDTO.PREPARE_GAME);
		
		Action action = new PrepareGameAction();
		action.execute(dto);

		for(Player player: Bukkit.getOnlinePlayers() ) {
			MineCraftPlayer mcp = delegate.findPlayerByName(player.getName());
			dto.setName(ActionDTO.JOIN_GAME);
			dto.setPlayer(mcp);
			action = new JoinGameAction();
			action.execute(dto);
		}
		

		return true;
    }

}