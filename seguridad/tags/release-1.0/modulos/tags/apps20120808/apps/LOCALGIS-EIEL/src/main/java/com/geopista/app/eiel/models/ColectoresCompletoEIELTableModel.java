package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.ColectorEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class ColectoresCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.clave"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.codorden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.titular"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.gestor"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.estado"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.material"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.sist_impulsion"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.tipo_red_interior"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.tip_interceptor"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.fecha_inst"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.estado_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Colectores.columna.bloqueado")};

	    
    public ColectoresCompletoEIELTableModel() {        

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
            return ((ColectorEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1:
        	return ((ColectorEIEL)lstElementos.get(row)).getClave();
        case 2: 
            return ((ColectorEIEL)lstElementos.get(row)).getCodINEProvincia() + ((ColectorEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 3: 
            return ((ColectorEIEL)lstElementos.get(row)).getCodOrden();
        case 4: 
            Estructuras.cargarEstructura("eiel_Titularidad");
            return LocalGISEIELUtils.getNameFromEstructura(((ColectorEIEL)lstElementos.get(row)).getTitularidad());
        case 5: 
            Estructuras.cargarEstructura("eiel_Gestión");
            return LocalGISEIELUtils.getNameFromEstructura(((ColectorEIEL)lstElementos.get(row)).getGestion());
        case 6: 
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            return LocalGISEIELUtils.getNameFromEstructura(((ColectorEIEL)lstElementos.get(row)).getEstado());
        case 7: 
            Estructuras.cargarEstructura("eiel_material");
            return LocalGISEIELUtils.getNameFromEstructura(((ColectorEIEL)lstElementos.get(row)).getMaterial());
        case 8: 
            Estructuras.cargarEstructura("eiel_Sistema de impulsión");
            return LocalGISEIELUtils.getNameFromEstructura(((ColectorEIEL)lstElementos.get(row)).getSist_impulsion());
        case 9: 
            Estructuras.cargarEstructura("eiel_tipo_red_interior");
            return LocalGISEIELUtils.getNameFromEstructura(((ColectorEIEL)lstElementos.get(row)).getTipo_red());
        case 10: 
            Estructuras.cargarEstructura("eiel_Colector de Tipo interceptor");
            return LocalGISEIELUtils.getNameFromEstructura(((ColectorEIEL)lstElementos.get(row)).getTip_interceptor());
        case 11: 
            return ((ColectorEIEL)lstElementos.get(row)).getFecha_inst();
        case 12: 
            return ((ColectorEIEL)lstElementos.get(row)).getObservaciones();
        case 13: 
            return ((ColectorEIEL)lstElementos.get(row)).getFechaRevision();
        case 14: 
			Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((ColectorEIEL) lstElementos.get(row)).getEstado_Revision().toString());    
        case 15: 
            return ((ColectorEIEL)lstElementos.get(row)).getBloqueado();
        default: 
            return null;
        
        }        
    }
    
    /**
     * Devuelve la Depuradora1EIEL completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto FincaCatastro se solicita
     * @return FincaCatastro completa
     */
    public ColectorEIEL getValueAt(int row) {
        
        return (ColectorEIEL)lstElementos.get(row);
        
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
