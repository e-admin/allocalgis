/*
 
 */
package com.geopista.app.layerutil.images;

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.ImageIcon;


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
