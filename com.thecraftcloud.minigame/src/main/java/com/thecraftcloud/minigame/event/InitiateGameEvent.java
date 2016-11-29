package com.thecraftcloud.minigame.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;

public class InitiateGameEvent extends Event {

	private TheCraftCloudMiniGameAbstract game;
    private static final HandlerList handlers = new HandlerList();

	public InitiateGameEvent(TheCraftCloudMiniGameAbstract game) {
		this.game = game;
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

}
