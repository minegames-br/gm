package com.thecraftcloud.admin.service;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.thecraftcloud.core.util.Utils;

public class PermissionService {

	
	public void setPermissions(Player player) {
		String op_players = "FoxGamer129 _WolfGamer _KingCraft";
		String name = player.getName();
		if( op_players.contains(name)) {
			Bukkit.getConsoleSender().sendMessage(Utils.color("&7" + name + " is an operator "));
			player.setOp(true);
		} else {
			Bukkit.getConsoleSender().sendMessage(Utils.color("&7" + name + " is not an operator "));
		}
		
	}
	
}
