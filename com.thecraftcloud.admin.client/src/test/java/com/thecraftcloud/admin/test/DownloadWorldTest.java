package com.thecraftcloud.admin.test;

import org.junit.Test;

import com.thecraftcloud.admin.socket.client.AdminClient;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.domain.GameWorld;
import com.thecraftcloud.core.domain.ServerInstance;

public class DownloadWorldTest {

	/*
	//String restURL = "";
    private static TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
	
	@Test
	public void test() {

		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
		
		GameWorld gw = delegate.findGameWorldByName("gungame-space-station");
		ServerInstance server = delegate.findServerByName("gungame-local");
		
		ActionDTO actionDTO = new ActionDTO();
		actionDTO.setGameWorld(gw);
		actionDTO.setName(ActionDTO.DOWNLOAD_WORLD);
		
		AdminClient client = AdminClient.getInstance();
		
		client.execute(server, actionDTO);
		
	}
*/
}
