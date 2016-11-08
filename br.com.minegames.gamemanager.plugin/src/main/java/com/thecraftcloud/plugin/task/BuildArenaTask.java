package com.thecraftcloud.plugin.task;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.core.export.ExportBlock;
import com.thecraftcloud.core.util.BlockManipulationUtil;
import com.thecraftcloud.plugin.MineGamesPlugin;
import com.thecraftcloud.plugin.MyCloudCraftPlugin;

public class BuildArenaTask implements Runnable {
	
	private JavaPlugin controller;
	private BlockManipulationUtil blockManipulationUtil = new BlockManipulationUtil();
	
	public BuildArenaTask(JavaPlugin controller) {
		this.controller = controller;
	}
	
    @Override
    public void run() {
    	if( this.controller instanceof MineGamesPlugin) {
    		MineGamesPlugin p = (MineGamesPlugin)this.controller;
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
    	} else if( this.controller instanceof MyCloudCraftPlugin ) {
    		MyCloudCraftPlugin p = (MyCloudCraftPlugin)this.controller;
        	for(int i=0; i<5000; i++) {
        		if( (i % 500) == 0) {
        			Bukkit.getLogger().info(" - " + i + " blocks created" );
        		}
            	ExportBlock block = p.getNextBlock();
    	    	if(block == null) {
    	        	Bukkit.getLogger().info("&6finalizing task BuildArenaTask" );
    	        	p.completeBuildArenaTask();
    	        	return;
    	    	}
    			//Bukkit.getConsoleSender().sendMessage("&6creating block: " + block + " p.getWorld() " + p.getWorld());
    	    	new BlockManipulationUtil().createBlock(p, p.getWorld(), block);
        	}
    	}
    }
    
}
