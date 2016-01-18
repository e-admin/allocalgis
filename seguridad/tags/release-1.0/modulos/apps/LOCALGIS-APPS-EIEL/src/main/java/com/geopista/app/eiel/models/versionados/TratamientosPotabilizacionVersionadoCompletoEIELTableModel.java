package com.geopista.app.eiel.models.versionados;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.MataderosEIEL;
import com.geopista.app.eiel.beans.TratamientosPotabilizacionEIEL;
import com.geopista.app.eiel.beans.VersionEiel;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class TratamientosPotabilizacionVersionadoCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoVersion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoAccion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoUsuario"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoFecha"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.clave"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.orden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.tipo_tp"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.ubicacion"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.s_desinf"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.categoria_a1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.categoria_a2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.categoria_a3"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.desaladora"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.otros_tp"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.desinf_1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.desinf_2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.desinf_3"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.periodicidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.organismo_control"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.observaciones"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.estado_tp"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.fecha_inst"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.estado_revision"),
    		I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.bloqueado")};

    public TratamientosPotabilizacionVersionadoCompletoEIELTableModel() {        
    	
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

				return	 fila[col];
			case 22:
		          Estructuras.cargarEstructura("eiel_Estado de conservación");
		            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
		       
			case 8:
	            Estructuras.cargarEstructura("eiel_Automatización del equipamiento");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
			case 9:
	            Estructuras.cargarEstructura("eiel_Ubicación del tratamiento de potabilización");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 10:
		          Estructuras.cargarEstructura("eiel_s_desinf");
		            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
		        
			case 11:
	            Estructuras.cargarEstructura("eiel_categoria_a1");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
			case 12:
		          Estructuras.cargarEstructura("eiel_categoria_a2");
		            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
		        
			case 13:
	            Estructuras.cargarEstructura("eiel_categoria_a3");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
			case 14:
	            Estructuras.cargarEstructura("eiel_desaladora");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 15:
	            Estructuras.cargarEstructura("eiel_Otras");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
			case 16:
	            Estructuras.cargarEstructura("eiel_Método de desinfección");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
			case 17:
		          Estructuras.cargarEstructura("eiel_Método de desinfección");
		            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
		        
			case 18:
	            Estructuras.cargarEstructura("eiel_Método de desinfección");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
			case 19:
	            Estructuras.cargarEstructura("eiel_Periodicidad");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
			case 20:	
	            Estructuras.cargarEstructura("eiel_Control de calidad: Organismo");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 25:
				Estructuras.cargarEstructura("eiel_Estado de revisión");
            	return fila[col]!=null? LocalGISEIELUtils.getNameFromEstructura(Integer.toString((Integer)fila[col])):"";
	            
			case 21:
			case 23:
			case 24:
	
			case 26:
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
    public TratamientosPotabilizacionEIEL getValueAt(int row) {
        
    	Object[] fila = (Object[]) lstElementos.get(row);
    	TratamientosPotabilizacionEIEL obj = new TratamientosPotabilizacionEIEL();
    	 
    	VersionEiel version = new VersionEiel();
		version.setIdVersion((Integer) fila[0]);
		version.setAccion((String) fila[1]);
		version.setUsuario((String) fila[2]);
		version.setFecha((Date) fila[3]); 
		
		obj.setVersion(version);
		
		obj.setClave((String) fila[4]);
		obj.setCodINEProvincia((String) fila[5]);
		obj.setCodINEMunicipio((String) fila[6]); 
		obj.setOrdenPotabilizadora((String) fila[7]);
		obj.setTipo((String) fila[8]); 
		obj.setUbicacion((String) fila[9]);
		obj.setSoloDesinfeccion((String) fila[10]); 
		obj.setCategoriaA2((String) fila[11]);
		obj.setCategoriaA3((String) fila[12]);
		obj.setCategoriaA3((String) fila[13]);
		obj.setDesaladora((String) fila[14]);
		obj.setOtros((String) fila[15]);
		obj.setMetodoDesinfeccion1((String) fila[16]);
		obj.setMetodoDesinfeccion2((String) fila[17]);
		obj.setMetodoDesinfeccion3((String) fila[18]);
		obj.setPerioricidad((String) fila[19]);
		obj.setOrganismoControl((String) fila[20]);
		obj.setObserv((String) fila[21]);
		obj.setEstado((String) fila[22]);
		obj.setFechaInstalacion((Date) fila[23]);
		obj.setFechaRevision((Date) fila[24]);
		obj.setEstadoRevision((Integer) fila[25]);
		obj.setBloqueado((String) fila[26]);
    	
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
