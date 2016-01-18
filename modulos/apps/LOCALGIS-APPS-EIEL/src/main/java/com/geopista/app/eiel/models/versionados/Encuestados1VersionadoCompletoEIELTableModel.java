/**
 * Encuestados1VersionadoCompletoEIELTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.models.versionados;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.Encuestados1EIEL;
import com.geopista.app.eiel.beans.VersionEiel;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class Encuestados1VersionadoCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoVersion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoAccion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoUsuario"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoFecha"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.padron"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.pob_estacional"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.altitud"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.viviendas_total"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.hoteles"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.casas_rural"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.accesibilidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.estado_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.bloqueado")};

    public Encuestados1VersionadoCompletoEIELTableModel() {        
    	
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
			case 16:
				return	 fila[col];
			case 14:
		         Estructuras.cargarEstructura("eiel_Accesibilidad del núcleo de población");
		         return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 17:
				Estructuras.cargarEstructura("eiel_Estado de revisión");
				return fila[col]!=null? LocalGISEIELUtils.getNameFromEstructura(Integer.toString((Integer)fila[col])):"";
			             
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
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
    public Encuestados1EIEL getValueAt(int row) {
    	Object[] fila = (Object[]) lstElementos.get(row);
    	Encuestados1EIEL obj = new Encuestados1EIEL();
    	 
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
		obj.setPadron((Integer) fila[8]);
		obj.setPoblacionEstacional((Integer) fila[9]);
		obj.setAltitud((Integer) fila[10]);
		obj.setViviendasTotales((Integer) fila[11]);
		obj.setPlazasHoteleras((Integer) fila[12]);
		obj.setPlazasCasasRurales((Integer) fila[13]);
		obj.setAccesibilidad((String) fila[14]);
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
