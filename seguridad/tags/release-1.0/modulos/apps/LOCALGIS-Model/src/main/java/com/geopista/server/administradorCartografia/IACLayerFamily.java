package com.geopista.server.administradorCartografia;

import java.util.Hashtable;

import com.vividsolutions.jump.workbench.model.ILayerManager;

public interface IACLayerFamily {

	public abstract int getId();

	public abstract void setId(int id);

	public abstract String getName();

	public abstract void setName(String name);

	public abstract Hashtable getLayers();

	public abstract void setLayers(Hashtable layers);

	public abstract String getDescription();

	public abstract void setDescription(String description);

	public abstract void convert(ILayerManager layerManager, int iPosition,
			Hashtable htLayers, Hashtable htStyleXMLs);

}