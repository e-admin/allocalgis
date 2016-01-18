/**
 * LocalgisSerlvetContextListener.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
 
public class LocalgisSerlvetContextListener implements ServletContextListener{
		private static ServletContext servletContext;
		
		public void contextInitialized(ServletContextEvent contextEvent) {
			System.out.println("Context Created for Module:" +contextEvent.getServletContext().getServletContextName());
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

