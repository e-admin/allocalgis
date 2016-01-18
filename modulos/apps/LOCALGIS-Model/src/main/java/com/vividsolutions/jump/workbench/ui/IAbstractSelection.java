/**
 * IAbstractSelection.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui;

import java.util.Collection;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.CollectionMap;
import com.vividsolutions.jump.workbench.model.ILayer;

public interface IAbstractSelection {

	public abstract String getRendererContentID();

	public abstract List items(Geometry geometry);

	public abstract Collection items(Geometry geometry, Collection indices);

	/**
	 * Note that some features in the map may not have selected items.
	 */
	public abstract CollectionMap getFeatureToSelectedItemIndexCollectionMap(
			ILayer layer);

	public abstract Collection getSelectedItemIndices(ILayer layer,
			Feature feature);

	/**
	 * Note that some features in the map may not have selected items.
	 */
	public abstract CollectionMap getFeatureToSelectedItemCollectionMap(
			ILayer layer);

	public abstract Collection getLayersWithSelectedItems();

	public abstract Collection getFeaturesWithSelectedItems();

	public abstract Collection getFeaturesWithSelectedItems(ILayer layer);

	public abstract Collection getSelectedItems();

	public abstract Collection getSelectedItems(ILayer layer);

	public abstract Collection getSelectedItems(ILayer layer, Feature feature);

	/**
	 * @param geometry the feature's Geometry or equivalent; that is, a clone or
	 * similar enough Geometry from which Geometries can be retrieved using
	 * the selection indices.
	 */
	public abstract Collection getSelectedItems(ILayer layer, Feature feature,
			Geometry geometry);

	public abstract Collection indices(Geometry geometry, Collection items);

	public abstract void unselectItems(ILayer layer,
			CollectionMap featureToItemCollectionMap);

	public abstract void selectItems(ILayer layer,
			CollectionMap featureToItemCollectionMap);

	public abstract void selectItems(ILayer layer, Feature feature,
			Collection items);

	public abstract void unselectItems(ILayer layer, Feature feature,
			Collection items);

	public abstract Collection itemsNotSelectedInAncestors(ILayer layer,
			Feature feature, Collection items);

	public abstract void selectItems(ILayer layer, Feature feature);

	public abstract void selectItems(ILayer layer, Collection features);

	public abstract void unselectFromFeaturesWithModifiedItemCounts(
			ILayer layer, Collection features, Collection oldFeatureClones);

	public abstract void unselectItems();

	public abstract void unselectItems(ILayer layer);

	public abstract void unselectItems(ILayer layer, Collection features);

	public abstract void unselectItems(ILayer layer, Feature feature);

	public abstract void unselectItem(ILayer layer, Feature feature,
			int selectedItemIndex);

	public abstract void setChild(AbstractSelection child);

	public abstract void setParent(AbstractSelection parent);

}