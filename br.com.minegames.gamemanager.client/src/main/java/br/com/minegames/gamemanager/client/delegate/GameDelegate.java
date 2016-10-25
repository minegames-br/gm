package br.com.minegames.gamemanager.client.delegate;

import java.util.UUID;

import br.com.minegames.core.domain.Config;
import br.com.minegames.core.domain.GameInstance;
import br.com.minegames.gamemanager.client.fake.FakeGameInstanceArqueiroArena1;
import br.com.minegames.gamemanager.client.fake.FakeGameInstanceArqueiroArena2;
import br.com.minegames.gamemanager.client.fake.FakeGameInstanceArqueiroArena3;

public class GameDelegate {

	private static GameDelegate me;
	
	private GameDelegate() {
		
	}
	
	public static GameDelegate getInstance() {
		if (me == null) {
			me = new GameDelegate();
		}
		return me;
	}
	
	public GameInstance joinGame(String arena) {
		GameInstance gi = null;
		
		if(arena.equalsIgnoreCase("arena1")) {
			gi = FakeGameInstanceArqueiroArena1.createGameInstance();
		} else if (arena.equalsIgnoreCase("arena2")) {
			gi = FakeGameInstanceArqueiroArena2.createGameInstance();
		} else if (arena.equalsIgnoreCase("arena3")) {
			gi = FakeGameInstanceArqueiroArena3.createGameInstance();
		}
		
		return gi;
	}
	
	public Config getGlobalConfig(String name) {
		if(name.equalsIgnoreCase("ARQUEIRO-MIN-PLAYERS")) {
			Config config = new Config();
			config.setConfig_uuid(UUID.randomUUID());
			config.setName(name);
			config.setType("GLOBAL");
			config.setValue("1");
			return config;
		} else if(name.equalsIgnoreCase("ARQUEIRO-MAX-PLAYERS")) {
			Config config = new Config();
			config.setConfig_uuid(UUID.randomUUID());
			config.setName(name);
			config.setType("GLOBAL");
			config.setValue("4");
			return config;
		} else if(name.equalsIgnoreCase("ARQUEIRO-START-COUNTDOWN")) {
			Config config = new Config();
			config.setConfig_uuid(UUID.randomUUID());
			config.setName(name);
			config.setType("GLOBAL");
			config.setValue("10");
			return config;
		}
		return null;
	}
}
