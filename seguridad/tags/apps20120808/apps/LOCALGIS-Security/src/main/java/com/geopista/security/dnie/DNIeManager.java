package com.geopista.security.dnie;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.List;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

import org.apache.log4j.Logger;

import com.geopista.security.dnie.global.CertificateConstants;
import com.geopista.security.dnie.utils.CertificateUtils;


public class DNIeManager {
	
	Logger log = Logger.getLogger(DNIeManager.class);
	
	public static final byte[] DNIe_ATR = {
		(byte)0x3B, (byte)0x7F, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x6A, (byte)0x44,
		(byte)0x4E, (byte)0x49, (byte)0x65, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
		(byte)0x00, (byte)0x00, (byte)0x90, (byte)0x00};

	public static final byte[] DNIe_MASK = {
		(byte)0xFF, (byte)0xFF, (byte)0x00, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
		(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
		(byte)0x00, (byte)0x00, (byte)0xFF, (byte)0xFF};
	
	private X509Certificate clientCert = null;
	KeyStore keyStore = null;
	
	private Card dnie = null;
	
	public DNIeManager(){		
		searchDNIe();
	}
			
	public X509Certificate getClientCert() {
		return clientCert;
	}
	
	public boolean isCertificateDNIe(){
		return CertificateUtils.isClientCertificate(clientCert,CertificateUtils.getCaDnie002Cert());
	}

	public Card getDNIe() {
		return dnie;
	}
	
	public KeyStore getKeyStore() {
		return keyStore;
	}

	public boolean existDNIe() {
		return (dnie!=null);
	}
	
	private void searchDNIe(){
		Card card = null;
		try{
			TerminalFactory factory = TerminalFactory.getDefault();
			List<CardTerminal> terminals = factory.terminals().list();
			int i=0;
			while (i < terminals.size()) {
				CardTerminal terminal = (CardTerminal) terminals.get(i);
				if(terminal.isCardPresent()) {
					card = terminal.connect("*");
					//card = terminal.connect("T=0");
				}
				i++;
			}
			if(isDNIe(card)){
				dnie = card;
			}
		} catch (CardException e) {
			System.out.println(e);
			log.error(e);
		}
	}

	public boolean isDNIe(){
		return dnie!=null;
	}
	
	private boolean isDNIe(Card card){
		Boolean isDNIe = false;	
		if(existDNIe()) return true;
		else if(card!=null){
			byte[] atrCard = card.getATR().getBytes();
			if(atrCard.length == DNIe_ATR.length) {
				isDNIe = true;
				int j=0;
				//byte[] dnie_v_1_0_Atr = atrCard;
				while(j < DNIe_ATR.length && isDNIe) {
					if((atrCard[j] & DNIe_MASK[j]) != (DNIe_ATR[j] & DNIe_MASK[j])){
						isDNIe = false; /*No es una tarjeta DNIe*/					
					}
					j++;
				}
			}else{
				isDNIe = false;
			}
		}
		return isDNIe;
	}	
	
	public boolean setConfig(String dnieLibrary){		
		String configName="";		
		String pkcs11ConfigSettings = "";
		pkcs11ConfigSettings += "name = DNIe\n";
		pkcs11ConfigSettings += "library = " + dnieLibrary + "\n";		
		for(int i=0;i<3;i++){
			try {
				Class sunPkcs11Class = sun.security.pkcs11.SunPKCS11.class;		
				ByteArrayInputStream confStream = new ByteArrayInputStream(pkcs11ConfigSettings.getBytes());
				Constructor pkcs11Constr = sunPkcs11Class.getConstructor(InputStream.class);		
				Provider p = (Provider) pkcs11Constr.newInstance(confStream);
				Security.addProvider(p);
				return true;
			} catch (SecurityException e) {
				System.out.println(e); 
			} catch (NoSuchMethodException e) {
				System.out.println(e);
			} catch (IllegalArgumentException e) {
				System.out.println(e);
			} catch (InstantiationException e) {
				System.out.println(e);
			} catch (IllegalAccessException e) {
				System.out.println(e);
			} catch (InvocationTargetException e) {
				System.out.println(e);
			}
		}
		return false;
	}
	
	public boolean isCertificate(){
		return CertificateUtils.isClientCertificate(clientCert, CertificateUtils.getCaDnie002Cert());
	}
			
	public boolean accessPin(String pin){
		try {
			if(!setConfig(CertificateUtils.getDNIeLibrary())) return false;
			keyStore = KeyStore.getInstance(CertificateConstants.KEYSTORETYPE_PKCS11);
			keyStore.load(null, pin.toCharArray());
			boolean found = false;
			Enumeration<String> enumeration = keyStore.aliases();
			String alias;
			do{
				alias = enumeration.nextElement().toString();
				if(alias.compareTo("CertAutenticacion")==0)
					found = true;
				else
					found = false;
			}while(enumeration.hasMoreElements() && found == false);
			System.out.println(alias);
			clientCert = (X509Certificate)keyStore.getCertificate(alias);
			System.out.println(clientCert.getSubjectDN());			
			return found;
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		} catch (CertificateException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		} catch (KeyStoreException e) {
			System.out.println(e);
		}
		return false;
	}
	
	
	
	
}
