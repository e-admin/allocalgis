/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 *
 */


package com.geopista.app.layerutil.dbtable;



/**
 * Bean que representa una columna de Base de Datos
 * 
 * @author cotesa
 *
 */

public class ColumnDB
{
    
    private String   name;
    private String   type;
    private int      length;
    private int      precission;
    private String   description;
    private boolean  isNotNull;
    private boolean  isUnique;
    private boolean  isPrimary;
    private String   defaultValue;
    private String   tableName;
        
    public static final int GEOMETRY = 1;
    public static final int NUMERIC = 2;
    public static final int VARCHAR = 3;
    public static final int DATE = 4;
    public static final int BOOLEAN = 5;
    
    /**
     * Constructor de la clase
     *
     */
    public ColumnDB()
    {
      
    }
    /**
     * Constructor de la clase
     * @param coldb ColumnDB a partir de la que se desea generar la nueva ColumnDB
     */
    public ColumnDB(ColumnDB coldb)
    {
       this.name= new String (coldb.getTableName());
       this.type = new String (coldb.getType());
       this.length = new Integer (coldb.getLength()).intValue();
       this.precission = new Integer (coldb.getPrecission()).intValue();
       this.description = new String (coldb.getDescription());
       this.isNotNull = new Boolean (coldb.isNotNull()).booleanValue();
       this.isUnique = new Boolean (coldb.isUnique()).booleanValue();
       this.isPrimary = new Boolean (coldb.isPrimary()).booleanValue();
       this.defaultValue = new String (coldb.getDefaultValue());
       this.tableName = new String(coldb.getTableName());
    }
    
    /**
     * @return Returns the defaultValue.
     */
    public String getDefaultValue()
    {
        return defaultValue;
    }
    /**
     * @param defaultValue The defaultValue to set.
     */
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }
    /**
     * @return Returns the description.
     */
    public String getDescription()
    {
        return description;
    }
    /**
     * @param description The description to set.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }
    /**
     * @return Returns the isNotNull.
     */
    public boolean isNotNull()
    {
        return isNotNull;
    }
    /**
     * @param isNull The isNull to set.
     */
    public void setNotNull(boolean isNotNull)
    {
        this.isNotNull = isNotNull;
    }
    /**
     * @return Returns the isUnique.
     */
    public boolean isUnique()
    {
        return isUnique;
    }
    /**
     * @param isUnique The isUnique to set.
     */
    public void setUnique(boolean isUnique)
    {
        this.isUnique = isUnique;
    }
    /**
     * @return Returns the length.
     */
    public int getLength()
    {
        return length;
    }
    /**
     * @param length The length to set.
     */
    public void setLength(int length)
    {
        this.length = length;
    }
    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    /**
     * @return Returns the type.
     */
    public String getType()
    {
        return type;
    }
    /**
     * @param type The type to set.
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * @return Returns the tableName.
     */
    public String getTableName()
    {
        return tableName;
    }

    /**
     * @param tableName The tableName to set.
     */
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    /**
     * @return Returns the precission.
     */
    public int getPrecission()
    {
        return precission;
    }

    /**
     * @param precission The precission to set.
     */
    public void setPrecission(int precission)
    {
        this.precission = precission;
    }   
    
    /**
     * Obtiene el tipo de geometria
     * 
     * @param item Cadena que representa el tipo de geometria que tiene la columna
     * @return Valor entero que representa el tipo de geometria de la columna 
     * de acuerdo a su representacion en la Base de Datos
     */
    public static int getGeometryType(String item)
    {
        if (item.equalsIgnoreCase(TablesDBPanel.COL_GEOMETRY))
            return ColumnDB.GEOMETRY;
        else if (item.equalsIgnoreCase(TablesDBPanel.COL_INTEGER)|| item.equalsIgnoreCase(TablesDBPanel.COL_INT4)
                || item.equalsIgnoreCase(TablesDBPanel.COL_NUMERIC) )
            return ColumnDB.NUMERIC;
        else if (item.equalsIgnoreCase(TablesDBPanel.COL_CHAR) 
                || item.equalsIgnoreCase(TablesDBPanel.COL_VARCHAR))
            return ColumnDB.VARCHAR;
        else if (item.equalsIgnoreCase(TablesDBPanel.COL_DATE)
                || item.equalsIgnoreCase(TablesDBPanel.COL_TIMESTAMP))
            return ColumnDB.DATE;
        else if (item.equalsIgnoreCase(TablesDBPanel.COL_BOOLEAN))
            return ColumnDB.BOOLEAN;        
        else 
            return 0;        
        
    }
    /**
     * Compara la igualdad entre objetos
     * @param o Objeto con el que se desea comparar el actual
     * @return Verdadero si el objeto a comparar coincide con el actual
     */
    public boolean equals(Object o) {
        if (!(o instanceof ColumnDB)) return false;
        
        if ((((ColumnDB)o).getDefaultValue()==null ||
                ((ColumnDB)o).getDefaultValue().equals(this.getDefaultValue())) &&
                ((ColumnDB)o).isNotNull()==this.isNotNull()&&
                ((ColumnDB)o).isUnique()==this.isUnique() &&
                (((ColumnDB)o).getDescription()==null ||
                        ((ColumnDB)o).getDescription().equals(this.getDescription())) &&
                        ((ColumnDB)o).getLength()==this.getLength() &&
                        (((ColumnDB)o).getName()==null ||
                                ((ColumnDB)o).getName().equals(this.getName())) &&
                                ((ColumnDB)o).getPrecission()==this.getPrecission()&&
                                (((ColumnDB)o).getTableName()==null ||
                                        ((ColumnDB)o).getTableName().equals(this.getTableName()))&&
                                        (((ColumnDB)o).getType()==null ||
                                        ((ColumnDB)o).getType().equals(this.getType()))
                                        
        ) 
            return true;
        
        else
            return false;
    }

    /**
     * @return Returns the isPrimary.
     */
    public boolean isPrimary()
    {
        return isPrimary || isUnique;
    }

    /**
     * @param isPrimary The isPrimary to set.
     */
    public void setPrimary(boolean isPrimary)
    {
        this.isPrimary = isPrimary;
    }
}
