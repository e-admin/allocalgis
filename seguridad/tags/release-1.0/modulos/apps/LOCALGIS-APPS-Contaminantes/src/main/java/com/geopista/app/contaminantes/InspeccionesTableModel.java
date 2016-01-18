package com.geopista.app.contaminantes;

import com.geopista.protocol.contaminantes.Inspeccion;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import java.util.Enumeration;
import java.text.SimpleDateFormat;

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
 * Date: 21-oct-2004
 * Time: 17:55:10
 */
public class InspeccionesTableModel extends DefaultTableModel {

        private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(InspectoresTableModel.class);
        private static String[] tableModelNames = new String[] { "ID", "Num folios","Fecha Inicio","Fecha Fin",""};
        public static int idIndex=4;
        public void setModelData(Vector listaInspecciones) {
            try
            {
                if (listaInspecciones==null)return;
                for (Enumeration e=listaInspecciones.elements();e.hasMoreElements();) {
                    Inspeccion aux =(Inspeccion) e.nextElement();

                    SimpleDateFormat df=new SimpleDateFormat("dd-MM-yyyy");
                    String sFechaInicio="";
                    if (aux.getFinicio()!=null)
                      sFechaInicio=df.format(aux.getFinicio());
                    String sFechaFin="";
                    if (aux.getFfin()!=null)
                      sFechaFin=df.format(aux.getFfin());

                    Object row[] = new Object[] { aux.getId(),(aux.getNfolios()==null?"":aux.getNfolios().toString()),sFechaInicio,sFechaFin,aux};
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
         public static void setColumnNames(String[] colNames){
        tableModelNames= colNames;
         }

    }

