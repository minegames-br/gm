package com.thecraftcloud.gungame;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import com.thecraftcloud.core.domain.Local;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.domain.GamePlayer;
import com.thecraftcloud.gungame.domain.GunGame;
import com.thecraftcloud.gungame.domain.GunGamePlayer;
import com.thecraftcloud.gungame.listener.PlayerDeath;
import com.thecraftcloud.plugin.TheCraftCloudConfig;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;

/**
 * Created by GyuriX on 2016. 09. 22..
 */
public class GameController extends TheCraftCloudMiniGameAbstract {
	private Integer gameDuration;

    @Override
    public void onEnable() {

		super.onEnable();
		
    	//ao criar, o jogo fica imediatamente esperando jogadores
		this.myCloudCraftGame = new GunGame();
    }

	@Override
	public void startGameEngine() {
		super.startGameEngine();
		
	}
	
	@Override
	public boolean shouldEndGame() {
    	//Terminar o jogo caso não tenha mais jogadores
    	if( this.getLivePlayers().size() == 0  && this.myCloudCraftGame.isStarted()) {
            Bukkit.getConsoleSender().sendMessage(Utils.color("&6EndGameTask - No more players"));
            return true;
    	}
    	
    	//Terminar o jogo caso tenha alcançado o limite de tempo
    	long currentTime = System.currentTimeMillis();
    	long duration = currentTime - this.getMyCloudCraftGame().getGameStartTime();
		this.gameDuration = (Integer)this.configService.getGameConfigInstance(TheCraftCloudConfig.GAME_DURATION_IN_SECONDS);

    	if( duration >= this.gameDuration  ) {
    		return true;
    	}
    	return false;
	}

	@Override
	public boolean isLastLevel() {
		return false;
	}

	@Override
	public void levelUp() {
		return;
	}

	@Override
	public GamePlayer createGamePlayer() {
		return new GunGamePlayer();
	}

	@Override
	public Integer getStartCountDown() {
		return this.countDown;
	}

	@Override
	public void setStartCountDown() {
		this.countDown = (Integer)this.configService.getGameConfigInstance(TheCraftCloudConfig.START_COUNTDOWN);
	}


	@Override
	public Local getLobby() {
		return this.lobbyLocal;
	}

	@Override
	public void setLobby() {
		Local l = (Local)this.configService.getGameArenaConfig(TheCraftCloudConfig.LOBBY_LOCATION);
		this.lobbyLocal = l;
	}

	@Override
	public Integer getMinPlayers() {
		Integer minPlayers = (Integer)this.configService.getGameConfigInstance(TheCraftCloudConfig.MIN_PLAYERS);
		return minPlayers;
	}

	@Override
	public Integer getMaxPlayers() {
		Integer maxPlayers = (Integer)this.configService.getGameConfigInstance(TheCraftCloudConfig.MAX_PLAYERS);
		return maxPlayers;
	}

	@Override
	protected void registerListeners() {
		super.registerListeners();
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerDeath(this), this);
	}

}
