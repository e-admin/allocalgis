package com.geopista.security.dnie.utils;

import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.security.SecurityManager;
import com.geopista.security.dnie.global.CertificateConstants;
import com.geopista.security.sso.SSOAuthManager;


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
			String port = AppContext.getApplicationContext().getString(CertificateConstants.CERTIFICATE_PORT_ADMCAR); 
			String [] host = AppContext.getApplicationContext().getUserPreference(AppContext.HOST_ADMCAR,"",false).replace("//", "").replace("\\\\", "").split(":");
			String url = "https://" + host[1] + ":" + port + "/dnie";
			return url;
		}
		catch(Exception e){	
			System.out.println(e);
		}
		return null;		
	}
		
}
