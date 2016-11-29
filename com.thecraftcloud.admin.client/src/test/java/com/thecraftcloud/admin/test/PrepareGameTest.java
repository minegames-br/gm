package com.thecraftcloud.admin.test;

import org.junit.Test;

import com.thecraftcloud.admin.socket.client.AdminClient;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.ServerInstance;

public class PrepareGameTest {

	public static final String URL_SERVICES = "http://services.thecraftcloud.com:8080/gamemanager/webresources";
	//public static final String URL_SERVICES = "http://localhost:8080/gamemanager/webresources";
	//String restURL = "";
    private static TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
    
	@Test
	public void test() {
		ActionDTO dto = new ActionDTO();
		dto.setName(ActionDTO.PREPARE_GAME);
		
		Game game = delegate.findGame("d10e8c62-6124-4952-a054-c7c668e7944f");
		Arena arena = delegate.findArena("30d00221-b371-4828-a0e6-5d75de7bfaec");
		
		dto.setGame(game);
		dto.setArena(arena);
		
		try{
			ServerInstance server = new ServerInstance();
			server.setIp_address("localhost");
			server.setAdminPort(65000);
			AdminClient client = new AdminClient();
			ResponseDTO responseDTO = client.execute(server, dto); 
			System.out.println( server.getName() + " - " + responseDTO.getMessage() + " " + responseDTO.getResult() );
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
