package com.thecraftcloud.test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.Game;

public class CreateGameRESTPostTest {
	public static final String URL_SERVICES = "http://services.minegames.com.br:8080/gamemanager/webresources";

	public static void main(String args[]) {
		Game game = new Game();
		game.setDescription("Gun Game");
		game.setName("gungame" );

		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
		delegate.createGame(game);
	}
}
