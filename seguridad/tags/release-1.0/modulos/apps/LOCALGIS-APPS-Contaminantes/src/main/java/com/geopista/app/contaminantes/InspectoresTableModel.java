package com.geopista.app.contaminantes;

import com.geopista.protocol.contaminantes.Inspector;
import com.geopista.app.contaminantes.CMainContaminantes;

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
 * Date: 14-oct-2004
 * Time: 15:32:47
 */
public class InspectoresTableModel extends DefaultTableModel {

        private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(InspectoresTableModel.class);
        private static String[] tableModelNames = new String[] {
            CMainContaminantes.messages.getString("InspectoresTableModel.ID"),
            CMainContaminantes.messages.getString("InspectoresTableModel.Apellidos"),
            CMainContaminantes.messages.getString("InspectoresTableModel.Nombre"),
            CMainContaminantes.messages.getString("InspectoresTableModel.Empresa")};
        public static int idIndex=0;
        public void setModelData(Vector listaInspectores) {
            try
            {
                if (listaInspectores==null)return;
                for (Enumeration e=listaInspectores.elements();e.hasMoreElements();) {
                    Inspector aux =(Inspector) e.nextElement();
                    Object row[] = new Object[] { aux.getId(), aux.getApellido1()+" "+aux.getApellido2(),aux.getNombre(),aux.getEmpresa()};
                    this.addRow(row);
                }
                fireTableDataChanged();
            }catch(Exception e)
            {
                logger.error("Error al poner la lista de Inpectores: "+ e.toString());
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
