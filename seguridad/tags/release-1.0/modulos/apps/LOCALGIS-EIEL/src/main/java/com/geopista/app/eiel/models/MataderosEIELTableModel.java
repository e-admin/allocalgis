package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.CementeriosEIEL;
import com.geopista.app.eiel.beans.MataderosEIEL;
import com.vividsolutions.jump.I18N;

public class MataderosEIELTableModel  extends EIELTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.clave"),
            //I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.orden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.nombre_mt")};
	    
    public MataderosEIELTableModel() {        
    	
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
            return ((MataderosEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
            return ((MataderosEIEL)lstElementos.get(row)).getClave();
        //case 1: 
        //    return ((MataderosEIEL)lstElementos.get(row)).getCodINEProvincia();
        case 2: 
            return ((MataderosEIEL)lstElementos.get(row)).getCodINEProvincia()+((MataderosEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 3: 
            return getNombreNucleo( ((MataderosEIEL)lstElementos.get(row)).getCodINEEntidad(),((MataderosEIEL)lstElementos.get(row)).getCodINEPoblamiento());
        case 4: 
            return ((MataderosEIEL)lstElementos.get(row)).getCodINEPoblamiento();
        case 5: 
            return ((MataderosEIEL)lstElementos.get(row)).getOrden();
        case 6: 
            return ((MataderosEIEL)lstElementos.get(row)).getNombre();
        default: 
            return null;
        
        }        
    }
    
    public MataderosEIEL getValueAt(int row) {
        
        return (MataderosEIEL)lstElementos.get(row);
        
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
