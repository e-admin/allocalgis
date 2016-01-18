package com.geopista.ui.plugin.georeference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.table.DefaultTableModel;

import com.geopista.ui.plugin.georeference.beans.Via;
import com.vividsolutions.jump.I18N;

/**
 * @author rubengomez
 * Modelo de tabla no georreferenciada
 */
public class NoGeoRefTableModel extends DefaultTableModel  {
	private static final long serialVersionUID = 1L;
	private static String[] tableModelNames = new String[] { "Nombre", "Direccion"};
    public static final int ROW_NOMBRE=0;
    public static final int ROW_DIRECCION=1;
    
    public NoGeoRefTableModel(){
    	Locale loc=I18N.getLocaleAsObject();      
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.georeference.language.Georreferenciacioni18n",loc,this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("Georreferenciacion",bundle);
    	tableModelNames = new String[] { I18N.get("Georreferenciacion","georeference.model.noGeorefTableModel.nameKey"), I18N.get("Georreferenciacion","georeference.model.noGeorefTableModel.nameAddress")};
    
    } 
    
    
	public void setModelData(ArrayList vias) {
		
	    try
	    {
	        if (vias==null)return;
	        Iterator recorreVias = vias.iterator();
	        while (recorreVias.hasNext()) {
	             Object obj=recorreVias.next();
	             Via via =(Via) obj;
	             Object row[]= new Object[] {via,via.getCalle()};
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
        return false;
	}

}    


