package com.geopista.app.inventario.sicalwin.util;

import gnu.crypto.hash.Sha160;
import gnu.crypto.hash.Sha512;
import gnu.crypto.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DecodeUtils {

	

	/*
	 * codificacion: "SHA-512" "SHA-256" "MD5" "SHA" SHA-1" "SHA-384"
	 */
	public static String codificar(String origen, String codificacion)
			throws NoSuchAlgorithmException {
		MessageDigest md;
		String out = "";
		try {
			md = MessageDigest.getInstance(codificacion);
			md.update(origen.getBytes());
			byte[] mb = md.digest();
			for (int i = 0; i < mb.length; i++) {
				byte temp = mb[i];
				String s = Integer.toHexString(new Byte(temp));
				while (s.length() < 2) {
					s = "0" + s;
				}
				s = s.substring(s.length() - 2);
				out += s;
			}
		} catch (NoSuchAlgorithmException e) {
			System.out.println("ERROR: " + e.getMessage());
		}
		return out;
	}

	public static String codificarSha1Base64(String origen) {
		Sha160 hash = new Sha160();
		hash.update(origen.getBytes(), 0, origen.getBytes().length);
		return Base64.encode(hash.digest());
	}

	public static String codificarSha512Base64(String origen) {
		Sha512 hash = new Sha512();
		hash.update(origen.getBytes(), 0, origen.getBytes().length);
		return Base64.encode(hash.digest());
	}

	public static String base64(String origen) {
		return Base64.encode(origen.getBytes());
	}

	public static byte[] decodificarBase64(String origen) {
		try {
			return Base64.decode(origen);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}
}
