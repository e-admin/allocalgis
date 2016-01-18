/*
 * Created on 31-may-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.server.database;

import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.sql.Connection;
import java.io.StringWriter;
import java.io.PrintWriter;

/**
 * @author adominguez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class freeCPoolDatabase extends CPoolDatabase {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CPoolDatabase.class);
    public static final String CIRCULAR="CIRCULAR";
    private static  Integer numeroConexiones= new Integer(0);
    private static DataSource datasource ;


	public static Connection getConnection() {
		try {
			Context context = context = new InitialContext();
            if (datasource==null)
            	datasource = (DataSource) context.lookup("java:comp/env/geopista");
			Connection connection = datasource.getConnection();
            addConexion();
			logger.debug("connection: " + connection);
            return connection;


		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			System.out.println("Exception connection****************************");
			logger.error("Exception: " + sw.toString());
			return null;

		}
	}

    

}

