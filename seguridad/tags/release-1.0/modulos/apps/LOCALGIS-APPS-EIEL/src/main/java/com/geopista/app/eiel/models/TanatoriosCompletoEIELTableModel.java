package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.CementeriosEIEL;
import com.geopista.app.eiel.beans.TanatoriosEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class TanatoriosCompletoEIELTableModel  extends EIELCompletoTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.clave"),
//			I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.codprov"),
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
            I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.fecha_inst"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.bloqueado"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.acceso_s_ruedas"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.tanatorios.columna.obra_ejec")};

    public TanatoriosCompletoEIELTableModel() {        

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
        	return ((TanatoriosEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
        	return ((TanatoriosEIEL)lstElementos.get(row)).getClave();
        case 2: 
        	return ((TanatoriosEIEL)lstElementos.get(row)).getCodINEProvincia() + ((TanatoriosEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 3: 
            return getNombreNucleo( ((TanatoriosEIEL)lstElementos.get(row)).getCodINEEntidad(),((TanatoriosEIEL)lstElementos.get(row)).getCodINEPoblamiento());
        case 4:
        	return ((TanatoriosEIEL)lstElementos.get(row)).getCodINEPoblamiento();
        case 5:
        	return ((TanatoriosEIEL)lstElementos.get(row)).getCodOrden();
        case 6:
            return ((TanatoriosEIEL)lstElementos.get(row)).getNombre();
        case 7: 
            Estructuras.cargarEstructura("eiel_Titularidad Tanatorios");
            return LocalGISEIELUtils.getNameFromEstructura(((TanatoriosEIEL)lstElementos.get(row)).getTitularidad());
        case 8: 
            Estructuras.cargarEstructura("eiel_Gestor Tanatorios");
            return LocalGISEIELUtils.getNameFromEstructura(((TanatoriosEIEL)lstElementos.get(row)).getGestion());
        case 9:
            return ((TanatoriosEIEL) lstElementos.get(row)).getSupCubierta() != null ? ((TanatoriosEIEL) lstElementos
                    .get(row)).getSupCubierta().toString()
                    : "";
        case 10:
            return ((TanatoriosEIEL)lstElementos.get(row)).getSupLibre() != null ? ((TanatoriosEIEL) lstElementos
                    .get(row)).getSupLibre().toString()
                    : "";
        case 11:
            return ((TanatoriosEIEL)lstElementos.get(row)).getSupSolar() != null ? ((TanatoriosEIEL) lstElementos
                    .get(row)).getSupSolar().toString()
                    : "";
        case 12:
            return ((TanatoriosEIEL)lstElementos.get(row)).getSalas() != null ? ((TanatoriosEIEL) lstElementos
                    .get(row)).getSalas().toString()
                    : "";
        case 13: 
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            return LocalGISEIELUtils.getNameFromEstructura(((TanatoriosEIEL)lstElementos.get(row)).getEstado());
        case 14: 
            return ((TanatoriosEIEL)lstElementos.get(row)).getFechaInstalacion() != null ? ((TanatoriosEIEL) lstElementos
                    .get(row)).getFechaInstalacion().toString()
                    : "";
        case 15:
            return ((TanatoriosEIEL)lstElementos.get(row)).getObservaciones();
        case 16:
            return ((TanatoriosEIEL)lstElementos.get(row)).getFechaRevision() != null ? ((TanatoriosEIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 17:
            Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((TanatoriosEIEL) lstElementos.get(row)).getEstadoRevision().toString());
        case 18:
            return ((TanatoriosEIEL)lstElementos.get(row)).getFechaInstalacion() != null ? ((TanatoriosEIEL) lstElementos
                    .get(row)).getFechaInstalacion().toString()
                    : "";
        case 19:
            return ((TanatoriosEIEL)lstElementos.get(row)).getBloqueado() != null ? ((TanatoriosEIEL) lstElementos
                    .get(row)).getBloqueado().toString()
                    : "";
        case 20:
            Estructuras.cargarEstructura("eiel_Acceso con silla de ruedas");
            return LocalGISEIELUtils.getNameFromEstructura(((TanatoriosEIEL) lstElementos.get(row)).getAcceso_s_ruedas().toString());
        case 21:
            Estructuras.cargarEstructura("eiel_Obra ejecutada");
            return LocalGISEIELUtils.getNameFromEstructura(((TanatoriosEIEL) lstElementos.get(row)).getObra_ejec().toString());
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
        
        return (TanatoriosEIEL)lstElementos.get(row);
        
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
