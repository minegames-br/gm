package com.thecraftcloud.tnt_tag;

import com.thecraftcloud.minigame.TheCraftCloudConfig;

public class TntTagConfig extends TheCraftCloudConfig {

	private static TntTagConfig me;

	public static TntTagConfig getInstance() {
		if (me == null) {
			me = new TntTagConfig();
		}
		return me;
	}

}
