package com.thecraftcloud.admin;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.admin.socket.server.SocketServer;

public class TheCraftCloudAdmin extends JavaPlugin {

	private static final String SOCKET_SERVER_PORT = "thecraftcloud.admin.port";
	private Integer socketServerPort;

	@Override
	public void onEnable() {
		this.socketServerPort = this.getConfig().getInt(TheCraftCloudAdmin.SOCKET_SERVER_PORT);

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
		
	}
	
}
