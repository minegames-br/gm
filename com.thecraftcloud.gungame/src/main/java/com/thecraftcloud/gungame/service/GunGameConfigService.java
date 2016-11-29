package com.thecraftcloud.gungame.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Chest;

import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.domain.GameConfigInstance;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.gungame.GunGameConfig;
import com.thecraftcloud.gungame.domain.GunGamePlayer;
import com.thecraftcloud.minigame.service.ConfigService;

public class GunGameConfigService {
	private GunGameConfig ggConfig = GunGameConfig.getInstance();
	private List<Chest> chestList;
	private ConfigService configService;
	//private TheCraftCloudDelegate delegate;
	private static GunGameConfigService me;
	
	public static GunGameConfigService getInstance() {
		if(me == null) {
			me = new GunGameConfigService();
		}
		return me;
	}
	
	private GunGameConfigService() {
		this.configService = ConfigService.getInstance();
		//this.delegate = TheCraftCloudDelegate.getInstance();
	}
	
	public void loadConfig() {
		//Carregar configuracoes especificas do Gun Game
		Bukkit.getConsoleSender().sendMessage( Utils.color("&6 [GunGame] - GunGameConfigService - configService: " + this.configService) );
		Integer killPoints = (Integer)this.configService.getGameConfigInstance( GunGameConfig.KILL_POINTS );
		
		Bukkit.getConsoleSender().sendMessage( Utils.color("&6 [GunGame] - GunGameConfigService - loadConfig KILL-POINTS: " + killPoints) );
		ggConfig.setKillPoints( killPoints );
		
		//Materiais para dar ao evoluir
		List<GameConfigInstance> gciList = this.configService.getGameConfigInstanceGroup(GunGameConfig.GUNGAME_LEVEL_GROUP);
		ggConfig.setGunGameLevelList(gciList);
		
		//setar spawn points
		CopyOnWriteArraySet<GameArenaConfig> gacSpawnPoints = this.configService.getGameArenaConfigByGroup(GunGameConfig.PLAYER_SPAWN);
		this.configService.getConfig().setSpawnPoints(gacSpawnPoints);
		
	}

	public void setChestList(List<Chest> chestList) {
		this.chestList = chestList;
	}
	
	public List<Chest> getChestList() {
		return this.chestList;
	}


	public GameConfigInstance getGunGameLevel(Integer level) {
		String gunGameLevel = GunGameConfig.GUNGAME_LEVEL + level.toString();
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + " " + gunGameLevel );
		for(GameConfigInstance _gci: this.ggConfig.getGunGameLevelList()) {
			if(_gci.getGameConfig().getName().equalsIgnoreCase(gunGameLevel)) {
				return _gci;
			}
		}
		return null;
	}
	
	public GameConfigInstance getNextGunGameLevel(GunGamePlayer ggPlayer) {
		GameConfigInstance gci = null;
		Integer level = ggPlayer.getLevel();
		Integer nextLevel = level+1;
		gci = getGunGameLevel(nextLevel);
		return gci;
	}

	public GameConfigInstance setupGunGameFirstLevel(GunGamePlayer ggPlayer) {
		GameConfigInstance gci = null;
		Integer level = ggPlayer.getLevel();
		gci = getGunGameLevel(level);
		return gci;
	}

	public GameConfigInstance getPreviousGunGameLevel(GunGamePlayer ggPlayer) {
		GameConfigInstance gci = null;
		Integer level = ggPlayer.getLevel();
		Integer previousLevel = level-1;
		if(previousLevel < 1) previousLevel = 1;
		gci = getGunGameLevel(previousLevel);
		return gci;
	}

	public Integer getKillPoints() {
		return this.ggConfig.getKillPoints();
	}

}
