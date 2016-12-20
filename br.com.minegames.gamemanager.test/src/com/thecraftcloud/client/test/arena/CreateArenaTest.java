package com.thecraftcloud.client.test.arena;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.FacingDirection;
import com.thecraftcloud.core.domain.Schematic;

public class CreateArenaTest {
	public static final String URL_SERVICES = "http://services.thecraftcloud.com:8080/gamemanager/webresources";
	//public static final String URL_SERVICES = "http://localhost:8080/gamemanager/webresources";
	//String restURL = "";
    private static TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
	
	@Test
	public void test() {
		
		Schematic schematic = new Schematic();
		schematic.setDescription("schematic para arena splegg orbit");
		schematic.setName("splegg-orbit");
		schematic.setPath("/opt/mg/worlds/splegg-orbit.schematic");
		
		delegate.createSchematic(schematic);
		
		Arena arena = new Arena();
		arena.setName("splegg-orbit");
		arena.setDescription("arena Orbit para o jogo splegg");
		arena.setFacing(FacingDirection.NORTH);
		arena.setSchematic(schematic);
		arena.setTime(1000);

		delegate.createArena(arena);
	}

}
