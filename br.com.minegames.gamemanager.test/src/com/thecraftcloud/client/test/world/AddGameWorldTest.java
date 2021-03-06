package com.thecraftcloud.client.test.world;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.client.test.TheCraftCloudJUnitTest;
import com.thecraftcloud.core.domain.GameWorld;

public class AddGameWorldTest  extends TheCraftCloudJUnitTest {
	
	
	@Test
	public void test() {

		/*
		GameWorld gw = new GameWorld();
		gw.setName("gungame-space-station");
		gw.setDescription("arena de batalha do gungame que simula uma estacao espacial");
		
		delegate.addGameWorld( gw );
*/
		
		/*
		GameWorld gw = new GameWorld();
		gw.setName("arqueiro-pokemon-go");
		gw.setDescription("arena estilo pokemon-go para o TheLastArcher");

		GameWorld gw = new GameWorld();
		gw.setName("thearcher.stadium");
		gw.setDescription("Arena Stadium para o jogo TheLastArcher");
		gw.setPath("/opt/mg/worlds/thearcher-stadium.zip");
		delegate.addGameWorld( gw );
		*/
		//createArathiWorld();
		//createSpleggOrbitWorld();
		//createSpleggTaurusWorld();
		createTheArcherOriginalWorld();
		
	}
	
	private void createArathiWorld() {
		GameWorld gw = new GameWorld();
		gw.setName("arathi");
		gw.setDescription("Arena para o jogo domination (Arathi Basin)");
		gw.setPath("/opt/mg/worlds/arathi.zip");
		delegate.addGameWorld( gw );
	}

	private void createSpleggOrbitWorld() {
		GameWorld gw = new GameWorld();
		gw.setName("splegg-orbit");
		gw.setDescription("Arena Orbit para o jogo Splegg");
		gw.setPath("/opt/mg/worlds/splegg-orbit.zip");
		delegate.addGameWorld( gw );
	}

	private void createSpleggTaurusWorld() {
		GameWorld gw = new GameWorld();
		gw.setName("splegg-taurus");
		gw.setDescription("Arena Taurus para o jogo Splegg");
		gw.setPath("/opt/mg/worlds/splegg-taurus.zip");
		delegate.addGameWorld( gw );
	}

	private void createTheArcherOriginalWorld() {
		GameWorld gw = new GameWorld();
		gw.setName("thearcher-original");
		gw.setDescription("Arena Original para o jogo TheArcher");
		gw.setPath("/opt/mg/worlds/thearcher-original.zip");
		delegate.addGameWorld( gw );
	}


}
