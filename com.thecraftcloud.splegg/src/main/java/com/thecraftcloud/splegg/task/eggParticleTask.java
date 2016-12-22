package com.thecraftcloud.splegg.task;

import com.thecraftcloud.splegg.GameController;
import com.thecraftcloud.splegg.domain.Egg;

public class eggParticleTask implements Runnable {

	private GameController controller;
	private Egg eggShoot;

	public eggParticleTask(GameController game) {
		this.controller = game;
	}

	@Override
	public void run() {
		/*
		 * for(Projectile eggs : eggShoot.getEggs()){ for(Player online :
		 * Bukkit.getOnlinePlayers()){ Location loc = eggs.getLocation();
		 * ((CraftPlayer) online).getHandle().playerConnection .sendPacket(new
		 * PacketPlayOutWorldParticles()); } }
		 * 
		 * 
		 * }, 0, 1);
		 * 
		 */

	}
}
