package com.thecraftcloud.client.test.arena;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.client.test.TheCraftCloudJUnitTest;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.FacingDirection;

public class UpdateArenaTest  extends TheCraftCloudJUnitTest {
	
	
	@Test
	public void test() {
		Arena arena = delegate.findArenaByName("thearcher-stadium");
		arena.setFacing(FacingDirection.NORTH);

		delegate.updateArena(arena);
	}

}
