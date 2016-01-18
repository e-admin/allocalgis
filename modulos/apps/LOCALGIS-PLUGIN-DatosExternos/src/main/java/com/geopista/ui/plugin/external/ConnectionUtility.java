/**
 * ConnectionUtility.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.external;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;



public class ConnectionUtility{
	
    private static String[] loadedDriverNames;  
    private static Driver pluginDrivers[] = null;
    private static Hashtable driversHT = new Hashtable();
    
    private static Logger logger = Logger.getLogger(ConnectionUtility.class);

    private ConnectionUtility()
    {
    	super();
    }
    
    public static String[] getLoadedDriversClassName(){
    	return loadedDriverNames;
    }

    private static boolean implementsSQLDriver(Class aClass)
    {
        Class interfaces[] = aClass.getInterfaces();
        for(int i = 0; i < interfaces.length; i++)
        {
            Class anInterface = interfaces[i];
            if("java.sql.Driver".equals(anInterface.getName()))
                return true;
        }

        return false;
    }

    private static boolean instanceOfSQLDriver(Class aClass)
    {
        for(Class superClass = aClass; (superClass = superClass.getSuperclass()) != null;)
        {
            if(superClass.isInterface() && "java.sql.Driver".equals(superClass.getName()))
                return true;
            if(implementsSQLDriver(aClass))
                return true;
        }

        return false;
    }

    private static String[] getJarFileEntries(File jarFile)
    {
        ArrayList jarEntries = new ArrayList();
        try
        {
            ZipFile zf = new ZipFile(jarFile);
            Enumeration e = zf.entries();
            do
            {
                if(!e.hasMoreElements())
                    break;
                ZipEntry ze = (ZipEntry)e.nextElement();
                if(!ze.isDirectory())
                    jarEntries.add(ze.getName());
            } while(true);
            zf.close();
        }
        catch(IOException e1) { }
        return (String[])(String[])jarEntries.toArray(new String[jarEntries.size()]);
    }
    
    public static Hashtable findAllDrivers() {
    	
        String driversNames[] = {"sun.jdbc.odbc.JdbcOdbcDriver", "jTDS 1.2", "com.mysql.jdbc.Driver", "org.postgresql.Driver", "oracle.jdbc.driver.OracleDriver"};
        String drivers[] = {"sun.jdbc.odbc.JdbcOdbcDriver", "net.sourceforge.jtds.jdbc.Driver", "com.mysql.jdbc.Driver", "org.postgresql.Driver", "oracle.jdbc.driver.OracleDriver"};
        String connectStrings[] = {"jdbc:odbc:dataBase", "jdbc:jtds:sqlserver://hostname:1433/dataBase", "jdbc:mysql://&lt;hostname&gt;[:&lt;3306&gt;]/&lt;dbname&gt;", "jdbc:postgresql:[&lt;//host&gt;[:&lt;5432&gt;/]]&lt;database&gt;", "jdbc:oracle:thin:@&lt;server&gt;[:1521]:database_name"};
		/*
        String driversNames[] = {"sun.jdbc.odbc.JdbcOdbcDriver"};
        String drivers[] = {"sun.jdbc.odbc.JdbcOdbcDriver"};
        String connectStrings[] = {""};        
        */
    	Properties properties = System.getProperties();
    	//System.out.println("Properties:"+System.getProperties());
    	Enumeration enumeration = properties.keys();
    	Hashtable hashtable = new Hashtable();
    	
    	for (int i = 0; i < drivers.length; i++) {
    		hashtable.put(drivers[i], connectStrings[i]);
    		driversHT.put(drivers[i], driversNames[i]);
    		
    	}
    	
    	while (enumeration.hasMoreElements()) {
			String item = (String) enumeration.nextElement();
			if (item.indexOf("geopista.jdbc")==0) {
				String driverClassName = "";
				String driverUrl = "";
				String driverName = "";
				//System.out.println(item);
				String value = (String)properties.get(item);
				if (item.indexOf("class") > 0) {
						driverClassName = properties.getProperty(item);				
						driverUrl = properties.getProperty(item.replaceFirst("class","url") );
						if (driverUrl == null) {
							driverUrl = "";
						}
						driverName = properties.getProperty(item.replaceFirst("class","name") );
						if (driverName == null) {
							driverName = driverClassName;
						}
						hashtable.put(driverClassName, driverUrl);
						driversHT.put(driverClassName, driverName);
				}
			}
		}
    	return hashtable;
    }

    private static Driver findLoadedDriver(String name) {
   		for (int i = 0; i < pluginDrivers.length; i++) {
			Driver driver = pluginDrivers[i];
			String driversName = (String) driversHT.get(name);
			if ((driversName!=null)&&(driver.toString().startsWith(driversName)))
				return driver;
		}
   		return null;
    }
    
	public static Connection getConnection(ExternalDataSource externalDataSource) {
		// TODO Auto-generated method stub
    	try {
    		Driver d = findLoadedDriver(externalDataSource.getDriver());
    		if (d!=null) {
    			Properties properties = new Properties();
    			properties.put("user", externalDataSource.getUserName());
    			properties.put("password", externalDataSource.getPassword());
    			Connection connection =d.connect(externalDataSource.getConnectString(), properties);
    			return connection;
    		}	

		} catch (SQLException e) {
			logger.error("Error a obtener la conexion contra la base de datos externa:"+externalDataSource.getConnectString());
		}
    	return null;
	}

    public static boolean testConnection(String driver, String connectString, String user, String password)
    {
    	try {
    		Driver d = findLoadedDriver(driver);
    		if (d!=null) {
    			Properties info = new Properties();
    			info.put("user", user);
    			info.put("password", password);    			
    			Connection connection = d.connect(connectString, info);
    			if (connection!=null) {
    				connection.close();
    				return true;
    			}
    		}	

		} catch (SQLException e) {
			
		}
    	return false;
    }

	public static void loadDrivers(Hashtable drivers) {
		Enumeration driversClassNames = drivers.keys();
		Vector allDrivers = new Vector();
		while (driversClassNames.hasMoreElements()) {
			String className = (String) driversClassNames.nextElement();
			Driver driver;
			try {
				driver = (Driver)Class.forName(className).newInstance();
				DriverManager.registerDriver(driver);
				allDrivers.add(driver);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		pluginDrivers = (Driver[]) allDrivers.toArray(new Driver[allDrivers.size()]);
		
		
	}

}
