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
		    	else
		    		DriverManager.deregisterDriver(driver);
	    	}
	    	else{
	    		DriverManager.deregisterDriver(driver);
	    	}
	    }
	    if (driverClass!=null){
		    java.sql.Driver driver=(java.sql.Driver)Class.forName(driverClass).newInstance();
		
		    DriverManager.registerDriver(driver);
		    //DriverManager.setLogWriter(new PrintWriter((System.err)));
	    }
	}
}
