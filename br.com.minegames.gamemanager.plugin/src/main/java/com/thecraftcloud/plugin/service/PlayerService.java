package com.thecraftcloud.plugin.service;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArraySet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.thecraftcloud.core.logging.MGLogger;
import com.thecraftcloud.core.util.LocationUtil;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.domain.GamePlayer;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.plugin.TheCraftCloudPlugin;

public class PlayerService extends TheCraftCloudService {
	
	protected LocationUtil locationUtil = new LocationUtil();
	
	public PlayerService(TheCraftCloudMiniGameAbstract miniGame) {
		super(miniGame);
	}
	
	public void giveBonus(Player shooter) {

	}

	public void killPlayer(Player dead) {
		String deadname = dead.getDisplayName();
		Bukkit.broadcastMessage(ChatColor.GOLD + " " + deadname + "" + ChatColor.GREEN + " died.");

		dead.setHealth(20); // Do not show the respawn screen
		dead.getInventory().clear();

		if (this.miniGame.getMyCloudCraftGame().isStarted()) {
			this.miniGame.removeLivePlayer(dead);
			dead.teleport( locationUtil.toLocation(this.miniGame.getWorld(), miniGame.getLobby() ) ); //TELEPORT DEAD PLAYER TO LOBBY
		}
	}

	public void givePoints(Player player, Integer pointsEarned) {
		GamePlayer gamePlayer = (GamePlayer)this.findGamePlayerByPlayer(player);
		gamePlayer.addPoints( pointsEarned );
		updateScoreBoards();
	}

	public void teleportPlayersToPodium() {
		Object aList[] = this.miniGame.getLivePlayers().toArray();
		Arrays.sort(aList);
		MGLogger.trace("teleport players to podium - aList.lengh: " + aList.length + "");
	}

	/*
	 * Nesse método poderemos decidir o que dar a cada jogador
	 */
	public void setupPlayerToStartGame(Player player) {
		PlayerInventory inventory = player.getInventory();

		inventory.clear();
		inventory.setArmorContents(null);

		ItemStack bow = new ItemStack(Material.BOW);
		ItemStack arrow = new ItemStack(Material.ARROW);
		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);

		bow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 15);
		bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 15);

		inventory.addItem(bow);
		inventory.addItem(arrow);
		inventory.addItem(sword);

		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective objective = scoreboard.registerNewObjective(Utils.color("&6Placar"), "placar");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		player.setScoreboard(scoreboard);
	}

	public void updateScoreBoards() {
		for (GamePlayer gp : this.miniGame.getLivePlayers()) {
			Player player = gp.getPlayer();
			Scoreboard scoreboard = player.getScoreboard();
			for (GamePlayer gp1 : this.miniGame.getLivePlayers()) {
				String name = gp1.getPlayer().getName();
				scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(name).setScore(gp1.getPoint());
			}
		}
	}

	public BossBar createBossBar() {
		BossBar bar = Bukkit.createBossBar("Base", BarColor.PINK, BarStyle.SOLID);
		bar.setProgress(1F);
		return bar;
	}

	public GamePlayer findGamePlayerByPlayer(Player player) {
		CopyOnWriteArraySet<GamePlayer> playerList = this.miniGame.getLivePlayers();
		for (GamePlayer gp : playerList) {
			if (gp.getPlayer().equals(player)) {
				return gp;
			}
		}
		return null;
	}


}
