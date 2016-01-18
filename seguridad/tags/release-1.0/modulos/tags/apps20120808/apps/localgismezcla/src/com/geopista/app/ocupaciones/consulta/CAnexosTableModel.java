package com.geopista.app.ocupaciones.consulta;

import javax.swing.table.DefaultTableModel;

public class CAnexosTableModel extends DefaultTableModel {

        public static String[] columnNames = {"Url Fichero", "Observación"};

        public CAnexosTableModel() {
            new DefaultTableModel(columnNames, 0);
        }

        public static void setColumnNames(String[] colNames){
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
