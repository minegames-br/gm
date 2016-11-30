package com.thecraftcloud.admin.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.minigame.event.EndGameEvent;

public class EndGameListener implements Listener {
	
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onEndGame(EndGameEvent event) {
    	Bukkit.getConsoleSender().sendMessage(Utils.color("&3Game: " + event.getGame().getName() + " has ended."));
    }

}
