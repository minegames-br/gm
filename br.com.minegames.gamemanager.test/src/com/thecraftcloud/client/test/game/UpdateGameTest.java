package com.thecraftcloud.client.test.game;

import org.junit.Test;

import com.thecraftcloud.client.test.TheCraftCloudJUnitTest;
import com.thecraftcloud.core.domain.Game;

public class UpdateGameTest extends TheCraftCloudJUnitTest {
	
	@Test
	public void test() {
		Game game = delegate.findGame("d10e8c62-6124-4952-a054-c7c668e7944f");
		
		game.setPluginName("GunGame");
		
		delegate.updateGame(game);
		
	}

}
