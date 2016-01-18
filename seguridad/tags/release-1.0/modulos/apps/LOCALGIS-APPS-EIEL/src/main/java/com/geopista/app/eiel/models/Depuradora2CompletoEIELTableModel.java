package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.Depuradora2EIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class Depuradora2CompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.clave"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.codorden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.titular_ed"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.gestor_ed"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.capacidad_ed"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.problem_1"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.problem_2"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.problem_3"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.lodo_gest"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.lodo_vert"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.lodo_inci"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.lodo_con_agri"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.lodo_sin_agri"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.lodo_ot"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.fecha_inst"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.estado_revision"),
    I18N.get("LocalGISEIEL","localgiseiel.tabla.depuradora2.columna.bloqueado")};

    public Depuradora2CompletoEIELTableModel() {        

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
            return ((Depuradora2EIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1:
        	return ((Depuradora2EIEL)lstElementos.get(row)).getClave();
        case 2: 
            return ((Depuradora2EIEL)lstElementos.get(row)).getCodINEProvincia() +((Depuradora2EIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 3: 
            return ((Depuradora2EIEL)lstElementos.get(row)).getCodOrden();
        case 4: 
			Estructuras.cargarEstructura("eiel_Titularidad");
            return LocalGISEIELUtils.getNameFromEstructura(((Depuradora2EIEL)lstElementos.get(row)).getTitular());
        case 5: 
			Estructuras.cargarEstructura("eiel_Gestión");
            return LocalGISEIELUtils.getNameFromEstructura(((Depuradora2EIEL)lstElementos.get(row)).getGestor());
        case 6: 
            return ((Depuradora2EIEL)lstElementos.get(row)).getCapacidad().toString();
        case 7: 
			Estructuras.cargarEstructura("eiel_EDAR problemas existentes");
            return LocalGISEIELUtils.getNameFromEstructura(((Depuradora2EIEL)lstElementos.get(row)).getProblemas1());
        case 8: 
			Estructuras.cargarEstructura("eiel_EDAR problemas existentes");
            return LocalGISEIELUtils.getNameFromEstructura(((Depuradora2EIEL)lstElementos.get(row)).getProblemas2());
        case 9: 
			Estructuras.cargarEstructura("eiel_EDAR problemas existentes");
            return LocalGISEIELUtils.getNameFromEstructura(((Depuradora2EIEL)lstElementos.get(row)).getProblemas3());
        case 10: 
			Estructuras.cargarEstructura("eiel_EDAR Gestión de lodos");
            return LocalGISEIELUtils.getNameFromEstructura(((Depuradora2EIEL)lstElementos.get(row)).getGestionLodos());
        case 11: 
            return ((Depuradora2EIEL) lstElementos.get(row)).getLodosVertedero() != null ? ((Depuradora2EIEL) lstElementos
                    .get(row)).getLodosVertedero().toString()
                    : "";
        case 12: 
            return ((Depuradora2EIEL)lstElementos.get(row)).getLodosIncineracion() != null ? ((Depuradora2EIEL) lstElementos
                    .get(row)).getLodosIncineracion().toString()
                    : "";
        case 13: 
            return ((Depuradora2EIEL)lstElementos.get(row)).getLodosAgrConCompostaje() != null ? ((Depuradora2EIEL) lstElementos
                    .get(row)).getLodosAgrConCompostaje().toString()
                    : "";
        case 14: 
            return ((Depuradora2EIEL)lstElementos.get(row)).getLodosAgrSinCompostaje() != null ? ((Depuradora2EIEL) lstElementos
                    .get(row)).getLodosAgrSinCompostaje().toString()
                    : "";
        case 15: 
            return ((Depuradora2EIEL)lstElementos.get(row)).getLodosOtroFinal() != null ? ((Depuradora2EIEL) lstElementos
                    .get(row)).getLodosOtroFinal().toString()
                    : "";
        case 16: 
            return ((Depuradora2EIEL)lstElementos.get(row)).getFechaInstalacion() != null ? ((Depuradora2EIEL) lstElementos
                    .get(row)).getFechaInstalacion().toString()
                    : "";
        case 17: 
            return ((Depuradora2EIEL)lstElementos.get(row)).getObservaciones();
        case 18: 
            return ((Depuradora2EIEL)lstElementos.get(row)).getFechaRevision() != null ? ((Depuradora2EIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 19: 
			Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((Depuradora2EIEL) lstElementos.get(row)).getEstadoRevision().toString());
       
        case 20: 
            return ((Depuradora2EIEL)lstElementos.get(row)).getBloqueado() != null ? ((Depuradora2EIEL) lstElementos
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
    public Depuradora2EIEL getValueAt(int row) {
        
        return (Depuradora2EIEL)lstElementos.get(row);
        
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
