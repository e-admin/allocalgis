/**
 * TableCodigoViasModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.catastro.intercambio.edicion.utils;


/**
 * Modelo para mostrar las vías en las tablas de vías
 * 
 * @author cotesa
 *
 */

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import com.geopista.protocol.catastro.Via;
import com.vividsolutions.jump.I18N;


public class TableCodigoViasModel extends AbstractTableModel {
    
    private String[] columnNames = {I18N.get("Expedientes","busqueda.codigovias.columna.codigovia")};
    
    private ArrayList lstCodigoVias = new ArrayList();
    
    
    public TableCodigoViasModel() {
        new DefaultTableModel(columnNames, 0);
    }
    
    public TableCodigoViasModel(String[] colNames) {
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
        return lstCodigoVias.size();
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
        
        if (lstCodigoVias.get(row)==null)
            return null;
        
        switch (col)
        {
        case 0: 
            return ((String)lstCodigoVias.get(row));
               
        default: 
            return null;
        
        }        
    }
    
    /**
     * Devuelve la Via completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto Via se solicita
     * @return Via completa
     */
    public Via getValueAt(int row) {
        
        return (Via)lstCodigoVias.get(row);
        
    }
    /**
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  
     */
    public Class getColumnClass(int c) {
        if (getValueAt(0, c)!=null)        
            return getValueAt(0, c).getClass();
        else
            return String.class;
    }
    
    public boolean isCellEditable(int row, int col) {
        return false;
    }
    
    
    /**
     * Establece los datos mostrados en el modelo
     * @param datos Datos a mostrar en el modelo
     */
    public void setData (ArrayList datos)
    {
        this.lstCodigoVias = datos;
    }
    
    /**
     * Recupera los datos del modelo
     * @return Datos del modelo
     */
    public ArrayList getData (){
        return lstCodigoVias;
    }    
}