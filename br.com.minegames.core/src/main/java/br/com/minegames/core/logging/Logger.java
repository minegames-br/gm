package br.com.minegames.core.logging;

import java.util.logging.Level;

import org.bukkit.Bukkit;

public class Logger {

	public static void debug(String msg) {
		
		Bukkit.getLogger().log(Level.FINE, msg);
	}

	public static void info(String msg) {
		Bukkit.getLogger().log(Level.INFO, msg);
	}
	
	public static void trace(String msg) {
		Bukkit.getLogger().log(Level.FINER, msg);
	}
	
	public static void finest(String msg) {
		Bukkit.getLogger().log(Level.FINEST, msg);
	}
	
	
}
