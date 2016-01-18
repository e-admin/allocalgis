package com.geopista.app.eiel.models.versionados;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.Encuestados2EIEL;
import com.geopista.app.eiel.beans.NucleosAbandonadosEIEL;
import com.geopista.app.eiel.beans.VersionEiel;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class NucleosAbandonadosVersionadoCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoVersion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoAccion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoUsuario"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoFecha"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.a_abandono"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.causa_abandono"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.titular_abandono"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.rehabilitacion"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.acceso"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.serv_agua"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.serv_elect"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.estado_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.bloqueado")};

    public NucleosAbandonadosVersionadoCompletoEIELTableModel() {        
    	
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
			case 8:
			case 16:
				return	 fila[col];
			case 9:
	            Estructuras.cargarEstructura("eiel_Causa de Abandono");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 10:
	            Estructuras.cargarEstructura("eiel_Titularidad Núcleo Abandonado");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 11:
	            Estructuras.cargarEstructura("eiel_Posibilidad de rehabilitación");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 12:
	            Estructuras.cargarEstructura("eiel_Acceso a Núcleo Abandonado");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 13:
	            Estructuras.cargarEstructura("eiel_Agua en Núcleo Abandonado");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 14:
	            Estructuras.cargarEstructura("eiel_Servicio de electricidad");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 17:
				Estructuras.cargarEstructura("eiel_Estado de revisión");
            	return fila[col]!=null? LocalGISEIELUtils.getNameFromEstructura(Integer.toString((Integer)fila[col])):"";
	                
			case 15:
			case 18:		
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
    public NucleosAbandonadosEIEL getValueAt(int row) {
        
    	Object[] fila = (Object[]) lstElementos.get(row);
    	NucleosAbandonadosEIEL obj = new NucleosAbandonadosEIEL();
    	 
    	VersionEiel version = new VersionEiel();
		version.setIdVersion((Integer) fila[0]);
		version.setAccion((String) fila[1]);
		version.setUsuario((String) fila[2]);
		version.setFecha((Date) fila[3]); 
		
		obj.setVersion(version);
    	
		obj.setCodINEProvincia((String) fila[4]);
		obj.setCodINEMunicipio((String) fila[5]);
		obj.setCodINEEntidad((String) fila[6]);
		obj.setCodINEPoblamiento((String) fila[7]);
		obj.setAnnoAbandono((String) fila[8]);
		obj.setCausaAbandono((String) fila[9]);
		obj.setTitularidad((String) fila[10]);
		obj.setRehabilitacion((String) fila[11]);
		obj.setAcceso((String) fila[12]);
		obj.setServicioAgua((String) fila[13]);
		obj.setServicioElectricidad((String) fila[14]);
		obj.setFechaRevision((Date) fila[15]);
		obj.setObservaciones((String) fila[16]);
		obj.setEstadoRevision((Integer) fila[17]);
		obj.setBloqueado((String) fila[18]);
    	
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
