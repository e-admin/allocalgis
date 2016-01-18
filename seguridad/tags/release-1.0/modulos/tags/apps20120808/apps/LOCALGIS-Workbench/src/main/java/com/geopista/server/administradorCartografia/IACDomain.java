package com.geopista.server.administradorCartografia;

public interface IACDomain {

	public abstract int getId();

	public abstract void setId(int id);

	public abstract String getName();

	public abstract void setName(String name);



	public abstract com.geopista.feature.Domain convert(String sLocale);

}