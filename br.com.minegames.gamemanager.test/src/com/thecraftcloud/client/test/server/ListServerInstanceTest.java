package com.thecraftcloud.client.test.server;

import java.util.List;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.ServerInstance;

public class ListServerInstanceTest {

	
	public static final String URL_SERVICES = "http://services.thecraftcloud.com:8080/gamemanager/webresources";
	//public static final String URL_SERVICES = "http://localhost:8080/gamemanager/webresources";
	//String restURL = "";
    private static TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
	
	@Test
	public void test() {

		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
		List<ServerInstance> list = delegate.findAllServerInstance();
		for(ServerInstance server: list) {
			System.out.println("Name: " + server.getName() + " IP: " + server.getIp_address() + " port: " + server.getPort() + " admin port: " + server.getAdminPort() );
		}
		
	}

}
