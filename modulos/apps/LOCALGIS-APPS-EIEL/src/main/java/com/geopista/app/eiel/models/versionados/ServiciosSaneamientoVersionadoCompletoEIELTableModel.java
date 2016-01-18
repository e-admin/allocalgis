/**
 * ServiciosSaneamientoVersionadoCompletoEIELTableModel.java
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

import com.geopista.app.eiel.beans.ServiciosSaneamientoEIEL;
import com.geopista.app.eiel.beans.VersionEiel;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class ServiciosSaneamientoVersionadoCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = { 
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoVersion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoAccion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoUsuario"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoFecha"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.pozos_registo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.sumideros"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.aliv_c_acum"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.aliv_s_acum"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.calidad_serv"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.viviendas_s_conex"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.viviendas_c_conex"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.long_rs_deficit"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.viviendas_def_conex"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.pobl_res_def_afect"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.pobl_est_def_afect"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.caudal_total"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.caudal_tratado"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.c_reutilizado_urb"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.c_reutilizado_rust"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.c_reutilizado_ind"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.estado_revision"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.bloqueado")};

    public ServiciosSaneamientoVersionadoCompletoEIELTableModel() {        

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
			case 24:	
				return	 fila[col];	
			case 8:	
	            Estructuras.cargarEstructura("eiel_pozos_registro");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 9:
	            Estructuras.cargarEstructura("eiel_sumideros");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
			case 10:
	            Estructuras.cargarEstructura("eiel_aliv_c_acum");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 11:
	            Estructuras.cargarEstructura("eiel_aliv_s_acum");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
			case 12:
	            Estructuras.cargarEstructura("eiel_Calidad del servicio");//eiel_Servicio de limpieza de calles
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 26:
				Estructuras.cargarEstructura("eiel_Estado de revisión");
            	return fila[col]!=null? LocalGISEIELUtils.getNameFromEstructura(Integer.toString((Integer)fila[col])):"";
	            
			case 13:
			case 14:	
			case 15:	
			case 16:
			case 17:
			case 18:
			case 19:
			case 20:
			case 21:
			case 22:
			case 23:
			case 25:

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
    public ServiciosSaneamientoEIEL getValueAt(int row) {
        
        
    	Object[] fila = (Object[]) lstElementos.get(row);
    	ServiciosSaneamientoEIEL obj = new ServiciosSaneamientoEIEL();
    	 

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
		obj.setPozos((String) fila[8]);
		obj.setSumideros((String) fila[9]);
		obj.setAlivAcumulacion((String) fila[10]);
		obj.setAlivSinAcumulacion((String) fila[11]); 
		obj.setCalidad((String) fila[12]);
		obj.setVivNoConectadas((Integer) fila[13]); 
		obj.setVivConectadas((Integer) fila[14]);		
		obj.setLongDeficitaria((Integer) fila[15]); 
		obj.setVivDeficitarias((Integer) fila[16]);  
		obj.setPoblResDeficitaria((Integer) fila[17]);
		obj.setPoblEstDeficitaria((Integer) fila[18]); 
		obj.setCaudalTotal((Integer) fila[19]);							
		obj.setCaudalTratado((Integer)fila[20]);
		obj.setCaudalUrbano((Integer) fila[21]); 
		obj.setCaudalRustico((Integer) fila[22]);
		obj.setCaudalIndustrial((Integer) fila[23]); 
		obj.setObservaciones((String) fila[24]);		
		obj.setFechaRevision((Date) fila[25]); 
		obj.setEstadoRevision((Integer) fila[26]);
		obj.setBloqueado((String) fila[27]);
    	
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
