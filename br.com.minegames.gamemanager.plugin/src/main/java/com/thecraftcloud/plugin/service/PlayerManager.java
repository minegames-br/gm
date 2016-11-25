package com.thecraftcloud.plugin.service;

import java.util.concurrent.CopyOnWriteArraySet;

import org.bukkit.entity.Player;

public class PlayerManager {
	
	private CopyOnWriteArraySet<Player> playerList = new CopyOnWriteArraySet<Player>();
	private static PlayerManager me;
	
	private PlayerManager() {
		
	}
	
	public static PlayerManager getInstance() {
		if( me == null) {
			me = new PlayerManager();
		}
		return me;
	}
	
	public void addPlayerToList(Player playerJoined) {
		Player player = playerJoined.getPlayer();
		playerList.add(player);
		System.out.println("[ServerManager] Add{" + player + "}. Player list updated to: " + playerList.size()
				+ " player(s) in server");
	}

	public void removePlayer(Player playerKicked) {
		Player player = playerKicked.getPlayer();
		playerList.remove(playerKicked);
		System.out.println("[ServerManager] Remove{" + player + "}. Player list updated to: " + playerList.size()
		+ " player(s) in server");
	}

	public CopyOnWriteArraySet<Player> getPlayerList() {
		return this.playerList;
	}

}
