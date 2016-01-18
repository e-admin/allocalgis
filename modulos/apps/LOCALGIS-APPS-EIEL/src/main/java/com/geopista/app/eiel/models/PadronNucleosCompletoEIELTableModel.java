/**
 * PadronNucleosCompletoEIELTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.models;

import java.util.ArrayList;

import com.geopista.app.eiel.beans.PadronNucleosEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class PadronNucleosCompletoEIELTableModel  extends EIELCompletoTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronnucleos.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronnucleos.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronnucleos.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronnucleos.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronnucleos.columna.n_hombres_a1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronnucleos.columna.n_mujeres_a1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronnucleos.columna.total_poblacion_a1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronnucleos.columna.n_hombres_a2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronnucleos.columna.n_mujeres_a2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronnucleos.columna.total_poblacion_a2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronnucleos.columna.fecha_a1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronnucleos.columna.fecha_a2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronnucleos.columna.fecha_actuliza"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronnucleos.columna.observ"),
    I18N.get("LocalGISEIEL","localgiseiel.tabla.padronnucleos.columna.estado_revision"),
    I18N.get("LocalGISEIEL","localgiseiel.tabla.padronnucleos.columna.bloqueado")};
    
    public PadronNucleosCompletoEIELTableModel() {        
    	
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
        	return ((PadronNucleosEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
        	return ((PadronNucleosEIEL)lstElementos.get(row)).getCodINEProvincia() + ((PadronNucleosEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 2: 
            return getNombreNucleo( ((PadronNucleosEIEL)lstElementos.get(row)).getCodINEEntidad(),((PadronNucleosEIEL)lstElementos.get(row)).getCodINEPoblamiento());
        case 3: 
        	return ((PadronNucleosEIEL)lstElementos.get(row)).getCodINEPoblamiento();
        case 4: 
            return ((PadronNucleosEIEL) lstElementos.get(row)).getHombres_a1() != null ? ((PadronNucleosEIEL) lstElementos
                    .get(row)).getHombres_a1().toString()
                    : "";
        case 5: 
            return ((PadronNucleosEIEL)lstElementos.get(row)).getMujeres_a1() != null ? ((PadronNucleosEIEL) lstElementos
                    .get(row)).getMujeres_a1().toString()
                    : "";
        case 6: 
            return ((PadronNucleosEIEL)lstElementos.get(row)).getTotPobl_a1() != null ? ((PadronNucleosEIEL) lstElementos
                    .get(row)).getTotPobl_a1().toString()
                    : "";
        case 7: 
            return ((PadronNucleosEIEL)lstElementos.get(row)).getHombres_a2() != null ? ((PadronNucleosEIEL) lstElementos
                    .get(row)).getHombres_a2().toString()
                    : "";
        case 8: 
            return ((PadronNucleosEIEL)lstElementos.get(row)).getMujeres_a2() != null ? ((PadronNucleosEIEL) lstElementos
                    .get(row)).getMujeres_a2().toString()
                    : "";
        case 9: 
            return ((PadronNucleosEIEL)lstElementos.get(row)).getTotPobl_a2() != null ? ((PadronNucleosEIEL) lstElementos
                    .get(row)).getTotPobl_a2().toString()
                    : "";
        case 10: 
            return ((PadronNucleosEIEL)lstElementos.get(row)).getFecha_a1() != null ? ((PadronNucleosEIEL) lstElementos
                    .get(row)).getFecha_a1().toString()
                    : "";
        case 11: 
            return ((PadronNucleosEIEL)lstElementos.get(row)).getFecha_a2() != null ? ((PadronNucleosEIEL) lstElementos
                    .get(row)).getFecha_a2().toString()
                    : "";
                    
        case 12: 
            return ((PadronNucleosEIEL)lstElementos.get(row)).getFechaRevision() != null ? ((PadronNucleosEIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 13: 
            return ((PadronNucleosEIEL)lstElementos.get(row)).getObservaciones();
        case 14: 
			Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((PadronNucleosEIEL) lstElementos.get(row)).getEstadoRevision().toString());
       
        case 15: 
            return ((PadronNucleosEIEL)lstElementos.get(row)).getBloqueado() != null ? ((PadronNucleosEIEL) lstElementos
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
    public PadronNucleosEIEL getValueAt(int row) {
        
        return (PadronNucleosEIEL)lstElementos.get(row);
        
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
