package com.thecraftcloud.client.test.gameinstance;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.client.test.TheCraftCloudJUnitTest;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameArenaConfig;

public class GameArenaConfigListByGameArenaTest  extends TheCraftCloudJUnitTest {
	

	@Test
	public void test() {
		Game game = delegate.findGameByName("TheArcher");
		Arena arena = delegate.findArenasByGame(game).get(1);
		List<GameArenaConfig> gacList = delegate.findAllGameConfigArenaByGameArena( game.getGame_uuid().toString(), arena.getArena_uuid().toString() );
		for(GameArenaConfig gac: gacList) {
			System.out.println(gac.getArena().getName());
			System.out.println(gac.getGameConfig().getName());
		}
	}

}
