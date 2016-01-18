package com.geopista.ui.plugin.georeference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.table.DefaultTableModel;

import com.geopista.ui.plugin.georeference.beans.PoliciaCoincidencias;
import com.vividsolutions.jump.I18N;

/**
 * @author rubengomez
 * Modelo de tabla callejero
 */
public class CallejeroTableModel extends DefaultTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String[] tableModelNames;
    public static final int ROW_TIPO=0;
    public static final int ROW_VIA=1;
    public static final int ROW_PORTAL=2;
    
    public CallejeroTableModel(){
    	Locale loc=I18N.getLocaleAsObject();      
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.georeference.language.Georreferenciacioni18n",loc,this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("Georreferenciacion",bundle);
    	tableModelNames = new String[] { I18N.get("Georreferenciacion","georeference.model.callejeroTableModel.nameType"), I18N.get("Georreferenciacion","georeference.model.callejeroTableModel.nameAddress"),I18N.get("Georreferenciacion","georeference.model.callejeroTableModel.namePoliceNumber")};
    
    }
	public void setModelData(ArrayList datos) {
		
	    try
	    {
	        if (datos==null)return;
	        Iterator recorreDatos = datos.iterator();
	        while (recorreDatos.hasNext()) {
	             Object obj=recorreDatos.next();
	             PoliciaCoincidencias valor =(PoliciaCoincidencias) obj;
	             Object row[]= new Object[] {valor,valor.getCalle(),new Integer(0)};
	             this.addRow(row);
	        } 
	        fireTableDataChanged();
	    }catch(Exception e)
	    {
	        e.printStackTrace();
	    }
	}
	public int getColumnCount() {
		return tableModelNames.length;
	}

	public String getColumnName(int c) {
		return tableModelNames[c];
	}
    public String setColumnName(int c, String sName) {
        return tableModelNames[c]=sName;
    }

	public boolean isCellEditable(int rowIndex, int columnIndex) {
        return (columnIndex==ROW_PORTAL);
	}
}
