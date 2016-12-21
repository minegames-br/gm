package com.thecraftcloud.splegg;

import com.thecraftcloud.minigame.TheCraftCloudConfig;

public class SpleggConfig extends TheCraftCloudConfig {

	private static SpleggConfig me;

	public static SpleggConfig getInstance() {
		if (me == null) {
			me = new SpleggConfig();
		}
		return me;
	}

}
