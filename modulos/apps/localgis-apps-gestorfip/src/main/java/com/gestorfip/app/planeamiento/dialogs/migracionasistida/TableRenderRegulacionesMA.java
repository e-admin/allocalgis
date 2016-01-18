package com.gestorfip.app.planeamiento.dialogs.migracionasistida;
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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.I18N;

import es.gestorfip.serviciosweb.ServicesStub.ConfLayerRegimenEspecificoBean;
import es.gestorfip.serviciosweb.ServicesStub.ConfLayerUsosRegulacionesBean;
import es.gestorfip.serviciosweb.ServicesStub.ConfLayerValorBean;
import es.gestorfip.serviciosweb.ServicesStub.ConfLayerValorReferenciaBean;
import es.gestorfip.serviciosweb.ServicesStub.ConfRegulacionesBean;

/** 
 * TableRenderDemo is just like TableDemo, except that it
 * explicitly initializes column sizes and it uses a combo box
 * as an editor for the Sport column.
 */
public class TableRenderRegulacionesMA extends JPanel {

    private ConfLayerUsosRegulacionesBean[] lstUsosRegulaciones;
	private ArrayList<Integer> colEditable;
    private Object[][] data;
    private String valueModif;
    private  MyTableModel tableModel ;
    public static final int POSCAMPOCODIGO = 0;
    public static final int POSCAMPOSEL = 1;
    public static final int POSCAMPOCODDETSEL = 2;
    public static final int POSCAMPODETSEL = 3;
    public static final int POSCAMPONOMBREREGULA = 4;
    public static final int POSCAMPOALIAS = 5;
    private AppContext application;
    private JTable table = null;
    
    
	public TableRenderRegulacionesMA(ConfLayerUsosRegulacionesBean[] lstUsosRegulaciones,
			AppContext application) {
        super(new GridLayout(1,0));
        initColEditable();
 
        this.lstUsosRegulaciones = lstUsosRegulaciones;
        this.application = application;
        tableModel = new MyTableModel();
        tableModel.initData();

        table= new JTable(){

            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    jc.setToolTipText(getValueAt(row, column).toString());
                }
                return c;
            }
        };
       
        table.setModel(tableModel);
        
        table.getModel().addTableModelListener(new SimpleTableListener());
        
          
      //  JTable table = new JTable( tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        // Instanciamos el TableRowSorter y lo añadimos al JTable
        TableRowSorter<TableModel> order = new TableRowSorter<TableModel>(tableModel);
        table.setRowSorter(order);
        
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Set up column sizes.
        initColumnSizes(table);

        //Fiddle with the Sport column's cell editors/renderers.

        setUpSportColumn(table, table.getColumnModel().getColumn(POSCAMPOALIAS));


        //Add the scroll pane to this panel.
        add(scrollPane);
        setAnchoColumnas(table);
        setInvisible(0, table);
        setInvisible(2, table);
    }
    
	 public void setAnchoColumnas(JTable table){        
    	int ancho = (int) Math.floor(table.getPreferredScrollableViewportSize().getWidth());
        int anchoColumna = 0; 
        TableColumnModel modeloColumna = table.getColumnModel(); 
        TableColumn columnaTabla; 
        for (int i = 0; i < table.getColumnCount(); i++) { 
            columnaTabla = modeloColumna.getColumn(i); 
            switch(i){ 
                case 1: 
                		anchoColumna = (2*ancho)/100; 
                        break; 
                case 2: anchoColumna = (49*ancho)/100; 
                        break; 
                case 3: anchoColumna = (49*ancho)/100; 
                        break; 
            }                      
            columnaTabla.setPreferredWidth(anchoColumna);            
        } 
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
        int headerWidth = 10;
        int cellWidth = 10;
        Object[] longValues = model.longValues;
        TableCellRenderer headerRenderer =
            table.getTableHeader().getDefaultRenderer();

        for (int i = 0; i < longValues.length; i++) {
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
    	textField.addKeyListener(new KeyAdapter() {
    		
            public void keyReleased(KeyEvent evt) {
            }

            public void keyTyped(KeyEvent evt) {
            }

            public void keyPressed(KeyEvent evt) {
//            	 int k = (int) evt.getKeyChar();//k = al valor de la tecla presionada    
//                 if ((k <= 96 || k >= 123) && k != 8 && k != 9 && k != 10 && k != 127 && k != 65535 ) {
//                     evt.setKeyChar((char) KeyEvent.VK_CLEAR);//Limpiar el caracter ingresado
//
//                     JOptionPane.showMessageDialog(null, I18N.get("ImportacionFipLayer", "gestorFip.importacion.generatelayer.validate.caracter.novalido"), 
//                    		 I18N.get("ImportacionFipLayer", "gestorFip.importacion.generatelayer.validate.data"),
//                             JOptionPane.ERROR_MESSAGE);
//
//                }
//                
//                 if(k == 9){
//             		// validar
//                	 
//                	 Pattern p = Pattern.compile("[a-z]*");
//                	 String value = textField.getText();
//                	 Matcher m = p.matcher(value);
//                	 boolean correcto = m.matches();
//                    if(!correcto){
//                    	  JOptionPane.showMessageDialog(null, I18N.get("ImportacionFipLayer", "gestorFip.importacion.generatelayer.validate.caracter.novalido"), 
//                         		 I18N.get("ImportacionFipLayer", "gestorFip.importacion.generatelayer.validate.data"),
//                                  JOptionPane.ERROR_MESSAGE);
//                    }
//                   
//             	 }
            }
        });

        //Set up tool tips for the sport cells.
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
//        renderer.setToolTipText("Click for combo box");
        sportColumn.setCellRenderer(renderer);
    }

    public class MyTableModel extends AbstractTableModel {
    	

    	String colAlias = I18N.get("MigracionAsistidaLayers", "gestorFip.migracionasistida.colalias");
    	String colRegula = I18N.get("MigracionAsistidaLayers", "gestorFip.migracionasistida.colregula");
    	String colDetAplic = I18N.get("MigracionAsistidaLayers", "gestorFip.migracionasistida.detselected");
    	
     	
    	// posicion 0- codigo determinacion
    	// posicion 1- Campo de seleccionar
    	// posicion 2- codigo determinación uso aplicado
    	// posicion 3- nombre determinación uso aplicado
    	// posicion 4- nombre regulacion 
    	// posicion 5- alias regulacion

		private String[] columnNames = {"", "", "",	colDetAplic, colRegula, colAlias};    
		   
		public final Object[] longValues = {"", Boolean.FALSE , "", "", "", ""};
	   
	   public void initData(){
		   ArrayList<String> lstRegulacionesAnadidas  = new ArrayList<String>();
		   int numValores = 0;
		   for (int i = 0; i < lstUsosRegulaciones.length; i++) {
			   if(lstUsosRegulaciones[i].getSelected()){
				   ConfRegulacionesBean[] lstRegulaciones = (ConfRegulacionesBean[])lstUsosRegulaciones[i].getLstRegulaciones();
				   if(lstRegulaciones != null  && lstRegulaciones[0] != null){  
					   for (int j = 0; j < lstRegulaciones.length; j++) {
						   if(!lstRegulacionesAnadidas.contains(lstRegulaciones[j].getRegulacionValor().getCodigo())){
							   lstRegulacionesAnadidas.add(lstRegulaciones[j].getRegulacionValor().getCodigo());
							   numValores ++;
						   }
					   }
				   }
			   }
		   }
		   
		   lstRegulacionesAnadidas = new ArrayList<String>();
		   data = new Object[numValores][longValues.length];
		 
		   int index = 0;
		   for (int i = 0; i < lstUsosRegulaciones.length; i++) {
			   if(lstUsosRegulaciones[i] != null){
				   if(lstUsosRegulaciones[i].getSelected()){
					   ConfRegulacionesBean[] lstRegulaciones = (ConfRegulacionesBean[])lstUsosRegulaciones[i].getLstRegulaciones();
					   if(lstRegulaciones != null  && lstRegulaciones[0] != null){  
						   for (int j = 0; j < lstRegulaciones.length; j++) {
							   if(lstRegulaciones[j].getRegulacionValor() != null && lstRegulaciones[j].getRegulacionValor().getCodigo() != null){
								   if(!lstRegulacionesAnadidas.contains(lstRegulaciones[j].getRegulacionValor().getCodigo())){
									   data[index][POSCAMPOCODIGO] = lstRegulaciones[j].getRegulacionValor().getCodigo();
									   data[index][POSCAMPOSEL] = new Boolean(lstRegulaciones[j].getSelected());
									   data[index][POSCAMPOCODDETSEL] = lstUsosRegulaciones[i].getDeterminacionUso().getCodigo();
									   if(lstRegulaciones[j].getRegulacion() != null && lstRegulaciones[j].getRegulacion().getNombre() != null &&
											   !lstRegulaciones[j].getRegulacion().getNombre().equals("null")){
										   data[index][POSCAMPODETSEL] = lstRegulaciones[j].getRegulacion().getNombre();
									   }
									   else{
										   data[index][POSCAMPODETSEL] = "";
									   }
									   data[index][POSCAMPONOMBREREGULA] = lstRegulaciones[j].getRegulacionValor().getNombre();
									   data[index][POSCAMPOALIAS] = lstRegulaciones[j].getAlias();
									   lstRegulacionesAnadidas.add(lstRegulaciones[j].getRegulacionValor().getCodigo());
									   index++;
								   }
							   }
						   }
					   }
				   }
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
    	colEditable.add(POSCAMPOSEL);
    	colEditable.add(POSCAMPOALIAS);
    }
    
    public Object[][] getData() {
  		return data;
  	}
    
    
    private void setInvisible(int column, JTable jTable){
        /** columna hidden no visible */
        TableColumn col= jTable.getColumnModel().getColumn(column);
        col.setResizable(false);
        col.setWidth(0);
        col.setMaxWidth(0);
        col.setMinWidth(0);
        col.setPreferredWidth(0);
    }
    

    public MyTableModel getTableModel() {
		return tableModel;
	}


	public void setTableModel(MyTableModel tableModel) {
		this.tableModel = tableModel;
	}


	public ConfLayerUsosRegulacionesBean[] getLstUsosRegulaciones() {
		return lstUsosRegulaciones;
	}


	public void setLstUsosRegulaciones(
			ConfLayerUsosRegulacionesBean[] lstUsosRegulaciones) {
		this.lstUsosRegulaciones = lstUsosRegulaciones;
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}
	
	public class SimpleTableListener  implements TableModelListener {

        public SimpleTableListener() {

            table.getModel().addTableModelListener(this);

        }

		
		public void tableChanged(TableModelEvent e) {
			TableModel model = (TableModel)e.getSource();
			int row = e.getFirstRow();
			boolean checked =  (Boolean) model.getValueAt(row, POSCAMPOSEL);
			String codigo = (String) model.getValueAt(row, POSCAMPOCODIGO);
			String alias = (String) model.getValueAt(row, POSCAMPOALIAS);	
			
			if(alias.length() > 50 && checked){
				JOptionPane.showMessageDialog(application.getMainFrame(), 
						I18N.get("MigracionAsistidaLayers", "gestorFip.migracionasistida.validate.limitecaracter"));
				
				model.setValueAt(alias.substring(0, 50), row, POSCAMPOALIAS);	
			}
			else{
				for (int i = 0; i < lstUsosRegulaciones.length; i++) {
					if(lstUsosRegulaciones[i] != null){
						if(lstUsosRegulaciones[i].getSelected()){
	
							ConfRegulacionesBean[] lstRegulaciones = (ConfRegulacionesBean[])lstUsosRegulaciones[i].getLstRegulaciones();
							if(lstRegulaciones!= null && lstRegulaciones[0] != null){
								for (int j = 0; j < lstRegulaciones.length; j++) {
								   
									if(codigo.equals(lstRegulaciones[j].getRegulacionValor().getCodigo())){
										lstRegulaciones[j].setSelected(checked);
										lstRegulaciones[j].setAlias(alias);
									}
								}
							}
					   }
					}
				}
			}
		}
    }

}