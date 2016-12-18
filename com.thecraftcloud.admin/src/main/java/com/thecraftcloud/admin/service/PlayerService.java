package com.thecraftcloud.admin.service;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.thecraftcloud.admin.TheCraftCloudAdmin;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.GameQueue;
import com.thecraftcloud.core.domain.GameQueueStatus;
import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.domain.PlayerStatus;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.util.Utils;

public class PlayerService {
	
	private TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();
	
	private TheCraftCloudAdmin plugin;
	
	public PlayerService(TheCraftCloudAdmin plugin) {
		this.plugin = plugin;
	}
	
	public void joinServer(Player player) {
		MineCraftPlayer mcp = delegate.findPlayerByName(player.getName());
		Bukkit.getConsoleSender().sendMessage(Utils.color("&player: " + player.getName() + " has joined"));
		ServerInstance server = delegate.findServerByName( Bukkit.getServer().getMotd() );
		mcp.setServer(server);
		
		Bukkit.getConsoleSender().sendMessage(Utils.color("&Update player: " + player.getName() + " " + server.getName() ));
		delegate.updatePlayer(mcp);
		if(mcp.getStatus().equals(PlayerStatus.INGAME)) {
			Bukkit.getConsoleSender().sendMessage(Utils.color("&player: " + player.getName() + " teleporting to waiting lobby"));
			player.teleport(new Location( Bukkit.getWorld("world"), -56, 5, -25 ));
		}
	}

	public void quitServer(Player player) {
		Bukkit.getConsoleSender().sendMessage(Utils.color("&player: " + player.getName() + " has left"));
		MineCraftPlayer mcp = delegate.findPlayerByName(player.getName());
		mcp.setServer(null);
		mcp.setStatus(PlayerStatus.OFFLINE);
		delegate.updatePlayer(mcp);
	}

	public void joinGame(MineCraftPlayer player) {
		player.setStatus(PlayerStatus.INGAME);
		ServerInstance server = delegate.findServerByName(Bukkit.getServer().getMotd());
		player.setServer(server);
		delegate.updatePlayer(player);
		
		List<GameQueue> gqList = delegate.findAllGameQueueByStatus(GameQueueStatus.WAITING);
		for(GameQueue gq: gqList) {
			if(gq.getPlayer().getMcp_uuid().equals(player.getMcp_uuid())) {
				delegate.removePlayerFromGameQueue(gq);
			}
		}
		
	}

}
