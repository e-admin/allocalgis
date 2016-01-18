package es.satec.localgismobile.session;

import java.io.File;
import java.util.MissingResourceException;

import org.apache.log4j.Logger;

import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.utils.PropertiesReader;

public class CellPermissionsBean {

	/**
	 * Fichero de properties con los permisos que habrá en cada celda
	 */
	private PropertiesReader prPermisos;

	private Logger logger = (Logger) Logger.getInstance(CellPermissionsBean.class);
	
	public CellPermissionsBean(String nombreArchivoPath, String nombreArchivoPermisosCeldas){
		try{
			prPermisos = new PropertiesReader(Config.prLocalgis.getProperty("proyectos.ruta")+File.separator+nombreArchivoPath+File.separator+nombreArchivoPermisosCeldas);
		}
		catch(Exception e){
			logger.error(e);
		}
	}
		
	public boolean permisoUsuCelda(String celda, String Usuario){
		boolean permiso=false;
		try{
			String valor=prPermisos.getProperty(celda);
			if(valor!=null){
				if(valor.equals(Usuario)){
					permiso=true;
				}
			}
		} catch (MissingResourceException e) {
			logger.error(e);
		}
			
		return permiso;
	}
}
