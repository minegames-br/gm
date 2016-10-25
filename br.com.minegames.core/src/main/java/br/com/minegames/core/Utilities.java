package br.com.minegames.core;

import java.util.UUID;

public class Utilities {

	public static String generateUuid() {
		String uuid = null;
		
		uuid = UUID.randomUUID().toString();
		
		return uuid;
	}
	
}
