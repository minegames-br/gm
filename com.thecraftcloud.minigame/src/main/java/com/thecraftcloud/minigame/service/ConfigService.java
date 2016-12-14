package com.thecraftcloud.minigame.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.domain.GameConfigInstance;
import com.thecraftcloud.core.domain.GameConfigType;
import com.thecraftcloud.core.domain.GameInstance;
import com.thecraftcloud.core.domain.Local;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.json.JSONParser;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.minigame.TheCraftCloudConfig;
import com.thecraftcloud.minigame.domain.MyCloudCraftGame;

public class ConfigService {

	protected static ConfigService me;
	protected TheCraftCloudConfig config;
	private MyCloudCraftGame myCloudCraftGame;
	private GameInstance gameInstance;
		
	protected ConfigService() {
	}
	
	public static ConfigService getInstance() {
		if( me == null) {
			me = new ConfigService();
		}
		return me;
	}

	public void setGame(JavaPlugin plugin, Game game) {
		this.setProperty(plugin, "thecraftcloud.game.uuid", game.getGame_uuid().toString() );
		this.config.setGame(game);
	}
	
	public void setProperty(JavaPlugin plugin, String path, String value) {
		plugin.getConfig().set(path, value);
		plugin.saveConfig();
	}

	public void registerServer(JavaPlugin plugin, ServerInstance server) {
		String value = server.getServer_uuid().toString();
		this.config.setServer_uuid( server.getServer_uuid().toString() );
		this.setProperty(plugin, "thecraftcloud.server.uuid", value);
	}

	public void setArena(JavaPlugin plugin, Arena arena) {
		this.setProperty(plugin, "thecraftcloud.arena.uuid", arena.getArena_uuid().toString());
		this.config.setArena(arena);
	}

	public Arena getArena() {
		
		return this.config.getArena();
	}

	public boolean isGameReady() {
		boolean result = true;
		
		if(this.config.getGame() == null) {
			Bukkit.getLogger().info("Game is null");
			return false;
		}
		
		if(this.config.getArena() == null) {
			Bukkit.getLogger().info("Arena is null");
			return false;
		}
		
		if(this.config.getServer_uuid() == null) {
			Bukkit.getLogger().info("Server is null");
			return false;
		}
		
		return result;
	}

	public ServerInstance getServerInstance() {
		return this.config.getServerInstance();
	}

	public Game getGame() {
		return this.config.getGame();
	}

	public TheCraftCloudConfig getConfig() {
		return this.config;
	}

	public Object getGameArenaConfig(String name) {
		Object result = null;
		//if( isDebugging() ) Bukkit.getConsoleSender().sendMessage( Utils.color("&6 [TheCraftCloudPlugin] getGameArenaConfig: " + name) );

		for(GameArenaConfig gac: this.config.getGacList()) {
			//Bukkit.getLogger().info("getGameArenaConfig " + name + " comparing with " + gac.getGameConfig().getName());
			//if( isDebugging() ) Bukkit.getConsoleSender().sendMessage( Utils.color("&6 [TheCraftCloudPlugin] getGameArenaConfig: " + gac.getGameConfig().getName()) );

			if(gac.getGameConfig().getName().equalsIgnoreCase(name)) {
				if(gac.getGameConfig().getConfigType() == GameConfigType.INT) {
					result = gac.getIntValue();
				}else if(gac.getGameConfig().getConfigType() == GameConfigType.LOCAL) {
					result = gac.getLocalValue();
				}else if(gac.getGameConfig().getConfigType() == GameConfigType.AREA3D) {
					result = gac.getAreaValue();
				}else if(gac.getGameConfig().getConfigType() == GameConfigType.ITEM) {
					result = gac.getItemValue();
				}
				break;
			}
		}
		
		return result;
	}

	public boolean isDebugging() {
		return false;
	}

	public Object getGameConfigInstance(String name) {
		//if( isDebugging() ) Bukkit.getConsoleSender().sendMessage( Utils.color("&6 [TheCraftCloudPlugin] getGameConfigInstance: " + name) );
		Object result = null;
		//Bukkit.getLogger().info("getGameConfigInstance " + name );
		for(GameConfigInstance gci: this.config.getGciList()) {
			//if( isDebugging() ) Bukkit.getConsoleSender().sendMessage( Utils.color("&6 [TheCraftCloudPlugin] getGameConfigInstance: " + gci.getGameConfig().getName()) );
			//Bukkit.getLogger().info("getGameConfigInstance " + name + " comparing with " + gci.getGameConfig().getName());
			if(gci.getGameConfig().getName().equalsIgnoreCase(name)) {
				if(gci.getGameConfig().getConfigType() == GameConfigType.INT) {
					result = gci.getIntValue();
				}else if(gci.getGameConfig().getConfigType() == GameConfigType.LOCAL) {
					result = gci.getLocal();
				}else if(gci.getGameConfig().getConfigType() == GameConfigType.AREA3D) {
					result = gci.getArea();
				} else if(gci.getGameConfig().getConfigType() == GameConfigType.ITEM) {
					result = gci.getItem();
				}
				break;
			}
		}
		
		return result;
	}

