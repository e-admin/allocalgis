/**
 * NucleosAbandonadosCompletoEIELTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.models;

import java.util.ArrayList;

import com.geopista.app.eiel.beans.NucleosAbandonadosEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class NucleosAbandonadosCompletoEIELTableModel  extends EIELCompletoTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.a_abandono"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.causa_abandono"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.titular_abandono"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.rehabilitacion"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.acceso"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.serv_agua"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.serv_elect"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.estado_revision"),
    I18N.get("LocalGISEIEL","localgiseiel.tabla.na.columna.bloqueado")};

    public NucleosAbandonadosCompletoEIELTableModel() {        
    	
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
            return ((NucleosAbandonadosEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
            return ((NucleosAbandonadosEIEL)lstElementos.get(row)).getCodINEProvincia() + ((NucleosAbandonadosEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 2: 
            return getNombreNucleo( ((NucleosAbandonadosEIEL)lstElementos.get(row)).getCodINEEntidad(),((NucleosAbandonadosEIEL)lstElementos.get(row)).getCodINEPoblamiento());
        case 3: 
            return ((NucleosAbandonadosEIEL)lstElementos.get(row)).getCodINEPoblamiento();
        case 4: 
            return ((NucleosAbandonadosEIEL)lstElementos.get(row)).getAnnoAbandono();
        case 5: 
            Estructuras.cargarEstructura("eiel_Causa de Abandono");
            return LocalGISEIELUtils.getNameFromEstructura(((NucleosAbandonadosEIEL)lstElementos.get(row)).getCausaAbandono());
        case 6: 
            Estructuras.cargarEstructura("eiel_Titularidad Núcleo Abandonado");
            return LocalGISEIELUtils.getNameFromEstructura(((NucleosAbandonadosEIEL)lstElementos.get(row)).getTitularidad());
        case 7: 
            Estructuras.cargarEstructura("eiel_Posibilidad de rehabilitación");
            return LocalGISEIELUtils.getNameFromEstructura(((NucleosAbandonadosEIEL)lstElementos.get(row)).getRehabilitacion());
        case 8: 
            Estructuras.cargarEstructura("eiel_Acceso a Núcleo Abandonado");
            return LocalGISEIELUtils.getNameFromEstructura(((NucleosAbandonadosEIEL)lstElementos.get(row)).getAcceso());
        case 9: 
            Estructuras.cargarEstructura("eiel_Agua en Núcleo Abandonado");
            return LocalGISEIELUtils.getNameFromEstructura(((NucleosAbandonadosEIEL)lstElementos.get(row)).getServicioAgua());
        case 10: 
            Estructuras.cargarEstructura("eiel_Servicio de electricidad");
            return LocalGISEIELUtils.getNameFromEstructura(((NucleosAbandonadosEIEL)lstElementos.get(row)).getServicioElectricidad());
        case 11: 
            return ((NucleosAbandonadosEIEL) lstElementos.get(row)).getFechaRevision() != null ? ((NucleosAbandonadosEIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 12: 
            return ((NucleosAbandonadosEIEL)lstElementos.get(row)).getObservaciones();
        case 13: 
        	Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((NucleosAbandonadosEIEL) lstElementos.get(row)).getEstadoRevision().toString());
       
        case 14: 
            return ((NucleosAbandonadosEIEL)lstElementos.get(row)).getBloqueado() != null ? ((NucleosAbandonadosEIEL) lstElementos
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
    public NucleosAbandonadosEIEL getValueAt(int row) {
        
        return (NucleosAbandonadosEIEL)lstElementos.get(row);
        
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
