package com.thecraftcloud.test;

import org.junit.Test;

import com.thecraftcloud.client.exception.InvalidRegistrationException;
import com.thecraftcloud.client.test.TheCraftCloudJUnitTest;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;

public class LoadGameConfigTest  extends TheCraftCloudJUnitTest {
	
	@Test
	public void test() throws InvalidRegistrationException {

		Game game = delegate.findGame("e2f4757e-d7bc-43fc-a4e7-4b07ad646f1e");
		Arena arena = delegate.findArena("4a84d366-9f64-4631-aecf-fc57fec48b6b");
		
		
		
	}

}
