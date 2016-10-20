package br.com.minegames.gamemanager.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.minegames.core.domain.GamePlayer;
import br.com.minegames.core.json.JSONParser;

public class PlayerRESTTest {

	public static void main(String args[]) {
		String restURL = "http://localhost:8080/gamemanager/webresources";
		//String restURL = "http://jamine-bot.mybluemix.net/webresources";
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(restURL);
		
		GamePlayer domain = new GamePlayer();
		domain.setEmail("joaoemilio@gmail.com");
		domain.setName("João Emilio");
		domain.setNickName("FoxGamer129");
		domain.setPlayer_uuid("88165e7fa83a49f89f755cb77c23f8d3");

        String jsonString;
		jsonString = JSONParser.getInstance().toJSONString(domain);
        Entity<String> entity = Entity.entity(jsonString, MediaType.APPLICATION_JSON);
        Response response = target.path("/player").request().post(entity);
        System.out.println(response.readEntity(String.class));
	}
}
