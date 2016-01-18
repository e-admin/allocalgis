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
import javax.swing.JComponent;
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
import com.vividsolutions.jump.I18N;
import es.gestorfip.serviciosweb.ServicesStub.ConfLayerDeterminacionAplicadaBean;
import es.gestorfip.serviciosweb.ServicesStub.ConfLayerRegimenEspecificoBean;
import es.gestorfip.serviciosweb.ServicesStub.ConfLayerValorBean;
import es.gestorfip.serviciosweb.ServicesStub.ConfLayerValorReferenciaBean;

/** 
 * TableRenderDemo is just like TableDemo, except that it
 * explicitly initializes column sizes and it uses a combo box
 * as an editor for the Sport column.
 */
public class TableRenderValoresMA extends JPanel {

    private ConfLayerDeterminacionAplicadaBean[] lstDeterminacionesAplic;
    private ArrayList<Integer> colEditable;
    private Object[][] data;
    private String valueModif;
    private  MyTableModel tableModel ;
    public static final int POSCAMPOID = 0;
    public static final int POSCAMPOSEL = 1;
    public static final int POSCAMPOCODDETSEL = 2;
    public static final int POSCAMPODETSEL = 3;
    public static final int POSCAMPOVALOR = 4;
    public static final int POSCAMPOALIAS = 5;
    public static final int POSCAMPOTIPOVALOR = 6; // indica si el valor es de valor, valor referencia o regimen especifico
    											// 1- valor, 2 valor referencia, 3- regimen especifico
    public static String TYPE_VALOR = "1";
    public static String TYPE_VALOR_REFERENCIA = "2";
    public static String TYPE_REGIMEN_ESPECIFICO = "3";
    private JTable table = null;

