package com.thecraftcloud.splegg.service;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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

public class SpleggPlayerService extends PlayerService {

	protected SpleggConfigService spleggService = SpleggConfigService.getInstance();
	protected SpleggPlayerService playerService;

	public SpleggPlayerService(TheCraftCloudMiniGameAbstract controller) {
		super(controller);
		this.configService = ConfigService.getInstance();
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
		player.setGameMode(GameMode.SURVIVAL);
	}

	@Override
	public void killPlayer(Player dead) {
		String deadname = dead.getDisplayName();
		Bukkit.broadcastMessage(ChatColor.GOLD + " " + deadname + "" + ChatColor.GREEN + " died.");

		dead.setHealth(20);
		dead.getInventory().clear();
		dead.setGameMode(GameMode.SPECTATOR);
		dead.sendMessage(ChatColor.YELLOW.ITALIC + dead.getName() + " , você agora é um espectador!");

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
		Objective objective1 = scoreboard.registerNewObjective(Utils.color("&6Splegg"), "splegg");
		objective1.setDisplaySlot(DisplaySlot.SIDEBAR);
		player.setScoreboard(scoreboard);
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
			objective1 = scoreboard.registerNewObjective(ChatColor.BOLD.UNDERLINE + "❐ SPLEGG ❐", "splegg");
			objective1.setDisplaySlot(DisplaySlot.SIDEBAR);

			Integer time = (configService.getGameDurationInSeconds() - this.miniGame.getGameDuration());

			
			Score space0 = objective1.getScore("");
			space0.setScore(7);
			
			Score p1 = objective1.getScore(ChatColor.BOLD + Utils.color("&BTempo"));
			p1.setScore(6);
			
			Score p2 = objective1.getScore("" + time);
			p2.setScore(5);
			
			Score space1 = objective1.getScore("");
			space1.setScore(4);
			
			Score space2 = objective1.getScore("");
			space2.setScore(3);

			Score p3 = objective1.getScore(ChatColor.BOLD + Utils.color("&AJogadores"));
			p3.setScore(2);
			
			Score p4 = objective1.getScore("" + this.miniGame.getLivePlayers().size());
			p4.setScore(1);
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
