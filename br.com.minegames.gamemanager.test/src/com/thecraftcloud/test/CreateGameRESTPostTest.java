package com.thecraftcloud.test;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.client.test.TheCraftCloudJUnitTest;
import com.thecraftcloud.core.domain.Game;

public class CreateGameRESTPostTest  extends TheCraftCloudJUnitTest {
	

	@Test
	public void test() {
		Game game = new Game();
		game.setDescription("Gun Game");
		game.setName("gungame" );

		delegate.createGame(game);
	}
}
