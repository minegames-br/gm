package com.thecraftcloud.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.admin.listener.EndGameListener;
import com.thecraftcloud.admin.listener.StartGameListener;
import com.thecraftcloud.admin.socket.server.SocketServer;
import com.thecraftcloud.core.util.Utils;

public class TheCraftCloudAdmin extends JavaPlugin {

	private static final String SOCKET_SERVER_PORT = "thecraftcloud.admin.port";
	private static final String THECRAFTCLOUD_ADMIN_ACTIONS = "thecraftcloud.admin.actions";
	
	private Integer socketServerPort;
	private HashMap<String, String> actions = new HashMap<String, String>();

	@Override
	public void onEnable() {
		this.loadConfiguration();
		this.socketServerPort = this.getConfig().getInt(TheCraftCloudAdmin.SOCKET_SERVER_PORT);
		
		ArrayList<String> list = (ArrayList<String>)this.getConfig().getList( THECRAFTCLOUD_ADMIN_ACTIONS );
		for(String action: list) {
			String[] args = action.split(":");
			String actionName = args[0];
			String className = args[1];
			actions.put(actionName, className);
			Bukkit.getConsoleSender().sendMessage(Utils.color("&9Action: " +  actionName + " - " + className ) );
		}

		//iniciar o administrador remoto - socket server json
		Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable(){
			public void run() {
				SocketServer socketServer = new SocketServer(socketServerPort);
				try {
					socketServer.start();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		Bukkit.getPluginManager().registerEvents(new StartGameListener(), this);
		Bukkit.getPluginManager().registerEvents(new EndGameListener(), this);
		
	}
	
	public void loadConfiguration(){
	     this.getConfig().options().copyDefaults(true);
	     this.saveConfig();
	}

	public HashMap<String, String> getActions() {
		return this.actions;
	}

	public void joinGame(Player player, String gameName) {
		
	}

}
