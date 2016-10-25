package br.com.minegames.gamemanager.plugin.task;

import org.bukkit.scheduler.BukkitRunnable;

import br.com.minegames.gamemanager.plugin.MineGamesPlugin;

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
