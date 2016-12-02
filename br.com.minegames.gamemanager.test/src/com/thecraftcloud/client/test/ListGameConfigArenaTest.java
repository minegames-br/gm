package com.thecraftcloud.client.test;

import java.util.List;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.FacingDirection;
import com.thecraftcloud.core.domain.GameArenaConfig;

public class ListGameConfigArenaTest {

	
	public static final String URL_SERVICES = "http://services.thecraftcloud.com:8080/gamemanager/webresources";
	//public static final String URL_SERVICES = "http://localhost:8080/gamemanager/webresources";
	//String restURL = "";
    private static TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
	
	@Test
	public void test() {
		String arena_uuid = "1cb3dd98-b534-44f3-9a9c-1f8fe4183645";
		String game_uuid = "00000000-0000-0000-0000-000000000000";
		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
		List<GameArenaConfig> list = delegate.findAllGameConfigArenaByGameArena(game_uuid, arena_uuid);
		for(GameArenaConfig gac: list) {
			System.out.println(gac.getGameConfig().getName() + " " + gac.getIntValue() + " " + gac.getAreaValue() + " " + gac.getLocalValue() );
		}
		
		Arena arena = delegate.findArena(arena_uuid);
		arena.setFacing(FacingDirection.EAST);
			
		delegate.updateArena(arena);
	}

}
