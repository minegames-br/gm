package br.com.minegames.gamemanager.test;

import br.com.minegames.core.domain.Arena;
import br.com.minegames.core.domain.Game;
import br.com.minegames.gamemanager.client.GameManagerDelegate;

public class LoadGameConfigTest {

	//public static final String URL_SERVICES = "http://services.minegames.com.br:8080/gamemanager/webresources";
	public static final String URL_SERVICES = "http://localhost:8080/gamemanager/webresources";
	//String restURL = "";
    private static GameManagerDelegate delegate = GameManagerDelegate.getInstance(URL_SERVICES);
	
	public static void main(String[] args) {

		Game game = delegate.findGame("e2f4757e-d7bc-43fc-a4e7-4b07ad646f1e");
		Arena arena = delegate.findArena("4a84d366-9f64-4631-aecf-fc57fec48b6b");
		
		
		
	}

}
