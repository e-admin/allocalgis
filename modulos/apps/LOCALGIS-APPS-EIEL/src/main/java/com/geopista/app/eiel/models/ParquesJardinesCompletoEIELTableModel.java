/**
 * ParquesJardinesCompletoEIELTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.models;

import java.util.ArrayList;

import com.geopista.app.eiel.beans.ParquesJardinesEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class ParquesJardinesCompletoEIELTableModel  extends EIELCompletoTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.clave"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.codorden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.nombre_pj"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.tipo_pj"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.titular_pj"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.gestor_pj"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.s_cubi"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.s_aire"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.s_solar"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.agua"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.saneamiento"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.electricidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.comedor"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.juegos_inf"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.otras_pj"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.estado_pj"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.estado_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.observ"),
		    I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.bloqueado"),
		    I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.acceso_s_ruedas"),
		    I18N.get("LocalGISEIEL","localgiseiel.tabla.pj.columna.obra_ejec")};

    public ParquesJardinesCompletoEIELTableModel() {        

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
            return ((ParquesJardinesEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
        	return ((ParquesJardinesEIEL)lstElementos.get(row)).getClave();
        case 2: 
            return ((ParquesJardinesEIEL)lstElementos.get(row)).getCodINEProvincia() + ((ParquesJardinesEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 3: 
            return ((ParquesJardinesEIEL)lstElementos.get(row)).getCodOrden();
        case 4: 
            return getNombreNucleo( ((ParquesJardinesEIEL)lstElementos.get(row)).getCodINEEntidad(),((ParquesJardinesEIEL)lstElementos.get(row)).getCodINEPoblamiento());
        case 5: 
            return ((ParquesJardinesEIEL)lstElementos.get(row)).getCodINEPoblamiento();
        case 6: 
            return ((ParquesJardinesEIEL)lstElementos.get(row)).getNombre();
        case 7: 
            Estructuras.cargarEstructura("eiel_Tipo de parque");
            return LocalGISEIELUtils.getNameFromEstructura(((ParquesJardinesEIEL)lstElementos.get(row)).getTipo());
        case 8: 
            Estructuras.cargarEstructura("eiel_Titularidad_General_equip");
            return LocalGISEIELUtils.getNameFromEstructura(((ParquesJardinesEIEL)lstElementos.get(row)).getTitularidad());
        case 9: 
            Estructuras.cargarEstructura("eiel_Gestor_General_equip");
            return LocalGISEIELUtils.getNameFromEstructura(((ParquesJardinesEIEL)lstElementos.get(row)).getGestion());
        case 10: 
            return ((ParquesJardinesEIEL) lstElementos.get(row)).getSupCubierta() != null ? ((ParquesJardinesEIEL) lstElementos
                    .get(row)).getSupCubierta().toString()
                    : "";
        case 11: 
            return ((ParquesJardinesEIEL)lstElementos.get(row)).getSupLibre() != null ? ((ParquesJardinesEIEL) lstElementos
                    .get(row)).getSupLibre().toString()
                    : "";
        case 12: 
            return ((ParquesJardinesEIEL)lstElementos.get(row)).getSupSolar() != null ? ((ParquesJardinesEIEL) lstElementos
                    .get(row)).getSupSolar().toString()
                    : "";
        case 13: 
            Estructuras.cargarEstructura("eiel_Existencia de agua");
            return LocalGISEIELUtils.getNameFromEstructura(((ParquesJardinesEIEL)lstElementos.get(row)).getAgua());
        case 14: 
            Estructuras.cargarEstructura("eiel_saneamiento");
            return LocalGISEIELUtils.getNameFromEstructura(((ParquesJardinesEIEL)lstElementos.get(row)).getSaneamiento());
        case 15: 
            Estructuras.cargarEstructura("eiel_electricidad");
            return LocalGISEIELUtils.getNameFromEstructura(((ParquesJardinesEIEL)lstElementos.get(row)).getElectricidad());
        case 16: 
            Estructuras.cargarEstructura("eiel_comedor");
            return LocalGISEIELUtils.getNameFromEstructura(((ParquesJardinesEIEL)lstElementos.get(row)).getComedor());
        case 17: 
            Estructuras.cargarEstructura("eiel_juegos_inf");
            return LocalGISEIELUtils.getNameFromEstructura(((ParquesJardinesEIEL)lstElementos.get(row)).getJuegosInf());
        case 18: 
            Estructuras.cargarEstructura("eiel_Otras");
            return LocalGISEIELUtils.getNameFromEstructura(((ParquesJardinesEIEL)lstElementos.get(row)).getOtros());
        case 19: 
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            return LocalGISEIELUtils.getNameFromEstructura(((ParquesJardinesEIEL)lstElementos.get(row)).getEstado());
        case 20: 
            return ((ParquesJardinesEIEL)lstElementos.get(row)).getFechaRevision() != null ? ((ParquesJardinesEIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 21: 
            Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((ParquesJardinesEIEL) lstElementos.get(row)).getEstadoRevision().toString());
        case 22: 
            return ((ParquesJardinesEIEL)lstElementos.get(row)).getObservaciones();
        case 23: 
            return ((ParquesJardinesEIEL)lstElementos.get(row)).getBloqueado() != null ? ((ParquesJardinesEIEL) lstElementos
                    .get(row)).getBloqueado().toString()
                    : "";
        case 24: 
            Estructuras.cargarEstructura("eiel_Acceso con silla de ruedas");
            return LocalGISEIELUtils.getNameFromEstructura(((ParquesJardinesEIEL) lstElementos.get(row)).getAccesoSilla().toString());
                  
        case 25: 
            Estructuras.cargarEstructura("eiel_Obra ejecutada");
            return LocalGISEIELUtils.getNameFromEstructura(((ParquesJardinesEIEL) lstElementos.get(row)).getObra_ejec().toString());
                   
        default: 
            return null;
        
        }        
    }
    
    /**
     * Devuelve la Depuradora1EIEL completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto FincaCatastro se solicita
     * @return FincaCatastro completa
     */
    public ParquesJardinesEIEL getValueAt(int row) {
        
        return (ParquesJardinesEIEL)lstElementos.get(row);
        
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