	public Integer getStartCountDown() {
		return this.config.getStartCountDown();
	}

	public void setStartCountDown() {
		this.config.setStartCountDown( (Integer)this.getGameConfigInstance(TheCraftCloudConfig.START_COUNTDOWN) );
	}
	
	public Integer getGameDurationInSeconds() {
		Integer gameDuration = (Integer)this.getGameConfigInstance(TheCraftCloudConfig.GAME_DURATION_IN_SECONDS);
		return gameDuration;
	}


	public Local getLobby() {
		return this.config.getLobbyLocation();
	}

	public void setLobby() {
		Local l = (Local)this.getGameArenaConfig(TheCraftCloudConfig.LOBBY_LOCATION);
		this.config.setLobbyLocation( l );
	}

	public Integer getMinPlayers() {
		Integer minPlayers = (Integer)this.getGameConfigInstance(TheCraftCloudConfig.MIN_PLAYERS);
		return minPlayers;
	}

	public Integer getMaxPlayers() {
		Integer maxPlayers = (Integer)this.getGameConfigInstance(TheCraftCloudConfig.MAX_PLAYERS);
		return maxPlayers;
	}
	
	public boolean isConfigValid() {
		boolean valid = true;
		for(String name: this.config.getMandatoryConfigList()) {
			Object gci = this.getGameConfigInstance(name);
			Object gca = this.getGameArenaConfig(name);
			if(gci == null && gca == null) {
				//System.out.println("missing config: " + name);
				valid = false;
			}
		}
		return valid;
	}
	
	public List<GameArenaConfig> getGameArenaConfigGroup(String groupName ) {
		List<GameArenaConfig> gacList = new ArrayList<GameArenaConfig>();
		for(GameArenaConfig gac: this.config.getGacList()) {
			if(gac.getGameConfig().getGroupName().equalsIgnoreCase( groupName )) {
				gacList.add(gac);
			}
		}
		return gacList;
	}

	public List<GameConfigInstance> getGameConfigInstanceGroup(String groupName) {
		List<GameConfigInstance> gciList = new ArrayList<GameConfigInstance>();
		for(GameConfigInstance gci: this.config.getGciList()) {
			if(gci.getGameConfig().getGroupName().equalsIgnoreCase( groupName )) {
				gciList.add(gci);
			}
		}
		return gciList;
	}
	
	public CopyOnWriteArraySet<GameArenaConfig> getSpawnPoints() {
		return this.config.getSpawnPoints();
	}

	public void setGacList(CopyOnWriteArraySet<GameArenaConfig> gacList) {
		this.config.setGacList(gacList);
	}
	
	public void setGciList(CopyOnWriteArraySet<GameConfigInstance> gciList) {
		this.config.setGciList(gciList);
	}

	public MyCloudCraftGame getMyCloudCraftGame() {
		return this.myCloudCraftGame;
	}
	
	public void setMyCloudCraftGame(MyCloudCraftGame myCloudCraftGame) {
		this.myCloudCraftGame = myCloudCraftGame;
	}
	
	public CopyOnWriteArraySet<GameArenaConfig> getGameArenaConfigByGroup(String group) {
		
		CopyOnWriteArraySet<GameArenaConfig> gacs = new CopyOnWriteArraySet<GameArenaConfig>();
		for(GameArenaConfig gac: this.config.getGacList() ) {
			if(gac.getGameConfig().getGroupName().equalsIgnoreCase(group)){
				gacs.add(gac);
			}
		}
		
		return gacs;
	}

	public World getWorld() {
		return this.config.getWorld();
	}

	public void setWorld(World world) {
		this.config.setWorld(world);
	}

	public void setConfig(TheCraftCloudConfig instance) {
		this.config = instance;
	}

	public World getArenaWorld() {
		return Bukkit.getWorld(this.config.getArena().getName());
	}

	public GameInstance getGameInstance() {
		return this.gameInstance;
	}	
	
	public void setGameInstance(GameInstance gi) {
		this.gameInstance = gi;
	}
}
