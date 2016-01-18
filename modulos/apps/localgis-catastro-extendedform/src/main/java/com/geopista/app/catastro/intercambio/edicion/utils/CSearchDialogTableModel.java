/**
 * CSearchDialogTableModel.java
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

import com.geopista.protocol.catastro.Via;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 19-may-2005
 * Time: 15:32:30
 * To change this template use File | Settings | File Templates.
 */
public class CSearchDialogTableModel  extends AbstractTableModel{
    
    public String[] columnNames= {""};
    private ArrayList lstVias = new ArrayList();
    
    
    public CSearchDialogTableModel(String[] colNames) {
        columnNames= colNames;
        new DefaultTableModel(columnNames, 0);
    }
    
    public void setColumnNames(String[] colNames) {
        columnNames= colNames;
    }
    
    
    public int getColumnCount() {
        return columnNames.length;
    }
    
    
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    public boolean isCellEditable(int row, int col) {
        return false;
    }
    
    /**
     * @return número de filas de la tabla
     */
    public int getRowCount() {
        return lstVias.size();
    }
    
    
    /**
     * Devuelve la Via completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto Via se solicita
     * @return Via completa
     */
    public Via getValueAt(int row) {
        
        return (Via)lstVias.get(row);
        
    }
    
    
    /**
     * Devuelve el objeto que contiene la celda en la posición indicada
     * @param row Índice de la fila
     * @param col Índice de la columna
     * 
     * @return Objeto contenido en la posición seleccionada
     */
    public Object getValueAt(int row, int col) {
        
        if (lstVias.get(row)==null)
            return null;
        
        switch (col)
        {
        case 0: 
            return ((Via)lstVias.get(row)).getTipoViaNormalizadoCatastro();
        case 1: 
            return ((Via)lstVias.get(row)).getNombreCatastro();
            
        default: 
            return null;
        
        }        
    }
    /**
     * Establece los datos mostrados en el modelo
     * @param datos Datos a mostrar en el modelo
     */
    public void setData (ArrayList datos)
    {
        this.lstVias = datos;
    }
}
