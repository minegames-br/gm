package com.thecraftcloud.client.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.GameConfig;
import com.thecraftcloud.core.domain.GameConfigScope;
import com.thecraftcloud.core.domain.GameConfigType;
import com.thecraftcloud.core.domain.Item;

public class CreateGameConfigTest  extends TheCraftCloudJUnitTest {
	
	
	@Test
	public void test() {

		for(int i = 1; i < 50; i++) {
			GameConfig gameConfig = createConfig("player" + i +".spawn", "Select the location " + i + " to spawn a player", 
				"Location where the player will spawn", "PLAYER-SPAWN", GameConfigType.LOCAL, GameConfigScope.ARENA);
		}
		
	}

	public GameConfig createConfig(String name, String displayName, String description, String group, GameConfigType type, GameConfigScope scope) {
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

}
