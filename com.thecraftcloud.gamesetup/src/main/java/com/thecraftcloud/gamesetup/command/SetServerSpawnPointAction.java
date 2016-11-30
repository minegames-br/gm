package com.thecraftcloud.gamesetup.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.Local;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.json.JSONParser;
import com.thecraftcloud.gamesetup.TheCraftCloudGameSetupPlugin;

public class SetServerSpawnPointAction extends TheCraftCloudCommandAction {

	public SetServerSpawnPointAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	public void execute() {
		Bukkit.getLogger().info("Executando commando Set Lobby " + this.commandSender + " "
				+ "\n" + this.command 
				+ "\n" + this.arg2
				+ "\n" + this.arguments);
		
		Player player = null; 
		if( this.commandSender instanceof Player ) {
			player = (Player)commandSender;
		}

		TheCraftCloudGameSetupPlugin p = (TheCraftCloudGameSetupPlugin)plugin;
		if(p.isServerRegistered()) {
			if(player != null) {
				player.sendMessage("Please, register server first. /mg register <name>");
				return;
			}
		}
		
		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();
		
		Location loc = player.getLocation();
		Local l = new Local(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		String json = JSONParser.getInstance().toJSONString(l);
		p.setProperty("thecraftcloud.server.spawnpoint", json);
		String world = player.getWorld().getName();
		p.setProperty("thecraftcloud.server.lobby", world );
		
		ServerInstance server = p.getServerInstance();
		server.setLobby(l);
		server.setWorld(world);
		delegate.updateServer(server);
		
		player.sendMessage("Server spawn point defined" );
	}
}
