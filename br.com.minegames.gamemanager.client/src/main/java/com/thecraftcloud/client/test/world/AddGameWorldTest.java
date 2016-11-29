package com.thecraftcloud.client.test.world;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.GameWorld;

public class AddGameWorldTest {
	
	public static final String URL_SERVICES = "http://services.thecraftcloud.com:8080/gamemanager/webresources";
	//public static final String URL_SERVICES = "http://localhost:8080/gamemanager/webresources";
	//String restURL = "";
    private static TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
	
	@Test
	public void test() {

		GameWorld gw = new GameWorld();
		gw.setName("gungame-space-station");
		gw.setDescription("arena de batalha do gungame que simula uma estacao espacial");
		
		delegate.addGameWorld( gw );
		
	}


}
