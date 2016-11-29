package com.thecraftcloud.gamesetup.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.core.util.BlockManipulationUtil;
import com.thecraftcloud.gamesetup.TheCraftCloudGameSetupPlugin;

public class DestroySelectionAction extends TheCraftCloudCommandAction {

	public DestroySelectionAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
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
		
		TheCraftCloudGameSetupPlugin p = (TheCraftCloudGameSetupPlugin)plugin;

		
		new BlockManipulationUtil().destroyArea3D(player, p.getSelection());
		
	}

}
