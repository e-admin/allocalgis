/*
 * 
 * Created on 25-abr-2005 by juacas
 *
 * 
 */
package com.geopista.server.administradorCartografia;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.FileNotFoundException;
import java.io.IOException;

import es.enxenio.util.configuration.ConfigurationParametersManager;
import es.enxenio.util.exceptions.MissingConfigurationParameterException;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class WmsSRID extends SRID
{
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory.getLog(WmsSRID.class);

	/**
	 * @param sFileName
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public WmsSRID(String sFileName) throws FileNotFoundException, IOException
	{
	super(sFileName);
	
	}
	

	
	public String getUserSchema()
	{
	try{
	String user =ConfigurationParametersManager.getParameter("WMSManager/User");
	return user;
	}catch(MissingConfigurationParameterException ex)
	{
		logger
				.debug("getSRID() - Error al coger el Usuario del Esquema.");
		return "guiaurbana";
	}
	
	
		
	}
	
	public void load() throws FileNotFoundException, IOException
	{
	// no carga nada
	}
}
