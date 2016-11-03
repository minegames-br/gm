package br.com.minegames.gamemanager.plugin.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import br.com.minegames.gamemanager.plugin.MyCloudCraftPlugin;

public class ServerListener implements Listener {

	private MyCloudCraftPlugin game;

    public ServerListener(MyCloudCraftPlugin plugin) {
		super();
		this.game = plugin;
	}

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onStarve(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

}
