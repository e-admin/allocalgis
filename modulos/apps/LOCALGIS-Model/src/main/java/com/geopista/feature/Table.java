/**
 * Table.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.feature;

import java.util.HashMap;

/**
 * @author juacas
 *
 *Representación de una Tabla
 */
public class Table {
	
	/**
	 * 
	 */
	private String name;
    private int idTabla;
	private String description;
	private HashMap columns=new HashMap();
	private int geometryType;
	private int external;

  public static final int GEOMETRY = 0;
  public static final int POINT = 1;
  public static final int LINESTRING = 3;
  public static final int POLYGON = 5;
  public static final int COLLECTION = 6;
  public static final int MULTIPOINT = 7;
  public static final int MULTILINESTRING = 9;
  public static final int MULTIPOLYGON = 11;
  

  public Table()
  {
    
  }
	
  	public Table(String name, String description, int external){
		super();
		// TODO Auto-generated constructor stub
		this.name=name;
		this.description=description;
		this.external=external;
  	}
  
	public Table(String name, String description) {
		super();
		// TODO Auto-generated constructor stub
		this.name=name;
		this.description=description;
		this.external= 0;
	}
	
	/**
	 * @return Returns the columns.
	 */
	public HashMap getColumns() {
		return columns;
	}
	/**
	 * @param columns The columns to set.
	 */
	public void setColumns(HashMap columns) {
		this.columns = columns;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param key
	 * @return
	 */
	public Column getColumn(Object key) {
		return (Column) columns.get(key);
	}
	/**
	 * @param key
	 * @param value
	 * @return
	 */
	public Column addColumn(Object key, Column value) {
		return (Column) columns.put(key, value);
	}

  public int getGeometryType()
  {
    return geometryType;
  }

  public void setGeometryType(int newGeometryType)
  {
    geometryType = newGeometryType;
  }

public int getIdTabla()
{
    return idTabla;
}

public void setIdTabla(int idTabla)
{
    this.idTabla = idTabla;
}

public int getExternal() {
	return external;
}

public void setExternal(int external) {
	this.external = external;
}
  
  
}
