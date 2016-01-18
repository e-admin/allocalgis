/**
 * DNIeManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.security.dnie;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
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
import com.geopista.util.config.UserPreferenceStore;


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
	
	Provider p;
	
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
		String pkcs11ConfigSettings = "";
		pkcs11ConfigSettings += "name=DNIe\n";
		pkcs11ConfigSettings += "library=" + dnieLibrary +"\n";
		//pkcs11ConfigSettings = "name = DNIe\nlibrary = c:/WINDOWS/system32/opensc-pkcs11.dll\n";
		//pkcs11ConfigSettings="name=OpenSC-OpenDNIe\r\nlibrary=C:\\WINDOWS\\system32\\opensc-pkcs11.dll\r\n";
		File f = new File(dnieLibrary);
		if(f.exists()){
			log.info("Libreria para el DNI electronico encontrada. "+pkcs11ConfigSettings+". Verificando PKCS11...");
			for(int i=0;i<3;i++){
				try {
					Class sunPkcs11Class = sun.security.pkcs11.SunPKCS11.class;		
					ByteArrayInputStream confStream = new ByteArrayInputStream(pkcs11ConfigSettings.getBytes());
					Constructor pkcs11Constr = sunPkcs11Class.getConstructor(InputStream.class);		
					p = (Provider) pkcs11Constr.newInstance(confStream);
					Security.addProvider(p);
					log.info("Libreria PKCS11 verificada");
					return true;
				} catch (SecurityException e) {
					//System.out.println(e);
					log.error(e);
				} catch (NoSuchMethodException e) {
					//System.out.println(e);
					log.error(e);
				} catch (IllegalArgumentException e) {
					//System.out.println(e);
					log.error(e);
				} catch (InstantiationException e) {
					//System.out.println(e);
					log.error(e);
				} catch (IllegalAccessException e) {
					//System.out.println(e);
					log.error(e);
				} catch (InvocationTargetException e) {
					log.error("Error al cargar el componente PKCS11",e);
					/*System.out.println(e);
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);
					System.out.println(sw.toString());*/
				}
			}
			
		}
		else{
			log.error("La libreria "+dnieLibrary+" no existe");
		}
		return false;
	}
	
	public boolean isCertificate(){
		return CertificateUtils.isClientCertificate(clientCert, CertificateUtils.getCaDnie002Cert());
	}
	
	private boolean loadKeyStore() throws KeyStoreException{
		String arquitectura=System.getProperty("sun.arch.data.model");
		String so=UserPreferenceStore.getUserPreference(CertificateConstants.OPERATIVE_SYSTEM,CertificateConstants.DEFAULT_WINDOWS_XP_NAME,true);
		
		log.info("Cargando keystore para la arquitectura:"+arquitectura);
		if ((arquitectura!=null) && (arquitectura.equals("32"))){
			if(!setConfig(CertificateUtils.getDNIeLibraryWindows7())) return false;
			
			//printProviders();
			try {
				//keyStore = KeyStore.getInstance(CertificateConstants.KEYSTORETYPE_PKCS11,p);
				keyStore = KeyStore.getInstance(CertificateConstants.KEYSTORETYPE_PKCS11);
			} catch (Exception e) {			
				keyStore = KeyStore.getInstance("Windows-MY");
				log.info("Usando CSP");
			}
		}
		else if ((arquitectura!=null) && (arquitectura.equals("64")) && (so.contains("Windows"))){
			keyStore = KeyStore.getInstance("Windows-MY");
		}
		else{
			if(!setConfig(CertificateUtils.getDNIeLibraryWindows7())) return false;
			try {
				keyStore = KeyStore.getInstance(CertificateConstants.KEYSTORETYPE_PKCS11,p);
			} catch (Exception e) {				
				keyStore = KeyStore.getInstance("Windows-MY");
				log.info("Usando CSP");
			}
		}	
		return true;
	}
	
	private void printProviders(){
		Provider[] providerList = Security.getProviders();
	    for (int i = 0; i < providerList.length; i++) {
	      System.out.println("[" + (i + 1) + "] - Provider name: " + providerList[i].getName());
	      System.out.println("Provider version number: " + providerList[i].getVersion());
	      System.out.println("Provider information:\n" + providerList[i].getInfo());
	      //System.out.println("Provider information:\n" + providerList[i].getProperty("type"));
	    }
	}
			
	public boolean accessPin(String pin){
		try {
			
			if (!loadKeyStore())
				return false;
						
			if (pin!=null)
				keyStore.load(null, pin.toCharArray());
			else
				keyStore.load(null, null);
			boolean found = false;
			Enumeration<String> enumeration = keyStore.aliases();
			String alias;
			do{
				alias = enumeration.nextElement().toString();
				if ((alias.compareTo("CertAutenticacion")==0) || (alias.contains("(AUTENTICACI")))
					found = true;
				else
					found = false;
			}while(enumeration.hasMoreElements() && found == false);
			//System.out.println(alias);
			clientCert = (X509Certificate)keyStore.getCertificate(alias);
			log.info("Alias:"+alias+" DN:"+clientCert.getSubjectDN());			
			return found;
		} catch (NoSuchAlgorithmException e) {
			if (pin!=null)
				System.out.println(e);
		} catch (CertificateException e) {
			if (pin!=null)
				System.out.println(e);
		} catch (IOException e) {
			if (pin!=null)
				System.out.println(e);
		} catch (KeyStoreException e) {
			if (pin!=null)
				System.out.println(e);
		}
		return false;
	}
	
	
	public static void main(String args[]){
		new DNIeManager().setConfig(CertificateUtils.getDNIeLibraryWindows7());
	}
	
}
