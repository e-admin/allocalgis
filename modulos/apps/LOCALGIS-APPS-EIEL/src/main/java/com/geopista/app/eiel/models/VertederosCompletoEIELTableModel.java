/**
 * VertederosCompletoEIELTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.VertederosEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class VertederosCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.clave"),
//			I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.codorden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.tipo_vt"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.titular"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.gestor"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.olores"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.humos"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.cont_anima"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.r_inun"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.filtracion"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.impacto_v"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.frec_averia"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.saturacion"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.inestable"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.otros"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.capac_tot"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.capac_tot_porc"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.capac_transf"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.estado"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.vida_util"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.categoria"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.actividad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.fecha_apertura"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.capac_ampl"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.estado_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.bloqueado"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.obra_ejec")};
    
	public VertederosCompletoEIELTableModel() {        

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
        	return ((VertederosEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
        	return ((VertederosEIEL)lstElementos.get(row)).getClave();
        case 2: 
        	return ((VertederosEIEL)lstElementos.get(row)).getCodINEProvincia() + ((VertederosEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 3:
        	return ((VertederosEIEL)lstElementos.get(row)).getCodOrden();
        case 4:
            Estructuras.cargarEstructura("eiel_Tipo de vertedero");
            return LocalGISEIELUtils.getNameFromEstructura(((VertederosEIEL)lstElementos.get(row)).getTipo());
        case 5: 
            Estructuras.cargarEstructura("eiel_Titular del vertedero");
            return LocalGISEIELUtils.getNameFromEstructura(((VertederosEIEL)lstElementos.get(row)).getTitularidad());
        case 6: 
            Estructuras.cargarEstructura("eiel_Gestor del Vertedero");
            return LocalGISEIELUtils.getNameFromEstructura(((VertederosEIEL)lstElementos.get(row)).getGestion());
        case 7:
            Estructuras.cargarEstructura("eiel_olor");
            return LocalGISEIELUtils.getNameFromEstructura(((VertederosEIEL)lstElementos.get(row)).getOlores());
        case 8:
            Estructuras.cargarEstructura("eiel_humo");
            return LocalGISEIELUtils.getNameFromEstructura(((VertederosEIEL)lstElementos.get(row)).getHumos());
        case 9: 
            Estructuras.cargarEstructura("eiel_cont animal");
            return LocalGISEIELUtils.getNameFromEstructura(((VertederosEIEL)lstElementos.get(row)).getContAnimal());
        case 10: 
            Estructuras.cargarEstructura("eiel_riesgo inundación");
            return LocalGISEIELUtils.getNameFromEstructura(((VertederosEIEL)lstElementos.get(row)).getRsgoInundacion());
        case 11:
            Estructuras.cargarEstructura("eiel_filtraciones");

            return LocalGISEIELUtils.getNameFromEstructura(((VertederosEIEL)lstElementos.get(row)).getFiltraciones());
        case 12:
            Estructuras.cargarEstructura("eiel_impacto visual");
            return LocalGISEIELUtils.getNameFromEstructura(((VertederosEIEL)lstElementos.get(row)).getImptVisual());
        case 13: 
            Estructuras.cargarEstructura("eiel_frecuentes averías");
            return LocalGISEIELUtils.getNameFromEstructura(((VertederosEIEL)lstElementos.get(row)).getFrecAverias());
        case 14: 
            Estructuras.cargarEstructura("eiel_saturación");
            return LocalGISEIELUtils.getNameFromEstructura(((VertederosEIEL)lstElementos.get(row)).getSaturacion());
        case 15:
            Estructuras.cargarEstructura("eiel_inestabilidad");
            return LocalGISEIELUtils.getNameFromEstructura(((VertederosEIEL)lstElementos.get(row)).getInestabilidad());
        case 16:
            Estructuras.cargarEstructura("eiel_otros vertedero");
            return LocalGISEIELUtils.getNameFromEstructura(((VertederosEIEL)lstElementos.get(row)).getOtros());
        case 17: 
            return ((VertederosEIEL)lstElementos.get(row)).getCapTotal() != null ? ((VertederosEIEL) lstElementos
                    .get(row)).getCapTotal().toString()
                    : "";
        case 18: 
            return ((VertederosEIEL)lstElementos.get(row)).getCapOcupada() != null ? ((VertederosEIEL) lstElementos
                    .get(row)).getCapOcupada().toString()
                    : "";
        case 19:
            return ((VertederosEIEL)lstElementos.get(row)).getCapTransform() != null ? ((VertederosEIEL) lstElementos
                    .get(row)).getCapTransform().toString()
                    : "";
        case 20:
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            return LocalGISEIELUtils.getNameFromEstructura(((VertederosEIEL)lstElementos.get(row)).getEstado());
        case 21: 
            return ((VertederosEIEL)lstElementos.get(row)).getVidaUtil() != null ? ((VertederosEIEL) lstElementos
                    .get(row)).getVidaUtil().toString()
                    : "";
        case 22: 
            Estructuras.cargarEstructura("eiel_Categoria del vertedero");
            return LocalGISEIELUtils.getNameFromEstructura(((VertederosEIEL)lstElementos.get(row)).getCategoria());
        case 23:
            Estructuras.cargarEstructura("eiel_Situación de la actividad de la instalación");
            return LocalGISEIELUtils.getNameFromEstructura(((VertederosEIEL)lstElementos.get(row)).getActividad());
        case 24:
            return ((VertederosEIEL)lstElementos.get(row)).getFechaApertura() != null ? 
            		((VertederosEIEL) lstElementos.get(row)).getFechaApertura().toString()
                    : "";
        case 25: 
            return ((VertederosEIEL)lstElementos.get(row)).getObservaciones();
        case 26: 
            Estructuras.cargarEstructura("eiel_Posibilidad de ampliación");
            return LocalGISEIELUtils.getNameFromEstructura(((VertederosEIEL)lstElementos.get(row)).getPosbAmpliacion());
        case 27:
            return ((VertederosEIEL) lstElementos.get(row)).getFechaRevision() != null ? ((VertederosEIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 28:
			Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((VertederosEIEL) lstElementos.get(row)).getEstadoRevision().toString());
        
        case 29:
            return ((VertederosEIEL)lstElementos.get(row)).getBloqueado() != null ? ((VertederosEIEL) lstElementos
                    .get(row)).getBloqueado().toString()
                    : "";
        case 30:
            Estructuras.cargarEstructura("eiel_Obra ejecutada");
            return LocalGISEIELUtils.getNameFromEstructura(((VertederosEIEL) lstElementos.get(row)).getObra_ejecutada().toString());
        default: 
            return null;
        
        }        
    }
    
    /**
     * Devuelve la Depuradora1EIEL completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto FincaCatastro se solicita
     * @return FincaCatastro completa
     */
    public VertederosEIEL getValueAt(int row) {
        
        return (VertederosEIEL)lstElementos.get(row);
        
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
