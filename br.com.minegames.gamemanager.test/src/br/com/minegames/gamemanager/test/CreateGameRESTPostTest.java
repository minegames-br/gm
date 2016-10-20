package br.com.minegames.gamemanager.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.minegames.core.domain.Game;
import br.com.minegames.core.json.JSONParser;

public class CreateGameRESTPostTest {

	public static void main(String args[]) {
		String restURL = "http://localhost:8080/gamemanager/webresources";
		//String restURL = "http://jamine-bot.mybluemix.net/webresources";
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(restURL);

		Game game = new Game();
		game.setDescription("testando rest post");
		game.setName("Name Test" );
        String jsonString;
		jsonString = JSONParser.getInstance().toJSONString(game);
        Entity<String> entity = Entity.entity(jsonString, MediaType.APPLICATION_JSON);
        Response response = target.path("/game").request().post(entity);
        System.out.println(response.readEntity(String.class));
	}
}
