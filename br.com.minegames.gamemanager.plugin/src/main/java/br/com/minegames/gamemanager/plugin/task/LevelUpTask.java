package br.com.minegames.gamemanager.plugin.task;

import org.bukkit.Bukkit;

import br.com.minegames.gamemanager.domain.GameState;
import br.com.minegames.gamemanager.domain.MyCloudCraftGame;
import br.com.minegames.gamemanager.plugin.MyCloudCraftPlugin;

public class LevelUpTask implements Runnable {
	
	private MyCloudCraftPlugin controller;
	
	public LevelUpTask(MyCloudCraftPlugin game) {
		this.controller = game;
	}
	
    @Override
    public void run() {
    	
    	MyCloudCraftGame game = controller.getMyCloudCraftGame();
    	if(!game.isStarted()) {
    		Bukkit.getLogger().info("LEVELUPTASK - Game not started");
    		return;
    	}
    	
    	//Aumentar de nível depois de 15 segundos
    	//Caso seja o último nível, terminar o jogo
    	
    	Integer duration = (Integer)this.controller.getGameConfigInstance("ARQUEIRO-GAME-DURATION");
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
