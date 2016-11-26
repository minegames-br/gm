package com.thecraftcloud.client.test;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.GameConfig;

public class FindGameConfigByNameTest {
	
	public static final String URL_SERVICES = "http://services.thecraftcloud.com:8080/gamemanager/webresources";
	//public static final String URL_SERVICES = "http://localhost:8080/gamemanager/webresources";
	//String restURL = "";
    private static TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
	
	@Test
	public void test() {

		GameConfig gc = delegate.findGameConfigByName( "GAME-DURATION-IN-SECONDS" );
		System.out.println("GameConfig: " + gc.getName() + " uuid: " + gc.getGame_config_uuid().toString() );
	}


}
