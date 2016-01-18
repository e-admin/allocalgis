package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.Depuradora1EIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class Depuradora1CompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.clave"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.codorden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.trat_pr_1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.trat_pr_2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.trat_pr_3"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.trat_sc_1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.trat_sc_2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.trat_sc_3"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.trat_av_1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.trat_av_2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.trat_av_3"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.proc_cm_1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.proc_cm_2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.proc_cm_3"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.trat_ld_1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.trat_ld_2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.trat_ld_3"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.estado_revision"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora1.columna.bloqueado")};

	    
    public Depuradora1CompletoEIELTableModel() {        

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
            return ((Depuradora1EIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1:
        	return ((Depuradora1EIEL)lstElementos.get(row)).getClave();
        case 2: 
            return ((Depuradora1EIEL)lstElementos.get(row)).getCodINEProvincia()+ ((Depuradora1EIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 3: 
            return ((Depuradora1EIEL)lstElementos.get(row)).getCodOrden();
        case 4: 
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamiento primario");
            return LocalGISEIELUtils.getNameFromEstructura(((Depuradora1EIEL)lstElementos.get(row)).getTratPrimario1());
        case 5: 
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamiento primario");
            return LocalGISEIELUtils.getNameFromEstructura(((Depuradora1EIEL)lstElementos.get(row)).getTratPrimario2());
        case 6: 
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamiento primario");
            return LocalGISEIELUtils.getNameFromEstructura(((Depuradora1EIEL)lstElementos.get(row)).getTratPrimario3());
        case 7: 
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamiento secundario");
            return LocalGISEIELUtils.getNameFromEstructura(((Depuradora1EIEL)lstElementos.get(row)).getTratSecundario1());
        case 8: 
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamiento secundario");
            return LocalGISEIELUtils.getNameFromEstructura(((Depuradora1EIEL)lstElementos.get(row)).getTratSecundario2());
        case 9: 
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamiento secundario");
            return LocalGISEIELUtils.getNameFromEstructura(((Depuradora1EIEL)lstElementos.get(row)).getTratSecundario3());
        case 10: 
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamientos avanzados");
            return LocalGISEIELUtils.getNameFromEstructura(((Depuradora1EIEL)lstElementos.get(row)).getTratAvanzado1());
        case 11: 
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamientos avanzados");
            return LocalGISEIELUtils.getNameFromEstructura(((Depuradora1EIEL)lstElementos.get(row)).getTratAvanzado2());
        case 12: 
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamientos avanzados");
            return LocalGISEIELUtils.getNameFromEstructura(((Depuradora1EIEL)lstElementos.get(row)).getTratAvanzado3());
        case 13: 
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Procesos complementarios");
            return LocalGISEIELUtils.getNameFromEstructura(((Depuradora1EIEL)lstElementos.get(row)).getProcComplementario1());
        case 14: 
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Procesos complementarios");
            return LocalGISEIELUtils.getNameFromEstructura(((Depuradora1EIEL)lstElementos.get(row)).getProcComplementario2());
        case 15: 
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Procesos complementarios");
            return LocalGISEIELUtils.getNameFromEstructura(((Depuradora1EIEL)lstElementos.get(row)).getProcComplementario3());
        case 16: 
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamientos de fangos o lodos");
            return LocalGISEIELUtils.getNameFromEstructura(((Depuradora1EIEL)lstElementos.get(row)).getTratLodos1());
        case 17: 
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamientos de fangos o lodos");
            return LocalGISEIELUtils.getNameFromEstructura(((Depuradora1EIEL)lstElementos.get(row)).getTratLodos2());
        case 18: 
            Estructuras.cargarEstructura("eiel_Sist. de depuración, Tratamientos de fangos o lodos");
            return LocalGISEIELUtils.getNameFromEstructura(((Depuradora1EIEL)lstElementos.get(row)).getTratLodos3());
        case 19: 
            return ((Depuradora1EIEL) lstElementos.get(row)).getFechaRevision() != null ? ((Depuradora1EIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 20: 
			Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((Depuradora1EIEL) lstElementos.get(row)).getEstadoRevision().toString());
            
        default: 
            return null;
        
        }        
    }
    
    /**
     * Devuelve la Depuradora1EIEL completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto FincaCatastro se solicita
     * @return FincaCatastro completa
     */
    public Depuradora1EIEL getValueAt(int row) {
        
        return (Depuradora1EIEL)lstElementos.get(row);
        
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
