package com.geopista.app.eiel.models;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.eiel.beans.CementeriosEIEL;
import com.geopista.app.eiel.beans.InstalacionesDeportivasEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class InstalacionesDeportivasCompletoEIELTableModel extends EIELCompletoTableModel {

    private static String[] columnNames = {
    		I18N.get("LocalGISEIEL","localgiseiel.tabla.generico.columna.estado"),    	
            I18N.get("LocalGISEIEL", "localgiseiel.tabla.cc.columna.clave"),
//            I18N.get("LocalGISEIEL", "localgiseiel.tabla.cc.columna.codprov"),
            I18N.get("LocalGISEIEL", "localgiseiel.tabla.cc.columna.codmunic"),
            I18N.get("LocalGISEIEL", "localgiseiel.tabla.cc.columna.codentidad"),
            I18N.get("LocalGISEIEL", "localgiseiel.tabla.cc.columna.codnucleo"),
            I18N.get("LocalGISEIEL", "localgiseiel.tabla.cc.columna.codorden"),
            I18N.get("LocalGISEIEL", "localgiseiel.tabla.cc.columna.nombre_id"),
            I18N.get("LocalGISEIEL", "localgiseiel.tabla.cc.columna.tipo_id"),
            I18N.get("LocalGISEIEL", "localgiseiel.tabla.cc.columna.titular_id"),
            I18N.get("LocalGISEIEL", "localgiseiel.tabla.cc.columna.gestor_id"),
            I18N.get("LocalGISEIEL", "localgiseiel.tabla.cc.columna.s_cubierta_id"),
            I18N.get("LocalGISEIEL", "localgiseiel.tabla.cc.columna.s_aire_id"),
            I18N.get("LocalGISEIEL", "localgiseiel.tabla.cc.columna.s_solar_id"),
            I18N.get("LocalGISEIEL", "localgiseiel.tabla.cc.columna.estado_id"),
            I18N.get("LocalGISEIEL", "localgiseiel.tabla.cc.columna.fecha_inst"),
            I18N.get("LocalGISEIEL", "localgiseiel.tabla.cc.columna.observ"),
            I18N.get("LocalGISEIEL", "localgiseiel.tabla.cc.columna.fecha_revision"),
            I18N.get("LocalGISEIEL", "localgiseiel.tabla.cc.columna.estado_revision") ,
    I18N.get("LocalGISEIEL", "localgiseiel.tabla.cc.columna.bloqueado"),
    I18N.get("LocalGISEIEL", "localgiseiel.tabla.cc.columna.acceso_s_ruedas"),
    I18N.get("LocalGISEIEL", "localgiseiel.tabla.cc.columna.obra_ejec"),
    I18N.get("LocalGISEIEL", "localgiseiel.tabla.cc.columna.inst_pertenece")};
    public InstalacionesDeportivasCompletoEIELTableModel() {

    }

    private ArrayList lstElementos = new ArrayList();

    /**
     * @return número de columnas de la tabla
     */
    public int getColumnCount() {
        if (columnNames != null) {
            return columnNames.length;
        } else
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
     * 
     * @param col
     *            Índice de la columna
     * @return nombre de la columna
     */
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /**
     * Devuelve el objeto que contiene la celda en la posición indicada
     * 
     * @param row
     *            Índice de la fila
     * @param col
     *            Índice de la columna
     * 
     * @return Objeto contenido en la posición seleccionada
     */
    public Object getValueAt(int row, int col) {

        if (lstElementos.get(row) == null)
            return null;

        switch (col) {
        case 0:
            return ((InstalacionesDeportivasEIEL) lstElementos.get(row)).getEstadoValidacion();
        case 1:
        	return ((InstalacionesDeportivasEIEL) lstElementos.get(row)).getClave();
        case 2:
            return ((InstalacionesDeportivasEIEL) lstElementos.get(row)).getCodINEProvincia() + ((InstalacionesDeportivasEIEL) lstElementos.get(row)).getCodINEMunicipio();
        case 3: 
            return getNombreNucleo( ((InstalacionesDeportivasEIEL)lstElementos.get(row)).getCodINEEntidad(),((InstalacionesDeportivasEIEL)lstElementos.get(row)).getCodINEPoblamiento());
        case 4:
            return ((InstalacionesDeportivasEIEL) lstElementos.get(row)).getCodINEPoblamiento();
        case 5:
            return ((InstalacionesDeportivasEIEL) lstElementos.get(row)).getOrdenIdDeportes();
        case 6:
            return ((InstalacionesDeportivasEIEL) lstElementos.get(row)).getNombre();
        case 7:
            Estructuras.cargarEstructura("eiel_Tipo de instalaciones deportivas");
            return LocalGISEIELUtils.getNameFromEstructura(((InstalacionesDeportivasEIEL) lstElementos.get(row)).getTipo());
        case 8:
            Estructuras.cargarEstructura("eiel_Titularidad_General_equip");
            return LocalGISEIELUtils.getNameFromEstructura(((InstalacionesDeportivasEIEL) lstElementos.get(row)).getTitular());
        case 9:
            Estructuras.cargarEstructura("eiel_Gestor_General_equip");
            return LocalGISEIELUtils.getNameFromEstructura(((InstalacionesDeportivasEIEL) lstElementos.get(row)).getGestor());
        case 10:
            return ((InstalacionesDeportivasEIEL) lstElementos.get(row)).getSupCubierta() != null ? ((InstalacionesDeportivasEIEL) lstElementos
                    .get(row)).getSupCubierta()
                    : "";
        case 11:
            return ((InstalacionesDeportivasEIEL) lstElementos.get(row)).getSupAire() != null ? ((InstalacionesDeportivasEIEL) lstElementos
                    .get(row)).getSupAire()
                    : "";
        case 12:
            return ((InstalacionesDeportivasEIEL) lstElementos.get(row)).getSupSolar() != null ? ((InstalacionesDeportivasEIEL) lstElementos
                    .get(row)).getSupSolar()
                    : "";
        case 13:
            Estructuras.cargarEstructura("eiel_Estado de conservación");
            return LocalGISEIELUtils.getNameFromEstructura(((InstalacionesDeportivasEIEL) lstElementos.get(row)).getEstado());
        case 14:
            return ((InstalacionesDeportivasEIEL) lstElementos.get(row)).getFechaInstalacion() != null ? ((InstalacionesDeportivasEIEL) lstElementos
                    .get(row)).getFechaInstalacion()
                    : "";
        case 15:
            return ((InstalacionesDeportivasEIEL) lstElementos.get(row)).getObservaciones();
        case 16:
            return ((InstalacionesDeportivasEIEL) lstElementos.get(row)).getFechaRevision() != null ? ((InstalacionesDeportivasEIEL) lstElementos
                    .get(row)).getFechaRevision()
                    : "";
        case 17:
            Estructuras.cargarEstructura("eiel_Estado de revisión");
            return LocalGISEIELUtils.getNameFromEstructura(((InstalacionesDeportivasEIEL) lstElementos.get(row)).getEstadoRevision().toString());
        case 18:
            return ((InstalacionesDeportivasEIEL) lstElementos.get(row)).getBloqueado() != null ? ((InstalacionesDeportivasEIEL) lstElementos
                    .get(row)).getBloqueado()
                    : "";
        case 19:
            Estructuras.cargarEstructura("eiel_Acceso con silla de ruedas");
            return LocalGISEIELUtils.getNameFromEstructura(((InstalacionesDeportivasEIEL) lstElementos.get(row)).getAcceso_s_ruedas());
        case 20:
            Estructuras.cargarEstructura("eiel_Obra ejecutada");
            return LocalGISEIELUtils.getNameFromEstructura(((InstalacionesDeportivasEIEL) lstElementos.get(row)).getObra_ejec());
        case 21:
            return ((InstalacionesDeportivasEIEL) lstElementos.get(row)).getInst_P() != null ? ((InstalacionesDeportivasEIEL) lstElementos
                    .get(row)).getInst_P()
                    : "";
        default:
            return null;

        }
    }

    public InstalacionesDeportivasEIEL getValueAt(int row) {

        return (InstalacionesDeportivasEIEL) lstElementos.get(row);

    }

    /**
     * JTable uses this method to determine the default renderer/ editor for
     * each cell.
     */
    public Class getColumnClass(int c) {

        if (getValueAt(0, c) != null)
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
     * @param datos
     *            Datos a mostrar en el modelo
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
