/*
 * 
 * Created on 03-jun-2005 by juacas
 *
 * 
 */
package com.geopista.security;

import java.security.acl.AclNotFoundException;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public interface ISecurityManager
{

	/**
	 * 
	 */
	void unLogged();

	/**
	 * @param newUrl
	 */
	void setUrl(String newUrl);

	/**
	 * @param newUrl
	 */
	void setsUrl(String newUrl);



	/**
	 * @param timeToSleep
	 */
	void setHeartBeatTime(long timeToSleep);

	/**
	 * @return
	 * @throws Exception
	 */
	boolean logout() throws Exception;

	/**
	 * @param usuario
	 * @param password
	 * @param idApp
	 * @param idMunicipio
	 * @return
	 * @throws Exception
	 */
	boolean login(String usuario, String password, String idApp, int idMunicipio) throws Exception;

	
	/**
	 * @param idMunicipio
	 */
	void setIdMunicipio(String idMunicipio);

	/**
	 * 
	 */
	void callHeartBeat();

	/**
	 * @return
	 */
	String getIdApp();

	/**
	 * @return
	 */
	String getIdMunicipio();

	/**
	 * @return
	 */
	String getIdSesion();

	/**
	 * @param nombreAcl
	 * @return
	 * @throws Exception
	 * @throws AclNotFoundException
	 */
	GeopistaAcl getPerfil(String nombreAcl) throws AclNotFoundException, Exception;

	/**
	 * @return
	 */
	GeopistaPrincipal getPrincipal();

	/**
	 * @return
	 */
	TestConnection getTestConnection();

	/**
	 * @return
	 */
	String getUrl();

	/**
	 * @return
	 */
	GeopistaPrincipal getUserPrincipal();

	/**
	 * @return
	 */
	boolean isConnected();

	/**
	 * @return
	 */
	boolean isLogged();
}