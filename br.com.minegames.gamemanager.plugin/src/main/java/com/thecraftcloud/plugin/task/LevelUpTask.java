package com.thecraftcloud.plugin.task;

import org.bukkit.Bukkit;

import com.thecraftcloud.domain.GameState;
import com.thecraftcloud.domain.MyCloudCraftGame;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.plugin.service.ConfigService;

public class LevelUpTask implements Runnable {
	
	protected TheCraftCloudMiniGameAbstract controller;
	protected ConfigService configService = ConfigService.getInstance(); 
	
	public LevelUpTask(TheCraftCloudMiniGameAbstract game) {
		this.controller = game;
	}
	
    @Override
    public void run() {
    	
    	MyCloudCraftGame game = controller.getMyCloudCraftGame();
    	
    	if(!game.hasLevels()) {
    		return;
    	}
    	
    	if(!game.isStarted()) {
    		Bukkit.getLogger().info("LEVELUPTASK - Game not started");
    		return;
    	}
    	
    	//Aumentar de nível depois de 15 segundos
    	//Caso seja o último nível, terminar o jogo
    	
    	Integer duration = (Integer)this.configService.getGameConfigInstance("GAME-DURATION");
    	duration = duration * 60; // segundos
    	duration = duration * 1000; //milissegundos
    	Integer levelDuration = (duration/10);
    	
    	if(game.getLevel().lifeTime() >= levelDuration) {
    		game.setGameState(GameState.LEVELUP);
    		if(controller.isLastLevel()) {
    			controller.endGame();
    		} else {
        		controller.levelUp();
    		}
    	}
    	
    }

}
