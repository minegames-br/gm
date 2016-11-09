package com.thecraftcloud.plugin.task;

import org.bukkit.scheduler.BukkitRunnable;

import com.thecraftcloud.plugin.TheCraftCloudPlugin;

public class SelectRegionTask extends BukkitRunnable {
	
	private TheCraftCloudPlugin controller;
	
	public SelectRegionTask(TheCraftCloudPlugin controller) {
		this.controller = controller;
	}
	
    @Override
    public void run() {

    	//habilitar/desabilitar os eventos de escutar cliques
    	
    }

}
