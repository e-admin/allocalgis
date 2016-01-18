package com.geopista.server.administradorCartografia;

import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;

import com.geopista.model.GeopistaMap;

public interface IACMap {

	public abstract Collection getLayerStyles();

	public abstract void setLayerStyleData(Collection layerStyleData);

	public abstract Hashtable getLayerFamilies();

	public abstract void setLayerFamilies(Hashtable layerFamilies);

	public abstract String getId();

	public abstract void setId(String id);

	public abstract String getName();

	public abstract void setName(String name);

	public abstract String getXml();

	public abstract void setXml(String xml);

	public abstract Object getImage();

	public abstract void setImage(byte[] image);

	public abstract Date gettimeStamp();

	public abstract void settimeStamp(java.sql.Timestamp timeStamp);

	public abstract GeopistaMap convert(Hashtable htLayers,
			Hashtable htStyleXMLs, Date hora, String sLocale);

	/**
	 * @return Returns the idMunicipio.
	 */
	public abstract int getIdMunicipio();

	/**
	 * @param idMunicipio The idMunicipio to set.
	 */
	public abstract void setIdMunicipio(int idMunicipio);

	/**
	 * @return Returns the idEntidad.
	 */
	public abstract int getIdEntidad();

	/**
	 * @param idEntidad The idEntidad to set.
	 */
	public abstract void setIdEntidad(int idEntidad);

	public abstract Collection getMapServerLayers();

	public abstract void setMapServerLayers(Collection mapServerLayers);

	public abstract String getMapProjection(String projection);

	public abstract String getIdEntidadSeleccionada();

	public abstract void setIdEntidadSeleccionada(String idEntidadSeleccionada);

	public abstract String getLocale();

	public abstract void setLocale(String locale);

}