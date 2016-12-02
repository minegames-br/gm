package com.thecraftcloud.admin.service;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.GameInstance;

public class GameInstanceService {
	private TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();

	public GameInstance createGameInstance(GameInstance gi) {
		return delegate.createGameInstance(gi);
	}

}
