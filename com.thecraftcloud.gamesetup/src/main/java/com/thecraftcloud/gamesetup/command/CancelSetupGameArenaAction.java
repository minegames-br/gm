package com.thecraftcloud.gamesetup.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.gamesetup.TheCraftCloudGameSetupPlugin;

public class CancelSetupGameArenaAction extends TheCraftCloudCommandAction {

	
	public CancelSetupGameArenaAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	public void execute() {
		Bukkit.getLogger().info("Executando commando Cancel Setup " + this.commandSender + " "
				+ "\n" + this.command 
				+ "\n" + this.arg2
				+ "\n" + this.arguments);
		
		Player player = null; 
		if( this.commandSender instanceof Player ) {
			player = (Player)commandSender;
		}
	
		TheCraftCloudGameSetupPlugin p = (TheCraftCloudGameSetupPlugin)this.plugin;

		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();
		
		if(!p.getSetupArena()) {
			player.sendMessage("Setup was not on going. Nothing has been done.");
			return;
		}
		
		if(p.isServerRegistered()) {
			if(player != null) {
				player.sendMessage("Please, register server first. /mg register <name>");
			}
			return;
		}
		
		if(p.getArena() == null ) {
			if(player != null) {
				player.sendMessage("Choose one of the existing arenas: /mg listarenas [name] or create one /mg createarena <name>");
			}
			return;
		}
		
		if(p.getGame() == null) {
			if(player != null) {
				player.sendMessage("Please, select the game first: /mg listgames and /mg setgame <game>");
			}
			return;
		}
		
		p.setGameGameConfigMap(null);
		p.setGameConfigArenaMap(null);
		p.setConfigList(null);
		p.setPlayer(player);
		p.cancelArenaSetupTask();
		p.setSetup(false);
	}
	
}
