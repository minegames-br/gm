package com.thecraftcloud.client.test.arena;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.client.test.TheCraftCloudJUnitTest;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.FacingDirection;
import com.thecraftcloud.core.domain.Schematic;

public class CreateArenaTest  extends TheCraftCloudJUnitTest {
	
	
	@Test
	public void test() {
		
		Schematic schematic = new Schematic();
		schematic.setDescription("schematic para arena thearcher original");
		schematic.setName("thearcher-original");
		schematic.setPath("/opt/mg/worlds/thearcher-original.schematic");
		
		delegate.createSchematic(schematic);
		
		Arena arena = new Arena();
		arena.setName("thearcher-original");
		arena.setDescription("arena Original para o jogo TheArcher");
		arena.setFacing(FacingDirection.NORTH);
		arena.setSchematic(schematic);
		arena.setTime(1000);

		delegate.createArena(arena);
				
	}

//	{"schematic_uuid":"8152d874-0fb0-4ffb-ab0d-df62291cc9e3","name":"splegg-taurus","path":"/opt/mg/schematics/","description":"schematic para arena splegg taurus"}
//	{"arena_uuid":"8d38f59a-f997-4bec-9eb5-70d02fcf6cf4","name":"splegg-taurus","description":"arena Taurus para o jogo splegg","time":1000,"schematic":{"schematic_uuid":"8e6687b9-806a-4422-b12f-52a3c51aa58a","name":"splegg-taurus","path":"/opt/mg/schematics/","description":"schematic para arena splegg taurus"},"area":null,"facing":"NORTH","spawn":null}

}
