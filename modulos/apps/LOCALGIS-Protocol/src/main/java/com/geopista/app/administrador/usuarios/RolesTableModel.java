/**
 * RolesTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.administrador.usuarios;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.table.DefaultTableModel;

import com.geopista.protocol.administrador.ListaRoles;
import com.geopista.protocol.administrador.Rol;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 26-may-2004
 * Time: 18:14:48
 */
public class RolesTableModel  extends DefaultTableModel {
    public static final int idIndex=0;
    public static final int idNombre=1;

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(RolesTableModel.class);
    private static String[] rolesModelNames = new String[] { "ID","Nombre", "Descripcion"};

   /* public ListaRoles getModelListaRoles() {
        return modelListaRoles;
    }*/

    public void setModelData(ListaRoles listaRoles) {
    	setModelData(listaRoles, "");
	}

    public void setModelData(ListaRoles listaRoles, String filtro) {
        try
        {
          //   modelListaRoles=listaRoles;
             Hashtable auxRoles= listaRoles.gethRoles();
            for (Enumeration e=auxRoles.elements();e.hasMoreElements();) {
			    Rol auxRol =(Rol) e.nextElement();
			    if(filtro.equals("") || auxRol.getNombre().toUpperCase().contains(filtro.toUpperCase())){
				    Object row[] = new Object[] { auxRol.getId(),auxRol.getNombre(),auxRol.getDescripcion()};
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
		return rolesModelNames.length;
	}

	public String getColumnName(int c) {
		return rolesModelNames[c];
	}
    public String setColumnName(int c, String sName) {
        return rolesModelNames[c]=sName;
    }

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}





}
