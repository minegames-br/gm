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

public class PingServerJob extends ManagerJob {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.err.println("Running PingServerJob Job.");
		
		List<ServerInstance> serverList = delegate.findAllServerInstanceOnline();
		
		for( ServerInstance server: serverList ) {
			
			if(server.getAdminPort() == null || server.getIp_address() == null) {
				continue;
			}
			
			ActionDTO dto = new ActionDTO();
			dto.setName(ActionDTO.PING_SERVER);
		
			try{
				AdminClient client = AdminClient.getInstance();
				ResponseDTO responseDTO = client.execute(server, dto);
				if(responseDTO == null) {
					markServerOffline(server);
				}else {
					markServerOnline(server);
				}
			}catch(Exception e) {
				markServerOffline(server);
				e.printStackTrace();
			}
		}
		System.err.println("Ending PingServerJob Job.");
	}

	private void markServerOnline(ServerInstance server) {
		if(server.getStatus().equals(ServerStatus.OFFLINE)) {
			server.setStatus(ServerStatus.ONLINE);
			delegate.updateServer(server);		
		}
	}

	private void markServerOffline(ServerInstance server) {
		server.setStatus(ServerStatus.OFFLINE);
		delegate.updateServer(server);
	}

}
