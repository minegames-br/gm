package br.com.minegames.gamemanager.client;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;

import br.com.minegames.core.domain.ServerInstance;
import br.com.minegames.core.json.JSONParser;

public class GameManagerDelegate {

	
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
	
	private static String post(String path, String jsonString) {
		ClientConfig clientConfig = new ClientConfig();
        clientConfig.connectorProvider(new ApacheConnectorProvider());

		String restURL = "http://localhost:8080/gamemanager/webresources";
		//String restURL = "http://jamine-bot.mybluemix.net/webresources";
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(restURL);

        Entity<String> entity = Entity.entity(jsonString, MediaType.APPLICATION_JSON);
        Response response = target.path(path).request().post(entity);
        return response.readEntity(String.class);
		
	}

}
