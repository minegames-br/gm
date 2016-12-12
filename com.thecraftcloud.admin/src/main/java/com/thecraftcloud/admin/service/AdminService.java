package com.thecraftcloud.admin.service;

import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.thecraftcloud.admin.TheCraftCloudAdmin;
import com.thecraftcloud.admin.action.SendPlayerToServerAction;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.bungee.BungeeUtils;
import com.thecraftcloud.core.domain.AdminQueue;
import com.thecraftcloud.core.domain.GameInstance;
import com.thecraftcloud.core.domain.GameState;
import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.domain.PlayerStatus;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.domain.ServerStatus;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.domain.GamePlayer;

public class AdminService {

	private TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();
	private TheCraftCloudAdmin admin = null;
	
	public AdminService( TheCraftCloudAdmin plugin ) {
		this.admin = plugin;
	}

	public void notifyGameStart(TheCraftCloudMiniGameAbstract miniGame, ServerInstance server) {
		
		GameInstance gi = miniGame.getConfigService().getGameInstance();
		gi.setStartTime(Calendar.getInstance());
		gi.setStatus(GameState.RUNNING);
		delegate.updateGameInstance(gi);
		
		server.setStatus(ServerStatus.INGAME);
		delegate.updateServer(server);
		
		this.admin.setMiniGame(miniGame);
		
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
		MineCraftPlayer mcp = delegate.findPlayerByName(gamePlayer.getPlayer().getName());
		mcp.setStatus(PlayerStatus.INGAME);
		delegate.updatePlayer(mcp);
	}
	
	public void notifyPlayerLeft(final GamePlayer gamePlayer, TheCraftCloudMiniGameAbstract miniGame) {
		MineCraftPlayer mcp = delegate.findPlayerByName(gamePlayer.getPlayer().getName());
		mcp.setStatus(PlayerStatus.ONLINE);
		delegate.updatePlayer(mcp);
	}

	public void sendPlayersToLobby(TheCraftCloudMiniGameAbstract game ) {
		/*
		//ESSE CODIGO ESTA HARD CODED RETORNANDO MGLOBBY
		ServerInstance server = delegate.findLobbyAvailable();
		
		BungeeUtils bu = new BungeeUtils();
		bu.setup(game);
		for( GamePlayer gp: game.getLivePlayers() ) {
			bu.sendToServer(gp.getPlayer(), server.getName());
		}
		*/
		for( GamePlayer gp: game.getLivePlayers() ) {
			sendPlayerToLobby(game, gp);
		}
		
	}

	public void sendPlayerToLobby(TheCraftCloudMiniGameAbstract game, GamePlayer gamePlayer) {
		/*
		ServerInstance server = delegate.findLobbyAvailable();
		BungeeUtils bu = new BungeeUtils();
		bu.setup(game);
		Bukkit.getConsoleSender().sendMessage(Utils.color("&6 BungeeUtils sendToServer: " + server.getName() ));
		bu.sendToServer(gamePlayer.getPlayer(), server.getName());
		*/
		
		delegate.sendPlayerToLobby( gamePlayer.getPlayer().getName() );
	}

	public void removeLivePlayer(Player player) {
		
		this.admin.getMiniGame().removeLivePlayer(player);
		
	}
	

}
