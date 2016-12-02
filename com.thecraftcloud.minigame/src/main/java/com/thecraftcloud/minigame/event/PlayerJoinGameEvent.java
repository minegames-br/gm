package com.thecraftcloud.minigame.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.domain.GamePlayer;

public class PlayerJoinGameEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private TheCraftCloudMiniGameAbstract game;
    private GamePlayer gamePlayer;

	public PlayerJoinGameEvent(TheCraftCloudMiniGameAbstract game, GamePlayer gp) {
		this.game = game;
		this.gamePlayer = gp;
	}
	
	@Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    
	public TheCraftCloudMiniGameAbstract getGame() {
		return this.game;
	}

	public GamePlayer getGamePlayer() {
		return gamePlayer;
	}

	public void setGamePlayer(GamePlayer gamePlayer) {
		this.gamePlayer = gamePlayer;
	}

	public void setGame(TheCraftCloudMiniGameAbstract game) {
		this.game = game;
	}
	
}
