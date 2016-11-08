package com.thecraftcloud.client.test;

import java.util.List;

import org.junit.Test;

import com.thecraftcloud.client.GameManagerDelegate;
import com.thecraftcloud.core.domain.GameArenaConfig;

public class ListGameConfigArenaTest {

	
	//public static final String URL_SERVICES = "http://services.minegames.com.br:8080/gamemanager/webresources";
	public static final String URL_SERVICES = "http://localhost:8080/gamemanager/webresources";
	//String restURL = "";
    private static GameManagerDelegate delegate = GameManagerDelegate.getInstance(URL_SERVICES);
	
	@Test
	public void test() {
		GameManagerDelegate delegate = GameManagerDelegate.getInstance(URL_SERVICES);
		List<GameArenaConfig> list = delegate.findAllGameConfigArenaByGameArena("c6905743-6514-49ba-9257-420743f65b65", "04cdb0ab-bbc2-41b9-8ccb-5cd555838f68");
		for(GameArenaConfig gac: list) {
			System.out.println(gac.getGameConfig().getName() + " " + gac.getIntValue() + " " + gac.getAreaValue() + " " + gac.getLocalValue() );
		}
	}

}
