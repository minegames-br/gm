package com.thecraftcloud.tnt_tag.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.tnt_tag.GameController;
import com.thecraftcloud.tnt_tag.domain.TNT;

public class TNTExplosionListener implements Listener {

	private ConfigService configService = ConfigService.getInstance();
	private GameController controller;
	private TNT tnt;

	public TNTExplosionListener(GameController controller) {
		super();
		this.controller = controller;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onExplosion(BlockExplodeEvent event) {
		
		Bukkit.getConsoleSender().sendMessage(Utils.color("&3 BOMBA EXPLODIU!"));

		if (!event.getBlock().getType().equals(Material.TNT))
			return;

		tnt.setHasTntInGame(false);
	}
}
