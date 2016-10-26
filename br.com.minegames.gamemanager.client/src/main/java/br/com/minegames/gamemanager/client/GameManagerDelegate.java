package br.com.minegames.gamemanager.client;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
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

import br.com.minegames.core.domain.Area3D;
import br.com.minegames.core.domain.Arena;
import br.com.minegames.core.domain.Game;
import br.com.minegames.core.domain.GameArenaConfig;
import br.com.minegames.core.domain.GameWorld;
import br.com.minegames.core.domain.Local;
import br.com.minegames.core.domain.Schematic;
import br.com.minegames.core.domain.ServerInstance;
import br.com.minegames.core.dto.SearchGameWorldDTO;
import br.com.minegames.core.json.JSONParser;
import br.com.minegames.core.logging.MGLogger;

public class GameManagerDelegate {

	private static GameManagerDelegate me;
	private String gameManagerUrl;
	
	private GameManagerDelegate() {
		
	}
	
	public static GameManagerDelegate getInstance() {
		String gamemanagerUrl = GameManagerClientPlugin.getMinegamesGameManagerUrl();
		//Bukkit.getLogger().info("URL: "+ gamemanagerUrl);
		if(me == null) {
			me = new GameManagerDelegate();
			me.gameManagerUrl = gamemanagerUrl;
		}
		return me;
	}
	
	public static GameManagerDelegate getInstance(String url) {
		if(me == null) {
			me = new GameManagerDelegate();
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
	
	public Arena addArea3D(Arena arena, Area3D area) {
		Area3D result = null;
		
		arena.getAreas().add(area);
		String json = JSONParser.getInstance().toJSONString(arena);
		json = post("/arena", json);
		MGLogger.info("arena: " + json);
		arena = (Arena)JSONParser.getInstance().toObject(json, Arena.class);
		
		return arena;
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
	
	public static void main(String args[]) {
		GameManagerClientPlugin.setMinegamesGameManagerUrl("http://localhost:8080/gamemanager/webresources");
		GameManagerDelegate delegate = GameManagerDelegate.getInstance("http://localhost:8080/gamemanager/webresources");
		
		/*
		List<Arena> arenas = delegate.findArenas("");
		for(Arena a: arenas) {
			System.out.println(a.getArena_uuid());
		}
		
		*/
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
/*
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
		*/
	}

	public GameArenaConfig createGameArenaConfig(GameArenaConfig config) {
		String json = JSONParser.getInstance().toJSONString(config);
		json = post("/gamearenaconfig", json);
		MGLogger.info("create game arena config: " + json);
		config = (GameArenaConfig) JSONParser.getInstance().toObject(json, GameArenaConfig.class);
		MGLogger.info("Arena: " + config.getGa_config_uuid().toString() );
		return config;
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
		json = post("/game", json);
		MGLogger.info("create game: " + json);
		game = (Game) JSONParser.getInstance().toObject(json, Game.class);
		MGLogger.info("Game: " + game.getGame_uuid().toString() );
		return game;
	}

	public Game findGame(String uuid) {
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
	
}
