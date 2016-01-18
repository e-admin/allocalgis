package com.geopista.app.eiel.models;


import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.NivelesCentrosEnsenianza;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class NivelesCentrosEnsenianzaCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.nivel"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.aulas"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.plazas"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.alumnos"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.fecha_curso"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.codigoorden")};
	    
    public NivelesCentrosEnsenianzaCompletoEIELTableModel() {        
    	
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
            return ((NivelesCentrosEnsenianza)lstElementos.get(row)).getNivel();
        case 1: 
            return ((NivelesCentrosEnsenianza) lstElementos.get(row)).getUnidades() != null ? ((NivelesCentrosEnsenianza) lstElementos
                    .get(row)).getUnidades().toString()
                    : "";
        case 2: 
            return ((NivelesCentrosEnsenianza) lstElementos.get(row)).getNumeroPlazas() != null ? ((NivelesCentrosEnsenianza) lstElementos
                    .get(row)).getNumeroPlazas().toString()
                    : "";
        case 3: 
            return ((NivelesCentrosEnsenianza) lstElementos.get(row)).getNumeroAlumnos() != null ? ((NivelesCentrosEnsenianza) lstElementos
                    .get(row)).getNumeroAlumnos().toString()
                    : "";
        case 4: 
            return ((NivelesCentrosEnsenianza)lstElementos.get(row)).getFechaCurso() != null ? ((NivelesCentrosEnsenianza) lstElementos
                    .get(row)).getFechaCurso().toString()
                    : "";
        case 5: 
            return ((NivelesCentrosEnsenianza)lstElementos.get(row)).getObservacionesNivel();            
        case 6: 
            return ((NivelesCentrosEnsenianza)lstElementos.get(row)).getCodigoOrdenNivel();
        default: 
            return null;
        
        }        
    }
    
  
    public NivelesCentrosEnsenianza getValueAt(int row) {
        
        return (NivelesCentrosEnsenianza)lstElementos.get(row);
        
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
