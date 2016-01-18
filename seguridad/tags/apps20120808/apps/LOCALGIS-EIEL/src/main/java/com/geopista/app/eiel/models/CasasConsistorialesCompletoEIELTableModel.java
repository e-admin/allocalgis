package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;


import com.geopista.app.eiel.beans.CasasConsistorialesEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class CasasConsistorialesCompletoEIELTableModel  extends EIELCompletoTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),		
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.clave"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.codorden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.nombrecc"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.tipo_cc"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.titular_cc"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.tenencia_cc"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.s_cubierta_cc"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.s_aire_cc"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.s_solar_cc"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.estado_cc"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.fecha_instal"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.estado_revision"),
    		I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.bloqueado"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.acceso_s_ruedas"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.cc.columna.obra_ejec")};

    public CasasConsistorialesCompletoEIELTableModel() {        
    	
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
        	return ((CasasConsistorialesEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
        	return ((CasasConsistorialesEIEL)lstElementos.get(row)).getClave();
        case 2: 
//            return ((CasasConsistorialesEIEL)lstElementos.get(row)).getCodINEMunicipio();
        	 return ((CasasConsistorialesEIEL)lstElementos.get(row)).getCodINEProvincia()+((CasasConsistorialesEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 3: 
            return getNombreNucleo( ((CasasConsistorialesEIEL)lstElementos.get(row)).getCodINEEntidad(),((CasasConsistorialesEIEL)lstElementos.get(row)).getCodINEPoblamiento());

       case 4: 
            return ((CasasConsistorialesEIEL)lstElementos.get(row)).getCodINEPoblamiento();
        case 5: 
            return ((CasasConsistorialesEIEL)lstElementos.get(row)).getCodOrden();
        case 6: 
            return ((CasasConsistorialesEIEL)lstElementos.get(row)).getNombre();
        case 7: 
            Estructuras.cargarEstructura("eiel_Tipo de Casa Consistorial");
            return LocalGISEIELUtils.getNameFromEstructura(((CasasConsistorialesEIEL)lstElementos.get(row)).getTipo());
        case 8: 
            Estructuras.cargarEstructura("eiel_Titularidad Casa Consistorial");
            return LocalGISEIELUtils.getNameFromEstructura(((CasasConsistorialesEIEL)lstElementos.get(row)).getTitular());
        case 9: 
            Estructuras.cargarEstructura("eiel_Forma de tenencia CC");
            return LocalGISEIELUtils.getNameFromEstructura(((CasasConsistorialesEIEL)lstElementos.get(row)).getTenencia());
        case 10: 
            return ((CasasConsistorialesEIEL) lstElementos.get(row)).getSupCubierta() != null ? ((CasasConsistorialesEIEL) lstElementos
                    .get(row)).getSupCubierta().toString()
                    : "";
        case 11: 
            return ((CasasConsistorialesEIEL)lstElementos.get(row)).getSupAire() != null ? ((CasasConsistorialesEIEL) lstElementos
                    .get(row)).getSupAire().toString()
                    : "";
        case 12: 
            return ((CasasConsistorialesEIEL)lstElementos.get(row)).getSupSolar() != null ? ((CasasConsistorialesEIEL) lstElementos
                    .get(row)).getSupSolar().toString()
                    : "";
        case 13: 
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            return LocalGISEIELUtils.getNameFromEstructura(((CasasConsistorialesEIEL)lstElementos.get(row)).getEstado());
        case 14: 
            return ((CasasConsistorialesEIEL)lstElementos.get(row)).getFechaInstalacion() != null ? ((CasasConsistorialesEIEL) lstElementos
                    .get(row)).getFechaInstalacion().toString()
                    : "";
        case 15: 
            return ((CasasConsistorialesEIEL)lstElementos.get(row)).getObservaciones();
        case 16: 
            return ((CasasConsistorialesEIEL)lstElementos.get(row)).getFechaRevision() != null ? ((CasasConsistorialesEIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 17: 
			Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((CasasConsistorialesEIEL) lstElementos.get(row)).getEstadoRevision().toString());
                    
        case 18: 
            return ((CasasConsistorialesEIEL)lstElementos.get(row)).getBloqueado() != null ? ((CasasConsistorialesEIEL) lstElementos
                    .get(row)).getBloqueado().toString()
                    : "";
        case 19: 
            Estructuras.cargarEstructura("eiel_Acceso con silla de ruedas");
            return LocalGISEIELUtils.getNameFromEstructura(((CasasConsistorialesEIEL) lstElementos.get(row)).getAcceso_s_ruedas().toString());
        case 20: 
            Estructuras.cargarEstructura("eiel_Obra ejecutada");
            return LocalGISEIELUtils.getNameFromEstructura(((CasasConsistorialesEIEL) lstElementos.get(row)).getObra_ejec().toString());
                   

        default: 
            return null;
        
        }        
    }
    
  
    public CasasConsistorialesEIEL getValueAt(int row) {
        
        return (CasasConsistorialesEIEL)lstElementos.get(row);
        
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
