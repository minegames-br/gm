package com.thecraftcloud.client.test.gameinstance;

import java.util.Calendar;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.GameInstance;
import com.thecraftcloud.core.domain.GameState;

public class UpdateGameInstanceTest {
	
	public static final String URL_SERVICES = "http://services.thecraftcloud.com:8080/gamemanager/webresources";
	//public static final String URL_SERVICES = "http://localhost:8080/gamemanager/webresources";
	//String restURL = "";
    private static TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
	
	@Test
	public void test() {

		GameInstance gi = delegate.findGameInstanceByUUID("c58a81d7-42de-4a05-8c0b-deb7759e4d9c");
		System.out.println("gi: " + gi.getGi_uuid() + " server: " + gi.getServer().getName() );
		gi.setStartTime(Calendar.getInstance());
		gi.setEndTime(null);
		gi.setStatus(GameState.RUNNING);
		gi = delegate.updateServer(gi);
	}


}
