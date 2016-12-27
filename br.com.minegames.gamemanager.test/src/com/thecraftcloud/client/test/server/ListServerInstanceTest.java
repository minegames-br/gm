package com.thecraftcloud.client.test.server;

import java.util.List;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.client.test.TheCraftCloudJUnitTest;
import com.thecraftcloud.core.domain.ServerInstance;

public class ListServerInstanceTest  extends TheCraftCloudJUnitTest {
	
	
	@Test
	public void test() {

		List<ServerInstance> list = delegate.findAllServerInstance();
		for(ServerInstance server: list) {
			System.out.println("Name: " + server.getName() + " IP: " + server.getIp_address() + " port: " + server.getPort() + " admin port: " + server.getAdminPort() );
		}
		
	}

}
