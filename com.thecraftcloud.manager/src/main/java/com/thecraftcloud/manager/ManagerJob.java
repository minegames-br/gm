package com.thecraftcloud.manager;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import org.quartz.Job;

import com.thecraftcloud.client.TheCraftCloudDelegate;

public abstract class ManagerJob implements Job {
	
	protected TheCraftCloudDelegate delegate;
	protected Logger logger = Logger.getLogger("file");
	
	public ManagerJob() {
		Properties props = new Properties();
		try {
			props.load(this.getClass().getResourceAsStream("/config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String URL = props.getProperty("url");
		delegate = TheCraftCloudDelegate.getInstance(URL);
	}
	
}
