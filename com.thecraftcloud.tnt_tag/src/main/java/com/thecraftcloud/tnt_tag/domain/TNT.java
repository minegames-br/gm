package com.thecraftcloud.tnt_tag.domain;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.thecraftcloud.tnt_tag.GameController;

public class TNT {
	
	private static Player playerWithTnt;
	public static boolean hasTntInGame;
	
	public TNT() {
	}
	
	public void setTntHolder(Player player) {
		player.getInventory().setItemInMainHand(new ItemStack(Material.TNT));
		setPlayerWithTnt(player);
		setHasTntInGame(true);
	}
	
	public Player getTntHolder() {
		return getPlayerWithTnt();
	}
	

	public boolean getHasTntInGame() {
		return hasTntInGame;
	}

	public void setHasTntInGame(boolean hasTntInGame) {
		TNT.hasTntInGame = hasTntInGame;
	}

	public Player getPlayerWithTnt() {
		return playerWithTnt;
	}

	public void setPlayerWithTnt(Player playerWithTnt) {
		TNT.playerWithTnt = playerWithTnt;
	}
	
	
}
