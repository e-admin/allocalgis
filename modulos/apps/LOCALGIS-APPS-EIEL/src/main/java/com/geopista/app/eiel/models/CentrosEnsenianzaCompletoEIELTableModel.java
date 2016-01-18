/**
 * CentrosEnsenianzaCompletoEIELTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.models;

import java.util.ArrayList;

import com.geopista.app.eiel.beans.CentrosEnsenianzaEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class CentrosEnsenianzaCompletoEIELTableModel  extends EIELCompletoTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.clave"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.codorden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.nombre_en"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.ambito_en"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.titular_en"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.s_cubierta_en"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.s_aire_en"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.s_solar_en"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.estado_en"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.fecha_inst"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.estado_revision"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.bloqueado"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.acceso_s_ruedas"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.obra_ejec")};

    public CentrosEnsenianzaCompletoEIELTableModel() {        
    	
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
            return ((CentrosEnsenianzaEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
        	return ((CentrosEnsenianzaEIEL)lstElementos.get(row)).getClave();
        case 2: 
        	return ((CentrosEnsenianzaEIEL)lstElementos.get(row)).getCodINEProvincia()+((CentrosEnsenianzaEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 3: 
            return getNombreNucleo( ((CentrosEnsenianzaEIEL)lstElementos.get(row)).getCodINEEntidad(),((CentrosEnsenianzaEIEL)lstElementos.get(row)).getCodINEPoblamiento());
        case 4: 
            return ((CentrosEnsenianzaEIEL)lstElementos.get(row)).getCodINEPoblamiento();
        case 5: 
            return ((CentrosEnsenianzaEIEL)lstElementos.get(row)).getCodOrden();
        case 6: 
            return ((CentrosEnsenianzaEIEL)lstElementos.get(row)).getNombre();
        case 7: 
            Estructuras.cargarEstructura("eiel_Ámbitos de Centros de enseñanza");
            return LocalGISEIELUtils.getNameFromEstructura(((CentrosEnsenianzaEIEL)lstElementos.get(row)).getAmbito());
        case 8: 
            Estructuras.cargarEstructura("eiel_Titularidad Enseñanza");

            return LocalGISEIELUtils.getNameFromEstructura(((CentrosEnsenianzaEIEL)lstElementos.get(row)).getTitular());
        case 9: 
            return ((CentrosEnsenianzaEIEL) lstElementos.get(row)).getSupCubierta() != null ? ((CentrosEnsenianzaEIEL) lstElementos
                    .get(row)).getSupCubierta().toString()
                    : "";
        case 10: 
            return ((CentrosEnsenianzaEIEL)lstElementos.get(row)).getSupAire() != null ? ((CentrosEnsenianzaEIEL) lstElementos
                    .get(row)).getSupAire().toString()
                    : "";
        case 11: 
            return ((CentrosEnsenianzaEIEL)lstElementos.get(row)).getSupSolar() != null ? ((CentrosEnsenianzaEIEL) lstElementos
                    .get(row)).getSupSolar().toString()
                    : "";
        case 12: 
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            return LocalGISEIELUtils.getNameFromEstructura(((CentrosEnsenianzaEIEL)lstElementos.get(row)).getEstado());
        case 13: 
            return ((CentrosEnsenianzaEIEL)lstElementos.get(row)).getFechaInstalacion() != null ? ((CentrosEnsenianzaEIEL) lstElementos
                    .get(row)).getFechaInstalacion().toString()
                    : "";
        case 14: 
            return ((CentrosEnsenianzaEIEL)lstElementos.get(row)).getObservaciones();
        case 15: 
            return ((CentrosEnsenianzaEIEL)lstElementos.get(row)).getFechaRevision() != null ? ((CentrosEnsenianzaEIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 16: 
            Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((CentrosEnsenianzaEIEL) lstElementos.get(row)).getEstadoRevision().toString());
        case 17: 
            return ((CentrosEnsenianzaEIEL)lstElementos.get(row)).getBloqueado() != null ? ((CentrosEnsenianzaEIEL) lstElementos
                    .get(row)).getBloqueado().toString()
                    : "";
        case 18: 
            Estructuras.cargarEstructura("eiel_Acceso con silla de ruedas");
            return LocalGISEIELUtils.getNameFromEstructura(((CentrosEnsenianzaEIEL) lstElementos.get(row)).getAcceso_s_ruedas().toString());
        case 19: 
            Estructuras.cargarEstructura("eiel_Obra ejecutada");
            return LocalGISEIELUtils.getNameFromEstructura(((CentrosEnsenianzaEIEL) lstElementos
                    .get(row)).getObra_ejec().toString());
        default: 
            return null;
        
        }        
    }
    
  
    public CentrosEnsenianzaEIEL getValueAt(int row) {
        
        return (CentrosEnsenianzaEIEL)lstElementos.get(row);
        
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
