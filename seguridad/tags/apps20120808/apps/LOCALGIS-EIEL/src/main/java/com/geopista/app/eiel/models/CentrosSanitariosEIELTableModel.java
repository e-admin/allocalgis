package com.geopista.app.eiel.models;

import java.util.ArrayList;


import com.geopista.app.eiel.beans.CentrosSanitariosEIEL;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class CentrosSanitariosEIELTableModel  extends EIELTableModel {
	
	private static String[] columnNames = { 
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.clave"),
            //I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.codorden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.nombre_sa")};
	    
    public CentrosSanitariosEIELTableModel() {        

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
         return ((CentrosSanitariosEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1:
        	return ((CentrosSanitariosEIEL)lstElementos.get(row)).getClave();
        //case 1: 
        //	return ((CentrosSanitariosEIEL)lstElementos.get(row)).getCodINEProvincia();
        case 2: 
        	return ((CentrosSanitariosEIEL)lstElementos.get(row)).getCodINEProvincia()+((CentrosSanitariosEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 3: 
            return getNombreNucleo( ((CentrosSanitariosEIEL)lstElementos.get(row)).getCodINEEntidad(),((CentrosSanitariosEIEL)lstElementos.get(row)).getCodINEPoblamiento());
        case 4:
        	return ((CentrosSanitariosEIEL)lstElementos.get(row)).getCodINEPoblamiento();
        case 5:
        	return ((CentrosSanitariosEIEL)lstElementos.get(row)).getOrden();
        case 6:
            return ((CentrosSanitariosEIEL)lstElementos.get(row)).getNombre();
        default: 
            return null;
        
        }        
    }
    
    /**
     * Devuelve la Depuradora1EIEL completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto FincaCatastro se solicita
     * @return FincaCatastro completa
     */
    public CentrosSanitariosEIEL getValueAt(int row) {
        
        return (CentrosSanitariosEIEL)lstElementos.get(row);
        
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
