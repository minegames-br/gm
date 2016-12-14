package com.thecraftcloud.minigame.domain;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import com.thecraftcloud.core.domain.Area3D;
import com.thecraftcloud.core.domain.Local;
import com.thecraftcloud.core.domain.TransferObject;

public class GamePlayer extends TransferObject {

	private UUID gp_uuid;
	
	protected String player_uuid;
	protected String email;
	protected String name;
	protected String nickName;
	protected Local spawnPoint;
	protected Player player;
	protected BossBar baseBar;
	protected Integer point = 0;

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
	
	
	public void setSpawnPoint(Local l) {
		this.spawnPoint = l;
	}
	
	public Local getSpawnPoint() {
		return this.spawnPoint;
	}
	
	public void addBaseBar(BossBar bar) {
		this.baseBar = bar;
	}
	
	public BossBar getBaseBar() {
		return this.baseBar;
	}
	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}
	
	public void addPoints( int value ) {
		this.point = this.point + value;
	}
	
	public void removePoints( int value ) {
		this.point = this.point - value;
		if( this.point < 0) {
			this.point = 0;
		}
	}
	
	
	public boolean isNear(Location l) {
		int x = l.getBlockX();
		int minX = spawnPoint.getX();
		int maxX = spawnPoint.getX();
		int maxZ = spawnPoint.getZ();
		if( x >= minX || x <= maxX) {
			if(l.getBlockZ() == maxZ+1) {
				return true;
			}
		}
		return false;
	}

}
