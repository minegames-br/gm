package com.thecraftcloud.test;

import com.thecraftcloud.client.GameManagerDelegate;
import com.thecraftcloud.core.domain.Area3D;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.domain.GameConfig;
import com.thecraftcloud.core.domain.GameConfigInstance;
import com.thecraftcloud.core.domain.GameConfigScope;
import com.thecraftcloud.core.domain.GameConfigType;
import com.thecraftcloud.core.domain.Local;
import com.thecraftcloud.core.domain.Schematic;

public class AddGameConfigRESTPostTest {

	public static final String URL_SERVICES = "http://services.minegames.com.br:8080/gamemanager/webresources";
	//public static final String URL_SERVICES = "http://localhost:8080/gamemanager/webresources";
	//String restURL = "";
    private static GameManagerDelegate delegate = GameManagerDelegate.getInstance(URL_SERVICES);
	
	public static final String FLOATING_AREA = "ARQUEIRO-FLOATING-AREA";
	public static final String MONSTERS_SPAWN_AREA = "ARQUEIRO-MONSTERS-SPAWN-AREA";
	public static final String LOBBY_LOCATION = "ARQUEIRO-LOBBY-LOCATION";
	public static final String MAX_ZOMBIE_SPAWNED_PER_PLAYER = "ARQUEIRO-MAX-ZOMBIE-SPAWNED-PER-PLAYER";
	public static final String MAX_TARGET = "ARQUEIRO-MAX-TARGET";
	public static final String MAX_MOVING_TARGET = "ARQUEIRO-MAX-MOVING-TARGET";
	public static final String MIN_PLAYERS = "ARQUEIRO-MIN-PLAYERS";
	public static final String MAX_PLAYERS = "ARQUEIRO-MAX-PLAYERS";
	public static final String ARENA = "ARQUEIRO-ARENA";
	public static final String START_COUNTDOWN = "ARQUEIRO-START-COUNTDOWN";
	public static final String GAME_DURATION = "ARQUEIRO-GAME-DURATION";
	public static final String GAME_LEVELS = "ARQUEIRO-GAME-NUMBER-OF-LEVELS";

	public static void main2(String args[]) {
		GameManagerDelegate delegate = GameManagerDelegate.getInstance(URL_SERVICES);
		Game game = delegate.findGame("c6905743-6514-49ba-9257-420743f65b65");
		Arena arena = delegate.findArena("c5253674-8c19-4620-b500-51645a620f64");
		
		delegate.updateGame(game);
	}
	
	public static void main(String args[]) {
		
		GameManagerDelegate delegate = GameManagerDelegate.getInstance(URL_SERVICES);

		createGameConfig();
        
	}
	
