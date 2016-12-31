package com.thecraftcloud.tnt_tag.domain;

import org.bukkit.entity.Player;

import com.thecraftcloud.minigame.domain.GamePlayer;
import com.thecraftcloud.tnt_tag.GameController;

public class TNTTagPlayer extends GamePlayer {

	private GameController controller;

	private float speedTagger = 0.3F;
	private float defaultSpeed = 0.3F;

	public TNTTagPlayer(GameController controller) {
		this.controller = controller;
	}

	public void setTaggerSpeed(Player player) {
		player.setWalkSpeed(speedTagger);
	}

	public void setDefaultSpeed(Player player) {
		player.setWalkSpeed(defaultSpeed);
	}

}
