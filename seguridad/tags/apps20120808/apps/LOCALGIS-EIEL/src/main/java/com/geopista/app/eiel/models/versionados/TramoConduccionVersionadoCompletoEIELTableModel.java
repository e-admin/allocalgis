package com.geopista.app.eiel.models.versionados;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.EmisariosEIEL;
import com.geopista.app.eiel.beans.TramosConduccionEIEL;
import com.geopista.app.eiel.beans.VersionEiel;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class TramoConduccionVersionadoCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoVersion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoAccion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoUsuario"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoFecha"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.clave"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.codorden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.titular"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.gestor"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.estado"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.material"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.sist_trans"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.fecha_inst"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.estado_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.bloqueado")};

	    
    public TramoConduccionVersionadoCompletoEIELTableModel() {        

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
			case 17:
			case 18:
			case 13:
			case 14:
			case 15:
				return	 fila[col];
			case 10:
	            Estructuras.cargarEstructura("eiel_Estado de conservación");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);

			case 12:
		         Estructuras.cargarEstructura("eiel_sist_trans");
		          return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);

			case 16:
				Estructuras.cargarEstructura("eiel_Estado de revisión");
            	return fila[col]!=null? LocalGISEIELUtils.getNameFromEstructura(Integer.toString((Integer)fila[col])):"";
	            
			case 8:      
				Estructuras.cargarEstructura("eiel_Titularidad");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);

			case 9:
	            Estructuras.cargarEstructura("eiel_Gestión");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 11:
	            Estructuras.cargarEstructura("eiel_material");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
//			case 19:
//			case 20:
//			case 21:
//			case 22:
//			case 23:
//			case 24:
//			case 25:
//			case 26:
//			case 27:
//				return fila[col] != null ? fila[col].toString() : "";
//			case 3:	
//        		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
//        		return fila[col] != null ? formatter.format(fila[col]):"";
			default:
				return null;
		}      
    }
    
    /**
     * Devuelve la Depuradora1EIEL completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto FincaCatastro se solicita
     * @return FincaCatastro completa
     */
    public TramosConduccionEIEL getValueAt(int row) {
    	Object[] fila = (Object[]) lstElementos.get(row);
    	TramosConduccionEIEL tcn = new TramosConduccionEIEL();
    	 

		VersionEiel version = new VersionEiel();
		version.setIdVersion((Integer) fila[0]);
		version.setAccion((String) fila[1]);
		version.setUsuario((String) fila[2]);
		version.setFecha((java.util.Date) fila[3]); 
		
		tcn.setVersion(version);
		
		tcn.setClave((String) fila[4]);
		tcn.setCodINEProvincia((String) fila[5]); 
		tcn.setCodINEMunicipio((String) fila[6]);
		tcn.setTramo_cn((String) fila[7]); 
		tcn.setTitular((String) fila[8]); 
		tcn.setGestor((String) fila[9]);
		tcn.setEstado((String) fila[10]); 
		tcn.setMaterial((String) fila[11]);
		tcn.setSist_trans((String) fila[12]); 
		tcn.setFechaInstalacion((Date) fila[13]);
		tcn.setObservaciones((String) fila[14]);
		tcn.setFecha_revision((Date) fila[15]);
		tcn.setEstado_revision((Integer) fila[16]);
		tcn.setBloqueado((String) fila[17]);
		
		return tcn;
        
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
