package com.geopista.app.eiel.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.TramosConduccionEIEL;
import com.geopista.app.eiel.beans.TramosConduccionEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class TramosConduccionCompletoEIELTableModel  extends DefaultTableModel {
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.clave"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.codorden"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.titular"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.gestor"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.estado"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.material"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.sist_trans"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.fecha_inst"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.estado_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.Conduccion.columna.bloqueado")};
	    
    public TramosConduccionCompletoEIELTableModel() {        

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
            return ((TramosConduccionEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1:
        	return ((TramosConduccionEIEL)lstElementos.get(row)).getClave();
        case 2: 
            return ((TramosConduccionEIEL)lstElementos.get(row)).getCodINEProvincia() + ((TramosConduccionEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 3: 
            return ((TramosConduccionEIEL)lstElementos.get(row)).getTramo_cn();
        case 4: 
			Estructuras.cargarEstructura("eiel_Titularidad");
            return LocalGISEIELUtils.getNameFromEstructura(((TramosConduccionEIEL)lstElementos.get(row)).getTitular());

        case 5: 
            Estructuras.cargarEstructura("eiel_Gestión");
            return LocalGISEIELUtils.getNameFromEstructura(((TramosConduccionEIEL)lstElementos.get(row)).getGestor());
        case 6: 
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            return LocalGISEIELUtils.getNameFromEstructura(((TramosConduccionEIEL)lstElementos.get(row)).getEstado());
        case 7: 
            Estructuras.cargarEstructura("eiel_material");
            return LocalGISEIELUtils.getNameFromEstructura(((TramosConduccionEIEL)lstElementos.get(row)).getMaterial());
        case 8: 
            Estructuras.cargarEstructura("eiel_sist_trans");
            return LocalGISEIELUtils.getNameFromEstructura(((TramosConduccionEIEL)lstElementos.get(row)).getSist_trans());
		case 9:	{
//            return ((TramosConduccionEIEL)lstElementos.get(row)).getFechaInstalacion();
			if(((TramosConduccionEIEL)lstElementos.get(row)).getFechaInstalacion()!=null)
				return formatter.format(((TramosConduccionEIEL)lstElementos.get(row)).getFechaInstalacion());
			else
				return "";
		}
        case 10: 
            return ((TramosConduccionEIEL)lstElementos.get(row)).getObservaciones();
        case 11: {
        	if(((TramosConduccionEIEL)lstElementos.get(row)).getFecha_revision()!=null)
				return formatter.format(((TramosConduccionEIEL)lstElementos.get(row)).getFecha_revision());
			else
				return "";
        }
        case 12: 
            Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((TramosConduccionEIEL)lstElementos.get(row)).getEstado_revision().toString());
        case 13: 
            return ((TramosConduccionEIEL)lstElementos.get(row)).getBloqueado();       
            
        default: 
            return null;
        
        }        
    }
    
    /**
     * Devuelve la Depuradora1EIEL completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto FincaCatastro se solicita
     * @return FincaCatastro completa
     */
    public TramosConduccionEIEL getValueAt(int row) {
        
        return (TramosConduccionEIEL)lstElementos.get(row);
        
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
