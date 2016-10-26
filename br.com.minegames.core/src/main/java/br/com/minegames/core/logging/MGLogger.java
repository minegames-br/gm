package br.com.minegames.core.logging;

import java.util.logging.LogManager;

public class MGLogger {

	public static void debug(String msg) {
		System.out.println(msg);
		//LogManager.getLogManager().getLogger("MG").fine(msg);
		//Bukkit.getLogger().log(Level.FINE, msg);
	}

	public static void info(String msg) {
		//Bukkit.getLogger().log(Level.INFO, msg);
		System.out.println(msg);
		//LogManager.getLogManager().getLogger("MG").info(msg);
	}
	
	public static void trace(String msg) {
		System.out.println(msg);
		//Bukkit.getLogger().log(Level.FINER, msg);
		//LogManager.getLogManager().getLogger("MG").finer(msg);
	}
	
	public static void finest(String msg) {
		System.out.println(msg);
		//Bukkit.getLogger().log(Level.FINEST, msg);
		//LogManager.getLogManager().getLogger("MG").finest(msg);
	}
	
	
}
