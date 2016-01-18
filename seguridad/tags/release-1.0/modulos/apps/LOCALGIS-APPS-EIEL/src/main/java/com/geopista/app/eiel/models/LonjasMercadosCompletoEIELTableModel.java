package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.CementeriosEIEL;
import com.geopista.app.eiel.beans.LonjasMercadosEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class LonjasMercadosCompletoEIELTableModel  extends EIELCompletoTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),		
            I18N.get("LocalGISEIEL","localgiseiel.tabla.lo.columna.clave"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.lo.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.lo.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.lo.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.lo.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.lo.columna.orden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.lo.columna.nombre_lm"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.lo.columna.tipo_lm"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.lo.columna.titular_lm"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.lo.columna.gestor_lm"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.lo.columna.s_cubierta_lm"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.lo.columna.s_aire_lm"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.lo.columna.s_solar_lm"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.lo.columna.estado_lm"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.lo.columna.fecha_inst"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.lo.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.lo.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.lo.columna.estado_revision"),
		    I18N.get("LocalGISEIEL","localgiseiel.tabla.lo.columna.bloqueado"),
		    I18N.get("LocalGISEIEL","localgiseiel.tabla.lo.columna.acceso_s_ruedas"),
		    I18N.get("LocalGISEIEL","localgiseiel.tabla.lo.columna.obra_ejec")};

    public LonjasMercadosCompletoEIELTableModel() {        
    	
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
            return ((LonjasMercadosEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
        	return ((LonjasMercadosEIEL)lstElementos.get(row)).getClave();
        case 2: 
            return ((LonjasMercadosEIEL)lstElementos.get(row)).getCodINEProvincia() + ((LonjasMercadosEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 3: 
            return getNombreNucleo( ((LonjasMercadosEIEL)lstElementos.get(row)).getCodINEEntidad(),((LonjasMercadosEIEL)lstElementos.get(row)).getCodINEPoblamiento());
        case 4: 
            return ((LonjasMercadosEIEL)lstElementos.get(row)).getCodINEPoblamiento();
        case 5: 
            return ((LonjasMercadosEIEL)lstElementos.get(row)).getOrden();
        case 6: 
            return ((LonjasMercadosEIEL)lstElementos.get(row)).getNombre();
        case 7: 
            Estructuras.cargarEstructura("eiel_Lonja/Mercado/Feria");
            return LocalGISEIELUtils.getNameFromEstructura(((LonjasMercadosEIEL)lstElementos.get(row)).getTipo());
        case 8: 
            Estructuras.cargarEstructura("eiel_Titularidad Lonja Mercado");
            return LocalGISEIELUtils.getNameFromEstructura(((LonjasMercadosEIEL)lstElementos.get(row)).getTitular());
        case 9: 
            Estructuras.cargarEstructura("eiel_Gestor Lonja Mercado");
            return LocalGISEIELUtils.getNameFromEstructura(((LonjasMercadosEIEL)lstElementos.get(row)).getGestion());
        case 10: 
            return ((LonjasMercadosEIEL) lstElementos.get(row)).getSuperficieCubierta() != null ? ((LonjasMercadosEIEL) lstElementos
                    .get(row)).getSuperficieCubierta().toString()
                    : "";
        case 11: 
            return ((LonjasMercadosEIEL)lstElementos.get(row)).getSuperficieAireLibre() != null ? ((LonjasMercadosEIEL) lstElementos
                    .get(row)).getSuperficieAireLibre().toString()
                    : "";
        case 12: 
            return ((LonjasMercadosEIEL)lstElementos.get(row)).getSuperficieSolar() != null ? ((LonjasMercadosEIEL) lstElementos
                    .get(row)).getSuperficieSolar().toString()
                    : "";
        case 13: 
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            return LocalGISEIELUtils.getNameFromEstructura(((LonjasMercadosEIEL)lstElementos.get(row)).getEstado());
        case 14: 
            return ((LonjasMercadosEIEL)lstElementos.get(row)).getFechaInstalacion() != null ? ((LonjasMercadosEIEL) lstElementos
                    .get(row)).getFechaInstalacion().toString()
                    : "";
        case 15: 
            return ((LonjasMercadosEIEL)lstElementos.get(row)).getObservaciones();
        case 16: 
            return ((LonjasMercadosEIEL)lstElementos.get(row)).getFechaRevision() != null ? ((LonjasMercadosEIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 17: 
            Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((LonjasMercadosEIEL) lstElementos.get(row)).getEstadoRevision().toString());

        case 18: 
            return ((LonjasMercadosEIEL)lstElementos.get(row)).getBloqueado() != null ? ((LonjasMercadosEIEL) lstElementos
                    .get(row)).getBloqueado().toString()
                    : "";
        case 19: 
            Estructuras.cargarEstructura("eiel_Acceso con silla de ruedas");
            return LocalGISEIELUtils.getNameFromEstructura(((LonjasMercadosEIEL) lstElementos.get(row)).getAcceso_s_ruedas().toString());
        case 20: 
            Estructuras.cargarEstructura("eiel_Obra ejecutada");
            return LocalGISEIELUtils.getNameFromEstructura(((LonjasMercadosEIEL) lstElementos.get(row)).getObra_ejec().toString());
        default: 
            return null;
        
        }        
    }
    
    /**
     * Devuelve la Depuradora1EIEL completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto FincaCatastro se solicita
     * @return FincaCatastro completa
     */
    public LonjasMercadosEIEL getValueAt(int row) {
        
        return (LonjasMercadosEIEL)lstElementos.get(row);
        
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
