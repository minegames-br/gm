package com.thecraftcloud.gamesetup.task;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.core.export.ExportBlock;
import com.thecraftcloud.core.util.BlockManipulationUtil;
import com.thecraftcloud.gamesetup.TheCraftCloudGameSetupPlugin;

public class BuildArenaTask implements Runnable {
	
	private JavaPlugin controller;
	private BlockManipulationUtil blockManipulationUtil = new BlockManipulationUtil();
	
	public BuildArenaTask(JavaPlugin controller) {
		this.controller = controller;
	}
	
    public void run() {
    	if( this.controller instanceof TheCraftCloudGameSetupPlugin) {
    		TheCraftCloudGameSetupPlugin p = (TheCraftCloudGameSetupPlugin)this.controller;
			Bukkit.getLogger().info(" MineGamesPlugin - BuildArenaTask" );
        	for(int i=0; i<1000; i++) {
            	ExportBlock block = p.getNextBlock();
    	    	if(block == null) {
    	        	Bukkit.getLogger().info("&6finalizing task BuildArenaTask" );
    	        	p.completeBuildArenaTask();
    	        	return;
    	    	}
    	    	
    	    	if(block.getMaterial() == Material.AIR) {
    	    		blockManipulationUtil.createBlock(p, p.getWorld(), block);
    	    	} else {
        			//Bukkit.getConsoleSender().sendMessage("&6creating block: " + block + " p.getWorld() " + p.getWorld());
        	    	blockManipulationUtil.createBlock(p, p.getWorld(), block);
    	    	}
    	    	
        	}
    	} 
    }
    
}
