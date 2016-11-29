package com.thecraftcloud.client.test.world;

import java.io.File;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.GameWorld;

public class UploadGameWorldTest {
	
	public static final String URL_SERVICES = "http://services.thecraftcloud.com:8080/gamemanager/webresources";

    private static TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
	
	@Test
	public void test() {

		GameWorld gw = delegate.findGameWorldByName("gungame-space-station");
		
		File file = new File("D:/minecraft/mcs-tcc-gungame/gungame-space-station.zip");
		delegate.uploadWorld(gw, file);
		
	}


}
