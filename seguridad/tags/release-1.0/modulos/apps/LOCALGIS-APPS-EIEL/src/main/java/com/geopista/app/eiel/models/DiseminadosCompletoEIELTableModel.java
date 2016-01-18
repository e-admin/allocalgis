package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.DiseminadosEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class DiseminadosCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),
//            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.codprov"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.codmunic"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.padron_dis"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.pob_estaci"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.viv_total"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.hoteles"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.casas_rural"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.longitud"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.aag_v_cone"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.aag_v_ncon"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.aag_c_invi"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.aag_c_vera"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.aag_v_expr"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.aag_v_depr"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.aag_l_defi"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.aag_v_defi"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.aag_pr_def"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.aag_pe_def"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.aau_pob_re"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.aau_pob_es"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.aau_def_vi"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.aau_def_re"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.aau_def_es"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.aau_fencon"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.longi_ramal"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.syd_v_cone"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.syd_v_ncon"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.syd_l_defi"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.syd_v_defi"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.syd_pr_def"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.syd_pe_def"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.syd_c_desa"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.syd_c_trat"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.sau_vivien"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.sau_pob_es"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.sau_vi_def"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.sau_pob_re_def"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.sau_pob_es_def"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.produ_basu"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.contenedores"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.rba_v_sser"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.rba_pr_sse"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.rba_pe_sse"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.rba_plalim"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.puntos_luz"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.alu_v_sin"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.alu_l_sin"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.aau_vivien"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.aau_fecont"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.sau_pob_re"),
    I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.fecha_revision"),
    I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.estado_revision"),
    I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.bloqueado")};

    public DiseminadosCompletoEIELTableModel() {        
    	
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
        	return ((DiseminadosEIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getCodINEProvincia()+ ((DiseminadosEIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 2: 
            return ((DiseminadosEIEL) lstElementos.get(row)).getPadron() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getPadron().toString()
                    : "";
        case 3: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getPoblacionEstacional() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getPoblacionEstacional().toString()
                    : "";
        case 4: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getViviendasTotales() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getViviendasTotales().toString()
                    : "";
        case 5: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getPlazasHoteleras() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getPlazasHoteleras().toString()
                    : "";
        case 6: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getPlazasCasasRurales() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getPlazasCasasRurales().toString()
                    : "";
        case 7: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getLongitudAbastecimiento() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getLongitudAbastecimiento().toString()
                    : "";
        case 8: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getViviendasConAbastecimiento() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getViviendasConAbastecimiento().toString()
                    : "";
        case 9: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getViviendasSinAbastecimiento() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getViviendasSinAbastecimiento().toString()
                    : "";
        case 10: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getConsumoInvierno() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getConsumoInvierno().toString()
                    : "";
        case 11: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getConsumoVerano() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getConsumoVerano().toString()
                    : "";
        case 12: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getViviendasExcesoPresion() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getViviendasExcesoPresion().toString()
                    : "";
        case 13: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getViviendasDefectoPresion() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getViviendasDefectoPresion().toString()
                    : "";
        case 14: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getLongDeficitariaAbast() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getLongDeficitariaAbast().toString()
                    : "";
        case 15: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getViviendasDeficitAbast() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getViviendasDeficitAbast().toString()
                    : "";
        case 16: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getPoblacionResidenteDefAbast() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getPoblacionResidenteDefAbast().toString()
                    : "";
        case 17: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getPoblacionEstacionalDefAbast() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getPoblacionEstacionalDefAbast().toString()
                    : "";
        case 18: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getPoblacionResidenteAbastAuto() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getPoblacionResidenteAbastAuto().toString()
                    : "";
        case 19: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getPoblacionEstacionalAbastAuto() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getPoblacionEstacionalAbastAuto().toString()
                    : "";
        case 20: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getViviendasDefAbastAuto() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getViviendasDefAbastAuto().toString()
                    : "";
        case 21: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getPoblacionResidenteDefAbastAuto() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getPoblacionResidenteDefAbastAuto().toString()
                    : "";
        case 22: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getPoblacionEstacionalDefAbastAuto() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getPoblacionEstacionalDefAbastAuto().toString()
                    : "";
        case 23: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getFuentesNoControladas() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getFuentesNoControladas().toString()
                    : "";
        case 24: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getLongitudSaneamiento() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getLongitudSaneamiento().toString()
                    : "";
        case 25: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getViviendasConSaneamiento() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getViviendasConSaneamiento().toString()
                    : "";
        case 26: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getViviendasSinSaneamiento() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getViviendasSinSaneamiento().toString()
                    : "";
        case 27: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getLongDeficitariaSaneam() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getLongDeficitariaSaneam().toString()
                    : "";
        case 28: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getViviendasDefSaneam() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getViviendasDefSaneam().toString()
                    : "";
        case 29: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getPoblacionResidenteDefSaneam() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getPoblacionResidenteDefSaneam().toString()
                    : "";
        case 30: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getPoblacionEstacionalDefSaneam() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getPoblacionEstacionalDefSaneam().toString()
                    : "";
        case 31: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getCaudalDesaguado() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getCaudalDesaguado().toString()
                    : "";
        case 32: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getCaudalTratado() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getCaudalTratado().toString()
                    : "";
        case 33: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getViviendasSaneamientoAuto() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getViviendasSaneamientoAuto().toString()
                    : "";
        case 34: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getPoblacionEstacionalSaneamAuto() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getPoblacionEstacionalSaneamAuto().toString()
                    : "";
        case 35: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getViviendasDeficitSaneamAuto() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getViviendasDeficitSaneamAuto().toString()
                    : "";
        case 36: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getPoblacionResidenteDefSaneamAuto() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getPoblacionResidenteDefSaneamAuto().toString()
                    : "";
        case 37: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getPoblacionEstacionalDefSaneamAuto() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getPoblacionEstacionalDefSaneamAuto().toString()
                    : "";
        case 38: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getTmBasura() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getTmBasura().toString()
                    : "";
        case 39: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getContenedores() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getContenedores().toString()
                    : "";
        case 40: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getViviendasSinBasura() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getViviendasSinBasura().toString()
                    : "";
        case 41: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getPoblacionResidenteSinBasura() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getPoblacionResidenteSinBasura().toString()
                    : "";
        case 42: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getPoblacionEstacionalSinBasura() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getPoblacionEstacionalSinBasura().toString()
                    : "";
        case 43: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getPlantillaLimpieza() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getPlantillaLimpieza().toString()
                    : "";
        case 44: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getPuntosLuz() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getPuntosLuz().toString()
                    : "";
        case 45: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getViviendasSinAlumbrado() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getViviendasSinAlumbrado().toString()
                    : "";
        case 46: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getLongDeficitariaAlumbrado() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getLongDeficitariaAlumbrado().toString()
                    : "";
        case 47: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getVivendasAbastecimientoAuto() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getVivendasAbastecimientoAuto().toString()
                    : "";
        case 48: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getFuentesControladas() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getFuentesControladas().toString()
                    : "";
        case 49: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getPoblacionResidenteSaneamAuto() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getPoblacionResidenteSaneamAuto().toString()
                    : "";
        case 50: 
            return ((DiseminadosEIEL) lstElementos.get(row)).getFecha() != null ? ((DiseminadosEIEL) lstElementos
                    .get(row)).getFecha().toString()
                    : "";
        case 51: 
			Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((DiseminadosEIEL) lstElementos.get(row)).getEstado().toString());
       
        case 52: 
            return ((DiseminadosEIEL)lstElementos.get(row)).getPoblacionResidenteSaneamAuto() != null ? ((DiseminadosEIEL) lstElementos
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
    public DiseminadosEIEL getValueAt(int row) {
        
        return (DiseminadosEIEL)lstElementos.get(row);
        
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
