package com.thecraftcloud.admin.test;

import org.junit.Test;

import com.thecraftcloud.admin.socket.client.AdminClient;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.domain.ServerInstance;

public class GetGameTest {

	@Test
	public void test() {
		ActionDTO dto = new ActionDTO();
		dto.setName(ActionDTO.GET_GAME);
	
		try{
			ServerInstance server = new ServerInstance();
			server.setIp_address("localhost");
			server.setAdminPort(65000);
			AdminClient client = AdminClient.getInstance();
			ResponseDTO responseDTO = client.execute(server, dto); 
			System.out.println( server.getName() + " - " + responseDTO.getMessage() + " " + responseDTO.getResult() );
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
