/**
 * IncendiosProteccionVersionadoCompletoEIELTableModel.java
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

import com.geopista.app.eiel.beans.IncendiosProteccionEIEL;
import com.geopista.app.eiel.beans.VersionEiel;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class IncendiosProteccionVersionadoCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoVersion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoAccion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoUsuario"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoFecha"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.clave"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.orden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.nombre_ip"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.tipo_ip"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.titular_ip"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.gestor_ip"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.ambito"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.s_cubierta_ip"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.s_aire_ip"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.s_solar_ip"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.plan_profe"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.plan_volun"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.estado_ip"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.vehic_incendio"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.vehic_rescate"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.ambulancia"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.medios_aereos"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.otros_vehic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.quitanieve"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.detec_ince"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.otros"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.fecha_inst"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.estado_revision"),
		    I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.bloqueado"),
		    I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.acceso_s_ruedas"),
		    I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.obra_ejec")};

    public IncendiosProteccionVersionadoCompletoEIELTableModel() {        
    	
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
			case 8:
			case 9:
			case 10:
			case 30:
				return	 fila[col];
			case 11:
	            Estructuras.cargarEstructura("eiel_Tipo de Centro de Protección Civil");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 12:
				 Estructuras.cargarEstructura("eiel_Titularidad del Centro de Protección Civil");
		            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
		        
			case 13:
	            Estructuras.cargarEstructura("eiel_Gestor Extincion de Incendios y Proteccion Civil");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 14:
	            Estructuras.cargarEstructura("eiel_ámbito territorial");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	       
			case 20:	
	            Estructuras.cargarEstructura("eiel_Estado de conservación");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
			case 32:
				Estructuras.cargarEstructura("eiel_Estado de revisión");
            	return fila[col]!=null? LocalGISEIELUtils.getNameFromEstructura(Integer.toString((Integer)fila[col])):"";
			case 34:
	            Estructuras.cargarEstructura("eiel_Acceso con silla de ruedas");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	             
			case 35:
	            Estructuras.cargarEstructura("eiel_Obra ejecutada");
	            return LocalGISEIELUtils.getNameFromEstructura((String) fila[col]);
	          
			case 15:
			case 16:
			case 17:
			case 18:
			case 19:
			case 21:
			case 22:	
			case 23:
			case 24:
			case 25:
			case 26:
			case 27:
			case 28:
			case 29:
			case 31:
			case 33:
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
    public IncendiosProteccionEIEL getValueAt(int row) {
    	
    	Object[] fila = (Object[]) lstElementos.get(row);
    	IncendiosProteccionEIEL obj = new IncendiosProteccionEIEL();
    	 
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
		obj.setOrden((String) fila[9]);
		obj.setNombre((String) fila[10]); 
		obj.setTipo((String) fila[11]);
		obj.setTitular((String) fila[12]);
		obj.setGestor((String) fila[13]);
		obj.setAmbito((String) fila[14]);
		obj.setSuperficieCubierta((Integer) fila[15]);
		obj.setSuperficieAireLibre((Integer) fila[16]);
		obj.setSuperficieSolar((Integer) fila[17]);
		obj.setPlantillaProfesionales((Integer) fila[18]);
		obj.setPlantillaVoluntarios((Integer) fila[19]);
		obj.setEstado((String) fila[20]);
		obj.setVechiculosIncendios((Integer) fila[21]);
		obj.setVechiculosRescate((Integer) fila[22]);
		obj.setAmbulancias((Integer) fila[23]);
		obj.setMediosAereos((Integer) fila[24]);
		obj.setOtrosVehiculos((Integer) fila[25]);
		obj.setQuitanieves((Integer) fila[26]);
		obj.setSistemasDeteccionIncencios((Integer) fila[27]);
		obj.setOtros((Integer) fila[28]);
		obj.setFechaInstalacion((Date) fila[29]);
		obj.setObservaciones((String) fila[30]);
		obj.setFechaRevision((Date) fila[31]);
		obj.setEstadoRevision((Integer) fila[32]);
		obj.setBloqueado((String) fila[33]);
		obj.setAcceso_s_ruedas((String) fila[34]);
		obj.setObra_ejec((String) fila[35]);
		
		
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
