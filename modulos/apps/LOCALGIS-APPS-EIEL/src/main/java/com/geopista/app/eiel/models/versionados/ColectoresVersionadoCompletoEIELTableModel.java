/**
 * ColectoresVersionadoCompletoEIELTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.models.versionados;

import java.sql.Date;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.ColectorEIEL;
import com.geopista.app.eiel.beans.VersionEiel;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class ColectoresVersionadoCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoVersion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoAccion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoUsuario"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoFecha"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.clave"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.codorden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.titular"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.gestor"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.estado"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.material"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.sist_impulsion"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.tipo_red_interior"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.tip_interceptor"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.fecha_inst"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.estado_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.bloqueado")};

	    
    public ColectoresVersionadoCompletoEIELTableModel() {        

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
			case 15:
			case 16:
			case 17:
			case 19:
				return	 fila[col];
			case 8:
	            Estructuras.cargarEstructura("eiel_Titularidad");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 9:
	            Estructuras.cargarEstructura("eiel_Gestión");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 10:
	            Estructuras.cargarEstructura("eiel_Estado de conservación");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
			case 12:
	            Estructuras.cargarEstructura("eiel_Sistema de impulsión");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 11:
	            Estructuras.cargarEstructura("eiel_material");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 13:
	            Estructuras.cargarEstructura("eiel_tipo_red_interior");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 14:
	            Estructuras.cargarEstructura("eiel_Colector de Tipo interceptor");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 18:
				Estructuras.cargarEstructura("eiel_Estado de revisión");
            	return fila[col]!=null? LocalGISEIELUtils.getNameFromEstructura(Integer.toString((Integer)fila[col])):"";
	         
//			
//			
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
    public ColectorEIEL getValueAt(int row) {
    	Object[] fila = (Object[]) lstElementos.get(row);
    	ColectorEIEL col = new ColectorEIEL();
    	 

		VersionEiel version = new VersionEiel();
		version.setIdVersion((Integer) fila[0]);
		version.setAccion((String) fila[1]);
		version.setUsuario((String) fila[2]);
		version.setFecha((java.util.Date) fila[3]); 
		
		col.setVersion(version);
		
		col.setClave((String) fila[4]);
		col.setCodINEProvincia((String) fila[5]); 
		col.setCodINEMunicipio((String) fila[6]);
		col.setCodOrden((String) fila[7]); 
		col.setTitularidad((String) fila[8]); 
		col.setGestion((String) fila[9]);
		col.setEstado((String) fila[10]);
		col.setMaterial((String) fila[11]); 
		col.setSist_impulsion((String) fila[12]); 
		col.setTipo_red((String) fila[13]);
		col.setTip_interceptor((String) fila[14]);
		col.setFecha_inst((Date) fila[15]);
		col.setObservaciones((String) fila[16]);
		col.setFechaRevision((Date) fila[17]);
		col.setEstado_Revision((Integer) fila[18]); 
		col.setBloqueado((String) fila[19]);
		
		return col;
        
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
