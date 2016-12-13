package com.thecraftcloud.lobby.service;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.domain.PlayerStatus;
import com.thecraftcloud.core.util.Utils;

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
		Bukkit.getConsoleSender().sendMessage(Utils.color("&9Player Status: " + mcp.getStatus().name() ));
		if(!mcp.getStatus().equals(PlayerStatus.ONLINE)) {
			return;
		}
		
		mcp.setStatus(PlayerStatus.INGAME);
		delegate.updatePlayer(mcp);
		Game game = delegate.findGameByName(gameName);
		delegate.joinGameQueue(mcp, game);
		
	}

}
