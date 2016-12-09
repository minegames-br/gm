package com.thecraftcloud.client.test.game;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameConfig;

public class AddGameConfigToGameTest {
	public static final String URL_SERVICES = "http://services.thecraftcloud.com:8080/gamemanager/webresources";
	//public static final String URL_SERVICES = "http://localhost:8080/gamemanager/webresources";
	//String restURL = "";
    private static TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
	
	@Test
	public void test() {
		Game game = delegate.findGame("d10e8c62-6124-4952-a054-c7c668e7944f");
		for(int i = 4; i < 14; i++) {
			GameConfig gc = delegate.findGameConfigByName("GUNGAME." + i);
			delegate.addGameConfigToGame( game, gc);
		}
		
		
	}

}
