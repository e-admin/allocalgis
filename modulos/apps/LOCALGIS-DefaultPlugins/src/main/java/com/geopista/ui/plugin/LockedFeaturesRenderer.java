/**
 * LockedFeaturesRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.ImageIcon;

import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.LockManager;
import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.CollectionMap;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.Viewport;
import com.vividsolutions.jump.workbench.ui.renderer.AbstractSelectionRenderer;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class LockedFeaturesRenderer extends AbstractSelectionRenderer
{
	public final static String CONTENT_ID = "LOCKED_FEATURES";
	ImageIcon lock;
	
	/**
	 */
	public LockedFeaturesRenderer(LayerViewPanel panel)
	{
	super(CONTENT_ID, panel, Color.BLUE, false, false);
	
	lock = IconLoader.icon("lock_closed.png");
	}

	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.renderer.AbstractSelectionRenderer#featureToSelectedItemsMap(com.vividsolutions.jump.workbench.model.Layer)
	 */
	protected CollectionMap featureToSelectedItemsMap(Layer layer)
	{
	LockManager lockMgr= (LockManager) panel.getBlackboard().get(LockManager.LOCK_MANAGER_KEY);
	if (lockMgr==null)
		{
		return new CollectionMap();
		}
	Collection features= lockMgr.getLockedFeatures();
	   CollectionMap collectionMap = new CollectionMap();
       for (Iterator i = features.iterator();
           i.hasNext();)
       	{
		Feature feature = (Feature) i.next();
		if (feature instanceof GeopistaFeature)
			{
			if ( !(layer instanceof GeopistaLayer) ) continue;
			if(	!((GeopistaLayer) ((GeopistaFeature) feature).getLayer())
					.getSystemId()
					.equals(((GeopistaLayer) layer).getSystemId())||((GeopistaLayer) layer).isLocal()) 
				continue;
			}
		
			
		ArrayList items = new ArrayList();
		items.add(feature.getGeometry());
		collectionMap.put(feature, items);
		}


       return collectionMap;
	}
	Stroke fillStroke=new BasicStroke(3);
	Stroke lineStroke=new BasicStroke(8);
	Color fillColor = GUIUtil.alphaColor(Color.red, 30);
	Color lineColor = GUIUtil.alphaColor(Color.red, 10);
	
	public void paint(Geometry geometry, Graphics2D g, Viewport viewport)
	throws NoninvertibleTransformException {
	
	  if (geometry instanceof GeometryCollection) 
	  	{
	  	paintGeometryCollection((GeometryCollection) geometry, g, viewport);
	  	return;
	  	}
	  Shape centroide=viewport.getJava2DConverter().toShape(geometry.getCentroid());
	  Rectangle encuadre=centroide.getBounds();
	  //g.drawString("Locked",encuadre.x,encuadre.y);
	  g.drawImage(lock.getImage(),encuadre.x,encuadre.y,null);
	//StyleUtil.paint(geometry, g, viewport, false, fillStroke, fillColor,
	//		true, lineStroke, lineColor);

	}
	 private void paintGeometryCollection(GeometryCollection collection,
	        Graphics2D g, Viewport viewport)
	        throws NoninvertibleTransformException {
	        //For GeometryCollections, render each element separately. Otherwise,
	        //for example, if you pass in a GeometryCollection containing a ring and a
	        // disk, you cannot render them as such: if you use Graphics.fill, you'll get
	        //two disks, and if you use Graphics.draw, you'll get two rings. [Jon Aquino]
	        for (int i = 0; i < collection.getNumGeometries(); i++) {
	            paint(collection.getGeometryN(i), g, viewport);
	        }
	    }
}
