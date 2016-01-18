/**
 * TablaRegimenes.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoRegimenesBean;

public class TablaRegimenes extends JPanel{

	private String etiqueta;
    private JTable elementosTable;
	private String[] identificadores;
    private DefaultTableModel modelo;
	private JScrollPane tablaScrollPanel;
    private ArrayList editors;
    private boolean sorted = false;
	private ArrayList lstCUCR;

  
	private String titleCampo1;
    
	  public TablaRegimenes( ArrayList lstCUCR, String label,  String titleCampo1)
	    {
		  	this.etiqueta = label;
		  	this.lstCUCR = lstCUCR;
	        this.titleCampo1 = titleCampo1;
	        inicializaTabla();
	        if(lstCUCR!=null && !lstCUCR.isEmpty())
	        {
	           cargaDatosTabla(lstCUCR);
	        }
	    }
	  
	  /**
	     * Inicializa todos los elementos del panel y los coloca en su posicion. Carga la tabla y le asigna los modos
	     * que queremos. Se sobreescribe el TableCellEditor para que devuelva el array de referencias  de la
	     * fila.
	     */
	    private void inicializaTabla()
	    {
	    	this.setBorder(new TitledBorder(etiqueta));
	    	elementosTable= new JTable(){
	            public TableCellEditor getCellEditor(int row, int column){
	                if (column == 7)
	                    return (TableCellEditor)editors.get(row);
	                else
	                    return super.getCellEditor(row, column);
	            }
	            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
	                Component c = super.prepareRenderer(renderer, row, column);
	                if (c instanceof JComponent) {
	                    JComponent jc = (JComponent) c;
	                    jc.setToolTipText(getValueAt(row, column).toString());
	                }
	                return c;
	            }
	        };

	        editors = new ArrayList();
	        identificadores = new String[2];

	        modelo= new DefaultTableModel() {
	            public boolean isCellEditable(int row, int column) {
	                if(column==7)
	                    return true;
	                else
	                    return false;
	            }
	        };

	        renombrarComponentes();
	        elementosTable.setModel(modelo);
	        elementosTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	        elementosTable.setCellSelectionEnabled(false);
			elementosTable.setColumnSelectionAllowed(false);
			elementosTable.setRowSelectionAllowed(true);
	        elementosTable.getTableHeader().setReorderingAllowed(false);
	        elementosTable.getTableHeader().setResizingAllowed(true);
	        elementosTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

	        // Disable autoCreateColumnsFromModel otherwise all the column customizations
	        // and adjustments will be lost when the model data is sorted
	        elementosTable.setAutoCreateColumnsFromModel(false);
	        
	        setInvisible(0,elementosTable);
	        
	        tablaScrollPanel= new JScrollPane(elementosTable);
	        tablaScrollPanel.setPreferredSize(new Dimension(100,300));

			this.add(tablaScrollPanel,
			            new GridBagConstraints(0, 0, 1, 1, 1.0, 1,
			                GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST,
			                new Insets(0, 0, 0, 0), 0, 0));

	    }
	  
	  
	 // Regardless of sort order (ascending or descending), null values always appear last.
	    // colIndex specifies a column in model.
	    public void sortAllRowsBy(DefaultTableModel model, int colIndex, boolean ascending) {
	        Vector data = model.getDataVector();
	        Collections.sort(data, new ColumnSorter(colIndex, ascending));
	        model.fireTableStructureChanged();
	    }
	  
	    // This comparator is used to sort vectors of data
	    public class ColumnSorter implements Comparator {
	        int colIndex;
	        boolean ascending;
	        ColumnSorter(int colIndex, boolean ascending) {
	            this.colIndex = colIndex;
	            this.ascending = ascending;
	        }
	        public int compare(Object a, Object b) {
	            Vector v1 = (Vector)a;
	            Vector v2 = (Vector)b;
	            Object o1 = v1.get(colIndex);
	            Object o2 = v2.get(colIndex);

	            // Treat empty strains like nulls
	            if (o1 instanceof String && ((String)o1).length() == 0) {
	                o1 = null;
	            }
	            if (o2 instanceof String && ((String)o2).length() == 0) {
	                o2 = null;
	            }

	            // Sort nulls so they appear last, regardless
	            // of sort order
	            if (o1 == null && o2 == null) {
	                return 0;
	            } else if (o1 == null) {
	                return 1;
	            } else if (o2 == null) {
	                return -1;
	            } else if (o1 instanceof Comparable) {
	                if (ascending) {
	                    return ((Comparable)o1).compareTo(o2);
	                } else {
	                    return ((Comparable)o2).compareTo(o1);
	                }
	            } else {
	                if (ascending) {
	                    return o1.toString().compareTo(o2.toString());
	                } else {
	                    return o2.toString().compareTo(o1.toString());
	                }
	            }
	        }
	    }
	  
	  
	    /**
	     * Funcion que carga los datos del arrayList pasado por parametro en la tabla. Se cargan ciertos datos de los
	     * expedientes pasados por parametro. Para ver las referencias se hace un instanceof
	     *
	     * @param expedientes Los expedientes a mostrar en la tabla
	     * @param usuarios Los nombres de los usuarios de los expediente, ya que el expediente solo guarda el id usuario.
	     */
	    public void cargaDatosTabla(ArrayList lstRegimenes)
	    {
	    	this.lstCUCR = lstRegimenes;
	        editors.clear();
	        String[][] datos=null;
	        if(lstRegimenes != null && !lstRegimenes.isEmpty()){
	        	datos = new String [lstRegimenes.size()][2];
	        	 for(int i=0; i< lstRegimenes.size() ;i++)
			     {
	        		 String nombre = "";
		        	int id = 0;
		        	
	        		nombre = ((CondicionUrbanisticaCasoRegimenesBean)lstRegimenes.get(i)).getNombre(); 
	        		id = ((CondicionUrbanisticaCasoRegimenesBean)lstRegimenes.get(i)).getId();
		        	
		        	datos[i][0] = String.valueOf(id);
		        	datos[i][1] = nombre;
	        		 
			     }
	        }
	    	else{
	    		datos= new String[0][0];
	    	}
	    
	        modelo.setDataVector(datos,identificadores);
	        elementosTable.setModel(modelo);
	        
	    }
	    
	    /**
	     * Renombra las etiquetas, botones, nombre del panel, etc. Clase
	     * necesaria para implementar IMultilingue
	     * */
	    public void renombrarComponentes()
	    {
	    	
	        identificadores[0] = "";
	        identificadores[1] = titleCampo1;
	        
	        modelo.setColumnIdentifiers(identificadores);
	    }
	    
	    /**
	     * Devuelve el panel.
	     *
	     * @return this
	     * */    
	     public JPanel getPanel()
	     {

	         return this;
	     }

	     
	     public void desactivarTabla(){
	    	 
	    	 
	    	 tablaScrollPanel.setEnabled(false);
	    	 elementosTable.setEnabled(false);
	    	 elementosTable.clearSelection();
	     }
	     
	     
	     /**
	      * Devuelve el numero de la fila seleccionada
	      *
	      * @return int Numero de la fila seleccionada
	      */
	     public ArrayList getIdElementosSeleccionados()
	     {
	    
	    	 ArrayList lstIds = new ArrayList();
	    	 
	    	 int [] numElementosSeleccionados = elementosTable.getSelectedRows();
	    	 
	    	 if(numElementosSeleccionados != null && numElementosSeleccionados.length !=0){
	    		 
	    		 for(int i=0; i<numElementosSeleccionados.length; i++){
	    			 Vector fila =(Vector) modelo.getDataVector().elementAt(numElementosSeleccionados[i]) ;
	    			
	    			 lstIds.add(new Integer((String)fila.elementAt(0)));
	    			 
	    		 }
	    	 }

	         return lstIds;
	     }
	     /**
	      * Devuelve el numero de la fila seleccionada
	      *
	      * @return int Numero de la fila seleccionada
	      */
	     public int getIdElementoSeleccionado()
	     {
	    	 
	    	 int idRegimen = -1;
	    	 if (elementosTable.getSelectedRow() == -1){
	    		 idRegimen = -1;
	    	 }
	    	 else{
		         Vector fila =(Vector) modelo.getDataVector().elementAt(elementosTable.getSelectedRow()) ;
		         idRegimen = new Integer((String)fila.elementAt(0)).intValue();
	    	 }

	         return idRegimen;
	     }
	     
	     
	     public CondicionUrbanisticaCasoRegimenesBean elementoSeleccionado(){
	    	 CondicionUrbanisticaCasoRegimenesBean reg = null;
	    	 Vector fila =(Vector) modelo.getDataVector().elementAt(elementosTable.getSelectedRow()) ;
	    	 int idRegimen = new Integer((String)fila.elementAt(0)).intValue();
	    	 
	    	 for(int i=0; i<lstCUCR.size(); i++){
	    		
	    		 if(idRegimen == ((CondicionUrbanisticaCasoRegimenesBean)lstCUCR.get(i)).getId()){
	    			 reg = ((CondicionUrbanisticaCasoRegimenesBean)lstCUCR.get(i));
	    		 }
	    	 }
	    	 return reg;
	     }
	     
	     
	     
	     public boolean hayElementosEnTabla(){
	    	 boolean hayElementosEnTabla = false;
	    	 if (elementosTable.getRowCount() > 0){
	    		 hayElementosEnTabla = true;
	    	 }
	    	 return hayElementosEnTabla;
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
	     
	     public JTable getElementosTable() {
	 		return elementosTable;
	 	}

	 	public void setElementosTable(JTable elementosTable) {
	 		this.elementosTable = elementosTable;
	 	}

	 	public CondicionUrbanisticaCasoRegimenesBean getElementoSeleccionado()
	     {
	 		CondicionUrbanisticaCasoRegimenesBean cucr = null;
	    	 if (elementosTable.getSelectedRow() == -1){
	    		 cucr = null;
	    	 }
	    	 else{
		         Vector fila =(Vector) modelo.getDataVector().elementAt(elementosTable.getSelectedRow()) ;
		         int idCaso = new Integer((String)fila.elementAt(0)).intValue();
		         
		         for(int i=0; i<lstCUCR.size(); i++){
		        	 if( ((CondicionUrbanisticaCasoRegimenesBean)lstCUCR.get(i)).getId() ==
		        		 idCaso){
		        		 cucr = (CondicionUrbanisticaCasoRegimenesBean)lstCUCR.get(i);
		        	 }
		         }
		         
	    	 }

	         return cucr;
	     }
	 	
		  public ArrayList getLstCUCR() {
			return lstCUCR;
		}
		
		public void setLstCUCR(ArrayList lstCUCR) {
			this.lstCUCR = lstCUCR;
			}
		
		public boolean isSorted() {
			return sorted;
		}

		public void setSorted(boolean sorted) {
			this.sorted = sorted;
		}
		public DefaultTableModel getModelo() {
			return modelo;
		}

		public void setModelo(DefaultTableModel modelo) {
			this.modelo = modelo;
		}


		public void vaciarTabla(){
			int filas=modelo.getRowCount(); //obtienes el n de filas
			int x=0; // inicias este contador
			int tmp=filas; // esta variable sera la que lleve el conteo de la nueva ultima fila, pues al eliminar una fila cambian los indices
			while(x<filas){ 
				modelo.removeRow(tmp-1); //eliminas la ultima fila 
				x++; // contador avanza 
				tmp--; //sale un indice
			}
		}
}


