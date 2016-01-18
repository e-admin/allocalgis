/**
 * ImageCachingFeatureCollectionRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.renderer;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.geopista.style.sld.model.SLDStyle;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.renderer.style.Style;

/**
 * @see SimpleFeatureCollectionRenderer
 * @see FeatureCollectionRenderer
 */
public class ImageCachingFeatureCollectionRenderer extends ImageCachingRenderer {

	public ImageCachingFeatureCollectionRenderer(Object contentID,
			LayerViewPanel panel) {
		super(contentID, panel);
	}

	private Collection styles = new ArrayList();

	private Map layerToFeaturesMap = new HashMap();

	protected void setLayerToFeaturesMap(Map layerToFeaturesMap) {
		this.layerToFeaturesMap = layerToFeaturesMap;
	}

	protected void setStyles(Collection styles) {
		this.styles = styles;
	}

	protected void renderHook(ThreadSafeImage image, Collection features,
			Layer layer, final Style style) throws Exception {
		if (!layer.isVisible()) {
			return;
		}
		if (!style.isEnabled()) {
			return;
		}
		style.initialize(layer);
		//new ArrayList to avoid ConcurrentModificationException. [Jon Aquino]

		// Revert to the original list, as the features may be a LazyList
		// containing a huge number of features from a database. Monitor the
		// frequency of ConcurrentModificationException errors. [Jon Aquino
		// 2005-03-02]
		for (Iterator i = features.iterator(); i.hasNext();) {
			final Feature feature = (Feature) i.next();
			if (cancelled) {
				break;
			}
			if (feature.getGeometry().isEmpty()) {
				continue;
			}
			//Because image.draw is synchronized, it might be faster to do
			//several paints inside #draw. [Jon Aquino]
			image.draw(new Drawer() {
				public void draw(Graphics2D g) throws Exception {
					style.paint(feature, g, panel.getViewport());
				}
			});
		}
	}

	protected void renderHook(ThreadSafeImage image) throws Exception {
		for (Iterator i = styles.iterator(); i.hasNext();) {
			Style style = (Style) i.next();
			//[JPC] Parche para habilitar el pintado por bandas
			if (style instanceof SLDStyle)
				{
				for (Iterator j = layerToFeaturesMap.keySet().iterator(); j.hasNext();)
					{
					Layer layer = (Layer) j.next();
					if (cancelled) {return;}
					Collection features = (Collection) layerToFeaturesMap.get(layer);
					
					((SLDStyle)style).setStyleTypeFilter(SLDStyle.PAINT_POLYGONS);
					renderHook(image, features, layer, style);
					((SLDStyle)style).setStyleTypeFilter(SLDStyle.PAINT_POINTS);
					renderHook(image, features, layer, style);
					((SLDStyle)style).setStyleTypeFilter(SLDStyle.PAINT_LINES);
					renderHook(image, features, layer, style);
					((SLDStyle)style).setStyleTypeFilter(SLDStyle.PAINT_LABELS);
					renderHook(image, features, layer, style);
					((SLDStyle)style).setStyleTypeFilter(SLDStyle.PAINT_LABELS|SLDStyle.PAINT_LINES|SLDStyle.PAINT_POINTS|SLDStyle.PAINT_POLYGONS);
					
				}
				
				
				
				}
			else
//				 Fin del parche de bandas
			for (Iterator j = layerToFeaturesMap.keySet().iterator(); j
					.hasNext();) {
				Layer layer = (Layer) j.next();
				Collection features = (Collection) layerToFeaturesMap
						.get(layer);
				renderHook(image, features, layer, style);
			}
		}
	}
    //ASO añade por problemas de memoria
    public void destroy()
    {
       	//if (layerToFeaturesMap!=null)
    	//	System.out.println("ImageCachingFeatures:"+layerToFeaturesMap.getClass().getName());
        //if (layerToFeaturesMap!=null)
        //    layerToFeaturesMap.clear();
        layerToFeaturesMap=null;
    }

}