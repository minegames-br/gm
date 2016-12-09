package com.thecraftcloud.core.util;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.thecraftcloud.core.domain.Item;

public class MaterialUtil {

	private static MaterialUtil me;

	private MaterialUtil() {

	}

	public static MaterialUtil getInstance() {
		if (me == null) {
			me = new MaterialUtil();
		}
		return me;
	}

	public ItemStack toItemStack(String materialName) {
		ItemStack itemStack = null;

		Material material = Material.getMaterial(materialName);
		itemStack = new ItemStack(material, 1);

		return itemStack;
	}

	public ItemStack toItemStack(Item item) {
		ItemStack itemStack = null;

		String materialName = item.getName();
		itemStack = toItemStack(materialName);

		return itemStack;
	}

	public boolean isWeapon(ItemStack item) {

		if (isSword(item) || isAxe(item))
			return true;

		return false;
	}

	public boolean isTool(ItemStack item) {

		if (isPickAxe(item) || isSpade(item) || isHoe(item))
			return true;

		return false;
	}

	public boolean isArmor(ItemStack item) {

		if (isHelmet(item) || isChestplate(item) || isLeggings(item) || isBoots(item))
			return true;

		return false;
	}

	public boolean isHelmet(ItemStack item) {

		if (item.getType() == Material.DIAMOND_HELMET || item.getType() == Material.GOLD_HELMET
				|| item.getType() == Material.IRON_HELMET || item.getType() == Material.CHAINMAIL_HELMET
				|| item.getType() == Material.LEATHER_HELMET)
			return true;

		return false;
	}

	public boolean isLeggings(ItemStack item) {

		if (item.getType() == Material.DIAMOND_LEGGINGS || item.getType() == Material.GOLD_LEGGINGS
				|| item.getType() == Material.IRON_LEGGINGS || item.getType() == Material.CHAINMAIL_LEGGINGS
				|| item.getType() == Material.LEATHER_LEGGINGS)
			return true;

		return false;
	}

	public boolean isBoots(ItemStack item) {

		if (item.getType() == Material.DIAMOND_BOOTS || item.getType() == Material.GOLD_BOOTS
				|| item.getType() == Material.IRON_BOOTS || item.getType() == Material.CHAINMAIL_BOOTS
				|| item.getType() == Material.LEATHER_BOOTS)
			return true;

		return false;
	}

	public boolean isChestplate(ItemStack item) {

		if (item.getType() == Material.DIAMOND_CHESTPLATE || item.getType() == Material.GOLD_CHESTPLATE
				|| item.getType() == Material.IRON_CHESTPLATE || item.getType() == Material.CHAINMAIL_CHESTPLATE
				|| item.getType() == Material.LEATHER_CHESTPLATE)
			return true;

		return false;
	}

	public static boolean isSword(ItemStack item) {

		if (item.getType() == Material.DIAMOND_SWORD || item.getType() == Material.GOLD_SWORD
				|| item.getType() == Material.IRON_SWORD || item.getType() == Material.WOOD_SWORD)
			return true;

		return false;
	}

	public static boolean isAxe(ItemStack item) {

		if (item.getType() == Material.DIAMOND_AXE || item.getType() == Material.GOLD_AXE
				|| item.getType() == Material.IRON_AXE || item.getType() == Material.WOOD_AXE)
			return true;

		return false;
	}

	public static boolean isPickAxe(ItemStack item) {

		if (item.getType() == Material.DIAMOND_PICKAXE || item.getType() == Material.GOLD_PICKAXE
				|| item.getType() == Material.IRON_PICKAXE || item.getType() == Material.WOOD_PICKAXE)
			return true;

		return false;
	}

	public static boolean isSpade(ItemStack item) {

		if (item.getType() == Material.DIAMOND_SPADE || item.getType() == Material.GOLD_SPADE
				|| item.getType() == Material.IRON_SPADE || item.getType() == Material.WOOD_SPADE)
			return true;

		return false;
	}

	public static boolean isHoe(ItemStack item) {

		if (item.getType() == Material.DIAMOND_HOE || item.getType() == Material.GOLD_HOE
				|| item.getType() == Material.IRON_HOE || item.getType() == Material.WOOD_HOE)
			return true;

		return false;
	}

}
