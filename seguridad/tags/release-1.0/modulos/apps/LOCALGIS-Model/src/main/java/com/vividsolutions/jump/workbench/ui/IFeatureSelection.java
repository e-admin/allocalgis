package com.vividsolutions.jump.workbench.ui;

import java.util.Collection;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.ILayer;
import com.vividsolutions.jump.workbench.model.Layer;

public interface IFeatureSelection extends IAbstractSelection {

	public abstract List items(Geometry geometry);

	public abstract String getRendererContentID();
	public abstract void selectItems(ILayer layer, Collection features);
	public abstract void selectItems(ILayer layer, Feature feature);

}