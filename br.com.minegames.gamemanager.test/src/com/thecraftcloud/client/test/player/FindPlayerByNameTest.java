package com.thecraftcloud.client.test.player;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.MineCraftPlayer;

public class FindPlayerByNameTest {
	public static final String URL_SERVICES = "http://services.minegames.com.br:8080/gamemanager/webresources";
	//public static final String URL_SERVICES = "http://localhost:8080/gamemanager/webresources";
	//String restURL = "";
    private static TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);

	@Test
	public void test() {

		MineCraftPlayer player = delegate.findPlayerByName("_KingCraft");
		System.out.println(player.getEmail() + " " + player.getName() );
	}

}
