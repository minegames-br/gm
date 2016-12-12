package com.thecraftcloud.manager;

import java.util.logging.Logger;

public class ManagerMain {

	private static Logger logger = Logger.getGlobal();
	
	   public static void main(String []args) throws Exception  {
		   logger.info("**********************************************************");
		   logger.info("STARTING THE CRAFT CLOUD MANAGER");
		   logger.info("**********************************************************");
		   MonitorAgent agent = new MonitorAgent();
		   agent.run();
		   
	   }
	   
	}