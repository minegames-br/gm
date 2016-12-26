package com.thecraftcloud.client.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.Item;
import com.thecraftcloud.core.domain.Kit;

public class AddKitTest  extends TheCraftCloudJUnitTest {
	
	
	@Test
	public void test() {

		Kit kit = new Kit();
		for(int i = 2; i < 50; )
		kit.setName("GUNGAME.NIVEL2");
		delegate.addKit(kit);
		
	}


}
