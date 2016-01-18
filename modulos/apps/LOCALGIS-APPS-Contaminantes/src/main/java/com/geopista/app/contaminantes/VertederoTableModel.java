/**
 * VertederoTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.contaminantes;

import com.geopista.protocol.contaminantes.Vertedero;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import java.util.Enumeration;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 18-oct-2004
 * Time: 17:12:08
 */
public class VertederoTableModel  extends DefaultTableModel {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(VertederoTableModel.class);
    private static String[] tableModelNames = new String[] { "ID", "TIPO", "TITULARIDAD"};
    public static int idIndex=0;
    public void setModelData(Vector listaVertederos) {
        try
        {
            if (listaVertederos==null)return;
            for (Enumeration e=listaVertederos.elements();e.hasMoreElements();) {
			    Vertedero aux =(Vertedero) e.nextElement();
                Object row[] = new Object[] { aux.getId(), (aux.getTipo()==null?"":aux.getTipo()),(aux.getTitularidad()==null?"":aux.getTitularidad())};
			    this.addRow(row);
	        }
            fireTableDataChanged();
        }catch(Exception e)
        {
            logger.error("Error al poner la lista de Vertederos: "+ e.toString());
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

    public static void setColumnNames(String[] colNames){
        tableModelNames= colNames;
    }



}
