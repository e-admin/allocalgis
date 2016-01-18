package com.geopista.security.dnie.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERObject;

import com.geopista.security.config.SecurityPropertiesStore;
import com.geopista.security.dnie.beans.KeyValue;
import com.geopista.security.dnie.global.CertificateConstants;
import com.geopista.util.config.UserPreferenceStore;


public class CertificateUtils {
	
	static Logger log = Logger.getLogger(CertificateUtils.class);
		
	public static X509Certificate getCaFNMTCert(){
		try {
			return importCertificateFromInputStream(CertificateUtils.class.getResourceAsStream("/" + CertificateConstants.CA_FNTM_CERT_PATH));		
		} catch (Exception e) {
			System.out.println(e);				
		}
		return null;
	}
	
	public static X509Certificate getCaDnie002Cert(){
		try {
			return importCertificateFromInputStream(CertificateUtils.class.getResourceAsStream("/" + CertificateConstants.CA_DNIE002_CERT_PATH));
		} catch (Exception e) {
			System.out.println(e);				
		}
		return null;
	}
				    
	public static X509Certificate [] getCACertificatesArray(){
		X509Certificate [] certificateArray = {getCaFNMTCert(),getCaDnie002Cert()};
		return certificateArray;	
	}
	
	public static InputStream getPersonalCertificateStorePath(){
		try {
			  return CertificateUtils.class.getResourceAsStream("/" + CertificateConstants.PERSONAL_TRUSTED_CERTIFICATE_STORE);		
		} catch (Exception e) {
			System.out.println(e);			
		}	
		return null;
	}
	
//	public static URI getPersonalCertificateStorePath(){
//		try {
//			return ClassLoader.getSystemResource(CertificateGlobal.PERSONAL_TRUSTED_CERTIFICATE_STORE).toURI();		
//		} catch (URISyntaxException e) {
//			System.out.println(e);			
//		}	
//		return null;
//	}
	
	public static KeyStore getSystemKeyStore(char [] password){		
		return getSystemKeyStore(getSystemCertStore(), password);
	}
	
