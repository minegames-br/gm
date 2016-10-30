package br.com.minegames.gamemanager.plugin.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.minegames.core.command.CommandAction;
import br.com.minegames.core.domain.Area3D;
import br.com.minegames.core.domain.Arena;
import br.com.minegames.core.domain.GameWorld;
import br.com.minegames.core.domain.ServerInstance;
import br.com.minegames.gamemanager.client.GameManagerDelegate;
import br.com.minegames.gamemanager.plugin.MineGamesPlugin;

public class DefineAreaAction extends CommandAction {

	public DefineAreaAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	public void execute() {
		Bukkit.getLogger().info("Executando commando Define Area " + this.commandSender + " "
				+ "\n" + this.command 
				+ "\n" + this.arg2
				+ "\n" + this.arguments);
		
		Player player = null; 
		if( this.commandSender instanceof Player ) {
			player = (Player)commandSender;
		}

		MineGamesPlugin p = (MineGamesPlugin)plugin;
		GameManagerDelegate delegate = GameManagerDelegate.getInstance();
		String server_uuid = p.getConfigFile().getString("minegames.server.uuid");
		if(server_uuid == null || server_uuid.equals("")) {
			if(player != null) {
				player.sendMessage("Please, register server first. /mg register <name>");
				return;
			}
		}
		Arena arena = p.getArena();
		if(arena == null) {
			if(player != null) {
				player.sendMessage("Find available arenas: /mg listarenas [name]");
				player.sendMessage("Please, set the active Arena first: /mg setarena <#>");
				return;
			}
		}
		
		if(arguments.length < 2) {
			player.sendMessage("Please, send a name for the area. /mg addarea <name> [category]");
			return;
		}
		String areaName = arguments[1];
		
		ServerInstance server = delegate.findServerInstance(server_uuid);
		Area3D area = p.getSelection();
		area.setName(areaName);
		if(arguments.length == 3) {
			area.setType(arguments[2]);			
		}
		
		arena = delegate.addArenaArea3D(arena, area);
		player.sendMessage("Listing Areas...");
		for(Area3D a: arena.getAreas()) {
			player.sendMessage(a.getName() + ":" + a.getType() );
		}
	}
}
