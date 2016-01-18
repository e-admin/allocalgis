/**
 * CertificateOperations.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.security.dnie.utils;

import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;

import org.apache.log4j.Logger;

import com.geopista.app.UserPreferenceConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.security.SecurityManager;
import com.geopista.security.dnie.global.CertificateConstants;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.util.config.UserPreferenceStore;


public class CertificateOperations extends CertificateUtils{
	
	static Logger log = Logger.getLogger(CertificateUtils.class);
					
	public static boolean certificateLogin(X509Certificate certificate, KeyManagerFactory kmf, String idApp){
		try {	
			if(SecurityManager.loginCertificate(idApp, certificate, kmf, getCertificateUrl())){
				if (SecurityManager.isConnected() && SecurityManager.getIdSesion()!=null) {
					SSOAuthManager.saveSSORegistry();
					return true;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
		return false;
	}
	
	private static String getCertificateUrl(){
		try{
			String port = UserPreferenceStore.getUserPreference(CertificateConstants.DNIE_SAFE_PORT,"9443",false); 
			String [] host = UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_SERVER_URL,"",false).replace("//", "").replace("\\\\", "").split(":");
			String url = "https://" + host[1] + ":" + port + WebAppConstants.DNIE_WEBAPP_NAME;
			return url;
		}
		catch(Exception e){	
			System.out.println(e);
		}
		return null;		
	}
		
}
