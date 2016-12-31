package com.thecraftcloud.tnt_tag.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.thecraftcloud.minigame.domain.MyCloudCraftGame;
import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.tnt_tag.GameController;
import com.thecraftcloud.tnt_tag.domain.TNT;
import com.thecraftcloud.tnt_tag.service.TNTTagPlayerService;

public class EntityHitEvent implements Listener {

	private GameController controller;
	private TNTTagPlayerService tntPlayerService;
	private ConfigService configService = ConfigService.getInstance();
	private TNT tnt;

	public EntityHitEvent(GameController controller) {
		super();
		this.controller = controller;
		this.tntPlayerService = new TNTTagPlayerService(this.controller);
		this.tnt = new TNT();
	}

	@EventHandler
	public void onInteract(EntityDamageByEntityEvent event) {

		MyCloudCraftGame game = configService.getMyCloudCraftGame();
		if (!game.isStarted())
			return;
	
		if (!(event.getDamager() instanceof Player || event.getEntity() instanceof Player))
			return;
		
		Player tagger = (Player) event.getDamager();
		Player nextTagger = (Player) event.getEntity();
		
		tnt.changePlayerWithTnt(tagger, nextTagger);
	}

}
