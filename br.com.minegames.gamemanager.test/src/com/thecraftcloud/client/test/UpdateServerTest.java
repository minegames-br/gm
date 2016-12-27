package com.thecraftcloud.client.test;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.domain.ServerType;

public class UpdateServerTest  extends TheCraftCloudJUnitTest {
	
	
	@Test
	public void test() {
		ServerInstance server = delegate.findServerByName("mglobby");
		server.setType(ServerType.LOBBY);
		delegate.updateServer(server);
	}


}
