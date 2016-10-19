package br.com.minegames.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.minegames.core.domain.Area3D;

public class GameConfig {

	protected HashMap<String, Object> variables = new HashMap<String,Object>();
	protected Area3D gameArena;
	protected List<Area3D> playerSpawnAreas = new ArrayList<Area3D>();

	public static final String START_COUNTDOWN = "START-COUNDOWN";
	public static final String MIN_PLAYERS = "MIN-PLAYERS";
	public static final String MAX_PLAYERS = "MAX-PLAYERS";

	
	public List<Area3D> getPlayerSpawnAreas() {
		return playerSpawnAreas;
	}

	public void setPlayerSpawnAreas(List<Area3D> playerSpawnAreas) {
		this.playerSpawnAreas = playerSpawnAreas;
	}

	public HashMap<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(HashMap<String, Object> variables) {
		this.variables = variables;
	}

	public Area3D getGameArena() {
		return gameArena;
	}

	public void setGameArena(Area3D gameArena) {
		this.gameArena = gameArena;
	}
	
	public void addVariable(String name, Object value) {
		if(variables == null) {
			variables = new HashMap<String, Object>();
		}
		variables.put(name, value);
	}
	
	public void addPlayerSpawnArea(Area3D area3d) {
		if(playerSpawnAreas == null) {
			playerSpawnAreas = new ArrayList<Area3D>();
		}
		playerSpawnAreas.add(area3d);
	}

	public int getCountDown() {
		Integer value = (Integer)variables.get(GameConfig.START_COUNTDOWN);
		if( value != null) {
			return value;
		} else {
			return 20;
		}
	}

	public void setCountDown(int value) {
		variables.put(GameConfig.START_COUNTDOWN, value);
	}

	public int getMaxPlayers() {
		Integer value = (Integer)variables.get(GameConfig.MAX_PLAYERS);
		if( value != null) {
			return value;
		} else {
			return 4;
		}
	}

	public void setMaxPlayers(int value) {
		variables.put(GameConfig.MAX_PLAYERS, value);
	}

	public int getMinPlayers() {
		Integer value = (Integer)variables.get(GameConfig.MIN_PLAYERS);
		if( value != null) {
			return value;
		} else {
			return 1;
		}
	}

	public void setMinPlayers(int value) {
		variables.put(GameConfig.MIN_PLAYERS, value);
	}
	
}
