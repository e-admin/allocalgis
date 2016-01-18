package com.geopista.app.eiel.models.versionados;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.DiseminadosEIEL;
import com.geopista.app.eiel.beans.TratamientosPotabilizacionEIEL;
import com.geopista.app.eiel.beans.VersionEiel;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class DiseminadosVersionadoCompletoEIELTableModel  extends DefaultTableModel {
	
	private static String[] columnNames = {
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoVersion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoAccion"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoUsuario"),
			I18N.get("LocalGISEIEL","localgiseiel.version.CampoFecha"),
            I18N.get("LocalGISEIEL","localgiseiel.tabla.di.columna.codprov"),
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

    public DiseminadosVersionadoCompletoEIELTableModel() {        
    	
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
        
        Object[] fila = (Object[]) lstElementos.get(row);
		switch (col) {
			case 0:
			case 1:
			case 2:
			case 4:
			case 5:
				return	 fila[col];
			case 55:
				Estructuras.cargarEstructura("eiel_Estado de revisión");
            	return fila[col]!=null? LocalGISEIELUtils.getNameFromEstructura(Integer.toString((Integer)fila[col])):"";
	            
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
			case 18:
			case 19:
			case 20:	
			case 21:				
			case 22:	
			case 23:
			case 24:
			case 25:
			case 26:
			case 27:
			case 28:
			case 29:
			case 30:
			case 31:
			case 32:
			case 33:
			case 34:
			case 35:
			case 36:
			case 37:
			case 38:
			case 39:
			case 40:
			case 41:
			case 42:
			case 43:
			case 44:
			case 45:
			case 46:
			case 47:
			case 48:
			case 49:
			case 50:
			case 51:
			case 52:
			case 53:
			case 54:
			case 56:
				return fila[col] != null ? fila[col].toString() : "";
			case 3:	
        		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        		return fila[col] != null ? formatter.format(fila[col]):"";				
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
        
    	
    	Object[] fila = (Object[]) lstElementos.get(row);
    	DiseminadosEIEL obj = new DiseminadosEIEL();
    	 
    	VersionEiel version = new VersionEiel();
		version.setIdVersion((Integer) fila[0]);
		version.setAccion((String) fila[1]);
		version.setUsuario((String) fila[2]);
		version.setFecha((Date) fila[3]); 
		
		obj.setVersion(version);
		
		obj.setCodINEProvincia((String) fila[4]);
		obj.setCodINEMunicipio((String) fila[5]);
		obj.setPadron((Integer) fila[6]);
		obj.setPoblacionEstacional((Integer) fila[7]);
		obj.setViviendasTotales((Integer) fila[8]);
		obj.setPlazasHoteleras((Integer) fila[9]);
		obj.setPlazasCasasRurales((Integer) fila[10]);
		obj.setLongitudAbastecimiento((Integer) fila[11]);
		obj.setViviendasConAbastecimiento((Integer) fila[12]);
		obj.setViviendasSinAbastecimiento((Integer) fila[13]);
		obj.setConsumoInvierno((Integer) fila[14]);
		obj.setConsumoVerano((Integer) fila[15]);
		obj.setViviendasExcesoPresion((Integer) fila[16]);
		obj.setViviendasDefectoPresion((Integer) fila[17]);
		obj.setLongDeficitariaAbast((Integer) fila[18]);
		obj.setViviendasDeficitAbast((Integer) fila[19]);
		obj.setPoblacionResidenteDefAbast((Integer) fila[20]);
		obj.setPoblacionEstacionalDefAbast((Integer) fila[21]);
		obj.setPoblacionResidenteAbastAuto((Integer) fila[22]);
		obj.setPoblacionEstacionalAbastAuto((Integer) fila[23]);
		obj.setViviendasDefAbastAuto((Integer) fila[24]);
		obj.setPoblacionResidenteDefAbastAuto((Integer) fila[25]);
		obj.setPoblacionEstacionalDefAbastAuto((Integer) fila[26]);
		obj.setFuentesNoControladas((Integer) fila[27]);
		obj.setLongitudSaneamiento((Integer) fila[28]);
		obj.setViviendasConSaneamiento((Integer) fila[29]);
		obj.setViviendasSinSaneamiento((Integer) fila[30]);
		obj.setLongDeficitariaSaneam((Integer) fila[31]);
		obj.setViviendasDefSaneam((Integer) fila[32]);
		obj.setPoblacionResidenteDefSaneam((Integer) fila[33]);
		obj.setPoblacionEstacionalDefSaneam((Integer) fila[34]);
		obj.setCaudalDesaguado((Integer) fila[35]);
		obj.setCaudalTratado((Integer) fila[36]);
		obj.setViviendasSaneamientoAuto((Integer) fila[37]);
		obj.setPoblacionEstacionalSaneamAuto((Integer) fila[38]);
		obj.setViviendasDeficitSaneamAuto((Integer) fila[39]);
		obj.setPoblacionResidenteDefSaneamAuto((Integer) fila[40]);
		obj.setPoblacionEstacionalDefSaneamAuto((Integer) fila[41]);
		obj.setTmBasura((Float) fila[42]);
		obj.setContenedores((Integer) fila[43]);	
		obj.setViviendasSinBasura((Integer) fila[44]);	
		obj.setPoblacionResidenteSinBasura((Integer) fila[45]);	
		obj.setPoblacionEstacionalSinBasura((Integer) fila[46]);
		obj.setPlantillaLimpieza((Integer) fila[47]);	
		obj.setPuntosLuz((Integer) fila[48]);	
		obj.setViviendasSinAlumbrado((Integer) fila[49]);	
		obj.setLongDeficitariaAlumbrado((Integer) fila[50]);
		obj.setVivendasAbastecimientoAuto((Integer) fila[51]);	
		obj.setFuentesControladas((Integer) fila[52]);	
		obj.setPoblacionResidenteSaneamAuto((Integer) fila[53]);
		obj.setFecha((Date) fila[54]);
		obj.setEstado((Integer) fila[55]);
		obj.setBloqueado((String) fila[56]);
    	
        return obj;
        
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
