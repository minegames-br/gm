package com.thecraftcloud.plugin.command;

import java.net.InetSocketAddress;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.thecraftcloud.core.logging.MGLogger;
import com.thecraftcloud.plugin.MyCloudCraftPlugin;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class AddServerCommand implements CommandExecutor {

	private MyCloudCraftPlugin game;

    public AddServerCommand(MyCloudCraftPlugin plugin) {
		super();
		this.game = plugin;
	}

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
    	
    	MGLogger.debug(commandSender.getName() + " " + command.getName() + " " + label + " " + args);
    	
        if (!(commandSender instanceof Player)) {
        	MGLogger.debug(commandSender + " - commando somente para players");
            return false;
        }

        addServer(args[1], new InetSocketAddress(args[2], 25565), args[1], false);
        
        return true;
    }

    public static void addServer(String name, InetSocketAddress address, String motd, boolean restricted) {
        ProxyServer.getInstance().getServers().put(name, ProxyServer.getInstance().constructServerInfo(name, address, motd, restricted));
    }
    public static void removeServer(String name) {
        for (ProxiedPlayer p : ProxyServer.getInstance().getServerInfo(name).getPlayers()) {
            p.disconnect(new TextComponent("This server was forcefully closed.\nPlease report this error in the bug report section of the forums."));
        }
        ProxyServer.getInstance().getServers().remove(name);
    }
}