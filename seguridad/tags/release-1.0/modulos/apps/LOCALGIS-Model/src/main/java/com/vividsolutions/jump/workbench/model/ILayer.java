package com.vividsolutions.jump.workbench.model;

import java.util.Collection;
import java.util.List;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureCollectionWrapper;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;

import com.vividsolutions.jump.util.Blackboard;

import com.vividsolutions.jump.workbench.ui.renderer.style.Style;



public interface ILayer extends Layerable{

	public static final String FIRING_APPEARANCE_CHANGED_ON_ATTRIBUTE_CHANGE = ILayer.class.getName() +
	        " - FIRING APPEARANCE CHANGED ON ATTRIBUTE CHANGE";
	
	public abstract void setDescription(String description);

	/**
	 *  Used for lightweight layers like the Vector layer.
	 *
	 *@param  drawingLast  true if the layer should be among those drawn last
	 */
	public abstract void setDrawingLast(boolean drawingLast);

	public abstract void setFeatureCollection(
			final FeatureCollection featureCollection);

	/**
	 * Editability is not enforced; all parties are responsible for heeding
	 * this flag.
	 */
	public abstract void setEditable(boolean editable);

	public abstract boolean isEditable();

	public abstract void setDinamica(boolean dinamica);

	public abstract boolean isDinamica();

	public abstract void setUrl(String url);

	public abstract String getUrl();

	public abstract void setSynchronizingLineColor(
			boolean synchronizingLineColor);

	public abstract Style getBasicStyle();

	public abstract Style getVertexStyle();

	public abstract Style getLabelStyle();

	public abstract String getDescription();

	/**
	 * Returns a wrapper around the FeatureCollection which was added using
	 * #wrapFeatureCollection. The original FeatureCollection can be retrieved using
	 * FeatureCollectionWrapper#getWrappee. However, parties are encouraged
	 * to use the FeatureCollectionWrapper instead, so that feature additions and
	 * removals cause FeatureEvents to be fired (by the Layer).
	 */
	public abstract FeatureCollectionWrapper getFeatureCollectionWrapper();

	/**
	 * Styles do not notify the Layer when their parameters change. Therefore,
	 * after you modify a Style's parameters (for example, the fill colour of
	 * BasicStyle), be sure to call #fireAppearanceChanged
	 *@param  c  Can even be the desired Style's superclass or interface
	 *@return    The style value
	 */
	public abstract Style getStyle(Class c);

	public abstract List getStyles();

	public abstract boolean hasReadableDataSource();

	public abstract boolean isDrawingLast();

	public abstract boolean isSynchronizingLineColor();

	public abstract void addStyle(Style style);

	/**
	 * Releases references to the data, to facilitate garbage collection.
	 * Important for MDI apps like the JUMP Workbench. Called when the last JInternalFrame
	 * viewing the LayerManager is closed (i.e. internal frame's responsibility). To conserve
	 * memory, if layers are frequently added and removed from the LayerManager,
	 * parties may want to call #dispose themselves rather than waiting for the internal
	 * frame to be closed.
	 */
	public abstract void dispose();

	public abstract void removeStyle(Style p);

	public abstract Collection cloneStyles();

	public abstract void setStyles(Collection newStyles);

	public abstract void setLayerManager(ILayerManager layerManager);

	public abstract Blackboard getBlackboard();

	public abstract DataSourceQuery getDataSourceQuery();

	public abstract ILayer setDataSourceQuery(DataSourceQuery dataSourceQuery);

	public abstract boolean isFeatureCollectionModified();

	public abstract ILayer setFeatureCollectionModified(
			boolean featureCollectionModified);

	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.Layerable#getEnvelope()
	 */
	public abstract Envelope getEnvelope();

	public abstract String getSrid();

	public abstract void setSrid(String srid);

	public abstract boolean isVersionable();

	public abstract void setVersionable(boolean versionable);

	public abstract String getValidator();

	public abstract void setValidator(String validator);
	
	public void fireAppearanceChanged();
}