package com.localgis.server.taskscheduler.jobs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.localgis.server.taskscheduler.utils.ScheduleUtils;

public class ScheduleJob implements Job {
	
	/**
	 * Logger
	 */
	private static final Logger log = Logger.getLogger(ScheduleJob.class); 
		
	/**
	 * Método de ejecución cuando se invoca la tarea
	 * @param jec: Contexto de la ejecucion de la tarea (contiene información de la tarea y del trigger de ejecución)
	 */
	public void execute(JobExecutionContext jec) throws JobExecutionException {	
		try {
			ScheduleUtils.executeClass(Class.forName((String) jec.getJobDetail().getJobDataMap().get("mainClass")), (String) jec.getJobDetail().getJobDataMap().get("mainClassMethod"));
			log.info("----------------------------------------------------------");
			log.info("Nombre: " + jec.getTrigger().getKey());
			log.info("Tarea invocada a la hora: " + new SimpleDateFormat("hh:mm:ss").format(new Date()));
			log.info("----------------------------------------------------------");
		} catch (Exception e) {
			log.error(e.getMessage());
			log.info("----------------------------------------------------------");
			log.info("ERROR DE EJECUCIÓN: Nombre: " + jec.getTrigger().getKey());
			log.info("ERROR DE EJECUCIÓN: Tarea invocada a la hora: " + new SimpleDateFormat("hh:mm:ss").format(new Date()));
			log.info("----------------------------------------------------------");
		}
		
		
	}
	

}
