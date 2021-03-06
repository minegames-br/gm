package com.thecraftcloud.client.test.game;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.client.exception.InvalidRegistrationException;
import com.thecraftcloud.client.test.TheCraftCloudJUnitTest;
import com.thecraftcloud.core.domain.Area3D;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.domain.GameConfig;
import com.thecraftcloud.core.domain.GameConfigInstance;
import com.thecraftcloud.core.domain.GameConfigScope;
import com.thecraftcloud.core.domain.GameConfigType;
import com.thecraftcloud.core.domain.Local;

public class AddGameConfigToDominationTest  extends TheCraftCloudJUnitTest {
	

	public static final String MIN_PLAYERS = "MIN-PLAYERS";
	public static final String MAX_PLAYERS = "MAX-PLAYERS";
	public static final String START_COUNTDOWN = "START-COUNTDOWN";
	public static final String GAME_DURATION = "GAME-DURATION-IN-SECONDS";

	@Test
	public void test() {
		
		Game game = delegate.findGameByName("domination");
		Arena arena = delegate.findArenaByName("arathi");
		//configurarDomination(game);
		//createGameConfig(game);
		
		GameConfig gameConfig = delegate.findGameConfigByName("domination.waiting.lobby"); //createConfig("domination.waiting.lobby", "Select the location to spawn players before the game", "Location where the players will spawn before the game", "", GameConfigType.LOCAL, GameConfigScope.ARENA, game);
		Local pointA = createLocal( 0, 6, 0 );
		createGameArenaConfig( gameConfig, arena, game, pointA );	
	}
	
	private void configurarDomination(Game game) {
		addGameConfigToGame("GAME-DURATION-IN-SECONDS", game);
		addGameConfigToGame("MAX-PLAYERS", game);
		addGameConfigToGame("MIN-PLAYERS", game);
		addGameConfigToGame("START-COUNTDOWN", game);
	}
	
	private void addGameConfigToGame( String name, Game game ) {
		GameConfig gc = null;
		gc = delegate.findGameConfigByName(name);
		delegate.addGameConfigToGame( game, gc);
	}

	public void createGameConfig(Game game) {
		GameConfig gameConfig = null;
		
		System.out.println("creating configs");
		gameConfig = delegate.findGameConfigByName(MIN_PLAYERS);
		createGameConfigInstance( game, gameConfig, 3 );

		gameConfig = delegate.findGameConfigByName(MAX_PLAYERS);
		createGameConfigInstance( game, gameConfig, 25 );

		gameConfig = delegate.findGameConfigByName(START_COUNTDOWN);
		createGameConfigInstance( game, gameConfig, 20 );

		gameConfig = delegate.findGameConfigByName(GAME_DURATION);
		createGameConfigInstance( game, gameConfig, 120 );
	}

	public void createGameConfigInstance(Game game, GameConfig gameConfig, Object object ) {
        GameConfigInstance domain = new GameConfigInstance();
        System.out.println("game_config_uuid: " + gameConfig.getGame_config_uuid());
        domain.setGameConfig(gameConfig);
        domain.setGame(game);

        if(object instanceof Integer) {
        	Integer intValue = (Integer)object;
        	domain.setIntValue(intValue);
        	delegate.createGameConfigInstance(domain);
        }
        
	}	
	
	private void createGameArenaConfig(GameConfig gameConfig, Arena arena, Game game, Object object) {
		GameArenaConfig gac = new GameArenaConfig();
		gac.setArena(arena);
		gac.setGameConfig(gameConfig);
		gac.setGame(game);
		
		if(object instanceof Integer) {
			gac.setIntValue( (Integer)object );
		} else if( object instanceof Local) {
			gac.setLocalValue((Local)object);
		} else if( object instanceof Area3D) {
			Area3D area = (Area3D)object;
			System.out.println(area.getName() + " - " + area.getArea_uuid());
			gac.setAreaValue(area);
		}
		
		delegate.createGameArenaConfig(gac);
	}

	public GameConfig createConfig(String name, String displayName, String description, String group, GameConfigType type, GameConfigScope scope, Game game ) {
		System.out.println("creating config " + name);

        GameConfig domain = new GameConfig();
        domain.setConfigScope(scope);
        domain.setConfigType(type);
        domain.setName(name);
        domain.setDescription(description);
        domain.setDisplayName(displayName);
        domain.setGroup(group);

        GameConfig gameConfig = delegate.addGameConfig(domain);
        return gameConfig;
	}
	public Local createLocal( int x, int y, int z) {
		Local l = new Local();
		l.setX(x);
		l.setY(y);
		l.setZ(z);

		Local local = delegate.addLocal(l);
		return local;
	}
	
}
