package com.thecraftcloud.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.thecraftcloud.core.domain.ServerInstance;

public class DeleteServerRESTTest {

	public static void main(String args[]) {
		String restURL = "http://localhost:8080/gamemanager/webresources";
		//String restURL = "http://jamine-bot.mybluemix.net/webresources";
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(restURL);
		ServerInstance domain = new ServerInstance();
		
		try{
			Response response = target.path("/server/a8b8d361-5640-4a59-bbfe-b78996ab5568").request().get();
			System.out.println("http code: " + response.getStatus() );
			if(response.getStatus() == 200) {
				System.out.println("json: " + response.readEntity(String.class)  );
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
