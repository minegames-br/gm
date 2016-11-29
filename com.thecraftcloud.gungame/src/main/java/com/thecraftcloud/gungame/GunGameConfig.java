package com.thecraftcloud.gungame;

import java.util.List;

import org.bukkit.Bukkit;

import com.thecraftcloud.core.domain.GameConfigInstance;
import com.thecraftcloud.minigame.TheCraftCloudConfig;

public class GunGameConfig extends TheCraftCloudConfig {
	
	public static final String KILL_POINTS = "KILL-POINTS";

	public static final String GUNGAME_LEVEL_GROUP = "GUNGAME.LEVEL";
	
	public static final String GUNGAME_LEVEL = "GUNGAME.";

	private static GunGameConfig me;
	
	private Integer killPoints;
	private List<GameConfigInstance> gunGameLevelList;

	public static GunGameConfig getInstance() {
		if(me == null) {
			me = new GunGameConfig();
		}
		return me;
	}
	
	private GunGameConfig() {
		Bukkit.getConsoleSender().sendMessage("Criou TheCraftCloudConfig");
	}
	
	public Integer getKillPoints() {
		return killPoints;
	}

	public void setKillPoints(Integer killPoints) {
		this.killPoints = killPoints;
	}

	@Override
	public List<String> getMandatoryConfigList() {
		List<String> gcList = super.getMandatoryConfigList();
		gcList.add(KILL_POINTS);
		return gcList;
	}

	public List<GameConfigInstance> getGunGameLevelList() {
		return gunGameLevelList;
	}

	public void setGunGameLevelList(List<GameConfigInstance> gunGameLevelList) {
		this.gunGameLevelList = gunGameLevelList;
	}

}
