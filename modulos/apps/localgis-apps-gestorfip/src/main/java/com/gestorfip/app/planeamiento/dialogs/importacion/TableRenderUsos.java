package com.gestorfip.app.planeamiento.dialogs.importacion;
/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 



/*
 * TableRenderDemo.java requires no other files.
 */

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.vividsolutions.jump.I18N;

import es.gestorfip.serviciosweb.ServicesStub;
import es.gestorfip.serviciosweb.ServicesStub.DeterminacionBean;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

/** 
 * TableRenderDemo is just like TableDemo, except that it
 * explicitly initializes column sizes and it uses a combo box
 * as an editor for the Sport column.
 */
public class TableRenderUsos extends JPanel {

    private ServicesStub.DeterminacionLayerBean[] lstDeterminaciones;
    private ArrayList<Integer> colEditable;
    private Object[][] data;
    
    public TableRenderUsos(ServicesStub.DeterminacionLayerBean[]lstDeterminaciones) {
        super(new GridLayout(1,0));
        this.lstDeterminaciones = lstDeterminaciones;
        initColEditable();
        
        MyTableModel tableModel = new MyTableModel();
        tableModel.initData();
        
        JTable table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Set up column sizes.
        initColumnSizes(table);

        //Fiddle with the Sport column's cell editors/renderers.
        setUpSportColumn(table, table.getColumnModel().getColumn(2));

        //Add the scroll pane to this panel.
        add(scrollPane);
        
        
    }
    
    public Object[][] getData() {
  		return data;
  	}

    /*
     * This method picks good column sizes.
     * If all column heads are wider than the column's cells'
     * contents, then you can just use column.sizeWidthToFit().
     */
    private void initColumnSizes(JTable table) {
        MyTableModel model = (MyTableModel)table.getModel();
        TableColumn column = null;
        Component comp = null;
        int headerWidth = 0;
        int cellWidth = 0;
        Object[] longValues = model.longValues;
        TableCellRenderer headerRenderer =
            table.getTableHeader().getDefaultRenderer();

        for (int i = 0; i < 3; i++) {
            column = table.getColumnModel().getColumn(i);

            comp = headerRenderer.getTableCellRendererComponent(
                                 null, column.getHeaderValue(),
                                 false, false, 0, 0);
            headerWidth = comp.getPreferredSize().width;

//            comp = table.getDefaultRenderer(model.getColumnClass(i)).
//                             getTableCellRendererComponent(
//                                 table, longValues[i],
//                                 false, false, 0, i);
            cellWidth = comp.getPreferredSize().width;
            
            column.setPreferredWidth(Math.max(headerWidth, cellWidth));
        }
    }

    public void setUpSportColumn(JTable table,
                                 TableColumn sportColumn) {
    	
    	final JTextField textField = new JTextField ();
    	sportColumn.setCellEditor(new DefaultCellEditor(textField));
    
        //Set up tool tips for the sport cells.
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
//        renderer.setToolTipText("Click for combo box");
        sportColumn.setCellRenderer(renderer);
    }

    class MyTableModel extends AbstractTableModel {
    	
    	String colUso = I18N.get("ImportacionFipLayer", "gestorFip.importacion.generatelayer.coluso");
    	String colAlias = I18N.get("ImportacionFipLayer", "gestorFip.importacion.generatelayer.colalias");
    	
    	private String[] columnNames = {"",
    			colUso,
    			colAlias};
    	
    	public final Object[] longValues = {Boolean.FALSE, "Kathy",
               "None of the above"};

	   public void initData(){
		   data = new Object[lstDeterminaciones.length][3];
		   int index = 0;
		   for (int i = 0; i < lstDeterminaciones.length; i++) {
			   if(lstDeterminaciones[i] != null){
				   ServicesStub.DeterminacionLayerBean det = lstDeterminaciones[i];
				   data[index][0] = new Boolean(true);
				   data[index][1] = det.getNombre();
				   data[index][2] = det.getNombre();
				   index ++;
			   }
		   }
	   }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (!colEditable.contains(col)) {
                return false;
            } else {
                return true;
            }
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }
        
    }
   
    private void initColEditable(){
     	colEditable =  new ArrayList<Integer>();
    	colEditable.add(0);
    	colEditable.add(2);
    }
}