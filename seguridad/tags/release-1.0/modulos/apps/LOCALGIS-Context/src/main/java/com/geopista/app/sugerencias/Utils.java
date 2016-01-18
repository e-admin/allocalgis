package com.geopista.app.sugerencias;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.activation.MimetypesFileTypeMap;


public class Utils {
	
	public static byte[] readResource(String name) throws IOException {
		InputStream is = Utils.class.getClassLoader().getResourceAsStream(name);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[512];
		int read = -1;
		while ((read = is.read(buffer)) != -1) {
			baos.write(buffer, 0, read);
		}
		return baos.toByteArray();
	}
	
	public static byte[] readFile(File file) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[1024];
		int read = -1;
		while ((read = fis.read(buffer)) != -1) {
			baos.write(buffer, 0, read);
		}
		return baos.toByteArray();
	}
	
	public static String getMimeType(File file) {
		return new MimetypesFileTypeMap().getContentType(file);
	}
	
	public static String getMimeType(String string) {
		return new MimetypesFileTypeMap().getContentType(string);
	}
	
}