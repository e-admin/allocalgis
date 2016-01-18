package com.geopista.coordsys.ed50toetrs89.info;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.jar.JarFile;

/**
 * This class implements some utilities methods for coordsys.
 * 
 * @author javieraragon
 *
 */
public class UtilsToReadGridFile {

	/**
	 * Reads a file identifier parameter as a String from a grid file.
	 * @param buf {@link RandomAccessFile} to read.
	 * @return identifier parameter.
	 */
	public static String readId (RandomAccessFile buf){
		String id_readed = "";

		int n = 8;
		byte[] r = new byte[n];
		try {
			buf.read(r);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		//To Char
		for(int i= 0; i < n; i++){
			char c = (char)(r[i] & 0xFF);
			id_readed = id_readed + c;
		}


		return id_readed;
	}

	
	/**
	 * Reads a integer parameter as a String from a grid file.
	 * @param buf {@link RandomAccessFile} to read.
	 * @return identifier parameter.
	 */
	public static  int readIntegerBigEndian (RandomAccessFile buf){
		int result;
		int n = 4;
		byte[] r = new byte[n];
		try {
			buf.read(r);
		} catch (IOException e) {
			e.printStackTrace();
		}

		result = r[0];

		// read padding before exit
		try {
			buf.read(r);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Reads a float parameter as a long from a grid file.
	 * @param buf {@link RandomAccessFile} to read.
	 * @return identifier parameter.
	 */
	public static  long readFlotante (RandomAccessFile buf){
		int n = 4;
		byte[] r = new byte[n];
		try {
			buf.read(r);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return converByteArrayToLong(r);
	}

	/**
	 * Reads a chain caracter parameter as a char[] from a grid file.
	 * @param buf {@link RandomAccessFile} to read.
	 * @return identifier parameter.
	 */
	public static  char[] readChain (RandomAccessFile buf){
		int n = 8;
		char[] readed = new char[8];
		byte[] r = new byte[n];
		try {
			buf.read(r);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		for(int i= 0; i < n; i++){
			readed[i]= (char)(r[i] & 0xFF);
		}

		return readed;
	}

	/**
	 * Reads a double parameter as a double from a grid file.
	 * @param buf {@link RandomAccessFile} to read.
	 * @return identifier parameter.
	 */
	public static  Double readDouble (RandomAccessFile buf){
		int n = 8;
		byte[] r = new byte[n];
		try {
			buf.read(r);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return convertByteArrayToDouble(r);
	}

	/**
	 * Reads a long (represented by 4 Bytes) parameter as a String from a grid file.
	 * @param buf {@link RandomAccessFile} to read.
	 * @return identifier parameter.
	 */
	public static Long readLong (RandomAccessFile buf){
		Long result;
		int n = 4;
		byte[] r = new byte[n];
		try {
			buf.read(r);
		} catch (IOException e) {
			e.printStackTrace();
		}

		result = converByteArrayToLong(r);

		// read padding before exit
		try {
			buf.read(r);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}


	/**
	 * Read a integer parameter as a String from a grid file.
	 * @param buf {@link RandomAccessFile} to read.
	 * @return identifier parameter.
	 */
	public static double convertByteArrayToDouble (byte[] arr) {
		int i =0;
		int cnt = 0;
		byte[] tmp = new byte[8];
		for ( i = 0; i < arr.length; i++) {
			tmp[cnt] = arr[i];
			cnt++;
		}
		long accum = 0;
		i = 0;
		for ( int shiftBy = 0; shiftBy < 64; shiftBy += 8 ) {
			accum |= ( (long)( tmp[i] & 0xff ) ) << shiftBy;
			i++;
		}
		return Double.longBitsToDouble(accum);
	}

	
	/**
	 * Converts a byte array to a long number.
	 * @param arr byte array.
	 * @return long number.
	 */
	public static long converByteArrayToLong (byte[] arr) {

		int i = 0;
		int cnt = 0;
		byte[] tmp = new byte[arr.length];
		for (i = 0; i < arr.length; i++) {
			tmp[cnt] = arr[i];
			cnt++;
		}
		long accum = 0;
		i = 0;
		for ( int shiftBy = 0; shiftBy < 32; shiftBy += 8 ) {
			accum |= ( (long)( tmp[i] & 0xff ) ) << shiftBy;
			i++;
		}
		return accum;
	}

	/**
	 * Converts a byte array that represents a float number to a float. Indicates the start position to read the number
	 * @param arr array byte to convert.
	 * @param start a start position from the array.
	 * @return a float number.
	 */
	public static float byteArrayToFloat (byte[] arr, int start) {
		int i = 0;
		int cnt = 0;
		byte[] tmp = new byte[arr.length];
		for (i = start; i < (start + arr.length); i++) {
			tmp[cnt] = arr[i];
			cnt++;
		}
		int accum = 0;
		i = 0;
		for ( int shiftBy = 0; shiftBy < 32; shiftBy += 8 ) {
			accum |= ( (long)( tmp[i] & 0xff ) ) << shiftBy;
			i++;
		}
		return Float.intBitsToFloat(accum);
	}


	/**
	 * Copy a file located into jar to a destination file.
	 * @param srFile jar file.
	 * @param pathInsideJar the path where is located the file to copy inside jar file.
	 * @param dtFile the destination file
	 * @return true if the file has been copied successfully and false in any other case.
	 */
	public static boolean copyFileFromJarToFile(JarFile srFile,String pathInsideJar, File dtFile){
		InputStream in = null;
		OutputStream out = null;
		try{
			in = srFile.getInputStream(srFile.getEntry(pathInsideJar));
			out = new FileOutputStream(dtFile);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0){
				out.write(buf, 0, len);
			}
			return true;
		}
		catch(FileNotFoundException ex){
			System.out.println(ex.getMessage());
			return false;
		}
		catch(IOException e){
			System.out.println(e.getMessage());
			return false;
		}
		finally{
			try{
				in.close();
			}catch(Exception e){e.printStackTrace();}
			try{
				out.close();
			}catch(Exception e){e.printStackTrace();}
		}
	}

	/**
	 * Copy a source file to a destination file.
	 * @param srFile the source file.
	 * @param dtFile the destination file
	 * @return true if the file has been copied successfully and false in any other case.
	 */
	public static boolean copyfile(File srFile, File dtFile){
		InputStream in = null;
		OutputStream out = null;
		try{
			in = new FileInputStream(srFile);
			out = new FileOutputStream(dtFile);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0){
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			return true;
		}
		catch(FileNotFoundException ex){
			System.out.println(ex.getMessage());
			return false;
		}
		catch(IOException e){
			System.out.println(e.getMessage());
			return false;
		}
		finally{
			try{
				in.close();
			}catch(Exception e){e.printStackTrace();}
			try{
				out.close();
			}catch(Exception e){e.printStackTrace();}
		}
	}

	/**
	 * Copy a file from a {@link URL} to a destination file.
	 * @param url the URL source file.
	 * @param dtFile the destination file
	 * @return true if the file has been copied successfully and false in any other case.
	 */
	public static boolean copyfile(URL url,
			File dtFile) {
		// TODO Auto-generated method stub
		InputStream in = null;
		OutputStream out = null;
		try{
			in = url.openStream();
			out = new FileOutputStream(dtFile);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0){
				out.write(buf, 0, len);
			}
			return true;
		}
		catch(FileNotFoundException ex){
			System.out.println(ex.getMessage() + " in the specified directory.");
			return false;
		}
		catch(IOException e){
			System.out.println(e.getMessage());
			return false;
		}finally{
			try{
				in.close();
			}catch(Exception e){e.printStackTrace();}
			try{
				out.close();
			}catch(Exception e){e.printStackTrace();}
		}
	}


}
