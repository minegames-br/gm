package com.thecraftcloud.admin.service;

import java.util.Calendar;

import org.bukkit.Bukkit;

import com.thecraftcloud.admin.TheCraftCloudAdmin;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.GameInstance;
import com.thecraftcloud.core.domain.GameState;
import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.domain.PlayerStatus;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.domain.ServerStatus;
import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.domain.GamePlayer;

public class AdminService {

	private TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();

	public void notifyGameStart(TheCraftCloudMiniGameAbstract miniGame) {
		
		GameInstance gi = miniGame.getConfigService().getGameInstance();
		gi.setStartTime(Calendar.getInstance());
		gi.setStatus(GameState.RUNNING);
		delegate.updateGameInstance(gi);
		
		ServerInstance server = miniGame.getConfigService().getServerInstance();
		server.setStatus(ServerStatus.INGAME);
		delegate.updateServer(server);
		
	}
	
	public void notifyGameOver(TheCraftCloudMiniGameAbstract miniGame) {
		
		GameInstance gi = miniGame.getConfigService().getGameInstance();
		gi.setStatus(GameState.GAMEOVER);
		gi.setEndTime(Calendar.getInstance());
		delegate.updateGameInstance(gi);
		
		TheCraftCloudAdmin pAdmin = (TheCraftCloudAdmin)Bukkit.getPluginManager().getPlugin(TheCraftCloudAdmin.PLUGIN_NAME);
		ServerInstance server = pAdmin.getServerInstance();
		server.setStatus(ServerStatus.ONLINE);
		delegate.updateServer(server);
		
	}

	public void notifyPlayerJoin(final GamePlayer gamePlayer, TheCraftCloudMiniGameAbstract miniGame) {
		MineCraftPlayer mcp = delegate.findPlayerByName(gamePlayer.getName());
		mcp.setStatus(PlayerStatus.INGAME);
		delegate.updatePlayer(mcp);
	}
	
	public void notifyPlayerLeft(final GamePlayer gamePlayer, TheCraftCloudMiniGameAbstract miniGame) {
		MineCraftPlayer mcp = delegate.findPlayerByName(gamePlayer.getName());
		mcp.setStatus(PlayerStatus.ONLINE);
		delegate.updatePlayer(mcp);
	}
	

}