	public static void createGameConfig() {
		GameConfig gameConfig = null;
		Local pointA = null;
		Local pointB = null;

		Game game = delegate.findGame("00000000-0000-0000-0000-000000000000");
		
		System.out.println("creating configs");
		gameConfig = createConfig("thelastarcher.global.startcountdown", "Countdown", "", "", GameConfigType.INT, GameConfigScope.GLOBAL, game );
		gameConfig = createConfig(MIN_PLAYERS, "Min # of Players", "Min number of players", "", GameConfigType.INT, GameConfigScope.GLOBAL , game);
		gameConfig = createConfig(MAX_PLAYERS, "Max # of players", "Max # of players", "", GameConfigType.INT, GameConfigScope.GLOBAL , game);
		gameConfig = createConfig(START_COUNTDOWN, "Countdown", "Countdown to start the game in seconds", "", GameConfigType.INT, GameConfigScope.GLOBAL , game);
		gameConfig = createConfig(GAME_DURATION, "Duration in minutes", "Duration in minutes", "", GameConfigType.INT, GameConfigScope.GLOBAL , game);
		gameConfig = createConfig(GAME_LEVELS, "# of Rounds", "Not in use", "", GameConfigType.INT, GameConfigScope.GLOBAL , game);
		gameConfig = createConfig(FLOATING_AREA, "Select the are to spawn floating targets", "3D area where the floating targets will spawn", "", GameConfigType.AREA3D, GameConfigScope.ARENA, game );
		gameConfig = createConfig(MONSTERS_SPAWN_AREA, "Select the area to spawn mobs", "3D (flat) area for spawning mobs", "", GameConfigType.AREA3D, GameConfigScope.ARENA, game );
		gameConfig = createConfig(LOBBY_LOCATION, "Select the location to teleport players after the game", "Location to where players will be teleported after leaving the game", "", GameConfigType.LOCAL, GameConfigScope.ARENA, game );
		gameConfig = createConfig(MAX_ZOMBIE_SPAWNED_PER_PLAYER, "Max # of zombies per player", "Max # of zombies to spawn per player in game at the same time", "", GameConfigType.INT, GameConfigScope.ARENA , game);
		gameConfig = createConfig(MAX_TARGET, "Max # of targets", "Max # of targets in the arena at the same time", "", GameConfigType.INT, GameConfigScope.ARENA , game);
		gameConfig = createConfig(MAX_MOVING_TARGET, "Max # of bonus targets", "Max # of bonus targets", "", GameConfigType.INT, GameConfigScope.ARENA, game );
		gameConfig = createConfig("arqueiro.player1.spawn", "Select the location 1 to spawn a player", "Location where the player will spawn", "PLAYER-SPAWN", GameConfigType.LOCAL, GameConfigScope.ARENA, game);
		gameConfig = createConfig("arqueiro.player2.spawn", "Select the location 2 to spawn a player", "Location where the player will spawn", "PLAYER-SPAWN",  GameConfigType.LOCAL, GameConfigScope.ARENA, game);
		gameConfig = createConfig("arqueiro.player3.spawn", "Select the location 3 to spawn a player", "Location where the player will spawn", "PLAYER-SPAWN",  GameConfigType.LOCAL, GameConfigScope.ARENA, game);
		gameConfig = createConfig("arqueiro.player4.spawn", "Select the location 4 to spawn a player", "Location where the player will spawn", "PLAYER-SPAWN",  GameConfigType.LOCAL, GameConfigScope.ARENA, game);
		gameConfig = createConfig("arqueiro.player1.area", "Select the area for player 1", "3D (flat) area that the player will have to protect","PLAYER-AREA", GameConfigType.AREA3D, GameConfigScope.ARENA, game);
		gameConfig = createConfig("arqueiro.player2.area", "Select the area for player 2", "3D (flat) area that the player will have to protect", "PLAYER-AREA", GameConfigType.AREA3D, GameConfigScope.ARENA, game);
		gameConfig = createConfig("arqueiro.player3.area", "Select the area for player 3", "3D (flat) area that the player will have to protect", "PLAYER-AREA", GameConfigType.AREA3D, GameConfigScope.ARENA, game);
		gameConfig = createConfig("arqueiro.player4.area", "Select the area for player 4", "3D (flat) area that the player will have to protect", "PLAYER-AREA", GameConfigType.AREA3D, GameConfigScope.ARENA, game);
	}
	
