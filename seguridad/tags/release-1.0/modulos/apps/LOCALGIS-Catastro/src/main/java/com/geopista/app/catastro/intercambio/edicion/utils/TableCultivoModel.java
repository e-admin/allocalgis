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
 * Modelo para mostrar los locales en las tablas de locales
 * 
 * @author cotesa
 *
 */

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.geopista.app.catastro.model.beans.ConstruccionCatastro;
import com.geopista.app.catastro.model.beans.Cultivo;
import com.vividsolutions.jump.I18N;


public class TableCultivoModel extends AbstractTableModel {
    
    private String[] columnNames = {
    		I18N.get("Expedientes","tabla.cultivo.columna.subparcela"),
            I18N.get("Expedientes","tabla.cultivo.columna.tiposubparcela"),            
            I18N.get("Expedientes","tabla.cultivo.columna.calificacioncatastral"),            
            I18N.get("Expedientes","tabla.cultivo.columna.intensidadproductiva"),
            I18N.get("Expedientes","tabla.cultivo.columna.cargo"),
    		I18N.get("Expedientes","tabla.cultivo.columna.clase")};
    
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
            return ((Cultivo)lstElementos.get(row)).getCodSubparcela();
        case 1: 
        	return ((Cultivo)lstElementos.get(row)).getTipoSubparcela();
        case 2: 
        	 return ((Cultivo)lstElementos.get(row)).getTipoSuelo();
        case 3:         	
        	return ((Cultivo)lstElementos.get(row)).getIntensidadProductiva();
        case 4: 
        	return ((Cultivo)lstElementos.get(row)).getIdCultivo().getNumOrden();
        case 5: 
        	return ((Cultivo)lstElementos.get(row)).getIdCultivo().getCalifCultivo();
        default: 
            return null;
        
        }        
    }
    
    /**
     * Devuelve la ConstruccionCatastro completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto ConstruccionCatastro se solicita
     * @return ConstruccionCatastro completa
     */
    public ConstruccionCatastro getValueAt(int row) {
        
        return (ConstruccionCatastro)lstElementos.get(row);
        
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