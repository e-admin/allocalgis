package com.geopista.app.licencias.tableModels;

import javax.swing.table.DefaultTableModel;

public class CReferenciasCatastralesTableModel extends DefaultTableModel {

        public static String[] columnNames = {"Referencia catastral","Tipo vía","Vía pública","nº"};


        public CReferenciasCatastralesTableModel() {
            new DefaultTableModel(columnNames, 0);
        }

        public CReferenciasCatastralesTableModel(String[] colNames) {
            columnNames= colNames;
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
            if (col == 0) return false;
            return true;
        }
}
