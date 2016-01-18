/**
 * CertificateConstants.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.security.dnie.global;

public class CertificateConstants {

	public static final String OPERATIVE_SYSTEM = "dnie.operativesystem";
    public static final String DEFAULT_WINDOWS_NAME = "WINDOWS";
    public static final String DEFAULT_WINDOWS_XP_NAME = "Windows XP";
    public static final String DEFAULT_WINDOWS_JKS_NAME = "Windows (JKS)";
    public static final String DEFAULT_LINUX_NAME = "LINUX";
    public static final String DEFAULT_LINUX_JKS_NAME = "Linux (JKS)"; 
		
	public static final String DEFAULT_WINDOWS_USER_APP = "APPDATA"; 		
	public static final String DEFAULT_WINDOWS_USER_PROFILE = "USERPROFILE"; 
	
	public static final String DEFAULT_LINUX_USER_HOME = "user.home"; 
    
	public static final String DEFAULT_WINDOWS_SYSTEM_CERTIFICATE_STORE = "WINDOWS-MY"; 
	public static final String DEFAULT_WINDOWS_XP_JKS_SYSTEM_CERTIFICATE_STORE = "/Sun/Java/Deployment/security/trusted.clientcerts";	
	public static final String DEFAULT_WINDOWS_VISTA_JKS_SYSTEM_CERTIFICATE_STORE = "/Roaming/Sun/Java/Deployment/security/trusted.clientcerts";	
	public static final String DEFAULT_WINDOWS_7_JKS_SYSTEM_CERTIFICATE_STORE = "/LocalLow/Sun/Java/Deployment/security/trusted.clientcerts";
	public static final String DEFAULT_LINUX_JKS_SYSTEM_CERTIFICATE_STORE = "/.java/Deployment/security/trusted.clientcerts";
	
	public static final String DEFAULT_WINDOWS_XP_DNIE_LIBRARY = "C:/WINDOWS/system32/UsrPkcs11.dll"; 
	public static final String DEFAULT_WINDOWS_VISTA_DNIE_LIBRARY = "C:/WINDOWS/system32/UsrPkcs11.dll";
	public static final String DEFAULT_WINDOWS_7_DNIE_LIBRARY = "C:/WINDOWS/SysWOW64/UsrPkcs11.dll"; 
	public static final String DEFAULT_LINUX_DNIE_LIBRARY = "/usr/lib/opensc-pkcs11.so"; 

	public static final String DEFAULT_WINDOWS_XP_DNIE_V10_LIBRARY_32 = "C:/WINDOWS/system32/DNIe_P11_priv.dll"; 
	public static final String DEFAULT_WINDOWS_XP_DNIE_V10_LIBRARY_64 = "C:/WINDOWS/SysWOW64/DNIe_P11_priv.dll"; 
	
	public static final String DEFAULT_WINDOWS_VISTA_DNIE_v10_LIBRARY_32 = "C:/WINDOWS/system32/DNIe_P11_priv.dll";
	public static final String DEFAULT_WINDOWS_VISTA_DNIE_v10_LIBRARY_64 = "C:/WINDOWS/SysWOW64/DNIe_P11_priv.dll";

	public static final String DEFAULT_WINDOWS_7_DNIE_v10_LIBRARY_64 = "C:/WINDOWS/SysWOW64/DNIe_P11_priv.dll"; 
	public static final String DEFAULT_WINDOWS_7_DNIE_v10_LIBRARY_32 = "C:/WINDOWS/system32/DNIe_P11_priv.dll"; 

	public static final String DEFAULT_LINUX_DNIE_v10_LIBRARY_32 = "/usr/lib/opensc-pkcs11.so"; 
	public static final String DEFAULT_LINUX_DNIE_v10_LIBRARY_64 = "/usr/lib/opensc-pkcs11.so"; 

	
	
    public static final String SYSTEM_CERT_STORE = "dnie.systemcertstore";
    public static final String DNIE_LIBRARY = "dnie.library";
	
    public static final String DNIE_SAFE_PORT = "dnie.safe.port";
	
	public static final String CA_FNTM_CERT_PATH = "dnieResources/FNMTClase2CA.cer"; 
	public static final String CA_DNIE001_CERT_PATH = "dnieResources/ACDNIE001-SHA1.crt"; 
	public static final String CA_DNIE002_CERT_PATH = "dnieResources/ACDNIE002-SHA1.crt"; 
	public static final String CA_DNIE003_CERT_PATH = "dnieResources/ACDNIE003-SHA1.crt"; 
	
	public static final String KEYSTORETYPE_PKCS11 = "PKCS11";
	public static final String KEYSTORETYPE_PKCS12 = "PKCS12"; 
	public static final String KEYSTORETYPE_JKS = "JKS"; 	
	
	public static final String KEYFACTORYTYPE_RSA = "RSA";
	
	public static final String PERSONAL_TRUSTED_CERTIFICATE_STORE = "dnieResources/clientcerts";
	public static final String PERSONAL_TRUSTED_CERTIFICATE_STORE_PASSWORD = "password";
	
	public static final String AFIRMA_AUTH_ACTIVE = "afirma.active";
	
}
