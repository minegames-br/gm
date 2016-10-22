package br.com.minegames.gamemanager.plugin.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.minegames.core.command.CommandAction;
import br.com.minegames.core.domain.ServerInstance;
import br.com.minegames.gamemanager.client.GameManagerDelegate;
import br.com.minegames.gamemanager.plugin.MineGamesPlugin;

public class RegisterServerAction extends CommandAction {

	
	public RegisterServerAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	public void execute() {
		Bukkit.getLogger().info("Executando commando Register Server " + this.commandSender + " "
				+ "\n" + this.command 
				+ "\n" + this.arg2
				+ "\n" + this.arguments);
		
		Player player = null; 
		if( this.commandSender instanceof Player ) {
			player = (Player)commandSender;
		}

		MineGamesPlugin plugin = (MineGamesPlugin)this.plugin;
		YamlConfiguration config = plugin.getConfigFile();
		String value = config.getString("minegames.server.uuid");
		Bukkit.getLogger().info("minegames.server.uuid: " + value);
		if(value != null && !value.trim().equals("")) {
			if(player != null) {
				player.sendMessage("Server already registered: " + value);
			}else{
				Bukkit.getLogger().info("Server already registered: " + value);
			}
			return;
		}
		
		GameManagerDelegate delegate = new GameManagerDelegate();
		ServerInstance server = delegate.registerServer(arguments[0]);
		value = server.getServer_uuid().toString();
		plugin.setConfig("minegames.server.uuid", value);
		if(player!= null) {
			player.sendMessage("Server registered successfully: "+ value );
		}else{
			Bukkit.getLogger().info("Server registered successfully: " + value );
		}
	}
	
}
