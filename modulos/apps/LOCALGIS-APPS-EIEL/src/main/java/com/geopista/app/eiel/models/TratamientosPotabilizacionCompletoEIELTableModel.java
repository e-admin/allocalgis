/**
 * TratamientosPotabilizacionCompletoEIELTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.TratamientosPotabilizacionEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class TratamientosPotabilizacionCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.clave"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.orden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.tipo_tp"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.ubicacion"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.s_desinf"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.categoria_a1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.categoria_a2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.categoria_a3"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.desaladora"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.otros_tp"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.desinf_1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.desinf_2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.desinf_3"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.periodicidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.organismo_control"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.estado_tp"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.fecha_inst"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.estado_revision"),
    		I18N.get("LocalGISEIEL","localgiseiel.tabla.tp.columna.bloqueado")};

    public TratamientosPotabilizacionCompletoEIELTableModel() {        
    	
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
        
        switch (col)
        {
        case 0: 
            return ((TratamientosPotabilizacionEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
        	return((TratamientosPotabilizacionEIEL)lstElementos.get(row)).getClave();
        case 2: 
            return ((TratamientosPotabilizacionEIEL)lstElementos.get(row)).getCodINEProvincia() + ((TratamientosPotabilizacionEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 3: 
            return ((TratamientosPotabilizacionEIEL)lstElementos.get(row)).getOrdenPotabilizadora();
        case 4: 
            Estructuras.cargarEstructura("eiel_Automatización del equipamiento");
            return LocalGISEIELUtils.getNameFromEstructura(((TratamientosPotabilizacionEIEL)lstElementos.get(row)).getTipo());
        case 5: 
            Estructuras.cargarEstructura("eiel_Ubicación del tratamiento de potabilización");
            return LocalGISEIELUtils.getNameFromEstructura(((TratamientosPotabilizacionEIEL)lstElementos.get(row)).getUbicacion());
        case 6: 
            Estructuras.cargarEstructura("eiel_s_desinf");
            return LocalGISEIELUtils.getNameFromEstructura(((TratamientosPotabilizacionEIEL)lstElementos.get(row)).getSoloDesinfeccion());
        case 7: 
            Estructuras.cargarEstructura("eiel_categoria_a1");
            return LocalGISEIELUtils.getNameFromEstructura(((TratamientosPotabilizacionEIEL)lstElementos.get(row)).getCategoriaA1());
        case 8: 
            Estructuras.cargarEstructura("eiel_categoria_a2");
            return LocalGISEIELUtils.getNameFromEstructura(((TratamientosPotabilizacionEIEL)lstElementos.get(row)).getCategoriaA2());
        case 9: 
            Estructuras.cargarEstructura("eiel_categoria_a3");
            return LocalGISEIELUtils.getNameFromEstructura(((TratamientosPotabilizacionEIEL)lstElementos.get(row)).getCategoriaA3());
        case 10: 
            Estructuras.cargarEstructura("eiel_desaladora");
            return LocalGISEIELUtils.getNameFromEstructura(((TratamientosPotabilizacionEIEL)lstElementos.get(row)).getDesaladora());
        case 11: 
            Estructuras.cargarEstructura("eiel_Otras");
            return LocalGISEIELUtils.getNameFromEstructura(((TratamientosPotabilizacionEIEL)lstElementos.get(row)).getOtros());
        case 12: 
            Estructuras.cargarEstructura("eiel_Método de desinfección");
            return LocalGISEIELUtils.getNameFromEstructura(((TratamientosPotabilizacionEIEL)lstElementos.get(row)).getMetodoDesinfeccion1());
        case 13: 
            Estructuras.cargarEstructura("eiel_Método de desinfección");
            return LocalGISEIELUtils.getNameFromEstructura(((TratamientosPotabilizacionEIEL)lstElementos.get(row)).getMetodoDesinfeccion2());
        case 14: 
            Estructuras.cargarEstructura("eiel_Método de desinfección");
            return LocalGISEIELUtils.getNameFromEstructura(((TratamientosPotabilizacionEIEL)lstElementos.get(row)).getMetodoDesinfeccion3());
        case 15: 
            Estructuras.cargarEstructura("eiel_Periodicidad");
            return LocalGISEIELUtils.getNameFromEstructura(((TratamientosPotabilizacionEIEL)lstElementos.get(row)).getPerioricidad());
        case 16: 
            Estructuras.cargarEstructura("eiel_Control de calidad: Organismo");
            return LocalGISEIELUtils.getNameFromEstructura(((TratamientosPotabilizacionEIEL)lstElementos.get(row)).getOrganismoControl());
        case 17: 
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            return LocalGISEIELUtils.getNameFromEstructura(((TratamientosPotabilizacionEIEL)lstElementos.get(row)).getEstado());
        case 18: 
            return ((TratamientosPotabilizacionEIEL) lstElementos.get(row)).getFechaInstalacion() != null ? ((TratamientosPotabilizacionEIEL) lstElementos
                    .get(row)).getFechaInstalacion().toString()
                    : "";
        case 19: 
            return ((TratamientosPotabilizacionEIEL)lstElementos.get(row)).getFechaRevision() != null ? ((TratamientosPotabilizacionEIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 20: 
            Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((TratamientosPotabilizacionEIEL) lstElementos.get(row)).getEstadoRevision().toString());
        case 21: 
            return ((TratamientosPotabilizacionEIEL)lstElementos.get(row)).getBloqueado() != null ? ((TratamientosPotabilizacionEIEL) lstElementos
                    .get(row)).getBloqueado().toString()
                    : "";
        default: 
            return null;
        
        }        
    }
    
    /**
     * Devuelve la Depuradora1EIEL completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto FincaCatastro se solicita
     * @return FincaCatastro completa
     */
    public TratamientosPotabilizacionEIEL getValueAt(int row) {
        
        return (TratamientosPotabilizacionEIEL)lstElementos.get(row);
        
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
