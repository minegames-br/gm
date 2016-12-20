package com.thecraftcloud.client;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thecraftcloud.client.exception.InvalidRegistrationException;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.domain.AdminQueue;
import com.thecraftcloud.core.domain.Area3D;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.domain.GameConfig;
import com.thecraftcloud.core.domain.GameConfigInstance;
import com.thecraftcloud.core.domain.GameInstance;
import com.thecraftcloud.core.domain.GameQueue;
import com.thecraftcloud.core.domain.GameQueueStatus;
import com.thecraftcloud.core.domain.GameWorld;
import com.thecraftcloud.core.domain.Item;
import com.thecraftcloud.core.domain.Kit;
import com.thecraftcloud.core.domain.Local;
import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.domain.Schematic;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.dto.SearchGameWorldDTO;
import com.thecraftcloud.core.json.JSONParser;

public class TheCraftCloudDelegate {

	private static TheCraftCloudDelegate me;
	private String gameManagerUrl;
	private String serverUuid;
	
	private TheCraftCloudDelegate() {
		
	}
	
	public static TheCraftCloudDelegate getInstance() {
		String gamemanagerUrl = TheCraftCloudClientPlugin.getMinegamesGameManagerUrl();
		//MG.getLogger().info("URL: "+ gamemanagerUrl);
		if(me == null) {
			me = new TheCraftCloudDelegate();
		}
		me.gameManagerUrl = gamemanagerUrl;
		return me;
	}
	
	public static TheCraftCloudDelegate getInstance(String url) {
		if(me == null) {
			me = new TheCraftCloudDelegate();
			me.gameManagerUrl = url;
		}
		return me;
	}
	
