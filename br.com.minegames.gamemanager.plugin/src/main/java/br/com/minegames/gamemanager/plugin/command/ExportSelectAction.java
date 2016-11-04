package br.com.minegames.gamemanager.plugin.command;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.minegames.core.command.CommandAction;
import br.com.minegames.core.domain.Area3D;
import br.com.minegames.core.util.BlockManipulationUtil;
import br.com.minegames.gamemanager.plugin.MineGamesPlugin;

public class ExportSelectAction extends CommandAction {

	public ExportSelectAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	@Override
	public void execute() {
		Bukkit.getLogger().info("Executando commando Export Selection " + this.commandSender + " "
				+ "\n" + this.command 
				+ "\n" + this.arg2
				+ "\n" + this.arguments);
		
		Player player = null; 
		if( this.commandSender instanceof Player ) {
			player = (Player)commandSender;
		}
		
		MineGamesPlugin p = (MineGamesPlugin)plugin;
		if(p.getSelection() == null) {
			player.sendMessage("Please, select the 3D area first: /mg select");
			return;
		}		
		
		Area3D area = p.getSelection();
		File folder = p.getDataFolder();
		new BlockManipulationUtil().exportSelection(player, area, folder);
		

	}

}
