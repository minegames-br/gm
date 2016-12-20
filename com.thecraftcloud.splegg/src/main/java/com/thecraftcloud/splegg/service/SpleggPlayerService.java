package com.thecraftcloud.splegg.service;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.domain.GameConfigInstance;
import com.thecraftcloud.core.domain.Item;
import com.thecraftcloud.core.util.MaterialUtil;
import com.thecraftcloud.core.util.PlayerUtil;
import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.domain.GamePlayer;
import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.minigame.service.PlayerService;
import com.thecraftcloud.splegg.domain.SpleggPlayer;

public class SpleggPlayerService extends PlayerService {

	private SpleggConfigService spleggService = SpleggConfigService.getInstance();
	private PlayerUtil playerUtil = PlayerUtil.getInstance();

	public SpleggPlayerService(TheCraftCloudMiniGameAbstract controller) {
		super(controller);
		this.configService = ConfigService.getInstance();
	}

	@Override
	public void killPlayer(Player dead) {
		String deadname = dead.getDisplayName();
	}

	public void teleportPlayersToArena() {
		int count = 0;
		this.configService.getArenaWorld().setPVP(true);
		CopyOnWriteArraySet<GameArenaConfig> gacList = ConfigService.getInstance().getSpawnPoints();
		Iterator it = gacList.iterator();
		Bukkit.getConsoleSender().sendMessage("&8gacList.size: " + gacList.size());
		for (GamePlayer gp : this.getLivePlayers()) {
			GameArenaConfig gac = (GameArenaConfig) it.next();
			this.regainAttributesToPlayer(gp.getPlayer());
			Bukkit.getConsoleSender().sendMessage("&7world: " + this.configService);
			Bukkit.getConsoleSender().sendMessage("&7world: " + this.configService.getArenaWorld());
			Bukkit.getConsoleSender().sendMessage("&6gac: " + gac);
			Location spawn = locationUtil.toLocation(this.configService.getArenaWorld(), gac.getLocalValue());
			gp.getPlayer().teleport(spawn);
			count++;
		}
	}

	public void setupPlayersToStartGame() {
		for (GamePlayer gp : this.miniGame.getLivePlayers()) {
			Player player = gp.getPlayer();
			this.setupPlayerToStartGame(player);
		}
	}

	public void setupPlayerToStartGame(Player player) {
		super.setupPlayerToStartGame(player);
		
		/*
		GameConfigInstance gci = spleggConfigService.getGunGameLevel(1);
		Item item = gci.getItem();
		ItemStack itemStack = MaterialUtil.getInstance().toItemStack(item);
		playerUtil.setItemInPlayer(player, itemStack);
		playMusic(player);
		*/
	}
	
	public void playMusic(Player player) {
		player.playSound(player.getLocation(), Sound.MUSIC_END, 10, 1);
	}
		

	public void spawnDeadPlayer(Player player) {
		CopyOnWriteArraySet<GameArenaConfig> gacList = ConfigService.getInstance().getSpawnPoints();

		int random = gacList.size() - 1;
		random = new Random().nextInt(random);
		GameArenaConfig gac = (GameArenaConfig) gacList.toArray()[random];
		Location spawn = locationUtil.toLocation(this.configService.getWorld(), gac.getLocalValue());
		player.teleport(spawn);
	}

}
