/**
 * OtrosServMunicipalesVersionadoCompletoEIELTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.models.versionados;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.OtrosServMunicipalesEIEL;
import com.geopista.app.eiel.beans.VersionEiel;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class OtrosServMunicipalesVersionadoCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoVersion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoAccion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoUsuario"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoFecha"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.otrosserviciosmunicipales.columna.codprov"),
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
    
    public OtrosServMunicipalesVersionadoCompletoEIELTableModel() {        

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
        
        Object[] fila = (Object[]) lstElementos.get(row);
		switch (col) {
			case 0:
			case 1:
			case 2:
			case 4:
			case 5:
			case 19:
				return	 fila[col];
				
			case 6:
	            Estructuras.cargarEstructura("eiel_sw_inf_grl");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 7:
	            Estructuras.cargarEstructura("eiel_sw_inf_tur");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);   
	       
			case 8:
	            Estructuras.cargarEstructura("eiel_sw_gb_elec");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
			case 9:
	            Estructuras.cargarEstructura("eiel_Ordenanza Soterramiento");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);   
	        
			case 10:
	            Estructuras.cargarEstructura("eiel_En Eolica");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 12:
	            Estructuras.cargarEstructura("eiel_En Solar");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 14:
		          Estructuras.cargarEstructura("eiel_En Mareo");
		            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
		        
			case 16:
	            Estructuras.cargarEstructura("eiel_En Otras");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 20:
				Estructuras.cargarEstructura("eiel_Estado de revisión");
            	return fila[col]!=null? LocalGISEIELUtils.getNameFromEstructura(Integer.toString((Integer)fila[col])):"";
			case 21:
	            Estructuras.cargarEstructura("eiel_cob_serv_tlf_m");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);  
	        
			case 22:
	            Estructuras.cargarEstructura("eiel_tv_dig_cable");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	          
			case 11:
			case 13:
			case 15:
			case 17:
			case 18:
			case 23:
				return fila[col] != null ? fila[col].toString() : "";
			case 3:	
        		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        		return fila[col] != null ? formatter.format(fila[col]):"";				
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
        
    	Object[] fila = (Object[]) lstElementos.get(row);
    	OtrosServMunicipalesEIEL obj = new OtrosServMunicipalesEIEL();
    	 

		VersionEiel version = new VersionEiel();
		version.setIdVersion((Integer) fila[0]);
		version.setAccion((String) fila[1]);
		version.setUsuario((String) fila[2]);
		version.setFecha((Date) fila[3]); 
		
		obj.setVersion(version);
		
    	obj.setCodINEProvincia((String) fila[4]);
		obj.setCodINEMunicipio((String) fila[5]);
		obj.setSwInfGeneral((String) fila[6]);
		obj.setSwInfTuristica((String) fila[7]);
		obj.setSwGbElectronico((String) fila[8]);
		obj.setOrdSoterramiento((String) fila[9]);
		obj.setEnEolica((String) fila[10]);
		obj.setKwEolica((Integer) fila[11]);
		obj.setEnSolar((String) fila[12]);
		obj.setKwSolar((Integer) fila[13]);
		obj.setPlMareomotriz((String) fila[14]);
		obj.setKwMareomotriz((Integer) fila[15]);
		obj.setOtEnergias((String) fila[16]);
		obj.setKwOtEnergias((Integer) fila[17]);
		obj.setFechaRevision((Date) fila[18]);
		obj.setObservaciones((String) fila[19]);
		obj.setEstadoRevision((Integer) fila[20]);
		obj.setCoberturaTlf((String) fila[21]);
		obj.setTeleCable((String) fila[22]); 
		obj.setBloqueado((String) fila[23]);
		
        return obj;
        
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
