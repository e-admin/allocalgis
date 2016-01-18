/**
 * CementeriosCompletoEIELTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.models;

import java.util.ArrayList;

import com.geopista.app.eiel.beans.CementeriosEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class CementeriosCompletoEIELTableModel  extends EIELCompletoTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ce.columna.clave"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.ce.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ce.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ce.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ce.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ce.columna.orden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ce.columna.nombre_ce"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ce.columna.titular_ce"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ce.columna.distancia"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ce.columna.acceso_ce"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ce.columna.capilla"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ce.columna.desposito_ce"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ce.columna.ampliacion_ce"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ce.columna.saturacion"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ce.columna.superficie"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ce.columna.crematorio_ce"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ce.columna.fecha_inst"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ce.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ce.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ce.columna.estado_revision"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.ce.columna.bloqueado"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ce.columna.acceso_s_ruedas"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.ce.columna.obra_ejec")};

    public CementeriosCompletoEIELTableModel() {        
    	
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
        	return ((CementeriosEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
        	return ((CementeriosEIEL)lstElementos.get(row)).getClave();
        case 2:
        	return ((CementeriosEIEL)lstElementos.get(row)).getCodINEProvincia()+((CementeriosEIEL)lstElementos.get(row)).getCodINEMunicipio();
//            return ((CementeriosEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 3: 
            return getNombreNucleo( ((CementeriosEIEL)lstElementos.get(row)).getCodINEEntidad(),((CementeriosEIEL)lstElementos.get(row)).getCodINEPoblamiento());
        case 4: 
            return ((CementeriosEIEL)lstElementos.get(row)).getCodINEPoblamiento();
        case 5: 
            return ((CementeriosEIEL)lstElementos.get(row)).getOrden();
        case 6: 
            return ((CementeriosEIEL)lstElementos.get(row)).getNombre();
        case 7: 
            Estructuras.cargarEstructura("eiel_Titularidad de cementerio");

           return LocalGISEIELUtils.getNameFromEstructura(((CementeriosEIEL)lstElementos.get(row)).getTitular());
        case 8: 
            return ((CementeriosEIEL) lstElementos.get(row)).getDistancia() != null ? ((CementeriosEIEL) lstElementos
                    .get(row)).getDistancia().toString()
                    : "";
        case 9: 
            Estructuras.cargarEstructura("eiel_Estado del acceso");
            return LocalGISEIELUtils.getNameFromEstructura(((CementeriosEIEL)lstElementos.get(row)).getAcceso());
        case 10: 
            Estructuras.cargarEstructura("eiel_Capilla");

            return LocalGISEIELUtils.getNameFromEstructura(((CementeriosEIEL)lstElementos.get(row)).getCapilla());
        case 11: 
            Estructuras.cargarEstructura("eiel_Depósito de cadáveres");

            return LocalGISEIELUtils.getNameFromEstructura(((CementeriosEIEL)lstElementos.get(row)).getDepositoCadaveres());
        case 12: 
            Estructuras.cargarEstructura("eiel_Posibilidad de ampliación");

            return LocalGISEIELUtils.getNameFromEstructura(((CementeriosEIEL)lstElementos.get(row)).getAmpliacion());
        case 13: 
            return ((CementeriosEIEL) lstElementos.get(row)).getSaturacion().toString();

        case 14: 
            return ((CementeriosEIEL)lstElementos.get(row)).getSuperficie() != null ? ((CementeriosEIEL) lstElementos
                    .get(row)).getSuperficie().toString()
                    : "";
        case 15: 
            Estructuras.cargarEstructura("eiel_Existencia de crematorio");

            return LocalGISEIELUtils.getNameFromEstructura(((CementeriosEIEL)lstElementos.get(row)).getCrematorio());
        case 16: 
            return ((CementeriosEIEL) lstElementos.get(row)).getFechaInstalacion() != null ? ((CementeriosEIEL) lstElementos
                    .get(row)).getFechaInstalacion().toString()
                    : "";
        case 17: 
            return ((CementeriosEIEL)lstElementos.get(row)).getObservaciones();
        case 18: 
            return ((CementeriosEIEL)lstElementos.get(row)).getFechaRevision() != null ? ((CementeriosEIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 19: 
            Estructuras.cargarEstructura("eiel_Estado de revisión");

            return LocalGISEIELUtils.getNameFromEstructura(((CementeriosEIEL) lstElementos.get(row)).getEstadoRevision().toString());
          
        case 20: 
            return ((CementeriosEIEL)lstElementos.get(row)).getBloqueado() != null ? ((CementeriosEIEL) lstElementos
                    .get(row)).getBloqueado().toString()
                    : "";
        case 21: 
            Estructuras.cargarEstructura("eiel_Acceso con silla de ruedas");

            return LocalGISEIELUtils.getNameFromEstructura(((CementeriosEIEL) lstElementos.get(row)).getAcceso_s_ruedas());
        case 22: 
            Estructuras.cargarEstructura("eiel_Obra ejecutada");

            return LocalGISEIELUtils.getNameFromEstructura(((CementeriosEIEL) lstElementos.get(row)).getObra_ejec());
        default: 
            return null;
        
        }        
    }
    
    /**
     * Devuelve la Depuradora1EIEL completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto FincaCatastro se solicita
     * @return FincaCatastro completa
     */
    public CementeriosEIEL getValueAt(int row) {
        
        return (CementeriosEIEL)lstElementos.get(row);
        
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
