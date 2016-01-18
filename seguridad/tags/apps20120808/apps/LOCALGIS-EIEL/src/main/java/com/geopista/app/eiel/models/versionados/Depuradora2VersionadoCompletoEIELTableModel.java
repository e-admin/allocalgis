package com.geopista.app.eiel.models.versionados;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.Depuradora1EIEL;
import com.geopista.app.eiel.beans.Depuradora2EIEL;
import com.geopista.app.eiel.beans.VersionEiel;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class Depuradora2VersionadoCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoVersion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoAccion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoUsuario"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoFecha"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.clave"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.codorden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.titular_ed"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.gestor_ed"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.capacidad_ed"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.problem_1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.problem_2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.problem_3"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.lodo_gest"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.lodo_vert"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.lodo_inci"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.lodo_con_agri"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.lodo_sin_agri"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.lodo_ot"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.fecha_inst"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.estado_revision"),
    I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.bloqueado")};

    public Depuradora2VersionadoCompletoEIELTableModel() {        

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
			case 10:
			case 21:
				return	 fila[col];
			case 8:
				Estructuras.cargarEstructura("eiel_Titularidad");
	            return LocalGISEIELUtils.getNameFromEstructura( (String) fila[col]);
			case 9:
				Estructuras.cargarEstructura("eiel_Gestión");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 11:
				Estructuras.cargarEstructura("eiel_EDAR problemas existentes");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 12:
				Estructuras.cargarEstructura("eiel_EDAR problemas existentes");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 13:
				Estructuras.cargarEstructura("eiel_EDAR problemas existentes");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 14:
				Estructuras.cargarEstructura("eiel_EDAR Gestión de lodos");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 23:
				Estructuras.cargarEstructura("eiel_Estado de revisión");
            	return fila[col]!=null? LocalGISEIELUtils.getNameFromEstructura(Integer.toString((Integer)fila[col])):"";
	            
			case 15:
			case 16:
			case 17:
			case 18:
			case 19:
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
    public Depuradora2EIEL getValueAt(int row) {
    	Object[] fila = (Object[]) lstElementos.get(row);
    	Depuradora2EIEL obj = new Depuradora2EIEL();
    	 

		VersionEiel version = new VersionEiel();
		version.setIdVersion((Integer) fila[0]);
		version.setAccion((String) fila[1]);
		version.setUsuario((String) fila[2]);
		version.setFecha((Date) fila[3]); 
		
		obj.setVersion(version);
		obj.setClave((String) fila[4]);
		obj.setCodINEProvincia((String) fila[5]); 
		obj.setCodINEMunicipio((String) fila[6]);
		obj.setCodOrden((String) fila[7]); 
		obj.setTitular((String) fila[8]);
		obj.setGestor((String) fila[9]); 
		obj.setCapacidad((Integer) fila[10]);
		obj.setProblemas1((String) fila[11]); 
		obj.setProblemas2((String) fila[12]);
		obj.setProblemas3((String) fila[13]); 
		obj.setGestionLodos((String) fila[14]);
		obj.setLodosVertedero((Integer) fila[15]);
		obj.setLodosIncineracion((Integer) fila[16]);
		obj.setLodosAgrConCompostaje((Integer) fila[17]);
		obj.setLodosAgrSinCompostaje((Integer) fila[18]);
		obj.setLodosOtroFinal((Integer) fila[19]);
		obj.setFechaInstalacion((Date) fila[20]);
		obj.setObservaciones((String) fila[21]); 
		obj.setFechaRevision((Date) fila[22]);
		obj.setEstadoRevision((Integer) fila[23]); 
		obj.setBloqueado((String) fila[24]);
    	
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