	public ServerInstance registerServer(String name) {
		ServerInstance server = null;
		try {
			String serverIp = InetAddress.getLocalHost().getHostAddress();
			String hostname = InetAddress.getLocalHost().getCanonicalHostName();
			server = new ServerInstance();
			server.setIp_address(serverIp);
			server.setHostname(hostname);
			server.setName(name);
			
			String json = JSONParser.getInstance().toJSONString(server);
			json = post("/server", json);

			server = (ServerInstance)JSONParser.getInstance().toObject(json, ServerInstance.class);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return server;
	}
	
	public ServerInstance updateServer(ServerInstance server) {
		String json = JSONParser.getInstance().toJSONString(server);
		json = post("/server/" + server.getServer_uuid().toString(), json);
		server = (ServerInstance)JSONParser.getInstance().toObject(json, ServerInstance.class);

		return server;
	}
	
	public ServerInstance createServer(ServerInstance server) {
		String json = JSONParser.getInstance().toJSONString(server);
		json = post("/server/", json);
		server = (ServerInstance)JSONParser.getInstance().toObject(json, ServerInstance.class);
		return server;
	}
	
	public Area3D addArea3D(Area3D area) {
		Area3D result = null;
		
		String json = JSONParser.getInstance().toJSONString(area);
		json = post("/area", json);
		result = (Area3D)JSONParser.getInstance().toObject(json, Area3D.class);
		
		return result;
	}
	
	public Local addLocal(Local local) {
		String json = JSONParser.getInstance().toJSONString(local);
		json = post("/local", json);
		local = (Local) JSONParser.getInstance().toObject(json, Local.class);
		return local;
	}
	
	private String post(String path, String jsonString) {
		ClientRequest request = new ClientRequest(this.gameManagerUrl + path);
		ClientResponse response = null;
		try {
			request.body("application/json", jsonString);
			response = request.post(String.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result = (String)response.getEntity(String.class);
		System.out.println(result);
        return result;
		
	}
	
	private boolean post(String path) {
		ClientRequest request = new ClientRequest(this.gameManagerUrl + path);
		ClientResponse response = null;
		try {
			request.body("application/json", "");
			response = request.post(String.class);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private String delete(String path) {
		ClientRequest request = new ClientRequest(this.gameManagerUrl + path);
		ClientResponse response = null;
		try {
			response = request.delete(String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String result = (String)response.getEntity(String.class);
        return result;
		
	}
	
	private String get(String path, String uuid) {
		ClientRequest client = new ClientRequest(this.gameManagerUrl + path + "/" + uuid);
		ClientResponse response = null;
		try {
			response = client.get(String.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result = (String) response.getEntity(String.class);
        return result;
	}

	private String get(String path) {
		ClientRequest client = new ClientRequest(this.gameManagerUrl + path);
		ClientResponse response = null;
		try {
			response = client.get(String.class);
			if(response.getStatus() != 200) {
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result = (String) response.getEntity(String.class);
        return result;
	}

	private List<Arena> getArenaList(String path) {
		ClientRequest client = new ClientRequest(this.gameManagerUrl + path);
		ClientResponse response = null;
		try {
			response = client.get(String.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        ObjectMapper mapper = new ObjectMapper();
        String json = (String)response.getEntity(String.class);
        List<Arena> myObjects = null;
		try {
			myObjects = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, Arena.class));
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
        return myObjects;
	}
	
	public List<Game> findGames() {
        String json = get("/game/list");
        ObjectMapper mapper = new ObjectMapper();
        List<Game> myObjects = null;
		try {
			myObjects = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, Game.class));
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myObjects;
	}

	public ServerInstance findServerInstance(String server_uuid) {
		ServerInstance server = null;
		String json = get("/server", server_uuid );
		server = (ServerInstance)JSONParser.getInstance().toObject(json, ServerInstance.class);
		
		return server;
	}

	public GameWorld findGameWorldByServerAndName(ServerInstance server, String name) {
		GameWorld gameWorld = null;
		
		SearchGameWorldDTO dto = new SearchGameWorldDTO();
		dto.setServer_uuid(server.getServer_uuid().toString());
		dto.setWorldName(name);
		
		String json = JSONParser.getInstance().toJSONString(dto);
		
		json = get("/world/search", json);
		gameWorld = (GameWorld)JSONParser.getInstance().toObject(json, GameWorld.class);
		
		return gameWorld;
	}

	public List<Arena> findArenas(String filterName) {
		List<Arena> arenas = getArenaList("/arena/list");
		//Bukkit.getLogger().info("listarenas: " + json);
		return arenas;
	}
	
	public GameArenaConfig createGameArenaConfig(GameArenaConfig config) {
		String json = JSONParser.getInstance().toJSONString(config);
		json = post("/gamearenaconfig", json);
		config = (GameArenaConfig) JSONParser.getInstance().toObject(json, GameArenaConfig.class);
		return config;
	}

	public GameArenaConfig updateGameArenaConfig(GameArenaConfig domain) {
		String json = JSONParser.getInstance().toJSONString(domain);
		json = post("/gamearenaconfig/" + domain.getGac_uuid().toString(), json);
		domain = (GameArenaConfig) JSONParser.getInstance().toObject(json, GameArenaConfig.class);
		return domain;
	}
	
	public Arena findArena(String arena_uuid) {
		Arena arena = null;
		String json = get("/arena", arena_uuid );
		arena = (Arena)JSONParser.getInstance().toObject(json, Arena.class);
		
		return arena;
	}

	public Arena createArena(Arena arena) {
		String json = JSONParser.getInstance().toJSONString(arena);
		json = post("/arena", json);
		arena = (Arena) JSONParser.getInstance().toObject(json, Arena.class);
		return arena;
	}
	
	public Game createGame(Game game ) {
		String json = JSONParser.getInstance().toJSONString(game);
		json = post("/game", json);
		game = (Game) JSONParser.getInstance().toObject(json, Game.class);
		return game;
	}

	public Game findGame(String uuid)  {
		Game domain = null;
		String json = get("/game", uuid );
		domain = (Game)JSONParser.getInstance().toObject(json, Game.class);
		return domain;
	}
	
	public Schematic createSchematic(Schematic domain) {
		String json = JSONParser.getInstance().toJSONString(domain);
		json = post("/schematic", json);
		domain = (Schematic) JSONParser.getInstance().toObject(json, Schematic.class);
		return domain;
	}


	public Schematic findSchematic(String uuid) {
		Schematic domain = null;
		String json = get("/schematic", uuid );
		domain = (Schematic)JSONParser.getInstance().toObject(json, Schematic.class);
		
		return domain;
	}

	public void uploadSchematic(Schematic schematic, File file) {
		ClientRequest request = new ClientRequest(this.gameManagerUrl + "/schematic/upload/" + schematic.getSchematic_uuid().toString() );
		request.accept("application/json");
		
		MultipartFormDataOutput upload = new MultipartFormDataOutput();
		try {
			upload.addFormData("file", FileUtils.readFileToByteArray(file), MediaType.APPLICATION_OCTET_STREAM_TYPE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.body(MediaType.MULTIPART_FORM_DATA_TYPE, upload);
		try {
			ClientResponse<?> response = request.post();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void uploadWorld(GameWorld gw, File file) {
		ClientRequest request = new ClientRequest(this.gameManagerUrl + "/world/" + gw.getWorld_uuid().toString() + "/upload");
		request.accept("application/json");
		
		MultipartFormDataOutput upload = new MultipartFormDataOutput();
		try {
			upload.addFormData("file", FileUtils.readFileToByteArray(file), MediaType.APPLICATION_OCTET_STREAM_TYPE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.body(MediaType.MULTIPART_FORM_DATA_TYPE, upload);
		try {
			ClientResponse<?> response = request.post();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<GameArenaConfig> listGameArenaConfig() {
        String json = get("/gamearenaconfig/list");
        ObjectMapper mapper = new ObjectMapper();
        List<GameArenaConfig> myObjects = null;
		try {
			myObjects = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, GameArenaConfig.class));
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myObjects;
	}

	public GameConfig addGameConfig(GameConfig domain) {
		String json = JSONParser.getInstance().toJSONString(domain);
		json = post("/gameconfig", json);
		domain = (GameConfig) JSONParser.getInstance().toObject(json, GameConfig.class);
		return domain;
	}

	public List<GameConfig> listGameConfig(Game game) {
        String json = get("/game/" + game.getGame_uuid().toString() + "/config/list");
        ObjectMapper mapper = new ObjectMapper();
        List<GameConfig> myObjects = null;
		try {
			myObjects = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, GameConfig.class));
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myObjects;
	}

	public List<GameArenaConfig> findAllGameConfigArenaByGameUUID(String uuid) {
        String json = get("/game/" + uuid + "/gamearenaconfig/list");
        ObjectMapper mapper = new ObjectMapper();
        List<GameArenaConfig> myObjects = null;
		try {
			myObjects = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, GameArenaConfig.class));
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myObjects;
	}


	public List<GameConfigInstance> findAllGameConfigInstanceByGameUUID(String uuid) {
        String json = get("/game/" + uuid + "/gameconfiginstance/list");
        ObjectMapper mapper = new ObjectMapper();
        List<GameConfigInstance> myObjects = null;
		try {
			myObjects = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, GameConfigInstance.class));
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myObjects;
	}

	public GameConfigInstance createGameConfigInstance(GameConfigInstance domain) {
		String json = JSONParser.getInstance().toJSONString(domain);
		json = post("/game/config/instance/add", json);
		domain = (GameConfigInstance) JSONParser.getInstance().toObject(json, GameConfigInstance.class);
		return domain;
	}

	public GameConfigInstance updateGameConfigInstance(GameConfigInstance domain) {
		String json = JSONParser.getInstance().toJSONString(domain);
		json = post("/game/config/instance/" + domain.getGci_uuid().toString(), json);

		domain = (GameConfigInstance) JSONParser.getInstance().toObject(json, GameConfigInstance.class);
		return domain;
	}
	
	public static void main(String args[]) throws InvalidRegistrationException {
		TheCraftCloudClientPlugin.setMinegamesGameManagerUrl( "http://services.thecraftcloud.com:8080/gamemanager/webresources" );
		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance("http://services.thecraftcloud.com:8080/gamemanager/webresources");
		delegate.lockGameQueue();
	}

	public Game updateGame(Game game) {
		String json = JSONParser.getInstance().toJSONString(game);
		json = post("/game/" + game.getGame_uuid(), json);
		game = (Game) JSONParser.getInstance().toObject(json, Game.class);
		return game;
	}

	public GameConfig findGameConfig(String uuid, String gameConfigUuid) {
		GameConfig domain = null;
		String json = get("/game/" + uuid + "/config", gameConfigUuid);
		domain = (GameConfig)JSONParser.getInstance().toObject(json, GameConfig.class);
		return domain;
	}

	public Area3D findArea3D(String uuid) {
		Area3D domain = null;
		String json = get("/area", uuid);
		domain = (Area3D)JSONParser.getInstance().toObject(json, Area3D.class);
		return domain;
	}

	public Arena findArenaByName(String name) {
		String json = get("/arena/search", name );
		Arena arena = (Arena)JSONParser.getInstance().toObject(json, Arena.class);
		return arena;
	}

	public List<Arena> findArenasByGameUUID(UUID uuid) {
		String json = get("/" + uuid.toString() + "/arena/list" );
		
        ObjectMapper mapper = new ObjectMapper();
        List<Arena> myObjects = null;
		try {
			myObjects = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, Arena.class));
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return myObjects;
	}
	
	public File downloadArenaSchematic(UUID uuid, String path) {
		File dir = null;
		File file = null;
		Arena arena = findArena(uuid.toString());
	    String URL=this.gameManagerUrl + "/arena/" + uuid.toString() + "/schematic";
	    ClientRequest client = new ClientRequest(URL);
	    try {
	        URL website = new URL(URL);
	        dir = new File(path);
	        if(!dir.exists() ) {
	        	dir.mkdirs();
	        }
	        
	        file = new File( dir, arena.getArena_uuid().toString() + ".schematic");
	        
	        FileUtils.copyURLToFile(website, file);
	    } catch ( Exception ex) {
	    	ex.printStackTrace();
	    }  
        return file;
	}

	public File downloadArenaWorld(Arena arena, File dir) {
		File file = null;
	    String URL=this.gameManagerUrl + "/arena/" + arena.getArena_uuid().toString() + "/world";
	    ClientRequest client = new ClientRequest(URL);
	    try {
	        URL website = new URL(URL);
	        if(!dir.exists() ) {
	        	dir.mkdirs();
	        }
	        
	        file = new File( dir + "/", arena.getName() + ".zip");
	        FileUtils.copyURLToFile(website, file);
	    } catch ( Exception ex) {
	    	ex.printStackTrace();
	    }  
        return file;
	}

	public File downloadWorld(GameWorld gw, File dir) {
		File file = null;
	    String URL=this.gameManagerUrl + "/world/" + gw.getWorld_uuid().toString() + "/download";
	    ClientRequest client = new ClientRequest(URL);
	    try {
	        URL website = new URL(URL);
	        if(!dir.exists() ) {
	        	dir.mkdirs();
	        }
	        file = new File( dir + "/", gw.getName() + ".zip");
	        FileUtils.copyURLToFile(website, file);
	    } catch ( Exception ex) {
	    	ex.printStackTrace();
	    }  
        return file;
	}

	public List<GameArenaConfig> findAllGameConfigArenaByGameArena(String gameUuid, String arenaUuid) {
        String json = get("/game/" + gameUuid + "/gamearenaconfig/" + arenaUuid);
        ObjectMapper mapper = new ObjectMapper();
        List<GameArenaConfig> myObjects = null;
		try {
			myObjects = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, GameArenaConfig.class));
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myObjects;
	}

	public Arena updateArena(Arena arena) {
		String json = JSONParser.getInstance().toJSONString(arena);
		json = post("/arena/" + arena.getArena_uuid(), json);
		arena = (Arena) JSONParser.getInstance().toObject(json, Arena.class);
		return arena;
	}

	public ServerInstance validateRegistration(String server_uuid) {
		this.serverUuid = server_uuid;
		ServerInstance domain = findServerInstance(server_uuid);
		return domain;
	}

	public Item addItem(Item item) {
		String json = JSONParser.getInstance().toJSONString(item);
		json = post("/item", json);
		item = (Item) JSONParser.getInstance().toObject(json, Item.class);
		return item;
	}

	public Kit addKit(Kit kit) {
		String json = JSONParser.getInstance().toJSONString(kit);
		json = post("/kit", json);
		kit = (Kit) JSONParser.getInstance().toObject(json, Kit.class);
		return kit;
	}

	public Item findItemByName(String name) {
		Item item = null;
		String json = "{}";
		json = get("/item/search/" + name);
		item = (Item)JSONParser.getInstance().toObject(json, Item.class);
		return item;
	}

	public GameConfig findGameConfigByName(String name) {
		GameConfig gc = null;
		String json = "{}";
		json = get("/gameconfig/search/" + name);
		gc = (GameConfig)JSONParser.getInstance().toObject(json, GameConfig.class);
		return gc;
	}

	public ServerInstance findServerByName(String name) {
		ServerInstance server = null;
		String json = "{}";
		json = get("/server/search/" + name);
		server = (ServerInstance)JSONParser.getInstance().toObject(json, ServerInstance.class);
		return server;
	}

	public List<ServerInstance> findAllServerInstance() {
        String json = get("/server/list");
        ObjectMapper mapper = new ObjectMapper();
        List<ServerInstance> myObjects = null;
		try {
			myObjects = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, ServerInstance.class));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return myObjects;
	}

	public GameInstance createGameInstance(GameInstance gi) {
		String json = JSONParser.getInstance().toJSONString(gi);
		json = post("/gameinstance/", json);
		gi = (GameInstance)JSONParser.getInstance().toObject(json, GameInstance.class);
		return gi;
	}

	public GameInstance findGameInstanceByUUID(String uuid) {
		GameInstance domain = null;
		String json = get("/gameinstance", uuid);
		domain = (GameInstance)JSONParser.getInstance().toObject(json, GameInstance.class);
		return domain;
	}

	public GameInstance updateGameInstance(GameInstance gi) {
		String json = JSONParser.getInstance().toJSONString(gi);
		json = post("/gameinstance/" + gi.getGi_uuid(), json);
		gi = (GameInstance) JSONParser.getInstance().toObject(json, GameInstance.class);
		return gi;
	}

	public List<GameInstance> findAllAvailableGameInstance() {
        String json = get("/gameinstance/list/open");
        ObjectMapper mapper = new ObjectMapper();
        List<GameInstance> myObjects = null;
		try {
			myObjects = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, GameInstance.class));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return myObjects;
	}

	public GameInstance cancelGameInstance(GameInstance gi) {
		String json = JSONParser.getInstance().toJSONString(gi);
		json = post("/gameinstance/" + gi.getGi_uuid() + "/cancel", json);
		gi = (GameInstance) JSONParser.getInstance().toObject(json, GameInstance.class);
		return gi;
	}

	public List<ServerInstance> findAllGameServerInstanceOnline() {
        String json = get("/server/game/list/online");
        ObjectMapper mapper = new ObjectMapper();
        List<ServerInstance> myObjects = null;
		try {
			myObjects = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, ServerInstance.class));
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myObjects;
	}

	public List<ServerInstance> findAllServerInstanceOnline() {
        String json = get("/server/list/online");
        ObjectMapper mapper = new ObjectMapper();
        List<ServerInstance> myObjects = null;
		try {
			myObjects = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, ServerInstance.class));
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myObjects;
	}

	public GameWorld addGameWorld(GameWorld gw) {
		String json = JSONParser.getInstance().toJSONString(gw);
		json = post("/world", json);
		gw = (GameWorld) JSONParser.getInstance().toObject(json, GameWorld.class);
		return gw;
	}

	public GameWorld findGameWorldByName(String name) {
		GameWorld gw = null;
		String json = "{}";
		json = get("/world/search/" + name);
		gw = (GameWorld)JSONParser.getInstance().toObject(json, GameWorld.class);
		return gw;
	}

	public MineCraftPlayer findPlayerByName(String name) {
		MineCraftPlayer mcp = null;
		String json = "{}";
		json = get("/player/search/" + name);
		mcp = (MineCraftPlayer)JSONParser.getInstance().toObject(json, MineCraftPlayer.class);
		return mcp;
	}

	public MineCraftPlayer createPlayer(MineCraftPlayer player) {
		String json = JSONParser.getInstance().toJSONString(player);
		json = post("/player", json);
		player = (MineCraftPlayer) JSONParser.getInstance().toObject(json, MineCraftPlayer.class);
		return player;
	}

	public ServerInstance findServerByHostName(String hostname) {
		ServerInstance server = null;
		String json = "{}";
		json = get("/server/search/byhost/" + hostname);
		server = (ServerInstance)JSONParser.getInstance().toObject(json, ServerInstance.class);
		return server;
	}

	public MineCraftPlayer updatePlayer(MineCraftPlayer player) {
		String json = JSONParser.getInstance().toJSONString(player);
		json = post("/player/" + player.getMcp_uuid().toString(), json);
		player = (MineCraftPlayer) JSONParser.getInstance().toObject(json, MineCraftPlayer.class);
		return player;
	}

	public Game findGameByName(String gameName) {
		Game game = null;
		String json = "{}";
		json = get("/game/search/" + gameName);
		game = (Game)JSONParser.getInstance().toObject(json, Game.class);
		return game;
	}

	public GameQueue joinGameQueue(MineCraftPlayer mcp, Game game) {
		GameQueue gq = new GameQueue();
		gq.setGame(game);
		gq.setPlayer(mcp);
		
		String json = JSONParser.getInstance().toJSONString(gq);
		json = post("/gamequeue/", json);
		gq = (GameQueue) JSONParser.getInstance().toObject(json, GameQueue.class);
		return gq;
	}

	public List<GameQueue> findAllGameQueueByStatus(GameQueueStatus status) {
        String json = get("/gamequeue/list/status/" + status.name() );
        ObjectMapper mapper = new ObjectMapper();
        List<GameQueue> myObjects = null;
		try {
			myObjects = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, GameQueue.class));
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myObjects;
	}

	public List<GameInstance> findGameInstanceAvailable(Game game) {
        String json = get("/gameinstance/search/" + game.getName() );
        ObjectMapper mapper = new ObjectMapper();
        List<GameInstance> myObjects = null;
		try {
			myObjects = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, GameInstance.class));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return myObjects;
	}

	public List<Arena> findAllArenas() {
        String json = get("/arena/list");
        ObjectMapper mapper = new ObjectMapper();
        List<Arena> myObjects = null;
		try {
			myObjects = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, Arena.class));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return myObjects;
	}

	public void removePlayerFromGameQueue(GameQueue gq) {
		delete("/gamequeue/" + gq.getGame_queue_uuid().toString());
	}
	
	public void removePlayerFromGameQueue(MineCraftPlayer player) {
		delete("/gamequeue/player/" + player.getPlayer_uuid().toString() );
	}

	public List<Arena> findArenasByGame(Game game) {
		String url = this.gameManagerUrl + "/game/" + game.getGame_uuid().toString() + "/arenas";
		ClientRequest client = new ClientRequest(url);
		ClientResponse response = null;
		try {
			response = client.get(String.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        ObjectMapper mapper = new ObjectMapper();
        String json = (String)response.getEntity(String.class);
        List<Arena> myObjects = null;
		try {
			myObjects = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, Arena.class));
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
        return myObjects;
	}

	public ServerInstance findLobbyAvailable() {
		ServerInstance server = this.findServerByName("mglobby");
		
		return server;
	}

	public void addGameConfigToGame(Game game, GameConfig gc) {
		String guuid = game.getGame_uuid().toString();
		String gcuuid = gc.getGame_config_uuid().toString();
		
		post("/game/" + guuid + "/config/add/" + gcuuid );
	}

	public List<GameConfig> findGameConfigByGame(Game game) {
		String url = this.gameManagerUrl + "/game/" + game.getGame_uuid().toString() + "/config/list";
		ClientRequest client = new ClientRequest(url);
		ClientResponse response = null;
		try {
			response = client.get(String.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        ObjectMapper mapper = new ObjectMapper();
        String json = (String)response.getEntity(String.class);
        List<GameConfig> myObjects = null;
		try {
			myObjects = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, GameConfig.class));
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
        return myObjects;
	}

	public Boolean sendPlayerToLobby(String name) {

		Boolean result = post("/admin/sendtolobby/" + name);
		return result;
	}

	public List<AdminQueue> findAdminQueueRequestsOpened() {
		String url = this.gameManagerUrl + "/admin/queue/open/list";
		ClientRequest client = new ClientRequest(url);
		ClientResponse response = null;
		try {
			response = client.get(String.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        ObjectMapper mapper = new ObjectMapper();
        String json = (String)response.getEntity(String.class);
        List<AdminQueue> myObjects = null;
		try {
			myObjects = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, AdminQueue.class));
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
        return myObjects;
	}

	public Boolean markAdminRequestCompleted(AdminQueue aq) {
		String json = JSONParser.getInstance().toJSONString(aq);
		Boolean result = post("/admin/queue/markcomplete/" + aq.getUuid().toString());
		return result;
	}

	public List<MineCraftPlayer> findPlayersNotInLobby() {
		String url = this.gameManagerUrl + "/player/notinlobby/list";
		ClientRequest client = new ClientRequest(url);
		ClientResponse response = null;
		try {
			response = client.get(String.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        ObjectMapper mapper = new ObjectMapper();
        String json = (String)response.getEntity(String.class);
        List<MineCraftPlayer> myObjects = null;
		try {
			myObjects = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, MineCraftPlayer.class));
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
        return myObjects;
	}

	public String lockGameQueue() {
		String result = post("/gamequeue/lock", "{test:name}" );
		return result;
	}

	public GameQueue completeGameQueueRequest(GameQueue gq) {
		String json = JSONParser.getInstance().toJSONString(gq);
		json = post("/gamequeue/" + gq.getGame_queue_uuid().toString() + "/complete", json);
		gq = (GameQueue) JSONParser.getInstance().toObject(json, GameQueue.class);
		return gq;
	}
	
}

