package com.vividsolutions.jump.workbench.ui.renderer;

import java.awt.Graphics2D;
import java.util.*;

import com.geopista.style.sld.model.SLDStyle;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.renderer.style.Style;

/**
 * @see ImageCachingFeatureCollectionRenderer
 * @see FeatureCollectionRenderer
 */
public class SimpleFeatureCollectionRenderer extends SimpleRenderer {

	public SimpleFeatureCollectionRenderer(Object contentID,
			LayerViewPanel panel) {
		super(contentID, panel);
	}

	private void paint(Graphics2D g, Collection features, Layer layer,
			Style style) throws Exception {
		try{
			if (!layer.isVisible()) {
				return;
			}
			if (!style.isEnabled()) {
				return;
			}
			style.initialize(layer);
			//new ArrayList to avoid ConcurrentModificationException. [Jon Aquino]
			for (Iterator i = new ArrayList(features).iterator(); i.hasNext();) {
				final Feature feature = (Feature) i.next();
				if (cancelled) {
					return;
				}
				if (feature.getGeometry().isEmpty()) {
					continue;
				}
				style.paint(feature, g, panel.getViewport());
			}
		}
		catch(NullPointerException e){
			//System.out.println("Registro con geometría nula");
		}
	}

	protected void paint(Graphics2D g) throws Exception {
		for (Iterator i = styles.iterator(); i.hasNext();) {
			Style style = (Style) i.next();
			if (cancelled) {
				return;
			}
			//[JPC] Parche para habilitar el pintado por bandas
			if (style instanceof SLDStyle)
				{
				for (Iterator j = layerToFeaturesMap.keySet().iterator(); j.hasNext();)
					{
					Layer layer = (Layer) j.next();
					if (cancelled) {return;}
					Collection features = (Collection) layerToFeaturesMap.get(layer);
					((SLDStyle)style).setStyleTypeFilter(SLDStyle.PAINT_POLYGONS);
					paint(g, features, layer, style);
					((SLDStyle)style).setStyleTypeFilter(SLDStyle.PAINT_POINTS);
					paint(g, features, layer, style);
					((SLDStyle)style).setStyleTypeFilter(SLDStyle.PAINT_LINES);
					paint(g, features, layer, style);
					((SLDStyle)style).setStyleTypeFilter(SLDStyle.PAINT_LABELS);
					paint(g, features, layer, style);
					((SLDStyle)style).setStyleTypeFilter(SLDStyle.PAINT_LABELS|SLDStyle.PAINT_LINES|SLDStyle.PAINT_POINTS|SLDStyle.PAINT_POLYGONS);
					
				}
				
				
				
				}
			else
//				 Fin del parche de bandas
			for (Iterator j = layerToFeaturesMap.keySet().iterator(); j
					.hasNext();) {
				Layer layer = (Layer) j.next();
				if (cancelled) {
					return;
				}
				Collection features = (Collection) layerToFeaturesMap
						.get(layer);
				paint(g, features, layer, style);
			}
		}
	}

	private Collection styles = new ArrayList();

	private Map layerToFeaturesMap = new HashMap();

	protected void setLayerToFeaturesMap(Map layerToFeaturesMap) {
		this.layerToFeaturesMap = layerToFeaturesMap;
	}

	protected void setStyles(Collection styles) {
		this.styles = styles;
	}
    //ASO añade por problemas de memoria
    public void destroy()
    {
    	//if (layerToFeaturesMap!=null)
    	//	System.out.println("LayerToFeatures:"+layerToFeaturesMap.getClass().getName());
		//if (layerToFeaturesMap!=null)
		//    layerToFeaturesMap.clear();
		layerToFeaturesMap=null;
    }

}