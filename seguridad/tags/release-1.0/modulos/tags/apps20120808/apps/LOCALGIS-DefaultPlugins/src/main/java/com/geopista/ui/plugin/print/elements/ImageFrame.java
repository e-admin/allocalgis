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
