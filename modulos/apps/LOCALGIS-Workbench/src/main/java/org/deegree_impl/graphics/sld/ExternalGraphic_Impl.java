/**
 * ExternalGraphic_Impl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*----------------    FILE HEADER  ------------------------------------------

This file is part of deegree.
Copyright (C) 2001 by:
EXSE, Department of Geography, University of Bonn
http://www.giub.uni-bonn.de/exse/
lat/lon Fitzke/Fretter/Poth GbR
http://www.lat-lon.de

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

Contact:

Andreas Poth
lat/lon Fitzke/Fretter/Poth GbR
Meckenheimer Allee 176
53115 Bonn
Germany
E-Mail: poth@lat-lon.de

Jens Fitzke
Department of Geography
University of Bonn
Meckenheimer Allee 166
53115 Bonn
Germany
E-Mail: jens.fitzke@uni-bonn.de

                 
 ---------------------------------------------------------------------------*/
package org.deegree_impl.graphics.sld;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import org.deegree.graphics.sld.ExternalGraphic;
import org.deegree.xml.Marshallable;
import org.deegree_impl.tools.Debug;
import org.deegree_impl.tools.NetWorker;

import com.sun.media.jai.codec.MemoryCacheSeekableStream;


/**
 * The ExternalGraphic element allows a reference to be made to an external graphic
 * file with a Web URL. The OnlineResource sub-element gives the URL and the
 * Format sub-element identifies the expected document MIME type of a successful
 * fetch. Knowing the MIME type in advance allows the styler to select the best-
 * supported format from the list of URLs with equivalent content.
 * <p>----------------------------------------------------------------------</p>
 *
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 1.3 $ $Date: 2011/12/29 18:58:52 $
 */
public class ExternalGraphic_Impl implements ExternalGraphic, Marshallable {
    private BufferedImage image = null;
    private String format = null;
    private URL onlineResource = null;

    /**
     * Creates a new ExternalGraphic_Impl object.
     *
     * @param format 
     * @param onlineResource 
     */
    ExternalGraphic_Impl( String format, URL onlineResource ) {
        setFormat( format );
        setOnlineResource( onlineResource );
    }

    /**
     * the Format sub-element identifies the expected document MIME type of a
     * successful fetch.
     * @return Format of the external graphic
     */
    public String getFormat() {
        return format;
    }

    /**
     * sets the format (MIME type)
     * @param format Format of the external graphic
     */
    public void setFormat( String format ) {
        this.format = format;
    }

    /**
     *  The OnlineResource gives the URL of the external graphic
     *  @return URL of the external graphic
     */
    public URL getOnlineResource() {
        return onlineResource;
    }

    /**
     * sets the online resource / URL of the external graphic
     * @param onlineResource URL of the external graphic
     */
    public void setOnlineResource( URL onlineResource ) {
        Debug.debugMethodBegin( this, "setOnlineResource" );

        this.onlineResource = onlineResource;
        
        try {
            InputStream is = onlineResource.openStream();
            MemoryCacheSeekableStream mcss = new MemoryCacheSeekableStream( is );
            RenderedOp rop = JAI.create( "stream", mcss );
            image = rop.getAsBufferedImage();
            mcss.close();
        } catch ( IOException e ) {
        	//System.out.println( "Yikes: " + e );

        try
		{
			// Intenta interpretar el URL como un path relativo local para 
			// la librería de símbolos.
			String path = onlineResource.getPath();
			String GEOPISTA_PACKAGE = "/com/geopista/app";
			Preferences pref = Preferences.userRoot().node(GEOPISTA_PACKAGE);
			String defaultPath=""; // Configuración del fichero de configuration.properties de la aplicacion Web
			try
				{
				ResourceBundle rb = ResourceBundle
						.getBundle("configuration.properties");
				if (rb != null)
					{
					defaultPath = rb.getString("WMSClient/installPath");
					}
				}
			catch (Exception e1)
				{
				// TODO: handle exception
				}
			String value = pref.get("SLD.Textures.Directory", defaultPath);
			// obtiene directorio de texturas en caso contrario el actual de la aplicación
			File file = new File(new File(value),path);

			String pp= file.getAbsolutePath();
			//System.out.println("Trying to get ICON_LIB:"+pp);
			InputStream is;
			
				is = new FileInputStream(file);
				
			MemoryCacheSeekableStream mcss = new MemoryCacheSeekableStream(is);
			RenderedOp rop = JAI.create("stream", mcss);
			image = rop.getAsBufferedImage();
			
				mcss.close();
				}
			catch (IOException e1)
				{
				// En caso de error devuelve un icono por defecto
				
				int width = 30;
				int height = 30;
				BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
				Graphics2D biContext = bi.createGraphics();
				biContext.setColor(Color.red);
				biContext.fillOval(0,0,width,height);
				biContext.setColor(Color.black);
				biContext.drawOval(0,0,width,height);
				biContext.setColor(Color.white);
				biContext.fillRect(width/4,height/2-2,width/2,4);
				image=bi;
				System.out.println(e1.getMessage());
				
				}
        
		
        
           
        }

        Debug.debugMethodEnd();
    }

    /**
     * returns the external graphic as an image. this method is not part
     * of the sld specifications but it is added for speed up applications
     * @return the external graphic as BufferedImage
     */
    public BufferedImage getAsImage() {
        return image;
    }
    
     
   /**
    * sets the external graphic as an image.
    * @param image the external graphic as BufferedImage 
    */ 
    public void setAsImage(BufferedImage image) {
        this.image = image;
    }
    
    /**
     * exports the content of the ExternalGraphic as XML formated String
     *
     * @return xml representation of the ExternalGraphic
     */
    public String exportAsXML() {
        Debug.debugMethodBegin();
        
        StringBuffer sb = new StringBuffer(200);
        sb.append( "<ExternalGraphic>" );
        sb.append( "<OnlineResource xmlns:xlink='http://www.w3.org/1999/xlink' ");
        sb.append( "xlink:type='simple' xlink:href='" );
        sb.append( NetWorker.url2String( onlineResource ) + "'/>" );
        sb.append( "<Format>" ).append( format ).append( "</Format>" );
        sb.append( "</ExternalGraphic>" );
        
        Debug.debugMethodEnd();
        return sb.toString();
    }
    
}