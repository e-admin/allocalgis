package com.geopista.app.licencias.tableModels;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.*;
import java.awt.*;

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
