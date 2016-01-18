package com.geopista.app.eiel.models.versionados;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.VersionEiel;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class CaptacionesVersionadoCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoVersion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoAccion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoUsuario"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoFecha"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.clave"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.codorden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.nombre_ca"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.tipo_ca"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.titular_ca"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.gestor_ca"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.sist_impulsion"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.estado"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.uso_ca"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.proteccion"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.contador"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.estado_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.fecha_inst"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.cuenca"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.n_expediente"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.n_inventari"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.cota"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.profundidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.max_consumo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.bloqueado")};

	    
    public CaptacionesVersionadoCompletoEIELTableModel() {        

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
			case 17:
				return	 fila[col];
			case 9:
	        	Estructuras.cargarEstructura("eiel_Tipo de Captación");
	        	return	 LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 10:
				Estructuras.cargarEstructura("eiel_Titularidad");
	        	return	 LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 11:
				Estructuras.cargarEstructura("eiel_Gestión");
	        	return	 LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 12:
				Estructuras.cargarEstructura("eiel_sist_impulsion");
	        	return	 LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 13:
				Estructuras.cargarEstructura("eiel_Estado de conservación");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
			case 14:
				Estructuras.cargarEstructura("eiel_Tipo de uso CA");
	        	return	 LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 15:
				Estructuras.cargarEstructura("eiel_Proteccion CA");
	        	return	 LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 16:
				Estructuras.cargarEstructura("eiel_Contador Abast");
	        	return	 LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			
			case 19:
				Estructuras.cargarEstructura("eiel_Estado de revisión");
            	return fila[col]!=null? LocalGISEIELUtils.getNameFromEstructura(Integer.toString((Integer)fila[col])):"";
	            
			case 18:
			case 20:
			case 21:
			case 22:
			case 23:
			case 24:
			case 25:
			case 26:
			case 27:
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
    public CaptacionesEIEL getValueAt(int row) {
    	Object[] fila = (Object[]) lstElementos.get(row);
    	CaptacionesEIEL cap = new CaptacionesEIEL();
    	 

		VersionEiel version = new VersionEiel();
		version.setIdVersion((Integer) fila[0]);
		version.setAccion((String) fila[1]);
		version.setUsuario((String) fila[2]);
		version.setFecha((java.util.Date) fila[3]); 
		
		cap.setVersion(version);
		
		cap.setClave((String) fila[4]);
		cap.setCodINEProvincia((String) fila[5]); 
		cap.setCodINEMunicipio((String) fila[6]);
		cap.setCodOrden((String) fila[7]); 
		cap.setNombre((String) fila[8]); 
		cap.setTipo((String) fila[9]);
		cap.setTitular((String) fila[10]); 
		cap.setGestor((String) fila[11]);
		cap.setSistema((String) fila[12]); 
		cap.setEstado((String) fila[13]);
		cap.setUso((String) fila[14]);
		cap.setProteccion((String) fila[15]);
		cap.setContador((String) fila[16]);
		cap.setObservaciones((String) fila[17]);
		cap.setFechaRevision((Date) fila[18]);
		cap.setEstadoRevision((Integer) fila[19]);
		cap.setFechaInst((Date) fila[20]); 
		cap.setCuenca((String) fila[21]);
		cap.setN_expediente((String) fila[22]); 
		cap.setN_inventario((String) fila[23]);
		cap.setCota((Integer) fila[24]);
		cap.setProfundidad((Integer) fila[25]);
		cap.setMax_consumo((Double) fila[26]); 
		cap.setBloqueado((String) fila[27]);
		
		return cap;
        
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
