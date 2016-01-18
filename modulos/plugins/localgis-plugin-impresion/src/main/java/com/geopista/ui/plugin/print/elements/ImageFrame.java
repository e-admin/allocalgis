/**
 * ImageFrame.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * 
 * Created on 29-jul-2005 by juacas
 *
 * 
 */
package com.geopista.ui.plugin.print.elements;

import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutFrame;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElementsListener;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.NorthFrame;

import com.geopista.ui.images.IconLoader;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class ImageFrame extends NorthFrame
{
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory.getLog(ImageFrame.class);

	private URL	imageURL;
	
	/**
	 * 
	 */
	public ImageFrame()
	{
	super();
	setIconURL(IconLoader.class.getResource("app-icon.gif"));
	onScreen.removeMouseListener(listener);
	onScreen.removeMouseMotionListener(listener);
	listener=new ImageListener(this,parent);
	onScreen.addMouseListener(listener);
    onScreen.addMouseMotionListener(listener);
	}

	/**
	 * @param plf
	 */
	public ImageFrame(PrintLayoutFrame plf)
	{
	super(plf);
	setIconURL(IconLoader.class.getResource("app-icon.gif"));
	
	onScreen.removeMouseListener(listener);
	onScreen.removeMouseMotionListener(listener);
	listener=new ImageListener(this,plf);
	onScreen.addMouseListener(listener);
    onScreen.addMouseMotionListener(listener);
	}
	/**
	 * @param iconURL
	 */
	public void setIconURL(URL urlimg)
	{
	this.imageURL = urlimg;
	this.northSymbol=new ImageIcon(urlimg);
	this.onScreen.setIcon(northSymbol);
	this.forPrint.setIcon(northSymbol);
	
	}

public void setIconURLasString(String url) throws MalformedURLException
{
URL urlimg= new URL(url);
setIconURL(urlimg);
}
public String getIconURLasString()
{
return getIconURL().toString();
}
public URL getIconURL()
{
return imageURL;
}
	private class ImageListener extends GraphicElementsListener
	{
		/**
		 * Logger for this class
		 */
		private final Log	logger	= LogFactory.getLog(ImageListener.class);

	ImageFrame fr;
		public ImageListener(ImageFrame ge, PrintLayoutFrame plf)
		{
		super(ge, plf);
		fr=ge;
		}

		public void mouseClicked(MouseEvent e)
		{
		if (SwingUtilities.isLeftMouseButton(e))
			{
			if (e.getClickCount() == 2)
				{
				URLImageChooserDialog dlg = new URLImageChooserDialog(fr);
				dlg.setURLIcon(getIconURL());
				dlg.setVisible(true);
				if (dlg.isOkpressed())
					{
					try
						{
						setIconURL(dlg.getIconURL());
						}
					catch (MalformedURLException e1)
						{
						//ignora este caso
						
						logger.error("mouseClicked(MouseEvent)", e1);
						}
					}
				repaint();
				}
			else
				{
				super.mouseClicked(e);
				}
			}

		}

		
	}

}
