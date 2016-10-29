package br.com.minegames.gamemanager.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.minegames.core.domain.GameInstance;
import br.com.minegames.gamemanager.client.delegate.GameDelegate;

public class GameInstanceRESTTest {

	public static void main(String args[]) {

        String jsonString;
        GameInstance domain = GameDelegate.getInstance().joinGame("arena2");
        
        /*
        Game game = new Game();
        domain.setGame(game);
        game.setDescription("arqueiro");
        game.setName("arqueiro");
        game.setDescription("arqueiro");
        
        domain.setGi_uuid(null);
        domain.getArena().setArena_uuid(null);
        
		jsonString = JSONParser.getInstance().toJSONString(game);
		String jsonGame = post("/game", jsonString);
		game = (Game)JSONParser.getInstance().toObject(jsonGame,Game.class);
		domain.setGame(game);
        
        domain.getServer().setServer_uuid(null);
        ServerInstance server = domain.getServer();
		jsonString = JSONParser.getInstance().toJSONString(server);
		String jsonServer = post("/server", jsonString);
		server = (ServerInstance)JSONParser.getInstance().toObject(jsonServer,ServerInstance.class);
		domain.setServer(server);
		
        domain.getWorld().setWorld_uuid(null);
		//criar world
		GameWorld world = domain.getWorld();
		jsonString = JSONParser.getInstance().toJSONString(world);
		String jsonWorld = post("/world", jsonString);
		world = (GameWorld)JSONParser.getInstance().toObject(jsonWorld,  GameWorld.class);
		domain.setWorld(world);

        
        List<Area3D> areas = new ArrayList<Area3D>();
        for(Area3D area: domain.getArena().getAreas()) {
        	area.setArea_uuid(null);
    		
    		//criar locais
    		Local pointA = area.getPointA();
    		jsonString = JSONParser.getInstance().toJSONString(pointA);
    		String jsonPointA = post("/local", jsonString);
    		pointA = (Local)JSONParser.getInstance().toObject(jsonPointA,  Local.class);
    		area.setPointA(pointA);
    		
    		Local pointB = area.getPointB();
    		jsonString = JSONParser.getInstance().toJSONString(pointB);
    		String jsonPointB = post("/local", jsonString);
    		pointB = (Local)JSONParser.getInstance().toObject(jsonPointB,  Local.class);
    		area.setPointB(pointB);
    		
    		jsonString = JSONParser.getInstance().toJSONString(area);
    		String jsonArea = post("/area", jsonString);
    		Area3D areax = (Area3D)JSONParser.getInstance().toObject(jsonArea,  Area3D.class);
    		areas.add(areax);
        }
        domain.getArena().setAreas(areas);
        
		Arena arena = domain.getArena();
		jsonString = JSONParser.getInstance().toJSONString(arena);
		String jsonArena = post("/arena", jsonString);
		arena = (Arena)JSONParser.getInstance().toObject(jsonArena,  Arena.class);
		domain.setArena(arena);
        
        
        List<Config> configs = new ArrayList<Config>();
        for(Config config: domain.getConfigs()) {
        	config.setConfig_uuid(null);
    		jsonString = JSONParser.getInstance().toJSONString(config);
    		String jsonConfig = post("/config", jsonString);
    		Config c = (Config)JSONParser.getInstance().toObject(jsonConfig,  Config.class);
    		configs.add(c);
        }
        domain.setConfigs(configs);
        
		jsonString = JSONParser.getInstance().toJSONString(domain);
		String jsonGI = post("/gameinstance", jsonString);
		domain = (GameInstance)JSONParser.getInstance().toObject(jsonGI,  GameInstance.class);
		
		*/
	}
	
	public static String post(String path, String jsonString) {
		String restURL = "http://services.minegames.com.br:8080/gamemanager/webresources";
		//String restURL = "http://jamine-bot.mybluemix.net/webresources";
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(restURL);

        Entity<String> entity = Entity.entity(jsonString, MediaType.APPLICATION_JSON);
        Response response = target.path(path).request().post(entity);
        return response.readEntity(String.class);
		
	}
}
