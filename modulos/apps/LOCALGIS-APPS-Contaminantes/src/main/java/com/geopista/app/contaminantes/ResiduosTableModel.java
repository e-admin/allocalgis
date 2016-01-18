/**
 * ResiduosTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.contaminantes;

import com.geopista.protocol.contaminantes.Residuo;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import java.util.Enumeration;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 19-oct-2004
 * Time: 10:38:59
 */
public class ResiduosTableModel  extends DefaultTableModel {

        private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ResiduosTableModel.class);
        private static String[] tableModelNames = new String[] { "ID", "TIPO", "SITUACION",""};
        public static int idIndex=3;
        public void setModelData(Vector listaResiduos) {
            try
            {
                if (listaResiduos==null)return;
                for (Enumeration e=listaResiduos.elements();e.hasMoreElements();) {
                    Residuo aux =(Residuo) e.nextElement();
                    Object row[] = new Object[] { aux.getId(),(aux.getTipo()==null?"":aux.getTipo()),(aux.getSituacion()==null?"":aux.getSituacion()),aux};
                    this.addRow(row);
                }
                fireTableDataChanged();
            }catch(Exception e)
            {
                logger.error("Error al poner la lista de Residuos: "+ e.toString());
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


