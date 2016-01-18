package com.vividsolutions.jump.workbench.ui;

import java.util.Collection;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.ILayer;;


public interface ISelectionManager {

	/**
	 * A feature may get split into two or more -- for example, if two linestrings of a feature
	 * are selected. 
	 */
	public abstract Collection createFeaturesFromSelectedItems();

	public abstract Collection createFeaturesFromSelectedItems(ILayer layer);

	public abstract void clear();

	public abstract IFeatureSelection getFeatureSelection();

	public abstract ILineStringSelection getLineStringSelection();

	/**
	 * @return AbstractSelections
	 */
	public abstract Collection getSelections();

	/**
	 * "items" rather than "geometries" because the user may have selected a part
	 * of a Geometry (an element of a GeometryCollection or a ring of a Polygon).
	 * @return a collection of Geometries
	 */
	public abstract Collection getSelectedItems();

	public abstract Collection getSelectedItems(ILayer layer);

	public abstract Collection getSelectedItems(ILayer layer, Feature feature);

	/**
	 * @param geometry the feature's Geometry or equivalent; that is, a clone or
	 * similar enough Geometry from which Geometries can be retrieved using
	 * the selection indices
	 */
	public abstract Collection getSelectedItems(ILayer layer, Feature feature,
			Geometry geometry);

	public abstract Collection getLayersWithSelectedItems();

	public abstract IPartSelection getPartSelection();

	public abstract void updatePanel();

	public abstract void setPanelUpdatesEnabled(boolean panelUpdatesEnabled);

	public abstract Collection getFeaturesWithSelectedItems(ILayer layer);

	public abstract void unselectItems(ILayer layer);

	public abstract void unselectItems(ILayer layer, Collection features);

	public abstract void unselectFromFeaturesWithModifiedItemCounts(
			ILayer layer, Collection features, Collection oldFeatureClones);

	public abstract Collection getFeaturesWithSelectedItems();

	public abstract boolean arePanelUpdatesEnabled();

	public abstract void dispose();

}