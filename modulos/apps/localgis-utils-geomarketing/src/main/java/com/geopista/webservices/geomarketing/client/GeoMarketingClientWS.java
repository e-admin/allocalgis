/**
 * GeoMarketingClientWS.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.webservices.geomarketing.client;


import org.apache.axis2.AxisFault;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.util.config.UserPreferenceStore;
import com.geopista.webservices.geomarketing.client.protocol.GeoMarketingWSStub;

public class GeoMarketingClientWS {

	private static GeoMarketingWSStub geoStub = null;
	
	private final static String geoMarketingEndPoint = "TOMCATURL/LocalGISGeoWebServices/services/GeoMarketingWS/";

	public static GeoMarketingWSStub getGeoMarktingStub(){
		if (geoStub == null){
			try {
				String baseDirectory = "";
				String localgisBaseDir = AppContext.getApplicationContext().getString("ruta.base.mapas");
				if (localgisBaseDir!=null && !localgisBaseDir.equals("")){
					baseDirectory = localgisBaseDir;
				}
				String url = UserPreferenceStore.getUserPreference(UserPreferenceConstants.TOMCAT_URL, "", true);
				geoStub = new GeoMarketingWSStub(geoMarketingEndPoint.replaceFirst("TOMCATURL",  url));
			} catch (AxisFault e) {
				e.printStackTrace();
				return null;
			}
		}

		return geoStub;
	}
}
