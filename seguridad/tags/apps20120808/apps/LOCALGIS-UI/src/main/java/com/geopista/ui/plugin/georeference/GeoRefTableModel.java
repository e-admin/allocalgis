package com.geopista.ui.plugin.georeference;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.geopista.ui.plugin.georeference.beans.Via;
import com.vividsolutions.jump.I18N;

/**
 * @author rubengomez
 * Modelo de tabla georreferenciada
 */
public class GeoRefTableModel extends DefaultTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String[] tableModelNames;
    public static final int ROW_NOMBRE=0;
    public static final int ROW_VIA=1;
    public static final int ROW_REFERENCIA=2;

    public GeoRefTableModel(){
    	Locale loc=I18N.getLocaleAsObject();      
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.georeference.language.Georreferenciacioni18n",loc,this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("Georreferenciacion",bundle);
    	tableModelNames = new String[] { I18N.get("Georreferenciacion","georeference.model.georefTableModel.nameKey"), I18N.get("Georreferenciacion","georeference.model.georefTableModel.nameAddress"),I18N.get("Georreferenciacion","georeference.model.georefTableModel.nameReference")};
    }
	public void setModelData(ArrayList vias) {
		
	    try
	    {
	        if (vias==null)return;
	        Iterator recorreVias = vias.iterator();
	        while (recorreVias.hasNext()) {
	             Object obj=recorreVias.next();
	             Via via =(Via) obj;
	             Object row[]= new Object[] {via,via.getCalle(),new Integer(0)};
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
        return (columnIndex==ROW_REFERENCIA);
	}
	

}
