/*
 * 
 * Created on 01-abr-2005 by juacas
 *
 * 
 */
package com.geopista.bean;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class GeopistaMapProxy
{
String name;
String basePath;
boolean extracted;
public boolean isExtracted()
{
return extracted;
}
public void setExtracted(boolean extracted)
{
this.extracted = extracted;
}
public String getName()
{
return name;
}
public void setName(String name)
{
this.name = name;
}
public String getBasePath()
{
return basePath;
}
public void setBasePath(String path)
{
this.basePath = path;
}
	/**
	 * 
	 */
	public GeopistaMapProxy()
	{
	super();
	// TODO Auto-generated constructor stub
	}

}
