package com.geopista.protocol.ortofoto;

import com.geopista.app.AppContext;
import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.CEnvioOperacion;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.util.ApplicationContext;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.exolab.castor.xml.Marshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.util.Vector;



public class COperacionesOrtofoto {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(COperacionesOrtofoto.class);
	
	static ApplicationContext app= AppContext.getApplicationContext();
    static String url = app.getString("geopista.conexion.servidorurl");


public static CResultadoOperacion enviarOrtofoto(CSolicitudEnvioOrtofoto solicitudEnvioOrtofoto) {
		
		FTPClient ftp = null;
		CResultadoOperacion resultadoOperacion = new CResultadoOperacion();
		try {
			String serverHost = null;
			if (url.lastIndexOf("localhost") != -1) {
				String hostIp  = InetAddress.getLocalHost().getHostAddress();
				serverHost = hostIp;
			} else {
				if (url.contains("https"))
					serverHost = url.substring(8,url.lastIndexOf(":"));
				else
					serverHost = url.substring(7,url.lastIndexOf(":"));
			}
			System.out.println("Enviando la ortofoto a:"+serverHost+" PORT:"+23000);
			ftp = new FTPClient();
			ftp.setRemoteVerificationEnabled(false);
			
			//El puerto aquí está a fuego. Habría que ver como poder cambiarlo.
			ftp.connect(serverHost,23000);
			int reply = ftp.getReplyCode();
			if(!FTPReply.isPositiveCompletion(reply)) {
		        ftp.disconnect();   
		    }
			ftp.enterLocalPassiveMode();
			ftp.login("ortofotos", "ortofotos");
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			//El directorio donde se sube la ortofoto es el directorio ortofotos/temp
			//Es preciso verificar que el directorio existe porque sino fallaría.
			
			File image = solicitudEnvioOrtofoto.getImage();				
			System.out.println("Nombre ortofoto:"+image.getName());
			System.out.println("Path ortofoto:"+image.getPath());
			boolean resultado=ftp.storeFile(image.getName(), new FileInputStream(image));
			System.out.println("Resultado subida ortofoto:"+resultado);
			if (solicitudEnvioOrtofoto.isWorldfileAttached()) {
				File worldfile = solicitudEnvioOrtofoto.getWorldfile();
				ftp.storeFile(worldfile.getName(), new FileInputStream(worldfile));
			}
			ftp.logout();
			
			resultadoOperacion.setResultado(true);
			resultadoOperacion.setDescripcion("La ortofoto se ha subido con éxito al servidor.");
			return resultadoOperacion;
	    } catch(IOException e) {
	    	e.printStackTrace();
	    	resultadoOperacion.setResultado(false);
			resultadoOperacion.setDescripcion("ERROR AL SUBIR LA ORTOFOTO AL SERVIDOR.");
			logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
			logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
			return resultadoOperacion;    	
	    } finally {
			if(ftp.isConnected()) {
			    try {
			        ftp.disconnect();
			    } catch(IOException ioe) {
			    
			    }
			}
	    }		
	}


	public static CResultadoOperacion importarOrtofoto(CSolicitudImportacionOrtofoto solicitudImportacionOrtofoto) {
		
		try {
			CEnvioOperacion envioOperacion = new CEnvioOperacion(CConstantesComando.CMD_ORTHOPHOTO_IMPORT);
			envioOperacion.setSolicitudImportacionOrtofoto(solicitudImportacionOrtofoto);

			CResultadoOperacion resultadoOperacion = sendOverHttp(envioOperacion);

			if (!resultadoOperacion.getResultado()) {
				logger.warn("resultadoOperacion.getResultado(): " + resultadoOperacion.getResultado());
				logger.warn("resultadoOperacion.getDescripcion(): " + resultadoOperacion.getDescripcion());
			}

			return resultadoOperacion;
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new CResultadoOperacion(false, ex.toString());
		}
	}
	
	
	private static CResultadoOperacion sendOverHttp(com.geopista.protocol.CEnvioOperacion comando) {

		try {
            StringWriter sw = new StringWriter();
            Marshaller marshaller = new Marshaller(sw);
            marshaller.setEncoding("ISO-8859-1");
            marshaller.marshal(comando);

			String text = sw.toString();
			logger.info("text: " + text);

            CResultadoOperacion resultadoOperacion = EnviarSeguro.enviar(url+"ortofoto/CServletOrtofoto", text, new Vector());
            
			return resultadoOperacion;
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());

			return new CResultadoOperacion(false, ex.toString());
		}	
	}
	
	public static byte[] getBytesFromFile(File file) 
		throws IOException {
		
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		// You cannot create an array using a long type.
		// It needs to be an int type.
		// Before converting to an int type, check
		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}
}


