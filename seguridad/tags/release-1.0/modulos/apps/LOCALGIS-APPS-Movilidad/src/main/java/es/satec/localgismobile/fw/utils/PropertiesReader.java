package es.satec.localgismobile.fw.utils;

import java.io.*;
import java.util.Properties;

import org.apache.log4j.Logger;

import es.satec.localgismobile.fw.Global;

/**
 * Clase que se emplea para leer propiedades incluidas dentro de un fichero .properties
 * @author cgarciar
 *
 */
public class PropertiesReader {
	

	/**
	 * Campo necesario para gestionar los mensajes de log internos de la clase
	 */
	private static Logger log = Global.getLoggerFor(PropertiesReader.class);

	/**
	 * Ruta del archivo de properties a procesar
	 */
	private String ruta_properties;

	/**
	 * Constructor que inicializa el campo ruta_properties de la clase donde se hace referencia
	 * al archivo del que se quieren leer las propiedades
	 * @param ruta_properties
	 */
	public PropertiesReader(String ruta_properties) {
		this(ruta_properties, true);
	}
	
	public PropertiesReader(String ruta_properties, boolean appPathRelative) {
		if (appPathRelative) {
			this.ruta_properties = Global.APP_PATH + File.separator + ruta_properties;
		}
		else {
			this.ruta_properties = ruta_properties;
		}
	}
	
	/**
	 * Indica si el fichero properties existe en la ruta especificada
	 * @return true si el fichero existe; false en caso contrario
	 */
	public boolean fileExists() {
		File file = new File(ruta_properties);
		return file.exists();
	}

	/**
	 * Método para recuperar el valor de una propiedad almacenada dentro de un fichero .properties
	 * @param property Nombre de la propiedad a recuperar
	 * @return Valor de la propiedad recuperada
	 */
	public String getProperty(String property) {
		
		Properties propiedades;

		/* Carga del fichero de propiedades*/
		File conf = new File(ruta_properties);
		FileInputStream f;
		
		try {
			
			f = new FileInputStream(conf);
			propiedades = new Properties();
			
			try {
				propiedades.load(f);
				f.close();
			} catch (IOException e) {
				log.error("Error de E/S procesando un .properties...",e);
			}
			/* Imprimimos los pares clave = valor */
			// propiedades.list(System.out);
			/* Buscamos el valor de la propiedad que nos pasan*/
			String valor = (propiedades.getProperty(property));
			return valor;

		} catch (FileNotFoundException e) {
			log.error("Error: archivo no encontrado...",e);
		}
		
		return null;
	}
	
	public int getPropertyAsInt(String property, int def) {
		try {
			return Integer.parseInt(getProperty(property));
		} catch (Exception e) {
			return def;
		}
	}

	/**
	 * Método para recuperar el objeto Properties del fichero config
	 * @return Objeto Properties con la configuracion
	 */
	public Properties getProperties() {
		/* Carga del fichero de propiedades*/
		File conf = new File(ruta_properties);
		FileInputStream f;
		Properties propiedades = new Properties();
		
		try {
			f = new FileInputStream(conf);
			propiedades.load(f);
			f.close();
			return propiedades;
		} catch (FileNotFoundException e) {
			log.error("Error: archivo no encontrado...",e);
		} catch (IOException e) {
			log.error("Error de E/S procesando un .properties...",e);
		}
		
		return null;
	}
	
}
