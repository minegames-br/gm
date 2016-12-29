package com.thecraftcloud.tnt_tag;

import java.util.List;

import com.thecraftcloud.minigame.TheCraftCloudConfig;

public class TntTagConfig extends TheCraftCloudConfig {

	public static final String TNT_EXPLODE_TIMER = "TNT-TAG-EXPLODE-TIMER-IN-SECONDS";
	
	private static TntTagConfig me;

	public static TntTagConfig getInstance() {
		if (me == null) {
			me = new TntTagConfig();
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
