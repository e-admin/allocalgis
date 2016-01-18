package com.geopista.ui.plugin.routeenginetools.networkfusion.dialogs.tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import javax.swing.table.DefaultTableModel;
import com.geopista.ui.plugin.routeenginetools.networkfusion.beans.NetworkBean;
import com.vividsolutions.jump.I18N;


public class NetworkTableModel extends DefaultTableModel{
	private static final long serialVersionUID = 1L;
	private static String[] tableModelNames;
    public static final int ROW_NETWORK=0;
    public static final int ROW_STATUS=1;
    public static final int ROW_TYPE=2;
    
    public NetworkTableModel(){
    	
    	tableModelNames = new String[] { 
    			I18N.get("networkFusion","routeengine.networkfusion.networkName"), 
    			I18N.get("networkFusion","routeengine.networkfusion.status"),
    			I18N.get("networkFusion","routeengine.networkfusion.type")
    			};
    
    }
	public void setModelData(ArrayList<NetworkBean> datos) {
		Collections.sort(datos, new NetworkBeanComparator());
	    try
	    {
	        if (datos==null)return;
	        Iterator<NetworkBean> recorreDatos = datos.iterator();
	        while (recorreDatos.hasNext()) {
	             NetworkBean network=recorreDatos.next();
	             Object row[]= new Object[] {network,network.getRwType(),network.getgType()};
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
