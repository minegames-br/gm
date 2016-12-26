package com.thecraftcloud.client.test.game;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.client.test.TheCraftCloudJUnitTest;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameConfig;

public class AddGameConfigToGameTest  extends TheCraftCloudJUnitTest {
	
	
	@Test
	public void test() {
		
		configurarSplegg();
		
	}
	
	private void configurarGunGame() {
		Game game = delegate.findGame("d10e8c62-6124-4952-a054-c7c668e7944f");
		for(int i = 4; i < 14; i++) {
			GameConfig gc = delegate.findGameConfigByName("GUNGAME." + i);
			delegate.addGameConfigToGame( game, gc);
		}
	}

	private void configurarSplegg() {
		Game game = delegate.findGameByName("splegg");
		
		addGameConfigToGame("player1.spawn", game);
		addGameConfigToGame("player2.spawn", game);
		addGameConfigToGame("player3.spawn", game);
		addGameConfigToGame("player4.spawn", game);
		addGameConfigToGame("player5.spawn", game);
		addGameConfigToGame("player6.spawn", game);
		addGameConfigToGame("player7.spawn", game);
		addGameConfigToGame("player8.spawn", game);
		addGameConfigToGame("player9.spawn", game);
		addGameConfigToGame("player10.spawn", game);
		addGameConfigToGame("player11.spawn", game);
		addGameConfigToGame("player12.spawn", game);
		addGameConfigToGame("player13.spawn", game);
		addGameConfigToGame("player14.spawn", game);
		addGameConfigToGame("player15.spawn", game);
		addGameConfigToGame("player16.spawn", game);
		addGameConfigToGame("player17.spawn", game);
		addGameConfigToGame("player18.spawn", game);
		addGameConfigToGame("player19.spawn", game);
		addGameConfigToGame("player20.spawn", game);
	}
	
	private void configurarArqueiro() {
		Game game = delegate.findGameByName("TheArcher");
		
		addGameConfigToGame("ARQUEIRO-FLOATING-AREA", game);
		addGameConfigToGame("ARQUEIRO-GAME-DURATION", game);
		addGameConfigToGame("ARQUEIRO-GAME-NUMBER-OF-LEVELS", game);
		addGameConfigToGame("ARQUEIRO-LOBBY-LOCATION", game);
		addGameConfigToGame("ARQUEIRO-MAX-MOVING-TARGET", game);
		addGameConfigToGame("ARQUEIRO-MAX-PLAYERS", game);
		addGameConfigToGame("ARQUEIRO-MAX-TARGET", game);
		addGameConfigToGame("ARQUEIRO-MAX-ZOMBIE-SPAWNED-PER-PLAYER", game);
		addGameConfigToGame("ARQUEIRO-MIN-PLAYERS", game);
		addGameConfigToGame("ARQUEIRO-MONSTERS-SPAWN-AREA", game);
		addGameConfigToGame("ARQUEIRO-START-COUNTDOWN", game);
		addGameConfigToGame("arqueiro.player1.area", game);
		addGameConfigToGame("arqueiro.player1.spawn", game);
		addGameConfigToGame("arqueiro.player2.area", game);
		addGameConfigToGame("arqueiro.player2.spawn", game);
		addGameConfigToGame("arqueiro.player3.area", game);
		addGameConfigToGame("arqueiro.player3.spawn", game);
		addGameConfigToGame("arqueiro.player4.area", game);
		addGameConfigToGame("arqueiro.player4.spawn", game);
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

}
