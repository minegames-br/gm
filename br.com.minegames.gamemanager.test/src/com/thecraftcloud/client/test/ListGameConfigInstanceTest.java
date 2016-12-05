package com.thecraftcloud.client.test;

import java.util.List;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.GameConfigInstance;

public class ListGameConfigInstanceTest {

	
	//public static final String URL_SERVICES = "http://services.minegames.com.br:8080/gamemanager/webresources";
	public static final String URL_SERVICES = "http://services.thecraftcloud.com:8080/gamemanager/webresources";
	//String restURL = "";
    private static TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
	
	@Test
	public void test() {
		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
		List<GameConfigInstance> list = delegate.findAllGameConfigInstanceByGameUUID("c6905743-6514-49ba-9257-420743f65b65");
		for(GameConfigInstance gci: list) {
			System.out.println(gci.getGameConfig().getName() + " " + gci.getIntValue() + " " + gci.getArea() + " " + gci.getLocal() );
		}
	}

}