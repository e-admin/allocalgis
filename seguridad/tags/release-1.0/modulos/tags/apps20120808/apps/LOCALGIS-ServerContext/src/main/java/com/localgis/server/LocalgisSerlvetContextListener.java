package com.localgis.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
 
public class LocalgisSerlvetContextListener implements ServletContextListener{
		private static ServletContext servletContext;
		
		public void contextInitialized(ServletContextEvent contextEvent) {
			System.out.println("Context Created");
			servletContext = contextEvent.getServletContext();
			// set variable to servlet context
			//context.setAttribute("TEST", "TEST_VALUE");
		}
		
		public void contextDestroyed(ServletContextEvent contextEvent) {
			servletContext = contextEvent.getServletContext();
			System.out.println("Context Destroyed");
		}
		
		public static ServletContext getServletContext(){
			return servletContext;
		}
}

