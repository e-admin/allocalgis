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
 * Modelo para mostrar los datos de reparto
 * 
 * @author cotesa
 *
 */

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.vividsolutions.jump.I18N;


public class TableRepartoModel extends AbstractTableModel {
    
    private String[] columnNames = {
            
    		I18N.get("Expedientes","tabla.reparto.columna.pcatastral1"),
    		I18N.get("Expedientes","tabla.reparto.columna.pcatastral2"),
    		I18N.get("Expedientes","tabla.reparto.columna.cargo"),
            I18N.get("Expedientes","tabla.reparto.columna.destinatarioreparto")};
       
    private ArrayList lstElementos = new ArrayList();
    
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
        return lstElementos.size();
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
        
        if (lstElementos.get(row)==null)
            return null;
        
        switch (col)
        {
        case 0: 
            if (lstElementos.get(row) instanceof TableRowReparto){
            	TableRowReparto table = (TableRowReparto)lstElementos.get(row);
            	return table.getPcatastral1();
            }
        case 1: 
            if (lstElementos.get(row) instanceof TableRowReparto){
            	TableRowReparto table = (TableRowReparto)lstElementos.get(row);
            	return table.getPcatastral2();
            }
        case 2: 
            if (lstElementos.get(row) instanceof TableRowReparto){
            	TableRowReparto table = (TableRowReparto)lstElementos.get(row);
            	return table.getCargo();
            }
        
        case 3: 
            if (lstElementos.get(row) instanceof TableRowReparto){
            	TableRowReparto table = (TableRowReparto)lstElementos.get(row);
            	return table.getEditable();
            }
                       
        default: 
            return null;
        
        }        
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
    	if(col==3){
    		return true;
    	}
    	else{
    		return false;
    	}
    }
    
    /**
     * Establece los datos mostrados en el modelo
     * @param datos Datos a mostrar en el modelo
     */
    public void setData (ArrayList datos)
    {
        this.lstElementos = datos;
    }
   
    /**
     * Recupera los datos del modelo
     * @return Datos del modelo
     */
    public ArrayList getData (){
        return lstElementos;
    }
   
    public void setValueAt(Object value, int row, int col) {
        
    	TableRowReparto table;
        if (lstElementos.get(row) !=null)
        {    	
            switch (col)
            {
            case 0: 
            	table = (TableRowReparto)lstElementos.get(row);
            	table.setPcatastral1((String)value);
            	lstElementos.remove(row);
            	lstElementos.add(row, table);
                break;
            case 1: 
            	table = (TableRowReparto)lstElementos.get(row);
            	table.setPcatastral2((String)value);
            	lstElementos.remove(row);
            	lstElementos.add(row, table);
                break;
            case 2: 
            	table = (TableRowReparto)lstElementos.get(row);
            	table.setCargo((String)value);
            	lstElementos.remove(row);
            	lstElementos.add(row, table);
                break;
            case 3:             	
            	table = (TableRowReparto)lstElementos.get(row);
            	table.setEditable((Boolean)value);
            	lstElementos.remove(row);
            	lstElementos.add(row, table);
                break;
            
            default: 
                break;           
            
            }
                        
            fireTableCellUpdated(row, col);
        }
        
    }
    
    public void removeElements(){
    	
    	lstElementos.clear();
    	return;
    	
    }
       
}