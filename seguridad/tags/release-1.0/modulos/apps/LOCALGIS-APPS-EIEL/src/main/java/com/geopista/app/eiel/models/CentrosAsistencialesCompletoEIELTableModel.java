package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.CentrosAsistencialesEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class CentrosAsistencialesCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.clave"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.orden_as"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.nombre_as"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.tipo_as"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.titular_as"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.gestor_as"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.plazas"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.s_cubi"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.s_aire"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.s_solar"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.estado_as"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.fecha_inst"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.estado_revision"),
    		I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.bloqueado"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.acceso_s_ruedas"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.obra_ejec")};

    public CentrosAsistencialesCompletoEIELTableModel() {        
    	
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
        	return ((CentrosAsistencialesEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
        	return ((CentrosAsistencialesEIEL)lstElementos.get(row)).getClave();
        case 2: 
        	return ((CentrosAsistencialesEIEL)lstElementos.get(row)).getCodINEProvincia()+((CentrosAsistencialesEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 3: 
            return ((CentrosAsistencialesEIEL)lstElementos.get(row)).getCodINEEntidad();
        case 4: 
            return ((CentrosAsistencialesEIEL)lstElementos.get(row)).getCodINEPoblamiento();
        case 5: 
            return ((CentrosAsistencialesEIEL)lstElementos.get(row)).getOrdenAsistencial();
        case 6: 
            return ((CentrosAsistencialesEIEL)lstElementos.get(row)).getNombre();
        case 7: 
            Estructuras.cargarEstructura("eiel_Tipo de Centro Asistencial");
            return LocalGISEIELUtils.getNameFromEstructura(((CentrosAsistencialesEIEL)lstElementos.get(row)).getTipo());
        case 8:           
        	Estructuras.cargarEstructura("eiel_Titularidad_General_equip");
            return LocalGISEIELUtils.getNameFromEstructura(((CentrosAsistencialesEIEL)lstElementos.get(row)).getTitularidad());
        case 9: 
            Estructuras.cargarEstructura("eiel_Gestor_General_equip");
            return LocalGISEIELUtils.getNameFromEstructura(((CentrosAsistencialesEIEL)lstElementos.get(row)).getGestion());
        case 10: 
            return ((CentrosAsistencialesEIEL) lstElementos.get(row)).getPlazas() != null ? ((CentrosAsistencialesEIEL) lstElementos
                    .get(row)).getPlazas().toString()
                    : "";
        case 11: 
            return ((CentrosAsistencialesEIEL)lstElementos.get(row)).getSuperficieCubierta() != null ? ((CentrosAsistencialesEIEL) lstElementos
                    .get(row)).getSuperficieCubierta().toString()
                    : ""; 
        case 12: 
            return ((CentrosAsistencialesEIEL)lstElementos.get(row)).getSuperficieAireLibre() != null ? ((CentrosAsistencialesEIEL) lstElementos
                    .get(row)).getSuperficieAireLibre().toString()
                    : "";
        case 13: 
            return ((CentrosAsistencialesEIEL)lstElementos.get(row)).getSuperficieSolar() != null ? ((CentrosAsistencialesEIEL) lstElementos
                    .get(row)).getSuperficieSolar().toString()
                    : "";
        case 14: 
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            return LocalGISEIELUtils.getNameFromEstructura(((CentrosAsistencialesEIEL)lstElementos.get(row)).getEstado());
        case 15: 
            return ((CentrosAsistencialesEIEL)lstElementos.get(row)).getFechaInstalacion() != null ? ((CentrosAsistencialesEIEL) lstElementos
                    .get(row)).getFechaInstalacion().toString()
                    : "";
        case 16: 
            return ((CentrosAsistencialesEIEL)lstElementos.get(row)).getObservaciones();
        case 17: 
            return ((CentrosAsistencialesEIEL)lstElementos.get(row)).getFechaRevision() != null ? ((CentrosAsistencialesEIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 18: 
            Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((CentrosAsistencialesEIEL) lstElementos.get(row)).getEstadoRevision().toString());
        case 19: 
            return ((CentrosAsistencialesEIEL)lstElementos.get(row)).getBloqueado() != null ? ((CentrosAsistencialesEIEL) lstElementos
                    .get(row)).getBloqueado().toString()
                    : "";
        case 20: 
            Estructuras.cargarEstructura("eiel_Acceso con silla de ruedas");
            return LocalGISEIELUtils.getNameFromEstructura(((CentrosAsistencialesEIEL) lstElementos
                    .get(row)).getAcceso_s_ruedas().toString());
        case 21: 
            Estructuras.cargarEstructura("eiel_Obra ejecutada");
            return LocalGISEIELUtils.getNameFromEstructura(((CentrosAsistencialesEIEL) lstElementos.get(row)).getObra_ejec().toString());
        default: 
            return null;
        
        }        
    }
    
    /**
     * Devuelve la Depuradora1EIEL completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto FincaCatastro se solicita
     * @return FincaCatastro completa
     */
    public CentrosAsistencialesEIEL getValueAt(int row) {
        
        return (CentrosAsistencialesEIEL)lstElementos.get(row);
        
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
