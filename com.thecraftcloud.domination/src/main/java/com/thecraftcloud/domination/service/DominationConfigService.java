package com.thecraftcloud.domination.service;

import java.util.concurrent.CopyOnWriteArraySet;

import org.bukkit.Bukkit;

import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.domination.DominationConfig;
import com.thecraftcloud.domination.GameController;
import com.thecraftcloud.minigame.service.ConfigService;

public class DominationConfigService {
	private DominationConfig ggConfig = DominationConfig.getInstance();
	private GameController controller;

	private ConfigService configService;
	// private TheCraftCloudDelegate delegate;
	private static DominationConfigService me;
	
	public static DominationConfigService getInstance() {
		if (me == null) {
			me = new DominationConfigService();
		}
		return me;
	}

	private DominationConfigService() {
		this.configService = ConfigService.getInstance();
		// this.delegate = TheCraftCloudDelegate.getInstance();
	}

	public void loadConfig() {
		
		//Deixa PVP ativo
		this.configService.getArenaWorld().setPVP(false);
		
		// Carregar configuracoes especificas do Gun Game
		Bukkit.getConsoleSender()
				.sendMessage(Utils.color("&6 [Domination] - DominationConfigService - configService: " + this.configService));

		// setar spawn points
		CopyOnWriteArraySet<GameArenaConfig> gacSpawnPoints = this.configService
				.getGameArenaConfigByGroup(DominationConfig.PLAYER_SPAWN);
		this.configService.getConfig().setSpawnPoints(gacSpawnPoints);
	}

}
