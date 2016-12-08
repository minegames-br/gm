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
import com.thecraftcloud.core.logging.MGLogger;

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
			MGLogger.info("register server: " + json);

			server = (ServerInstance)JSONParser.getInstance().toObject(json, ServerInstance.class);
			MGLogger.info("Server: " + server.getServer_uuid());

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return server;
	}
	
	public ServerInstance updateServer(ServerInstance server) {
		String json = JSONParser.getInstance().toJSONString(server);
		json = post("/server/" + server.getServer_uuid().toString(), json);
		MGLogger.info("update server: " + json);
		server = (ServerInstance)JSONParser.getInstance().toObject(json, ServerInstance.class);
		MGLogger.info("Server: " + server.getServer_uuid());

		return server;
	}
	
	public ServerInstance createServer(ServerInstance server) {
		String json = JSONParser.getInstance().toJSONString(server);
		json = post("/server/", json);
		MGLogger.info("create server: " + json);
		server = (ServerInstance)JSONParser.getInstance().toObject(json, ServerInstance.class);
		MGLogger.info("Server: " + server.getServer_uuid());

		return server;
	}
	
	public Area3D addArea3D(Area3D area) {
		Area3D result = null;
		
		String json = JSONParser.getInstance().toJSONString(area);
		json = post("/area", json);
		MGLogger.info("area: " + json);
		result = (Area3D)JSONParser.getInstance().toObject(json, Area3D.class);
		
		return result;
	}
	
	public Local addLocal(Local local) {
		String json = JSONParser.getInstance().toJSONString(local);
		json = post("/local", json);
		MGLogger.info("addLocal: " + json);
		local = (Local) JSONParser.getInstance().toObject(json, Local.class);
		MGLogger.info("Local: " + local.getLocal_uuid().toString() );
		return local;
	}
	
	private String post(String path, String jsonString) {
		ClientRequest request = new ClientRequest(this.gameManagerUrl + path);
		System.out.println("post: " + this.gameManagerUrl + path + " --- " + jsonString);
		ClientResponse response = null;
		try {
			request.body("application/json", jsonString);
			response = request.post(String.class);
			System.out.println(response.getResponseStatus().getStatusCode());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result = (String)response.getEntity(String.class);
        return result;
		
	}
	
	private boolean post(String path) {
		ClientRequest request = new ClientRequest(this.gameManagerUrl + path);
		System.out.println("post: " + this.gameManagerUrl + path );
		ClientResponse response = null;
		try {
			request.body("application/json", "");
			response = request.post(String.class);
			System.out.println(response.getResponseStatus().getStatusCode());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private String delete(String path) {
		ClientRequest request = new ClientRequest(this.gameManagerUrl + path);
		System.out.println("delete: " + this.gameManagerUrl + path);
		ClientResponse response = null;
		try {
			response = request.delete(String.class);
			System.out.println(response.getResponseStatus().getStatusCode());
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
		System.out.println(this.gameManagerUrl + path);
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
		
		MGLogger.info("findServer: " + server_uuid);
		String json = get("/server", server_uuid );
		MGLogger.info("findServer: " + json);
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
		System.out.println("createGameArenaConfig: " + json);
		json = post("/gamearenaconfig", json);
		MGLogger.info("create game arena config: " + json);
		config = (GameArenaConfig) JSONParser.getInstance().toObject(json, GameArenaConfig.class);
		MGLogger.info("Arena: " + config.getGac_uuid().toString() );
		return config;
	}

	public GameArenaConfig updateGameArenaConfig(GameArenaConfig domain) {
		String json = JSONParser.getInstance().toJSONString(domain);
		System.out.println(json);
		json = post("/gamearenaconfig/" + domain.getGac_uuid().toString(), json);
		MGLogger.info("update game config instance: " + json);
		domain = (GameArenaConfig) JSONParser.getInstance().toObject(json, GameArenaConfig.class);
		return domain;
	}
	
	public Arena findArena(String arena_uuid) {
		Arena arena = null;
		
		MGLogger.info("findArena: " + arena_uuid);
		String json = get("/arena", arena_uuid );
		MGLogger.info("findArena: " + json);
		arena = (Arena)JSONParser.getInstance().toObject(json, Arena.class);
		
		return arena;
	}

	public Arena createArena(Arena arena) {
		String json = JSONParser.getInstance().toJSONString(arena);
		json = post("/arena", json);
		MGLogger.info("create arena: " + json);
		arena = (Arena) JSONParser.getInstance().toObject(json, Arena.class);
		MGLogger.info("Arena: " + arena.getArena_uuid().toString() );
		return arena;
	}
	
	public Game createGame(Game game ) {
		String json = JSONParser.getInstance().toJSONString(game);
		System.out.println(json);
		json = post("/game", json);
		MGLogger.info("create game: " + json);
		game = (Game) JSONParser.getInstance().toObject(json, Game.class);
		MGLogger.info("Game: " + game.getGame_uuid().toString() );
		return game;
	}

	public Game findGame(String uuid)  {
		Game domain = null;
		
		MGLogger.info("findGame: " + uuid);
		String json = get("/game", uuid );
		MGLogger.info("findGame: " + json);
		domain = (Game)JSONParser.getInstance().toObject(json, Game.class);
		
		return domain;
	}
	
	public Schematic createSchematic(Schematic domain) {
		String json = JSONParser.getInstance().toJSONString(domain);
		System.out.println("schematic json: " + json );
		json = post("/schematic", json);
		MGLogger.info("create schematic: " + json);
		domain = (Schematic) JSONParser.getInstance().toObject(json, Schematic.class);
		MGLogger.info("Schematic: " + domain.getSchematic_uuid().toString() );
		return domain;
	}


	public Schematic findSchematic(String uuid) {
		Schematic domain = null;
		
		MGLogger.info("findSchematic: " + uuid);
		String json = get("/schematic", uuid );
		MGLogger.info("findSchematic: " + json);
		domain = (Schematic)JSONParser.getInstance().toObject(json, Schematic.class);
		
		return domain;
	}

	public void uploadSchematic(Schematic schematic, File file) {
		ClientRequest request = new ClientRequest(this.gameManagerUrl + "/schematic/upload/" + schematic.getSchematic_uuid().toString() );
		System.out.println(this.gameManagerUrl + "/schematic/upload");
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
			System.out.println(response.getEntity(String.class));
			System.out.println(response.getResponseStatus().getStatusCode());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void uploadWorld(GameWorld gw, File file) {
		ClientRequest request = new ClientRequest(this.gameManagerUrl + "/world/" + gw.getWorld_uuid().toString() + "/upload");
		System.out.println(this.gameManagerUrl + "/world/" + gw.getWorld_uuid().toString() + "/upload");
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
			System.out.println(response.getEntity(String.class));
			System.out.println(response.getResponseStatus().getStatusCode());
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
		System.out.println("GameConfig json request: " + json );
		json = post("/game/config/add", json);
		MGLogger.info("create gameconfig response: " + json);
		domain = (GameConfig) JSONParser.getInstance().toObject(json, GameConfig.class);
		MGLogger.info("Game Config: " + domain.getGame_config_uuid().toString() );
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
        System.out.println("findAllGameConfigArenaByGameUUID request: " + uuid);
        String json = get("/game/" + uuid + "/gamearenaconfig/list");
        System.out.println("findAllGameConfigArenaByGameUUID response: " + json);
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
        System.out.println("findAllGameConfigInstanceByGameUUID request: " + uuid);
        String json = get("/game/" + uuid + "/gameconfiginstance/list");
        System.out.println("findAllGameConfigInstanceByGameUUID response: " + json);
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
		System.out.println("game config instance json: " + json );
		json = post("/game/config/instance/add", json);
		MGLogger.info("create gameconfig instance: " + json);
		domain = (GameConfigInstance) JSONParser.getInstance().toObject(json, GameConfigInstance.class);
		MGLogger.info("Game Config Instance : " + domain.toString() );
		return domain;
	}

	public GameConfigInstance updateGameConfigInstance(GameConfigInstance domain) {
		String json = JSONParser.getInstance().toJSONString(domain);
		System.out.println(json);
		json = post("/game/config/instance/" + domain.getGameConfig().getGame_config_uuid().toString(), json);
		MGLogger.info("update game config instance: " + json);
		domain = (GameConfigInstance) JSONParser.getInstance().toObject(json, GameConfigInstance.class);
		MGLogger.info("Game Config Instance: " + domain.getGameConfig().getGame_config_uuid().toString() );
		return domain;
	}
	
	public static void main(String args[]) throws InvalidRegistrationException {
		TheCraftCloudClientPlugin.setMinegamesGameManagerUrl("http://localhost:8080/gamemanager/webresources");
		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance("http://localhost:8080/gamemanager/webresources");
		Game game = delegate.findGame("46bea463-7bb9-46ed-8eae-ec004ce84833");
	}

	public Game updateGame(Game game) {
		String json = JSONParser.getInstance().toJSONString(game);
		System.out.println(json);
		json = post("/game/" + game.getGame_uuid(), json);
		MGLogger.info("update game: " + json);
		game = (Game) JSONParser.getInstance().toObject(json, Game.class);
		MGLogger.info("Game: " + game.getGame_uuid().toString() );
		return game;
	}

	public Arena addArenaArea3D(Arena arena, Area3D area) {
		String json = JSONParser.getInstance().toJSONString(arena);
		arena.getAreas().add(area);
		json = post("/arena", json);
		MGLogger.info("update arena: " + json);
		arena = (Arena) JSONParser.getInstance().toObject(json, Arena.class);
		MGLogger.info("Arena: " + arena.getArena_uuid().toString() );
		return arena;
	}

	public GameConfig findGameConfig(String uuid, String gameConfigUuid) {
		GameConfig domain = null;
		
		MGLogger.info("findGameConfig request: " + uuid);
		String json = get("/game/" + uuid + "/config", gameConfigUuid);
		MGLogger.info("findGameConfig response: " + json);
		domain = (GameConfig)JSONParser.getInstance().toObject(json, GameConfig.class);
		
		return domain;
	}

	public Area3D findArea3D(String uuid) {
		Area3D domain = null;
		
		MGLogger.info("findArea3D request: " + uuid);
		String json = get("/area", uuid);
		MGLogger.info("findArea3D response: " + json);
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
	        System.out.println("Download to: " + file.getAbsolutePath() );
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
	        System.out.println("Download to: " + file.getAbsolutePath() );
	        FileUtils.copyURLToFile(website, file);
	    } catch ( Exception ex) {
	    	ex.printStackTrace();
	    }  
        return file;
	}

	public List<GameArenaConfig> findAllGameConfigArenaByGameArena(String gameUuid, String arenaUuid) {
        System.out.println("findAllGameConfigArenaByGameArena request: " + gameUuid);
        String json = get("/game/" + gameUuid + "/gamearenaconfig/" + arenaUuid);
        System.out.println("findAllGameConfigArenaByGameArena response: " + json);
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
		System.out.println(json);
		json = post("/arena/" + arena.getArena_uuid(), json);
		MGLogger.info("update arena: " + json);
		arena = (Arena) JSONParser.getInstance().toObject(json, Arena.class);
		MGLogger.info("Arena: " + arena.getArena_uuid().toString() );
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
		MGLogger.info("addItem: " + json);
		item = (Item) JSONParser.getInstance().toObject(json, Item.class);
		MGLogger.info("Item: " + item.getItem_uuid().toString() );
		return item;
	}

	public Kit addKit(Kit kit) {
		String json = JSONParser.getInstance().toJSONString(kit);
		json = post("/kit", json);
		MGLogger.info("addkit: " + json);
		kit = (Kit) JSONParser.getInstance().toObject(json, Kit.class);
		MGLogger.info("Item: " + kit.getKit_uuid().toString() );
		return kit;
	}

	public Item findItemByName(String name) {
		Item item = null;
		String json = "{}";
		json = get("/item/search/" + name);
		System.out.println(json);
		item = (Item)JSONParser.getInstance().toObject(json, Item.class);
		if( item == null) {
			System.err.println("Não encontrou item: " + name);
		}
		return item;
	}

	public GameConfig findGameConfigByName(String name) {
		GameConfig gc = null;
		String json = "{}";
		json = get("/gameconfig/search/" + name);
		System.out.println(json);
		gc = (GameConfig)JSONParser.getInstance().toObject(json, GameConfig.class);
		if( gc == null) {
			System.err.println("Não encontrou GameConfig: " + name);
		}
		return gc;
	}

	public ServerInstance findServerByName(String name) {
		ServerInstance server = null;
		String json = "{}";
		json = get("/server/search/" + name);
		System.out.println(json);
		server = (ServerInstance)JSONParser.getInstance().toObject(json, ServerInstance.class);
		if( server == null) {
			System.err.println("Não encontrou Server: " + name);
		}
		return server;
	}

	public List<ServerInstance> findAllServerInstance() {
        System.out.println("findAllServerInstance");
        String json = get("/server/list");
        System.out.println("findAllServerInstance response: " + json);
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
		MGLogger.info("create game instance: " + json);
		gi = (GameInstance)JSONParser.getInstance().toObject(json, GameInstance.class);
		MGLogger.info("GameInstance: " + gi.getGi_uuid() );

		return gi;
	}

	public GameInstance findGameInstanceByUUID(String uuid) {
		GameInstance domain = null;
		
		MGLogger.info("findGameInstanceByUUID request: " + uuid);
		String json = get("/gameinstance", uuid);
		MGLogger.info("findGameInstanceByUUID response: " + json);
		domain = (GameInstance)JSONParser.getInstance().toObject(json, GameInstance.class);
		
		return domain;
	}

	public GameInstance updateGameInstance(GameInstance gi) {
		String json = JSONParser.getInstance().toJSONString(gi);
		System.out.println(json);
		json = post("/gameinstance/" + gi.getGi_uuid(), json);
		MGLogger.info("update gameinstance: " + json);
		gi = (GameInstance) JSONParser.getInstance().toObject(json, GameInstance.class);
		MGLogger.info("GameInstance: " + gi.getGi_uuid().toString() );
		return gi;
	}

	public List<GameInstance> findAllAvailableGameInstance() {
        System.out.println("findAllAvailableGameInstance request");
        String json = get("/gameinstance/list/open");
        System.out.println("findAllAvailableGameInstance response: " + json);
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
		System.out.println(json);
		json = post("/gameinstance/" + gi.getGi_uuid() + "/cancel", json);
		MGLogger.info("cancel gameinstance: " + json);
		gi = (GameInstance) JSONParser.getInstance().toObject(json, GameInstance.class);
		MGLogger.info("GameInstance: " + gi.getGi_uuid().toString() );
		return gi;
	}

	public List<ServerInstance> findAllServerInstanceOnline() {
        System.out.println("findAllServerInstanceOnline");
        String json = get("/server/list/online");
        System.out.println("findAllServerInstanceOnline response: " + json);
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
		MGLogger.info("addgameworld: " + json);
		gw = (GameWorld) JSONParser.getInstance().toObject(json, GameWorld.class);
		MGLogger.info("GameWorld: " + gw.getWorld_uuid().toString() );
		return gw;
	}

	public GameWorld findGameWorldByName(String name) {
		GameWorld gw = null;
		String json = "{}";
		json = get("/world/search/" + name);
		System.out.println(json);
		gw = (GameWorld)JSONParser.getInstance().toObject(json, GameWorld.class);
		if( gw == null) {
			System.err.println("Não encontrou GameWorld: " + name);
		}
		return gw;
	}

	public MineCraftPlayer findPlayerByName(String name) {
		MineCraftPlayer mcp = null;
		String json = "{}";
		json = get("/player/search/" + name);
		System.out.println(json);
		mcp = (MineCraftPlayer)JSONParser.getInstance().toObject(json, MineCraftPlayer.class);
		if( mcp == null) {
			System.err.println("Não encontrou MineCraftPlayer: " + name);
		}
		return mcp;
	}

	public MineCraftPlayer createPlayer(MineCraftPlayer player) {
		String json = JSONParser.getInstance().toJSONString(player);
		json = post("/player", json);
		MGLogger.info("createPlayer: " + json);
		player = (MineCraftPlayer) JSONParser.getInstance().toObject(json, MineCraftPlayer.class);
		MGLogger.info("MineCraftPlayer: " + player.getMcp_uuid().toString() );
		return player;
	}

	public ServerInstance findServerByHostName(String hostname) {
		ServerInstance server = null;
		String json = "{}";
		json = get("/server/search/byhost/" + hostname);
		System.out.println(json);
		server = (ServerInstance)JSONParser.getInstance().toObject(json, ServerInstance.class);
		if( server == null) {
			System.err.println("Não encontrou ServerInstance: " + hostname);
		}
		return server;
	}

	public MineCraftPlayer updatePlayer(MineCraftPlayer player) {
		String json = JSONParser.getInstance().toJSONString(player);
		json = post("/player/" + player.getMcp_uuid().toString(), json);
		MGLogger.info("updatePlayer: " + json);
		player = (MineCraftPlayer) JSONParser.getInstance().toObject(json, MineCraftPlayer.class);
		MGLogger.info("MineCraftPlayer: " + player.getMcp_uuid().toString() );
		return player;
	}

	public Game findGameByName(String gameName) {
		Game game = null;
		String json = "{}";
		json = get("/game/search/" + gameName);
		System.out.println(json);
		game = (Game)JSONParser.getInstance().toObject(json, Game.class);
		if( game == null) {
			System.err.println("Não encontrou Game: " + gameName);
		}
		return game;
	}

	public GameQueue joinGameQueue(MineCraftPlayer mcp, Game game) {
		GameQueue gq = new GameQueue();
		gq.setGame(game);
		gq.setPlayer(mcp);
		
		String json = JSONParser.getInstance().toJSONString(gq);
		json = post("/gamequeue/", json);
		MGLogger.info("joinGameQueue: " + json);
		gq = (GameQueue) JSONParser.getInstance().toObject(json, GameQueue.class);
		MGLogger.info("GameQueue: " + gq.getGame_queue_uuid().toString() );
		return gq;
	}

	public List<GameQueue> findAllGameQueueByStatus(GameQueueStatus status) {
        System.out.println("findAllGameQueueByStatus");
        String json = get("/gamequeue/list/status/" + status.name() );
        System.out.println("findAllGameQueueByStatus response: " + json);
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
       System.out.println("findGameInstanceAvailable");
        String json = get("/gameinstance/search/" + game.getName() );
        System.out.println("findGameInstanceAvailable response: " + json);
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
        System.out.println("findAllArenas");
        String json = get("/arena/list");
        System.out.println("findAllArenas response: " + json);
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
		System.out.println( url );
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
	
}

/*	public static void main(String args[]) {
GameManagerClientPlugin.setMinegamesGameManagerUrl("http://localhost:8080/gamemanager/webresources");
GameManagerDelegate delegate = GameManagerDelegate.getInstance("http://localhost:8080/gamemanager/webresources");

Arena arena = new Arena();
arena.setArena_uuid(UUID.fromString("fad5f5fc-e770-4049-bfa2-3ef9616a8ecf"));
arena.setDescription("arena1");
arena.setDescription("the last archer arena pokemon go");

File file = new File("d:/minecraft/worlds/schematics/s2.schematic");
Schematic schematic = new Schematic();
schematic.setName("arena1-schematic");
schematic.setDescription("the last archer arena pokemon go");

System.out.println("create schematic");
schematic = delegate.createSchematic(schematic);

System.out.println("upload schematic");
delegate.uploadSchematic(schematic, file);

Game game = delegate.findGame("46bea463-7bb9-46ed-8eae-ec004ce84833");

GameArenaConfig config = new GameArenaConfig();
config.setArena(arena);
config.setGame(game);

delegate.createGameArenaConfig(config);

arena.setSchematic(schematic);
System.out.println("create arena");
delegate.createArena(arena);

List<Arena> arenas = delegate.findArenas("");
for(Arena a: arenas) {
	System.out.println(a.getName());
}

Game game = delegate.findGame("46bea463-7bb9-46ed-8eae-ec004ce84833");
System.out.println(game.getName());

List<Game> games = delegate.findGames();
for(Game g: games) {
	System.out.println(g.getName() + ":" + g.getGame_uuid().toString());
}
}
*/
