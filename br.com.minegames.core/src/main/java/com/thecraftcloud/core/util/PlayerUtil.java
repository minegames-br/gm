package com.thecraftcloud.core.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_10_R1.Material;

public class PlayerUtil {

	private static PlayerUtil me;
	private MaterialUtil materialUtil = MaterialUtil.getInstance();

	private PlayerUtil() {

	}

	public static PlayerUtil getInstance() {
		if (me == null) {
			me = new PlayerUtil();
		}
		return me;
	}

	public void setItemInPlayer(Player player, ItemStack item) {
		if (this.materialUtil.isWeapon(item) || this.materialUtil.isTool(item)) {
			player.getInventory().setItemInMainHand(item);
		} else if (this.materialUtil.isArmor(item)) {
			if (this.materialUtil.isHelmet(item)) {
				player.getInventory().setHelmet(item);
			} else if (this.materialUtil.isChestplate(item)) {
				player.getInventory().setChestplate(item);
			} else if (this.materialUtil.isLeggings(item)) {
				player.getInventory().setLeggings(item);
			} else if (this.materialUtil.isBoots(item)) {
				player.getInventory().setBoots(item);
			}
		}

	}
	
	public void removeItem(Player player, ItemStack item) {
		if (this.materialUtil.isWeapon(item) || this.materialUtil.isTool(item)) {
			player.getInventory().remove(item);
		} else if (this.materialUtil.isArmor(item)) {
			if (this.materialUtil.isHelmet(item)) {
				player.getInventory().setHelmet(null);
			} else if (this.materialUtil.isChestplate(item)) {
				player.getInventory().setChestplate(null);
			} else if (this.materialUtil.isLeggings(item)) {
				player.getInventory().setLeggings(null);
			} else if (this.materialUtil.isBoots(item)) {
				player.getInventory().setBoots(null);
			}
		}
	}

}
