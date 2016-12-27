package com.thecraftcloud.client.test.player;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.client.test.TheCraftCloudJUnitTest;
import com.thecraftcloud.core.domain.MineCraftPlayer;

public class FindPlayerByNameTest  extends TheCraftCloudJUnitTest {
	

	@Test
	public void test() {

		MineCraftPlayer player = delegate.findPlayerByName("_KingCraft");
		System.out.println(player.getEmail() + " " + player.getName() );
	}

}
