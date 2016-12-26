package com.thecraftcloud.client.test.game;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.client.test.TheCraftCloudJUnitTest;
import com.thecraftcloud.core.domain.Game;

public class CreateGameTest  extends TheCraftCloudJUnitTest {
	
	
	@Test
	public void test() {
		Game game = new Game();
		/*
		game.setDescription("Domination game based on the World of Warcraft battleground!");
		game.setName("domination");
		game.setPluginName("TheCraftCloud-Domination");
		*/
		game.setDescription("Corra para n�o cair. Derrube seus advers�rios e seja o �ltimo jogador na arena para ganhar.");
		game.setName("splegg");
		game.setPluginName("TheCraftCloud-Splegg");
		
		delegate.createGame(game);
	}

}
