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


package com.geopista.app.catastro.intercambio.importacion.utils;


/**
 * Modelo para mostrar las parcelas actualizadas afectadas por expedientes activos 
 * 
 * @author cotesa
 *
 */

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import com.vividsolutions.jump.I18N;


public class TableParcelasAfectadasModel extends AbstractTableModel {
    
    private String[] columnNames = {I18N.get("Importacion","importar.validar.numeroexpediente"),
            I18N.get("Importacion","importar.validar.parcela")};
    
    
    private ArrayList lstExpedientesParcelas = new ArrayList();
    
    
    public TableParcelasAfectadasModel() {
        new DefaultTableModel(columnNames, 0);
    }
    
    public TableParcelasAfectadasModel(String[] colNames) {
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
        return lstExpedientesParcelas.size();
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
        
        if (lstExpedientesParcelas.get(row)==null)
            return null;
        
        switch (col)
        {
        case 0: 
            return ((ParcelaAfectada)lstExpedientesParcelas.get(row)).getExpediente().getNumeroExpediente();
        case 1: 
            return ((ParcelaAfectada)lstExpedientesParcelas.get(row)).getRefParcela();
       
        default: 
            return null;
        
        }        
    }
    
    /**
     * Devuelve la ParcelaAfectada completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto ParcelaAfectada se solicita
     * @return ParcelaAfectada completa
     */
    public ParcelaAfectada getValueAt(int row) {
        
        return (ParcelaAfectada)lstExpedientesParcelas.get(row);
        
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
      
    
    /**
     * Establece los datos mostrados en el modelo
     * @param datos Datos a mostrar en el modelo
     */
    public void setData (ArrayList datos)
    {
        this.lstExpedientesParcelas = datos;
    }
        
    /**
     * Recupera los datos del modelo
     * @return Datos del modelo
     */
    public ArrayList getData (){
        return lstExpedientesParcelas;
    }    
}