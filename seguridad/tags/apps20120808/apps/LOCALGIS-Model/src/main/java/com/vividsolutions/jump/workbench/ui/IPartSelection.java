package com.vividsolutions.jump.workbench.ui;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

public interface IPartSelection {

	public abstract List items(Geometry geometry);

	public abstract String getRendererContentID();

}