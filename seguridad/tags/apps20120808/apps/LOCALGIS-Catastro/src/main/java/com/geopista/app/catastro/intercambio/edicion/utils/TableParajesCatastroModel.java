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


package com.geopista.app.catastro.intercambio.edicion.utils;


/**
 * Modelo para mostrar los parajes en las tablas de parajes
 * 
 * @author cotesa
 *
 */

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import com.geopista.app.catastro.model.beans.Paraje;
import com.vividsolutions.jump.I18N;


public class TableParajesCatastroModel extends AbstractTableModel {
    
    private String[] columnNames = {
            I18N.get("Expedientes","busqueda.parajes.columna.nombre"),
            I18N.get("Expedientes","busqueda.parajes.columna.codigo")};
    
    private ArrayList lstParajes = new ArrayList();
    
    
    public TableParajesCatastroModel() {
        new DefaultTableModel(columnNames, 2);
    }
    
    public TableParajesCatastroModel(String[] colNames) {
        columnNames= colNames;
        new DefaultTableModel(columnNames, 2);
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
        return lstParajes.size();
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
        
        if (lstParajes.get(row)==null)
            return null;
        
        switch (col)
        {
        case 0: 
            //return ((String)lstParajes.get(row));
        	return ((Paraje)lstParajes.get(row)).getNombre();
        case 1: 
        	return new Integer(((Paraje)lstParajes.get(row)).getCodigo());
       
        default: 
            return null;
        
        }        
    }
    
    /**
     * Devuelve el paraje de la fila seleccionada
     * @param row Índice de la fila cuyo objeto Via se solicita
     * @return String con el nombre del paraje
     */
    public String getValueAt(int row) {
        
        return (String)lstParajes.get(row);
        
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
    /*public void setValueAt(Object value, int row, int col) {        
       
        if (lstParajes.get(row) !=null)
        {    	
            switch (col)
            {
            case 0: 
                
                lstParajes.set(row, value.toString());
                break;
                
           
            default: 
                break;
            }            
            
            fireTableCellUpdated(row, col);
        }        
    }  */
    
    /**
     * Establece los datos mostrados en el modelo
     * @param datos Datos a mostrar en el modelo
     */
    public void setData (ArrayList datos)
    {
        this.lstParajes = datos;
    }
    
    /**
     * Recupera los datos del modelo
     * @return Datos del modelo
     */
    public ArrayList getData (){
        return lstParajes;
    }    
}