package com.geopista.app.contaminantes;

import com.geopista.protocol.contaminantes.Arbolado;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;
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
 * Date: 13-oct-2004
 * Time: 16:40:50
 */
public class ArboladoTableModel extends DefaultTableModel {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ArboladoTableModel.class);
	private static String[] tableModelNames = new String[]{"ID", "Observaciones", "Extension"};
    public static int idIndex=0;

	public void setModelData(Vector listaArbolado) {
		try {
			if (listaArbolado == null) return;
			for (Enumeration e = listaArbolado.elements(); e.hasMoreElements();) {
				Arbolado aux = (Arbolado) e.nextElement();
				Object row[] = new Object[]{aux.getId(), aux.getObs(), new Float(aux.getExt()).toString()};
				this.addRow(row);
			}
			fireTableDataChanged();
		} catch (Exception e) {
			logger.error("Error al poner la lista de Arbolado: " + e.toString());
		}
	}


	public int getColumnCount() {
		return tableModelNames.length;
	}

	public String getColumnName(int c) {
		return tableModelNames[c];
	}

	public String setColumnName(int c, String sName) {
		return tableModelNames[c] = sName;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

    public static void setColumnNames(String[] colNames){
        tableModelNames= colNames;
    }



}
