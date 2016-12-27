package com.thecraftcloud.test;

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
import com.thecraftcloud.core.domain.Item;
import com.thecraftcloud.core.domain.Local;

public class AddGunGameGameConfig  extends TheCraftCloudJUnitTest {

	public static final String LOBBY_LOCATION = "LOBBY-LOCATION";
	public static final String MIN_PLAYERS = "MIN-PLAYERS";
	public static final String MAX_PLAYERS = "MAX-PLAYERS";
	public static final String START_COUNTDOWN = "START-COUNTDOWN";
	public static final String GAME_DURATION = "GAME-DURATION-IN-SECONDS";
	public static final String KILL_POINTS = "KILL-POINTS";

	@Test
	public void test() throws InvalidRegistrationException {
		
		createGameConfig();
        
	}
	
	public void createGameConfig() throws InvalidRegistrationException {
		GameConfig gameConfig = null;
		Local pointA = null;
		Local pointB = null;
		Item item = null;
		String material = "";
		
		//gun game
		Game game = delegate.findGameByName("gungame");
		Arena arena = delegate.findArena("30d00221-b371-4828-a0e6-5d75de7bfaec");

		gameConfig = createConfig("gungame.player1.spawn", "Select the location 1 to spawn a player", "Location where the player will spawn", "PLAYER-SPAWN", GameConfigType.LOCAL, GameConfigScope.ARENA, game);
		pointA = createLocal( -191, 53, -211 );
		createGameArenaConfig( gameConfig, game, arena, pointA );

		gameConfig = createConfig("gungame.player2.spawn", "Select the location 2 to spawn a player", "Location where the player will spawn", "PLAYER-SPAWN",  GameConfigType.LOCAL, GameConfigScope.ARENA, game);
		pointA = createLocal( -193, 53, -182 );
		createGameArenaConfig( gameConfig, game, arena, pointA );

		gameConfig = createConfig("gungame.player3.spawn", "Select the location 3 to spawn a player", "Location where the player will spawn", "PLAYER-SPAWN",  GameConfigType.LOCAL, GameConfigScope.ARENA, game);
		pointA = createLocal( -163, 65, -177 );
		createGameArenaConfig( gameConfig, game, arena, pointA );

		gameConfig = createConfig("gungame.player4.spawn", "Select the location 4 to spawn a player", "Location where the player will spawn", "PLAYER-SPAWN",  GameConfigType.LOCAL, GameConfigScope.ARENA, game);
		pointA = createLocal( -198, 54, -197 );
		createGameArenaConfig( gameConfig, game, arena, pointA );
		
		/*
		System.out.println("creating configs");
		gameConfig = createConfig(MIN_PLAYERS, "Min # of Players", "Min number of players", "", GameConfigType.INT, GameConfigScope.GLOBAL , game);
		createGameConfigInstance( gameConfig, game, 1 );

		gameConfig = createConfig(MAX_PLAYERS, "Max # of players", "Max # of players", "", GameConfigType.INT, GameConfigScope.GLOBAL , game);
		createGameConfigInstance( gameConfig, game,4 );

		gameConfig = createConfig(START_COUNTDOWN, "Countdown", "Countdown to start the game in seconds", "", GameConfigType.INT, GameConfigScope.GLOBAL , game);
		createGameConfigInstance( gameConfig,game, 30 );

		gameConfig = createConfig(GAME_DURATION, "Duration in seconds", "Duration in seconds", "", GameConfigType.INT, GameConfigScope.GLOBAL , game);
		createGameConfigInstance( gameConfig,game, 3 );
		
		gameConfig = createConfig(KILL_POINTS, "Points per kill", "Points per kill", "", GameConfigType.INT, GameConfigScope.GLOBAL , game);
		createGameConfigInstance( gameConfig,game, 100 );

		int i =0;
		item = delegate.findItemByName( "IRON_AXE" ); i = 1;
		gameConfig = createConfig("GUNGAME." + i, "Item earned at level " + i, "Item earned at level " + i, "GUNGAME.LEVEL", GameConfigType.ITEM, GameConfigScope.GLOBAL , game);
		createGameConfigInstance( gameConfig, game,item );
		
		material = "DIAMOND_AXE"; item = delegate.findItemByName( material ); i = 2;
		if(item == null) System.err.println("não encontrou: " + material );
		gameConfig = createConfig("GUNGAME." + i, "Item earned at level " + i, "Item earned at level " + i, "GUNGAME.LEVEL", GameConfigType.ITEM, GameConfigScope.GLOBAL , game);
		createGameConfigInstance( gameConfig, game,item );

		material = "IRON_SWORD"; item = delegate.findItemByName( material ); i = 3;
		if(item == null) System.err.println("não encontrou: " + material );
		gameConfig = createConfig("GUNGAME." + i, "Item earned at level " + i, "Item earned at level " + i, "GUNGAME.LEVEL", GameConfigType.ITEM, GameConfigScope.GLOBAL , game);
		createGameConfigInstance( gameConfig, game,item );

		item = delegate.findItemByName( "IRON_BOOTS" ); i = 4;
		if(item == null) System.err.println("não encontrou: " + material );
		gameConfig = createConfig("GUNGAME." + i, "Item earned at level " + i, "Item earned at level " + i, "GUNGAME.LEVEL", GameConfigType.ITEM, GameConfigScope.GLOBAL , game);
		createGameConfigInstance( gameConfig, game,item );

		item = delegate.findItemByName( "IRON_CHESTPLATE" ); i = 5;
		gameConfig = createConfig("GUNGAME." + i, "Item earned at level " + i, "Item earned at level " + i, "GUNGAME.LEVEL", GameConfigType.ITEM, GameConfigScope.GLOBAL , game);
		createGameConfigInstance( gameConfig, game,item );

		item = delegate.findItemByName( "IRON_LEGGINGS" ); i = 6;
		gameConfig = createConfig("GUNGAME." + i, "Item earned at level " + i, "Item earned at level " + i, "GUNGAME.LEVEL", GameConfigType.ITEM, GameConfigScope.GLOBAL , game);
		createGameConfigInstance( gameConfig, game,item );

		item = delegate.findItemByName( "IRON_HELMET" ); i = 7;
		gameConfig = createConfig("GUNGAME." + i, "Item earned at level " + i, "Item earned at level " + i, "GUNGAME.LEVEL", GameConfigType.ITEM, GameConfigScope.GLOBAL , game);
		createGameConfigInstance( gameConfig, game,item );

		item = delegate.findItemByName( "DIAMOND_SWORD" ); i++;
		gameConfig = createConfig("GUNGAME." + i, "Item earned at level " + i, "Item earned at level " + i, "GUNGAME.LEVEL", GameConfigType.ITEM, GameConfigScope.GLOBAL , game);
		createGameConfigInstance( gameConfig, game,item );

		item = delegate.findItemByName( "DIAMOND_BOOTS" ); i++;
		gameConfig = createConfig("GUNGAME." + i, "Item earned at level " + i, "Item earned at level " + i, "GUNGAME.LEVEL", GameConfigType.ITEM, GameConfigScope.GLOBAL , game);
		createGameConfigInstance( gameConfig, game,item );

		item = delegate.findItemByName( "DIAMOND_CHESTPLATE" ); i++;
		gameConfig = createConfig("GUNGAME." + i, "Item earned at level " + i, "Item earned at level " + i, "GUNGAME.LEVEL", GameConfigType.ITEM, GameConfigScope.GLOBAL , game);
		createGameConfigInstance( gameConfig, game,item );

		item = delegate.findItemByName( "DIAMOND_LEGGINGS" ); i++;
		gameConfig = createConfig("GUNGAME." + i, "Item earned at level " + i, "Item earned at level " + i, "GUNGAME.LEVEL", GameConfigType.ITEM, GameConfigScope.GLOBAL , game);
		createGameConfigInstance( gameConfig, game,item );

		item = delegate.findItemByName( "DIAMOND_HELMET" ); i++;
		gameConfig = createConfig("GUNGAME." + i, "Item earned at level " + i, "Item earned at level " + i, "GUNGAME.LEVEL", GameConfigType.ITEM, GameConfigScope.GLOBAL , game);
		createGameConfigInstance( gameConfig, game,item );

		item = delegate.findItemByName( "BOW" ); i++;
		gameConfig = createConfig("GUNGAME." + i, "Item earned at level " + i, "Item earned at level " + i, "GUNGAME.LEVEL", GameConfigType.ITEM, GameConfigScope.GLOBAL , game);
		createGameConfigInstance( gameConfig, game,item );
	
		/*
		int i = 1;		

		/*
		System.out.println("creating configs");
		gameConfig = createConfig(MIN_PLAYERS, "Min # of Players", "Min number of players", "", GameConfigType.INT, GameConfigScope.GLOBAL , game);
		gameConfig = createConfig(MAX_PLAYERS, "Max # of players", "Max # of players", "", GameConfigType.INT, GameConfigScope.GLOBAL , game);
		gameConfig = createConfig(START_COUNTDOWN, "Countdown", "Countdown to start the game in seconds", "", GameConfigType.INT, GameConfigScope.GLOBAL , game);
		gameConfig = createConfig(LOBBY_LOCATION, "Select the location to teleport players after the game", "Location to where players will be teleported after leaving the game", "", GameConfigType.LOCAL, GameConfigScope.ARENA, game );
		
		Arena arena = delegate.findArena("30d00221-b371-4828-a0e6-5d75de7bfaec");
		
		gameConfig = createConfig(LOBBY_LOCATION, "Select the location to teleport players after the game", "Location to where players will be teleported after leaving the game", "", GameConfigType.LOCAL, GameConfigScope.ARENA, game );
		pointA = createLocal( -198, 53, -195 );
		createGameArenaConfig( gameConfig, arena, pointA );
	*/	
	}
	
	
	private void createGameArenaConfig(GameConfig gameConfig, Game game, Arena arena, Object object) {
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

	private Area3D createArea3D(String name, Local pointA, Local pointB) {
		Area3D area = new Area3D(pointA, pointB);
		area.setName(name);
		area = delegate.addArea3D(area);
		System.out.println("createArea3D: " + area.getArea_uuid());
		return area;
	}

	public GameConfig createConfig(String name, String displayName, String description, String group, GameConfigType type, GameConfigScope scope, Game game ) {
		System.out.println("creating config " + name);

        //Game game = delegate.findGame("57b7b3df-9d18-4966-898f-f4ad8ee28a92");
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
	
	public void createGameConfigInstance(GameConfig gameConfig, Game game, Object object ) {
        GameConfigInstance domain = new GameConfigInstance();
        System.out.println("game_config_uuid: " + gameConfig.getGame_config_uuid());
        domain.setGameConfig(gameConfig);
        domain.setGame(game);

        if(object instanceof Integer) {
        	Integer intValue = (Integer)object;
        	domain.setIntValue(intValue);
        	delegate.createGameConfigInstance(domain);
        } else if( object instanceof Item) {
        	Item item = (Item) object;
        	domain.setItem(item);
        	delegate.createGameConfigInstance(domain);
        } else {
        	System.err.println("so cria config instance do tipo integer e item.");
        }
        
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
