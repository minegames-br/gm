package com.thecraftcloud.plugin.task;

import org.bukkit.scheduler.BukkitRunnable;

import com.thecraftcloud.plugin.MineGamesPlugin;

public class SelectRegionTask extends BukkitRunnable {
	
	private MineGamesPlugin controller;
	
	public SelectRegionTask(MineGamesPlugin controller) {
		this.controller = controller;
	}
	
    @Override
    public void run() {

    	//habilitar/desabilitar os eventos de escutar cliques
    	
    }

}
