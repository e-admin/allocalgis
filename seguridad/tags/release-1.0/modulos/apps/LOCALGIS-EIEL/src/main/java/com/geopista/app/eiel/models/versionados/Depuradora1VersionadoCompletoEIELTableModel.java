package com.geopista.app.eiel.models.versionados;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.Depuradora1EIEL;
import com.geopista.app.eiel.beans.VersionEiel;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class Depuradora1VersionadoCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoVersion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoAccion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoUsuario"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoFecha"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.clave"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.codorden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.trat_pr_1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.trat_pr_2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.trat_pr_3"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.trat_sc_1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.trat_sc_2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.trat_sc_3"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.trat_av_1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.trat_av_2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.trat_av_3"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.proc_cm_1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.proc_cm_2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.proc_cm_3"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.trat_ld_1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.trat_ld_2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.trat_ld_3"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.estado_revision"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.bloqueado")};

	    
    public Depuradora1VersionadoCompletoEIELTableModel() {        

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
				return	 fila[col];
			case 24:
				Estructuras.cargarEstructura("eiel_Estado de revisión");
            	return fila[col]!=null? LocalGISEIELUtils.getNameFromEstructura(Integer.toString((Integer)fila[col])):"";
			case 8:
	            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamiento primario");
	            return LocalGISEIELUtils.getNameFromEstructura( (String) fila[col]);
	       
			case 9:
	            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamiento primario");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 10:
	            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamiento primario");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
			case 11:
	            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamiento secundario");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
			case 12:
	            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamiento secundario");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 13:
	            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamiento secundario");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 14:
	            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamientos avanzados");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
			case 15:
	            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamientos avanzados");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
			case 16:
	            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamientos avanzados");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
			case 17:
	            Estructuras.cargarEstructura("eiel_Sist. de depuración, Procesos complementarios");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 18:
	            Estructuras.cargarEstructura("eiel_Sist. de depuración, Procesos complementarios");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
			case 19:
	            Estructuras.cargarEstructura("eiel_Sist. de depuración, Procesos complementarios");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 20:
	            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamientos de fangos o lodos");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 21:
	            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamientos de fangos o lodos");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 22:
	            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamientos de fangos o lodos");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
			case 23:

			case 25:
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
    public Depuradora1EIEL getValueAt(int row) {
        
    	Object[] fila = (Object[]) lstElementos.get(row);
    	Depuradora1EIEL obj = new Depuradora1EIEL();
    	 

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
		obj.setTratPrimario1((String) fila[8]);
		obj.setTratPrimario2((String) fila[9]);
		obj.setTratPrimario3((String) fila[10]);
		obj.setTratSecundario1((String) fila[11]);
		obj.setTratSecundario2((String) fila[12]);
		obj.setTratSecundario3((String) fila[13]); 
		obj.setTratAvanzado1((String) fila[14]);
		obj.setTratAvanzado2((String) fila[15]); 
		obj.setTratAvanzado3((String) fila[16]);
		obj.setProcComplementario1((String) fila[17]);
		obj.setProcComplementario2((String) fila[18]);
		obj.setProcComplementario3((String) fila[19]);
		obj.setTratLodos1((String) fila[20]); 
		obj.setTratLodos2((String) fila[21]); 
		obj.setTratLodos3((String) fila[22]);
		obj.setFechaRevision((Date) fila[23]); 
		obj.setEstadoRevision((Integer) fila[24]);
		obj.setBloqueado((String) fila[25]);
    	
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
