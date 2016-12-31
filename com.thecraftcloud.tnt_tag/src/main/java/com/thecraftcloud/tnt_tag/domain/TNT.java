package com.thecraftcloud.tnt_tag.domain;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.thecraftcloud.tnt_tag.GameController;
import com.thecraftcloud.tnt_tag.service.TNTTagPlayerService;

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
	

	public boolean hasTntInGame() {
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
	
	public void changePlayerWithTnt(Player tagger, Player nextTagger) {
		tagger.getInventory().clear();
		setTntHolder(nextTagger);
	}
	
	
}
