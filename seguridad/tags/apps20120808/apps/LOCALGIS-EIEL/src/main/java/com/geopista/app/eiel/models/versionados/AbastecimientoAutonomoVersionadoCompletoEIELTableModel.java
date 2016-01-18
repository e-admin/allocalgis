/**
 * 
 */
package com.geopista.app.eiel.models.versionados;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.AbastecimientoAutonomoEIEL;
import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.VersionEiel;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;


/**
 * @author seilagamo
 *
 */
public class AbastecimientoAutonomoVersionadoCompletoEIELTableModel extends DefaultTableModel {
    protected static String[] columnNames = {
		I18N.get("LocalGISEIEL","localgiseiel.version.CampoVersion"),
		I18N.get("LocalGISEIEL","localgiseiel.version.CampoAccion"),
		I18N.get("LocalGISEIEL","localgiseiel.version.CampoUsuario"),
		I18N.get("LocalGISEIEL","localgiseiel.version.CampoFecha"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.clave"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.codprov"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.codmunic"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.codentidad"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.codnucleo"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.aau_vivien"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.aau_pob_re"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.aau_pob_es"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.aau_def_vi"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.aau_def_re"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.aau_def_es"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.aau_fecont"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.aau_fencon"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.aau_caudal"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.observ"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.fecha_revision"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.estado_revision"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.bloqueado")};

    public AbastecimientoAutonomoVersionadoCompletoEIELTableModel() {
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
				return	 fila[col];
			case 17:
				Estructuras.cargarEstructura("eiel_Disponibilidad de agua");
	            return  LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        case 9:
			case 10:
			case 11:
			case 12:
			case 13:	
			case 14:
			case 15:
			case 16:
			case 18:
			case 21:
//				case 21:
					return fila[col] != null ? fila[col].toString() : "";
				
			case 20:
	            Estructuras.cargarEstructura("eiel_Estado de revisión");
	            	return fila[col]!=null? LocalGISEIELUtils.getNameFromEstructura(Integer.toString((Integer)fila[col])):"";
	            
	             
			case 3:
			case 19:
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
    public AbastecimientoAutonomoEIEL getValueAt(int row) {
    	Object[] fila = (Object[]) lstElementos.get(row);
    	AbastecimientoAutonomoEIEL obj = new AbastecimientoAutonomoEIEL();  	 

		VersionEiel version = new VersionEiel();
		version.setIdVersion((Integer) fila[0]);
		version.setAccion((String) fila[1]);
		version.setUsuario((String) fila[2]);
		version.setFecha((java.util.Date) fila[3]); 
		
		obj.setVersion(version);

		obj.setClave((String) fila[4]);
		obj.setCodINEProvincia((String) fila[5]);
		obj.setCodINEMunicipio((String) fila[6]);
		obj.setCodINEEntidad((String) fila[7]); 
		obj.setCodINENucleo((String) fila[8]);
		obj.setViviendas((Integer) fila[9]);
		obj.setPoblacionResidente((Integer) fila[10]);
		obj.setPoblacionEstacional((Integer) fila[11]);
		obj.setViviendasDeficitarias((Integer) fila[12]);
		obj.setPoblacionResidenteDef((Integer) fila[13]);
		obj.setPoblacionEstacionalDef((Integer) fila[14]);
		obj.setFuentesControladas((Integer) fila[15]);
		obj.setFuentesNoControladas((Integer) fila[16]);
		obj.setSuficienciaCaudal((String) fila[17]);
		obj.setObservaciones((String) fila[18]); 
		obj.setFechaRevision((Date) fila[19]);
		obj.setEstadoRevision((Integer) fila[20]); 
		obj.setBloqueado((String) fila[21]); 
    	
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
