package com.thecraftcloud.client.test.server;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.ServerInstance;

public class UpdateServerTest {
	
	public static final String URL_SERVICES = "http://services.thecraftcloud.com:8080/gamemanager/webresources";
	//public static final String URL_SERVICES = "http://localhost:8080/gamemanager/webresources";
	//String restURL = "";
    private static TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
	
	@Test
	public void test() {

		ServerInstance server = delegate.findServerByName("gungame2");
		System.out.println("Server: " + server.getName() + " uuid: " + server.getServer_uuid().toString() + " admin port: " + server.getAdminPort() );
		server.setAdminPort(65000);
		server = delegate.updateServer(server);
	}


}
