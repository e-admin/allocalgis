/**
 * MapImageEIELFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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