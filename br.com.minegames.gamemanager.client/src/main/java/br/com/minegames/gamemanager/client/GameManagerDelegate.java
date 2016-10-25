package br.com.minegames.gamemanager.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bukkit.Bukkit;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import br.com.minegames.core.domain.Area3D;
import br.com.minegames.core.domain.Arena;
import br.com.minegames.core.domain.GameWorld;
import br.com.minegames.core.domain.Local;
import br.com.minegames.core.domain.ServerInstance;
import br.com.minegames.core.dto.SearchGameWorldDTO;
import br.com.minegames.core.json.JSONParser;

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
	
	public Area3D addArea3D(Arena arena, Area3D area) {
		Area3D result = null;
		
		//criar pointA e pointB
		Local pointA = addLocal(area.getPointA());
		Local pointB = addLocal(area.getPointB());
		area.setPointA(pointA);
		area.setPointB(pointB);
		
		//criar area
		String json = JSONParser.getInstance().toJSONString(area);
		json = post("/area", json);
		area = (Area3D)JSONParser.getInstance().toObject(json, Area3D.class);
		
		
		return result;
	}
	
	public Local addLocal(Local local) {
		String json = JSONParser.getInstance().toJSONString(local);
		json = post("/local", json);
		local = (Local) JSONParser.getInstance().toObject(json, Local.class);
		return local;
	}
	
	private String post(String path, String jsonString) {
		ClientConfig clientConfig = new ClientConfig();
        clientConfig.connectorProvider(new ApacheConnectorProvider());

		String restURL = this.gameManagerUrl;
		//String restURL = "http://jamine-bot.mybluemix.net/webresources";
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(restURL);

        Entity<String> entity = Entity.entity(jsonString, MediaType.APPLICATION_JSON);
        Response response = target.path(path).request().post(entity);
        return response.readEntity(String.class);
		
	}
	
	private String get(String path, String uuid) {
		ClientConfig clientConfig = new ClientConfig();
        clientConfig.connectorProvider(new ApacheConnectorProvider());

		String restURL = this.gameManagerUrl + path + "/" + uuid;
		//String restURL = "http://jamine-bot.mybluemix.net/webresources";
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(restURL);

        Response response = target.path(path).request().get();
        return response.readEntity(String.class);
	}

	private String get(String path) {
		ClientConfig clientConfig = new ClientConfig();
        clientConfig.connectorProvider(new ApacheConnectorProvider());
        JacksonJsonProvider jacksonJsonProvider = 
        	    new JacksonJaxbJsonProvider().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        clientConfig.register(jacksonJsonProvider);
        	
		String restURL = this.gameManagerUrl;
		//String restURL = "http://jamine-bot.mybluemix.net/webresources";
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(restURL);

        Response response = target.path(path).request().get();
        return response.readEntity(String.class);
	}

	private List<Arena> getArenaList(String path) {
		ClientConfig clientConfig = new ClientConfig();
		String restURL = this.gameManagerUrl;
		Bukkit.getLogger().info("URL: " + restURL);
		//String restURL = "http://jamine-bot.mybluemix.net/webresources";
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(restURL).path(path);

        Response response = target.request().get();
        ObjectMapper mapper = new ObjectMapper();
        String json = response.readEntity(String.class);
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
	
	public static void main(String args[]) {
		GameManagerClientPlugin.setMinegamesGameManagerUrl("http://services.minegames.com.br:8080/gamemanager/webresources");
		List<Arena> arenas = GameManagerDelegate.getInstance().findArenas("");
		for(Arena arena: arenas) {
			System.out.println(arena.getName());
		}
	}

}
