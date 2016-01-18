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
import com.geopista.security.TestConnection;
import com.geopista.security.SecurityManager;
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
