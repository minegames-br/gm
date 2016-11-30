package com.thecraftcloud.admin.action;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParseException;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.thecraftcloud.client.exception.InvalidRegistrationException;
import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.domain.GameConfigInstance;
import com.thecraftcloud.core.json.JSONParser;
import com.thecraftcloud.core.util.zip.ExtractZipContents;

public class UpdateOfflineConfigAction {
	
	/**
	private void updateOfflineConfig(Boolean force) throws InvalidRegistrationException {
		try{
	    	File configDir = getDataFolder();
	
			String json = null;
	        ObjectMapper mapper = new ObjectMapper();
	
			File gameConfigListJsonFile = new File( configDir, "gameConfigList" + ".json");
			if( gameConfigListJsonFile.exists() && !force ) {
				Bukkit.getLogger().info("Game Config List Json exists");
				json = FileUtils.readFileToString(gameConfigListJsonFile);
				try {
					this.gameConfigInstanceList = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, GameConfigInstance.class));
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Bukkit.getLogger().info("GameConfigList size: " + this.gameConfigInstanceList.size() );
			} else {
				this.gameConfigInstanceList = delegate.findAllGameConfigInstanceByGameUUID(this.game.getGame_uuid().toString());
				json = JSONParser.getInstance().toJSONString(this.gameConfigInstanceList);
				FileUtils.writeStringToFile(gameConfigListJsonFile, json);
			}
			
			File gameArenaConfigListJsonFile = new File( configDir, "gameArenaConfigList" + ".json");
			if( gameArenaConfigListJsonFile.exists() && !force ) {
				Bukkit.getLogger().info("Game Arena Config List Json exists");
				json = FileUtils.readFileToString(gameArenaConfigListJsonFile);
				try {
					this.gameArenaConfigList = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, GameArenaConfig.class));
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Bukkit.getLogger().info("GameArenaConfigList size: " + this.gameArenaConfigList.size() );
			} else {
				this.gameArenaConfigList = delegate.findAllGameConfigArenaByGameArena(this.game.getGame_uuid().toString(), this.arena.getArena_uuid().toString());
				json = JSONParser.getInstance().toJSONString(this.gameArenaConfigList);
				FileUtils.writeStringToFile(gameArenaConfigListJsonFile, json);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new InvalidRegistrationException("Server not registered or Game/Arena config is invalid.");
		}
		Bukkit.getLogger().info("GameConfigList size: " + this.gameConfigInstanceList.size() );
		Bukkit.getLogger().info("GameArenaConfigList size: " + this.gameArenaConfigList.size() );

	}

	public void startArenaBuild(String arenaName) {
		File dir = this.getServer().getWorldContainer();
		//File schematicFile = delegate.downloadArenaSchematic(arena.getArena_uuid(), dir.getAbsolutePath());
		File arenaWorldFile = new File(dir, arena.getName());
		
		MultiverseCore mvplugin = (MultiverseCore)Bukkit.getPluginManager().getPlugin("Multiverse-Core");
		String worldPath = dir.getAbsolutePath() + "/" + arena.getName();
				
		if(!arenaWorldFile.exists()) {
			File zipFile = delegate.downloadArenaWorld(arena, dir);
			ExtractZipContents.unzip(zipFile);
			Bukkit.getLogger().info("world path: " + worldPath);
			mvplugin.getCore().getMVWorldManager().addWorld(arena.getName(), Environment.NORMAL, new Integer( arena.getName().hashCode() ).toString(), WorldType.NORMAL, new Boolean(false), null, false);
		} else {
			//Remover o mundo do multi verse para recarregar
			mvplugin.getMVWorldManager().unloadWorld(arena.getName());
			
			//Apagar o diretório
			try {
				FileUtils.deleteDirectory(arenaWorldFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//Descompactar arena
			File zipFile = new File( dir, arena.getName() + ".zip");
			ExtractZipContents.unzip(zipFile);
			
			//Carregar o mundo no multiverse
			Bukkit.getLogger().info("world path: " + worldPath);
			mvplugin.getCore().getMVWorldManager().addWorld(arena.getName(), Environment.NORMAL, new Integer( arena.getName().hashCode() ).toString(), WorldType.NORMAL, new Boolean(false), null, false);
		}
		this.world = Bukkit.getWorld(this.arena.getName());
	}
	

	/**
	 * 
    	for(final GameConfigInstance gcc: plugin.getGameGameConfigMap().values() ) {
    		scheduler.runTaskAsynchronously(this, new Runnable() {
    			@Override
    			public void run() {
            		if(gcc.getGameConfig().getGame_config_uuid() == null) {
    	            	delegate.createGameConfigInstance(gcc);
            		} else {
            			delegate.updateGameConfigInstance(gcc);
            		}
	            }
    		});
    	}
        		
	    for(final GameArenaConfig gac: plugin.getGameConfigArenaMap().values()) {
    		scheduler.runTaskAsynchronously(this, new Runnable() {
    			@Override
    			public void run() {
	            	if(gac.getGac_uuid() == null) {
		            	delegate.createGameArenaConfig(gac);
	            	} else {
	            		delegate.updateGameArenaConfig(gac);
	            	}
	            }
            });
		}

		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.runTaskAsynchronously(this, new Runnable() {
			@Override
			public void run() {
        		if(gameConfig.getConfigScope() == GameConfigScope.GLOBAL) {
        			if(gci.getGameConfig().getGame_config_uuid() == null) {
        				delegate.createGameConfigInstance(gci);
        			} else {
        				delegate.updateGameConfigInstance(gci);
        			}
        		} else {
	            	if(gac.getGac_uuid() == null) {
		            	delegate.createGameArenaConfig(gac);
	            	} else {
	            		delegate.updateGameArenaConfig(gac);
	            	}
        		}
            }
		});

	 
	public void loadTheCraftCloudData(JavaPlugin plugin, boolean force) throws InvalidRegistrationException {
		
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
	
	 
	 
	 */
}
