/**
 * FeatureCollectionRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.renderer;

//[sstein] : 30.07.2005 added variable maxFeatures with getters and setters

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.geopista.model.DynamicLayer;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;

public abstract class FeatureCollectionRenderer implements Renderer {
	
	private int maxFeatures = 100;
	
	private ImageCachingFeatureCollectionRenderer imageCachingFeatureCollectionRenderer;

	private SimpleFeatureCollectionRenderer simpleFeatureCollectionRenderer;

	private Renderer currentFeatureCollectionRenderer;

	public FeatureCollectionRenderer(Object contentID, LayerViewPanel panel) {
		this(contentID, panel, new ImageCachingFeatureCollectionRenderer(
				contentID, panel));
	}

	public FeatureCollectionRenderer(
			Object contentID,
			LayerViewPanel panel,
			ImageCachingFeatureCollectionRenderer imageCachingFeatureCollectionRenderer) {
		this.imageCachingFeatureCollectionRenderer = imageCachingFeatureCollectionRenderer;
		simpleFeatureCollectionRenderer = new SimpleFeatureCollectionRenderer(
				contentID, panel);
		currentFeatureCollectionRenderer = simpleFeatureCollectionRenderer;
	}

	public void clearImageCache() {
		imageCachingFeatureCollectionRenderer.clearImageCache();
		simpleFeatureCollectionRenderer.clearImageCache();

	}

	public boolean isRendering() {
		return currentFeatureCollectionRenderer.isRendering();
	}

	public Object getContentID() {
		return currentFeatureCollectionRenderer.getContentID();
	}

	public void copyTo(Graphics2D graphics) {
		currentFeatureCollectionRenderer.copyTo(graphics);
	}

	public Runnable createRunnable() {
		Map layerToFeaturesMap = layerToFeaturesMap();
		Collection styles = styles();
		imageCachingFeatureCollectionRenderer
				.setLayerToFeaturesMap(layerToFeaturesMap);
		imageCachingFeatureCollectionRenderer.setStyles(styles);
		simpleFeatureCollectionRenderer
				.setLayerToFeaturesMap(layerToFeaturesMap);
		simpleFeatureCollectionRenderer.setStyles(styles);
		if (simpleFeatureCollectionRenderer.getContentID() instanceof DynamicLayer){
			currentFeatureCollectionRenderer = simpleFeatureCollectionRenderer;
		}else{
			currentFeatureCollectionRenderer = useImageCaching(layerToFeaturesMap) ? (Renderer) imageCachingFeatureCollectionRenderer
					: simpleFeatureCollectionRenderer;
		}
		return currentFeatureCollectionRenderer.createRunnable();
	}

	protected boolean useImageCaching(Map layerToFeaturesMap) {
		return featureCount(layerToFeaturesMap) >= this.maxFeatures;
	}

	private int featureCount(Map layerToFeaturesMap) {
		int count = 0;
		for (Iterator i = layerToFeaturesMap.values().iterator(); i.hasNext();) {
			Collection features = (Collection) i.next();
			count += features.size();
		}

		return count;
	}

	protected abstract Map layerToFeaturesMap();

	protected abstract Collection styles();

	public void cancel() {
		imageCachingFeatureCollectionRenderer.cancel();
		simpleFeatureCollectionRenderer.cancel();
	}
	
	/**
	 * @return Returns the number of maxFeatures to render
	 * as vector graphic.
	 */
	public int getMaxFeatures() {
		return maxFeatures;
	}
	/**
	 * @param maxFeatures The maximum number of Features to render
	 * as vector graphic.
	 */
	public void setMaxFeatures(int maxFeatures) {
		this.maxFeatures = maxFeatures;
	}
	/**
	 * @return Returns the simpleFeatureCollectionRenderer.
	 */
	public SimpleFeatureCollectionRenderer getSimpleFeatureCollectionRenderer() {
		return simpleFeatureCollectionRenderer;
	}
    //ASO añade por problemas de memoria
    public void destroy()
    {
        if (simpleFeatureCollectionRenderer!=null)
            simpleFeatureCollectionRenderer.destroy();
        /*if (currentFeatureCollectionRenderer!=null){        	
            ((SimpleFeatureCollectionRenderer)currentFeatureCollectionRenderer).destroy();
        }*/
        if (imageCachingFeatureCollectionRenderer!=null)
            imageCachingFeatureCollectionRenderer.destroy();
    }
}