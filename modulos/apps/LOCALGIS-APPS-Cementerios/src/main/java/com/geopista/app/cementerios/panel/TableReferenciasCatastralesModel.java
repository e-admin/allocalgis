/**
 * TableReferenciasCatastralesModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.cementerios.panel;


import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.geopista.app.AppContext;
import com.geopista.protocol.licencias.CReferenciaCatastral;


public class TableReferenciasCatastralesModel extends AbstractTableModel {
    
    private String[] columnNames = {
    		AppContext.getApplicationContext().getI18NResource()==null?((AppContext)AppContext.getApplicationContext()).getI18nString("inventario.referenciasDialog.tab6"):AppContext.getApplicationContext().getI18NResource().getString("CReferenciaCatastralJDialog.resultadosJTableModel.text.column1"),
    				AppContext.getApplicationContext().getI18NResource()==null?((AppContext)AppContext.getApplicationContext()).getI18nString("inventario.referenciasDialog.tab7"):AppContext.getApplicationContext().getI18NResource().getString("CReferenciaCatastralJDialog.resultadosJTableModel.text.column2"),
    						AppContext.getApplicationContext().getI18NResource()==null?((AppContext)AppContext.getApplicationContext()).getI18nString("inventario.referenciasDialog.tab8"):AppContext.getApplicationContext().getI18NResource().getString("CReferenciaCatastralJDialog.resultadosJTableModel.text.column3"),
    								AppContext.getApplicationContext().getI18NResource()==null?((AppContext)AppContext.getApplicationContext()).getI18nString("inventario.referenciasDialog.tab9"):AppContext.getApplicationContext().getI18NResource().getString("CReferenciaCatastralJDialog.resultadosJTableModel.text.column4"),
    										AppContext.getApplicationContext().getI18NResource()==null?((AppContext)AppContext.getApplicationContext()).getI18nString("inventario.referenciasDialog.tab10"):AppContext.getApplicationContext().getI18NResource().getString("CReferenciaCatastralJDialog.resultadosJTableModel.text.column5"),
    												AppContext.getApplicationContext().getI18NResource()==null?((AppContext)AppContext.getApplicationContext()).getI18nString("inventario.referenciasDialog.tab11"):AppContext.getApplicationContext().getI18NResource().getString("CReferenciaCatastralJDialog.resultadosJTableModel.text.column6"),                                                                         
    														AppContext.getApplicationContext().getI18NResource()==null?((AppContext)AppContext.getApplicationContext()).getI18nString("inventario.referenciasDialog.tab15"):AppContext.getApplicationContext().getI18NResource().getString("CReferenciaCatastralJDialog.resultadosJTableModel.text.column10")};
    
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