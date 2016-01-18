package com.geopista.server.administradorCartografia;

import com.geopista.feature.Column;
import com.geopista.feature.Domain;
import com.geopista.feature.Table;

public interface IACColumn {

	public abstract int getLength();

	public abstract void setLength(int length);

	public abstract int getPrecision();

	public abstract void setPrecision(int precision);

	public abstract int getScale();

	public abstract void setScale(int scale);

	public abstract int getType();

	public abstract void setType(int type);

	public abstract int getId();

	public abstract void setId(int id);

	public abstract String getName();

	public abstract void setName(String name);


	public abstract int getDomainLevel();

	public abstract void setDomainLevel(int domainLevel);


	public abstract Column convert(Table t, Domain d);
	public abstract IACTable getTable();
	public abstract IACDomain getDomain();

}