/**
 * WmsSRID.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * 
 * Created on 25-abr-2005 by juacas
 *
 * 
 */
package com.geopista.server.administradorCartografia;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
