/**
 * CilvilworkClientWS.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.webservices.cilvilwork.client;


import org.apache.axis2.AxisFault;

import com.geopista.app.UserPreferenceConstants;
import com.geopista.util.config.UserPreferenceStore;
import com.geopista.webservices.cilvilwork.client.protocol.CivilWorkWSStub;


public class CilvilworkClientWS{

	private static CivilWorkWSStub civilworkStub = null;
	
	public static final String endPoint =  "TOMCATURL/LocalGISGeoWebServices/services/CivilWorkWS/";

	public static CivilWorkWSStub getCivilworkStub() {
		if (civilworkStub == null){
			try {
				//				String baseDirectory = "";
				//				String localgisBaseDir = AppContext.getApplicationContext().getString("ruta.base.mapas");
				//				if (localgisBaseDir!=null && !localgisBaseDir.equals("")){
				//					baseDirectory = localgisBaseDir;
				//				}
				civilworkStub = new CivilWorkWSStub(endPoint.replaceFirst("TOMCATURL", UserPreferenceStore.getUserPreference(UserPreferenceConstants.TOMCAT_URL, "", true)));
			} catch (AxisFault e) {
				e.printStackTrace();
				return null;
			}
		}
		return civilworkStub;
	}
}
