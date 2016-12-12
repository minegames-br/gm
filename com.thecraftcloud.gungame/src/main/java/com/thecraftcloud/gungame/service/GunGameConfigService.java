package com.thecraftcloud.gungame.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.domain.GameConfigInstance;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.gungame.GameController;
import com.thecraftcloud.gungame.GunGameConfig;
import com.thecraftcloud.gungame.domain.GunGamePlayer;
import com.thecraftcloud.minigame.domain.GamePlayer;
import com.thecraftcloud.minigame.service.ConfigService;

public class GunGameConfigService {
	private GunGameConfig ggConfig = GunGameConfig.getInstance();
	private GameController controller;
	private List<Chest> chestList;
	private ArrayList<Integer> prizeList;
	private ArrayList<ItemStack> itemList;
	private ConfigService configService;
	// private TheCraftCloudDelegate delegate;
	private static GunGameConfigService me;
	
	public static GunGameConfigService getInstance() {
		if (me == null) {
			me = new GunGameConfigService();
		}
		return me;
	}

	private GunGameConfigService() {
		this.configService = ConfigService.getInstance();
		// this.delegate = TheCraftCloudDelegate.getInstance();
	}

	public void loadConfig() {
		
		//Deixa PVP ativo
		this.configService.getArenaWorld().setPVP(false);
		
		// Carregar configuracoes especificas do Gun Game
		Bukkit.getConsoleSender()
				.sendMessage(Utils.color("&6 [GunGame] - GunGameConfigService - configService: " + this.configService));
		Integer killPoints = (Integer) this.configService.getGameConfigInstance(GunGameConfig.KILL_POINTS);

		Bukkit.getConsoleSender().sendMessage(
				Utils.color("&6 [GunGame] - GunGameConfigService - loadConfig KILL-POINTS: " + killPoints));
		ggConfig.setKillPoints(killPoints);

		// Materiais para dar ao evoluir
		List<GameConfigInstance> gciList = this.configService
				.getGameConfigInstanceGroup(GunGameConfig.GUNGAME_LEVEL_GROUP);
		ggConfig.setGunGameLevelList(gciList);

		// setar spawn points
		CopyOnWriteArraySet<GameArenaConfig> gacSpawnPoints = this.configService
				.getGameArenaConfigByGroup(GunGameConfig.PLAYER_SPAWN);
		this.configService.getConfig().setSpawnPoints(gacSpawnPoints);
	}

	public void createItemList() {
		itemList = new ArrayList();
		itemList.add(new ItemStack(Material.GOLDEN_APPLE, 1));
		itemList.add(new ItemStack(Material.DIAMOND_SWORD, 1));
		itemList.add(new ItemStack(Material.DIAMOND_CHESTPLATE, 1));

		// cria poção de força
		ItemStack forcePotion = new ItemStack(Material.POTION);
		PotionMeta forceMeta = (PotionMeta) forcePotion.getItemMeta();
		forceMeta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 400, 1), true);
		forcePotion.setItemMeta(forceMeta);
		itemList.add(forcePotion);

		// cria poção de velocidade
		ItemStack speedPotion = new ItemStack(Material.POTION);
		PotionMeta speedMeta = (PotionMeta) speedPotion.getItemMeta();
		speedMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 400, 1), true);
		speedPotion.setItemMeta(speedMeta);
		itemList.add(speedPotion);

	}

	public ArrayList<Integer> createPrizeList(CopyOnWriteArraySet<GamePlayer> numberOfPlayers) {
		setPrizeList(new ArrayList<Integer>());
		for (int i = 0; i < configService.getMaxPlayers(); i++) {
			getPrizeList().add(0);
		}

		for (GamePlayer gp : numberOfPlayers) {
			int i = new Random().nextInt(configService.getMaxPlayers());
			getPrizeList().remove(i);
			getPrizeList().add(i, 1);
		}
		return getPrizeList();
	}
	
	public void clearAllChests() {
		for (Chest chest : getChestList()) {
			chest.getInventory().clear();
		}
	}

	public void setChestList(List<Chest> chestList) {
		this.chestList = chestList;
	}

	public List<Chest> getChestList() {
		return this.chestList;
	}

	public GameConfigInstance getGunGameLevel(Integer level) {
		String gunGameLevel = GunGameConfig.GUNGAME_LEVEL + level.toString();
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + " " + gunGameLevel);
		for (GameConfigInstance _gci : this.ggConfig.getGunGameLevelList()) {
			if (_gci.getGameConfig().getName().equalsIgnoreCase(gunGameLevel)) {
				return _gci;
			}
		}
		return null;
	}

	public GameConfigInstance getNextGunGameLevel(GunGamePlayer ggPlayer) {
		GameConfigInstance gci = null;
		Integer level = ggPlayer.getLevel();
		Integer nextLevel = level + 1;
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
		Integer previousLevel = level - 1;
		if (previousLevel < 1)
			previousLevel = 1;
		gci = getGunGameLevel(previousLevel);
		return gci;
	}

	public Integer getKillPoints() {
		return this.ggConfig.getKillPoints();
	}

	public ArrayList<Integer> getPrizeList() {
		return prizeList;
	}

	public void setPrizeList(ArrayList<Integer> prizeList) {
		this.prizeList = prizeList;
	}

	public ArrayList<ItemStack> getItemList() {
		return itemList;
	}

	public void setItemList(ArrayList<ItemStack> itemList) {
		this.itemList = itemList;
	}

}
