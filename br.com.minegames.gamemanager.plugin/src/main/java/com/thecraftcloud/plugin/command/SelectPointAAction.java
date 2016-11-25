package com.thecraftcloud.plugin.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.core.domain.GameConfigType;
import com.thecraftcloud.core.domain.Local;
import com.thecraftcloud.plugin.TheCraftCloudPlugin;

public class SelectPointAAction extends TheCraftCloudCommandAction {

	public SelectPointAAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
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

		TheCraftCloudPlugin p = (TheCraftCloudPlugin)plugin;
		if(p.isServerRegistered()) {
			if(player != null) {
				player.sendMessage("Please, register server first. /mg register <name>");
				return;
			}
		}
		
		p.getSelection().setPointA( new Local(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()) );
		
		player.sendMessage("Point A has been set: " + p.getSelection().getPointA().getX() + "," + p.getSelection().getPointA().getY() + "," + p.getSelection().getPointA().getZ() );
		if(p.getGameConfig().getConfigType() == GameConfigType.AREA3D) {
			if(p.getSelection().getPointA() != null && p.getSelection().getPointB() != null) {
				p.setConfigArenaValue(p.getSelection());
				player.sendMessage("Area defined: " + p.getGameConfig().getName() );
				p.nextArenaConfig(player);
			}
		}
	}
}
