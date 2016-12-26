package com.thecraftcloud.client.test.arena;

import java.util.List;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.client.test.TheCraftCloudJUnitTest;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;

public class FindArenasByGameTest  extends TheCraftCloudJUnitTest {
	
	
	@Test
	public void test() {
		Game game = delegate.findGameByName( "gungame" );
		

		List<Arena> arenas = delegate.findArenasByGame(game);
		System.out.println(arenas);
	}

}
