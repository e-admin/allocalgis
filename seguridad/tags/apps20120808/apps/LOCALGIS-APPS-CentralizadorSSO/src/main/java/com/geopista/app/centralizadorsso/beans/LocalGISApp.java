package com.geopista.app.centralizadorsso.beans;

/**
*
* @author  dcaaveiro
*/
public class LocalGISApp {
	
	/*
	 * nombreAcl: Rol de acceso (relacionado con el entorno de trabajo del administrador de cartografía)
	 * permisoApp: Permiso mínimo para acceder a la aplicación
	 * tipoApp: Tipo de aplicación, establecido por el enum TipoApp como DESKTOP o WEB, escritorio o web
	 * rutaApp: Ruta de llamada de la aplicación
	 */
	
	private String nombreAcl;
	private String permisoApp;
	private TipoApp tipoApp;
	public enum TipoApp{DESKTOP,WEB};
	private String rutaApp;
	
	public LocalGISApp( String nombreAcl, String permisoApp, TipoApp tipoApp, String rutaApp) {
		this.nombreAcl = nombreAcl;
		this.permisoApp = permisoApp;
		this.tipoApp = tipoApp;
		this.rutaApp = rutaApp;
	}
	
	public String getNombreAcl() {
		return nombreAcl;
	}

	public void setNombreAcl(String nombreAcl) {
		this.nombreAcl = nombreAcl;
	}

	public String getPermisoApp() {
		return permisoApp;
	}

	public void setPermisoApp(String permisoApp) {
		this.permisoApp = permisoApp;
	}

	public TipoApp getTipoApp() {
		return tipoApp;
	}

	public void setTipoApp(TipoApp tipoApp) {
		this.tipoApp = tipoApp;
	}

	public String getRutaApp() {
		return rutaApp;
	}
	
	public void setRutaApp(String rutaApp) {
		this.rutaApp = rutaApp;
	}
		
}
