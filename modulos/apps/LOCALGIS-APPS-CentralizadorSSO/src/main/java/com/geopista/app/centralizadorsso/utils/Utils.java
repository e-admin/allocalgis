/**
 * Utils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.centralizadorsso.utils;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.geopista.app.UserPreferenceConstants;
import com.geopista.util.config.UserPreferenceStore;

/**
 * 
 * @author dcaaveiro
 */
public class Utils {

	static Logger logger = Logger.getLogger(Utils.class);

	private final static String APPS_FOLDER = UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,UserPreferenceConstants.DEFAULT_DATA_PATH, true) + "LocalGISapps";
	//private final static String APPS_FOLDER = "/LocalGISapps";
	/**
	 * rutaApp: la ruta de jnlp Si existe en la carpeta /LocalGISapps el jnlp lo
	 * abre Si no existe llama a la ruta web del jnlp, lo descarga en la carpeta
	 * 
	 */
	public static void openDesktopApp(String rutaApp) {
		try {
			if (!new File(APPS_FOLDER).exists())
				new File(APPS_FOLDER).mkdirs();
			URL url = new URL(rutaApp);
			logger.info("Ruta de la aplicacion:" + rutaApp);
			logger.info("Ruta de Aplicaciones:"+APPS_FOLDER);
			String rutaFichero = APPS_FOLDER
					+ "\\"+url.getFile().substring((url.getFile().lastIndexOf("/"))+1);
			logger.info("Ruta fichero:" + rutaFichero);
			if (!new File(rutaFichero).exists()) {
				logger.info("Descargando fichero a local");
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
				out.close();
			}			
			/*
			 * Se desactiva el Modo Desktop para que funcione sin permisos de
			 * Administrador en Windows 7, actualmente no funciona por un bug
			 * logger.info("Intentando Desktop Mode");
			 * Desktop.getDesktop().open(new File(rutaFichero));
			 */
			// Se activa el Modo Tradicional para que funcione sin permisos de
			// Administrador en Windows 7, actualmente no funciona por un bug
			try {
				String command = "javaws " + rutaApp;
				logger.info("Intentando Traditional Mode:" + command);
				Runtime.getRuntime().exec(command);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("Intentando Desktop Mode");
				Desktop.getDesktop().open(new File(rutaFichero));
			}
			//new File(APPS_FOLDER).delete();

		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (Throwable e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
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
