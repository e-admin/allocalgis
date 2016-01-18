package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.CementeriosEIEL;
import com.geopista.app.eiel.beans.ServiciosAbastecimientosEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class ServiciosAbastecimientosCompletoEIELTableModel  extends EIELCompletoTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.codentidad"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.codnucleo"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.viviendas_c_conex"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.viviendas_s_conex"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.consumo_inv"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.consumo_verano"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.viv_exceso_pres"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.viv_defic_presion"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.perdidas_agua"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.calidad_serv"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.long_deficit"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.viv_deficitarias"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.pobl_res_afect"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.pobl_est_afect"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.observ"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.fecha_revision"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.estado_revision"),
			I18N.get("LocalGISEIEL","localgiseiel.tabla.sa.columna.bloqueado")};

    public ServiciosAbastecimientosCompletoEIELTableModel() {        
    	
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
            return ((ServiciosAbastecimientosEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1:
            return ((ServiciosAbastecimientosEIEL)lstElementos.get(row)).getCodINEProvincia() + ((ServiciosAbastecimientosEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 2: 
            return getNombreNucleo( ((ServiciosAbastecimientosEIEL)lstElementos.get(row)).getCodINEEntidad(),((ServiciosAbastecimientosEIEL)lstElementos.get(row)).getCodINEPoblamiento());
        case 3: 
            return ((ServiciosAbastecimientosEIEL)lstElementos.get(row)).getCodINEPoblamiento();
        case 4: 
            return ((ServiciosAbastecimientosEIEL) lstElementos.get(row)).getViviendasConectadas() != null ? ((ServiciosAbastecimientosEIEL) lstElementos
                    .get(row)).getViviendasConectadas().toString()
                    : "";
        case 5: 
            return ((ServiciosAbastecimientosEIEL)lstElementos.get(row)).getViviendasNoConectadas() != null ? ((ServiciosAbastecimientosEIEL) lstElementos
                    .get(row)).getViviendasNoConectadas().toString()
                    : "";
        case 6: 
            return ((ServiciosAbastecimientosEIEL)lstElementos.get(row)).getConsumoInvierno() != null ? ((ServiciosAbastecimientosEIEL) lstElementos
                    .get(row)).getConsumoInvierno().toString()
                    : "";
        case 7: 
            return ((ServiciosAbastecimientosEIEL)lstElementos.get(row)).getConsumoVerano() != null ? ((ServiciosAbastecimientosEIEL) lstElementos
                    .get(row)).getConsumoVerano().toString()
                    : "";
        case 8: 
            return ((ServiciosAbastecimientosEIEL)lstElementos.get(row)).getViviendasExcesoPresion() != null ? ((ServiciosAbastecimientosEIEL) lstElementos
                    .get(row)).getViviendasExcesoPresion().toString()
                    : "";
        case 9: 
            return ((ServiciosAbastecimientosEIEL)lstElementos.get(row)).getViviendasDeficitPresion() != null ? ((ServiciosAbastecimientosEIEL) lstElementos
                    .get(row)).getViviendasDeficitPresion().toString()
                    : "";
        case 10: 
            return ((ServiciosAbastecimientosEIEL)lstElementos.get(row)).getPerdidasAgua() != null ? ((ServiciosAbastecimientosEIEL) lstElementos
                    .get(row)).getPerdidasAgua().toString()
                    : "";
        case 11: 
            Estructuras.cargarEstructura("eiel_Calidad del servicio");
            return LocalGISEIELUtils.getNameFromEstructura(((ServiciosAbastecimientosEIEL)lstElementos.get(row)).getCalidadServicio());
        case 12: 
            return ((ServiciosAbastecimientosEIEL) lstElementos.get(row)).getLogitudDeficitaria() != null ? ((ServiciosAbastecimientosEIEL) lstElementos
                    .get(row)).getLogitudDeficitaria().toString()
                    : "";
        case 13: 
            return ((ServiciosAbastecimientosEIEL) lstElementos.get(row))
                    .getViviendasDeficitarias() != null ? ((ServiciosAbastecimientosEIEL) lstElementos
                    .get(row)).getViviendasDeficitarias().toString()
                    : "";
        case 14: 
            return ((ServiciosAbastecimientosEIEL) lstElementos.get(row))
                    .getPoblacionResidenteDeficitaria() != null ? ((ServiciosAbastecimientosEIEL) lstElementos
                    .get(row)).getPoblacionResidenteDeficitaria().toString()
                    : "";
        case 15:
            return ((ServiciosAbastecimientosEIEL) lstElementos.get(row))
                    .getPoblacionEstacionalDeficitaria() != null ? ((ServiciosAbastecimientosEIEL) lstElementos
                    .get(row)).getPoblacionEstacionalDeficitaria().toString()
                    : "";
        case 16: 
            return ((ServiciosAbastecimientosEIEL)lstElementos.get(row)).getObservaciones();
        case 17: 
            return ((ServiciosAbastecimientosEIEL) lstElementos.get(row)).getFechaRevision() != null ? ((ServiciosAbastecimientosEIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 18: 
			Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((ServiciosAbastecimientosEIEL) lstElementos.get(row)).getEstadoRevision().toString());
       
        case 19: 
            return ((ServiciosAbastecimientosEIEL)lstElementos.get(row)).getBloqueado() != null ? ((ServiciosAbastecimientosEIEL) lstElementos
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
    public ServiciosAbastecimientosEIEL getValueAt(int row) {
        
        return (ServiciosAbastecimientosEIEL)lstElementos.get(row);
        
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
