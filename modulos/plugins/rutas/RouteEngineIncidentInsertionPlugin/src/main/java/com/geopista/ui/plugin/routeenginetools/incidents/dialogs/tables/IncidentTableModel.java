/**
 * IncidentTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.incidents.dialogs.tables;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.table.DefaultTableModel;

import com.localgis.route.graph.structure.basic.LocalGISIncident;


public class IncidentTableModel extends DefaultTableModel{
	private static final long serialVersionUID = 1L;
	private static String[] tableModelNames;
    public static final int ROW_INCIDENT_TYPE=0;
    public static final int ROW_INCIDENT_DESCRIPTION=1;
    public static final int ROW_DATE_START=2;
    public static final int ROW_DATE_END=3;
    
    public IncidentTableModel(){
    	/*Locale loc=I18N.getLocaleAsObject();      
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.georeference.language.Georreferenciacioni18n",loc,this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("Georreferenciacion",bundle);*/
    	tableModelNames = new String[] { 
    			/*I18N.get("networkFusion","routeengine.networkfusion.networkName")*/"Tipo", 
    			/*I18N.get("networkFusion","routeengine.networkfusion.status")*/"Descripcion",
    			/*I18N.get("networkFusion","routeengine.networkfusion.type")*/"Fecha inicio",
    			"Fecha fin"
    			};
    
    }
	public void setModelData(ArrayList<LocalGISIncident> datos) {
		//Collections.sort(datos, new LocalGISIncidentComparator());
	    try
	    {
	        if (datos==null)return;
	        Iterator<LocalGISIncident> recorreDatos = datos.iterator();
	        while (recorreDatos.hasNext()) {
	        	LocalGISIncident incident=recorreDatos.next();
	             Object row[]= new Object[] {incident,incident.getDescription(),incident.getDateStart(),incident.getDateEnd()};
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
