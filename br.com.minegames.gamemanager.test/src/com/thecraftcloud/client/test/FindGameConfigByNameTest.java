package com.thecraftcloud.client.test;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.GameConfig;

public class FindGameConfigByNameTest  extends TheCraftCloudJUnitTest {
	
	
	@Test
	public void test() {

		GameConfig gc = delegate.findGameConfigByName( "GAME-DURATION-IN-SECONDS" );
		System.out.println("GameConfig: " + gc.getName() + " uuid: " + gc.getGame_config_uuid().toString() );
	}


}
