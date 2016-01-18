/**
 * GeopistaSchemaAccesor.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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