/**
 * 
 */
package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.AbastecimientoAutonomoEIEL;
import com.geopista.app.eiel.beans.CabildoConsejoEIEL;
import com.geopista.app.eiel.beans.EntidadesAgrupadasEIEL;
import com.vividsolutions.jump.I18N;


/**
 * @author seilagamo
 *
 */
public class EntidadesAgrupadasCompletoEIELTableModel extends EIELTableModel  {
    protected static String[] columnNames = {
    	I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
    	I18N.get("LocalGISEIEL","localgiseiel.tabla.agrupaciones6000.columna.codmunic"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.agrupaciones6000.columna.codentidad"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.agrupaciones6000.columna.codnucleo"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.agrupaciones6000.columna.codentidadAGR"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.agrupaciones6000.columna.codnucleoAGR")};
   
    
    public EntidadesAgrupadasCompletoEIELTableModel() {
    }
    
    private ArrayList lstElementos = new ArrayList();

    /**
     * @return número de columnas de la tabla
     */
    public int getColumnCount() {
        if (columnNames!=null){
            return columnNames.length;
        }
        else
            return 0;
    }
    
    /**
     * @return número de filas de la tabla
     */
    public int getRowCount() {
        if (lstElementos != null)
            return lstElementos.size();
        else
            return 0;
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
            return ((EntidadesAgrupadasEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
        	return "33"+((EntidadesAgrupadasEIEL)lstElementos.get(row)).getCodINEMunicipio();//33Asturias
        case 2: 
        	return ((EntidadesAgrupadasEIEL)lstElementos.get(row)).getCodEntidad();
        case 3:
            return ((EntidadesAgrupadasEIEL)lstElementos.get(row)).getCodNucleo();
        case 4: 
            return ((EntidadesAgrupadasEIEL)lstElementos.get(row)).getCodEntidad_agrupada();
        case 5: 
            return ((EntidadesAgrupadasEIEL)lstElementos.get(row)).getCodNucleo_agrupado();
      
        default: 
            return null;
        
        }        
    }
    
    /**
     * Devuelve la Depuradora1EIEL completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto FincaCatastro se solicita
     * @return FincaCatastro completa
     */
    public EntidadesAgrupadasEIEL getValueAt(int row) {
        
        return (EntidadesAgrupadasEIEL)lstElementos.get(row);
        
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
