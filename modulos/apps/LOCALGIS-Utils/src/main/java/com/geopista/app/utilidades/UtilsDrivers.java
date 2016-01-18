/**
 * UtilsDrivers.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.utilidades;

import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class UtilsDrivers {

	
	public static void registerDriver(String driverClass) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		
		Enumeration enDrivers = DriverManager.getDrivers();
	    while(enDrivers.hasMoreElements()){
	    	java.sql.Driver driver=(java.sql.Driver)enDrivers.nextElement();
	    	if (driverClass!=null){
		    	if (driver.getClass().toString().contains(driverClass)){
		    		return;
		    	}
		    	else{
		    		//System.out.println("Desregistrando driver 1:"+driver);
		    		DriverManager.deregisterDriver(driver);
		    	}
	    	}
	    	else{
	    		//System.out.println("Desregistrando driver 2:"+driver);
	    		DriverManager.deregisterDriver(driver);
	    	}
	    }
	    if (driverClass!=null){
		    java.sql.Driver driver=(java.sql.Driver)Class.forName(driverClass).newInstance();
		
		    //System.out.println("Registrando driver:"+driver);
		    DriverManager.registerDriver(driver);
		    //DriverManager.setLogWriter(new PrintWriter((System.err)));
	    }
	}
}
