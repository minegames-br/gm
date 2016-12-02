package com.thecraftcloud.rest;

import org.jboss.logging.Logger;

public abstract class REST {
	
	protected Logger logger = Logger.getLogger(getClass());	

	protected void log(String message) {
		logger.info(message);
	}

}
