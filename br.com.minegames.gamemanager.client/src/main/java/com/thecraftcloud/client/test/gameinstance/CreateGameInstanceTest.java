package com.thecraftcloud.client.test.gameinstance;

import java.util.Calendar;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.client.exception.InvalidRegistrationException;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameInstance;
import com.thecraftcloud.core.domain.GameState;
import com.thecraftcloud.core.domain.ServerInstance;

public class CreateGameInstanceTest {
	
	public static final String URL_SERVICES = "http://services.thecraftcloud.com:8080/gamemanager/webresources";
	//public static final String URL_SERVICES = "http://localhost:8080/gamemanager/webresources";
	//String restURL = "";
    private static TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
	
	@Test
	public void test() throws InvalidRegistrationException {

		GameInstance gi = new GameInstance();
		Arena arena = delegate.findArena("30d00221-b371-4828-a0e6-5d75de7bfaec");
		gi.setArena(arena);
		
		Game game = delegate.findGame("d10e8c62-6124-4952-a054-c7c668e7944f");
		gi.setGame(game);
		
		ServerInstance server = delegate.findServerByName("gungame-local");
		gi.setServer(server);
		
		gi.setStatus(GameState.WAITING);

		gi = delegate.createGameConfigInstance(gi);
		
	}


}
