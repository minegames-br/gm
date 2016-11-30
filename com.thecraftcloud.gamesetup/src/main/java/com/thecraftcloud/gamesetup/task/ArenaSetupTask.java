package com.thecraftcloud.gamesetup.task;

import java.util.Collection;

import org.bukkit.Bukkit;

import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.domain.GameConfig;
import com.thecraftcloud.core.domain.GameConfigInstance;
import com.thecraftcloud.core.domain.GameConfigScope;
import com.thecraftcloud.core.domain.GameConfigType;
import com.thecraftcloud.gamesetup.TheCraftCloudGameSetupPlugin;

public class ArenaSetupTask implements Runnable {
	
	private TheCraftCloudGameSetupPlugin controller;
	
	public ArenaSetupTask(TheCraftCloudGameSetupPlugin controller) {
		this.controller = controller;
	}
	
    public void run() {
    	
    }
    
    public void x() {
    	Collection<GameArenaConfig> gacList = this.controller.getGameConfigArenaMap().values();
    	Collection<GameConfigInstance> gciList = this.controller.getGameGameConfigMap().values();
    	
    	boolean complete = true;

    	for(GameConfig gc: this.controller.getConfigList()) {
    		if(gc.getConfigScope() == GameConfigScope.ARENA) {
    			GameArenaConfig gac = this.controller.getGameArenaConfigByName(gc.getName());
    			if(gac == null) {
    				complete = false;
    				return;
    			} else {
    				if(gac.getGameConfig() == null) {
    					complete = false;
    					return;
    				}
        			if(gac.getGameConfig().getConfigType() == GameConfigType.LOCAL) {
        				if(gac.getLocalValue() == null) {
        					complete = false;
        					return;
        				}
        			}if(gac.getGameConfig().getConfigType() == GameConfigType.AREA3D) {
        				if(gac.getAreaValue() == null) {
        					complete = false;
        					return;
        				}
        			}
    			}
    		} else {
    			GameConfigInstance gci = this.controller.getGameConfigInstanceByName(gc.getName());
    			if( gci == null) {
    				complete = false;
    				return;
    			} else {
    				if(gci.getGameConfig() == null) {
    					complete = false;
    					return;
    				}
        			if(gci.getGameConfig().getConfigType() == GameConfigType.LOCAL) {
        				if(gci.getLocal() == null) {
        					complete = false;
        					return;
        				}
        			}if(gci.getGameConfig().getConfigType() == GameConfigType.AREA3D) {
        				if(gci.getArea() == null) {
        					complete = false;
        					return;
        				}
        			}
    			}
    		}
    	}
    	
    	if(complete) {
    		Bukkit.getLogger().info("ArenaSetupTask - complete");
    		this.controller.completeArenaSetupTask();
    	}
    	
    }

}
