package com.geopista.app.acteconomicas;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Iterator;

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
 * Date: 14-mar-2005
 * Time: 20:09:00
 */
public class ActEcoTableModel extends DefaultTableModel {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ActEcoTableModel.class);
    private static String[] tableModelNames = new String[] { "TIPO", "VIA", "PORTAL"};
    public static final int ROW_TIPO_VIA=0;
    public static final int ROW_VIA=1;
    public static final int ROW_NUMERO_POLICIA=2;
    public void setModelData(Vector vias) {
        try
        {
            if (vias==null)return;
            for (Enumeration e=vias.elements();e.hasMoreElements();) {
                 Object obj=e.nextElement();
                 if (obj instanceof Via)
                 {
                    Via via=(Via)obj;
                    Object row[]= new Object[] {via.getTipoviaine(), via,new Integer(0)};
                    this.addRow(row);
                 }
                if (obj instanceof DatosImportarActividades)
                {
                   DatosImportarActividades datos=(DatosImportarActividades)obj;
                   Object row[]= new Object[] {datos.getTipoviaine(), datos,
                                               datos.getRotulo()};
                   this.addRow(row);
                }

	        }
            fireTableDataChanged();
        }catch(Exception e)
        {
            logger.error("Error al poner la lista de permisos: "+ e.toString());
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
        return (columnIndex==ROW_NUMERO_POLICIA);
	}





}

