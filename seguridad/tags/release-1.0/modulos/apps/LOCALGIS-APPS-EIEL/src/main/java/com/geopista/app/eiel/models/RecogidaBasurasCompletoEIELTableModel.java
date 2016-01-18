package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.CementeriosEIEL;
import com.geopista.app.eiel.beans.RecogidaBasurasEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class RecogidaBasurasCompletoEIELTableModel  extends EIELCompletoTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.recogidabasuras.columna.clave"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.recogidabasuras.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.recogidabasuras.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.recogidabasuras.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.recogidabasuras.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.recogidabasuras.columna.tipo_rb"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.recogidabasuras.columna.gestor_rb"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.recogidabasuras.columna.periodicidad_rb"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.recogidabasuras.columna.calidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.recogidabasuras.columna.tm_res_urb"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.recogidabasuras.columna.n_contenedores"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.recogidabasuras.columna.fecha_actualiza"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.recogidabasuras.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.recogidabasuras.columna.estado_revision"),
    I18N.get("LocalGISEIEL","localgiseiel.tabla.recogidabasuras.columna.bloqueado")};

	    
    public RecogidaBasurasCompletoEIELTableModel() {        

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
        	return ((RecogidaBasurasEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
        	return ((RecogidaBasurasEIEL)lstElementos.get(row)).getClave();
        case 2: 
        	return ((RecogidaBasurasEIEL)lstElementos.get(row)).getCodINEProvincia() + ((RecogidaBasurasEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 3: 
            return getNombreNucleo( ((RecogidaBasurasEIEL)lstElementos.get(row)).getCodINEEntidad(),((RecogidaBasurasEIEL)lstElementos.get(row)).getCodINEPoblamiento());
        case 4:
        	return ((RecogidaBasurasEIEL)lstElementos.get(row)).getCodINEPoblamiento();
        case 5:
            Estructuras.cargarEstructura("eiel_Tipo de recogida selectiva de basura");
            return LocalGISEIELUtils.getNameFromEstructura(((RecogidaBasurasEIEL)lstElementos.get(row)).getTipo());
        case 6: 
            Estructuras.cargarEstructura("eiel_Gestor Recogida de Basuras");
            return LocalGISEIELUtils.getNameFromEstructura(((RecogidaBasurasEIEL)lstElementos.get(row)).getGestion());
        case 7: 
            Estructuras.cargarEstructura("eiel_Periodicidad");
            return LocalGISEIELUtils.getNameFromEstructura(((RecogidaBasurasEIEL)lstElementos.get(row)).getPeriodicidad());
        case 8:
            Estructuras.cargarEstructura("eiel_Calidad del Servicio_Recogida de Basuras");
            return LocalGISEIELUtils.getNameFromEstructura(((RecogidaBasurasEIEL)lstElementos.get(row)).getCalidad());
        case 9:
            return ((RecogidaBasurasEIEL) lstElementos.get(row)).getTonProducidas() != null ? ((RecogidaBasurasEIEL) lstElementos
                    .get(row)).getTonProducidas().toString()
                    : "";
        case 10:
            return ((RecogidaBasurasEIEL)lstElementos.get(row)).getNumContenedores() != null ? ((RecogidaBasurasEIEL) lstElementos
                    .get(row)).getNumContenedores().toString()
                    : "";
        case 11: 
            return ((RecogidaBasurasEIEL)lstElementos.get(row)).getFecharevision() != null ? ((RecogidaBasurasEIEL) lstElementos
                    .get(row)).getFecharevision().toString()
                    : "";
        case 12: 
            return ((RecogidaBasurasEIEL)lstElementos.get(row)).getObservaciones() != null ? ((RecogidaBasurasEIEL) lstElementos
                    .get(row)).getObservaciones().toString()
                    : "";
        case 13:
    		Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((RecogidaBasurasEIEL) lstElementos.get(row)).getEstadoRevision().toString());
        
        case 14:
            return ((RecogidaBasurasEIEL)lstElementos.get(row)).getBloqueado() != null ? ((RecogidaBasurasEIEL) lstElementos
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
    public RecogidaBasurasEIEL getValueAt(int row) {
        
        return (RecogidaBasurasEIEL)lstElementos.get(row);
        
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
