/**
 * EncriptarPassword.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.administrador;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;



/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 04-jun-2004
 * Time: 11:50:43
 */
public class EncriptarPassword {
	
		private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(EncriptarPassword.class);

        final static String USAGE = "Usage: java EncriptarPassword -e|-d password";
        Cipher ecipher=null;
        Cipher dcipher=null;
        SecretKey key;
        //private final String KEY_GEN_TRANS="DES";
        
        public static final int NO_ALGORITHM=-1;
        public static final int TYPE1_ALGORITHM=1;
        public static final int TYPE2_ALGORITHM=2;

        public static final int TYPE1_ALGORITHM_SPECIAL=3;

        
        private final String STR_TYPE1_ALGORITHM="DES";
        private final String STR_TYPE2_ALGORITHM="AES";
        
        private String recoverPassword=null;
        private int selectedAlgorithm=NO_ALGORITHM;
             
    public SecretKey createKey(int algorithm)
            throws InvalidKeyException, InvalidKeySpecException, IOException,
            NoSuchAlgorithmException {
        
        final char[] passwordChar = "GEOPISTA".toCharArray();    
        byte[] passwordBytes = "GEOPISTA".getBytes("UTF8");
        final byte[] salt = "a9v5n38s".getBytes();
        
        SecretKey secret=null;
        if ((algorithm==TYPE1_ALGORITHM) || (algorithm==TYPE1_ALGORITHM_SPECIAL)){
        	DESKeySpec keyspec = new DESKeySpec(passwordBytes);
        	SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(STR_TYPE1_ALGORITHM);
        	secret = keyfactory.generateSecret(keyspec);
        }
        else if (algorithm == TYPE2_ALGORITHM){
        	SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(passwordChar, salt, 1024, 128);
            SecretKey tmp = factory.generateSecret(spec);
            secret = new SecretKeySpec(tmp.getEncoded(), STR_TYPE2_ALGORITHM);
        }
   
        	
        return secret;
    }
    
    public EncriptarPassword(String password) {
    	String typeAlgorithm=null;
    	
		if ((password!=null) && (password.length()>7)){
				typeAlgorithm=password.substring(0,7);
				if ((typeAlgorithm!=null) && (typeAlgorithm.equals("{TYPE1}")) || 
						((typeAlgorithm!=null) && (typeAlgorithm.equals("{TYPE2}")))||
						((typeAlgorithm!=null) && (typeAlgorithm.equals("{TYPE0}")))){	
					recoverPassword=password.substring(7);
				}
				else{
					recoverPassword=password;
				}
		}
		else{
			recoverPassword=password;
		}
		if ((typeAlgorithm!=null) && (typeAlgorithm.equals("{TYPE1}"))){
			this.selectedAlgorithm = TYPE1_ALGORITHM;
			createCyphers(TYPE1_ALGORITHM);
		}
		else if ((typeAlgorithm!=null) && (typeAlgorithm.equals("{TYPE2}"))){
			this.selectedAlgorithm = TYPE2_ALGORITHM;
			createCyphers(TYPE2_ALGORITHM);				
		}
		else if ((typeAlgorithm!=null) && (typeAlgorithm.equals("{TYPE0}"))){
			this.selectedAlgorithm = NO_ALGORITHM;
			createCyphers(NO_ALGORITHM);				
		}
		else{
			//TODO. Las password cifradas originariamente estan con DES. Esto hay que mantenerlo.
			this.selectedAlgorithm = TYPE1_ALGORITHM;
			createCyphers(TYPE1_ALGORITHM);
			//this.selectedAlgorithm = NO_ALGORITHM;
			//createCyphers(NO_ALGORITHM);
			
		}
    }
        
    
	public EncriptarPassword(int algorithm) {
		this.selectedAlgorithm = algorithm;
		createCyphers(algorithm);
  }
   
