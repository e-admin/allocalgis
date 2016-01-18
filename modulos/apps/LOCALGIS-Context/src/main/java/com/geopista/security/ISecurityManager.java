/**
 * ISecurityManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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