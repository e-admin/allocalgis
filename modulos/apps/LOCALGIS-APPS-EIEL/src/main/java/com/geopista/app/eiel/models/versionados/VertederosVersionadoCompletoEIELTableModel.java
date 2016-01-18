/**
 * VertederosVersionadoCompletoEIELTableModel.java
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

import com.geopista.app.eiel.beans.VersionEiel;
import com.geopista.app.eiel.beans.VertederosEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class VertederosVersionadoCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = { 
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoVersion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoAccion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoUsuario"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoFecha"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.clave"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.vertederos.columna.codprov"),
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
    
	public VertederosVersionadoCompletoEIELTableModel() {        

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
			case 24:
			case 29:
		
				return	 fila[col];
			case 26:
		           Estructuras.cargarEstructura("eiel_Categoria del vertedero");
		            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 15:	
		         Estructuras.cargarEstructura("eiel_filtraciones");

		            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
		        
			case 27:
	            Estructuras.cargarEstructura("eiel_Situación de la actividad de la instalación");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 30:
	            Estructuras.cargarEstructura("eiel_Posibilidad de ampliación");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 8:	
	            Estructuras.cargarEstructura("eiel_Tipo de vertedero");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
			case 9:
	            Estructuras.cargarEstructura("eiel_Titular del vertedero");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 10:	
	            Estructuras.cargarEstructura("eiel_Gestor del Vertedero");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 11:
		          Estructuras.cargarEstructura("eiel_olor");
		            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
		       
			case 12:
		          Estructuras.cargarEstructura("eiel_humo");
		            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
		       
			case 13:
	            Estructuras.cargarEstructura("eiel_cont animal");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 14:
	            Estructuras.cargarEstructura("eiel_riesgo inundación");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 16:
		           Estructuras.cargarEstructura("eiel_impacto visual");
		            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 17:
	            Estructuras.cargarEstructura("eiel_frecuentes averías");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
			case 18:
	            Estructuras.cargarEstructura("eiel_saturación");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
			case 19:
	            Estructuras.cargarEstructura("eiel_inestabilidad");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	        
			case 20:
	            Estructuras.cargarEstructura("eiel_otros vertedero");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 32:
				Estructuras.cargarEstructura("eiel_Estado de revisión");
            	return fila[col]!=null? LocalGISEIELUtils.getNameFromEstructura(Integer.toString((Integer)fila[col])):"";
	             
			case 21:
			case 22:
			case 23:
			case 25:
			case 28:
			case 31:
	
			case 33:
			case 34:
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
    public VertederosEIEL getValueAt(int row) {
        
    	Object[] fila = (Object[]) lstElementos.get(row);
    	VertederosEIEL obj = new VertederosEIEL();
    	 

		VersionEiel version = new VersionEiel();
		version.setIdVersion((Integer) fila[0]);
		version.setAccion((String) fila[1]);
		version.setUsuario((String) fila[2]);
		version.setFecha((Date) fila[3]); 
		
		obj.setVersion(version);
		obj.setClave((String) fila[4]);
		obj.setCodINEProvincia((String) fila[5]);
		obj.setCodINEMunicipio((String) fila[6]); 
		obj.setCodOrden((String) fila[7]);
		obj.setTipo((String) fila[8]);
		obj.setTitularidad((String) fila[9]);
		obj.setGestion((String) fila[10]); 
		obj.setOlores((String) fila[11]); 
		obj.setHumos((String) fila[12]);
		obj.setContAnimal((String) fila[13]);
		obj.setRsgoInundacion((String) fila[14]);
		obj.setFiltraciones((String) fila[15]);
		obj.setImptVisual((String) fila[16]);
		obj.setFrecAverias((String) fila[17]);
		obj.setSaturacion((String) fila[18]);
		obj.setInestabilidad((String) fila[19]);							
		obj.setOtros((String) fila[20]);
		obj.setCapTotal((Integer) fila[21]);
		obj.setCapOcupada((Integer) fila[22]);
		obj.setCapTransform((Integer) fila[23]);
		obj.setEstado((String) fila[24]);	
		obj.setVidaUtil((Integer) fila[25]);
		obj.setCategoria((String) fila[26]);
		obj.setActividad((String) fila[27]);
		obj.setFechaApertura((Integer) fila[28]);
		obj.setObservaciones((String) fila[29]);	
		obj.setPosbAmpliacion((String) fila[30]);
		obj.setFechaRevision((Date) fila[31]);
		obj.setEstadoRevision((Integer) fila[32]);
		obj.setBloqueado((String) fila[33]); 
		obj.setObra_ejecutada((String) fila[34]);
    	
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
