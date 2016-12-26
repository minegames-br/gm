package com.thecraftcloud.client.test.world;

import java.io.File;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.client.test.TheCraftCloudJUnitTest;
import com.thecraftcloud.core.domain.GameWorld;

public class UploadGameWorldTest  extends TheCraftCloudJUnitTest {
	
	
	@Test
	public void test() {

		GameWorld gw = delegate.findGameWorldByName("gungame-space-station");
		
		File file = new File("D:/minecraft/mcs-tcc-gungame/gungame-space-station.zip");
		delegate.uploadWorld(gw, file);
		
	}


}
