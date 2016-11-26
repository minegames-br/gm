package com.thecraftcloud.plugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.domain.GameConfigInstance;
import com.thecraftcloud.core.domain.Local;
import com.thecraftcloud.core.domain.ServerInstance;

public class TheCraftCloudConfig {

	//Configuracoes guardadas no banco de dados
	public static final String LOBBY_LOCATION = "LOBBY-LOCATION";
	public static final String MIN_PLAYERS = "MIN-PLAYERS";
	public static final String MAX_PLAYERS = "MAX-PLAYERS";
	public static final String START_COUNTDOWN = "START-COUNTDOWN";
	public static final String GAME_DURATION_IN_SECONDS = "GAME-DURATION-IN-SECONDS";
	public static final String PLAYER_SPAWN = "PLAYER-SPAWN";
	
	protected Integer minPlayers;
	protected Integer maxPlayers;
	protected Integer startCountDown;
	protected Local lobbyLocation;
	protected Integer gameDurationInSeconds;
	protected String server_uuid;
	protected ServerInstance serverInstance;
	protected Arena arena;
	protected Game game;
	protected List<GameArenaConfig> gacList;
	protected List<GameConfigInstance> gciList;
	
	protected List<GameArenaConfig> spawnPoints;

	protected static TheCraftCloudConfig me;
	
	protected TheCraftCloudConfig() {
		Bukkit.getConsoleSender().sendMessage("Criou TheCraftCloudConfig");
	}
	
	public static TheCraftCloudConfig getInstance() {
		if(me == null) {
			me = new TheCraftCloudConfig();
		}
		return me;
	}
	
	public String getServer_uuid() {
		return this.server_uuid;
	}

	public Arena getArena() {
		return this.arena;
	}
	
	public Game getGame() {
		return this.game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public void setServer_uuid(String server_uuid) {
		this.server_uuid = server_uuid;
	}
	
	public Integer getMinPlayers() {
		return minPlayers;
	}
	public void setMinPlayers(Integer minPlayers) {
		this.minPlayers = minPlayers;
	}
	public Integer getMaxPlayers() {
		return maxPlayers;
	}
	public void setMaxPlayers(Integer maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	public Integer getStartCountDown() {
		return startCountDown;
	}
	public void setStartCountDown(Integer startCountDown) {
		this.startCountDown = startCountDown;
	}
	public Local getLobbyLocation() {
		return lobbyLocation;
	}
	public void setLobbyLocation(Local lobbyLocation) {
		this.lobbyLocation = lobbyLocation;
	}
	public Integer getGameDurationInSeconds() {
		return gameDurationInSeconds;
	}
	public void setGameDurationInSeconds(Integer gameDurationInSeconds) {
		this.gameDurationInSeconds = gameDurationInSeconds;
	}

	public ServerInstance getServerInstance() {
		return serverInstance;
	}

	public void setServerInstance(ServerInstance serverInstance) {
		this.serverInstance = serverInstance;
	}

	public void setArena(Arena arena) {
		this.arena = arena;
	}
	
	public void setGacList(List<GameArenaConfig> gacList) {
		this.gacList = gacList;
	}

	public void setGciList(List<GameConfigInstance> gciList) {
		this.gciList = gciList;
	}

	public List<GameArenaConfig> getGacList() {
		return gacList;
	}

	public List<GameConfigInstance> getGciList() {
		return gciList;
	}
	
	public List<String> getMandatoryConfigList() {
		List<String> gcList = new ArrayList<String>();
		
		gcList.add(GAME_DURATION_IN_SECONDS);
		gcList.add(MAX_PLAYERS);
		gcList.add(MIN_PLAYERS);
		gcList.add(LOBBY_LOCATION);
		gcList.add(START_COUNTDOWN);
		
		return gcList;
	}

	public List<GameArenaConfig> getSpawnPoints() {
		return spawnPoints;
	}

	public void setSpawnPoints(List<GameArenaConfig> spawnPoints) {
		this.spawnPoints = spawnPoints;
	}


}
