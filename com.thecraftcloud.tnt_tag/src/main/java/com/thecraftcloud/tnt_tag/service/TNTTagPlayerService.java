package com.thecraftcloud.tnt_tag.service;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.domain.GamePlayer;
import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.minigame.service.PlayerService;
import com.thecraftcloud.tnt_tag.GameController;
import com.thecraftcloud.tnt_tag.TNTTagConfig;

public class TNTTagPlayerService extends PlayerService {

	private GameController controller;
	private TNTTagConfigService tntTagConfigService = TNTTagConfigService.getInstance();
	private TNTTagConfig tntTagConfig = TNTTagConfig.getInstance();
	private TNTService tntService;
	

	public TNTTagPlayerService(TheCraftCloudMiniGameAbstract controller) {
		super(controller);
		this.configService = ConfigService.getInstance();
		this.tntTagConfigService = TNTTagConfigService.getInstance();
		this.tntService = new TNTService(this.controller);
	}

	public void teleportPlayersToArena() {
		int count = 0;
		this.configService.getArenaWorld().setPVP(true);
		CopyOnWriteArraySet<GameArenaConfig> gacList = ConfigService.getInstance().getSpawnPoints();
		Iterator<GameArenaConfig> it = gacList.iterator();
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
	}

	@Override
	public void killPlayer(Player dead) {	
		String deadname = dead.getDisplayName();
		Bukkit.broadcastMessage(ChatColor.GOLD + " " + deadname + "" + ChatColor.GREEN + " died.");
		
		dead.getWorld().createExplosion(dead.getLocation(), 2.0F);
		dead.setHealth(20);
		dead.getInventory().clear();
		dead.setGameMode(GameMode.SPECTATOR);

		if (!this.configService.getMyCloudCraftGame().isStarted()) {
			this.miniGame.removeLivePlayer(dead);
			dead.teleport(locationUtil.toLocation(this.configService.getWorld(), this.configService.getLobby()));
		}

		spawnDeadPlayer(dead);
		this.miniGame.removeLivePlayer(dead);
	}

	@Override
	public void createScoreBoard(Player player) {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective objective1 = scoreboard.registerNewObjective(ChatColor.BOLD + Utils.color("&CTNT TAG"), "tnt_tag");
		objective1.setDisplaySlot(DisplaySlot.SIDEBAR);
		player.setScoreboard(scoreboard);
	}
	
	public void clearPlayerInventory(Player player) {
		PlayerInventory inventory = player.getInventory();
		inventory.clear();
		inventory.setArmorContents(null);
	}

	@Override
	public void updateScoreBoards() {
		for (GamePlayer gp : this.miniGame.getLivePlayers()) {
			Player player = gp.getPlayer();
			Scoreboard scoreboard = player.getScoreboard();
			if (scoreboard == null || scoreboard.getObjective(DisplaySlot.SIDEBAR) == null)
				continue;

			Objective objective1 = scoreboard.getObjective(DisplaySlot.SIDEBAR);
			objective1.unregister();
			objective1 = scoreboard.registerNewObjective(ChatColor.BOLD + Utils.color( "&CTNT TAG"), "tnt_tag");
			objective1.setDisplaySlot(DisplaySlot.SIDEBAR);
			
			Integer time = (tntTagConfigService.getTntTimerInSeconds() - tntService.getTntDuration());

			Score p1 = objective1.getScore(ChatColor.BOLD + Utils.color("&FTempo:" + "   " + time));
			p1.setScore(3);

			Score space1 = objective1.getScore("");
			space1.setScore(2);

			Score p3 = objective1.getScore(ChatColor.BOLD + Utils.color("&FJogadores:" + "   " + this.miniGame.getLivePlayers().size()));
			p3.setScore(1);
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
