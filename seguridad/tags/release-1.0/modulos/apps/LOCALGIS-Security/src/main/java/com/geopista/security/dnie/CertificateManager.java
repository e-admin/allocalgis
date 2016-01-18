package com.geopista.security.dnie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.apache.log4j.Logger;

import com.geopista.security.config.SecurityPropertiesStore;
import com.geopista.security.dnie.afirma.AFirmaException;
import com.geopista.security.dnie.afirma.AFirmaManager;
import com.geopista.security.dnie.afirma.DatosValidacionCertificadoOut;
import com.geopista.security.dnie.global.CertificateConstants;
import com.geopista.security.dnie.utils.Base64Helper;
import com.geopista.security.dnie.utils.CertificateUtils;


public class CertificateManager {

	Logger log = Logger.getLogger(CertificateManager.class);

	private X509Certificate clientCert = null;
	private KeyStore keyStore = null;

	public CertificateManager(File certificateFile, String password) {
		try {
			KeyStore ks = KeyStore
					.getInstance(CertificateConstants.KEYSTORETYPE_PKCS12);
			ks.load(new FileInputStream(certificateFile),
					password.toCharArray());

			String alias = "";
			Enumeration<String> aliases = ks.aliases();
			while (aliases.hasMoreElements()) {
				alias = aliases.nextElement();
				clientCert = (X509Certificate) ks.getCertificate(alias);

				break;
			}
			if (isCertificateFNMT()) {
				Key privateKey = (Key) ks.getKey(alias, password.toCharArray());
				
				keyStore = KeyStore
						.getInstance(CertificateConstants.KEYSTORETYPE_JKS);
				keyStore.load(CertificateUtils
								.getPersonalCertificateStorePath(),
						CertificateUtils.getPersonalCertificateStorePassword());
				X509Certificate[] certsChain = new X509Certificate[] { clientCert };
				keyStore.setKeyEntry(alias, privateKey,
						CertificateUtils.getPersonalCertificateStorePassword(),
						certsChain);

				System.out.println("Certificado: " + clientCert.getSubjectDN());
			} else
				System.out.println("ERROR: Certificado incorrecto");
		} catch (KeyStoreException e) {
			System.out.println(e);
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		} catch (CertificateException e) {
			System.out.println(e);
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		} catch (UnrecoverableKeyException e) {
			System.out.println(e);		
		}
	}

	public CertificateManager(String alias, String password) {
		try {
			KeyStore ks = CertificateUtils.getSystemKeyStore(password
					.toCharArray());
			if (ks != null && ks.containsAlias(alias))
				clientCert = (X509Certificate) ks.getCertificate(alias);

			if (isCertificateFNMT()) {

				keyStore = KeyStore
						.getInstance(CertificateConstants.KEYSTORETYPE_JKS);
				keyStore.load(CertificateUtils.getPersonalCertificateStorePath(),
						CertificateUtils.getPersonalCertificateStorePassword());
				X509Certificate[] certsChain = new X509Certificate[] { clientCert };

				Key key = null;//ks.getKey(alias, password.toCharArray());

				// PrivateKey privateKey = null;
			//	KeyStore ks2 = null;
				//if (CertificateUtils.isDefaultWindowsCertificateStore()) {
			
					//System.setProperty("java.security.KeyStore","msks-MY");
					//ks2 = KeyStore.getInstance("msks-KEYSTORE", "JCAPI");
			
					//key = ks.getKey(alias,"48904969T".toCharArray());
					//JCAPIProperties.getInstance().

				//} else {
					key = ks.getKey(alias, password.toCharArray());
					// Key key = ks.getKey(alias, password.toCharArray());
					// privateKey = (PrivateKey)key;
				//}

				keyStore.setKeyEntry(alias, key,
						CertificateUtils.getPersonalCertificateStorePassword(),
						certsChain);
			} else
				System.out.println("ERROR: Certificado incorrecto");
		} catch (KeyStoreException e) {
			System.out.println(e);
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		} catch (CertificateException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		} catch (UnrecoverableEntryException e) {
			System.out.println(e);
		}
	}

	public CertificateManager(X509Certificate certificate) {
		clientCert = certificate;
	}

	public X509Certificate getClientCert() {
		return clientCert;
	}

	public KeyStore getKeyStore() {
		return keyStore;
	}

	public boolean isCertificateFNMT() {
		return CertificateUtils.isClientCertificate(clientCert,
				CertificateUtils.getCaFNMTCert());
	}

	public boolean isCertificate() {
		return CertificateUtils.isClientCertificate(clientCert);
	}

	public boolean isValid() {
		boolean result = false;
		if (isCertificate()) {
			if (isValidNonExpired()) {
				if (isValidNonRevoke()){
					result = true;
				} else
					log.error("El certificado ha sido revocado");
			} else
				log.error("El certificado a caducado");
		} else
			log.error("No es un certificado FNMT/DNIe");
		return result;
	}

	public boolean isValidNonRevoke() {
		if(isAfirmaActive()){			
			AFirmaManager aFirmaManager = new AFirmaManager();
			String certificadoBase64;
			try {
				certificadoBase64 = Base64Helper.getInstance().encodeBytes(
						clientCert.getEncoded());
				DatosValidacionCertificadoOut datosCert = aFirmaManager
						.validarCertificado(certificadoBase64);
				if (datosCert.certificadoCorrecto()) {
					return true;
				}
			} catch (CertificateEncodingException e) {
				log.error(e);
			} catch (AFirmaException e) {
				log.error(e);
			}
			return false;
		}
		return true;		
	}

	public boolean isValidNonExpired() {
		try {
			clientCert.checkValidity();
		} catch (CertificateExpiredException e) {
			System.out.println(e);
			return false;
		} catch (CertificateNotYetValidException e) {
			System.out.println(e);
			return false;
		}
		return true;
	}
	
	private static Boolean isAfirmaActive(){
		return Boolean.valueOf(SecurityPropertiesStore.getSecurityPropertiesStore().getString(CertificateConstants.AFIRMA_AUTH_ACTIVE));
	}

}
