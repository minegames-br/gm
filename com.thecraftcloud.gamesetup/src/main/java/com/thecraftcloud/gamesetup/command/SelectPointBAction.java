package com.thecraftcloud.gamesetup.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.core.domain.Area3D;
import com.thecraftcloud.core.domain.GameConfigType;
import com.thecraftcloud.core.domain.Local;
import com.thecraftcloud.gamesetup.TheCraftCloudGameSetupPlugin;

public class SelectPointBAction extends TheCraftCloudCommandAction {

	public SelectPointBAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	public void execute() {
		Bukkit.getLogger().info("Executando commando Select Point A " + this.commandSender + " "
				+ "\n" + this.command 
				+ "\n" + this.arg2
				+ "\n" + this.arguments);
		
		Player player = null; 
		if( this.commandSender instanceof Player ) {
			player = (Player)commandSender;
		}

		TheCraftCloudGameSetupPlugin p = (TheCraftCloudGameSetupPlugin)plugin;
		
		if(p.getSelection() == null) p.setSelection(new Area3D());
		p.getSelection().setPointB( new Local(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()) );
		
		if(!p.getSetupArena()) {
			player.sendMessage("Point B has been set: " + p.getSelection().getPointB().getX() + "," + p.getSelection().getPointB().getY() + "," + p.getSelection().getPointB().getZ() );
			return;
		}
		
		player.sendMessage("Point B has been set: " + p.getSelection().getPointB().getX() + "," + p.getSelection().getPointB().getY() + "," + p.getSelection().getPointB().getZ() );
		if(p.getGameConfig().getConfigType() == GameConfigType.AREA3D) {
			if(p.getSelection().getPointA() != null && p.getSelection().getPointB() != null) {
				p.setConfigArenaValue(p.getSelection());
				player.sendMessage("Area defined: " + p.getGameConfig().getName() );
				p.nextArenaConfig(player);
			}
		}
	}
}
