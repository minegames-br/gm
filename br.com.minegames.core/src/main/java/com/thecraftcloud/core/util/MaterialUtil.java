package com.thecraftcloud.core.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.thecraftcloud.core.domain.Item;

public class MaterialUtil {

	private static MaterialUtil me;
	
	private MaterialUtil(){
		
	}
	
	public static MaterialUtil getInstance() {
		if(me == null) {
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
	
}
