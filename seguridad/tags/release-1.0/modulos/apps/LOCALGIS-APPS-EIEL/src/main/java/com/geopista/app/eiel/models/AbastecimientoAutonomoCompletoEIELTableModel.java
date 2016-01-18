/**
 * 
 */
package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.AbastecimientoAutonomoEIEL;
import com.geopista.app.eiel.beans.CasasConsistorialesEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;


/**
 * @author seilagamo
 *
 */
public class AbastecimientoAutonomoCompletoEIELTableModel extends EIELCompletoTableModel {
    protected static String[] columnNames = {
    	I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.clave"),
//        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.codprov"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.codmunic"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.codentidad"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.codnucleo"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.aau_vivien"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.aau_pob_re"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.aau_pob_es"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.aau_def_vi"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.aau_def_re"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.aau_def_es"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.aau_fecont"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.aau_fencon"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.aau_caudal"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.observ"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.fecha_revision"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.estado_revision"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.au.columna.bloqueado")};

    public AbastecimientoAutonomoCompletoEIELTableModel() {
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
        	return ((AbastecimientoAutonomoEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
        	return ((AbastecimientoAutonomoEIEL)lstElementos.get(row)).getClave();
        case 2:
        	 return ((AbastecimientoAutonomoEIEL)lstElementos.get(row)).getCodINEProvincia()+((AbastecimientoAutonomoEIEL)lstElementos.get(row)).getCodINEMunicipio();
//            return ((AbastecimientoAutonomoEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 3: 
            return getNombreNucleo( ((AbastecimientoAutonomoEIEL)lstElementos.get(row)).getCodINEEntidad(),((AbastecimientoAutonomoEIEL)lstElementos.get(row)).getCodINENucleo());

        case 4: 
            return ((AbastecimientoAutonomoEIEL)lstElementos.get(row)).getCodINENucleo();
        case 5: 
            return ((AbastecimientoAutonomoEIEL) lstElementos.get(row)).getViviendas() != null ? ((AbastecimientoAutonomoEIEL) lstElementos
                    .get(row)).getViviendas().toString()
                    : "";            
        case 6: 
            return ((AbastecimientoAutonomoEIEL) lstElementos.get(row)).getPoblacionResidente() != null ? ((AbastecimientoAutonomoEIEL) lstElementos
                    .get(row)).getPoblacionResidente().toString()
                    : "";
        case 7: 
            return ((AbastecimientoAutonomoEIEL) lstElementos.get(row)).getPoblacionEstacional() != null ? ((AbastecimientoAutonomoEIEL) lstElementos
                    .get(row)).getPoblacionEstacional().toString()
                    : "";
        case 8: 
            return ((AbastecimientoAutonomoEIEL) lstElementos.get(row)).getViviendasDeficitarias() != null ? ((AbastecimientoAutonomoEIEL) lstElementos
                    .get(row)).getViviendasDeficitarias().toString()
                    : "";
        case 9: 
            return ((AbastecimientoAutonomoEIEL) lstElementos.get(row)).getPoblacionResidenteDef() != null ? ((AbastecimientoAutonomoEIEL) lstElementos
                    .get(row)).getPoblacionResidenteDef().toString()
                    : "";
        case 10: 
            return ((AbastecimientoAutonomoEIEL)lstElementos.get(row)).getPoblacionEstacionalDef() != null ? ((AbastecimientoAutonomoEIEL) lstElementos
                    .get(row)).getPoblacionEstacionalDef().toString()
                    : "";            
        case 11: 
            return ((AbastecimientoAutonomoEIEL)lstElementos.get(row)).getFuentesControladas() != null ? ((AbastecimientoAutonomoEIEL) lstElementos
                    .get(row)).getFuentesControladas().toString()
                    : "";            
        case 12: 
            return ((AbastecimientoAutonomoEIEL)lstElementos.get(row)).getFuentesNoControladas() != null ? ((AbastecimientoAutonomoEIEL) lstElementos
                    .get(row)).getFuentesNoControladas().toString()
                    : "";            
        case 13: 
    		Estructuras.cargarEstructura("eiel_Disponibilidad de agua");
            return  LocalGISEIELUtils.getNameFromEstructura(((AbastecimientoAutonomoEIEL)lstElementos.get(row)).getSuficienciaCaudal());
        case 14: 
            return ((AbastecimientoAutonomoEIEL)lstElementos.get(row)).getObservaciones();
        case 15: 
            return ((AbastecimientoAutonomoEIEL) lstElementos.get(row)).getFechaRevision() != null ? ((AbastecimientoAutonomoEIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";   
        case 16: 
            Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((AbastecimientoAutonomoEIEL) lstElementos.get(row)).getEstadoRevision().toString());
                      
        case 17: 
            return ((AbastecimientoAutonomoEIEL) lstElementos.get(row)).getBloqueado() != null ? ((AbastecimientoAutonomoEIEL) lstElementos
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
    public AbastecimientoAutonomoEIEL getValueAt(int row) {
        
        return (AbastecimientoAutonomoEIEL)lstElementos.get(row);
        
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
