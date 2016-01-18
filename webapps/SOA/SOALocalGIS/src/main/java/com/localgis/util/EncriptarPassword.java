/**
 * EncriptarPassword.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.util;


import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;



/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 04-jun-2004
 * Time: 11:50:43
 */
public class EncriptarPassword {
        final static String USAGE = "Usage: java EncriptarPassword -e|-d password";
        Cipher ecipher;
        Cipher dcipher;
        SecretKey key;
        private final String KEY_GEN_TRANS="DES";

    public SecretKey createKey()
            throws InvalidKeyException, InvalidKeySpecException, IOException,
            NoSuchAlgorithmException {
        String semilla = "GEOPISTA";
        byte[] rawkey = semilla.getBytes("UTF8");
        DESKeySpec keyspec = new DESKeySpec(rawkey);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(KEY_GEN_TRANS);
        SecretKey key = keyfactory.generateSecret(keyspec);
        return key;
    }
   public EncriptarPassword() {
    try {
                ecipher = Cipher.getInstance("DES");
                dcipher = Cipher.getInstance("DES");
                key=  createKey();
                ecipher.init(Cipher.ENCRYPT_MODE, key);
                dcipher.init(Cipher.DECRYPT_MODE, key);
                //System.out.println(key.toString());
            } catch (javax.crypto.NoSuchPaddingException e) {
            } catch (java.security.NoSuchAlgorithmException e) {
            } catch (java.security.InvalidKeyException e) {
            } catch (Exception e) {
            }

  }

  public String doEncrip(String strPassword) throws Exception
  {
    try {
           byte[] utf8 = strPassword.getBytes("UTF8");
           // Encrypt
           byte[] enc = ecipher.doFinal(utf8);
           // Encode bytes to base64 to get a string
            return new sun.misc.BASE64Encoder().encode(enc);
           } catch (javax.crypto.BadPaddingException e) {
           } catch (IllegalBlockSizeException e) {
           } catch (UnsupportedEncodingException e) {
           } catch (Exception e) {
               throw e;
           }
           return null;
  }


   public String undoEncrip(String  str)throws Exception
   {
           byte[] dec = new
           sun.misc.BASE64Decoder().decodeBuffer(str);
           byte[] utf8 = dcipher.doFinal(dec);
           return new String(utf8, "UTF8");
   }
     public static void main(String[] args) {
        try {
            if (args.length<2)
            {
                System.err.println(USAGE);
                System.exit(-1);
            }
            EncriptarPassword ep = new EncriptarPassword();
            if (args[0].equals("-e")) { // generate key
                System.out.print("Encriptando password ... ");
                System.out.flush();
                String sPassword  = ep.doEncrip(args[1]);
                System.out.println(sPassword);
                System.out.println("completado.");
            } else if (args[0].equals("-d")) { // encrypt stdin -> stdout
               long t1= System.currentTimeMillis();
            	
            	
            	System.out.print("Desencriptando password ... ");
                System.out.flush();
                String sPassword  = ep.undoEncrip(args[1]);
                System.out.println(sPassword);
                System.out.println("completado.");
                long t2 = System.currentTimeMillis();
                System.out.println (t2-t1);
                
            } else if (args[0].equals("-ed")) { // decrypt stdin -> stdout
                System.out.print("Encriptando password ... ");
                System.out.flush();
                String sPassword  = ep.doEncrip(args[1]);
                System.out.println(sPassword);
                System.out.println("completado.");
                System.out.print("Desencriptando password ... ");
                System.out.flush();
                sPassword  = ep.undoEncrip(sPassword);
                System.out.println(sPassword);
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



