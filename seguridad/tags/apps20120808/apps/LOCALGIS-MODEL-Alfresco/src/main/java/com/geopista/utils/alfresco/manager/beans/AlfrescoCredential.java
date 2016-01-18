package com.geopista.utils.alfresco.manager.beans;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 * @ClassComments Bean con las credenciales de Alfresco
 */
public class AlfrescoCredential {

	/**
	 * Variables
	 */
	private String url;
	private String user;
	private String pass;
	
	/**
	 * Constructor
	 * @param url: Ruta web del api de Alfresco 
	 */
	public AlfrescoCredential(String url) {
		this.url = url;
	}

	/**
	 * Getter url
	 * @return String: Url de autenticación con Alfresco
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Setter url
	 * @param url: Url de autenticación con Alfresco
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Getter user
	 * @return String: Usuario de autenticación con Alfresco
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Setter user
	 * @param user: Nombre del usuario de autenticación con Alfresco
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Getter pass
	 * @return String: Contraseña de autenticación con Alfresco
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * Setter pass
	 * @param pass: Contraseña de autenticación con Alfresco
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}
		
}
