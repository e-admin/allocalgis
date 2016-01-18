/**
 * TablePersonaModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.catastro.intercambio.edicion.utils;


/**
 * Modelo para mostrar los titulares en las tablas de titulares
 * 
 * @author cotesa
 *
 */

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.Persona;
import com.geopista.app.catastro.model.beans.Titular;
import com.vividsolutions.jump.I18N;


public class TablePersonaModel extends AbstractTableModel {
    
    private String[] columnNames = {
    		I18N.get("Expedientes","tabla.persona.columna.cargo"),
    		I18N.get("Expedientes","tabla.persona.columna.derecho"),
    		I18N.get("Expedientes","tabla.persona.columna.cif"),
    		I18N.get("Expedientes","tabla.persona.columna.porcentaje"),
    		I18N.get("Expedientes","tabla.persona.columna.nifconyuge"),
    		I18N.get("Expedientes","tabla.persona.columna.nifcb"),
    		I18N.get("Expedientes","tabla.persona.columna.sufijo")};      
            
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
        	if(((Persona)lstElementos.get(row)).getBienInmueble()!=null)
                return ((Persona)lstElementos.get(row)).getBienInmueble().getIdBienInmueble().getNumCargo();
                return null;
        case 1: 
        	if (lstElementos.get(row) instanceof Titular)
                return ((Titular)lstElementos.get(row)).getDerecho().getCodDerecho();
            return null;
        case 2: 
        	return ((Persona)lstElementos.get(row)).getNif();
        case 3: 
        	if (lstElementos.get(row) instanceof Titular)
                return EdicionUtils.getStringValue(((Titular)lstElementos.get(row)).getDerecho().getPorcentajeDerecho());
            return null;
        case 4: 
        	if (lstElementos.get(row) instanceof Titular)
                return ((Titular)lstElementos.get(row)).getNifConyuge();
            return null;
        case 5: 
        	if (lstElementos.get(row) instanceof Titular)
                return ((Titular)lstElementos.get(row)).getNifCb();
            return null;
        case 6: 
        	if (lstElementos.get(row) instanceof Titular)
                return null;
            return null;
        
        default: 
            return null;
        
        }        
    }
    
    /**
     * Devuelve la FincaCatastro completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto FincaCatastro se solicita
     * @return Via completa
     */
    public BienInmuebleCatastro getValueAt(int row) {
        
        return (BienInmuebleCatastro)lstElementos.get(row);
        
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