package com.thecraftcloud.minigame.task;

import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.domain.GameState;
import com.thecraftcloud.minigame.domain.MyCloudCraftGame;
import com.thecraftcloud.minigame.service.ConfigService;

public class LevelUpTask implements Runnable {
	
	protected TheCraftCloudMiniGameAbstract controller;
	protected ConfigService configService = ConfigService.getInstance(); 
	
	public LevelUpTask(TheCraftCloudMiniGameAbstract game) {
		this.controller = game;
	}
	
    @Override
    public void run() {
    	
    	MyCloudCraftGame game = this.configService.getMyCloudCraftGame();
    	
    	if(!game.hasLevels()) {
    		return;
    	}
    	
    	if(!game.isStarted()) {
    		return;
    	}
    	
    	game.setGameState(GameState.LEVELUP);
		if(controller.isLastLevel()) {
			controller.endGame();
		} else {
    		controller.levelUp();
		}
    	
    }

}
