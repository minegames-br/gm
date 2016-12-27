package com.thecraftcloud.client.test;

import java.util.List;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.Area3D;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.domain.Local;

public class SetupArenaAreaTest  extends TheCraftCloudJUnitTest {
	
	
	@Test
	public void test() {
		
		Arena arena = delegate.findArena("04cdb0ab-bbc2-41b9-8ccb-5cd555838f68");
		
		Area3D area = new Area3D();
		Local l = new Local(494, 4, 1163);
		Local l2 = new Local(456, 21, 1200);
		area.setPointA(l);
		area.setPointB(l2);
		
		area = delegate.addArea3D(area);
		
		arena.setArea(area);
		
		delegate.updateArena(arena);
	}

}
