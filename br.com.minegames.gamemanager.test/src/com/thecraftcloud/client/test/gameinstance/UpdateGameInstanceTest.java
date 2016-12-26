package com.thecraftcloud.client.test.gameinstance;

import java.util.Calendar;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.client.test.TheCraftCloudJUnitTest;
import com.thecraftcloud.core.domain.GameInstance;
import com.thecraftcloud.core.domain.GameState;

public class UpdateGameInstanceTest  extends TheCraftCloudJUnitTest {
	
	
	@Test
	public void test() {

		GameInstance gi = delegate.findGameInstanceByUUID("c58a81d7-42de-4a05-8c0b-deb7759e4d9c");
		System.out.println("gi: " + gi.getGi_uuid() + " server: " + gi.getServer().getName() );
		gi.setStartTime(Calendar.getInstance());
		gi.setEndTime(null);
		gi.setStatus(GameState.RUNNING);
		gi = delegate.updateGameInstance(gi);
	}


}
