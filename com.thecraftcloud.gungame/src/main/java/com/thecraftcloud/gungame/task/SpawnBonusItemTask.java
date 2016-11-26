package com.thecraftcloud.gungame.task;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.domain.MyCloudCraftGame;
import com.thecraftcloud.gungame.service.GunGameConfigService;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.plugin.service.ConfigService;

public class SpawnBonusItemTask implements Runnable {
	
	protected TheCraftCloudMiniGameAbstract controller;
	protected GunGameConfigService configService = GunGameConfigService.getInstance(); 
	
	public SpawnBonusItemTask(TheCraftCloudMiniGameAbstract game) {
		this.controller = game;
	}
	
    @Override
    public void run() {
    	
    	MyCloudCraftGame game = controller.getMyCloudCraftGame();
    	Integer numberOfChests = this.configService.getChestList().size();
    	Integer randomChestNumber = new Random().nextInt(numberOfChests);
    	
    	Chest chest = this.configService.getChestList().get(randomChestNumber);
    	Inventory inventory = chest.getInventory();
    	inventory.addItem( new ItemStack(Material.GOLDEN_APPLE, 1) );
    	Bukkit.getConsoleSender().sendMessage(Utils.color("&4Spawning gold apple on: " + chest.getBlock().getX() + " " + chest.getBlock().getY() + " " + chest.getBlock().getZ()));
    	
    	Bukkit.broadcastMessage("Pessoal! Maçã dourada na arena!");
    }

}
