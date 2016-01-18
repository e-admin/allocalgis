/**
 * CaptacionesCompletoEIELTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class CaptacionesCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.clave"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.codorden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.nombre_ca"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.tipo_ca"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.titular_ca"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.gestor_ca"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.sist_impulsion"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.estado"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.uso_ca"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.proteccion"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.contador"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.estado_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.fecha_inst"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.cuenca"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.n_expediente"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.n_inventari"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.cota"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.profundidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.max_consumo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.captaciones.columna.bloqueado")};

	    
    public CaptacionesCompletoEIELTableModel() {        

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
        	 return ((CaptacionesEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
        	return ((CaptacionesEIEL)lstElementos.get(row)).getClave();
        case 2:
        	return ((CaptacionesEIEL)lstElementos.get(row)).getCodINEProvincia()+((CaptacionesEIEL)lstElementos.get(row)).getCodINEMunicipio();            
        case 3: 
            return ((CaptacionesEIEL)lstElementos.get(row)).getCodOrden();
        case 4: 
            return ((CaptacionesEIEL)lstElementos.get(row)).getNombre();
        case 5: 
        	Estructuras.cargarEstructura("eiel_Tipo de Captación");
            return LocalGISEIELUtils.getNameFromEstructura(((CaptacionesEIEL)lstElementos.get(row)).getTipo());
        case 6: 
			Estructuras.cargarEstructura("eiel_Titularidad");
            return LocalGISEIELUtils.getNameFromEstructura(((CaptacionesEIEL)lstElementos.get(row)).getTitularidad());
        case 7: 
			Estructuras.cargarEstructura("eiel_Gestión");
            return LocalGISEIELUtils.getNameFromEstructura(((CaptacionesEIEL)lstElementos.get(row)).getGestion());
        case 8: 
			Estructuras.cargarEstructura("eiel_sist_impulsion");
            return LocalGISEIELUtils.getNameFromEstructura(((CaptacionesEIEL)lstElementos.get(row)).getSistema());
        case 9: 
			Estructuras.cargarEstructura("eiel_Estado de conservación");
            return LocalGISEIELUtils.getNameFromEstructura(((CaptacionesEIEL)lstElementos.get(row)).getEstado());
        case 10: 
			Estructuras.cargarEstructura("eiel_Tipo de uso CA");
            return LocalGISEIELUtils.getNameFromEstructura(((CaptacionesEIEL)lstElementos.get(row)).getTipoUso());
        case 11: 
			Estructuras.cargarEstructura("eiel_Proteccion CA");
            return LocalGISEIELUtils.getNameFromEstructura(((CaptacionesEIEL)lstElementos.get(row)).getProteccion());
        case 12: 
			Estructuras.cargarEstructura("eiel_Contador Abast");
            return LocalGISEIELUtils.getNameFromEstructura(((CaptacionesEIEL)lstElementos.get(row)).getContador());
        case 13: 
            return ((CaptacionesEIEL)lstElementos.get(row)).getObservaciones();
        case 14: 
            return ((CaptacionesEIEL) lstElementos.get(row)).getFechaRevision() != null ? ((CaptacionesEIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 15: 
			Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((CaptacionesEIEL) lstElementos.get(row)).getEstadoRevision().toString());
        case 16: 
            return ((CaptacionesEIEL)lstElementos.get(row)).getFechaInst() != null ? ((CaptacionesEIEL) lstElementos
                    .get(row)).getFechaInst().toString()
                    : "";
        case 17: 
            return ((CaptacionesEIEL)lstElementos.get(row)).getCuenca() != null ? ((CaptacionesEIEL) lstElementos
                    .get(row)).getCuenca().toString():"";
        case 18: 
            return ((CaptacionesEIEL)lstElementos.get(row)).getN_expediente() != null ? ((CaptacionesEIEL) lstElementos
                    .get(row)).getN_expediente():"";
        case 19: 
            return ((CaptacionesEIEL)lstElementos.get(row)).getN_inventario() != null ? ((CaptacionesEIEL) lstElementos
                    .get(row)).getN_inventario():"";
        
        case 20: 
            return ((CaptacionesEIEL)lstElementos.get(row)).getCota() != null ? ((CaptacionesEIEL) lstElementos
                    .get(row)).getCota()
                    :  0;
        case 21: 
            return ((CaptacionesEIEL)lstElementos.get(row)).getProfundidad() != null ? ((CaptacionesEIEL) lstElementos
                    .get(row)).getProfundidad()
                    :  0;
        case 22: 
            return ((CaptacionesEIEL)lstElementos.get(row)).getMax_consumo() != null ? ((CaptacionesEIEL) lstElementos
                    .get(row)).getMax_consumo()
                    :  0;
        case 23: 
            return ((CaptacionesEIEL)lstElementos.get(row)).getBloqueado() != null ? ((CaptacionesEIEL) lstElementos
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
    public CaptacionesEIEL getValueAt(int row) {
        
        return (CaptacionesEIEL)lstElementos.get(row);
        
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
