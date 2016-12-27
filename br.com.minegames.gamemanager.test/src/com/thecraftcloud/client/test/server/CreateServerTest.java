package com.thecraftcloud.client.test.server;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.client.test.TheCraftCloudJUnitTest;
import com.thecraftcloud.core.domain.ServerInstance;

public class CreateServerTest  extends TheCraftCloudJUnitTest {
	
	
	@Test
	public void test() {

		ServerInstance server = new ServerInstance();
		server.setDescription("servidor local do pc do joao emilio");
		server.setHostname("pc-joaoemilio");
		server.setIp_address("localhost");
		server.setPort(26001);
		server.setName("localhost-joao");
		server.setAdminPort(55000);
		
		server = delegate.createServer( server );
		System.out.println("Server: " + server.getName() + " uuid: " + server.getServer_uuid().toString() + " ip: " + server.getIp_address() + " port: " + server.getPort() + " admin port:" + server.getAdminPort() );
		
	}


}