	public static KeyStore getSystemKeyStore(String systemCertStore,char [] password){
		try{
			KeyStore keyStore = null;
			if(isDefaultWindowsCertificateStore(systemCertStore)){ 
				keyStore = KeyStore.getInstance(CertificateConstants.DEFAULT_WINDOWS_SYSTEM_CERTIFICATE_STORE); 
				keyStore.load(null, null);		
			}
			else {
				keyStore = KeyStore.getInstance(CertificateConstants.KEYSTORETYPE_JKS); 
				keyStore.load(new FileInputStream(new File(systemCertStore)),password);						
			}
			return keyStore;
		}catch (KeyStoreException e) {
			log.error(e);
		} catch (NoSuchAlgorithmException e) {
			log.error(e);
		} catch (CertificateException e) {
			log.error(e);
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
		return null;
	}
	
	/*
	 * Comprueba si el certificado cliente obtenido tiene como emisor al certificado de la FNMT
	 * Lo comprueba verificando que el certificado cliente fue firmado con la clave publica del certificado CA
	 */
	public static boolean isClientCertificate(X509Certificate clientCert,X509Certificate caCert){
		try {
			clientCert.verify(caCert.getPublicKey());
			return true;
		} catch (NullPointerException e) {
			System.out.println(e);	
		} catch (InvalidKeyException e) {
			System.out.println(e);
		} catch (CertificateException e) {
			System.out.println(e);
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		} catch (NoSuchProviderException e) {
			System.out.println(e);
		} catch (SignatureException e) {
			//System.out.println(e);
			return false;
		} catch (SecurityException e) {
			System.out.println(e);
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		return false;
	}
	
	/*
	 * Comprueba si el certificado cliente obtenido tiene como emisor al certificado de la FNMT
	 * Lo comprueba verificando que el certificado cliente fue firmado con la clave publica del certificado CA
	 */
	public static boolean isClientCertificate(X509Certificate clientCert){
		//return true;
		try {
			X509Certificate [] certificateArray = getCACertificatesArray();
			int i=0;
			while(i<certificateArray.length){
				//if(validateSignedCertificate(clientCert,certificateArray[i])) return true;
				if(isClientCertificate(clientCert,certificateArray[i])) return true;
				i++;
			}		
		} catch (NullPointerException e) {
			System.out.println(e);			
		}		
		return false;
	}

	
	public static X509Certificate importCertificateFromInputStream(InputStream certificateFile) {
	    try {
	    	CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
	        X509Certificate certificateTemp = (X509Certificate) certificateFactory.generateCertificate(certificateFile);
	        return certificateTemp;
	    } catch (CertificateException e) {
	    	System.out.println(e);
	    } catch (Exception e) {
	    	System.out.println(e);
	    }
	    return null;
	}
	
	public static char [] getPersonalCertificateStorePassword(){
		return CertificateConstants.PERSONAL_TRUSTED_CERTIFICATE_STORE_PASSWORD.toCharArray();
	}
		
	public static List<KeyValue> getPersonalCertificatesFromStore(String password){
		List<KeyValue> certificateslist = new ArrayList<KeyValue>();
		try {
			KeyStore keyStore = getSystemKeyStore(password.toCharArray());	
			if(keyStore!=null){
				Enumeration<String> enumeration = keyStore.aliases();	
				if(keyStore.aliases().hasMoreElements()){
					String alias = null;
					X509Certificate certificate = null;
					while(enumeration.hasMoreElements()){
						alias = enumeration.nextElement().toString();
						certificate = (X509Certificate) keyStore.getCertificate(alias);
						if(CertificateUtils.isClientCertificate(certificate)){
							certificateslist.add(new KeyValue(getSubjectCN(certificate.getSubjectDN().toString()),alias));
						}
					}	
				}
			}
		} catch (KeyStoreException e) {
			System.out.println(e);
		}  
		return certificateslist;
	}
	
	public static String getNIFfromSubjectDN(String subjectDN){
		try{
			//RegEx para NIF o NIE
			Pattern pattern = Pattern.compile("([0-9]{8})[A-Za-z]|[X-Zx-z]([0-9]{7})[A-Za-z]");			
			Matcher m = pattern.matcher(subjectDN);
			//(devuelve la primera ocurrencia de nif o nie)
			while (m.find()) return m.group().toUpperCase();			
		}
		catch(Exception e){
			System.out.println(e);			
		}	
		return null;
	}
	
	public static class DefaultTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
	}
	
	public static DERObject bytesEncodedToASN1Sequence(byte[] bytes) {
        if (bytes == null) {
                return null;
        }
        ASN1InputStream aIn = new ASN1InputStream(new ByteArrayInputStream(bytes));
        DERObject derobj = null;
        try {
                derobj = aIn.readObject();
                return derobj;
        } catch (IOException e) {
        	System.out.println(e.getMessage());       
        }
     	return null;
	}
	
	public static boolean isDNIeActive(){
		return Boolean.valueOf(SecurityPropertiesStore.getSecurityPropertiesStore().getString(CertificateConstants.DNIE_AUTH_ACTIVE));
	}
		
	public static String getOperativeSystem(){
		return UserPreferenceStore.getUserPreferenceStore().getUserPreference(CertificateConstants.OPERATIVE_SYSTEM, "Windows", true);
	}
	
	public static String getSystemCertStore(){
		return UserPreferenceStore.getUserPreferenceStore().getUserPreference(CertificateConstants.SYSTEM_CERT_STORE, getDefaultWindowsXPJksSystemCertStore(), true);
	}
	
	public static String getDNIeLibrary(){
		return UserPreferenceStore.getUserPreferenceStore().getUserPreference(CertificateConstants.DNIE_LIBRARY, CertificateConstants.DEFAULT_WINDOWS_DNIE_LIBRARY, true);
	}
	
	public static String getDefaultWindowsXPJksSystemCertStore(){
		return System.getenv(CertificateConstants.DEFAULT_WINDOWS_USER_APP) + CertificateConstants.DEFAULT_WINDOWS_XP_JKS_SYSTEM_CERTIFICATE_STORE;
	}
	
	public static String getDefaultWindows7JksSystemCertStore(){
		return System.getenv(CertificateConstants.DEFAULT_WINDOWS_USER_APP) + CertificateConstants.DEFAULT_WINDOWS_7_JKS_SYSTEM_CERTIFICATE_STORE;
	}
	
	public static String getDefaultLinuxJksSystemCertStore(){
		return System.getenv(CertificateConstants.DEFAULT_LINUX_USER_HOME) + CertificateConstants.DEFAULT_LINUX_JKS_SYSTEM_CERTIFICATE_STORE;
	}
	
	public static boolean isDefaultWindowsCertificateStore(){
		return CertificateUtils.getSystemCertStore().toUpperCase().equals(CertificateConstants.DEFAULT_WINDOWS_SYSTEM_CERTIFICATE_STORE);
	}
	
	public static boolean isDefaultWindowsCertificateStore(String systemCertificateStore){
		return systemCertificateStore.toUpperCase().equals(CertificateConstants.DEFAULT_WINDOWS_SYSTEM_CERTIFICATE_STORE);
	}
	
	public static boolean isOperativeSystemWindows(){
		return CertificateUtils.getOperativeSystem().toUpperCase().equals(CertificateConstants.DEFAULT_WINDOWS_NAME);
	}
	
	public static boolean isOperativeSystemLinux(){
		return CertificateUtils.getOperativeSystem().toUpperCase().equals(CertificateConstants.DEFAULT_LINUX_NAME);
	}
		
	private static String getSubjectCN(String subjectDN){		
		return subjectDN.split(",")[0].replace("CN=", "");
	}
	
	
//	public static boolean validateSignedCertificate(X509Certificate clientCert, X509Certificate caCert){
//		 byte[]tbs;
//        try{
//       	 tbs = clientCert.getTBSCertificate();
//        }catch(CertificateEncodingException e){
//       	 System.out.println(e.getMessage());
//            return false;
//        }        
//        Signature signature;
//        try{
//       	 signature = Signature.getInstance(clientCert.getSigAlgName());
//        }catch(NoSuchAlgorithmException e){
//       	 System.out.println(e.getMessage());
//            return false;
//        }        
//        try{                
//       	 signature.initVerify(caCert.getPublicKey());
//               
//            signature.update(tbs);
//               
//            if(signature.verify(clientCert.getSignature())){
//                return true;
//            }else{
//           	 return false;
//            }                   
//        }catch(InvalidKeyException e){
//            e.printStackTrace();
//            return false;
//        }catch(SignatureException e){
//            e.printStackTrace();
//            return false;
//        }
//	}
	
	public static KeyManagerFactory generateKeyManagerFactory(KeyStore keyStore, char [] password){
		try {
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());			
			kmf.init(keyStore,password);	
			return kmf;
		} catch (KeyStoreException e) {
			System.out.println(e);
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		} catch (UnrecoverableKeyException e) {
			System.out.println(e);
		}
		return null;
	};
	
}
