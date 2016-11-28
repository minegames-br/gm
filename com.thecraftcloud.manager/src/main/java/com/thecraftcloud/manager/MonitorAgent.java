package com.thecraftcloud.manager;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public class MonitorAgent {

	public void run() throws SchedulerException {
		SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
		
		Scheduler scheduler = schedFact.getScheduler();
		scheduler.start();
		JobDetail job = newJob(StartGameJob.class).withIdentity("start-game-job", "group-1-min").build();;
		CronTrigger trigger = newTrigger().withIdentity("trigger-start-game-job", "thecraftcloud-admin").withSchedule(cronSchedule("1 * * * * ?")).build();
		scheduler.scheduleJob( job, trigger );
		
		job = newJob(PingServerJob.class).withIdentity("ping-server-job", "group-1-min").build();
		Trigger t2 = TriggerBuilder
				.newTrigger()
				.withIdentity("every-30-seconds", "group1")
				.withSchedule(
				    SimpleScheduleBuilder.simpleSchedule()
					.withIntervalInSeconds(30).repeatForever())
				.build();		
		scheduler.scheduleJob( job, t2 );

		job = newJob(UpdateGameInstanceJob.class).withIdentity("update-game-instance-job", "group-1-min").build();
		Trigger t3 = TriggerBuilder
				.newTrigger()
				.withIdentity("every-60-seconds", "group2")
				.withSchedule(
				    SimpleScheduleBuilder.simpleSchedule()
					.withIntervalInSeconds(60).repeatForever())
				.build();		
		scheduler.scheduleJob( job, t3 );
	}
	
}