package es.satec.localgismobile.proxyserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.japisoft.fastparser.Parser;
import com.japisoft.fastparser.document.Document;
import com.japisoft.fastparser.dom.DomNodeFactory;


import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.Messages;
import es.satec.localgismobile.fw.net.communications.HttpManager;
import es.satec.localgismobile.fw.net.communications.exceptions.NoConnectionException;
import es.satec.localgismobile.fw.validation.exceptions.LoginException;
import es.satec.localgismobile.ui.utils.DownloadProjectEntry;
import es.satec.localgismobile.utils.LocalGISUtils;

public class LocalGISProxyServer {
	
	private static Logger logger = Global.getLoggerFor(LocalGISProxyServer.class);

	/**
	 * Hace la peticion al servidor para obtener el listado de mapas disponibles.
	 */
	public static Document listRemoteLocalGISMaps(String userId) throws LoginException, NoConnectionException, Exception {

		String url = Config.prLocalgis.getProperty(Config.PROPERTY_SERVER_PROTOCOL) + "://" +
			Config.prLocalgis.getProperty(Config.PROPERTY_SERVER_HOST) + ":" +
			Config.prLocalgis.getPropertyAsInt(Config.PROPERTY_SERVER_PORT, 80) +
			Config.prLocalgis.getProperty(Config.PROPERTY_SERVER_CONTEXT) +
			Config.prLocalgis.getProperty(Config.PROPERTY_LIST_MAPS_QUERY) + "&userId=" + userId;
		
		ByteArrayInputStream bais = null;
		try {
			HttpManager httpManager = new HttpManager(url, "GET", null, false, null);
			byte[] respuesta;
			respuesta = httpManager.enviarYRecibir(null);

			bais = new ByteArrayInputStream(respuesta);
			Parser p = new Parser();
			p.setNodeFactory(new DomNodeFactory());
			p.setInputStream(bais);
			p.parse();
			Document d = p.getDocument();
			return d;
		} catch (LoginException e) {
			throw e;
		} catch (NoConnectionException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			if (bais != null) bais.close();
		}
	}
	
	/**
	 * Hace la peticion al servidor para descargar un proyecto.
	 * @param shell 
	 */
	public static void downloadProject(DownloadProjectEntry dateProject, Shell shell) throws LoginException, NoConnectionException, Exception {
		/*Descargamos en disco el Zip*/
		String url = Config.prLocalgis.getProperty(Config.PROPERTY_SERVER_PROTOCOL) + "://" +
			Config.prLocalgis.getProperty(Config.PROPERTY_SERVER_HOST) + ":" +
			Config.prLocalgis.getPropertyAsInt(Config.PROPERTY_SERVER_PORT, 80) +
			Config.prLocalgis.getProperty(Config.PROPERTY_SERVER_CONTEXT) +
			Config.prLocalgis.getProperty(Config.PROPERTY_DOWNLOAD_MAP_QUERY) +
			"&projectId=" + dateProject.getIdProyecto();                  
		
		String file = Global.APP_PATH + File.separator + Config.prLocalgis.getProperty("proyectos.ruta")+File.separator+Config.prLocalgis.getProperty(Config.PROPERTY_MAP_FILE);
		File fileRutaZip=new File(Global.APP_PATH + File.separator + Config.prLocalgis.getProperty("proyectos.ruta"));
		if(!fileRutaZip.isDirectory() & !fileRutaZip.exists()){
			fileRutaZip.mkdirs();
		}
		FileOutputStream fos = null;
		try {
			// Escribe el mapa en el fichero indicado
			HttpManager httpManager = new HttpManager(url, "GET", null, false, null);
			byte[] respuesta = httpManager.enviarYRecibir(null);
			fos = new FileOutputStream(file);
			fos.write(respuesta);

		} catch (LoginException e) {
			throw e;
		} catch (NoConnectionException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			if (fos != null) fos.close();
		}
		
		/*Compruebo si el directorio que vamos a crear en disco existe previamente*/
		String nombreDirectorio=dateProject.getNombreProyecto()+"."+dateProject.getIdProyecto();
		File directorioDescargaProyecto= new File(Global.APP_PATH + File.separator + Config.prLocalgis.getProperty("proyectos.ruta")+File.separator+nombreDirectorio);
		if(directorioDescargaProyecto.isDirectory()){
			MessageBox mb = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK |SWT.CANCEL);
			mb.setMessage(Messages.getMessage("LocalGISProxyServer_DirectorioRepetido"));
			if(mb.open()==SWT.OK){
				unZip(file, nombreDirectorio);
			}
		}
		else{
			if(directorioDescargaProyecto.mkdirs()){
				unZip(file, nombreDirectorio);
				
			}
			else{
				logger.error(Messages.getMessage("LocalGISProxyServer_errorCrearDirectorio"));
			}
		}
		//Borramos el fichero zip descargado.
		boolean resultado=LocalGISUtils.deleteDir(new File(file));
		logger.info("Fichero descargado borrado:"+resultado);
	}
	
	private static void unZip(String fileUnZip, String nombreDirectorio) {

		int BUFFER = 2048;
		BufferedOutputStream dest = null;
        BufferedInputStream is = null;
        ZipEntry entry;
        //long ini=System.currentTimeMillis();
        ZipFile zipfile=null;
		try {
			zipfile = new ZipFile(fileUnZip);
			 Enumeration e = zipfile.entries();
		        while(e.hasMoreElements()) {
		           entry = (ZipEntry) e.nextElement();
		           String nombreZipdecode=java.net.URLDecoder.decode(entry.getName(),"UTF-8");
		           
		           if(!entry.isDirectory()) {
			           is = new BufferedInputStream(zipfile.getInputStream(entry));
			           int count;
			           byte data[] = new byte[BUFFER];
			           FileOutputStream fos2;
						fos2 = new FileOutputStream(Global.APP_PATH + File.separator + Config.prLocalgis.getProperty("proyectos.ruta")+File.separator+nombreDirectorio+File.separator+nombreZipdecode);
					
			           dest = new BufferedOutputStream(fos2, BUFFER);
			           while ((count = is.read(data, 0, BUFFER)) != -1) {
			              dest.write(data, 0, count);
			           }
			           dest.flush();
			           dest.close();
			           is.close();
		           }
		           else{
		        	   File f=new File(Global.APP_PATH + File.separator + Config.prLocalgis.getProperty("proyectos.ruta")+File.separator+nombreDirectorio+File.separator+nombreZipdecode);
		        	   f.mkdir();
		           }
		        }
		} catch (FileNotFoundException e1) {
			
			logger.error(Messages.getMessage("LocalGISProxyServer_errorDirectorio")+e1);
		}catch (IOException e1) {
			
			logger.error(Messages.getMessage("LocalGISProxyServer_errorUnZip")+e1.toString());
		}
		finally{
			if (zipfile!=null)
				try {
					zipfile.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
		}
        //long fin=System.currentTimeMillis();
        //System.out.println("valor fin. "+(fin-ini));
	}

	public static void main(String args[]) {
		try {
			System.out.println(LocalGISProxyServer.listRemoteLocalGISMaps(""));
			//LocalGISProxyServer.downloadProject(new DownloadProjectEntry(),);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
