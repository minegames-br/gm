package com.thecraftcloud.gungame.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.gungame.service.GunGameConfigService;
import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.domain.GamePlayer;
import com.thecraftcloud.minigame.domain.MyCloudCraftGame;
import com.thecraftcloud.minigame.service.ConfigService;

public class SpawnBonusItemTask implements Runnable {

	protected TheCraftCloudMiniGameAbstract controller;
	protected GunGameConfigService configService = GunGameConfigService.getInstance();
	private Chest chest;
	private Inventory inventory;

	public SpawnBonusItemTask(TheCraftCloudMiniGameAbstract game) {
		this.controller = game;
	}

	@Override
	public void run() {

		MyCloudCraftGame game = this.controller.getConfigService().getMyCloudCraftGame();

		// descobrir se é para dar o bonus ou não
		getPrizeNumber();

		// pegar baú aleatório da arena
		getRandomChest();

	

	}

	private void getPrizeNumber() {
		Integer prizeList = this.configService.getPrizeList().size();
		Integer randomItemNumber = new Random().nextInt(prizeList);
		Integer value = configService.getPrizeList().get(randomItemNumber);
		if (value == 0) {
			return;
		}
		// sortear qual item colocar no baú
		giveRandomItem();
	}

	private void getRandomChest() {
		Integer numberOfChests = this.configService.getChestList().size();
		Integer randomChestNumber = new Random().nextInt(numberOfChests);
		chest = this.configService.getChestList().get(randomChestNumber);
		inventory = chest.getInventory();
	}

	private void giveRandomItem() {
		int randomItem = new Random().nextInt(this.configService.getItemList().size());
		inventory.addItem(this.configService.getItemList().get(randomItem));
		switch (randomItem) {
		case 0:
			Bukkit.broadcastMessage("Maçã dourada na arena!");
			Bukkit.getConsoleSender().sendMessage(Utils.color("&4Spawning gold apple on: " + chest.getBlock().getX()
					+ " " + chest.getBlock().getY() + " " + chest.getBlock().getZ()));
			break;
		case 1:
			Bukkit.broadcastMessage("Espada de diamante na arena!");
			Bukkit.getConsoleSender().sendMessage(Utils.color("&4Spawning diamond sword on: " + chest.getBlock().getX()
					+ " " + chest.getBlock().getY() + " " + chest.getBlock().getZ()));
			break;
		case 2:
			Bukkit.broadcastMessage("Armadura de diamante na arena!");
			Bukkit.getConsoleSender().sendMessage(Utils.color("&4Spawning diamond chestplate on: "
					+ chest.getBlock().getX() + " " + chest.getBlock().getY() + " " + chest.getBlock().getZ()));
			break;
		case 3:
			Bukkit.broadcastMessage("Poção de força na arena!");
			Bukkit.getConsoleSender().sendMessage(Utils.color("&4Spawning force potion on: " + chest.getBlock().getX()
					+ " " + chest.getBlock().getY() + " " + chest.getBlock().getZ()));
			break;
		case 4:
			Bukkit.broadcastMessage("Poção de velocidade na arena!");
			Bukkit.getConsoleSender().sendMessage(Utils.color("&4Spawning speed potion on: " + chest.getBlock().getX()
					+ " " + chest.getBlock().getY() + " " + chest.getBlock().getZ()));
			break;

		}

	}
}
