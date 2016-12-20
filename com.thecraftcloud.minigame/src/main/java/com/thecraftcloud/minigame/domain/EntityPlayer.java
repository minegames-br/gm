package com.thecraftcloud.minigame.domain;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.thecraftcloud.core.domain.TransferObject;

public class EntityPlayer extends TransferObject {
	protected int killPoints = 15;
	protected LivingEntity entity;
	protected Player killer;

	public EntityPlayer(LivingEntity entity) {
		super();
		this.entity = entity;
	}

	public int getKillPoints() {
		return this.killPoints;
	}

	public void setKillPoints(int killPoints) {
		this.killPoints = killPoints;
	}

	public void kill(Player player) {
		Location loc = entity.getLocation();
	}

	public LivingEntity getLivingEntity() {
		return this.entity;
	}

	public Player getKiller() {
		return killer;
	}

	public void setKiller(Player killer) {
		this.killer = killer;
	}


}
