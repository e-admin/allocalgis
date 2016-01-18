package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.CementeriosEIEL;
import com.geopista.app.eiel.beans.SaneamientoAutonomoEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class SaneamientoAutonomoCompletoEIELTableModel  extends EIELCompletoTableModel {
	
	private static String[] columnNames = { 
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.saneamautonomo.columna.clave"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.saneamautonomo.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.saneamautonomo.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.saneamautonomo.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.saneamautonomo.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.saneamautonomo.columna.tipo_sau"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.saneamautonomo.columna.estado_sau"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.saneamautonomo.columna.adecuacion_sau"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.saneamautonomo.columna.sau_vivien"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.saneamautonomo.columna.sau_pob_re"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.saneamautonomo.columna.sau_pob_es"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.saneamautonomo.columna.sau_vi_def"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.saneamautonomo.columna.sau_pob_re_def"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.saneamautonomo.columna.sau_pob_es_def"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.saneamautonomo.columna.fecha_inst"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.saneamautonomo.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.saneamautonomo.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.saneamautonomo.columna.estado_revision"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.saneamautonomo.columna.bloqueado")};

    public SaneamientoAutonomoCompletoEIELTableModel() {        

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
        	return ((SaneamientoAutonomoEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1:
        	return ((SaneamientoAutonomoEIEL)lstElementos.get(row)).getClave();
        case 2: 
        	return ((SaneamientoAutonomoEIEL)lstElementos.get(row)).getCodINEProvincia() + ((SaneamientoAutonomoEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 3: 
            return getNombreNucleo( ((SaneamientoAutonomoEIEL)lstElementos.get(row)).getCodINEEntidad(),((SaneamientoAutonomoEIEL)lstElementos.get(row)).getCodINEPoblamiento());
        case 4:
        	return ((SaneamientoAutonomoEIEL)lstElementos.get(row)).getCodINENucleo();
        case 5:
            Estructuras.cargarEstructura("eiel_Tipos de saneamiento autónomo");
            return LocalGISEIELUtils.getNameFromEstructura(((SaneamientoAutonomoEIEL)lstElementos.get(row)).getTipo());
        case 6: 
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            return LocalGISEIELUtils.getNameFromEstructura(((SaneamientoAutonomoEIEL)lstElementos.get(row)).getEstado());
        case 7: 
            Estructuras.cargarEstructura("eiel_Adecuación");
            return LocalGISEIELUtils.getNameFromEstructura(((SaneamientoAutonomoEIEL)lstElementos.get(row)).getAdecuacion());
        case 8:
            return ((SaneamientoAutonomoEIEL) lstElementos.get(row)).getViviendas() != null ? ((SaneamientoAutonomoEIEL) lstElementos
                    .get(row)).getViviendas().toString()
                    : "";
        case 9:
            return ((SaneamientoAutonomoEIEL)lstElementos.get(row)).getPoblResidente() != null ? ((SaneamientoAutonomoEIEL) lstElementos
                    .get(row)).getPoblResidente().toString()
                    : "";
        case 10:
            return ((SaneamientoAutonomoEIEL)lstElementos.get(row)).getPoblEstacional() != null ? ((SaneamientoAutonomoEIEL) lstElementos
                    .get(row)).getPoblEstacional().toString()
                    : "";
        case 11: 
            return ((SaneamientoAutonomoEIEL)lstElementos.get(row)).getVivDeficitarias() != null ? ((SaneamientoAutonomoEIEL) lstElementos
                    .get(row)).getVivDeficitarias().toString()
                    : "";
        case 12: 
            return ((SaneamientoAutonomoEIEL)lstElementos.get(row)).getPoblResDeficitaria() != null ? ((SaneamientoAutonomoEIEL) lstElementos
                    .get(row)).getPoblResDeficitaria().toString()
                    : "";
        case 13:
            return ((SaneamientoAutonomoEIEL)lstElementos.get(row)).getPoblEstDeficitaria() != null ? ((SaneamientoAutonomoEIEL) lstElementos
                    .get(row)).getPoblEstDeficitaria().toString()
                    : "";
        case 14:
            return ((SaneamientoAutonomoEIEL)lstElementos.get(row)).getFechaInstalacion() != null ? ((SaneamientoAutonomoEIEL) lstElementos
                    .get(row)).getFechaInstalacion().toString()
                    : "";
        case 15:
            return ((SaneamientoAutonomoEIEL)lstElementos.get(row)).getObservaciones();
        case 16: 
            return ((SaneamientoAutonomoEIEL)lstElementos.get(row)).getFechaRevision() != null ? ((SaneamientoAutonomoEIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 17: 
			Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((SaneamientoAutonomoEIEL) lstElementos.get(row)).getEstadoRevision().toString());
       
        default: 
            return null;
        
        }        
    }
    
    /**
     * Devuelve la Depuradora1EIEL completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto FincaCatastro se solicita
     * @return FincaCatastro completa
     */
    public SaneamientoAutonomoEIEL getValueAt(int row) {
        
        return (SaneamientoAutonomoEIEL)lstElementos.get(row);
        
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
