package com.thecraftcloud.domain;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import com.thecraftcloud.core.domain.Area3D;
import com.thecraftcloud.core.domain.TransferObject;

public class GamePlayer extends TransferObject {

	private UUID gp_uuid;
	
	private String player_uuid;
	private String email;
	private String name;
	private String nickName;
	private Area3D spawnPoint;
	private Player player;
	private BossBar baseBar;

	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public UUID getGp_uuid() {
		return gp_uuid;
	}
	public void setGp_uuid(UUID gp_uuid) {
		this.gp_uuid = gp_uuid;
	}
	public String getPlayer_uuid() {
		return player_uuid;
	}
	public void setPlayer_uuid(String player_uuid) {
		this.player_uuid = player_uuid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	
	public void setSpawnPoint(Area3D l) {
		this.spawnPoint = l;
	}
	
	public Area3D getSpawnPoint() {
		return this.spawnPoint;
	}
	
	public void addBaseBar(BossBar bar) {
		this.baseBar = bar;
	}
	
	public BossBar getBaseBar() {
		return this.baseBar;
	}
	
	public boolean isNear(Location l) {
		int x = l.getBlockX();
		int minX = spawnPoint.getPointA().getX();
		int maxX = spawnPoint.getPointA().getX();
		int maxZ = spawnPoint.getPointB().getZ();
		if( x >= minX || x <= maxX) {
			if(l.getBlockZ() == maxZ+1) {
				return true;
			}
		}
		return false;
	}

}
