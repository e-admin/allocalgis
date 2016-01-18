package com.geopista.app.eiel.models.versionados;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.CentrosSanitariosEIEL;
import com.geopista.app.eiel.beans.RecogidaBasurasEIEL;
import com.geopista.app.eiel.beans.VersionEiel;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class CentrosSanitariosVersionadoCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = { 
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoVersion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoAccion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoUsuario"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoFecha"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.clave"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.codorden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.nombre_sa"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.tipo_sa"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.titular_sa"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.gestor_sa"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.s_cubierta_sa"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.s_aire_sa"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.s_solar_sa"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.uci"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.n_camas_sa"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.estado_sa"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.fecha_inst"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.estado_revision"),
    		I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.bloqueado"),
    		I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.acceso_s_ruedas"),
    		I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.obra_ejec")};

    public CentrosSanitariosVersionadoCompletoEIELTableModel() {        

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
			case 9:
			case 10:
			case 21:
				return	 fila[col];	
			case 11:
	            Estructuras.cargarEstructura("eiel_Tipo Centro Sanitario");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 12:
	            Estructuras.cargarEstructura("eiel_Titularidad Centro Sanitario");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 13:
	            Estructuras.cargarEstructura("eiel_Gestor Centro Sanitario");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);    
			case 17:
	            Estructuras.cargarEstructura("eiel_uci");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 19:
	            Estructuras.cargarEstructura("eiel_Estado de conservación");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 23:
				Estructuras.cargarEstructura("eiel_Estado de revisión");
            	return fila[col]!=null? LocalGISEIELUtils.getNameFromEstructura(Integer.toString((Integer)fila[col])):"";
			case 25:
	            Estructuras.cargarEstructura("eiel_Acceso con silla de ruedas");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 26:
	            Estructuras.cargarEstructura("eiel_Obra ejecutada");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	          
			case 14:	
			case 15:	
			case 16:
			case 18:
			case 20:
			case 22:
			case 24: 
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
    public CentrosSanitariosEIEL getValueAt(int row) {
        
    	Object[] fila = (Object[]) lstElementos.get(row);
    	CentrosSanitariosEIEL obj = new CentrosSanitariosEIEL();
    	 

		VersionEiel version = new VersionEiel();
		version.setIdVersion((Integer) fila[0]);
		version.setAccion((String) fila[1]);
		version.setUsuario((String) fila[2]);
		version.setFecha((Date) fila[3]); 
		
		obj.setVersion(version);
		
    	obj.setClave((String) fila[4]);
		obj.setCodINEProvincia((String) fila[5]);
		obj.setCodINEMunicipio((String) fila[6]); 
		obj.setCodINEEntidad((String) fila[7]);
		obj.setCodINEPoblamiento((String) fila[8]); 
		obj.setOrden((String) fila[9]);
		obj.setNombre((String) fila[10]);
		obj.setTipo((String) fila[11]);
		obj.setTitularidad((String) fila[12]);
		obj.setGestion((String) fila[13]);
		obj.setSupCubierta((Integer) fila[14]); 
		obj.setSupLibre((Integer) fila[15]);
		obj.setSupSolar((Integer) fila[16]); 
		obj.setUci((String) fila[17]);		
		obj.setNumCamas((Integer) fila[18]); 
		obj.setEstado((String) fila[19]);  
		obj.setFechaInstalacion((Date) fila[20]);
		obj.setObservaciones((String) fila[21]); 
		obj.setFechaRevision((Date) fila[22]);
		obj.setEstadoRevision((Integer) fila[23]); 
		obj.setBloqueado((String) fila[24]);		
		obj.setAcceso_s_ruedas((String) fila[25]); 
		obj.setObra_ejec((String) fila[26]);
    	
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
