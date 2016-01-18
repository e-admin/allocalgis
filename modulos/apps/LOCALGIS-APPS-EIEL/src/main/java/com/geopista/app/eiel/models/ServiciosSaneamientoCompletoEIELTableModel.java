/**
 * ServiciosSaneamientoCompletoEIELTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.models;

import java.util.ArrayList;

import com.geopista.app.eiel.beans.ServiciosSaneamientoEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class ServiciosSaneamientoCompletoEIELTableModel  extends EIELCompletoTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
//			I18N.get("LocalGISEIEL","localgiseiel.tabla.servsaneam.columna.codprov"),
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

    public ServiciosSaneamientoCompletoEIELTableModel() {        

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
        
        switch (col)
        {        
        case 0: 
        	return ((ServiciosSaneamientoEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
        	return ((ServiciosSaneamientoEIEL)lstElementos.get(row)).getCodINEProvincia() + ((ServiciosSaneamientoEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 2: 
            return getNombreNucleo( ((ServiciosSaneamientoEIEL)lstElementos.get(row)).getCodINEEntidad(),((ServiciosSaneamientoEIEL)lstElementos.get(row)).getCodINEPoblamiento());
        case 3:
        	return ((ServiciosSaneamientoEIEL)lstElementos.get(row)).getCodINEPoblamiento();
        case 4: 
            Estructuras.cargarEstructura("eiel_pozos_registro");
            return LocalGISEIELUtils.getNameFromEstructura(((ServiciosSaneamientoEIEL)lstElementos.get(row)).getPozos());
        case 5: 
            Estructuras.cargarEstructura("eiel_sumideros");
            return LocalGISEIELUtils.getNameFromEstructura(((ServiciosSaneamientoEIEL)lstElementos.get(row)).getSumideros());
        case 6:
            Estructuras.cargarEstructura("eiel_aliv_c_acum");
            return LocalGISEIELUtils.getNameFromEstructura(((ServiciosSaneamientoEIEL)lstElementos.get(row)).getAlivAcumulacion());
        case 7:
            Estructuras.cargarEstructura("eiel_aliv_s_acum");
            return LocalGISEIELUtils.getNameFromEstructura(((ServiciosSaneamientoEIEL)lstElementos.get(row)).getAlivSinAcumulacion());
        case 8: 
            Estructuras.cargarEstructura("eiel_Calidad del servicio");//eiel_Servicio de limpieza de calles
            return LocalGISEIELUtils.getNameFromEstructura(((ServiciosSaneamientoEIEL)lstElementos.get(row)).getCalidad());
        case 9: 
            return ((ServiciosSaneamientoEIEL) lstElementos.get(row)).getVivNoConectadas() != null ? ((ServiciosSaneamientoEIEL) lstElementos
                    .get(row)).getVivNoConectadas().toString()
                    : "";
        case 10:
            return ((ServiciosSaneamientoEIEL)lstElementos.get(row)).getVivConectadas() != null ? ((ServiciosSaneamientoEIEL) lstElementos
                    .get(row)).getVivConectadas().toString()
                    : "";
        case 11:
            return ((ServiciosSaneamientoEIEL)lstElementos.get(row)).getLongDeficitaria() != null ? ((ServiciosSaneamientoEIEL) lstElementos
                    .get(row)).getLongDeficitaria().toString()
                    : "";
        case 12: 
            return ((ServiciosSaneamientoEIEL)lstElementos.get(row)).getVivDeficitarias() != null ? ((ServiciosSaneamientoEIEL) lstElementos
                    .get(row)).getVivDeficitarias().toString()
                    : "";
        case 13: 
            return ((ServiciosSaneamientoEIEL)lstElementos.get(row)).getPoblResDeficitaria() != null ? ((ServiciosSaneamientoEIEL) lstElementos
                    .get(row)).getPoblResDeficitaria().toString()
                    : "";
        case 14:
            return ((ServiciosSaneamientoEIEL)lstElementos.get(row)).getPoblEstDeficitaria() != null ? ((ServiciosSaneamientoEIEL) lstElementos
                    .get(row)).getPoblEstDeficitaria().toString()
                    : "";
        case 15:
            return ((ServiciosSaneamientoEIEL)lstElementos.get(row)).getCaudalTotal() != null ? ((ServiciosSaneamientoEIEL) lstElementos
                    .get(row)).getCaudalTotal().toString()
                    : "";
        case 16:
            return ((ServiciosSaneamientoEIEL)lstElementos.get(row)).getcCaudalTratado() != null ? ((ServiciosSaneamientoEIEL) lstElementos
                    .get(row)).getcCaudalTratado().toString()
                    : "";
        case 17: 
            return ((ServiciosSaneamientoEIEL)lstElementos.get(row)).getCaudalUrbano() != null ? ((ServiciosSaneamientoEIEL) lstElementos
                    .get(row)).getCaudalUrbano().toString()
                    : "";
        case 18: 
            return ((ServiciosSaneamientoEIEL)lstElementos.get(row)).getCaudalRustico() != null ? ((ServiciosSaneamientoEIEL) lstElementos
                    .get(row)).getCaudalRustico().toString()
                    : "";
        case 19:
            return ((ServiciosSaneamientoEIEL)lstElementos.get(row)).getCaudalIndustrial() != null ? ((ServiciosSaneamientoEIEL) lstElementos
                    .get(row)).getCaudalIndustrial().toString()
                    : "";
        case 20:
            return ((ServiciosSaneamientoEIEL)lstElementos.get(row)).getObservaciones();
        case 21: 
            return ((ServiciosSaneamientoEIEL)lstElementos.get(row)).getFechaRevision() != null ? ((ServiciosSaneamientoEIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 22: 
			Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((ServiciosSaneamientoEIEL) lstElementos.get(row)).getEstadoRevision().toString());
       
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
        
        return (ServiciosSaneamientoEIEL)lstElementos.get(row);
        
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
