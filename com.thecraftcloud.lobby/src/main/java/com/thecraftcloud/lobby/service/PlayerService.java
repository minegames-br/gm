package com.thecraftcloud.lobby.service;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.thecraftcloud.admin.TheCraftCloudAdmin;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.GameQueue;
import com.thecraftcloud.core.domain.GameQueueStatus;
import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.domain.PlayerStatus;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.lobby.TheCraftCloudLobbyPlugin;

public class PlayerService {
	
	private TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();
	
	private TheCraftCloudLobbyPlugin plugin;
	
	public PlayerService(TheCraftCloudLobbyPlugin plugin) {
		this.plugin = plugin;
	}
	
	public void joinServer(Player player) {
		MineCraftPlayer mcp = delegate.findPlayerByName(player.getName());
		Bukkit.getConsoleSender().sendMessage(Utils.color("&player: " + player.getName() + " has joined"));
		ServerService sService = new ServerService();
		TheCraftCloudAdmin admin = TheCraftCloudAdmin.getBukkitPlugin();
		ServerInstance server = sService.getServerInstance(admin.getServerName());
		mcp.setServer(server);
		mcp.setStatus(PlayerStatus.ONLINE);
		delegate.updatePlayer(mcp);
		
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
		delegate.updatePlayer(player);
		
		List<GameQueue> gqList = delegate.findAllGameQueueByStatus(GameQueueStatus.WAITING);
		for(GameQueue gq: gqList) {
			if(gq.getPlayer().getMcp_uuid().equals(player.getMcp_uuid())) {
				delegate.removePlayerFromGameQueue(gq);
			}
		}
		
	}

}
