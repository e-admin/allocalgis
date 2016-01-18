package com.geopista.server.administradorCartografia;

import java.util.HashMap;

import com.geopista.model.DynamicLayer;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.workbench.model.LayerManager;

public interface IACDynamicLayer {

	public abstract void convertWMS(LayerManager layerManager,
			FeatureDataset features);

	public abstract DynamicLayer convertDynamic(LayerManager layerManager);

	public abstract HashMap getSelectedStyles();

	public abstract/*static */void setSelectedStyles(HashMap ss);

	public abstract String getTime();

	public abstract void setTime(String time);

}