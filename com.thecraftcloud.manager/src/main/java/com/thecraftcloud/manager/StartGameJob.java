package com.thecraftcloud.manager;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class StartGameJob implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.err.println("Running StartGameJob Job.");
		System.out.println("teste");
		System.err.println("Ending StartGameJob Job.");
	}

}
