package br.com.minegames.gamemanager.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.minegames.core.domain.Game;
import br.com.minegames.core.domain.GameConfig;
import br.com.minegames.core.domain.GameConfigScope;
import br.com.minegames.core.domain.GameConfigType;
import br.com.minegames.core.json.JSONParser;
import br.com.minegames.gamemanager.client.GameManagerDelegate;

public class AddGameConfigRESTPostTest {

	//public static final String URL_SERVICES = "http://services.minegames.com.br:8080/gamemanager/webresources";
	public static final String URL_SERVICES = "http://localhost:8080/gamemanager/webresources";
	//String restURL = "";
	
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

	public static void createConfig(String name, String group, GameConfigType type, GameConfigScope scope ) {
		String restURL = URL_SERVICES;
        GameManagerDelegate delegate = GameManagerDelegate.getInstance(restURL);
        //Game game = delegate.findGame("d817590b-e969-44fa-a080-ef00526b94f5");
        Game game = delegate.findGame("57b7b3df-9d18-4966-898f-f4ad8ee28a92");
        GameConfig domain = new GameConfig();
        domain.setConfigScope(scope);
        domain.setConfigType(type);
        domain.setName(name);
        domain.setGame(game);
        domain.setGroup(group);

        delegate.addGameConfig(domain);
	}
	
	public static void main(String args[]) {
		
		GameManagerDelegate delegate = GameManagerDelegate.getInstance(URL_SERVICES);
		
		Game game = new Game();
		game.setName("The Last Archer");
		game.setDescription("The Last Archer Description");
		//delegate.createGame(game);
		
		/*
		createConfig("thelastarcher.global.startcountdown", "", GameConfigType.INT, GameConfigScope.GLOBAL );
		createConfig(FLOATING_AREA, "", GameConfigType.AREA3D, GameConfigScope.ARENA );
		createConfig(MONSTERS_SPAWN_AREA, "", GameConfigType.AREA3D, GameConfigScope.ARENA );
		createConfig(LOBBY_LOCATION, "", GameConfigType.LOCAL, GameConfigScope.ARENA );
		createConfig(MAX_ZOMBIE_SPAWNED_PER_PLAYER, "", GameConfigType.INT, GameConfigScope.ARENA );
		createConfig(MAX_TARGET, "", GameConfigType.INT, GameConfigScope.ARENA );
		createConfig(MAX_MOVING_TARGET, "", GameConfigType.INT, GameConfigScope.ARENA );
		createConfig(MIN_PLAYERS, "", GameConfigType.INT, GameConfigScope.GLOBAL );
		createConfig(MAX_PLAYERS, "", GameConfigType.INT, GameConfigScope.GLOBAL );
		createConfig(START_COUNTDOWN, "", GameConfigType.INT, GameConfigScope.GLOBAL );
		createConfig(GAME_DURATION, "", GameConfigType.INT, GameConfigScope.GLOBAL );
		createConfig(GAME_LEVELS, "", GameConfigType.INT, GameConfigScope.GLOBAL );
		*/
		
		createConfig("arqueiro.player1.spawn", "PLAYER-SPAWN", GameConfigType.LOCAL, GameConfigScope.ARENA);
		createConfig("arqueiro.player2.spawn", "PLAYER-SPAWN", GameConfigType.LOCAL, GameConfigScope.ARENA);
		createConfig("arqueiro.player3.spawn", "PLAYER-SPAWN", GameConfigType.LOCAL, GameConfigScope.ARENA);
		createConfig("arqueiro.player4.spawn", "PLAYER-SPAWN", GameConfigType.LOCAL, GameConfigScope.ARENA);
		
		createConfig("arqueiro.player1.area", "PLAYER-AREA", GameConfigType.AREA3D, GameConfigScope.ARENA);
		createConfig("arqueiro.player2.area", "PLAYER-AREA", GameConfigType.AREA3D, GameConfigScope.ARENA);
		createConfig("arqueiro.player3.area", "PLAYER-AREA", GameConfigType.AREA3D, GameConfigScope.ARENA);
		createConfig("arqueiro.player4.area", "PLAYER-AREA", GameConfigType.AREA3D, GameConfigScope.ARENA);
	}
	
	
}
