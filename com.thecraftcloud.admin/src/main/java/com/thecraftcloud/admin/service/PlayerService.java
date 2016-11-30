package com.thecraftcloud.admin.service;

import org.bukkit.entity.Player;

import com.thecraftcloud.admin.TheCraftCloudAdmin;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.domain.PlayerStatus;
import com.thecraftcloud.core.domain.ServerInstance;

public class PlayerService {
	
	private TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();
	
	private TheCraftCloudAdmin plugin;
	
	public PlayerService(TheCraftCloudAdmin plugin) {
		this.plugin = plugin;
	}
	
	public void joinServer(Player player) {
		MineCraftPlayer mcp = delegate.findPlayerByName(player.getName());

		ServerService sService = new ServerService();
		ServerInstance server = sService.getServerInstance(plugin.getServerName());
		mcp.setServer(server);
		mcp.setStatus(PlayerStatus.ONLINE);
		delegate.updatePlayer(mcp);
		
	}

}
