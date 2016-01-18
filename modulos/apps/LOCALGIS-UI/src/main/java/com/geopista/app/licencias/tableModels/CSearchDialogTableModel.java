/**
 * CSearchDialogTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.licencias.tableModels;

import java.awt.Color;

import javax.swing.table.DefaultTableModel;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 19-may-2005
 * Time: 15:32:30
 * To change this template use File | Settings | File Templates.
 */
public class CSearchDialogTableModel  extends DefaultTableModel{

        public String[] columnNames= {""};

        public boolean bloqueado= false;
        private Color curColor;


        public CSearchDialogTableModel(String[] colNames) {
            columnNames= colNames;
            new DefaultTableModel(columnNames, 0);
        }

        public void setColumnNames(String[] colNames) {
            columnNames= colNames;
        }


        public int getColumnCount() {
            return columnNames.length;
        }


        public String getColumnName(int col) {
            return columnNames[col];
        }

        public boolean isCellEditable(int row, int col) {
            return false;
        }


}
