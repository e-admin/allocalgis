package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.CementeriosEIEL;
import com.geopista.app.eiel.beans.Encuestados1EIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class Encuestados1CompletoEIELTableModel  extends EIELCompletoTableModel {
	
	private static String[] columnNames = {
		I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.padron"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.pob_estacional"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.altitud"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.viviendas_total"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.hoteles"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.casas_rural"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.accesibilidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.estado_revision"),
    I18N.get("LocalGISEIEL","localgiseiel.tabla.p1.columna.bloqueado")};

    public Encuestados1CompletoEIELTableModel() {        
    	
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
        	return ((Encuestados1EIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
            return ((Encuestados1EIEL)lstElementos.get(row)).getCodINEProvincia() + ((Encuestados1EIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 2: 
            return getNombreNucleo( ((Encuestados1EIEL)lstElementos.get(row)).getCodINEEntidad(),((Encuestados1EIEL)lstElementos.get(row)).getCodINEPoblamiento());
        case 3: 
            return ((Encuestados1EIEL)lstElementos.get(row)).getCodINEPoblamiento();
        case 4: 
            return ((Encuestados1EIEL) lstElementos.get(row)).getPadron() != null ? ((Encuestados1EIEL) lstElementos
                    .get(row)).getPadron().toString()
                    : "";
        case 5: 
            return ((Encuestados1EIEL)lstElementos.get(row)).getPoblacionEstacional() != null ? ((Encuestados1EIEL) lstElementos
                    .get(row)).getPoblacionEstacional().toString()
                    : "";
        case 6: 
            return ((Encuestados1EIEL)lstElementos.get(row)).getAltitud() != null ? ((Encuestados1EIEL) lstElementos
                    .get(row)).getAltitud().toString()
                    : "";
        case 7: 
            return ((Encuestados1EIEL)lstElementos.get(row)).getViviendasTotales() != null ? ((Encuestados1EIEL) lstElementos
                    .get(row)).getViviendasTotales().toString()
                    : "";
        case 8: 
            return ((Encuestados1EIEL)lstElementos.get(row)).getPlazasHoteleras() != null ? ((Encuestados1EIEL) lstElementos
                    .get(row)).getPlazasHoteleras().toString()
                    : "";
        case 9: 
            return ((Encuestados1EIEL)lstElementos.get(row)).getPlazasCasasRurales() != null ? ((Encuestados1EIEL) lstElementos
                    .get(row)).getPlazasCasasRurales().toString()
                    : "";
        case 10: 
            Estructuras.cargarEstructura("eiel_Accesibilidad del núcleo de población");
            return LocalGISEIELUtils.getNameFromEstructura(((Encuestados1EIEL)lstElementos.get(row)).getAccesibilidad());
        case 11: 
            return ((Encuestados1EIEL)lstElementos.get(row)).getFechaRevision() != null ? ((Encuestados1EIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 12: 
            return ((Encuestados1EIEL)lstElementos.get(row)).getObservaciones();
        case 13: 
    		Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((Encuestados1EIEL) lstElementos.get(row)).getEstadoRevision().toString());
        
        case 14: 
            return ((Encuestados1EIEL)lstElementos.get(row)).getBloqueado() != null ? ((Encuestados1EIEL) lstElementos
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
    public Encuestados1EIEL getValueAt(int row) {
        
        return (Encuestados1EIEL)lstElementos.get(row);
        
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
