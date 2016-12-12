package com.thecraftcloud.manager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.thecraftcloud.admin.socket.client.AdminClient;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.domain.AdminQueue;
import com.thecraftcloud.core.domain.RequestStatus;
import com.thecraftcloud.core.json.JSONParser;

public class AdminQueueJob implements Job {

	private TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance("http://services.thecraftcloud.com:8080/gamemanager/webresources");

	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.err.println("Running AdminQueueJob Job.");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		List<AdminQueue> list = delegate.findAdminQueueRequestsOpened();
		for(AdminQueue aq: list) {
			aq.setResponseTime(Calendar.getInstance());
			aq.setStatus(RequestStatus.CLOSED);
			
			ActionDTO action = (ActionDTO)JSONParser.getInstance().toObject(aq.getAction(), ActionDTO.class);
			AdminClient client = AdminClient.getInstance();
			client.execute(action.getServer(), action);
			
			delegate.markAdminRequestCompleted(aq);
		}
		System.err.println("Completing AdminQueueJob Job.");
	}

}
