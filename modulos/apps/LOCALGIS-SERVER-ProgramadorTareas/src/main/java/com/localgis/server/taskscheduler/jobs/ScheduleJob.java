/**
 * ScheduleJob.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.server.taskscheduler.jobs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
			
			List<JobExecutionContext> jobs = jec.getScheduler().getCurrentlyExecutingJobs();
			for (JobExecutionContext job : jobs) {
			    if (job.getTrigger().equals(jec.getTrigger()) && !job.getFireInstanceId().equals(jec.getFireInstanceId())) {
			    	log.info("Existe otra instancia del proceso "+jec.getTrigger().getKey()+" ejecutandose. Abortado ejecucion");
			        return;
			    }

			}
			
			log.info("Ejecutando tarea: " + jec.getTrigger().getKey());
			String []params=new String[1];
			try {
				String param0=(String)jec.getJobDetail().getJobDataMap().get("param0");			
				params[0]=param0;
			} catch (Exception e) {}
			
			ScheduleUtils.executeClass(Class.forName((String) jec.getJobDetail().
						getJobDataMap().get("mainClass")), (String) jec.getJobDetail().
						getJobDataMap().get("mainClassMethod"),params);
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
