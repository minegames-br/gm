package com.thecraftcloud.manager;

import java.util.logging.Logger;

import org.quartz.Job;

import com.thecraftcloud.client.TheCraftCloudDelegate;

public abstract class ManagerJob implements Job {
	
	protected TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance("http://services.thecraftcloud.com:8080/gamemanager/webresources");
	protected Logger logger = Logger.getLogger("file");
	

}
