/*
 * 
 * Created on 28-mar-2005 by juacas
 *
 * 
 */
package com.geopista.feature;



/**
 * TODO Documentación
 * @author juacas
 *
 */
public interface GeopistaSchemaAccesor
{
	/**
	 * Obtiene el atributo que está enlazado con la columna de nombre colname
	 * @param colname el nombre de la columna
	 * @return null si no hay resultado.
	 */
	public abstract String getAttributeByColumn(String colname);

	public abstract Column getColumnByAttribute(int index);

	public abstract Domain getAttributeDomain(int index);

	/**
	 * Gets the attribute Name mapped from Column col in this schema
	 * @param col
	 * @return
	 */
	public abstract String getAttribute(Column col);

	/**
	 * @param attName
	 * @return
	 */
	public abstract Domain getAttributeDomain(String attName);

	/**
	 * @param attName
	 * @return
	 */
	public abstract Column getColumnByAttribute(String attName);

	/**
	 * @param index
	 * @return
	 */
	public abstract String getAttributeAccess(int index);

	public abstract String getAttributeAccess(String attName);
}