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
		Arena arena = delegate.findArenaByName("splegg-orbit");
		/*
		 * 
renatocesar [5:10 PM]  
orbit pointA : 1521 | 65 | -1121

[5:11]  
orbit pointB : 1625 | 15 | -1026
		 */
		Local pointA = createLocal( 1521, 65, -1121 );
		Local pointB = createLocal( 1625, 15, -1026 );
		Area3D area = createArea(pointA, pointB);
		area.setName("splegg-orbit");
		arena.setArea(area);
		delegate.updateArena(arena);
	}
	
	private Area3D createArea(Local pointA, Local pointB) {
		Area3D area = new Area3D();
		area.setPointA(pointA);
		area.setPointB(pointB);
		area.setName("splegg-orbit");
		area = delegate.addArea3D(area);
		return area;
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
