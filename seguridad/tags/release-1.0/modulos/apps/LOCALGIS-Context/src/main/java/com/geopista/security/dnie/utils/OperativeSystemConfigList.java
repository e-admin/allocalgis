package com.geopista.security.dnie.utils;

import java.util.Hashtable;



import com.geopista.security.dnie.beans.OperativeSystemConfig;
import com.geopista.security.dnie.global.CertificateConstants;

public class OperativeSystemConfigList {
		Hashtable<String,OperativeSystemConfig> operativeSystemConfigList = new Hashtable<String,OperativeSystemConfig>();

	    public OperativeSystemConfigList() { 	    	
	    	//operativeSystemConfigList.put("Windows XP",new OperativeSystemConfig(CertificateGlobal.DEFAULT_WINDOWS_SYSTEM_CERTIFICATE_STORE, CertificateGlobal.DEFAULT_WINDOWS_DNIE_LIBRARY,OperativeSystemConfig.osType.WINDOWS));
	    	operativeSystemConfigList.put("Windows XP (JKS)",new OperativeSystemConfig(CertificateUtils.getDefaultWindowsXPJksSystemCertStore(), CertificateConstants.DEFAULT_WINDOWS_DNIE_LIBRARY,OperativeSystemConfig.osType.WINDOWS));
	    	operativeSystemConfigList.put("Windows 7 (JKS)",new OperativeSystemConfig(CertificateUtils.getDefaultWindows7JksSystemCertStore(), CertificateConstants.DEFAULT_WINDOWS_DNIE_LIBRARY,OperativeSystemConfig.osType.WINDOWS));
			operativeSystemConfigList.put("Linux (JKS)",new OperativeSystemConfig(CertificateUtils.getDefaultLinuxJksSystemCertStore(), CertificateConstants.DEFAULT_LINUX_DNIE_LIBRARY,OperativeSystemConfig.osType.LINUX));		
	    }

	    public Hashtable getOperativeSystemConfigList() {
	        return operativeSystemConfigList;
	    }

	    public void setOperativeSystemConfigList(Hashtable operativeSystemConfigList) {
	        this.operativeSystemConfigList = operativeSystemConfigList;
	    }

	    public void setOperativeSystemConfig(String nombreOS, OperativeSystemConfig operativeSystemConfig)
	    {
	    	operativeSystemConfigList.put(nombreOS,operativeSystemConfig);
	    } 
	    
	    public OperativeSystemConfig getOperativeSystemConfig(String nombreOS)
	    {
	        return (OperativeSystemConfig)operativeSystemConfigList.get(nombreOS);
	    }		
}
