package com.thecraftcloud.gungame.service;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.domain.GameConfigInstance;
import com.thecraftcloud.core.domain.Item;
import com.thecraftcloud.core.util.MaterialUtil;
import com.thecraftcloud.core.util.PlayerUtil;
import com.thecraftcloud.gungame.domain.GunGamePlayer;
import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.domain.GamePlayer;
import com.thecraftcloud.minigame.service.ConfigService;
import com.thecraftcloud.minigame.service.PlayerService;

public class GunGamePlayerService extends PlayerService {

	private GunGameConfigService gunGameConfigService = GunGameConfigService.getInstance();
	private PlayerUtil playerUtil = PlayerUtil.getInstance();
	private MaterialUtil materialUtil = MaterialUtil.getInstance();

	public GunGamePlayerService(TheCraftCloudMiniGameAbstract controller) {
		super(controller);
		this.configService = ConfigService.getInstance();
	}

	@Override
	public void killPlayer(Player dead) {
		String deadname = dead.getDisplayName();

		if (dead.getKiller() != null && dead.getKiller() instanceof Player) {
			String killerName = dead.getKiller().getName();
			Bukkit.broadcastMessage(ChatColor.GOLD + " " + deadname + " foi morto por " + ChatColor.GREEN + killerName);

			// dar bonus a quem matou
			Bukkit.getConsoleSender().sendMessage(
					ChatColor.GOLD + " " + dead.getKiller().getName() + " matou " + ChatColor.GREEN + dead.getName());
			giveBonus(dead.getKiller());
		}

		// remover bonus de quem morreu
		removeBonus(dead);
	}

	@Override
	public void giveBonus(Player shooter) {
		GamePlayer gp = this.findGamePlayerByPlayer(shooter);
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + " " + gp.getName() + " gameplayer giveBonus");
		Bukkit.getConsoleSender()
				.sendMessage(ChatColor.GOLD + " " + "kill points: " + gunGameConfigService.getKillPoints());

		this.givePoints(gp, gunGameConfigService.getKillPoints());

		GunGamePlayer ggPlayer = (GunGamePlayer) gp;

		// dar o proximo item por passar de nivel
		GameConfigInstance gci = this.gunGameConfigService.getNextGunGameLevel(ggPlayer);
		ggPlayer.setLevel(ggPlayer.getLevel() + 1);
		Bukkit.getConsoleSender()
				.sendMessage(ChatColor.GOLD + "Next level (" + ggPlayer.getLevel() + ")item: " + gci.getItem());

		ItemStack itemStack = MaterialUtil.getInstance().toItemStack(gci.getItem());
		playerUtil.setItemInPlayer(shooter, itemStack);
		shooter.sendMessage(ChatColor.GOLD  + "Você subiu para o Nível " + ggPlayer.getLevel());
	}

	public void removeBonus(Player dead) {
		GamePlayer gp = this.findGamePlayerByPlayer(dead);
		GunGamePlayer ggPlayer = (GunGamePlayer) gp;
		
		Integer level = ggPlayer.getLevel();

		if (level.equals(1)) {
			return;
		}
		
		// remover o item ganho no level atual
		GameConfigInstance gciA = this.gunGameConfigService.getGunGameLevel(ggPlayer.getLevel());
		ItemStack itemStackA = MaterialUtil.getInstance().toItemStack(gciA.getItem());
		playerUtil.removeItem(dead, itemStackA);
		
		ggPlayer.decreaseLevel();
		dead.sendMessage(ChatColor.RED  + "Você caiu para o Nível " + ggPlayer.getLevel());

		// dar o item do nivel anterior
		GameConfigInstance gciB = this.gunGameConfigService.getGunGameLevel(ggPlayer.getLevel());
		ItemStack itemStackB = MaterialUtil.getInstance().toItemStack(gciB.getItem());
		playerUtil.setItemInPlayer(dead, itemStackB);

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
		GameConfigInstance gci = gunGameConfigService.getGunGameLevel(1);
		Item item = gci.getItem();
		ItemStack itemStack = MaterialUtil.getInstance().toItemStack(item);
		playerUtil.setItemInPlayer(player, itemStack);
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
