package com.thecraftcloud.core.admin.domain;

import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameWorld;
import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.domain.TransferObject;

public class ActionDTO extends TransferObject {

	public static final String PING_SERVER = "ping-server";
	public static final String GRANT_OPERATOR_ACTION = "grant-operator";
	public static final String EXECUTE_COMMAND = "execute-command";
	public static final String GET_GAME = "get-game";
	public static final String DOWNLOAD_WORLD = "download-world";
	public static final String PREPARE_GAME = "prepare-game";
	public static final String DEPLOY_GAME = "deploy-game";
	public static final String DEPLOY_ARENA = "deploy-arena";
	public static final String JOIN_GAME = "join-game";
	
	private String name;
	private MineCraftPlayer player;
	private GameWorld gameWorld;
	private Game game;
	private Arena arena;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MineCraftPlayer getPlayer() {
		return player;
	}

	public void setPlayer(MineCraftPlayer player) {
		this.player = player;
	}

	public GameWorld getGameWorld() {
		return gameWorld;
	}

	public void setGameWorld(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	public Game getGame() {
		return this.game;
	}

	public Arena getArena() {
		return arena;
	}

	public void setArena(Arena arena) {
		this.arena = arena;
	}

	public void setGame(Game game) {
		this.game = game;
	}
	
}