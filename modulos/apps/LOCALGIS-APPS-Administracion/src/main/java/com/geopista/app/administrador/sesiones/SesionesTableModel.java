/**
 * SesionesTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.administrador.sesiones;

import java.text.SimpleDateFormat;
import java.util.Enumeration;

import javax.swing.table.DefaultTableModel;

import com.geopista.protocol.control.ListaSesionesSimple;
import com.geopista.protocol.control.SesionSimple;
import com.geopista.security.SecurityManager;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 16-jun-2004
 * Time: 17:49:16
 */
public class SesionesTableModel extends DefaultTableModel {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SesionesTableModel.class);
    private static String[] sesionesModelNames = new String[] { "ID_SESION","USUARIO","ID_APP", "CONEXION"};
	private boolean checkPermission;

   /* public ListaRoles getModelListaRoles() {
        return modelListaRoles;
    }*/

    public SesionesTableModel(boolean checkPermission) {
		this.checkPermission=checkPermission;
	}



	public void setModelData(ListaSesionesSimple listaSesiones) {
        try
        {

        	
            for (Enumeration e=listaSesiones.getLista().elements();e.hasMoreElements();) {
            	SesionSimple auxSesion =(SesionSimple) e.nextElement();
            	if (checkPermission){
            		SimpleDateFormat formatter = new SimpleDateFormat ("dd-MM-yyyy HH:mm:ss");
	                String fechaConexion = formatter.format(auxSesion.getFechaConexion());
	                
	                Object row[] = new Object[] { auxSesion.getIdSesion(), auxSesion.getsName(),auxSesion.getIdApp(),fechaConexion};
				    this.addRow(row);
            	}
            	else{
            		if (SecurityManager.getIdSesion().equals(auxSesion.getIdSesion())){				    
		                SimpleDateFormat formatter = new SimpleDateFormat ("dd-MM-yyyy HH:mm:ss");
		                String fechaConexion = formatter.format(auxSesion.getFechaConexion());
		                
		                Object row[] = new Object[] { auxSesion.getIdSesion(), auxSesion.getsName(),auxSesion.getIdApp(),fechaConexion};
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
		return sesionesModelNames.length;
	}

	public String getColumnName(int c) {
		return sesionesModelNames[c];
	}
    public String setColumnName(int c, String sName) {
        return sesionesModelNames[c]=sName;
    }

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}





}
