package com.thecraftcloud.admin.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.bukkit.entity.Player;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.domain.PlayerStatus;
import com.thecraftcloud.core.domain.ServerInstance;

public class PlayerService {
	
	private TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();
	
	public void joinServer(Player player) {
		MineCraftPlayer mcp = delegate.findPlayerByName(player.getName());
		try {
			String hostname = InetAddress.getLocalHost().getCanonicalHostName();
			ServerInstance server = delegate.findServerByHostName(hostname);
			mcp.setServer(server);
			mcp.setStatus(PlayerStatus.ONLINE);
			delegate.updatePlayer(mcp);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
