package com.thecraftcloud.plugin;

import java.util.List;

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
	
	protected Integer minPlayers;
	protected Integer maxPlayers;
	protected Integer startCountDown;
	protected Local lobbyLocation;
	protected Integer gameDurationInSeconds;
	private String server_uuid;
	private ServerInstance serverInstance;
	private Arena arena;
	private Game game;
	private List<GameArenaConfig> gacList;
	private List<GameConfigInstance> gciList;

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
	

}
