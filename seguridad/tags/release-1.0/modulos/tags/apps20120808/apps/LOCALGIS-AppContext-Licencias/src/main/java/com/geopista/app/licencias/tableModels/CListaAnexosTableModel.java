package com.geopista.app.licencias.tableModels;

import com.geopista.app.licencias.utilidades.ComboBoxTableEditor;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 21-abr-2004
 * Time: 12:40:42
 * To change this template use File | Settings | File Templates.
 */
public class CListaAnexosTableModel extends DefaultTableModel {

        public static String[] columnNames = {"Fichero", "Tipo Anexo", "Descripción"};

        public CListaAnexosTableModel() {
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
            else return true;           
        }

}
