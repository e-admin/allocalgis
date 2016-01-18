package com.geopista.app.centralizadorsso.localgisapp;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.centralizadorsso.beans.LocalGISApp.TipoApp;
import com.geopista.app.centralizadorsso.utils.ListaLocalGISApps;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPermission;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */


/**
*
* @author  dcaaveiro
*/
public class LocalGISAppTableModel extends DefaultTableModel {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(LocalGISAppTableModel.class);
    private static String[] appsModelNames = new String[] { "Aplicaciones Permitidas" };
        
    public void setModelData(ListaLocalGISApps listaLocalGISApp, TipoApp tipoApp) {
        try
        {
        	GeopistaAcl defaultAcl;
        	String permisoApp;
            for (Enumeration e=listaLocalGISApp.getLista().keys();e.hasMoreElements();) {
            	String keyName = (String) e.nextElement();               	
            	if(listaLocalGISApp.getLocalGISApp(keyName).getTipoApp().equals(tipoApp)){
            		defaultAcl =com.geopista.security.SecurityManager.getPerfil(listaLocalGISApp.getLocalGISApp(keyName).getNombreAcl());
            		permisoApp = listaLocalGISApp.getLocalGISApp(keyName).getPermisoApp();
            		if(defaultAcl.checkPermission(new GeopistaPermission(permisoApp))){
            			Object row[] = new Object[] {CLocalGISAppFrame.APP_TITLE_MARKER + keyName };
            			this.addRow(row);			    
            		}
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
