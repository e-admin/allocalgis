package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.TramosCarreterasEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class TramosCarreterasCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
//			I18N.get("LocalGISEIEL","localgiseiel.tabla.carreteras.columna.codprov"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.carreteras.columna.cod_carrt"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.carreteras.columna.clase_via"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.carreteras.columna.denominacion"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.carreteras.columna.titular_via"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.carreteras.columna.fecha_act"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.carreteras.columna.observ"),
    		I18N.get("LocalGISEIEL","localgiseiel.tabla.carreteras.columna.bloqueado")};
    public TramosCarreterasCompletoEIELTableModel() {        

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
        	return ((TramosCarreterasEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1:
        	return ((TramosCarreterasEIEL)lstElementos.get(row)).getCodINEProvincia() + ((TramosCarreterasEIEL)lstElementos.get(row)).getCodCarretera();
        case 2: 
            return ((TramosCarreterasEIEL)lstElementos.get(row)).getClaseVia();      
        case 3:
            return ((TramosCarreterasEIEL)lstElementos.get(row)).getDenominacion();
        case 4: 
            Estructuras.cargarEstructura("eiel_Titular Via");
            return LocalGISEIELUtils.getNameFromEstructura(((TramosCarreterasEIEL)lstElementos.get(row)).getTitularidad());      
        case 5:
            return ((TramosCarreterasEIEL) lstElementos.get(row)).getFechaActualizacion() != null ? ((TramosCarreterasEIEL) lstElementos
                    .get(row)).getFechaActualizacion().toString()
                    : "";
        case 6:
            return ((TramosCarreterasEIEL)lstElementos.get(row)).getObservaciones();
            
        case 7:
            return ((TramosCarreterasEIEL) lstElementos.get(row)).getBloqueado() != null ? ((TramosCarreterasEIEL) lstElementos
                    .get(row)).getBloqueado().toString()
                    : "";
        default: 
            return null;
        
        }        
    }
    
    /**
     * Devuelve la Depuradora1EIEL completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto FincaCatastro se solicita
     * @return FincaCatastro completa
     */
    public TramosCarreterasEIEL getValueAt(int row) {
        
        return (TramosCarreterasEIEL)lstElementos.get(row);
        
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
