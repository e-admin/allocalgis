/**
 * ContactosTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos.contactos;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.table.DefaultTableModel;

import com.geopista.protocol.metadatos.CI_ResponsibleParty;
import com.geopista.protocol.metadatos.ListaContactos;



/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 26-may-2004
 * Time: 18:14:48
 */
public class ContactosTableModel  extends DefaultTableModel {
    public static final int idIndex=0;
    public static final int idNombre=1;
    public static final int idEmpresa=2;
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ContactosTableModel.class);
    private static String[] contactosModelNames = new String[] { "ID","Nombre", "Organización"};

    public void setModelData(ListaContactos listaContactos) {
        try
        {
            Hashtable auxUsuarios= listaContactos.gethContactos();
            for (Enumeration e=auxUsuarios.elements();e.hasMoreElements();) {
			    CI_ResponsibleParty auxContacto =(CI_ResponsibleParty) e.nextElement();
			    Object row[] = new Object[] { auxContacto.getId(),auxContacto.getIndividualName(),auxContacto.getOrganisationName()};
			    this.addRow(row);
		    }

	    	fireTableDataChanged();


        }catch(Exception e)
        {
            logger.error("Error al poner la lista de contactos: "+ e.toString());
        }
	}



	public int getColumnCount() {
		return contactosModelNames.length;
	}

	public String getColumnName(int c) {
		return contactosModelNames[c];
	}
    public String setColumnName(int c, String sName) {
        return contactosModelNames[c]=sName;
    }

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

}