	public TableRenderValoresMA(ConfLayerDeterminacionAplicadaBean[] lstDeterminacionesAplic) {
        super(new GridLayout(1,0));
        initColEditable();
 
        this.lstDeterminacionesAplic = lstDeterminacionesAplic;
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

//        JTable table = new JTable( tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        
        // Instanciamos el TableRowSorter y lo añadimos al JTable
//        TableRowSorter<TableModel> order = new TableRowSorter<TableModel>(tableModel);
//        table.setRowSorter(order);

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
        setInvisible(6, table);
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
    	
    	String colValor = I18N.get("MigracionAsistidaLayers", "gestorFip.migracionasistida.colvalor");
    	String colAlias = I18N.get("MigracionAsistidaLayers", "gestorFip.migracionasistida.colalias");
    	String colDetAplic = I18N.get("MigracionAsistidaLayers", "gestorFip.migracionasistida.detselected");
    	
    	// posicion 0- identificador
    	// posicion 1- Campo de selecciona
    	// posicion 2- codigo determinación aplicada
    	// posicion 3- nombre determinación aplicada
    	// posicion 4- valor
    	// posicion 5- alias valor
    	// posicion 6- tipo de valor (1=valor, 2=valor referencia, 3=regimen especifico)
    	private String[] columnNames = {"", "", "",	colDetAplic, colValor, colAlias, ""};    
	   
	   public final Object[] longValues = {"", Boolean.FALSE , "", "", "", "", ""};

	   
	   public void initData(){
		   int numValores = 0;
		   for (int i = 0; i < lstDeterminacionesAplic.length; i++) {
			   if(lstDeterminacionesAplic[i].getSelected()){
				  ConfLayerValorBean[] lstValores = (ConfLayerValorBean[])lstDeterminacionesAplic[i].getLstValores();
				  ConfLayerValorReferenciaBean[] lstValoresRef = (ConfLayerValorReferenciaBean[])lstDeterminacionesAplic[i].getLstValoresReferencia();
				  ConfLayerRegimenEspecificoBean[] lstRegimenesEsp =(ConfLayerRegimenEspecificoBean[])lstDeterminacionesAplic[i].getLstRegimenesEspecificos();
				  if(lstValores!= null && lstValores[0] != null){
					  for (int j = 0; j < lstValores.length; j++) {
						  numValores ++;
					  }
				  }
				  if(lstValoresRef!= null && lstValoresRef[0] != null){
					  for (int j = 0; j < lstValoresRef.length; j++) {
						  numValores ++;
					  }
				  }

				  if(lstRegimenesEsp!= null && lstRegimenesEsp[0] != null){
					  for (int j = 0; j < lstRegimenesEsp.length; j++) {
						  numValores ++;
					  }
				  }
			   }
		   }
		   data = new Object[numValores][longValues.length];
		   int index = 0;
		   for (int i = 0; i < lstDeterminacionesAplic.length; i++) {
			   if(lstDeterminacionesAplic[i] != null){
				   if(lstDeterminacionesAplic[i].getSelected()){
					   ConfLayerValorBean[] lstValores = (ConfLayerValorBean[])lstDeterminacionesAplic[i].getLstValores();
					   ConfLayerValorReferenciaBean[] lstValoresRef = (ConfLayerValorReferenciaBean[])lstDeterminacionesAplic[i].getLstValoresReferencia();
						  ConfLayerRegimenEspecificoBean[] lstRegimenesEsp =(ConfLayerRegimenEspecificoBean[])lstDeterminacionesAplic[i].getLstRegimenesEspecificos();
					   if(lstValores!= null && lstValores[0] != null){
						   for (int j = 0; j < lstValores.length; j++) {
							   data[index][POSCAMPOID] = String.valueOf(lstValores[j].getId());
							   data[index][POSCAMPOSEL] = new Boolean(lstValores[j].getSelected());
							   data[index][POSCAMPOCODDETSEL] = lstDeterminacionesAplic[i].getDeterminacionLayer().getCodigo();
							   data[index][POSCAMPODETSEL] = lstDeterminacionesAplic[i].getDeterminacionLayer().getNombre();
							   data[index][POSCAMPOVALOR] = lstValores[j].getValor();
							   data[index][POSCAMPOALIAS] = lstValores[j].getAlias();
							   data[index][POSCAMPOTIPOVALOR] = TYPE_VALOR;
							   index ++;
						   }
					   }
					   if(lstValoresRef!= null && lstValoresRef[0] != null){
						   for (int j = 0; j < lstValoresRef.length; j++) {
							   data[index][POSCAMPOID] = lstValoresRef[j].getDeterminacionLayer().getCodigo();
							   data[index][POSCAMPOSEL] = new Boolean(lstValoresRef[j].getSelected());
							   data[index][POSCAMPOCODDETSEL] = lstDeterminacionesAplic[i].getDeterminacionLayer().getCodigo();
							   data[index][POSCAMPODETSEL] = lstDeterminacionesAplic[i].getDeterminacionLayer().getNombre();
							   data[index][POSCAMPOVALOR] = lstValoresRef[j].getDeterminacionLayer().getNombre();
							   data[index][POSCAMPOALIAS] = lstValoresRef[j].getAlias();
							   data[index][POSCAMPOTIPOVALOR] = TYPE_VALOR_REFERENCIA;
							   index ++;
						   }
					   }
					   
					   if(lstRegimenesEsp!= null && lstRegimenesEsp[0] != null){
						   for (int j = 0; j < lstRegimenesEsp.length; j++) {
							   data[index][POSCAMPOID] = String.valueOf(lstRegimenesEsp[j].getId());
							   data[index][POSCAMPOSEL] = new Boolean(lstRegimenesEsp[j].getSelected());
							   data[index][POSCAMPOCODDETSEL] = lstDeterminacionesAplic[i].getDeterminacionLayer().getCodigo();
							   data[index][POSCAMPODETSEL] = lstDeterminacionesAplic[i].getDeterminacionLayer().getNombre();
							   data[index][POSCAMPOVALOR] = lstRegimenesEsp[j].getNombre() + " - "+ lstRegimenesEsp[j].getTexto();
							   data[index][POSCAMPOALIAS] = lstRegimenesEsp[j].getAlias();
							   data[index][POSCAMPOTIPOVALOR] = TYPE_REGIMEN_ESPECIFICO;
							   index ++;
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
	
	public ConfLayerDeterminacionAplicadaBean[] getLstDeterminacionesAplic() {
		return lstDeterminacionesAplic;
	}


	public void setLstDeterminacionesAplic(
			ConfLayerDeterminacionAplicadaBean[] lstDeterminacionesAplic) {
		this.lstDeterminacionesAplic = lstDeterminacionesAplic;
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
				String id = (String) model.getValueAt(row, POSCAMPOID);
				String type = (String) model.getValueAt(row, POSCAMPOTIPOVALOR);	
				String alias = (String) model.getValueAt(row, POSCAMPOALIAS);	
				
				for (int i = 0; i < lstDeterminacionesAplic.length; i++) {
					if(lstDeterminacionesAplic[i] != null){
						if(lstDeterminacionesAplic[i].getSelected()){
							   
							if(type.equals(TYPE_VALOR)){
								ConfLayerValorBean[] lstValores = (ConfLayerValorBean[])lstDeterminacionesAplic[i].getLstValores();
								if(lstValores!= null && lstValores[0] != null){
									for (int j = 0; j < lstValores.length; j++) {
									   
										if(id.equals(String.valueOf(lstValores[j].getId()))){
											lstValores[j].setSelected(checked);
											lstValores[j].setAlias(alias);
										}
									}
								}
							}
							else if(type.equals(TYPE_VALOR_REFERENCIA)){
								ConfLayerValorReferenciaBean[] lstValoresRef = (ConfLayerValorReferenciaBean[])lstDeterminacionesAplic[i].getLstValoresReferencia();
								if(lstValoresRef!= null && lstValoresRef[0] != null){
									for (int j = 0; j < lstValoresRef.length; j++) {
										   
										if(id.equals(String.valueOf(lstValoresRef[j].getDeterminacionLayer().getCodigo()))){
											lstValoresRef[j].setSelected(checked);
											lstValoresRef[j].setAlias(alias);
										}
									}
								}
							}
							else if(type.equals(TYPE_REGIMEN_ESPECIFICO)){
								ConfLayerRegimenEspecificoBean[] lstRegimenesEsp =(ConfLayerRegimenEspecificoBean[])lstDeterminacionesAplic[i].getLstRegimenesEspecificos();
								if(lstRegimenesEsp!= null && lstRegimenesEsp[0] != null){
									for (int j = 0; j < lstRegimenesEsp.length; j++) {
									
										if(id.equals(String.valueOf(lstRegimenesEsp[j].getId()))){
											lstRegimenesEsp[j].setSelected(checked);
											lstRegimenesEsp[j].setAlias(alias);
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