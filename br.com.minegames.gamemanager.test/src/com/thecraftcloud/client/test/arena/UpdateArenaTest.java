package com.thecraftcloud.client.test.arena;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.FacingDirection;

public class UpdateArenaTest {
	public static final String URL_SERVICES = "http://services.thecraftcloud.com:8080/gamemanager/webresources";
	//public static final String URL_SERVICES = "http://localhost:8080/gamemanager/webresources";
	//String restURL = "";
    private static TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
	
	@Test
	public void test() {
		Arena arena = delegate.findArenaByName("thearcher-stadium");
		arena.setFacing(FacingDirection.NORTH);

		delegate.updateArena(arena);
	}

}
