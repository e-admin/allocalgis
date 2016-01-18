package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.CementeriosEIEL;
import com.geopista.app.eiel.beans.IncendiosProteccionEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class IncendiosProteccionCompletoEIELTableModel  extends EIELCompletoTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.clave"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.pi.columna.codprov"),
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

    public IncendiosProteccionCompletoEIELTableModel() {        
    	
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
        	return ((IncendiosProteccionEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1:
        	return ((IncendiosProteccionEIEL)lstElementos.get(row)).getClave();            
        case 2: 
            return ((IncendiosProteccionEIEL)lstElementos.get(row)).getCodINEProvincia() + ((IncendiosProteccionEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 3: 
            return getNombreNucleo( ((IncendiosProteccionEIEL)lstElementos.get(row)).getCodINEEntidad(),((IncendiosProteccionEIEL)lstElementos.get(row)).getCodINEPoblamiento());
        case 4: 
            return ((IncendiosProteccionEIEL)lstElementos.get(row)).getCodINEPoblamiento();
        case 5: 
            return ((IncendiosProteccionEIEL)lstElementos.get(row)).getOrden();
        case 6: 
            return ((IncendiosProteccionEIEL)lstElementos.get(row)).getNombre();
        case 7: 
            Estructuras.cargarEstructura("eiel_Tipo de Centro de Protección Civil");
            return LocalGISEIELUtils.getNameFromEstructura(((IncendiosProteccionEIEL)lstElementos.get(row)).getTipo());
        case 8: 
            Estructuras.cargarEstructura("eiel_Titularidad del Centro de Protección Civil");
            return LocalGISEIELUtils.getNameFromEstructura(((IncendiosProteccionEIEL)lstElementos.get(row)).getTitular());
        case 9: 
            Estructuras.cargarEstructura("eiel_Gestor Extincion de Incendios y Proteccion Civil");
            return LocalGISEIELUtils.getNameFromEstructura(((IncendiosProteccionEIEL)lstElementos.get(row)).getGestor());
        case 10: 
            Estructuras.cargarEstructura("eiel_ámbito territorial");
            return LocalGISEIELUtils.getNameFromEstructura(((IncendiosProteccionEIEL)lstElementos.get(row)).getAmbito());
        case 11: 
            return ((IncendiosProteccionEIEL) lstElementos.get(row)).getSuperficieCubierta() != null ? ((IncendiosProteccionEIEL) lstElementos
                    .get(row)).getSuperficieCubierta().toString()
                    : "";
        case 12: 
            return ((IncendiosProteccionEIEL)lstElementos.get(row)).getSuperficieAireLibre() != null ? ((IncendiosProteccionEIEL) lstElementos
                    .get(row)).getSuperficieAireLibre().toString()
                    : "";
        case 13: 
            return ((IncendiosProteccionEIEL)lstElementos.get(row)).getSuperficieSolar() != null ? ((IncendiosProteccionEIEL) lstElementos
                    .get(row)).getSuperficieSolar().toString()
                    : "";
        case 14: 
            return ((IncendiosProteccionEIEL)lstElementos.get(row)).getPlantillaProfesionales() != null ? ((IncendiosProteccionEIEL) lstElementos
                    .get(row)).getPlantillaProfesionales().toString()
                    : "";
        case 15: 
            return ((IncendiosProteccionEIEL)lstElementos.get(row)).getPlantillaVoluntarios() != null ? ((IncendiosProteccionEIEL) lstElementos
                    .get(row)).getPlantillaVoluntarios().toString()
                    : "";
        case 16: 
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            return LocalGISEIELUtils.getNameFromEstructura(((IncendiosProteccionEIEL)lstElementos.get(row)).getEstado());
        case 17: 
            return ((IncendiosProteccionEIEL)lstElementos.get(row)).getVechiculosIncendios() != null ? ((IncendiosProteccionEIEL) lstElementos
                    .get(row)).getVechiculosIncendios().toString()
                    : "";
        case 18: 
            return ((IncendiosProteccionEIEL)lstElementos.get(row)).getVechiculosRescate() != null ? ((IncendiosProteccionEIEL) lstElementos
                    .get(row)).getVechiculosRescate().toString()
                    : "";
        case 19: 
            return ((IncendiosProteccionEIEL)lstElementos.get(row)).getAmbulancias() != null ? ((IncendiosProteccionEIEL) lstElementos
                    .get(row)).getAmbulancias().toString()
                    : "";
        case 20: 
            return ((IncendiosProteccionEIEL)lstElementos.get(row)).getMediosAereos() != null ? ((IncendiosProteccionEIEL) lstElementos
                    .get(row)).getMediosAereos().toString()
                    : "";
        case 21: 
            return ((IncendiosProteccionEIEL)lstElementos.get(row)).getOtrosVehiculos() != null ? ((IncendiosProteccionEIEL) lstElementos
                    .get(row)).getOtrosVehiculos().toString()
                    : "";
        case 22: 
            return ((IncendiosProteccionEIEL)lstElementos.get(row)).getQuitanieves() != null ? ((IncendiosProteccionEIEL) lstElementos
                    .get(row)).getQuitanieves().toString()
                    : "";
        case 23: 
            return ((IncendiosProteccionEIEL)lstElementos.get(row)).getSistemasDeteccionIncencios() != null ? ((IncendiosProteccionEIEL) lstElementos
                    .get(row)).getSistemasDeteccionIncencios().toString()
                    : "";
        case 24: 
            return ((IncendiosProteccionEIEL)lstElementos.get(row)).getOtros() != null ? ((IncendiosProteccionEIEL) lstElementos
                    .get(row)).getOtros().toString()
                    : "";
        case 25: 
            return ((IncendiosProteccionEIEL)lstElementos.get(row)).getFechaInstalacion() != null ? ((IncendiosProteccionEIEL) lstElementos
                    .get(row)).getFechaInstalacion().toString()
                    : "";
        case 26: 
            return ((IncendiosProteccionEIEL)lstElementos.get(row)).getObservaciones();
        case 27: 
            return ((IncendiosProteccionEIEL)lstElementos.get(row)).getFechaRevision() != null ? ((IncendiosProteccionEIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 28: 
            Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((IncendiosProteccionEIEL) lstElementos.get(row)).getEstadoRevision().toString());
                            
        case 29: 
            return ((IncendiosProteccionEIEL)lstElementos.get(row)).getBloqueado() != null ? ((IncendiosProteccionEIEL) lstElementos
                    .get(row)).getBloqueado().toString()
                    : "";
                    
        case 30: 
            Estructuras.cargarEstructura("eiel_Acceso con silla de ruedas");
            return LocalGISEIELUtils.getNameFromEstructura(((IncendiosProteccionEIEL) lstElementos.get(row)).getAcceso_s_ruedas().toString());
                    
        case 31: 
            Estructuras.cargarEstructura("eiel_Obra ejecutada");
            return LocalGISEIELUtils.getNameFromEstructura(((IncendiosProteccionEIEL) lstElementos.get(row)).getObra_ejec().toString());
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
        
        return (IncendiosProteccionEIEL)lstElementos.get(row);
        
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
