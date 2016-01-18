/**
 * CentrosSanitariosCompletoEIELTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.models;

import java.util.ArrayList;

import com.geopista.app.eiel.beans.CentrosSanitariosEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class CentrosSanitariosCompletoEIELTableModel  extends EIELCompletoTableModel {
	
	private static String[] columnNames = { 
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.clave"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.codorden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.nombre_sa"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.tipo_sa"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.titular_sa"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.gestor_sa"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.s_cubierta_sa"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.s_aire_sa"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.s_solar_sa"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.uci"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.n_camas_sa"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.estado_sa"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.fecha_inst"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.estado_revision"),
    		I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.bloqueado"),
    		I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.acceso_s_ruedas"),
    		I18N.get("LocalGISEIEL","localgiseiel.tabla.centrossanitarios.columna.obra_ejec")};

    public CentrosSanitariosCompletoEIELTableModel() {        

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
        	return ((CentrosSanitariosEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1:
        	return ((CentrosSanitariosEIEL)lstElementos.get(row)).getClave();
        case 2: 
        	return ((CentrosSanitariosEIEL)lstElementos.get(row)).getCodINEProvincia()+ ((CentrosSanitariosEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 3: 
            return getNombreNucleo( ((CentrosSanitariosEIEL)lstElementos.get(row)).getCodINEEntidad(),((CentrosSanitariosEIEL)lstElementos.get(row)).getCodINEPoblamiento());
        case 4:
        	return ((CentrosSanitariosEIEL)lstElementos.get(row)).getCodINEPoblamiento();
        case 5:
        	return ((CentrosSanitariosEIEL)lstElementos.get(row)).getOrden();
        case 6:
            return ((CentrosSanitariosEIEL)lstElementos.get(row)).getNombre();
        case 7: 
            Estructuras.cargarEstructura("eiel_Tipo Centro Sanitario");
            return LocalGISEIELUtils.getNameFromEstructura(((CentrosSanitariosEIEL)lstElementos.get(row)).getTipo());
        case 8: 
            Estructuras.cargarEstructura("eiel_Titularidad Centro Sanitario");
            return LocalGISEIELUtils.getNameFromEstructura(((CentrosSanitariosEIEL)lstElementos.get(row)).getTitularidad());
        case 9:
            Estructuras.cargarEstructura("eiel_Gestor Centro Sanitario");
            return LocalGISEIELUtils.getNameFromEstructura(((CentrosSanitariosEIEL)lstElementos.get(row)).getGestion());
        case 10:
            return ((CentrosSanitariosEIEL) lstElementos.get(row)).getSupCubierta() != null ? ((CentrosSanitariosEIEL) lstElementos
                    .get(row)).getSupCubierta().toString()
                    : "";
        case 11:
            return ((CentrosSanitariosEIEL)lstElementos.get(row)).getSupLibre() != null ? ((CentrosSanitariosEIEL) lstElementos
                    .get(row)).getSupLibre().toString()
                    : "";
        case 12:
            return ((CentrosSanitariosEIEL)lstElementos.get(row)).getSupSolar() != null ? ((CentrosSanitariosEIEL) lstElementos
                    .get(row)).getSupSolar().toString()
                    : "";
        case 13: 
            Estructuras.cargarEstructura("eiel_uci");
            return LocalGISEIELUtils.getNameFromEstructura(((CentrosSanitariosEIEL)lstElementos.get(row)).getUci());
        case 14: 
            return ((CentrosSanitariosEIEL)lstElementos.get(row)).getNumCamas() != null ? ((CentrosSanitariosEIEL) lstElementos
                    .get(row)).getNumCamas().toString()
                    : "";
        case 15:
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            return LocalGISEIELUtils.getNameFromEstructura(((CentrosSanitariosEIEL)lstElementos.get(row)).getEstado());
        case 16:
            return ((CentrosSanitariosEIEL)lstElementos.get(row)).getFechaInstalacion() != null ? ((CentrosSanitariosEIEL) lstElementos
                    .get(row)).getFechaInstalacion().toString()
                    : "";
        case 17:
            return ((CentrosSanitariosEIEL)lstElementos.get(row)).getObservaciones();
        case 18:
            return ((CentrosSanitariosEIEL)lstElementos.get(row)).getFechaRevision() != null ? ((CentrosSanitariosEIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 19:
            Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((CentrosSanitariosEIEL) lstElementos.get(row)).getEstadoRevision().toString());
                    
        case 20:
            return ((CentrosSanitariosEIEL)lstElementos.get(row)).getBloqueado() != null ? ((CentrosSanitariosEIEL) lstElementos
                    .get(row)).getBloqueado().toString()
                    : "";
                    
        case 21:
            Estructuras.cargarEstructura("eiel_Acceso con silla de ruedas");
            return LocalGISEIELUtils.getNameFromEstructura(((CentrosSanitariosEIEL) lstElementos.get(row)).getAcceso_s_ruedas().toString());
                    
        case 22:
            Estructuras.cargarEstructura("eiel_Obra ejecutada");
            return LocalGISEIELUtils.getNameFromEstructura(((CentrosSanitariosEIEL) lstElementos.get(row)).getObra_ejec().toString());
        default: 
            return null;
        
        }        
    }
    
    /**
     * Devuelve la Depuradora1EIEL completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto FincaCatastro se solicita
     * @return FincaCatastro completa
     */
    public CentrosSanitariosEIEL getValueAt(int row) {
        
        return (CentrosSanitariosEIEL)lstElementos.get(row);
        
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
