package br.com.minegames.gamemanager.test;

import br.com.minegames.core.domain.Game;
import br.com.minegames.gamemanager.client.GameManagerDelegate;

public class CreateGameRESTPostTest {

	public static void main(String args[]) {
		Game game = new Game();
		game.setDescription("The Last Archer");
		game.setName("thelastarcher" );

		GameManagerDelegate delegate = GameManagerDelegate.getInstance("http://localhost:8080/gamemanager/webresources");
		delegate.createGame(game);
	}
}
