package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.CementeriosEIEL;
import com.geopista.app.eiel.beans.NucleoEncuestado7EIEL;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class InfoTerminosMunicipalesEIELTableModel  extends EIELTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
            //I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.tv_ant")};
	    
    public InfoTerminosMunicipalesEIELTableModel() {        
    	
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
            return ((NucleoEncuestado7EIEL)lstElementos.get(row)).getEstadoValidacion();
        //case 0: 
        //    return ((NucleoEncuestado7EIEL)lstElementos.get(row)).getCodINEProvincia();
        case 1: 
            return ((NucleoEncuestado7EIEL)lstElementos.get(row)).getCodINEProvincia()+((NucleoEncuestado7EIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 2: 
            return getNombreNucleo( ((NucleoEncuestado7EIEL)lstElementos.get(row)).getCodINEEntidad(),((NucleoEncuestado7EIEL)lstElementos.get(row)).getCodINEPoblamiento());
        case 3: 
            return ((NucleoEncuestado7EIEL)lstElementos.get(row)).getCodINEPoblamiento();
        case 4: 
            return ((NucleoEncuestado7EIEL)lstElementos.get(row)).getTvAntena();
        default: 
            return null;
        
        }        
    }
    
    /**
     * Devuelve la Depuradora1EIEL completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto FincaCatastro se solicita
     * @return FincaCatastro completa
     */
    public NucleoEncuestado7EIEL getValueAt(int row) {
        
        return (NucleoEncuestado7EIEL)lstElementos.get(row);
        
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
