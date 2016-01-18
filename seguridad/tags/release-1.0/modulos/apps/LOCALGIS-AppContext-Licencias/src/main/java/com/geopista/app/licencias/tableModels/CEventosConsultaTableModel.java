package com.geopista.app.licencias.tableModels;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 21-abr-2004
 * Time: 12:40:42
 * To change this template use File | Settings | File Templates.
 */
public class CEventosConsultaTableModel extends DefaultTableModel {

        public static String[] columnNames = {"Num. Evento", "Fecha", "Revisado", "Revisado por", "Descripción"};

        public CEventosConsultaTableModel() {
            new DefaultTableModel(columnNames, 0);
        }

        public static void setColumnNames(String[] colNames) {
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
