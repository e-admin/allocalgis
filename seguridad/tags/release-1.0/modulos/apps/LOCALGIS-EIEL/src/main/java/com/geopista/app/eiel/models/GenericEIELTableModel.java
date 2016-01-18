package com.geopista.app.eiel.models;

import java.util.ArrayList;


import com.geopista.app.eiel.beans.WorkflowEIEL;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class GenericEIELTableModel  extends EIELTableModel {
	
	private static String[] columnNames = {
        	I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.codmunic"),
            I18N.get("LocalGISEIEL","Info1"),
            I18N.get("LocalGISEIEL","Info2")
            };
	    
    public GenericEIELTableModel() {        

    }

    private ArrayList lstElementos = new ArrayList();

    /**
     * @return número de columnas de la tabla
     */
    public int getColumnCount() {
    	if (columnNames != null)
    		return columnNames.length;
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
    	/*if (col>1){
    		String valor=(String)((WorkflowEIEL)lstElementos.get(0)).getDatosAdicionales().keySet().toArray()[0];
    		return valor;
    	}
    	else*/
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
        
        if ((lstElementos.size()==0) ||(lstElementos.get(row)==null))
            return null;
        
        switch (col)
        {
        case 0: 
            return ((WorkflowEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
            return ((WorkflowEIEL)lstElementos.get(row)).getCodINEProvincia()+((WorkflowEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 2: {
        	if (((WorkflowEIEL)lstElementos.get(row)).getDatosAdicionales().size()>0){
	        	String dato= (String)((WorkflowEIEL)lstElementos.get(row)).getDatosAdicionales().values().toArray()[0];
	            return dato;
        	}
        	else
        		return "";
        }
        case 3: {
        	if (((WorkflowEIEL)lstElementos.get(row)).getDatosAdicionales().size()>1){
	        	String dato= (String)((WorkflowEIEL)lstElementos.get(row)).getDatosAdicionales().values().toArray()[1];
	            return dato;
        	}
        	else
        		return "";
        }
        default: 
            return null;
        
        }        
    }
    
    /**
     * Devuelve la Depuradora1EIEL completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto FincaCatastro se solicita
     * @return FincaCatastro completa
     */
    public WorkflowEIEL getValueAt(int row) {
        
        return (WorkflowEIEL)lstElementos.get(row);
        
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
