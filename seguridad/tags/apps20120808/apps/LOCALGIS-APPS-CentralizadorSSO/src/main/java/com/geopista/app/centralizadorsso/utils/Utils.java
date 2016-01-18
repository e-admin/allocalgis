package com.geopista.app.centralizadorsso.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Window;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.apache.log4j.Logger;

/**
 * 
 * @author dcaaveiro
 */
public class Utils {

	static Logger logger = Logger.getLogger(Utils.class);

	private final static String APPS_FOLDER = "/LocalGISapps";

	/*
	 * rutaApp: la ruta de jnlp Si existe en la carpeta /LocalGISapps el jnlp lo
	 * abre Si no existe llama a la ruta web del jnlp, lo descarga en la carpeta
	 * /LocalGISapps y lo abre
	 */
	public static void openDesktopApp(String rutaApp) {
		try {
			if (!new File(APPS_FOLDER).exists())
				new File(APPS_FOLDER).mkdirs();
			URL url = new URL(rutaApp);
			String rutaFichero = APPS_FOLDER
					+ url.getFile().substring(url.getFile().lastIndexOf("/"));
			if (!new File(rutaFichero).exists()) {		
			 	URLConnection conexion = url.openConnection();
				InputStream stream = conexion.getInputStream();
				BufferedInputStream in = new BufferedInputStream(stream);
				FileOutputStream ficheroStream = new FileOutputStream(
						rutaFichero);
				BufferedOutputStream out = new BufferedOutputStream(
						ficheroStream);
				int i;
				while ((i = in.read()) != -1) {
					out.write(i);
				}
				out.flush(); 
			}
		/* Se desactiva el Modo Desktop para que funcione sin permisos de Administrador en Windows 7, actualmente no funciona por un bug 
			logger.info("Intentando Desktop Mode");
			Desktop.getDesktop().open(new File(rutaFichero));
		*/	
			// Se activa el Modo Tradicional para que funcione sin permisos de Administrador en Windows 7, actualmente no funciona por un bug 
			try{
				String command="javaws "+ rutaApp;
				logger.info("Intentando Traditional Mode:"+command);
				Runtime.getRuntime().exec(command);
			}
			catch(Exception e){
				e.printStackTrace();
				logger.info("Intentando Desktop Mode");
				Desktop.getDesktop().open(new File(rutaFichero));
			}
			
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		catch (Throwable e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/*
	 * rutaApp: la ruta de laaplicación web idSesion: el identificador de la
	 * sesión Abre el navegador por defecto del sistema enviandole la ruta de la
	 * aplicación junto con el identificador de sesión como parámetro
	 */
	public static void openWebApp(String rutaApp, String idSesion) {
		try {
			Desktop.getDesktop().browse(
					new URI(rutaApp + "?ssoActive=true&idSesion=" + idSesion));
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (URISyntaxException e) {
			logger.error(e.getMessage());
		}
	}

	public static void centreComponent(Component componentToMove,
			Component componentToCentreOn) {
		try {
			Dimension componentToCentreOnSize = componentToCentreOn.getSize();
			componentToMove
					.setLocation(
							componentToCentreOn.getX()
									+ ((componentToCentreOnSize.width - componentToMove
											.getWidth()) / 2),
							componentToCentreOn.getY()
									+ ((componentToCentreOnSize.height - componentToMove
											.getHeight()) / 2));
		} catch (Exception e) {
		}
	}
	
}
