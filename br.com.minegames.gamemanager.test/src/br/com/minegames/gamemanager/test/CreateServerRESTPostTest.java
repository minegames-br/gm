package br.com.minegames.gamemanager.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.minegames.core.domain.ServerInstance;
import br.com.minegames.core.json.JSONParser;

public class CreateServerRESTPostTest {

	public static void main(String args[]) {
		String restURL = "http://localhost:8080/gamemanager/webresources";
		//String restURL = "http://jamine-bot.mybluemix.net/webresources";
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(restURL);
		ServerInstance domain = new ServerInstance();
		domain.setDescription("server testando rest post");
		domain.setName("ServerName Test" );
		domain.setHostname("play.minegames.com.br");
		domain.setIp_address("52.67.109.214");
        String jsonString;
		jsonString = JSONParser.getInstance().toJSONString(domain);
        Entity<String> entity = Entity.entity(jsonString, MediaType.APPLICATION_JSON);
        Response response = target.path("/server").request().post(entity);
        System.out.println(response.readEntity(String.class));
	}
}
