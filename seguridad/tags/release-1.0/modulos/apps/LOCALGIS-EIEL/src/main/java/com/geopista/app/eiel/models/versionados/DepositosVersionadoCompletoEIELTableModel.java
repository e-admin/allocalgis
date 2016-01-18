package com.geopista.app.eiel.models.versionados;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.DepositosEIEL;
import com.geopista.app.eiel.beans.TramosCarreterasEIEL;
import com.geopista.app.eiel.beans.VersionEiel;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class DepositosVersionadoCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoVersion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoAccion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoUsuario"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoFecha"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.clave"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.orden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.ubicacion"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.titular_de"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.gestor_de"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.capacidad_de"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.estado_de"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.proteccion"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.fecha_limpieza"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.contador"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.fecha_inst"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.estado_revision"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.bloqueado")};

	    
    public DepositosVersionadoCompletoEIELTableModel() {        
    	
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
			case 6:
			case 7:

			case 14:
			case 17:
				return	 fila[col];
				
			case 8:
	    		Estructuras.cargarEstructura("eiel_Ubicacion Deposito");
	            return  LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 9:
	            Estructuras.cargarEstructura("eiel_Titularidad");
	            return  LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 10:
	            Estructuras.cargarEstructura("eiel_Gestión");
	            return  LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 12:
	            Estructuras.cargarEstructura("eiel_Estado de conservación");
	            return  LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 13:
	            Estructuras.cargarEstructura("eiel_Proteccion DE");
	            return  LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 15:
	            Estructuras.cargarEstructura("eiel_Contador DE");
	            return  LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 19:
				Estructuras.cargarEstructura("eiel_Estado de revisión");
            	return fila[col]!=null? LocalGISEIELUtils.getNameFromEstructura(Integer.toString((Integer)fila[col])):"";
	                
			case 11:
			case 16:
			case 18:

			case 20:
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
    public DepositosEIEL getValueAt(int row) {
        
    	Object[] fila = (Object[]) lstElementos.get(row);
    	DepositosEIEL obj = new DepositosEIEL();
    	 

    	VersionEiel version = new VersionEiel();
		version.setIdVersion((Integer) fila[0]);
		version.setAccion((String) fila[1]);
		version.setUsuario((String) fila[2]);
		version.setFecha((Date) fila[3]); 
		
		obj.setVersion(version);
		obj.setClave((String) fila[4]);
		obj.setCodINEProvincia((String) fila[5]);
		obj.setCodINEMunicipio((String) fila[6]); 
		obj.setOrdenDeposito((String) fila[7]);
		obj.setUbicacion((String) fila[8]);
		obj.setTitularidad((String) fila[9]);
		obj.setGestor((String) fila[10]); 
		obj.setCapacidad((Integer) fila[11]);
		obj.setEstado((String) fila[12]);
		obj.setProteccion((String) fila[13]);
		obj.setFechaLimpieza((String) fila[14]); 
		obj.setContador((String) fila[15]);
		obj.setFechaInstalacion((Date) fila[16]);
		obj.setObservaciones((String) fila[17]);
		obj.setFechaRevision((Date) fila[18]); 
		obj.setEstadoRevision((Integer) fila[19]);							
		obj.setBloqueado((String) fila[20]);
		
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
