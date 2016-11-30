package com.thecraftcloud.admin.test;

import org.junit.Test;

import com.thecraftcloud.admin.socket.client.AdminClient;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.domain.GameWorld;
import com.thecraftcloud.core.domain.ServerInstance;

public class DownloadWorldTest {

	
	public static final String URL_SERVICES = "http://services.thecraftcloud.com:8080/gamemanager/webresources";
	//public static final String URL_SERVICES = "http://localhost:8080/gamemanager/webresources";
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
		
		AdminClient client = new AdminClient();
		
		client.execute(server, actionDTO);
		
	}

}
