/**
 * OtrosServMunicipalesCompletoEIELTableModel.java
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
import com.geopista.app.eiel.beans.OtrosServMunicipalesEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class OtrosServMunicipalesCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.otrosserviciosmunicipales.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.otrosserviciosmunicipales.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.otrosserviciosmunicipales.columna.sw_inf_grl"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.otrosserviciosmunicipales.columna.sw_inf_tur"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.otrosserviciosmunicipales.columna.sw_gb_elec"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.otrosserviciosmunicipales.columna.ord_soterr"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.otrosserviciosmunicipales.columna.en_eolica"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.otrosserviciosmunicipales.columna.kw_eolica"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.otrosserviciosmunicipales.columna.en_solar"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.otrosserviciosmunicipales.columna.kw_solar"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.otrosserviciosmunicipales.columna.pl_mareo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.otrosserviciosmunicipales.columna.kw_mareo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.otrosserviciosmunicipales.columna.ot_energ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.otrosserviciosmunicipales.columna.kw_ot_energ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.otrosserviciosmunicipales.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.otrosserviciosmunicipales.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.otrosserviciosmunicipales.columna.estado_revision"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.otrosserviciosmunicipales.columna.cob_serv_tlf_m"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.otrosserviciosmunicipales.columna.tv_dig_cable"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.otrosserviciosmunicipales.columna.bloqueado")};
    
    public OtrosServMunicipalesCompletoEIELTableModel() {        

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
        	return ((OtrosServMunicipalesEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
        	return ((OtrosServMunicipalesEIEL)lstElementos.get(row)).getCodINEProvincia() + ((OtrosServMunicipalesEIEL)lstElementos.get(row)).getCodINEMunicipio();   
        case 2: 
            Estructuras.cargarEstructura("eiel_sw_inf_grl");
            return LocalGISEIELUtils.getNameFromEstructura(((OtrosServMunicipalesEIEL)lstElementos.get(row)).getSwInfGeneral());
        case 3: 
            Estructuras.cargarEstructura("eiel_sw_inf_tur");
            return LocalGISEIELUtils.getNameFromEstructura(((OtrosServMunicipalesEIEL)lstElementos.get(row)).getSwInfTuristica());   
        case 4: 
            Estructuras.cargarEstructura("eiel_sw_gb_elec");
            return LocalGISEIELUtils.getNameFromEstructura(((OtrosServMunicipalesEIEL)lstElementos.get(row)).getSwGbElectronico());
        case 5: 
            Estructuras.cargarEstructura("eiel_Ordenanza Soterramiento");
            return LocalGISEIELUtils.getNameFromEstructura(((OtrosServMunicipalesEIEL)lstElementos.get(row)).getOrdSoterramiento());   
        case 6: 
            Estructuras.cargarEstructura("eiel_En Eolica");
            return LocalGISEIELUtils.getNameFromEstructura(((OtrosServMunicipalesEIEL)lstElementos.get(row)).getEnEolica());
        case 7: 
            return ((OtrosServMunicipalesEIEL) lstElementos.get(row)).getKwEolica() != null ? ((OtrosServMunicipalesEIEL) lstElementos
                    .get(row)).getKwEolica().toString()
                    : "";   
        case 8: 
            Estructuras.cargarEstructura("eiel_En Solar");
            return LocalGISEIELUtils.getNameFromEstructura(((OtrosServMunicipalesEIEL)lstElementos.get(row)).getEnSolar());
        case 9: 
            return ((OtrosServMunicipalesEIEL)lstElementos.get(row)).getKwSolar() != null ? ((OtrosServMunicipalesEIEL) lstElementos
                    .get(row)).getKwSolar().toString()
                    : "";      
        case 10: 
            Estructuras.cargarEstructura("eiel_En Mareo");
            return LocalGISEIELUtils.getNameFromEstructura(((OtrosServMunicipalesEIEL)lstElementos.get(row)).getPlMareomotriz());
        case 11: 
            return ((OtrosServMunicipalesEIEL)lstElementos.get(row)).getKwMareomotriz() != null ? ((OtrosServMunicipalesEIEL) lstElementos
                    .get(row)).getKwMareomotriz().toString()
                    : "";      
        case 12: 
            Estructuras.cargarEstructura("eiel_En Otras");
            return LocalGISEIELUtils.getNameFromEstructura(((OtrosServMunicipalesEIEL)lstElementos.get(row)).getOtEnergias());
        case 13: 
            return ((OtrosServMunicipalesEIEL)lstElementos.get(row)).getKwOtEnergias() != null ? ((OtrosServMunicipalesEIEL) lstElementos
                    .get(row)).getKwOtEnergias().toString()
                    : "";      
        case 14: 
            return ((OtrosServMunicipalesEIEL)lstElementos.get(row)).getFechaRevision() != null ? ((OtrosServMunicipalesEIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";   
        case 15: 
            return ((OtrosServMunicipalesEIEL)lstElementos.get(row)).getObservaciones();   
        case 16: 
			Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((CaptacionesEIEL) lstElementos.get(row)).getEstadoRevision().toString()); 
        case 17: 
            Estructuras.cargarEstructura("eiel_cob_serv_tlf_m");
            return LocalGISEIELUtils.getNameFromEstructura(((OtrosServMunicipalesEIEL) lstElementos.get(row)).getCoberturaTlf().toString());  
        case 18: 
            Estructuras.cargarEstructura("eiel_tv_dig_cable");
            return LocalGISEIELUtils.getNameFromEstructura(((OtrosServMunicipalesEIEL) lstElementos.get(row)).getTeleCable().toString());
         
        case 19: 
            return ((OtrosServMunicipalesEIEL)lstElementos.get(row)).getBloqueado() != null ? ((OtrosServMunicipalesEIEL) lstElementos
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
    public OtrosServMunicipalesEIEL getValueAt(int row) {
        
        return (OtrosServMunicipalesEIEL)lstElementos.get(row);
        
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
