package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.CementeriosEIEL;
import com.geopista.app.eiel.beans.ServiciosRecogidaBasuraEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class ServiciosRecogidaBasuraCompletoEIELTableModel  extends EIELCompletoTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
//			I18N.get("LocalGISEIEL","localgiseiel.tabla.servbasura.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servbasura.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servbasura.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servbasura.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servbasura.columna.srb_viviendas_afec"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servbasura.columna.srb_pob_res_afect"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servbasura.columna.srb_pob_est_afect"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servbasura.columna.serv_limp_calles"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servbasura.columna.plantilla_serv_limp"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servbasura.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servbasura.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.servbasura.columna.estado_revision"),
    		I18N.get("LocalGISEIEL","localgiseiel.tabla.servbasura.columna.bloqueado")};

    public ServiciosRecogidaBasuraCompletoEIELTableModel() {        

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
        	return ((ServiciosRecogidaBasuraEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
        	return ((ServiciosRecogidaBasuraEIEL)lstElementos.get(row)).getCodINEProvincia() + ((ServiciosRecogidaBasuraEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 2: 
            return getNombreNucleo( ((ServiciosRecogidaBasuraEIEL)lstElementos.get(row)).getCodINEEntidad(),((ServiciosRecogidaBasuraEIEL)lstElementos.get(row)).getCodINEPoblamiento());
        case 3:
        	return ((ServiciosRecogidaBasuraEIEL)lstElementos.get(row)).getCodINEPoblamiento();
        case 4: 
            return ((ServiciosRecogidaBasuraEIEL) lstElementos.get(row)).getVivSinServicio() != null ? ((ServiciosRecogidaBasuraEIEL) lstElementos
                    .get(row)).getVivSinServicio().toString()
                    : "";
        case 5: 
            return ((ServiciosRecogidaBasuraEIEL)lstElementos.get(row)).getPoblResSinServicio() != null ? ((ServiciosRecogidaBasuraEIEL) lstElementos
                    .get(row)).getPoblResSinServicio().toString()
                    : "";
        case 6:
            return ((ServiciosRecogidaBasuraEIEL)lstElementos.get(row)).getPoblEstSinServicio() != null ? ((ServiciosRecogidaBasuraEIEL) lstElementos
                    .get(row)).getPoblEstSinServicio().toString()
                    : "";
        case 7:
            Estructuras.cargarEstructura("eiel_Servicio de limpieza de calles");
            return LocalGISEIELUtils.getNameFromEstructura(((ServiciosRecogidaBasuraEIEL)lstElementos.get(row)).getServLimpCalles());
        case 8: 
            return ((ServiciosRecogidaBasuraEIEL)lstElementos.get(row)).getPlantilla() != null ? ((ServiciosRecogidaBasuraEIEL) lstElementos
                    .get(row)).getPlantilla().toString()
                    : "";
        case 9: 
            return ((ServiciosRecogidaBasuraEIEL)lstElementos.get(row)).getObservaciones();
        case 10:
            return ((ServiciosRecogidaBasuraEIEL)lstElementos.get(row)).getFechaRevision() != null ? ((ServiciosRecogidaBasuraEIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 11:
			Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((ServiciosRecogidaBasuraEIEL) lstElementos.get(row)).getEstadoRevision().toString());
              
        case 12:
            return ((ServiciosRecogidaBasuraEIEL)lstElementos.get(row)).getBloqueado() != null ? ((ServiciosRecogidaBasuraEIEL) lstElementos
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
    public ServiciosRecogidaBasuraEIEL getValueAt(int row) {
        
        return (ServiciosRecogidaBasuraEIEL)lstElementos.get(row);
        
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
