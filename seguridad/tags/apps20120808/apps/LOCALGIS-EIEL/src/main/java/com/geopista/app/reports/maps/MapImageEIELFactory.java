package com.geopista.app.reports.maps;

import java.sql.SQLException;
import java.util.HashMap;

import net.sf.jasperreports.engine.JRReport;

import com.geopista.app.utilidades.UtilsDrivers;

public class MapImageEIELFactory {

    private static JRReport report = null;
    private static HashMap interactiveMapImageScales = null;

    public static void init(JRReport baseReport) {
    	MapImageEIELFactory.report = baseReport;
        interactiveMapImageScales = new HashMap();
        try {
			UtilsDrivers.registerDriver(null);
			//DriverManager.setLogWriter(new PrintWriter((System.err)));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
}