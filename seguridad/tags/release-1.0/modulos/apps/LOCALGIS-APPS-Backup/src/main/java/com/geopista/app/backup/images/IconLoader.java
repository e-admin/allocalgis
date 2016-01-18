/**
 * 
 */
package com.geopista.app.backup.images;

import java.net.URL;
import javax.swing.ImageIcon;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author javieraragon
 *
 */
public class IconLoader {
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
