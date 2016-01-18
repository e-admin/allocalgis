/**
 * TanatoriosVersionadoCompletoEIELTableModel.java
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

import com.geopista.app.eiel.beans.TanatoriosEIEL;
import com.geopista.app.eiel.beans.VersionEiel;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class TanatoriosVersionadoCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = { 
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoVersion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoAccion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoUsuario"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoFecha"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.clave"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.codorden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.nombre_ta"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.titular_ta"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.gestor_ta"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.s_cubi"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.s_aire"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.s_solar"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.salas"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.estado_ta"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.estado_revision"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.bloqueado"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.acceso_s_ruedas"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.obra_ejec")};

    public TanatoriosVersionadoCompletoEIELTableModel() {        

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
			case 6:
			case 7:
			case 8:	
			case 9:
			case 10:				
				return	 fila[col];
			case 11:
	            Estructuras.cargarEstructura("eiel_Titularidad Tanatorios");
	            return LocalGISEIELUtils.getNameFromEstructura( (String) fila[col]);
	        
			case 12:
		          Estructuras.cargarEstructura("eiel_Gestor Tanatorios");
		            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
		        
			case 17:
	            Estructuras.cargarEstructura("eiel_Estado de conservación");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 19:
				Estructuras.cargarEstructura("eiel_Estado de revisión");
            	return fila[col]!=null? LocalGISEIELUtils.getNameFromEstructura(Integer.toString((Integer)fila[col])):"";
			case 22:
	            Estructuras.cargarEstructura("eiel_Acceso con silla de ruedas");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
			case 23:
	            Estructuras.cargarEstructura("eiel_Obra ejecutada");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	            
			case 13:
			case 14:
			case 15:	
			case 16:
			case 18:
			case 20:
			case 21:

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
    public TanatoriosEIEL getValueAt(int row) {
    	
    	Object[] fila = (Object[]) lstElementos.get(row);
    	TanatoriosEIEL obj = new TanatoriosEIEL();
    	 

		VersionEiel version = new VersionEiel();
		version.setIdVersion((Integer) fila[0]);
		version.setAccion((String) fila[1]);
		version.setUsuario((String) fila[2]);
		version.setFecha((Date) fila[3]); 
		
		obj.setVersion(version);
		
		obj.setClave((String) fila[4]);
		obj.setCodINEProvincia((String) fila[5]);
		obj.setCodINEMunicipio((String) fila[6]); 
		obj.setCodINEEntidad((String) fila[7]);
		obj.setCodINEPoblamiento((String) fila[8]); 
		obj.setCodOrden((String) fila[9]);
		obj.setNombre((String) fila[10]);
		obj.setTitularidad((String) fila[11]);
		obj.setGestion((String) fila[12]); 
		obj.setSupCubierta((Integer) fila[13]); 
		obj.setSupLibre((Integer) fila[14]);
		obj.setSupSolar((Integer) fila[15]);
		obj.setSalas((Integer) fila[16]); 
		obj.setEstado((String) fila[17]);	
		obj.setObservaciones((String) fila[18]);
		obj.setFechaRevision((Date) fila[19]);
		obj.setEstadoRevision((Integer) fila[20]);
		obj.setBloqueado((String) fila[21]); 
		obj.setAcceso_s_ruedas((String) fila[22]);
		obj.setObra_ejec((String) fila[23]);
		
		
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
