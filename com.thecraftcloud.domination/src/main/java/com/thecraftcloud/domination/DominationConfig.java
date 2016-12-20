package com.thecraftcloud.domination;

import org.bukkit.Bukkit;

import com.thecraftcloud.minigame.TheCraftCloudConfig;

public class DominationConfig extends TheCraftCloudConfig {
	
	private static DominationConfig me;
	
	public static DominationConfig getInstance() {
		if(me == null) {
			me = new DominationConfig();
		}
		return me;
	}
	
	private DominationConfig() {
		Bukkit.getConsoleSender().sendMessage("Criou TheCraftCloudConfig");
	}
	
}
