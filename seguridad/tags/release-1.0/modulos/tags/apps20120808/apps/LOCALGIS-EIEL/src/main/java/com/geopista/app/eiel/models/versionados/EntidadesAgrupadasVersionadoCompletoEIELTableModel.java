package com.geopista.app.eiel.models.versionados;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.ColectorEIEL;
import com.geopista.app.eiel.beans.EntidadesAgrupadasEIEL;
import com.geopista.app.eiel.beans.VersionEiel;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class EntidadesAgrupadasVersionadoCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoVersion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoAccion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoUsuario"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoFecha"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.agrupaciones6000.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.agrupaciones6000.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.agrupaciones6000.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.agrupaciones6000.columna.codentidadAGR"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.agrupaciones6000.columna.codnucleoAGR")};

	    
    public EntidadesAgrupadasVersionadoCompletoEIELTableModel() {        

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
        Object[] fila = (Object[]) lstElementos.get(row);
        
		switch (col) {
			case 0:
			case 1:
			case 2:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
				return	 fila[col];
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
    	Object[] fila = (Object[]) lstElementos.get(row);
    	EntidadesAgrupadasEIEL entAgr = new EntidadesAgrupadasEIEL();
    	 

		VersionEiel version = new VersionEiel();
		version.setIdVersion((Integer) fila[0]);
		version.setAccion((String) fila[1]);
		version.setUsuario((String) fila[2]);
		version.setFecha((java.util.Date) fila[3]); 
		
		entAgr.setVersion(version);
		
		entAgr.setCodINEMunicipio((String) fila[4]);
		entAgr.setCodINEEntidad((String) fila[5]); 
		entAgr.setCodINENucleo((String) fila[6]); 
		entAgr.setCodINEEntidad_agrupada((String) fila[7]);
		entAgr.setCodINENucleo_agrupado((String) fila[8]);
		
		
		return entAgr;
        
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
    public void setData (ArrayList datos){
        
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
