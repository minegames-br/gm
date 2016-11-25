package com.thecraftcloud.gungame.service;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.thecraftcloud.domain.GamePlayer;
import com.thecraftcloud.gungame.GunGameConfig;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.plugin.service.PlayerService;

public class GunGamePlayerService extends PlayerService {
	
	private GunGameConfig ggConfig;
	
	public GunGamePlayerService(TheCraftCloudMiniGameAbstract controller) {
		super(controller);
		ggConfig = (GunGameConfig)this.configService.getConfig();
	}

	@Override
	public void killPlayer(Player dead) {
		String deadname = dead.getDisplayName();
		
		if( dead.getKiller() != null && dead.getKiller() instanceof Player) {
			String killerName = dead.getKiller().getName();
			Bukkit.broadcastMessage(ChatColor.GOLD + " " + deadname + " was killked by " + ChatColor.GREEN + killerName );
			
			giveBonus(dead.getKiller());
		}
	}
	
	@Override
	public void giveBonus(Player shooter) {
		super.giveBonus(shooter);
		GamePlayer gp = this.findGamePlayerByPlayer(shooter);
		this.givePoints(shooter, ggConfig.getKillPoints() );
		
	}

	


}
