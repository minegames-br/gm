package com.thecraftcloud.test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.client.exception.InvalidRegistrationException;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;

public class EntityManagerTest {

	public static final String URL_SERVICES = "http://services.thecraftcloud.com:8080/gamemanager/webresources";
	//public static final String URL_SERVICES = "http://localhost:8080/gamemanager/webresources";
	//String restURL = "";
    private static TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
	
	public static void main(String[] args) throws InvalidRegistrationException {

		for(int i =0; i < 1000; i++) {
			delegate.findAllArenas();
		}
		
		
	}

}
