package com.geopista.app.inventario;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 07-jul-2006
 * Time: 10:11:55
 * To change this template use File | Settings | File Templates.
 */

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.protocol.inventario.HistoricoAmortizacionesBean;
import com.geopista.protocol.inventario.InventarioEIELBean;
import com.geopista.protocol.inventario.ListaEIEL;
import com.geopista.protocol.inventario.ListaHistoricoAmortizaciones;
import com.vividsolutions.jump.I18N;

/**
 * Clase que implementa el modelo de datos de la tabla del panel BienesJPanel
 */

public class HistoricoAmortizacionBienTableModel  extends DefaultTableModel {
    private String locale;
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(HistoricoAmortizacionBienTableModel.class);
    private static String[] historicoAmortizaModelNames = new String[] {
			    I18N.get("LocalGISInventario","inventario.historicoamortizacion.tabla.columna.anio"),
			    I18N.get("LocalGISInventario","inventario.historicoamortizacion.tabla.columna.base"),
			    I18N.get("LocalGISInventario","inventario.historicoamortizacion.tabla.columna.cuentaAmortizacion"),
			    I18N.get("LocalGISInventario","inventario.historico.bien.amortizacion.tabla.columna.valorAmorAnio"),
			    I18N.get("LocalGISInventario","inventario.historico.bien.amortizacion.tabla.columna.valorPendientePorAnio"),
			    I18N.get("LocalGISInventario","inventario.historico.bien.amortizacion.tabla.columna.valorAmorPorc"),
			    I18N.get("LocalGISInventario","inventario.historico.bien.amortizacion.tabla.columna.valorPendientePorPorcentaje")};

   /* public ListaRoles getModelListaRoles() {
        return modelListaRoles;
    }*/


	public HistoricoAmortizacionBienTableModel(String locale) {
		this.locale = locale;
	}
	
    public void setModelData(ListaHistoricoAmortizaciones listaHA) {
        try
        {
          //   modelListaRoles=listaRoles;
            Hashtable auxListaHA= listaHA.getHistoricoAmortizaciones();
            for (Enumeration e=auxListaHA.elements();e.hasMoreElements();) {
            	HistoricoAmortizacionesBean auxHA =(HistoricoAmortizacionesBean) e.nextElement();	
            	DecimalFormat formateador8= new DecimalFormat("########.##");	
        		DecimalFormatSymbols dfs = formateador8.getDecimalFormatSymbols();
        		dfs.setDecimalSeparator('.');	
        		formateador8.setDecimalFormatSymbols(dfs);
        		
			    
			    Object row[] = new Object[] {auxHA.getAnio(),auxHA.getCoste(),
			    							auxHA.getCuentaAmortizacion().getCuenta()+" - "+auxHA.getCuentaAmortizacion().getDescripcion(),
			    							auxHA.getValorAmorAnio(),
			    							formateador8.format(auxHA.getCoste()-auxHA.getValorAmorAnio()),
			    							auxHA.getValorAmorPorc(),
			    							formateador8.format(auxHA.getCoste()-auxHA.getValorAmorPorc())};
			    this.addRow(row);
		    }

	    	fireTableDataChanged();


        }catch(Exception e)
        {
            logger.error("Error al poner la lista de elementos Historicos Amortizables: "+ e.toString());
        }
	}

	public int getColumnCount() {
		return historicoAmortizaModelNames.length;
	}

	public String getColumnName(int c) {
		return historicoAmortizaModelNames[c];
	}
    public String setColumnName(int c, String sName) {
        return historicoAmortizaModelNames[c]=sName;
    }

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

}
