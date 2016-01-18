/**
 * TableDatosPersonalesModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.edicion.utils;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import com.geopista.protocol.datosPersonales.DatosPersonales;
import com.vividsolutions.jump.I18N;

public class TableDatosPersonalesModel extends AbstractTableModel{

	
	private String[] columnNames = {I18N.get("Expedientes","busqueda.vias.columna.tipo"),
            I18N.get("Expedientes","busqueda.vias.columna.nombre")};

	private ArrayList lstTitulares = new ArrayList();
	 
	 
	public TableDatosPersonalesModel() {
	        new DefaultTableModel(columnNames, 0);
	}
	    
	public TableDatosPersonalesModel(String[] colNames) {
	        columnNames= colNames;
	        new DefaultTableModel(columnNames, 0);
	}
	
	 
	
	
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
		 return lstTitulares.size();
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
        
        if (lstTitulares.get(row)==null)
            return null;
        
        switch (col)
        {
        case 0: 
            return ((DatosPersonales)lstTitulares.get(row)).getNif();
        case 1: 
            return ((DatosPersonales)lstTitulares.get(row)).getNombreApellidos();
       
        default: 
            return null;
        
        }        
    }
	
    /**
     * Devuelve los titulares completos de la fila seleccionada
     * @param row Índice de la fila cuyo objeto Via se solicita
     * @return Via completa
     */
    public DatosPersonales getValueAt(int row) {
        
        return (DatosPersonales)lstTitulares.get(row);
        
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
    
    public boolean isCellEditable(int row, int col) {
        return false;
    }
    
    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {        
       
        if (lstTitulares.get(row) !=null)
        {    	
            switch (col)
            {
            case 0: 
                ((DatosPersonales)lstTitulares.get(row)).setNif(value.toString());
                break;
            case 1: 
                ((DatosPersonales)lstTitulares.get(row)).setNombreApellidos(value.toString());
                break;
            default: 
                break;
            }            
            
            fireTableCellUpdated(row, col);
        }        
    }  
    
    /**
     * Establece los datos mostrados en el modelo
     * @param datos Datos a mostrar en el modelo
     */
    public void setData (ArrayList datos)
    {
        this.lstTitulares = datos;
    }
    
    /**
     * Recupera los datos del modelo
     * @return Datos del modelo
     */
    public ArrayList getData (){
        return lstTitulares;
    }    


}
