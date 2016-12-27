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

public class AddGameConfigArenaToSpleggTaurusTest  extends TheCraftCloudJUnitTest {
	
    
    /**
     * 
     * NAO ESQUECER DE CRIAR 
     * ARENA
     * SCHEMATIC
     * GAMEWORLD
     * 
     * A AREA NAO PODE TER NOME DUPLICADO
     * 
     */
	@Test
	public void test() {
		Game game = delegate.findGameByName("splegg");
		Arena arena = delegate.findArenaByName("splegg-taurus");
		Local pointA = createLocal( -711, 58, -678 );
		Local pointB = createLocal( -831, 37, -591 );
		Area3D area = createArea(pointA, pointB);
		arena.setArea(area);
		delegate.updateArena(arena);
	}
	
	private Area3D createArea(Local pointA, Local pointB) {
		Area3D area = new Area3D();
		area.setPointA(pointA);
		area.setPointB(pointB);
		area.setName("area splegg-taurus");
		area = delegate.addArea3D(area);
		return area;
	}

	private void createSpleggTaurusSpawn() {
		Game game = delegate.findGameByName("splegg");
		Arena arena = delegate.findArenaByName("splegg-taurus");

		GameConfig gameConfig = delegate.findGameConfigByName("splegg.player1.spawn");
		Local pointA = createLocal(-766, 44, -620 );
		createGameArenaConfig( gameConfig, arena, game, pointA );

		gameConfig = delegate.findGameConfigByName("splegg.player2.spawn");
		pointA = createLocal( -766, 44, -640 );
		createGameArenaConfig( gameConfig, arena, game, pointA );

		gameConfig = delegate.findGameConfigByName("splegg.player3.spawn");
		pointA = createLocal( -790, 45, -597 );
		createGameArenaConfig( gameConfig, arena, game, pointA );

		gameConfig = delegate.findGameConfigByName("splegg.player4.spawn");
		pointA = createLocal( -800, 47, -620 );
		createGameArenaConfig( gameConfig, arena, game, pointA );

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
