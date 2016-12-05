package com.thecraftcloud.minigame.task;

import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.service.PlayerService;

public class UpdateScoreBoardTask implements Runnable {
	
	private TheCraftCloudMiniGameAbstract controller;
	
	public UpdateScoreBoardTask(TheCraftCloudMiniGameAbstract controller) {
		this.controller = controller;
	}
	
    @Override
    public void run() {

    	PlayerService pService = new PlayerService(this.controller);
    	pService.updateScoreBoards();
    	
    }

}