	public static void createArenaconfig() {
        
		System.err.println("creating schematic");
        Schematic schematic = new Schematic();
        schematic.setName("thearcher.pokemon-go.arena");
        schematic.setDescription("Export file of the pokemon-go arena for The Archer");
        schematic.setPath("d:/minecraft/worlds/schematics/S2.schematic");
        
        schematic = delegate.createSchematic(schematic);
        
		System.err.println("creating arena");
        Arena arena = new Arena();
        arena.setName("thearcher.pokemon-go");
        arena.setDescription("The Archer - Arena Pokemon Go");
        arena.setSchematic(schematic);
        
        arena = delegate.createArena(arena);
		
		Game game = delegate.findGame("2ca9458b-1e97-4dbf-b634-76fe1a8a54d5");
		/*
		Arena arena = delegate.findArena("b975ba9f-e350-4563-8182-045e34aed7a8");
		*/
		GameConfig gameConfig = null;
		Local pointA = null;
		Local pointB = null;
		
		System.out.println("creating configs");
		gameConfig = createConfig("thelastarcher.global.startcountdown", "Countdown", "", "", GameConfigType.INT, GameConfigScope.GLOBAL, game );
		createGameConfigInstance( gameConfig, 28 );

		gameConfig = createConfig(MIN_PLAYERS, "Min # of Players", "Min number of players", "", GameConfigType.INT, GameConfigScope.GLOBAL , game);
		createGameConfigInstance( gameConfig, 1 );

		gameConfig = createConfig(MAX_PLAYERS, "Max # of players", "Max # of players", "", GameConfigType.INT, GameConfigScope.GLOBAL , game);
		createGameConfigInstance( gameConfig, 4 );

		gameConfig = createConfig(START_COUNTDOWN, "Countdown", "Countdown to start the game in seconds", "", GameConfigType.INT, GameConfigScope.GLOBAL , game);
		createGameConfigInstance( gameConfig, 30 );

		gameConfig = createConfig(GAME_DURATION, "Duration in minutes", "Duration in minutes", "", GameConfigType.INT, GameConfigScope.GLOBAL , game);
		createGameConfigInstance( gameConfig, 3 );
		
		gameConfig = createConfig(GAME_LEVELS, "# of Rounds", "Not in use", "", GameConfigType.INT, GameConfigScope.GLOBAL , game);
		createGameConfigInstance( gameConfig, 10 );

		gameConfig = createConfig(FLOATING_AREA, "Select the are to spawn floating targets", "3D area where the floating targets will spawn", "", GameConfigType.AREA3D, GameConfigScope.ARENA, game );
		//gameConfig = delegate.findGameConfig(game.getGame_uuid().toString(), "6167c500-a8e8-43b0-9774-11f25d7a49f8");
		pointA = createLocal( -662, 8, 376 );
		pointB = createLocal( -629, 20, 360 );
		Area3D area = createArea3D( FLOATING_AREA, pointA, pointB );
		//Area3D area = delegate.findArea3D("13aca44e-a639-4441-bc20-9b2554c56e7b");
		createGameArenaConfig( gameConfig, arena, area );
		
		gameConfig = createConfig(MONSTERS_SPAWN_AREA, "Select the area to spawn mobs", "3D (flat) area for spawning mobs", "", GameConfigType.AREA3D, GameConfigScope.ARENA, game );
		pointA = createLocal( -662, 6, 376 );
		pointB = createLocal( -629, 6, 360 );
		area = createArea3D( MONSTERS_SPAWN_AREA, pointA, pointB );
		createGameArenaConfig( gameConfig, arena, area );
		
		gameConfig = createConfig(LOBBY_LOCATION, "Select the location to teleport players after the game", "Location to where players will be teleported after leaving the game", "", GameConfigType.LOCAL, GameConfigScope.ARENA, game );
		pointA = createLocal( -662, 6, 376 );
		createGameArenaConfig( gameConfig, arena, pointA );
		
		gameConfig = createConfig(MAX_ZOMBIE_SPAWNED_PER_PLAYER, "Max # of zombies per player", "Max # of zombies to spawn per player in game at the same time", "", GameConfigType.INT, GameConfigScope.ARENA , game);
		createGameArenaConfig( gameConfig, arena, 5 );
		
		gameConfig = createConfig(MAX_TARGET, "Max # of targets", "Max # of targets in the arena at the same time", "", GameConfigType.INT, GameConfigScope.ARENA , game);
		createGameArenaConfig( gameConfig, arena, 5 );

		gameConfig = createConfig(MAX_MOVING_TARGET, "Max # of bonus targets", "Max # of bonus targets", "", GameConfigType.INT, GameConfigScope.ARENA, game );
		createGameArenaConfig( gameConfig, arena, 1 );

		gameConfig = createConfig("arqueiro.player1.spawn", "Select the location 1 to spawn a player", "Location where the player will spawn", "PLAYER-SPAWN", GameConfigType.LOCAL, GameConfigScope.ARENA, game);
		pointA = createLocal( -659, 4, 396 );
		createGameArenaConfig( gameConfig, arena, pointA );

		gameConfig = createConfig("arqueiro.player2.spawn", "Select the location 2 to spawn a player", "Location where the player will spawn", "PLAYER-SPAWN",  GameConfigType.LOCAL, GameConfigScope.ARENA, game);
		pointA = createLocal( -649, 4, 396 );
		createGameArenaConfig( gameConfig, arena, pointA );

		gameConfig = createConfig("arqueiro.player3.spawn", "Select the location 3 to spawn a player", "Location where the player will spawn", "PLAYER-SPAWN",  GameConfigType.LOCAL, GameConfigScope.ARENA, game);
		pointA = createLocal( -639, 4, 396 );
		createGameArenaConfig( gameConfig, arena, pointA );

		gameConfig = createConfig("arqueiro.player4.spawn", "Select the location 4 to spawn a player", "Location where the player will spawn", "PLAYER-SPAWN",  GameConfigType.LOCAL, GameConfigScope.ARENA, game);
		pointA = createLocal( -629, 4, 396 );
		createGameArenaConfig( gameConfig, arena, pointA );
		
		gameConfig = createConfig("arqueiro.player1.area", "Select the area for player 1", "3D (flat) area that the player will have to protect","PLAYER-AREA", GameConfigType.AREA3D, GameConfigScope.ARENA, game);
		pointA = createLocal( -662, 4, 398 );
		pointB = createLocal( -657, 7, 392 );
		area = createArea3D( "arqueiro.player1.area", pointA, pointB );
		createGameArenaConfig( gameConfig, arena, area );
		
		gameConfig = createConfig("arqueiro.player2.area", "Select the area for player 2", "3D (flat) area that the player will have to protect", "PLAYER-AREA", GameConfigType.AREA3D, GameConfigScope.ARENA, game);
		pointA = createLocal( -652, 4, 398 );
		pointB = createLocal( -647, 7, 392 );
		area = createArea3D( "arqueiro.player2.area", pointA, pointB );
		createGameArenaConfig( gameConfig, arena, area );

		gameConfig = createConfig("arqueiro.player3.area", "Select the area for player 3", "3D (flat) area that the player will have to protect", "PLAYER-AREA", GameConfigType.AREA3D, GameConfigScope.ARENA, game);
		pointA = createLocal( -642, 4, 398 );
		pointB = createLocal( -637, 7, 392 );
		area = createArea3D( "arqueiro.player3.area", pointA, pointB );
		createGameArenaConfig( gameConfig, arena, area );

		gameConfig = createConfig("arqueiro.player4.area", "Select the area for player 4", "3D (flat) area that the player will have to protect", "PLAYER-AREA", GameConfigType.AREA3D, GameConfigScope.ARENA, game);
		pointA = createLocal( -632, 4, 398 );
		pointB = createLocal( -627, 7, 392 );
		area = createArea3D( "arqueiro.player4.area", pointA, pointB );
		createGameArenaConfig( gameConfig, arena, area );
		
	}
	
