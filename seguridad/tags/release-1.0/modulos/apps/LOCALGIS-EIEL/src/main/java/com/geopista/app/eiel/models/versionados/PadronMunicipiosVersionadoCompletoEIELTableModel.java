package com.geopista.app.eiel.models.versionados;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.PadronMunicipiosEIEL;
import com.geopista.app.eiel.beans.PadronNucleosEIEL;
import com.geopista.app.eiel.beans.VersionEiel;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class PadronMunicipiosVersionadoCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoVersion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoAccion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoUsuario"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoFecha"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.n_hombres_a1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.n_mujeres_a1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.total_poblacion_a1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.n_hombres_a2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.n_mujeres_a2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.total_poblacion_a2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.fecha_a1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.fecha_a2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.fecha_actuliza"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.estado_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.bloqueado")};
   
    public PadronMunicipiosVersionadoCompletoEIELTableModel() {        
    	
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
        
        Object[] fila = (Object[]) lstElementos.get(row);
		switch (col) {
			case 0:
			case 1:
			case 2:
			case 4:
			case 5:
			case 16:
				return	 fila[col];
			case 15:	
				Estructuras.cargarEstructura("eiel_Estado de revisión");
            	return fila[col]!=null? LocalGISEIELUtils.getNameFromEstructura(Integer.toString((Integer)fila[col])):"";
	            
			case 6:
			case 7:				
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 17:
				return fila[col] != null ? fila[col].toString() : "";
			case 3:	
        		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        		return fila[col] != null ? formatter.format(fila[col]):"";				
			default:
				return null;
		}      
    }
    
    /**
     * Devuelve la Depuradora1EIEL completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto FincaCatastro se solicita
     * @return FincaCatastro completa
     */
    public PadronMunicipiosEIEL getValueAt(int row) {
    	Object[] fila = (Object[]) lstElementos.get(row);
    	PadronMunicipiosEIEL obj = new PadronMunicipiosEIEL();
    	 

		VersionEiel version = new VersionEiel();
		version.setIdVersion((Integer) fila[0]);
		version.setAccion((String) fila[1]);
		version.setUsuario((String) fila[2]);
		version.setFecha((Date) fila[3]); 
		
		obj.setVersion(version);
    	
		obj.setCodINEProvincia((String) fila[4]); 
		obj.setCodINEMunicipio((String) fila[5]);
		obj.setHombres_a1((Integer) fila[6]); 
		obj.setMujeres_a1((Integer) fila[7]);
		obj.setTotPobl_a1((Integer) fila[8]); 
		obj.setHombres_a2((Integer) fila[9]);
		obj.setMujeres_a2((Integer) fila[10]); 
		obj.setTotPobl_a2((Integer) fila[11]); 
		obj.setFecha_a1((Integer) fila[12]); 
		obj.setFecha_a2((Integer) fila[13]);
		obj.setFechaActualizacion((Date) fila[14]); 
		obj.setEstadoRevision((Integer) fila[15]); 
		obj.setObservaciones((String) fila[16]); 
		obj.setBloqueado((String) fila[17]);
    	
        return obj;
        
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
