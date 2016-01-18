/**
 * SecurityManagerProxy.java
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
package com.geopista.util;

import java.security.acl.AclNotFoundException;

import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPrincipal;
import com.geopista.security.ISecurityManager;
import com.geopista.security.SecurityManager;
import com.geopista.security.TestConnection;
/**
 * TODO Documentación
 * @author juacas
 *
 */
public class SecurityManagerProxy implements ISecurityManager
{
boolean secMgr=false;

	/**
	 * 
	 */
	public SecurityManagerProxy()
	{
	super();	
	
	try
		{
		/**
		 * Evita que se cargue el paquete de seguridad si no está instalado
		 * para compatibilidad con JUMP o entornos no conectados.
		 */
		Class.forName("com.geopista.security.SecurityManager").newInstance();
		secMgr=true;
		}
	catch (InstantiationException e)
		{
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	catch (IllegalAccessException e)
		{
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	catch (ClassNotFoundException e)
		{
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	
	}

	public  void callHeartBeat()
	{
	if (secMgr) SecurityManager.callHeartBeat();
	}
	public  String getIdApp()
	{
	return secMgr?SecurityManager.getIdApp():"GEOPISTA";
	}
	public  String getIdMunicipio()
	{
	return secMgr?SecurityManager.getIdMunicipio():"";
	}
	public  String getIdSesion()
	{
	return secMgr?SecurityManager.getIdSesion():"";
	}
	public  GeopistaAcl getPerfil(String sNombreAcl)
			throws AclNotFoundException, Exception
	{
	return secMgr?SecurityManager.getPerfil(sNombreAcl):null;
	}
	public  GeopistaPrincipal getPrincipal()
	{
	return secMgr?SecurityManager.getPrincipal():null;
	}
	public  TestConnection getTestConnection()
	{
	return secMgr?SecurityManager.getTestConnection():null;
	}
	public  String getUrl()
	{
	return secMgr?SecurityManager.getUrl():"";
	}
	public  GeopistaPrincipal getUserPrincipal()
	{
	return secMgr?SecurityManager.getUserPrincipal():null;
	}
	public  boolean isConnected()
	{
	return secMgr?SecurityManager.isConnected():false;
	}
	public  boolean isLogged()
	{
	return secMgr?SecurityManager.isLogged():false;
	}
	public  boolean login(String sUsuario, String sPassword,
			String sIdApp, int iIdMunicipio) throws Exception
	{
	return secMgr?SecurityManager.login(sUsuario, sPassword, sIdApp):false;
	}
	public  boolean logout() throws Exception
	{
	return secMgr?SecurityManager.logout():false;
	}
	public  void setHeartBeatTime(long timeToSleep)
	{
	if (secMgr) SecurityManager.setHeartBeatTime(timeToSleep);
	}
	public  void setIdMunicipio(String sIdMunicipio)
	{
	if (secMgr) SecurityManager.setIdMunicipio(sIdMunicipio);
	}
	public  void setsUrl(String sNewUrl)
	{
	if (secMgr) 
		SecurityManager.setsUrl(sNewUrl);
	}
	public  void setUrl(String sNewUrl)
	{
	if (secMgr) SecurityManager.setUrl(sNewUrl);
	}
	public  void unLogged()
	{
	if (secMgr) SecurityManager.unLogged();
	}
}
