package com.thecraftcloud.lobby.service;

import org.bukkit.entity.Player;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.MineCraftPlayer;

public class GameService {

	private static GameService me;
	private TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();
	
	public static GameService getInstance() {
		if( me == null) {
			me = new GameService();
		}
		return me;
	}
	
	public void playGame( Player player, String gameName ) {
		//TheCraftCloudAdmin plugin = (TheCraftCloudAdmin) Bukkit.getPluginManager().getPlugin("TheCraftCloud-Admin");
		
		MineCraftPlayer mcp = delegate.findPlayerByName(player.getName());
		Game game = delegate.findGameByName(gameName);
		
		delegate.joinGameQueue(mcp, game);
		
	}

}
