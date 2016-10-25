package br.com.minegames.core.slack;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;

public class Main {

	public static void main(String[] args) {
		ClientConfig clientConfig = new ClientConfig();
        clientConfig.connectorProvider(new ApacheConnectorProvider());

		String restURL = "https://hooks.slack.com/services/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";
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(restURL);
		
        String jsonString = "{\"text\": \"Fala aí Renato\"}";
        Entity<String> entity = Entity.entity(jsonString, MediaType.APPLICATION_JSON);
        Response response = target.path("").request().post(entity);
        System.out.println(response.readEntity(String.class));
	}

}
