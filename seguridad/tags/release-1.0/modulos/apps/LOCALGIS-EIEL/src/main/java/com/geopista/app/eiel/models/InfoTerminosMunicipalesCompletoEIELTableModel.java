/**
 * 
 */
package com.geopista.app.eiel.models;



import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.CementeriosEIEL;
import com.geopista.app.eiel.beans.NucleoEncuestado7EIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;


/**
 * @author seilagamo
 *
 */
public class InfoTerminosMunicipalesCompletoEIELTableModel extends EIELCompletoTableModel {
    
    private static String[] columnNames = {        
    	I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),    	
//        I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.codprov"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.codmunic"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.codentidad"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.codnucleo"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.tv_ant"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.tv_ca"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.tm_gsm"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.tm_umts"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.tm_gprs"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.correo"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.ba_rd"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.ba_xd"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.ba_wi"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.ba_ca"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.ba_rb"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.ba_st"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.capi"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.electric"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.gas"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.alu_v_sin"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.alu_l_sin"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.observ"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.fecha_revision"),
        I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.estado_revision"),
    I18N.get("LocalGISEIEL","localgiseiel.tabla.tm.columna.bloqueado")};

    private ArrayList lstElementos = new ArrayList();
    
    public InfoTerminosMunicipalesCompletoEIELTableModel() {
    }

    /**
     * Devuelve el objeto que contiene la celda en la posición indicada
     * @param row Índice de la fila
     * @param col Índice de la columna
     * 
     * @return Objeto contenido en la posición seleccionada
     */
    public Object getValueAt(int row, int col) {
        
        if (lstElementos.get(row) == null)
            return null;
        
        switch (col) {
        case 0: 
        	return ((NucleoEncuestado7EIEL)lstElementos.get(row)).getEstadoValidacion();
        case 1: 
            return ((NucleoEncuestado7EIEL)lstElementos.get(row)).getCodINEProvincia() + ((NucleoEncuestado7EIEL)lstElementos.get(row)).getCodINEMunicipio();
        case 2: 
            return getNombreNucleo( ((NucleoEncuestado7EIEL)lstElementos.get(row)).getCodINEEntidad(),((NucleoEncuestado7EIEL)lstElementos.get(row)).getCodINEPoblamiento());
        case 3: 
            return ((NucleoEncuestado7EIEL)lstElementos.get(row)).getCodINEPoblamiento();
        case 4: 
            Estructuras.cargarEstructura("eiel_Cobertura del servicio TV por antena");
            return LocalGISEIELUtils.getNameFromEstructura(((NucleoEncuestado7EIEL)lstElementos.get(row)).getTvAntena());
        case 5:
            Estructuras.cargarEstructura("eiel_Cobertura del servicio TV por cable");
            return LocalGISEIELUtils.getNameFromEstructura(((NucleoEncuestado7EIEL)lstElementos.get(row)).getTvCable());
        case 6: 
            Estructuras.cargarEstructura("eiel_Calidad de cobertura en telefonía con sistema GSM");
            return LocalGISEIELUtils.getNameFromEstructura(((NucleoEncuestado7EIEL)lstElementos.get(row)).getCalidadGSM());
        case 7: 
            Estructuras.cargarEstructura("eiel_Calidad de cobertura en telefonía con sistema UMTS");
            return LocalGISEIELUtils.getNameFromEstructura(((NucleoEncuestado7EIEL)lstElementos.get(row)).getCalidadUMTS());
        case 8: 
            Estructuras.cargarEstructura("eiel_Calidad de cobertura en telefonía con sistema GPRS");
            return LocalGISEIELUtils.getNameFromEstructura(((NucleoEncuestado7EIEL)lstElementos.get(row)).getCalidadGPRS());
        case 9: 
            Estructuras.cargarEstructura("eiel_Existencia de oficina de Correos");
            return LocalGISEIELUtils.getNameFromEstructura(((NucleoEncuestado7EIEL)lstElementos.get(row)).getCorreos());
        case 10:
            Estructuras.cargarEstructura("eiel_Acceso RDSI");
            return LocalGISEIELUtils.getNameFromEstructura(((NucleoEncuestado7EIEL)lstElementos.get(row)).getRdsi());
        case 11: 
            Estructuras.cargarEstructura("eiel_Acceso XDSL");
            return LocalGISEIELUtils.getNameFromEstructura(((NucleoEncuestado7EIEL)lstElementos.get(row)).getAdsl());
        case 12: 
            Estructuras.cargarEstructura("eiel_Acceso Inalámbrico");
            return LocalGISEIELUtils.getNameFromEstructura(((NucleoEncuestado7EIEL)lstElementos.get(row)).getWifi());
        case 13: 
            Estructuras.cargarEstructura("eiel_Acceso tv_cable");
            return LocalGISEIELUtils.getNameFromEstructura(((NucleoEncuestado7EIEL)lstElementos.get(row)).getInternetTV());
        case 14:
            Estructuras.cargarEstructura("eiel_Acceso red electrica");
            return LocalGISEIELUtils.getNameFromEstructura(((NucleoEncuestado7EIEL)lstElementos.get(row)).getInternetRed());
        case 15: 
            Estructuras.cargarEstructura("eiel_Acceso Satélite");
            return LocalGISEIELUtils.getNameFromEstructura(((NucleoEncuestado7EIEL)lstElementos.get(row)).getInternetSatelite());
        case 16: 
            Estructuras.cargarEstructura("eiel_Acceso Público");
            return LocalGISEIELUtils.getNameFromEstructura(((NucleoEncuestado7EIEL)lstElementos.get(row)).getInternetPublico());
        case 17: 
            Estructuras.cargarEstructura("eiel_Calidad del servicio o suministro");
            return LocalGISEIELUtils.getNameFromEstructura(((NucleoEncuestado7EIEL)lstElementos.get(row)).getCalidadElectricidad());
        case 18:
            Estructuras.cargarEstructura("eiel_Calidad del servicio o suministro");
            return LocalGISEIELUtils.getNameFromEstructura(((NucleoEncuestado7EIEL)lstElementos.get(row)).getCalidadGas());
        case 19: 
            return ((NucleoEncuestado7EIEL) lstElementos.get(row))
                    .getViviendasDeficitariasAlumbrado() != null ? ((NucleoEncuestado7EIEL) lstElementos
                    .get(row)).getViviendasDeficitariasAlumbrado().toString()
                    : "";
        case 20: 
            return ((NucleoEncuestado7EIEL) lstElementos.get(row))
                    .getLongitudDeficitariaAlumbrado() != null ? ((NucleoEncuestado7EIEL) lstElementos
                    .get(row)).getLongitudDeficitariaAlumbrado().toString()
                    : "";
        case 21: 
            return ((NucleoEncuestado7EIEL)lstElementos.get(row)).getObservaciones();
        case 22:
            return ((NucleoEncuestado7EIEL) lstElementos.get(row)).getFechaRevision() != null ? ((NucleoEncuestado7EIEL) lstElementos
                    .get(row)).getFechaRevision().toString()
                    : "";
        case 23:
			Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((NucleoEncuestado7EIEL) lstElementos.get(row)).getEstadoRevision().toString());
       
        case 24:
            return ((NucleoEncuestado7EIEL) lstElementos.get(row)).getBloqueado() != null ? ((NucleoEncuestado7EIEL) lstElementos
                    .get(row)).getBloqueado().toString()
                    : "";
        default:
            return null;
        }
        
    }
    
    /**
     * Devuelve la InfoTerminosMunicipalesEIEL completa de la fila seleccionada
     * @param row Índice de la fila cuyo objeto InfoTerminosMunicipalesEIEL se solicita
     * @return InfoTerminosMunicipalesEIEL completa
     */
    public NucleoEncuestado7EIEL getValueAt(int row) {
        return (NucleoEncuestado7EIEL)lstElementos.get(row);
    }
    
    
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
     * 
     * @param datos Datos a mostrar en el modelo
     */
    public void setData(ArrayList datos) {
        this.lstElementos = datos;
    }

    /**
     * Recupera los datos del modelo
     * 
     * @return Datos del modelo
     */
    public ArrayList getData() {
        return lstElementos;
    }    

}
