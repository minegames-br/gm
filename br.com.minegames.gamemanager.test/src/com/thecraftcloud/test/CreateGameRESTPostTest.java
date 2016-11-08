package com.thecraftcloud.test;

import com.thecraftcloud.client.GameManagerDelegate;
import com.thecraftcloud.core.domain.Game;

public class CreateGameRESTPostTest {

	public static void main(String args[]) {
		Game game = new Game();
		game.setDescription("The Last Archer");
		game.setName("thelastarcher" );

		GameManagerDelegate delegate = GameManagerDelegate.getInstance("http://localhost:8080/gamemanager/webresources");
		delegate.createGame(game);
	}
}
