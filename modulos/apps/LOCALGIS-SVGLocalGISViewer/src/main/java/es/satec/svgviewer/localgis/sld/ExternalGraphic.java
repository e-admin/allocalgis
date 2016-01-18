/**
 * ExternalGraphic.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.svgviewer.localgis.sld;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * The ExternalGraphic element allows a reference to be made to an external graphic file with a Web
 * URL. The OnlineResource sub-element gives the URL and the Format sub-element identifies the
 * expected document MIME type of a successful fetch. Knowing the MIME type in advance allows the
 * styler to select the best- supported format from the list of URLs with equivalent content.
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: satec $
 * 
 * @version. $Revision: 1.1 $, $Date: 2011/09/19 13:48:12 $
 */
public class ExternalGraphic {
	
	private static Logger logger = (Logger) Logger.getInstance(ExternalGraphic.class);

	private Image image = null;

	private String format = null;

	private URL onlineResource = null;

	private URL urlBase;
	
	private boolean tried = false;

	
	/**
	 * Creates a new ExternalGraphic_Impl object.
	 * 
	 * @param format
	 * @param onlineResource
	 */
	ExternalGraphic( String format, URL onlineResource, URL urlBase ) {
		setFormat( format );
		this.urlBase = urlBase;
		setOnlineResource( onlineResource );
	}

	/**
	 * the Format sub-element identifies the expected document MIME type of a successful fetch.
	 * 
	 * @return Format of the external graphic
	 * 
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * sets the format (MIME type)
	 * 
	 * @param format
	 *            Format of the external graphic
	 * 
	 */
	public void setFormat( String format ) {
		this.format = format;
	}

	/**
	 * The OnlineResource gives the URL of the external graphic
	 * 
	 * @return URL of the external graphic
	 * 
	 */
	public URL getOnlineResource() {
		return onlineResource;
	}

	/**
	 * sets the online resource / URL of the external graphic
	 * 
	 * @param onlineResource
	 *            URL of the external graphic
	 * 
	 */
	public void setOnlineResource( URL onlineResource ) {

		this.onlineResource = onlineResource;
		String file = onlineResource.getFile();
		int idx = file.indexOf( "$" );
		if ( idx == -1 ) {
			retrieveImage( onlineResource, urlBase );
			 
		}
	}

	
	private void retrieveImage(URL onlineResource, URL urlBase) {
		InputStream is = null;
		retrieveImage (onlineResource);
		if (image == null)  {
			logger.debug ("no puedo con la image sin base pruebo con la base " + urlBase);
			//pruebo viendo si es relativa a la url base
		
			String path = onlineResource.toExternalForm();
			int index = path.indexOf ("/");
			if (index != -1) {
				path = path.substring(index +1);
			}
			try {
				URL nueva = new URL (urlBase, (path)) ;
				logger.debug ("URL sin path ->" + nueva);
				retrieveImage (nueva);
			} catch (MalformedURLException e) {
				 logger.error(e);
			}
		}
	}
	
	
	/**
	 * @param onlineResource
	 */
	private void retrieveImage(URL onlineResource) {
		InputStream is = null;
		String t= null;
		try {
			t = onlineResource.toExternalForm();
			logger.debug("External form " + t);
			if (!t.trim().toLowerCase().endsWith( ".svg" )) {
				is = onlineResource.openStream();
				image = new Image(Display.getCurrent(), is);
			}
		} catch (IOException e) {
			logger.error(e);
			
		} finally {
			if (is!=null) {
				try {
					is.close();
				} catch (IOException e) {}
			}
		}
	}

	/**
	 * returns the external graphic as an image. this method is not part of the sld specifications
	 * but it is added for speed up applications
	 * 
	 * @return the external graphic as BufferedImage
	 */
	public Image getAsImage( int targetSizeX, int targetSizeY) {

		// no quiero que me atiborren a peticioes de descarga de imagen por feature		
		if ((this.image == null) && ! (tried)) {
			retrieveImage( onlineResource , urlBase);
			tried = true;
		}

		return image;
	}

	/**
	 * sets the external graphic as an image.
	 * 
	 * @param image
	 *            the external graphic as BufferedImage
	 */
	public void setAsImage(Image image ) {
		this.image = image;
	}

	public Image getImage(){
		return image;
	}
}