	private static void createGameArenaConfig(GameConfig gameConfig, Arena arena, Object object) {
		GameArenaConfig gac = new GameArenaConfig();
		gac.setArena(arena);
		gac.setGameConfig(gameConfig);
		
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

	private static Area3D createArea3D(String name, Local pointA, Local pointB) {
		Area3D area = new Area3D(pointA, pointB);
		area.setName(name);
		area = delegate.addArea3D(area);
		System.out.println("createArea3D: " + area.getArea_uuid());
		return area;
	}

	public static GameConfig createConfig(String name, String displayName, String description, String group, GameConfigType type, GameConfigScope scope, Game game ) {
		String restURL = URL_SERVICES;
        GameManagerDelegate delegate = GameManagerDelegate.getInstance(restURL);

		System.out.println("creating config " + name);

        //Game game = delegate.findGame("57b7b3df-9d18-4966-898f-f4ad8ee28a92");
        GameConfig domain = new GameConfig();
        domain.setConfigScope(scope);
        domain.setConfigType(type);
        domain.setName(name);
        domain.setDescription(description);
        domain.setDisplayName(displayName);
        domain.setGame(game);
        domain.setGroup(group);

        GameConfig gameConfig = delegate.addGameConfig(domain);
        return gameConfig;
	}
	
	public static void createGameConfigInstance(GameConfig gameConfig, Object object ) {
		String restURL = URL_SERVICES;
        GameConfigInstance domain = new GameConfigInstance();
        System.out.println("game_config_uuid: " + gameConfig.getGame_config_uuid());
        domain.setGameConfig(gameConfig);

        if(object instanceof Integer) {
        	Integer intValue = (Integer)object;
        	domain.setIntValue(intValue);
        	delegate.createGameConfigInstance(domain);
        }
        
	}	
	
	public static Local createLocal( int x, int y, int z) {
		Local l = new Local();
		l.setX(x);
		l.setY(y);
		l.setZ(z);

		Local local = delegate.addLocal(l);
		return local;
	}
}
