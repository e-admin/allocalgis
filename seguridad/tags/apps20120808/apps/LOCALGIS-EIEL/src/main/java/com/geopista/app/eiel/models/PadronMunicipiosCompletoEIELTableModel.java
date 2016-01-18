package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.PadronMunicipiosEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class PadronMunicipiosCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.n_hombres_a1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.n_mujeres_a1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.total_poblacion_a1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.n_hombres_a2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.n_mujeres_a2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.total_poblacion_a2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.fecha_a1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.fecha_a2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.fecha_actuliza"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.estado_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.observ"),
    I18N.get("LocalGISEIEL","localgiseiel.tabla.padronmunicipios.columna.bloqueado")};
   
    public PadronMunicipiosCompletoEIELTableModel() {        
    	
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
        	return ((PadronMunicipiosEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
        	return ((PadronMunicipiosEIEL)lstElementos.get(row)).getCodINEProvincia() + ((PadronMunicipiosEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 2: 
            return ((PadronMunicipiosEIEL) lstElementos.get(row)).getHombres_a1() != null ? ((PadronMunicipiosEIEL) lstElementos
                    .get(row)).getHombres_a1().toString()
                    : "";
        case 3: 
            return ((PadronMunicipiosEIEL)lstElementos.get(row)).getMujeres_a1() != null ? ((PadronMunicipiosEIEL) lstElementos
                    .get(row)).getMujeres_a1().toString()
                    : "";
        case 4: 
            return ((PadronMunicipiosEIEL)lstElementos.get(row)).getTotPobl_a1() != null ? ((PadronMunicipiosEIEL) lstElementos
                    .get(row)).getTotPobl_a1().toString()
                    : "";
        case 5: 
            return ((PadronMunicipiosEIEL)lstElementos.get(row)).getHombres_a2() != null ? ((PadronMunicipiosEIEL) lstElementos
                    .get(row)).getHombres_a2().toString()
                    : "";
        case 6: 
            return ((PadronMunicipiosEIEL)lstElementos.get(row)).getMujeres_a2() != null ? ((PadronMunicipiosEIEL) lstElementos
                    .get(row)).getMujeres_a2().toString()
                    : "";
        case 7: 
            return ((PadronMunicipiosEIEL)lstElementos.get(row)).getTotPobl_a2() != null ? ((PadronMunicipiosEIEL) lstElementos
                    .get(row)).getTotPobl_a2().toString()
                    : "";
        case 8: 
            return ((PadronMunicipiosEIEL)lstElementos.get(row)).getFecha_a1() != null ? ((PadronMunicipiosEIEL) lstElementos
                    .get(row)).getFecha_a1().toString()
                    : "";
        case 9: 
            return ((PadronMunicipiosEIEL)lstElementos.get(row)).getFecha_a2() != null ? ((PadronMunicipiosEIEL) lstElementos
                    .get(row)).getFecha_a2().toString()
                    : "";
        case 10: 
            return ((PadronMunicipiosEIEL)lstElementos.get(row)).getFechaActualizacion() != null ? ((PadronMunicipiosEIEL) lstElementos
                    .get(row)).getFechaActualizacion().toString()
                    : "";
        case 11: 
			Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((PadronMunicipiosEIEL) lstElementos.get(row)).getEstadoRevision().toString());
      
        case 12: 
            return ((PadronMunicipiosEIEL)lstElementos.get(row)).getObservaciones();
        case 13: 
            return ((PadronMunicipiosEIEL)lstElementos.get(row)).getBloqueado() != null ? ((PadronMunicipiosEIEL) lstElementos
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
    public PadronMunicipiosEIEL getValueAt(int row) {
        
        return (PadronMunicipiosEIEL)lstElementos.get(row);
        
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
