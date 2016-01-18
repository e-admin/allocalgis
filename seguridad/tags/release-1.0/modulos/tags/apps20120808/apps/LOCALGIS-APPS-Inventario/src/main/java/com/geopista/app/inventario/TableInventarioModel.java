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


package com.geopista.app.inventario;


/**
 * Modelo para mostrar las vías en las tablas de vías
 * 
 * @author cotesa
 *
 */

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import com.geopista.app.AppContext;
import com.geopista.protocol.licencias.CReferenciaCatastral;


public class TableInventarioModel extends AbstractTableModel {
    
    private String[] columnNames = {AppContext.getApplicationContext().getI18nString("inventario.bienes.columna0"),
    		AppContext.getApplicationContext().getI18nString("inventario.bienes.columna1"),
    		AppContext.getApplicationContext().getI18nString("inventario.bienes.columna2"),
    		AppContext.getApplicationContext().getI18nString("inventario.bienes.columna3"),
    		AppContext.getApplicationContext().getI18nString("inventario.bienes.columna4"),
    		AppContext.getApplicationContext().getI18nString("inventario.bienes.columna5"),
    		//AppContext.getApplicationContext().getI18nString("inventario.bienes.columna6"), 
    		"HIDDEN"};
    
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
            return ((CReferenciaCatastral)lstElementos.get(row)).getReferenciaCatastral();
        case 1: 
            return ((CReferenciaCatastral)lstElementos.get(row)).getTipoVia();
        case 2: 
            return ((CReferenciaCatastral)lstElementos.get(row)).getNombreVia();
        case 3: 
            return ((CReferenciaCatastral)lstElementos.get(row)).getPrimerNumero();
        case 4: 
        	return ((CReferenciaCatastral)lstElementos.get(row)).getPrimeraLetra();
        case 5: 
        	return ((CReferenciaCatastral)lstElementos.get(row)).getBloque();
        case 6: 
        	return ((CReferenciaCatastral)lstElementos.get(row)).getCPostal();        
        default: 
            return null;
        
        }        
    }
    
    /**
     * Devuelve la FincaCatastro completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto FincaCatastro se solicita
     * @return FincaCatastro completa
     */
    public CReferenciaCatastral getValueAt(int row) {
        
        return (CReferenciaCatastral)lstElementos.get(row);
        
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
        this.lstElementos = datos;
    }
    
    /**
     * Recupera los datos del modelo
     * @return Datos del modelo
     */
    public ArrayList getData (){
        return lstElementos;
    }    
}