/**
 * DepositosCompletoEIELTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.DepositosEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class DepositosCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.clave"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.orden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.ubicacion"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.titular_de"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.gestor_de"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.capacidad_de"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.estado_de"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.proteccion"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.fecha_limpieza"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.contador"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.fecha_inst"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.estado_revision"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.de.columna.bloqueado")};

	    
    public DepositosCompletoEIELTableModel() {        
    	
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
            return ((DepositosEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1:
        	return ((DepositosEIEL)lstElementos.get(row)).getClave();
        case 2: 
            return ((DepositosEIEL)lstElementos.get(row)).getCodINEProvincia() + ((DepositosEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 3: 
            return ((DepositosEIEL)lstElementos.get(row)).getOrdenDeposito();
        case 4: 
    		Estructuras.cargarEstructura("eiel_Ubicacion Deposito");
            return  LocalGISEIELUtils.getNameFromEstructura(((DepositosEIEL)lstElementos.get(row)).getUbicacion());
        case 5: 
            Estructuras.cargarEstructura("eiel_Titularidad");
            return  LocalGISEIELUtils.getNameFromEstructura(((DepositosEIEL)lstElementos.get(row)).getTitularidad());
        case 6: 
            Estructuras.cargarEstructura("eiel_Gestión");
            return  LocalGISEIELUtils.getNameFromEstructura(((DepositosEIEL)lstElementos.get(row)).getGestor());
        case 7: 
            return ((DepositosEIEL) lstElementos.get(row)).getCapacidad() != null ? ((DepositosEIEL) lstElementos
                    .get(row)).getCapacidad().toString()
                    : "";
        case 8: 
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            return  LocalGISEIELUtils.getNameFromEstructura(((DepositosEIEL)lstElementos.get(row)).getEstado());
        case 9: 
            Estructuras.cargarEstructura("eiel_Proteccion DE");
            return  LocalGISEIELUtils.getNameFromEstructura(((DepositosEIEL)lstElementos.get(row)).getProteccion());
        case 10: 
            return ((DepositosEIEL)lstElementos.get(row)).getFechaLimpieza();
        case 11: 
            Estructuras.cargarEstructura("eiel_Contador DE");
            return  LocalGISEIELUtils.getNameFromEstructura(((DepositosEIEL)lstElementos.get(row)).getContador());
        case 12: 
            return ((DepositosEIEL)lstElementos.get(row)).getFechaInstalacion() != null ? ((DepositosEIEL) lstElementos
                    .get(row)).getFechaInstalacion().toString()
                    : "";
        case 13: 
            return ((DepositosEIEL)lstElementos.get(row)).getObservaciones();
        case 14: 
            return ((DepositosEIEL)lstElementos.get(row)).getFechaRevision() != null ? ((DepositosEIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 15: 
			Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((DepositosEIEL) lstElementos.get(row)).getEstadoRevision().toString());
       
        case 16: 
            return ((DepositosEIEL)lstElementos.get(row)).getBloqueado() != null ? ((DepositosEIEL) lstElementos
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
    public DepositosEIEL getValueAt(int row) {
        
        return (DepositosEIEL)lstElementos.get(row);
        
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
