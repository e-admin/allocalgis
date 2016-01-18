package com.geopista.server.administradorCartografia;

import com.geopista.feature.Table;

public interface IACTable {

	public abstract int getId();

	public abstract void setId(int id_table);

	public abstract String getName();

	public abstract void setName(String sName);

	public abstract int getGeometryType();

	public abstract void setGeometryType(int geometryType);

	public abstract Table convert();

}