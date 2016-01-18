package com.geopista.app.utilidades;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

/**
 * @author SATEC
 * @version $Revision: 1.5 $
 *          <p/>
 *          Autor:$Author: satec $
 *          Fecha Ultima Modificacion:$Date: 2011/07/04 07:01:29 $
 *          $Name:  $
 *          $RCSfile: CUtilidadesComponentes.java,v $
 *          $Revision: 1.5 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CUtilidadesComponentes {

	private static Logger logger = Logger.getLogger(CUtilidadesComponentes.class);

	public static boolean GetURLFile(String urlString, String localFileName, String proxyServer, int proxyPort) {
		BufferedInputStream fileStream = null;
		RandomAccessFile outFile = null;

		try {
			URL theUrl;
            /** charo comenta para hacer una prueba */

			if ((proxyServer.length() > 0) && (proxyPort > 0)) {
				// use HTTP proxy, even for FTP downloads
				theUrl = new URL("http", proxyServer, proxyPort, urlString);
			} else {
				theUrl = new URL(urlString);
			}

			System.out.println("Attempting to connect to " + theUrl);

			// go get the file
			URLConnection con = theUrl.openConnection();

			// if we were able to connect (we would have errored out
			// by now if not), try to get the file.
			// Use a BufferedInputStream instead of a BufferedReader,
			// because a BufferedReader won't retrieve non-text files
			// properly
			fileStream = new BufferedInputStream(con.getInputStream());

			// if we got the remote file, create the local file that
			// we can write the information to
			outFile = new RandomAccessFile(localFileName, "rw");

			System.out.println("Downloading to local file " + localFileName);

			// write to the file in bytes (in case it's not text)
			int howManyBytes;
			byte[] bytesIn = new byte[4096];
			while ((howManyBytes = fileStream.read(bytesIn)) >= 0) {
				outFile.write(bytesIn, 0, howManyBytes);
				//stringBuf.append(bytesIn, 0, howManyBytes); // to send to a StringBuffer
				//System.out.write(bytesIn, 0, howManyBytes);  // to send to the console
			}

			// close up the streams
			fileStream.close();
			outFile.close();

			System.out.println("Finished downloading file to " + localFileName);
			return true;


		} catch (MalformedURLException e) {
			System.out.println("ERROR: Invalid URL: " + urlString);
		} catch (NoRouteToHostException e) {
			System.out.println("ERROR: URL cannot be reached: " + urlString);
		} catch (ConnectException e) {
			System.out.println("ERROR: Connection error: " + e.getMessage());
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException. Fichero/Path no se encuentra en origen/destino.");
		} catch (Exception e) {
			e.printStackTrace();
			//System.err.println(e.getMessage());
		} finally {
			// make sure the streams got closed, in case of an error
			try {
				fileStream.close();
				outFile.close();
			} catch (Exception e) {
			}
		}

		//if we got here, there was some kind of error
		return false;

	}

}
