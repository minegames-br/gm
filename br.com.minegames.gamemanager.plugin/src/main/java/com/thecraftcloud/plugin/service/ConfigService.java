package com.thecraftcloud.plugin.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.client.exception.InvalidRegistrationException;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.domain.GameConfigInstance;
import com.thecraftcloud.core.domain.GameConfigType;
import com.thecraftcloud.core.domain.Local;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.json.JSONParser;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.plugin.TheCraftCloudConfig;

public class ConfigService {

	protected static ConfigService me;
	protected TheCraftCloudConfig config;
	protected TheCraftCloudDelegate delegate;
		
	protected ConfigService() {
		this.delegate = TheCraftCloudDelegate.getInstance();
	}
	
	public static ConfigService getInstance() {
		if( me == null) {
			me = new ConfigService();
		}
		return me;
	}

	public void loadTheCraftCloudData(JavaPlugin plugin, boolean force) throws InvalidRegistrationException {
	    this.delegate = TheCraftCloudDelegate.getInstance();
		
	    //this.game = delegate.findGame("57b7b3df-9d18-4966-898f-f4ad8ee28a92");
		String game_uuid = plugin.getConfig().getString("thecraftcloud.game.uuid");
		String arena_uuid = plugin.getConfig().getString("thecraftcloud.arena.uuid");
		String server_uuid = plugin.getConfig().getString("thecraftcloud.server.uuid");
		this.config = TheCraftCloudConfig.getInstance();
		this.config.setServer_uuid(server_uuid);

	    if(game_uuid == null || arena_uuid == null || server_uuid == null) { 
			throw new InvalidRegistrationException("Please check your The Craft Cloud setup before you start the game.");
		}
		
    	File configDir = plugin.getDataFolder();
		Bukkit.getConsoleSender().sendMessage("[TheCraftCloud] Config dir: " + configDir.getAbsolutePath() );

    	//create offline data
    	File gameJsonFile = new File( configDir, game_uuid + ".json");
    	Bukkit.getLogger().info(gameJsonFile.getAbsolutePath());
    	String json = null;
		try {
			if( gameJsonFile.exists() && !force) {
		    	Bukkit.getLogger().info("File exists: " + gameJsonFile.getAbsolutePath() + ". Loading config offline.");
				json = FileUtils.readFileToString(gameJsonFile);
				this.config.setGame( (Game)JSONParser.getInstance().toObject(json, Game.class) );
			} else {
		    	Bukkit.getLogger().info("[TheCraftCloud] Game File does not exist: " + gameJsonFile.getAbsolutePath() + ". Loading config online.");
				this.config.setGame( delegate.getInstance().findGame(game_uuid) );
				json = JSONParser.getInstance().toJSONString(this.config.getGame() );
				FileUtils.writeStringToFile(gameJsonFile, json);
			}

			File arenaJsonFile = new File( configDir, arena_uuid + ".json");
			if( arenaJsonFile.exists()  && !force) {
		    	Bukkit.getLogger().info("[TheCraftCloud] Arena File exists: " + arenaJsonFile.getAbsolutePath() + ". Loading config offline.");
				json = FileUtils.readFileToString(arenaJsonFile);
				this.config.setArena( (Arena)JSONParser.getInstance().toObject(json, Arena.class) );
			} else {
		    	Bukkit.getLogger().info("[TheCraftCloud] Arena File does not exist: " + arenaJsonFile.getAbsolutePath() + ". Loading config online.");
				this.config.setArena( delegate.getInstance().findArena(arena_uuid) );
				json = JSONParser.getInstance().toJSONString(this.config.getArena());
				FileUtils.writeStringToFile(arenaJsonFile, json);
			}

			File serverJsonFile = new File( configDir, server_uuid + ".json");
			if( serverJsonFile.exists()  && !force) {
		    	Bukkit.getLogger().info("[TheCraftCloud] Server File exists: " + serverJsonFile.getAbsolutePath() + ". Loading config offline.");
				json = FileUtils.readFileToString(serverJsonFile);
				this.config.setServerInstance( (ServerInstance)JSONParser.getInstance().toObject(json, ServerInstance.class) );
				this.config.setServer_uuid(this.config.getServerInstance().getServer_uuid().toString());
		    	Bukkit.getLogger().info("[TheCraftCloud] Server UUID setup: " + this.config.getServer_uuid() );
		    	Bukkit.getLogger().info("[TheCraftCloud] Server Instance setup: " + this.config.getServerInstance() );
			} else {
		    	Bukkit.getLogger().info("[TheCraftCloud] Server File does not exist: " + serverJsonFile.getAbsolutePath() + ". Loading config online.");
				this.config.setServerInstance( delegate.getInstance().findServerInstance(this.config.getServer_uuid() ) );
			    ServerInstance server = this.delegate.validateRegistration(this.config.getServer_uuid() );
			    this.config.setServerInstance(server);
		    	Bukkit.getLogger().info("[TheCraftCloud] Server UUID setup: " + this.config.getServer_uuid() );
		    	Bukkit.getLogger().info("[TheCraftCloud] Server Instance setup: " + this.config.getServerInstance() );
				json = JSONParser.getInstance().toJSONString(this.config.getServerInstance() );
				FileUtils.writeStringToFile(serverJsonFile, json);
			}
			
			this.config.setGacList( this.delegate.findAllGameConfigArenaByGameUUID( this.config.getGame().getGame_uuid().toString() ) );
			this.config.setGciList( this.delegate.findAllGameConfigInstanceByGameUUID( this.config.getGame().getGame_uuid().toString() ) );
			
		} catch (IOException e) {
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("[TheCraftCloud] Server not registered or Game/Arena config is invalid.");
			throw new InvalidRegistrationException("Server not registered or Game/Arena config is invalid.");
		}

		//Locais de spawn
		this.config.setSpawnPoints(this.getGameArenaConfigGroup(TheCraftCloudConfig.PLAYER_SPAWN));
		

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
		Bukkit.getConsoleSender().sendMessage( Utils.color("&6 [TheCraftCloudPlugin] getGameArenaConfig: " + name) );

		for(GameArenaConfig gac: this.config.getGacList()) {
			//Bukkit.getLogger().info("getGameArenaConfig " + name + " comparing with " + gac.getGameConfig().getName());
			Bukkit.getConsoleSender().sendMessage( Utils.color("&6 [TheCraftCloudPlugin] getGameArenaConfig: " + gac.getGameConfig().getName()) );

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

	public Object getGameConfigInstance(String name) {
		Bukkit.getConsoleSender().sendMessage( Utils.color("&6 [TheCraftCloudPlugin] getGameConfigInstance: " + name) );
		Object result = null;
		//Bukkit.getLogger().info("getGameConfigInstance " + name );
		for(GameConfigInstance gci: this.config.getGciList()) {
			Bukkit.getConsoleSender().sendMessage( Utils.color("&6 [TheCraftCloudPlugin] getGameConfigInstance: " + gci.getGameConfig().getName()) );
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
				System.out.println("missing config: " + name);
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
	
	public List<GameArenaConfig> getSpawnPoints() {
		return this.config.getSpawnPoints();
	}
}
