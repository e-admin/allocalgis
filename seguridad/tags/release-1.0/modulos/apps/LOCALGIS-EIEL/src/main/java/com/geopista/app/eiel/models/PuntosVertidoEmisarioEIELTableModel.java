package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.PuntoVertidoEmisario;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class PuntosVertidoEmisarioEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
		I18N.get("LocalGISEIEL","localgiseiel.tabla.nucleospobl.columna.clave_pv"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.nucleospobl.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.nucleospobl.columna.codmunic"),           
            I18N.get("LocalGISEIEL","localgiseiel.tabla.nucleospobl.columna.orden_pv")};
	    
    public PuntosVertidoEmisarioEIELTableModel() {        
    	
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
            return ((PuntoVertidoEmisario)lstElementos.get(row)).getCodClavePuntoVertido();
        case 1: 
        	return ((PuntoVertidoEmisario)lstElementos.get(row)).getCodProvPuntoVertido();
        case 2: 
        	return ((PuntoVertidoEmisario)lstElementos.get(row)).getCodMunicPuntoVertido();
        case 3: 
            return ((PuntoVertidoEmisario)lstElementos.get(row)).getCodOrdenPuntoVertido();
        
        default: 
            return null;
        
        }        
    }
    
    public PuntoVertidoEmisario getValueAt(int row) {
        
        return (PuntoVertidoEmisario)lstElementos.get(row);
        
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
