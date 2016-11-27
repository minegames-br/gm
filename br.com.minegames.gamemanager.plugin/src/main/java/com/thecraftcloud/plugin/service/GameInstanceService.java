package com.thecraftcloud.plugin.service;

import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameInstance;
import com.thecraftcloud.core.domain.GameState;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;

public class GameInstanceService extends TheCraftCloudService {

	public GameInstanceService(TheCraftCloudMiniGameAbstract miniGame) {
		super(miniGame);
	}
	
	public GameInstance startGameInstance(ServerInstance server, Game game, Arena arena) {
		
		GameInstance gi = new GameInstance();
		gi.setGame(game);
		gi.setArena(arena);
		gi.setServer(server);
		gi.setStatus(GameState.WAITING);
		
		gi = delegate.createGameInstance(gi);
		return gi;
	}

}
