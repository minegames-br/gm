package br.com.minegames.gamemanager.plugin.task;

import org.bukkit.Bukkit;

import br.com.minegames.core.export.ExportBlock;
import br.com.minegames.core.util.BlockManipulationUtil;
import br.com.minegames.gamemanager.plugin.MyCloudCraftPlugin;

public class BuildArenaTask implements Runnable {
	
	private MyCloudCraftPlugin controller;
	
	public BuildArenaTask(MyCloudCraftPlugin controller) {
		this.controller = controller;
	}
	
    @Override
    public void run() {
    	for(int i=0; i<50000; i++) {
        	ExportBlock block = controller.getNextBlock();
	    	if(block == null) {
	        	Bukkit.getLogger().info("&6finalizing task BuildArenaTask" );
	        	controller.completeBuildArenaTask();
	        	return;
	    	}
			//Bukkit.getConsoleSender().sendMessage("&6creating block: " + block );
	    	BlockManipulationUtil.createBlock(controller, controller.getWorld(), block);
    	}
    }
    
}
