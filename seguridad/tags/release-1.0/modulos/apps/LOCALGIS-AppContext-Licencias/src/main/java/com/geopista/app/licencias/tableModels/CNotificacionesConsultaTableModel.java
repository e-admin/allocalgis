package com.geopista.app.licencias.tableModels;

import javax.swing.table.DefaultTableModel;

public class CNotificacionesConsultaTableModel extends DefaultTableModel {

        public static String[] columnNames = {"Estado", "Fecha Vencimiento", "DNI Interesado", "Fecha Notificación", "Fecha Reenvío"};

        public CNotificacionesConsultaTableModel() {
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
