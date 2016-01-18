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


package com.geopista.app.layerutil.schema.column;


/**
 * Bean que representa una columna completa, con su representación en el sistema
 * y en la base de datos
 * 
 * @author cotesa
 *
 */
import com.geopista.app.layerutil.dbtable.ColumnDB;
import com.geopista.feature.Column;

public class ColumnRow implements Cloneable 
{
    
    private Column columnaSistema = new Column();
    private ColumnDB columnaBD = new ColumnDB();
    
    /**
     * Constructor por defecto de la clase
     *
     */
    public ColumnRow ()
    {
        super();
    }
    
    public ColumnRow (ColumnRow cr)
    {
        this.columnaBD = new ColumnDB(cr.getColumnaBD());
        this.columnaSistema = new Column();
        columnaSistema.setAttribute(cr.getColumnaSistema().getAttribute());
        columnaSistema.setDescription(cr.getColumnaSistema().getDescription());
        columnaSistema.setDomain(cr.getColumnaSistema().getDomain());
        columnaSistema.setIdColumn(cr.getColumnaSistema().getIdColumn());
        columnaSistema.setLevel(cr.getColumnaSistema().getLevel());
        columnaSistema.setName(cr.getColumnaSistema().getName());
        columnaSistema.setTable(cr.getColumnaSistema().getTable());
    }
    
    /**
     * Constructor de la clase
     * @param colSistema Columna de sistema
     * @param colBD Columna de Base de datos
     */
    public ColumnRow (Column colSistema, ColumnDB colBD)
    {
        this.columnaBD       =  colBD;
        this.columnaSistema  =  colSistema;
    }
    
    
    /**
     * @return Returns the columnaBD.
     */
    public ColumnDB getColumnaBD()
    {
        return columnaBD;
    }

    /**
     * @param columnaBD The columnaBD to set.
     */
    public void setColumnaBD(ColumnDB columnaBD)
    {
        this.columnaBD = columnaBD;
    }

    /**
     * @return Returns the columnaSistema.
     */
    public Column getColumnaSistema()
    {
        return columnaSistema;
    }

    /**
     * @param columnaSistema The columnaSistema to set.
     */
    public void setColumnaSistema(Column columnaSistema)
    {
        this.columnaSistema = columnaSistema;
    }

    /**
     * @return Código hash de la clase
     */
    public int hashCode() 
    {
        if (this.columnaSistema!=null)
            return this.columnaSistema.getIdColumn();
        else 
            return 0;
    }
    
    /**
     * Compara la igualdad entre objetos
     * @param o Objeto con el que se desea comparar el actual
     * @return Verdadero si el objeto a comparar coincide con el actual
     */
    public boolean equals(Object o) {
        if (!(o instanceof ColumnRow)) return false;
        
        if (((ColumnRow)o).getColumnaBD().equals(this.columnaBD) &&
                ((ColumnRow)o).getColumnaSistema().equals(this.columnaSistema)) 
            return true;
        
        else
            return false;
    }

    public Object clone() {
        Cloneable theClone = new ColumnRow();
        return theClone;
    }
}
