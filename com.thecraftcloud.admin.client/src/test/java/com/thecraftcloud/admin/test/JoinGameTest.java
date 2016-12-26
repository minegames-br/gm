package com.thecraftcloud.admin.test;

import org.junit.Test;

import com.thecraftcloud.admin.socket.client.AdminClient;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.domain.ServerInstance;

public class JoinGameTest {
/*
	//String restURL = "";
    private static TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
    
	@Test
	public void test() {
		ActionDTO dto = new ActionDTO();
		dto.setName(ActionDTO.JOIN_GAME);
		
		MineCraftPlayer player = delegate.findPlayerByName("_KingCraft");
		dto.setPlayer(player);
		ServerInstance server = new ServerInstance();
		server.setIp_address("localhost");
		server.setAdminPort(65000);
		
		try{
			AdminClient client = AdminClient.getInstance();
			ResponseDTO responseDTO = client.execute(server, dto); 
			System.out.println( server.getName() + " - " + responseDTO.getMessage() + " " + responseDTO.getResult() );
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		player = delegate.findPlayerByName("_WolfGamer");
		dto.setPlayer(player);
		try{
			AdminClient client = AdminClient.getInstance();
			ResponseDTO responseDTO = client.execute(server, dto); 
			System.out.println( server.getName() + " - " + responseDTO.getMessage() + " " + responseDTO.getResult() );
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
*/
}