   /**
    * Create los ciphers
    * @param algorithm
    */
  private void createCyphers(int algorithm){
	  try {
		if (algorithm!=NO_ALGORITHM){
				if (algorithm==TYPE1_ALGORITHM){
		            ecipher = Cipher.getInstance(STR_TYPE1_ALGORITHM);
		            dcipher = Cipher.getInstance(STR_TYPE1_ALGORITHM);
		            key=  createKey(TYPE1_ALGORITHM);
				}
				else if (algorithm==TYPE2_ALGORITHM){
					  ecipher = Cipher.getInstance(STR_TYPE2_ALGORITHM);
		            dcipher = Cipher.getInstance(STR_TYPE2_ALGORITHM);
		            key=  createKey(TYPE2_ALGORITHM);
				}
				else if (algorithm==TYPE1_ALGORITHM_SPECIAL){
					  ecipher = Cipher.getInstance(STR_TYPE1_ALGORITHM);
		            dcipher = Cipher.getInstance(STR_TYPE1_ALGORITHM);
		            key=  createKey(TYPE1_ALGORITHM);
				}
				
		      ecipher.init(Cipher.ENCRYPT_MODE, key);
		      dcipher.init(Cipher.DECRYPT_MODE, key);
		}
	} catch (InvalidKeyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NoSuchPaddingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvalidKeySpecException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }

  public String encriptar(String strPassword) throws Exception
  {
    try {
           byte[] utf8 = strPassword.getBytes("UTF8");
           // Encrypt
           if (ecipher!=null){
	           byte[] enc = ecipher.doFinal(utf8);
	           // Encode bytes to base64 to get a string
	           String cypherPassword=new String(Base64.encodeBase64(enc));
	           if (selectedAlgorithm==TYPE1_ALGORITHM)
	        	   cypherPassword="{TYPE1}"+cypherPassword;
	           else if (selectedAlgorithm==TYPE2_ALGORITHM)
	        	   cypherPassword="{TYPE2}"+cypherPassword;
	           else if (selectedAlgorithm==TYPE1_ALGORITHM_SPECIAL)
	        	   cypherPassword=cypherPassword;
	           return cypherPassword;
           }
           return strPassword;
           } catch (javax.crypto.BadPaddingException e) {
           } catch (IllegalBlockSizeException e) {
           } catch (UnsupportedEncodingException e) {
           } catch (Exception e) {
               throw e;
           }
           return null;
  }


   public String desencriptar(String str)throws Exception
   {
           byte[] dec = Base64.decodeBase64(str);
           byte[] utf8 = dcipher.doFinal(dec);
           return new String(utf8, "UTF8");
   }
   
   public String desencriptar()throws Exception
   {
           try {
			byte[] dec = Base64.decodeBase64(recoverPassword);
			   byte[] utf8 = dcipher.doFinal(dec);
			   return new String(utf8, "UTF8");
		} catch (Exception e) {
			return recoverPassword;
		}
   }
   
   
   
     public static void main(String[] args) {
        try {
            if (args.length<2)
            {
                System.err.println(USAGE);
                System.exit(-1);
            }
            EncriptarPassword ep = new EncriptarPassword(-1);
            if (args[0].equals("-e")) { // generate key
                System.out.print("Encriptando password ... ");
                System.out.flush();
                String sPassword  = ep.encriptar(args[1]);                
                System.out.println("completado.");
            } else if (args[0].equals("-d")) { // encrypt stdin -> stdout
               System.out.print("Desencriptando password ... ");
                System.out.flush();
                String sPassword  = ep.desencriptar(args[1]);
                System.out.println("completado.");
            } else if (args[0].equals("-ed")) { // decrypt stdin -> stdout
                System.out.print("Encriptando password ... ");
                System.out.flush();
                String sPassword  = ep.encriptar(args[1]);
                System.out.println("completado.");
                System.out.print("Desencriptando password ... ");
                System.out.flush();
                sPassword  = ep.desencriptar(sPassword);
                System.out.println("completado.");
             }
             else
            {
                System.err.println(USAGE);
            }
        } catch(Exception e) {
             java.io.StringWriter sw=new java.io.StringWriter();
		    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	e.printStackTrace(pw);
            System.err.println(sw.toString());
            System.err.println(USAGE);
            System.exit(-1);
        }
    }
}



