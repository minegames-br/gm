package com.thecraftcloud.tnt_tag;

import java.util.List;

import com.thecraftcloud.minigame.TheCraftCloudConfig;

public class TNTTagConfig extends TheCraftCloudConfig {

	public static final String TNT_EXPLODE_TIMER = "TNT-TAG-EXPLODE-TIMER-IN-SECONDS";
	
	private static TNTTagConfig me;

	public static TNTTagConfig getInstance() {
		if (me == null) {
			me = new TNTTagConfig();
		}
		return me;
	}
	
	@Override
	public List<String> getMandatoryConfigList() {
		List<String> gcList = super.getMandatoryConfigList();
		gcList.add(TNT_EXPLODE_TIMER);
		return gcList;
	}
	
}
