package com.thecraftcloud.plugin.service;

import java.io.File;
import java.io.IOException;

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
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.json.JSONParser;
import com.thecraftcloud.plugin.TheCraftCloudConfig;

public class ConfigService {

	private static ConfigService me;
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

	    if(game_uuid == null || arena_uuid == null || server_uuid == null) { 
			throw new InvalidRegistrationException("Please check your The Craft Cloud setup before you start the game.");
		}
		
    	File configDir = plugin.getDataFolder();

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
				this.config.setGame( delegate.getInstance().findGame(game_uuid) );
				json = JSONParser.getInstance().toJSONString(this.config.getGame() );
				FileUtils.writeStringToFile(gameJsonFile, json);
			}

			File arenaJsonFile = new File( configDir, arena_uuid + ".json");
			if( arenaJsonFile.exists()  && !force) {
				json = FileUtils.readFileToString(arenaJsonFile);
				this.config.setArena( (Arena)JSONParser.getInstance().toObject(json, Arena.class) );
			} else {
				this.config.setArena( delegate.getInstance().findArena(arena_uuid) );
				json = JSONParser.getInstance().toJSONString(this.config.getArena());
				FileUtils.writeStringToFile(arenaJsonFile, json);
			}

			File serverJsonFile = new File( configDir, server_uuid + ".json");
			if( serverJsonFile.exists()  && !force) {
				json = FileUtils.readFileToString(serverJsonFile);
				this.config.setServerInstance( (ServerInstance)JSONParser.getInstance().toObject(json, ServerInstance.class) );
			} else {
				this.config.setServerInstance( delegate.getInstance().findServerInstance(this.config.getServer_uuid() ) );
			    this.delegate.validateRegistration(this.config.getServer_uuid() );
				json = JSONParser.getInstance().toJSONString(this.config.getServerInstance() );
				FileUtils.writeStringToFile(serverJsonFile, json);
			}
			
			this.config.setGacList( this.delegate.findAllGameConfigArenaByGameUUID( this.config.getGame().getGame_uuid().toString() ) );
			this.config.setGciList( this.delegate.findAllGameConfigInstanceByGameUUID( this.config.getGame().getGame_uuid().toString() ) );
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new InvalidRegistrationException("Server not registered or Game/Arena config is invalid.");
		}


	}
	
	protected TheCraftCloudConfig createConfigDomain() {
		return new TheCraftCloudConfig();
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
		//Bukkit.getLogger().info("getGameArenaConfig " + name );

		for(GameArenaConfig gac: this.config.getGacList()) {
			//Bukkit.getLogger().info("getGameArenaConfig " + name + " comparing with " + gac.getGameConfig().getName());
			if(gac.getGameConfig().getName().equals(name)) {
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
		Object result = null;
		//Bukkit.getLogger().info("getGameConfigInstance " + name );
		for(GameConfigInstance gci: this.config.getGciList()) {
			//Bukkit.getLogger().info("getGameConfigInstance " + name + " comparing with " + gci.getGameConfig().getName());
			if(gci.getGameConfig().getName().equals(name)) {
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
	
}
