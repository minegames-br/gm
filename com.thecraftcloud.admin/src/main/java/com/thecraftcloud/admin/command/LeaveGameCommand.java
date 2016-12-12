package com.thecraftcloud.admin.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.thecraftcloud.admin.TheCraftCloudAdmin;
import com.thecraftcloud.admin.service.AdminService;
import com.thecraftcloud.core.util.Utils;

public class LeaveGameCommand implements CommandExecutor {

	private TheCraftCloudAdmin controller;

    public LeaveGameCommand(TheCraftCloudAdmin plugin) {
		super();
		this.controller = plugin;
	}

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Bukkit.getConsoleSender().sendMessage(Utils.color("&6comando sair sendo executado"));
        
        Player player = (Player) commandSender;
        Bukkit.getConsoleSender().sendMessage(Utils.color("&6remove live player: " + player.getName() ));
        
        AdminService aservice = new AdminService(this.controller);
        aservice.removeLivePlayer(player);
        
        return true;
    }

}