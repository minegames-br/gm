package com.thecraftcloud.plugin.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.client.GameManagerDelegate;
import com.thecraftcloud.core.command.CommandAction;
import com.thecraftcloud.core.domain.Local;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.json.JSONParser;
import com.thecraftcloud.plugin.MineGamesPlugin;

public class SetServerSpawnPointAction extends CommandAction {

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

		MineGamesPlugin p = (MineGamesPlugin)plugin;
		if(p.isServerRegistered()) {
			if(player != null) {
				player.sendMessage("Please, register server first. /mg register <name>");
				return;
			}
		}
		
		GameManagerDelegate delegate = GameManagerDelegate.getInstance();
		
		Location loc = player.getLocation();
		Local l = new Local(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		String json = JSONParser.getInstance().toJSONString(l);
		p.setProperty("thecraftcloud.server.spawnpoint", json);
		String world = player.getWorld().getName();
		p.setProperty("thecraftcloud.server.lobby", world );
		
		ServerInstance server = delegate.findServerInstance(p.getServer_uuid());
		server.setLobby(l);
		server.setWorld(world);
		delegate.updateServer(server);
		
		player.sendMessage("Server spawn point defined" );
	}
}
