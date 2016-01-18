package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.PlaneamientoUrbanoEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class PlaneamientoUrbanoCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.planurb.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.planurb.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.planurb.columna.tipo_urba"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.planurb.columna.estado_tramit"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.planurb.columna.denominacion"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.planurb.columna.sup_muni"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.planurb.columna.fecha_bo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.planurb.columna.s_urbano"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.planurb.columna.s_urbanizable"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.planurb.columna.s_no_urbanizable"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.planurb.columna.s_no_urban_especial"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.planurb.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.planurb.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.planurb.columna.estado_revision"),
    I18N.get("LocalGISEIEL","localgiseiel.tabla.planurb.columna.orden_plan"),
    I18N.get("LocalGISEIEL","localgiseiel.tabla.planurb.columna.bloqueado")};

    public PlaneamientoUrbanoCompletoEIELTableModel() {        

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
        	return ((PlaneamientoUrbanoEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
        	return ((PlaneamientoUrbanoEIEL)lstElementos.get(row)).getCodINEProvincia() + ((PlaneamientoUrbanoEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 2: 
            Estructuras.cargarEstructura("eiel_Figuras de Planeamiento");
            return LocalGISEIELUtils.getNameFromEstructura(((PlaneamientoUrbanoEIEL)lstElementos.get(row)).getTipo());
        case 3: 
            Estructuras.cargarEstructura("eiel_Estado de tramitación de la Figura de Planeamiento");
            return LocalGISEIELUtils.getNameFromEstructura(((PlaneamientoUrbanoEIEL)lstElementos.get(row)).getEstado());
        case 4: 
            return ((PlaneamientoUrbanoEIEL)lstElementos.get(row)).getDenominacion();
        case 5: 
            return ((PlaneamientoUrbanoEIEL) lstElementos.get(row)).getSupMunicipal() != null ? ((PlaneamientoUrbanoEIEL) lstElementos
                    .get(row)).getSupMunicipal().toString()
                    : "";
        case 6: 
            return ((PlaneamientoUrbanoEIEL)lstElementos.get(row)).getFechaPublicacion() != null ? ((PlaneamientoUrbanoEIEL) lstElementos
                    .get(row)).getFechaPublicacion().toString()
                    : "";
        case 7: 
            return ((PlaneamientoUrbanoEIEL)lstElementos.get(row)).getSupUrbano() != null ? ((PlaneamientoUrbanoEIEL) lstElementos
                    .get(row)).getSupUrbano().toString()
                    : "";
        case 8: 
            return ((PlaneamientoUrbanoEIEL)lstElementos.get(row)).getSupUrbanizable() != null ? ((PlaneamientoUrbanoEIEL) lstElementos
                    .get(row)).getSupUrbanizable().toString()
                    : "";
        case 9: 
            return ((PlaneamientoUrbanoEIEL)lstElementos.get(row)).getSupNoUrbanizable() != null ? ((PlaneamientoUrbanoEIEL) lstElementos
                    .get(row)).getSupNoUrbanizable().toString()
                    : "";
        case 10: 
            return ((PlaneamientoUrbanoEIEL)lstElementos.get(row)).getSupNoUrbanizableEsp() != null ? ((PlaneamientoUrbanoEIEL) lstElementos
                    .get(row)).getSupNoUrbanizableEsp().toString()
                    : "";
        case 11: 
            return ((PlaneamientoUrbanoEIEL)lstElementos.get(row)).getFechaRevision() != null ? ((PlaneamientoUrbanoEIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 12: 
            return ((PlaneamientoUrbanoEIEL)lstElementos.get(row)).getObservaciones();
        case 13: 
			Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((PlaneamientoUrbanoEIEL) lstElementos.get(row)).getEstadoRevision().toString());
      
        case 14: 
            return ((PlaneamientoUrbanoEIEL)lstElementos.get(row)).getOrden() != null ? ((PlaneamientoUrbanoEIEL) lstElementos
                    .get(row)).getOrden().toString()
                    : "";
        case 15: 
            return ((PlaneamientoUrbanoEIEL)lstElementos.get(row)).getBloqueado() != null ? ((PlaneamientoUrbanoEIEL) lstElementos
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
    public PlaneamientoUrbanoEIEL getValueAt(int row) {
        
        return (PlaneamientoUrbanoEIEL)lstElementos.get(row);
        
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
