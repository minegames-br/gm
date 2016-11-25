package com.thecraftcloud.gungame;

import java.util.List;

import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.domain.GameConfigInstance;
import com.thecraftcloud.plugin.TheCraftCloudConfig;

public class GunGameConfig extends TheCraftCloudConfig {
	
	public static final String KILL_POINTS = "KILL-POINTS";
	
	private Integer killPoints;

	public Integer getKillPoints() {
		return killPoints;
	}

	public void setKillPoints(Integer killPoints) {
		this.killPoints = killPoints;
	}

	

}
