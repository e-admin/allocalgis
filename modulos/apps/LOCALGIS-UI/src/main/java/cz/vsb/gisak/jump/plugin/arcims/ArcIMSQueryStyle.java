/**
 * ArcIMSQueryStyle.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * 
 * Created on 01-jun-2005 by juacas
 *
 * 
 */
package cz.vsb.gisak.jump.plugin.arcims;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.ui.plugin.io.arcIMS.AddArcIMSQueryPlugIn;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.ILayer;
import com.vividsolutions.jump.workbench.ui.IViewport;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.renderer.style.Style;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class ArcIMSQueryStyle implements Style
{
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory
												.getLog(ArcIMSQueryStyle.class);
	private String	serviceName;
	private String	url;


	/**
	 * 
	 */
	public ArcIMSQueryStyle()
	{
	super();
	// TODO Auto-generated constructor stub
	}

public ArcIMSQueryStyle(String servicename, String url)
{
this.serviceName=servicename;
this.url = url;

}
    private Image cachedImage;
    private Envelope cachedEnvelope;
    boolean imageOK = false;
	private boolean	enabled=true;

public void paint(Feature f, Graphics2D g, IViewport viewport) {   
//if (!enabled)
//	return;
      /** Gets the actual envelope*/
  Envelope envelope;
      Image image;
      envelope = viewport.getEnvelopeInModelCoordinates();
		if (logger.isDebugEnabled())
			{
			logger
					.debug("paint(Feature, Graphics2D, Viewport) - Trying to draw layer: "
							+ serviceName);
			}  
      if (!(envelope.equals(cachedEnvelope)) || cachedImage == null) {
        
//        FeatureCollection features = viewport.getPanel().getLayerManager().getLayer(serviceName).getFeatureCollectionWrapper();
//    features.clear();
//    features.add(FeatureUtil.toFeature(EnvelopeUtil.toGeometry(envelope), features.getFeatureSchema()));

        ArcIMSConnector arcims = new ArcIMSConnector(url);
        URL imageURL = null;
        try {
          imageURL = new URL(arcims.getImageURL(envelope, viewport.getPanel().getWidth(), viewport.getPanel().getHeight()));
		if (logger.isDebugEnabled())
			{
			logger
					.debug("paint(Feature, Graphics2D, Viewport) - Image URL: "
							+ imageURL);
			}
          image = Toolkit.getDefaultToolkit().getImage(imageURL);
          imageOK = true;
	      } catch (java.net.MalformedURLException mue) {
	if (logger.isDebugEnabled())
		{
		logger
				.debug("paint(Feature, Graphics2D, Viewport) - Problems with reading image: "
						+ imageURL + " " + mue);
		}
          image = Toolkit.getDefaultToolkit().getImage(AddArcIMSQueryPlugIn.class.getResource("notavailable.png"));
	imageOK = false;
    }            
        cachedImage = image;
      } else {
    image = cachedImage;  
      }
      cachedEnvelope = envelope; 
      if (imageOK) {
     MediaTracker mediaTracker;
    mediaTracker = new MediaTracker((LayerViewPanel)viewport.getPanel());
    mediaTracker.addImage(image, 0);
    try {
	mediaTracker.waitForID(0);
    } catch (java.lang.InterruptedException ie) {
	if (logger.isDebugEnabled())
		{
		logger
				.debug("paint(Feature, Graphics2D, Viewport) - Problems with drawing image in Panel: "
						+ ie);
		}
    }

    AffineTransform originalTransform;
    originalTransform = g.getTransform();
    try {
      double xScale, yScale;
      xScale = viewport.getScale() * envelope.getWidth()/image.getWidth(null);
      yScale = viewport.getScale() * envelope.getHeight()/image.getHeight(null);
      g.scale(xScale, yScale);
      Point2D upperLeftCorner = null;
	try {
        upperLeftCorner = viewport.toViewPoint(new Coordinate(envelope.getMinX(), envelope.getMaxY()));
	} catch(java.awt.geom.NoninvertibleTransformException ne) {

	}
      g.translate(upperLeftCorner.getX()/xScale, upperLeftCorner.getY()/yScale);
      g.drawImage(image, 0, 0, null);    
    } 
    finally {
	g.setTransform(originalTransform);  
    }
      } else {
        g.drawString("Service " + serviceName + " is not available", 10, 50);
      }
}
public void initialize(ILayer layer) {}
public Object clone() { throw new UnsupportedOperationException(); }
public void setEnabled(boolean enabled) {this.enabled=enabled;}
public boolean isEnabled() { return enabled; }

	public String getServiceName()
	{
	return serviceName;
	}
	public void setServiceName(String serviceName)
	{
	this.serviceName = serviceName;
	}
	public String getUrl()
	{
	return url;
	}
	public void setUrl(String url)
	{
	this.url = url;
	}
}
