/*
 * * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 * 
 * Created on 25-nov-2004 by juacas
 *
 * 
 */
package com.geopista.app.help;

import java.awt.Component;
import java.net.URL;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class HelpLoader
{
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(HelpLoader.class);

	/**
	 * 
	 */
	public static HelpSet getHelpSet(String helpHS)
	{
	ClassLoader c1 =  HelpLoader.class.getClassLoader();

		
		logger.info("getHelpSet(String) - classloader : ClassLoader c1 = " + c1);
		
URL recurso= HelpLoader.class.getResource("/Ayuda/Image10.jpg");

	
		logger.info("getHelpSet(String) - otro recurso." + recurso);
		

    URL hsURL = HelpSet.findHelpSet(c1,helpHS);

		
		logger.info("getHelpSet(String) - Cargando ayuda : ClassLoader c1 = " + c1
		+ ", URL hsURL = " + hsURL);
		

    HelpSet hs=null;
    if (hsURL==null)
    	return null;
    try
	{
    	hs = new HelpSet(null,hsURL);
	} catch (HelpSetException e)
	{
		logger.error("getHelpSet(String)", e);	
	}
	return hs;
	}
	public static void installHelp(String helpHS,String nodename,Component comp)
	{
	      try
		{
		HelpSet hs = com.geopista.app.help.HelpLoader.getHelpSet(helpHS);
		HelpBroker hb = hs.createHelpBroker();
		hb.enableHelpKey(comp, nodename, hs);
		} catch (Exception e)
		{
		// TODO: handle exception
			logger.error("installHelp(helpHS = " + helpHS + ", comp = " + comp
					+ ") - Error en la instalación de la ayuda:", e);
		}
	}
	public static void main(String[] args)
	{
		logger.info("main() - testing HelpLoader.");
		HelpSet hs = HelpLoader.getHelpSet("help.hs");
	}
}
