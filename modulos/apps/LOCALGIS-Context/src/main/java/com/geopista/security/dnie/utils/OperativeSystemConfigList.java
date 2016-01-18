/**
 * OperativeSystemConfigList.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.security.dnie.utils;

import java.util.Hashtable;

import com.geopista.security.dnie.beans.OperativeSystemConfig;
import com.geopista.security.dnie.global.CertificateConstants;

public class OperativeSystemConfigList {
		Hashtable<String,OperativeSystemConfig> operativeSystemConfigList = new Hashtable<String,OperativeSystemConfig>();

	    public OperativeSystemConfigList() { 	    	
	    	//operativeSystemConfigList.put("Windows XP",new OperativeSystemConfig(CertificateGlobal.DEFAULT_WINDOWS_SYSTEM_CERTIFICATE_STORE, CertificateGlobal.DEFAULT_WINDOWS_DNIE_LIBRARY,OperativeSystemConfig.osType.WINDOWS));
	    	operativeSystemConfigList.put("Windows 7 (JKS)",new OperativeSystemConfig(CertificateUtils.getDefaultWindows7JksSystemCertStore(), CertificateConstants.DEFAULT_WINDOWS_7_DNIE_LIBRARY,OperativeSystemConfig.osType.WINDOWS_7));
	    	operativeSystemConfigList.put("Windows Vista (JKS)",new OperativeSystemConfig(CertificateUtils.getDefaultWindowsVistaJksSystemCertStore(), CertificateConstants.DEFAULT_WINDOWS_VISTA_DNIE_LIBRARY,OperativeSystemConfig.osType.WINDOWS_VISTA));
	    	operativeSystemConfigList.put("Windows XP (JKS)",new OperativeSystemConfig(CertificateUtils.getDefaultWindowsXPJksSystemCertStore(), CertificateConstants.DEFAULT_WINDOWS_XP_DNIE_LIBRARY,OperativeSystemConfig.osType.WINDOWS_XP));
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
