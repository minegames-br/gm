package com.thecraftcloud.client.test;

import java.util.List;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.GameConfigInstance;

public class ListGameConfigInstanceTest  extends TheCraftCloudJUnitTest {
	
	
	@Test
	public void test() {
		List<GameConfigInstance> list = delegate.findAllGameConfigInstanceByGameUUID("c6905743-6514-49ba-9257-420743f65b65");
		for(GameConfigInstance gci: list) {
			System.out.println(gci.getGameConfig().getName() + " " + gci.getIntValue() + " " + gci.getArea() + " " + gci.getLocal() );
		}
	}

}
