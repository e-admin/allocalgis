package com.geopista.app.administrador.usuarios;

import com.geopista.protocol.administrador.ListaUsuarios;
import com.geopista.protocol.administrador.Usuario;

import javax.swing.table.DefaultTableModel;
import java.util.Hashtable;
import java.util.Enumeration;


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
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 26-may-2004
 * Time: 18:14:48
 */
public class UsuariosTableModel  extends DefaultTableModel {
    public static final int idIndex=0;
    public static final int idNombre=1;

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(UsuariosTableModel.class);
    private static String[] rolesModelNames = new String[] { "ID","Nombre", "Descripcion"};

   /* public ListaRoles getModelListaRoles() {
        return modelListaRoles;
    }*/

    public void setModelData(ListaUsuarios listaUsuarios) {
        try
        {
          //   modelListaRoles=listaRoles;
            Hashtable auxUsuarios= listaUsuarios.gethUsuarios();
            for (Enumeration e=auxUsuarios.elements();e.hasMoreElements();) {
			    Usuario auxUsuario =(Usuario) e.nextElement();
			    Object row[] = new Object[] { auxUsuario.getId(),auxUsuario.getName(),auxUsuario.getDescripcion()};
			    this.addRow(row);
		    }

	    	fireTableDataChanged();


        }catch(Exception e)
        {
            logger.error("Error al poner la lista de usuarios: "+ e.toString());
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
