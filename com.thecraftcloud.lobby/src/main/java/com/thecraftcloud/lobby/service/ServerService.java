package com.thecraftcloud.lobby.service;

import java.net.InetAddress;
import java.util.Calendar;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.domain.ServerStatus;

public class ServerService {

	private TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();

	public ServerInstance getServerInstance(String name) {
		ServerInstance server = null;
		try{
			server = delegate.findServerByName(name);
		}catch( Exception e) {
			e.printStackTrace();
		}
		return server;
	}

	public void registerServer(String name, Integer adminPort, Integer port) {
		ServerInstance server = new ServerInstance();
		try{
			String hostname = InetAddress.getLocalHost().getCanonicalHostName();
			String ip = InetAddress.getLocalHost().getHostAddress();
			server.setHostname(hostname);
			server.setName(name);
			server.setIp_address(ip);
			server.setAdminPort(adminPort);
			server.setPort(port);
			server.setDescription("MineGames Server");
			server.setLastUpdate(Calendar.getInstance());
			server.setStatus(ServerStatus.ONLINE);
			
			delegate.createServer(server);
		}catch( Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void updateServer(ServerInstance server, String name, Integer adminPort, Integer port) {
		try{
			String hostname = InetAddress.getLocalHost().getCanonicalHostName();
			String ip = InetAddress.getLocalHost().getHostAddress();
			server.setHostname(hostname);
			server.setName(name);
			server.setIp_address(ip);
			server.setAdminPort(adminPort);
			server.setPort(port);
			server.setDescription("MineGames Server");
			server.setLastUpdate(Calendar.getInstance());
			server.setStatus(ServerStatus.ONLINE);
			
			delegate.updateServer(server);
		}catch( Exception e) {
			e.printStackTrace();
		}
	}

	public void notifyServerStart(ServerInstance server) {
		server.setStatus(ServerStatus.ONLINE);
		server.setLastUpdate(Calendar.getInstance());
		delegate.updateServer(server);
	}
	
	public void notifyServerStop(ServerInstance server) {
		server.setStatus(ServerStatus.OFFLINE);
		server.setLastUpdate(Calendar.getInstance());
		delegate.updateServer(server);
	}

	
	
}
