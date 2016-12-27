package com.thecraftcloud.test;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.client.exception.InvalidRegistrationException;
import com.thecraftcloud.client.test.TheCraftCloudJUnitTest;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;

public class EntityManagerTest  extends TheCraftCloudJUnitTest {
	
	@Test
	public void test() throws InvalidRegistrationException {

		for(int i =0; i < 1000; i++) {
			delegate.findAllArenas();
		}
		
		
	}

}
