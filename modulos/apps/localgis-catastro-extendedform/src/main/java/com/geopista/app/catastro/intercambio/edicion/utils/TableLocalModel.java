/**
 * TableLocalModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
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
import com.vividsolutions.jump.I18N;


public class TableLocalModel extends AbstractTableModel {
    
    private String[] columnNames = {
    		I18N.get("Expedientes","tabla.local.columna.orden"),
            I18N.get("Expedientes","tabla.local.columna.bloque"),
            I18N.get("Expedientes","tabla.local.columna.escalera"),
            I18N.get("Expedientes","tabla.local.columna.planta"),
            I18N.get("Expedientes","tabla.local.columna.puerta"),
            I18N.get("Expedientes","tabla.local.columna.uc"),
            I18N.get("Expedientes","tabla.local.columna.destino"),
            I18N.get("Expedientes","tabla.local.columna.tipologia"),
            I18N.get("Expedientes","tabla.local.columna.cargo")};
    
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
        	return ((ConstruccionCatastro)lstElementos.get(row)).getNumOrdenConstruccion();
        case 1: 
        	return ((ConstruccionCatastro)lstElementos.get(row)).getDomicilioTributario().getBloque();
        case 2: 
        	return ((ConstruccionCatastro)lstElementos.get(row)).getDomicilioTributario().getEscalera();
        case 3: 
        	return ((ConstruccionCatastro)lstElementos.get(row)).getDomicilioTributario().getPlanta();
        case 4: 
        	return ((ConstruccionCatastro)lstElementos.get(row)).getDomicilioTributario().getPuerta();
        case 5: 
        	return ((ConstruccionCatastro)lstElementos.get(row)).getDatosFisicos().getCodUnidadConstructiva();
        case 6: 
            return ((ConstruccionCatastro)lstElementos.get(row)).getDatosFisicos().getCodDestino();
        case 7: 
            return ((ConstruccionCatastro)lstElementos.get(row)).getDatosEconomicos().getTipoConstruccion();
        case 8:
            return ((ConstruccionCatastro)lstElementos.get(row)).getNumOrdenBienInmueble();        
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