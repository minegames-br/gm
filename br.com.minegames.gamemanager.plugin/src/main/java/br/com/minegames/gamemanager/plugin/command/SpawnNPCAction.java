package br.com.minegames.gamemanager.plugin.command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.minegames.core.command.CommandAction;
import br.com.minegames.gamemanager.plugin.MineGamesPlugin;
import net.dragonode.npcs.NPC;

public class SpawnNPCAction extends CommandAction {

	public SpawnNPCAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	@Override
	public void execute() {
		Bukkit.getLogger().info("Executando commando Spawn NPC " + this.commandSender + " "
				+ "\n" + this.command 
				+ "\n" + this.arg2
				+ "\n" + this.arguments);
		
		Player player = null; 
		if( this.commandSender instanceof Player ) {
			player = (Player)commandSender;
		}
		
		MineGamesPlugin p = (MineGamesPlugin)plugin;
		
		String uuid = "88165e7f-a83a-49f8-9f75-5cb77c23f8d3";
		if(arguments.length >= 2) {
			uuid = arguments[1];
		}
		
		uuid = "88165e7f-a83a-49f8-9f75-5cb77c23f8d3";
		NPC npc = new NPC("The Archer", UUID.fromString( uuid ), player.getLocation());
		
	}

}