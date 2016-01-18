/**
 * LocalGISAppTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.centralizadorsso.localgisapp;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.centralizadorsso.beans.LocalGISApp;


/**
*
* @author  dcaaveiro
*/
public class LocalGISAppTableModel extends DefaultTableModel {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(LocalGISAppTableModel.class);
    private static String[] appsModelNames = new String[] { "Aplicaciones Permitidas" };
        
    public void setModelData(HashMap<String,LocalGISApp> listaLocalGISApp, String tipoApp, Locale locale) {
        try
        {
        	Iterator<String> itKeys = listaLocalGISApp.keySet().iterator();
            while (itKeys.hasNext()) {
            	String keyName = (String) itKeys.next();
            	LocalGISApp localgisApp = listaLocalGISApp.get(keyName);
            	if(localgisApp.getAppType().equals(tipoApp) && localgisApp.getActive()){            	
	            	Object row[] = new Object[] {CLocalGISAppFrame.APP_TITLE_MARKER + localgisApp.getDictionary().get(locale.toString().toLowerCase())};
	            	this.addRow(row);			    
            	}
		    }

	    	fireTableDataChanged();


        }catch(Exception e)
        {        	
            logger.error("Error al poner la lista de roles: "+ e.toString());
        }
	}
    
	public int getColumnCount() {
		return appsModelNames.length;
	}

	public String getColumnName(int c) {
		return appsModelNames[c];
	}
    public String setColumnName(int c, String sName) {
        return appsModelNames[c]=sName;
    }

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

}
