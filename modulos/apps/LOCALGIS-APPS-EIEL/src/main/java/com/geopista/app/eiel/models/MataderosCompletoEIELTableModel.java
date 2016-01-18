/**
 * MataderosCompletoEIELTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.models;

import java.util.ArrayList;

import com.geopista.app.eiel.beans.MataderosEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

public class MataderosCompletoEIELTableModel  extends EIELCompletoTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.clave"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.orden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.nombre_mt"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.clase_mt"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.titular_mt"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.gestor_mt"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.s_cubi"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.s_aire"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.s_solar"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.estado_mt"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.capacidad_mt"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.utilizacion_mt"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.tunel_mt"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.bovino"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.ovino"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.porcino"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.otros"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.fecha_inst"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.estado_revision"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.bloqueado"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.acceso_s_ruedas"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.ma.columna.obra_ejec")};

    public MataderosCompletoEIELTableModel() {        
    	
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
            return ((MataderosEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
        	return ((MataderosEIEL)lstElementos.get(row)).getClave();
        case 2: 
            return ((MataderosEIEL)lstElementos.get(row)).getCodINEProvincia() + ((MataderosEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 3: 
            return getNombreNucleo( ((MataderosEIEL)lstElementos.get(row)).getCodINEEntidad(),((MataderosEIEL)lstElementos.get(row)).getCodINEPoblamiento());
        case 4: 
            return ((MataderosEIEL)lstElementos.get(row)).getCodINEPoblamiento();
        case 5: 
            return ((MataderosEIEL)lstElementos.get(row)).getOrden();
        case 6: 
            return ((MataderosEIEL)lstElementos.get(row)).getNombre();
        case 7: 
            Estructuras.cargarEstructura("eiel_Clase de Matadero");
            return LocalGISEIELUtils.getNameFromEstructura(((MataderosEIEL)lstElementos.get(row)).getClase());
        case 8: 
            Estructuras.cargarEstructura("eiel_Titularidad_General_equip");
            return LocalGISEIELUtils.getNameFromEstructura(((MataderosEIEL)lstElementos.get(row)).getTitular());
        case 9: 
            Estructuras.cargarEstructura("eiel_Gestor_General_equip");
            return LocalGISEIELUtils.getNameFromEstructura(((MataderosEIEL)lstElementos.get(row)).getGestion());
        case 10: 
            return ((MataderosEIEL) lstElementos.get(row)).getSuperficieCubierta() != null ? ((MataderosEIEL) lstElementos
                    .get(row)).getSuperficieCubierta().toString()
                    : "";
        case 11: 
            return ((MataderosEIEL)lstElementos.get(row)).getSuperficieAireLibre() != null ? ((MataderosEIEL) lstElementos
                    .get(row)).getSuperficieAireLibre().toString()
                    : "";
        case 12: 
            return ((MataderosEIEL)lstElementos.get(row)).getSuperficieSolar() != null ? ((MataderosEIEL) lstElementos
                    .get(row)).getSuperficieSolar().toString()
                    : "";
        case 13: 
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            return LocalGISEIELUtils.getNameFromEstructura(((MataderosEIEL)lstElementos.get(row)).getEstado());
        case 14: 
            return ((MataderosEIEL)lstElementos.get(row)).getCapacidadMax() != null ? ((MataderosEIEL) lstElementos
                    .get(row)).getCapacidadMax().toString()
                    : "";
        case 15: 
            return ((MataderosEIEL)lstElementos.get(row)).getCapacidadUtilizada() != null ? ((MataderosEIEL) lstElementos
                    .get(row)).getCapacidadUtilizada().toString()
                    : "";
        case 16: 
            Estructuras.cargarEstructura("eiel_tunel");
            return LocalGISEIELUtils.getNameFromEstructura(((MataderosEIEL)lstElementos.get(row)).getTunel());
        case 17: 
            Estructuras.cargarEstructura("eiel_bovino");
            return LocalGISEIELUtils.getNameFromEstructura(((MataderosEIEL)lstElementos.get(row)).getBovino());
        case 18: 
            Estructuras.cargarEstructura("eiel_ovino");
            return LocalGISEIELUtils.getNameFromEstructura(((MataderosEIEL)lstElementos.get(row)).getOvino());
        case 19: 
            Estructuras.cargarEstructura("eiel_porcino");
            return ((MataderosEIEL)lstElementos.get(row)).getPorcino();
        case 20: 
            Estructuras.cargarEstructura("eiel_Otros Mataderos");
            return LocalGISEIELUtils.getNameFromEstructura(((MataderosEIEL)lstElementos.get(row)).getOtros());
        case 21: 
            return ((MataderosEIEL)lstElementos.get(row)).getFechaInstalacion() != null ? ((MataderosEIEL) lstElementos
                    .get(row)).getFechaInstalacion().toString()
                    : "";
        case 22: 
            return ((MataderosEIEL)lstElementos.get(row)).getObservaciones();
        case 23: 
            return ((MataderosEIEL)lstElementos.get(row)).getFechaRevision() != null ? ((MataderosEIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 24: 
            Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((MataderosEIEL) lstElementos.get(row)).getEstadoRevision().toString());
        case 25: 
            return ((MataderosEIEL)lstElementos.get(row)).getBloqueado() != null ? ((MataderosEIEL) lstElementos
                    .get(row)).getBloqueado().toString()
                    : "";
        case 26: 
            Estructuras.cargarEstructura("eiel_Acceso con silla de ruedas");
            return LocalGISEIELUtils.getNameFromEstructura(((MataderosEIEL) lstElementos.get(row)).getAcceso_s_ruedas().toString());
        case 27: 
            Estructuras.cargarEstructura("eiel_Obra ejecutada");
            return LocalGISEIELUtils.getNameFromEstructura(((MataderosEIEL) lstElementos.get(row)).getObra_ejec().toString());
        default: 
            return null;
        
        }        
    }
    
    public MataderosEIEL getValueAt(int row) {
        
        return (MataderosEIEL)lstElementos.get(row);
        
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
