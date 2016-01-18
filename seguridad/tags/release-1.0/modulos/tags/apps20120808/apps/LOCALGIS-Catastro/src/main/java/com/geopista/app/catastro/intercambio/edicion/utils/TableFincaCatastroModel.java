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
 * Modelo para mostrar las vías en las tablas de vías
 * 
 * @author cotesa
 *
 */

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.vividsolutions.jump.I18N;


public class TableFincaCatastroModel extends AbstractTableModel {
    
    private String[] columnNames = {
            I18N.get("Expedientes","tabla.fincas.columna.delegacion"),
            I18N.get("Expedientes","tabla.fincas.columna.municipio"),
            I18N.get("Expedientes","tabla.fincas.columna.pc1"),
            I18N.get("Expedientes","tabla.fincas.columna.pc2"),
            I18N.get("Expedientes","tabla.fincas.columna.via"),
            I18N.get("Expedientes","tabla.fincas.columna.numero"),
            I18N.get("Expedientes","tabla.fincas.columna.numeroD"),
            I18N.get("Expedientes","tabla.fincas.columna.bloque"),
            I18N.get("Expedientes","tabla.fincas.columna.km"),
            I18N.get("Expedientes","tabla.fincas.columna.restodir")};
    
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
            return ((FincaCatastro)lstElementos.get(row)).getCodDelegacionMEH();
        case 1: 
            return ((FincaCatastro)lstElementos.get(row)).getCodMunicipioDGC();
        case 2: 
            return ((FincaCatastro)lstElementos.get(row)).getRefFinca().getRefCatastral1();
        case 3: 
            return ((FincaCatastro)lstElementos.get(row)).getRefFinca().getRefCatastral2();
        case 4: 
            return EdicionUtils.getStringValue(((FincaCatastro)lstElementos.get(row)).getDirParcela().getIdVia());
        case 5: 
        	if (((FincaCatastro)lstElementos.get(row)).getDirParcela().getPrimerNumero() == -1){
        		return EdicionUtils.getStringValue("");
        	}
        	else{
        		return EdicionUtils.getStringValue(((FincaCatastro)lstElementos.get(row)).getDirParcela().getPrimerNumero());
        	}
        case 6: 
        	if (((FincaCatastro)lstElementos.get(row)).getDirParcela().getSegundoNumero() == -1){
        		return EdicionUtils.getStringValue("");
        	}
        	else{
        		return EdicionUtils.getStringValue(((FincaCatastro)lstElementos.get(row)).getDirParcela().getSegundoNumero());
        	}
        case 7: 
            return ((FincaCatastro)lstElementos.get(row)).getDirParcela().getBloque();
        case 8:
        	if (((FincaCatastro)lstElementos.get(row)).getDirParcela().getKilometro() == -1){
        		return "";
        	}
        	else{
        		return EdicionUtils.getStringValue(((FincaCatastro)lstElementos.get(row)).getDirParcela().getKilometro());
        	}
        case 9:
            return ((FincaCatastro)lstElementos.get(row)).getDirParcela().getDireccionNoEstructurada();
        default: 
            return null;
        
        }        
    }
    
    /**
     * Devuelve la FincaCatastro completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto FincaCatastro se solicita
     * @return FincaCatastro completa
     */
    public FincaCatastro getValueAt(int row) {
        
        return (FincaCatastro)lstElementos.get(row);
        
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