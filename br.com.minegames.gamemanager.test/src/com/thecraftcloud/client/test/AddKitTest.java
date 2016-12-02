package com.thecraftcloud.client.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.Item;
import com.thecraftcloud.core.domain.Kit;

public class AddKitTest {
	
	public static final String URL_SERVICES = "http://services.thecraftcloud.com:8080/gamemanager/webresources";
	//public static final String URL_SERVICES = "http://localhost:8080/gamemanager/webresources";
	//String restURL = "";
    private static TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
	
	@Test
	public void test() {

		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
		Kit kit = new Kit();
		for(int i = 2; i < 50; )
		kit.setName("GUNGAME.NIVEL2");
		delegate.addKit(kit);
		
	}


}
