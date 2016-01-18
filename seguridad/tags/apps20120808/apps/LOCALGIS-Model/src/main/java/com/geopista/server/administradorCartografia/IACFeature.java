package com.geopista.server.administradorCartografia;

import java.io.Serializable;
import java.util.HashMap;

import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;

public interface IACFeature {

	public abstract String getGeometry();

	public abstract void setGeometry(String geometry);

	public abstract HashMap getAttributes();

	public abstract void setAttributes(HashMap attributes);

	public abstract void setAttribute(String sKey, Serializable oValue);

	public abstract Object getAttribute(String sKey);


	/** Obtiene un nuevo GeopistaFeature a partir de estos datos */
	public abstract GeopistaFeature convert(GeopistaSchema schema)
			throws NoIDException;

	public abstract String getError();

}