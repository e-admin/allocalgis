package com.vividsolutions.jump.workbench.ui;

import java.util.Collection;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.workbench.model.Layer;

public interface IFeatureSelection {

	public abstract List items(Geometry geometry);

	public abstract String getRendererContentID();
	public abstract void selectItems(Layer layer, Collection features);

}