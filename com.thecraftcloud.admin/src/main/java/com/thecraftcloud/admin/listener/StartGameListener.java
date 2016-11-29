package com.thecraftcloud.admin.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.minigame.event.StartGameEvent;

public class StartGameListener implements Listener {

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onStartGame(StartGameEvent event) {
    	Bukkit.getConsoleSender().sendMessage(Utils.color("&2Game: " + event.getGame().getName() + " has started."));
    }

}
