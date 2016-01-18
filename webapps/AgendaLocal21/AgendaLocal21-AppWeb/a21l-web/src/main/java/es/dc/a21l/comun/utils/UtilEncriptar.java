/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.comun.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.internet.MimeUtility;

public abstract class UtilEncriptar {
	private final static String keyBuffer = "56af65d2";

	public static byte[] encode(byte[] b) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStream b64os = MimeUtility.encode(baos, "base64");
		b64os.write(b);
		b64os.close();
		return baos.toByteArray();
	}

	public static byte[] decode(byte[] b) throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		InputStream b64is = MimeUtility.decode(bais, "base64");
		byte[] tmp = new byte[b.length];
		int n = b64is.read(tmp);
		byte[] res = new byte[n];
		System.arraycopy(tmp, 0, res, 0, n);
		return res;
	}

	private static SecretKeySpec getKey() {
		// keyBuffer = keyBuffer.substring(0, 8);
		SecretKeySpec key = new SecretKeySpec(keyBuffer.getBytes(), "DES");
		return key;
	}

	public static String desencripta(String s) throws Exception {
		String s1 = null;
		s = s.replace(' ', '+');
		
		if (s.indexOf("{A21L}") != -1) 
		{
			String s2 = s.substring("{A21L}".length());
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(2, getKey());
			byte abyte0[] = cipher.doFinal(decode(s2.getBytes()));
			s1 = new String(abyte0);
		}
		else 
		{
			s1 = s;
		}
		
		return (s1 != null ? s1.trim() : s1);
	}

	public static String encripta(String s) throws Exception 
	{
		byte abyte0[];
		SecureRandom securerandom = new SecureRandom();
		securerandom.nextBytes(keyBuffer.getBytes());
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipher.init(1, getKey());
		abyte0 = encode(cipher.doFinal(s.getBytes())); // antes
		
		return ("{A21L}" + new String(abyte0)).trim();
	}
}
