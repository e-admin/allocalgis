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


package com.geopista.app.layerutil.schema.table;


/**
 * Bean con los datos de un atributo de una capa de geopista
 * 
 * @author cotesa
 *
 */

import com.geopista.feature.Attribute;
import com.geopista.feature.Column;

public class TableRow {
	
	Column columna;
	Attribute atributo;
	String sqlQuery;
	Boolean editable;
	
    /**
     * Constructor por defecto
     *
     */
	public TableRow ()
	{
		super();
	}
	
    /**
     * Constructor de la clase
     * 
     * @param columna Column del atributo
     * @param atributo Attribute del atributo
     * @param sqlQuery Cadena en la que se especifica una consulta SQL del atributo
     * @param editable Verdadero si el atributo es editable
     */
	public TableRow (Column columna, Attribute atributo, String sqlQuery, Boolean editable)
	{
		this.columna = columna;
		this.columna.setAttribute(atributo);
		this.atributo = atributo;
		this.sqlQuery = sqlQuery;
		this.editable = editable;
	}
	
    /**
     * Constructor de la clase
     * 
     * @param columna Column del atributo
     * @param atributo Attribute del atributo
     * @param sqlQuery Cadena en la que se especifica una consulta SQL del atributo
     * @param editable Verdadero si el atributo es editable
     */
	public TableRow (Column columna, Attribute atributo, String sqlQuery, boolean editable)
	{
		this.columna = columna;
		this.columna.setAttribute(atributo);
		this.atributo = atributo;
		this.sqlQuery = sqlQuery;
		this.editable = new Boolean(editable);
	}

	/**
	 * @return Returns the atributo.
	 */
	public Attribute getAtributo() {
		return atributo;
	}

	/**
	 * @param atributo The atributo to set.
	 */
	public void setAtributo(Attribute atributo) {
		this.atributo = atributo;
	}

	/**
	 * @return Returns the columna.
	 */
	public Column getColumna() {
		return columna;
	}

	/**
	 * @param columna The columna to set.
	 */
	public void setColumna(Column columna) {
		this.columna = columna;
	}

	/**
	 * @return Returns the editable.
	 */
	public Boolean getEditable() {
		return editable;
	}

	/**
	 * @param editable The editable to set.
	 */
	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	/**
	 * @return Returns the sqlQuery.
	 */
	public String getSqlQuery() {
		return sqlQuery;
	}

	/**
	 * @param sqlQuery The sqlQuery to set.
	 */
	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}
    
    public int hashCode() 
    {
        if (this.columna!=null)
            return this.columna.getIdColumn();
        else 
            return 0;
    }
	
    public boolean equals(Object o) {
        /*
         if (!(o instanceof TableRow)) return false;
         if (((TableRow)o).getSqlQuery()!=null && ((TableRow)o).getSqlQuery()==this.getSqlQuery()) 
         return true;
         if (((TableRow)o).getSqlQuery()!=null && ((TableRow)o).getSqlQuery()!=this.getSqlQuery()) 
         return false;
         if (((TableRow)o).getColumna()==null) 
         return true;
         if ( ((TableRow)o).getColumna().getIdColumn() == this.getColumna().getIdColumn())
         return true;
         else
         return false;
         */
        
        if (!(o instanceof TableRow)) return false;
        
        if ( ((TableRow)o).getColumna().getIdColumn() == this.getColumna().getIdColumn()
                || ((TableRow)o).getColumna()==null)
            return true;
        else
            return false;
        
        
    }

}
