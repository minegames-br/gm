package br.com.minegames.gamemanager.plugin.task;

import org.bukkit.scheduler.BukkitRunnable;

import br.com.minegames.core.logging.MGLogger;
import br.com.minegames.gamemanager.domain.GameState;
import br.com.minegames.gamemanager.domain.MyCloudCraftGame;
import br.com.minegames.gamemanager.plugin.MyCloudCraftPlugin;

public class LevelUpTask extends BukkitRunnable {
	
	private MyCloudCraftPlugin controller;
	
	public LevelUpTask(MyCloudCraftPlugin game) {
		this.controller = game;
	}
	
    @Override
    public void run() {
    	
    	MyCloudCraftGame game = controller.getMyCloudCraftGame();
    	if(!game.isStarted()) {
    		return;
    	}
    	
    	//Aumentar de nível depois de 15 segundos
    	//Caso seja o último nível, terminar o jogo
    	if(game.getLevel().lifeTime() >= 15000) {
    		game.setGameState(GameState.LEVELUP);
    		if(controller.isLastLevel()) {
    			controller.endGame();
    		} else {
        		controller.levelUp();
        		MGLogger.debug("LevelUp " + game.getLevel().getLevel());
    		}
    	}
    	
    }

}
