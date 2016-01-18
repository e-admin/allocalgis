/**
 * TablaCamposExternosModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.layerutil.datosexternos;


/**
 * Modelo para mostrar atributos en la tabla de atributos de una capa de GeoPISTA
 * 
 * @author cotesa
 *
 */

import javax.swing.table.AbstractTableModel;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.I18N;

public class TablaCamposExternosModel extends AbstractTableModel {
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
    private String[] columnNames = {I18N.get("GestorCapas","datosExternos.camposExternos.origen"),
            I18N.get("GestorCapas","datosExternos.camposExternos.nombre"),
            I18N.get("GestorCapas","datosExternos.camposExternos.tipo")};
    
    private TablaCamposExternosRow[] data= new TablaCamposExternosRow[0];
    
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
            return data[row].getOrigen();
        case 1: 
            return data[row].getNombre();
        case 2: 
            return data[row].getTipo();
        default: 
            return null;
        
        }
        
    }
    
    /**
     * Devuelve el TableRow completo de la fila seleccionada
     * @param row Índice de la fila cuyo TableRow se solicita
     * @return TableRow completo
     */
    public TablaCamposExternosRow getValueAt(int row) {
        
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
                data[row].setOrigen((String)value);
                break;
            case 1: 
                data[row].setNombre((String)value);
                break;
            case 2: 
                data[row].setTipo((String)value);
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
     * @param value TablaCamposExternosRow 
     * @param row Índice de la fila
     */
    public void setValueAt(TablaCamposExternosRow value, int row) {
        
        
        setValueAt(value.getOrigen(), row, 0);
        setValueAt(value.getNombre(), row, 1);
        setValueAt(value.getTipo(), row, 2);
    }      
  
    /**
     * Inserta una nueva fila en la tabla
     * @param valores TablaCamposExternosRow con la nueva fila
     */
    public void insertRow (TablaCamposExternosRow valores){
        insertRow(valores, -1);
    }
    
    /**
     * Inserta una nueva fila en la tabla en la posición indicada
     * @param valores TableRow con la nueva fila
     * @param fila Posición de la fila
     */
    public void insertRow (TablaCamposExternosRow valores, int fila){
        int longitud = getRowCount();
        data = new TablaCamposExternosRow[longitud+1];
        if (fila==-1) 
            fila = longitud;       
        
        this.setValueAt(valores, fila);    
        
    }
    
    /**
     * Establece los datos mostrados en el modelo
     * @param datos Datos a mostrar en el modelo
     */
    public void setData (TablaCamposExternosRow[] datos)
    {
        this.data = datos;
    }
    
    /**
     * Recupera los datos del modelo
     * @return Datos del modelo
     */
    public TablaCamposExternosRow[] getData (){
        return data;
    }
    
}