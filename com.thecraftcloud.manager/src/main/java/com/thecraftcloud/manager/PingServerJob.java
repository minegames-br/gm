package com.thecraftcloud.manager;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.thecraftcloud.admin.socket.client.AdminClient;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.domain.ServerStatus;

public class PingServerJob implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.err.println("Running PingServerJob Job.");
		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance("http://services.thecraftcloud.com:8080/gamemanager/webresources");
		
		//System.err.println("Delegate criado.");
		
		List<ServerInstance> serverList = delegate.findAllServerInstanceOnline();
		
		//System.err.println("Chamou metodo findAllServerInstance.");
		
		System.err.println( "serverList " + serverList );
		for( ServerInstance server: serverList ) {
			
			//System.err.println("server: " + server.getName() + " " + server.getIp_address() + ":" + server.getAdminPort() );
			if(server.getAdminPort() == null || server.getIp_address() == null) {
				//System.out.println("server: " + server.getName() + " " + server.getIp_address() + " is not being managed.");
				continue;
			}
			
			ActionDTO dto = new ActionDTO();
			dto.setName(ActionDTO.PING_SERVER);
		
			try{
				AdminClient client = AdminClient.getInstance();
				ResponseDTO responseDTO = client.execute(server, dto); 
				System.out.println( server.getName() + " - " + responseDTO.getMessage() + " " + responseDTO.getResult() );
			}catch(Exception e) {
				
				server.setStatus(ServerStatus.OFFLINE);
				delegate.updateServer(server);
				
				e.printStackTrace();
			}
		}
		System.err.println("Ending PingServerJob Job.");
	}

}
