package com.thecraftcloud.manager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.thecraftcloud.admin.socket.client.AdminClient;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.domain.AdminQueue;
import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.domain.RequestStatus;
import com.thecraftcloud.core.json.JSONParser;

public class AdminQueueJob implements Job {

	private TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance("http://services.thecraftcloud.com:8080/gamemanager/webresources");
	private Logger logger = Logger.getLogger("file");
	
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info((char)27 + "[32m" + "Running AdminQueueJob Job."+ "[0m");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		List<AdminQueue> list = delegate.findAdminQueueRequestsOpened();
		logger.info((char)27 + "[32m" + "size: " + list.size() + (char)27 + "[0m");
		for(AdminQueue aq: list) {
			aq.setResponseTime(Calendar.getInstance());
			aq.setStatus(RequestStatus.CLOSED);
			
			ActionDTO action = (ActionDTO)JSONParser.getInstance().toObject(aq.getAction(), ActionDTO.class);
			AdminClient client = AdminClient.getInstance();
			
			logger.info("action: " + action );
			logger.info("server: " + action.getServer() );
			logger.info("action: " + action.getName() );
			logger.info("player: " + action.getPlayer() );
			
			MineCraftPlayer mcp = delegate.findPlayerByName(action.getPlayer().getName());
			
			for(int i = 0; i< 10; i++) {
				ResponseDTO rdto = client.execute(mcp.getServer(), action);
				if(rdto.getResult()) {
					String s = ( (char)27 + "[32m" + "player: " + mcp.getName() + " enviado para o server na tentativa: " + i  + (char)27 + "[0m"); 
					System.out.println(s + rdto.getMessage() + " \n " + rdto.getJson() );
					break;
				} else {
					String s = ( (char)27 + "[32m" + "nao conseguiu enviar o player: " + mcp.getName() + " ... tentando novamente... " + i  + (char)27 + "[0m"); 
					System.out.println(s + rdto.getMessage() + " \n " + rdto.getJson() );
				}
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			delegate.markAdminRequestCompleted(aq);
		}
		logger.info((char)27 + "[32m" + "Completing AdminQueueJob Job." + (char)27 + "[0m");
	}

}
