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


package com.geopista.app.layerutil.schema.attribute;


/**
 * Modelo para mostrar atributos en la tabla de atributos de una capa de GeoPISTA
 * 
 * @author cotesa
 *
 */

import javax.swing.table.AbstractTableModel;

import com.geopista.app.AppContext;
import com.geopista.app.layerutil.schema.table.TableRow;
import com.geopista.feature.Attribute;
import com.geopista.feature.Column;
import com.vividsolutions.jump.I18N;

public class TableAttributesModel extends AbstractTableModel {
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
    private String[] columnNames = {I18N.get("GestorCapas","layers.atributos.origendatos"),
            I18N.get("GestorCapas","layers.atributos.nombreatributo"),
            I18N.get("GestorCapas","layers.atributos.sql"),
            I18N.get("GestorCapas","layers.atributos.editable")};
    
    private TableRow[] data= new TableRow[0];
    
    /**
     * @return número de columnas de la tabla
     */
    public int getColumnCount() {
        return columnNames.length;
    }
    
    /**
     * @return número de filas de la tabla
     */
    public int getRowCount() {
        return data.length;
    }
    
    
    /**
     * Devuelve el nombre de la columna solicitada
     * @param col Índice de la columna
     * @return nombre de la columna
     */
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    /**
     * Devuelve el objeto que contiene la celda en la posición indicada
     * @param row Índice de la fila
     * @param col Índice de la columna
     * 
     * @return Objeto contenido en la posición seleccionada
     */
    public Object getValueAt(int row, int col) {
        
        if (data[row]==null)
            return null;
        
        switch (col)
        {
        case 0: 
            return data[row].getColumna();
        case 1: 
            return data[row].getAtributo();
        case 2: 
            return data[row].getSqlQuery();
        case 3: 
            return data[row].getEditable();
        default: 
            return null;
        
        }
        
    }
    
    /**
     * Devuelve el TableRow completo de la fila seleccionada
     * @param row Índice de la fila cuyo TableRow se solicita
     * @return TableRow completo
     */
    public TableRow getValueAt(int row) {
        
        return data[row];
        
    }
    
    
    /**
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  
     */
    public Class getColumnClass(int c) {
        //System.out.println("clase: " +getValueAt(0, c).getClass());
        if (getValueAt(0, c)!=null)        
            return getValueAt(0, c).getClass();
        else
            return String.class;
    }
    
    /*
     * Don't need to implement this method unless your table is
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col ==0) {
            return false;
        } else {
            return true;
        }
    }
    
    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
        
        //    	System.out.println("Setting value at " + row + "," + col
        //    			+ " to " + value
        //    			+ " (an instance of "
        //    			+ value.getClass() + ")");
        
        if (data[row] !=null)
        {    	
            switch (col)
            {
            case 0: 
                data[row].setColumna((Column)value);
                break;
            case 1: 
                data[row].setAtributo((Attribute)value);
                break;
            case 2: 
                data[row].setSqlQuery((String)value);
                break;
            case 3: 
                data[row].setEditable((Boolean)value);
                break;
            default: 
                break;
            
            
            }
            
            
            fireTableCellUpdated(row, col);
        }
        
        //System.out.println("New value of data:");
        //printDebugData();
        
    }
    
    /**
     * Establece el valor de una fila a un TableRow
     * @param value TableRow 
     * @param row Índice de la fila
     */
    public void setValueAt(TableRow value, int row) {
        
        
        setValueAt(value.getColumna(), row, 0);
        setValueAt(value.getAtributo(), row, 1);
        setValueAt(value.getSqlQuery(), row, 2);
        setValueAt(value.getEditable(), row, 3);
    }      
  
    /**
     * Inserta una nueva fila en la tabla
     * @param valores TableRow con la nueva fila
     */
    public void insertRow (TableRow valores){
        insertRow(valores, -1);
    }
    
    /**
     * Inserta una nueva fila en la tabla en la posición indicada
     * @param valores TableRow con la nueva fila
     * @param fila Posición de la fila
     */
    public void insertRow (TableRow valores, int fila){
        int longitud = getRowCount();
        data = new TableRow[longitud+1];
        if (fila==-1) 
            fila = longitud;       
        
        this.setValueAt(valores, fila);    
        
    }
    
    /**
     * Establece los datos mostrados en el modelo
     * @param datos Datos a mostrar en el modelo
     */
    public void setData (TableRow[] datos)
    {
        this.data = datos;
    }
    
    /**
     * Recupera los datos del modelo
     * @return Datos del modelo
     */
    public TableRow[] getData (){
        return data;
    }
    
}