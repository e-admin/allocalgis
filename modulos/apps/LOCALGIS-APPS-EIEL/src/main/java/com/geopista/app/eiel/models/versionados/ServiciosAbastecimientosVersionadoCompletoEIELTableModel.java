/**
 * ServiciosAbastecimientosVersionadoCompletoEIELTableModel.java
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

import com.geopista.app.eiel.beans.ServiciosAbastecimientosEIEL;
import com.geopista.app.eiel.beans.VersionEiel;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class ServiciosAbastecimientosVersionadoCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoVersion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoAccion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoUsuario"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoFecha"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.viviendas_c_conex"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.viviendas_s_conex"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.consumo_inv"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.consumo_verano"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.viv_exceso_pres"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.viv_defic_presion"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.perdidas_agua"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.calidad_serv"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.long_deficit"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.viv_deficitarias"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.pobl_res_afect"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.pobl_est_afect"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.estado_revision"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.bloqueado")};

    public ServiciosAbastecimientosVersionadoCompletoEIELTableModel() {        
    	
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
        
        
        Object[] fila = (Object[]) lstElementos.get(row);
		switch (col) {
			case 0:
			case 1:
			case 2:
			case 4:
			case 5:
			case 6:
			case 7:

			case 20:
				return	 fila[col];
			case 22:
				Estructuras.cargarEstructura("eiel_Estado de revisión");
            	return fila[col]!=null? LocalGISEIELUtils.getNameFromEstructura(Integer.toString((Integer)fila[col])):"";
			case 15:
	            Estructuras.cargarEstructura("eiel_Calidad del servicio");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	         
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 16:
			case 17:
			case 18:
			case 19:
			case 21:

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
    public ServiciosAbastecimientosEIEL getValueAt(int row) {
    	
    	Object[] fila = (Object[]) lstElementos.get(row);
    	ServiciosAbastecimientosEIEL obj = new ServiciosAbastecimientosEIEL();
    	 
    	VersionEiel version = new VersionEiel();
		version.setIdVersion((Integer) fila[0]);
		version.setAccion((String) fila[1]);
		version.setUsuario((String) fila[2]);
		version.setFecha((Date) fila[3]); 
		
		obj.setVersion(version);
		obj.setCodINEProvincia((String) fila[4]);
		obj.setCodINEMunicipio((String) fila[5]); 
		obj.setCodINEEntidad((String) fila[6]);
		obj.setCodINEPoblamiento((String) fila[7]);
		obj.setViviendasConectadas((Integer) fila[8]);
		obj.setViviendasNoConectadas((Integer) fila[9]); 
		obj.setConsumoInvierno((Integer) fila[10]);
		obj.setConsumoVerano((Integer) fila[11]);
		obj.setViviendasExcesoPresion((Integer) fila[12]);
		obj.setViviendasDeficitPresion((Integer) fila[13]);
		obj.setPerdidasAgua((Integer) fila[14]);
		obj.setCalidadServicio((String) fila[15]);
		obj.setLongitudDeficitaria((Integer) fila[16]); 
		obj.setViviendasDeficitarias((Integer) fila[17]);
		obj.setPoblacionResidenteDeficitaria((Integer) fila[18]);
		obj.setPoblacionEstacionalDeficitaria((Integer) fila[19]);							
		obj.setObservaciones((String) fila[20]);
		obj.setFechaRevision((Date) fila[21]);
		obj.setEstadoRevision((Integer) fila[22]);														
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
