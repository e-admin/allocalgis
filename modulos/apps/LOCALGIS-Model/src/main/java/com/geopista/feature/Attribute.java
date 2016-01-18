/**
 * Attribute.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.feature;

import java.util.Hashtable;

public class Attribute 
{
    private String name;
    private String type;
    private Column column;
    private GeopistaSchema schema;
    private String accessType = GeopistaSchema.READ_WRITE;
    private String category;
    
    
    private int systemID;
    private int idAlias;
    private int idLayer;
    private int position;
    private boolean editable;
    private Hashtable htTraducciones;
    
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String newName)
    {
        name = newName;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String newType)
    {
        type = newType;
    }
    
    public Column getColumn()
    {
        return column;
    }
    
    public void setColumn(Column newColumn)
    {
        column = newColumn;
        column.setAttribute(this);
    }
    
    public GeopistaSchema getSchema()
    {
        return schema;
    }
    
    public void setSchema(GeopistaSchema newSchema)
    {
        schema = newSchema;
    }
    
    public String getAccessType()
    {
        return accessType;
    }
    
    public void setAccessType(String newAccessType)
    {
        accessType = newAccessType;
    }
    
    
    
    public String getCategory()
    {
        return category;
    }
    public void setCategory(String category)
    {
        this.category = category;
    }
    
    public boolean isEditable()
    {
        return editable;
    }
    
    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }
    
    public int getIdAlias()
    {
        return idAlias;
    }
    
    public void setIdAlias(int idAlias)
    {
        this.idAlias = idAlias;
    }
    
    public int getIdLayer()
    {
        return idLayer;
    }
    
    public void setIdLayer(int idLayer)
    {
        this.idLayer = idLayer;
    }
    
    public int getPosition()
    {
        return position;
    }
    
    public void setPosition(int position)
    {
        this.position = position;
    }
    
    public int getSystemID()
    {
        return systemID;
    }
    
    public void setSystemID(int systemID)
    {
        this.systemID = systemID;
    }

    public Hashtable getHtTraducciones()
    {
        return htTraducciones;
    }

    public void setHtTraducciones(Hashtable htTraducciones)
    {
        this.htTraducciones = htTraducciones;
    }

	@Override
	public String toString() {
		return "Attribute [name=" + name + ", column=" + column + "]";
	}
    
    
    
}