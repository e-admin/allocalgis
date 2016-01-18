package com.geopista.security.dnie.global;

public class CertificateConstants {

	public static final String OPERATIVE_SYSTEM = "dnie.operativesystem";
    public static final String DEFAULT_WINDOWS_NAME = "WINDOWS";
    public static final String DEFAULT_WINDOWS_XP_NAME = "Windows XP";
    public static final String DEFAULT_WINDOWS_JKS_NAME = "Windows (JKS)";
    public static final String DEFAULT_LINUX_NAME = "LINUX";
    public static final String DEFAULT_LINUX_JKS_NAME = "Linux (JKS)"; 
		
	public static final String DEFAULT_WINDOWS_USER_APP = "APPDATA"; 	
	public static final String DEFAULT_LINUX_USER_HOME = "user.home"; 
    
	public static final String DEFAULT_WINDOWS_SYSTEM_CERTIFICATE_STORE = "WINDOWS-MY"; 	
	public static final String DEFAULT_WINDOWS_JKS_SYSTEM_CERTIFICATE_STORE = "/Sun/Java/Deployment/security/trusted.clientcerts";
	public static final String DEFAULT_LINUX_JKS_SYSTEM_CERTIFICATE_STORE = "/.java/Deployment/security/trusted.clientcerts";
	
	public static final String DEFAULT_WINDOWS_DNIE_LIBRARY = "C:/WINDOWS/system32/UsrPkcs11.dll"; 	
	public static final String DEFAULT_LINUX_DNIE_LIBRARY = "/usr/lib/opensc-pkcs11.so"; 
		
    public static final String SYSTEM_CERT_STORE = "dnie.systemcertstore";
    public static final String DNIE_LIBRARY = "dnie.library";
	
    public static final String CERTIFICATE_PORT_ADMCAR = "dnie.admcar.connection.port";
	
	public static final String CA_FNTM_CERT_PATH = "dnieResources/FNMTClase2CA.cer"; 
	public static final String CA_DNIE002_CERT_PATH = "dnieResources/ACDNIE002-SHA1.crt"; 
	
	public static final String KEYSTORETYPE_PKCS11 = "PKCS11";
	public static final String KEYSTORETYPE_PKCS12 = "PKCS12"; 
	public static final String KEYSTORETYPE_JKS = "JKS"; 	
	
	public static final String KEYFACTORYTYPE_RSA = "RSA";
	
	public static final String PERSONAL_TRUSTED_CERTIFICATE_STORE = "dnieResources/clientcerts";
	public static final String PERSONAL_TRUSTED_CERTIFICATE_STORE_PASSWORD = "password";
		
	public static final String DNIE_AUTH_ACTIVE = "dnie.authactive";
	
}
