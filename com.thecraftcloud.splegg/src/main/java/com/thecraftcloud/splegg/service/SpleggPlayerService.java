package com.thecraftcloud.splegg.service;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.util.PlayerUtil;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.domain.GamePlayer;
import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.minigame.service.PlayerService;
import com.thecraftcloud.splegg.GameController;

public class SpleggPlayerService extends PlayerService {

	private GameController controller;
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
	
	@Override
	public void setupPlayerToStartGame(Player player) {
		super.setupPlayerToStartGame(player);
		
		PlayerInventory inventory = player.getInventory();
		ItemStack spade = new ItemStack(Material.IRON_SPADE);
		inventory.setItemInMainHand(spade);
		
		createScoreBoard(player);
	}

	@Override
	public void createScoreBoard(Player player) {
		Bukkit.getConsoleSender().sendMessage(Utils.color("&6CRIANDO SCOREBOARD"));
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective objective1 = scoreboard.registerNewObjective(Utils.color("&6Splegg"), "splegg");
		objective1.setDisplaySlot(DisplaySlot.SIDEBAR);
		player.setScoreboard(scoreboard);
	}

	@Override
	public void updateScoreBoards() {
		for (GamePlayer gp : this.controller.getLivePlayers()) {
			Player player = gp.getPlayer();
			Scoreboard scoreboard = player.getScoreboard();
			if (scoreboard == null || scoreboard.getObjective(DisplaySlot.SIDEBAR) == null)
				continue;

			Objective objective1 = scoreboard.getObjective(DisplaySlot.SIDEBAR);
			objective1.unregister();
			objective1 = scoreboard.registerNewObjective(Utils.color("&6Splegg"), "splegg");
			objective1.setDisplaySlot(DisplaySlot.SIDEBAR);

			Integer time = (configService.getGameDurationInSeconds() - this.miniGame.getGameDuration());

			Score p1 = objective1.getScore("&5Tempo: " + time);
			p1.setScore(0);

			Score p2 = objective1.getScore("&3Jogadores: " + this.controller.getLivePlayers().size());
			p1.setScore(1);
		}

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
