package br.com.minegames.gamemanager.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.minegames.core.domain.Game;
import br.com.minegames.core.domain.GameInstance;
import br.com.minegames.core.domain.ServerInstance;
import br.com.minegames.core.json.JSONParser;

public class StartGameRESTPostTest {

	public static void main(String args[]) {
		String restURL = "http://localhost:8080/gamemanager/webresources";
		//String restURL = "http://jamine-bot.mybluemix.net/webresources";
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(restURL);

		String jsonString;
        Response response = target.path("/game/0eb36d46-8148-4332-8c59-c40ca1834f0d").request().get();
        jsonString = response.readEntity(String.class);
        Game game = (Game)JSONParser.getInstance().toObject(jsonString, Game.class);

        response = target.path("/server/a8b8d361-5640-4a59-bbfe-b78996ab5568").request().get();
        jsonString = response.readEntity(String.class);
        ServerInstance server = (ServerInstance)JSONParser.getInstance().toObject(jsonString, ServerInstance.class);

		GameInstance gameInstance = new GameInstance();
		gameInstance.setGame(game);
		gameInstance.setServer(server);
		jsonString = JSONParser.getInstance().toJSONString(gameInstance);
		
		System.out.println("game instance: " + jsonString);
		
        Entity<String> entity = Entity.entity(jsonString, MediaType.APPLICATION_JSON);
        response = target.path("/game/start").request().post(entity);
        System.out.println(response.readEntity(String.class));
	}
}
