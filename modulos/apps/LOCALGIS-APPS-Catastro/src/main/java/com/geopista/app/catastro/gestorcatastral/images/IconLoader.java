/**
 * IconLoader.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 
 */
package com.geopista.app.catastro.gestorcatastral.images;

import java.net.URL;

import javax.swing.ImageIcon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Gets an icon from this class' package.
 */
public class IconLoader {
	/**
	 * Logger for this class
	 */
	private static final Log logger	= LogFactory.getLog(IconLoader.class);

	public static ImageIcon icon(String filename) {
	ImageIcon im = new ImageIcon();

	try
		{
		URL url = IconLoader.class.getResource(filename);
		im = new ImageIcon(url);

		}
	catch (NullPointerException ex)
		{
		if (logger.isInfoEnabled())
			{
			logger.info("icon(String) - Falta el recurso imagen:" + filename,
					ex);
			}
		
			URL url = IconLoader.class.getResource("Wrench.gif");
			if (url!=null)
				im = new ImageIcon(url);
		

		}
	return im;
	}
}
