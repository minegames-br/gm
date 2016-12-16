package com.thecraftcloud.admin.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.thecraftcloud.admin.TheCraftCloudAdmin;
import com.thecraftcloud.admin.service.AdminService;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.minigame.event.EndGameEvent;
import com.thecraftcloud.minigame.service.ConfigService;

public class EndGameListener implements Listener {
	
	private TheCraftCloudAdmin plugin;
	
    public EndGameListener(TheCraftCloudAdmin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority=EventPriority.HIGHEST)
    public void onEndGame(final EndGameEvent event) {
    	Bukkit.getConsoleSender().sendMessage(Utils.color("&3Game: " + event.getGame().getName() + " has ended."));
    	final AdminService service = new AdminService(this.plugin);
    	final ConfigService cService = ConfigService.getInstance();

    	if(plugin.isLocal()) {
    		World world = Bukkit.getWorld("world");
    		for( Player player: Bukkit.getOnlinePlayers() ) {
    			player.teleport(new Location(world, 0, 5, 0));
    		}
    	}
    	
    	Bukkit.getScheduler().runTaskAsynchronously(event.getGame(), new Runnable() {
    		public void run() {
    	    	service.notifyGameOver( event.getGame() );
    	    	AdminService adminService = new AdminService(plugin);
    	    	adminService.sendPlayersToLobby( event.getGame() );
    	    	
    		}
    	});
    }

}
