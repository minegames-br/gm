package com.thecraftcloud.manager;

import static org.quartz.JobBuilder.newJob;

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
		JobDetail job = null;
			
		job = newJob(PingServerJob.class).withIdentity("ping-server-job", "group-1-min").build();
		Trigger t2 = TriggerBuilder
				.newTrigger()
				.withIdentity("every-5-seconds", "group1")
				.withSchedule(
				    SimpleScheduleBuilder.simpleSchedule()
					.withIntervalInSeconds(30).repeatForever())
				.build();		
		scheduler.scheduleJob( job, t2 );

		job = newJob(UpdateGameInstanceJob.class).withIdentity("update-game-instance-job", "group-1-min").build();
		Trigger t3 = TriggerBuilder
				.newTrigger()
				.withIdentity("every-10-seconds", "group2")
				.withSchedule(
				    SimpleScheduleBuilder.simpleSchedule()
					.withIntervalInSeconds(30).repeatForever())
				.build();		
		scheduler.scheduleJob( job, t3 );

		job = newJob(GameQueueJob.class).withIdentity("game-queue-job", "group-5-secs").build();
		Trigger t4 = TriggerBuilder
				.newTrigger()
				.withIdentity("every-5-seconds", "group3")
				.withSchedule(
				    SimpleScheduleBuilder.simpleSchedule()
					.withIntervalInSeconds(1).repeatForever())
				.build();		
		scheduler.scheduleJob( job, t4 );

		job = newJob(PrepareGamesJob.class).withIdentity("prepare-games-job", "group-30-secs").build();
		Trigger t5 = TriggerBuilder
				.newTrigger()
				.withIdentity("every-10-seconds", "group3")
				.withSchedule(
				    SimpleScheduleBuilder.simpleSchedule()
					.withIntervalInSeconds(30).repeatForever())
				.build();		
		scheduler.scheduleJob( job, t5 );

		job = newJob(AdminQueueJob.class).withIdentity("admin-queue-job", "group-2-secs").build();
		Trigger t6 = TriggerBuilder
				.newTrigger()
				.withIdentity("every-2-seconds", "group8")
				.withSchedule(
				    SimpleScheduleBuilder.simpleSchedule()
					.withIntervalInSeconds(30).repeatForever())
				.build();		
		scheduler.scheduleJob( job, t6 );

		job = newJob(SendPlayersToLobbyJob.class).withIdentity("send-player-to-lobby-job", "group-5-secs").build();
		Trigger t7 = TriggerBuilder
				.newTrigger()
				.withIdentity("every-5-seconds", "group9")
				.withSchedule(
				    SimpleScheduleBuilder.simpleSchedule()
					.withIntervalInSeconds(5).repeatForever())
				.build();		
		scheduler.scheduleJob( job, t7 );

	}
	
}