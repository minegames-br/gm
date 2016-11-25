package com.thecraftcloud.gungame.service;

import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.client.exception.InvalidRegistrationException;
import com.thecraftcloud.core.domain.Item;
import com.thecraftcloud.gungame.GunGameConfig;
import com.thecraftcloud.plugin.TheCraftCloudConfig;
import com.thecraftcloud.plugin.service.ConfigService;

public class GunGameConfigService extends ConfigService {
	
	protected GunGameConfigService() {
		super();
		this.delegate = TheCraftCloudDelegate.getInstance();
		this.config = (TheCraftCloudConfig)createConfigDomain();
	}
		
	
	protected GunGameConfig createConfigDomain() {
		return new GunGameConfig();
	}
	
	@Override
	public void loadTheCraftCloudData(JavaPlugin plugin, boolean force) throws InvalidRegistrationException {
		super.loadTheCraftCloudData(plugin, force);
		
		//Carregar configuracoes especificas do Gun Game
		GunGameConfig ggConfig = (GunGameConfig)this.config;
		Integer killPoints = (Integer)this.getGameConfigInstance( GunGameConfig.KILL_POINTS );
		ggConfig.setKillPoints( killPoints );
		
		//Locais de spawn
		
		//Materiais para dar ao evoluir
		
	}

}
