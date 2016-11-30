package com.thecraftcloud.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.admin.listener.EndGameListener;
import com.thecraftcloud.admin.listener.StartGameListener;
import com.thecraftcloud.admin.service.ServerService;
import com.thecraftcloud.admin.socket.server.SocketServer;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.util.Utils;

public class TheCraftCloudAdmin extends JavaPlugin {

	private static final String SOCKET_SERVER_PORT = "thecraftcloud.admin.port";
	private static final String SERVER_NAME = "thecraftcloud.admin.name";
	private static final String THECRAFTCLOUD_ADMIN_ACTIONS = "thecraftcloud.admin.actions";
	private TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();
	
	private Integer socketServerPort;
	private String serverName;
	private HashMap<String, String> actions = new HashMap<String, String>();

	@Override
	public void onEnable() {
		this.loadConfiguration();
		this.socketServerPort = this.getConfig().getInt(TheCraftCloudAdmin.SOCKET_SERVER_PORT);
		this.serverName = this.getConfig().getString(TheCraftCloudAdmin.SERVER_NAME);
		//verificar se o servidor esta cadastrado. Se nao, cadastrar.
		registerServer(this.serverName);
		
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
	
	private void registerServer(String name) {
		ServerService sService = new ServerService();
		ServerInstance server = sService.getServerInstance(name);
		Properties props = this.readServerProperties();
		String port = props.getProperty("server-port");
		if(server == null) {
			sService.registerServer(name, this.socketServerPort, new Integer(port) );
		} else {
			sService.updateServer(server, name, socketServerPort, new Integer(port) ); 
		}
		
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
	
	public Properties readServerProperties() {
		FileInputStream in = null;
	    Properties properties = new Properties();
		try {
		    // You can read files using FileInputStream or FileReader.
		    File dir = Bukkit.getWorldContainer();
		    File file = new File(dir, "server.properties");
		    in = new FileInputStream( file );
		    properties.load(in);
		} catch (FileNotFoundException ex) {
	    	ex.printStackTrace();
		} catch (IOException ex) {
	    	ex.printStackTrace();
		} finally {
		    try {
		        if (in != null) in.close();
		    } catch (IOException ex) {
		    	ex.printStackTrace();
		    }
		}
		return properties;
	}

	public String getServerName() {
		return this.serverName;
	}

}
