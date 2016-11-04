package br.com.minegames.gamemanager.plugin.task;

import java.util.Collection;

import org.bukkit.Bukkit;

import br.com.minegames.core.domain.GameArenaConfig;
import br.com.minegames.core.domain.GameConfig;
import br.com.minegames.core.domain.GameConfigInstance;
import br.com.minegames.core.domain.GameConfigScope;
import br.com.minegames.core.domain.GameConfigType;
import br.com.minegames.gamemanager.plugin.MineGamesPlugin;

public class ArenaSetupTask implements Runnable {
	
	private MineGamesPlugin controller;
	
	public ArenaSetupTask(MineGamesPlugin controller) {
		this.controller = controller;
	}
	
    @Override
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
