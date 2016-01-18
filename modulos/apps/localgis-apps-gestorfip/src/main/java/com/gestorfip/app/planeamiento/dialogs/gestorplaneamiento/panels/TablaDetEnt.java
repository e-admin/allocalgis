/**
 * TablaDetEnt.java
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
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

import com.gestorfip.app.planeamiento.beans.panels.tramite.DeterminacionPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.EntidadPanelBean;
import com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean;
import com.gestorfip.app.planeamiento.beans.tramite.EntidadBean;

public class TablaDetEnt extends JPanel{

	private String etiqueta;
    private JTable elementosTable;

	private String[] identificadores;
    private DefaultTableModel modelo;
    private JScrollPane tablaScrollPanel;
    private ArrayList editors;
    private boolean sorted = false;
    
    private String nomCampo1 ;
    
 
	  public TablaDetEnt(String label,String nomCampo1, Object[] lst, int width, int heigh)
	    {
		  	//this.lstDeterminaciones = lstDeterminaciones;
	        etiqueta= label;
	        this.nomCampo1 = nomCampo1;
	        
	        inicializaTabla(width, heigh);
	        if(lst instanceof EntidadBean[]){
		        if(lst!=null && lst.length !=0)
		        {
		        	
		            cargaDatosTablaEnt((EntidadBean[])lst);
		        }
	    	}
	        else if(lst instanceof DeterminacionBean[]){
		        if(lst!=null && lst.length !=0)
		        {
		            cargaDatosTablaDet((DeterminacionBean[])lst);
		        }
	    	}
	    }
	  
	  /**
	     * Inicializa todos los elementos del panel y los coloca en su posicion. Carga la tabla y le asigna los modos
	     * que queremos. Se sobreescribe el TableCellEditor para que devuelva el array de referencias  de la
	     * fila.
	     */
	    private void inicializaTabla(int width, int heigh)
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
	        elementosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        elementosTable.setCellSelectionEnabled(false);
			elementosTable.setColumnSelectionAllowed(false);
			elementosTable.setRowSelectionAllowed(true);
	        elementosTable.getTableHeader().setReorderingAllowed(false);
	        elementosTable.getTableHeader().setResizingAllowed(true);
	        elementosTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

	        // Disable autoCreateColumnsFromModel otherwise all the column customizations
	        // and adjustments will be lost when the model data is sorted
	        elementosTable.setAutoCreateColumnsFromModel(false);

	        elementosTable.getTableHeader().addMouseListener(new MouseListener(){

	            public void mouseClicked(MouseEvent e) {

	                int columna = elementosTable.columnAtPoint(e.getPoint());
	                sorted = !sorted;
	                sortAllRowsBy(modelo, columna, sorted);//e.getClickCount()%2 == 0? false : true);

	            }

	            public void mouseEntered(MouseEvent e) {}

	            public void mouseExited(MouseEvent e) {}

	            public void mousePressed(MouseEvent e) {}

	            public void mouseReleased(MouseEvent e) {}
	        });

	        setInvisible(0,elementosTable);
//	        setInvisible(1,docAsocTable);
//	        
	        tablaScrollPanel= new JScrollPane(elementosTable);
	        tablaScrollPanel.setPreferredSize(new Dimension(width,heigh));

			this.add(tablaScrollPanel,
			            new GridBagConstraints(0, 0, 1, 1, 1.0, 1,
			                GridBagConstraints.BOTH, GridBagConstraints.BOTH,
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
	    public void cargaDatosTablaDet(DeterminacionPanelBean[] lstDeterminaciones) {
	        editors.clear();
	        
	        String[][] datos= null;
	        
	        if(lstDeterminaciones != null ){
	        	datos= new String[lstDeterminaciones.length][2];
		        for(int i=0; i< lstDeterminaciones.length ;i++)
		        {

			        	DeterminacionPanelBean detPanelBean = lstDeterminaciones[i];
			        	if(detPanelBean != null){
			        		datos[i][0] = String.valueOf(detPanelBean.getId());
				        	datos[i][1] = detPanelBean.getNombre();
			        	}
		        }
	        }

	        modelo.setDataVector(datos,identificadores);
	        elementosTable.setModel(modelo);
	    }
	    
	    
	    public void cargaDatosTablaDet(DeterminacionBean[] lstDeterminaciones) {
	        editors.clear();
	        
	        String[][] datos= null;
	        
	        if(lstDeterminaciones != null ){
	        	datos= new String[lstDeterminaciones.length][2];
		        for(int i=0; i< lstDeterminaciones.length ;i++)
		        {

		        	DeterminacionBean detBean = lstDeterminaciones[i];
		        	if(detBean != null){
		        		datos[i][0] = String.valueOf(detBean.getId());
			        	datos[i][1] = detBean.getNombre();
		        	}
		        }
	        }

	        modelo.setDataVector(datos,identificadores);
	        elementosTable.setModel(modelo);
	    }
	    
	    public void cargaDatosTablaEnt(EntidadBean[] lstEntidades) {
	        editors.clear();
	        
	        String[][] datos= null;
	        
	        if(lstEntidades != null ){
	        	datos= new String[lstEntidades.length][2];
		        for(int i=0; i< lstEntidades.length ;i++)
		        {

		        	EntidadBean entBean = lstEntidades[i];
		        	if(entBean != null){
		        		datos[i][0] = String.valueOf(entBean.getId());
			        	datos[i][1] = entBean.getNombre();
		        	}
		        }
	        }

	        modelo.setDataVector(datos,identificadores);
	        elementosTable.setModel(modelo);
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

	    
	    public ArrayList getDatosTabla()
	    {
	    	 Enumeration filas =(Enumeration) modelo.getDataVector().elements() ;
	    	 ArrayList lstDatosTabla = new ArrayList();
	    	 while (filas.hasMoreElements()){
	    		 String nomFich = (String)filas.nextElement();
	    		 lstDatosTabla.add(nomFich);
	    	 }
	    	
	    	return lstDatosTabla;
	    }
	    /**
	     * Renombra las etiquetas, botones, nombre del panel, etc. Clase
	     * necesaria para implementar IMultilingue
	     * */
	    public void renombrarComponentes()
	    {
	    	identificadores[0] = "";
	    	identificadores[1] = nomCampo1;
	        
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

	     /**
	      * Devuelve el numero de la fila seleccionada
	      *
	      * @return int Numero de la fila seleccionada
	      */
	     public int getIdElementoSeleccionado()
	     {
	    	 int idElementoTabla = -1;
	    	 //DeterminacionPanelBean detBean;
	    	 if (elementosTable.getSelectedRow() == -1){
	    		 idElementoTabla = -1;
	    	 }
	    	 else{
		         Vector fila =(Vector) modelo.getDataVector().elementAt(elementosTable.getSelectedRow()) ;
		        // detBean = new DeterminacionPanelBean();
		       //  detBean.setId(new Integer((String)fila.elementAt(0)).intValue());
		         idElementoTabla =  new Integer((String)fila.elementAt(0)).intValue();
		        // detBean.setNombre((String)fila.elementAt(1));
		        // detBean.setCodigo((String)fila.elementAt(2));
	    	 }
	         return idElementoTabla;
	     }
	     
	     public int getIdDeterminacionSeleccionada(int id)
	     {
	    	 int valorDevolver = -1;
	    	 Enumeration enume = modelo.getDataVector().elements();
	    	 
	    	 while (enume.hasMoreElements()){
	    		 Vector fila = (Vector)enume.nextElement();
	    		 
	    		int  idDeterAsoc = new Integer((String)fila.elementAt(1)).intValue();
	    		 
	    		 if(idDeterAsoc == id){
	    			 valorDevolver =  idDeterAsoc;
	    		 }
	    	 }
	    	 
	    	// Vector fila =(Vector) modelo.getDataVector().elementAt(id);
	    	 
	    //	 return new Integer((String)fila.elementAt(1)).intValue();
	    	 
	    	 return valorDevolver;
	     
	     }
	     
	     
	     public void desactivarTabla(){
	    	 	    	 
	    	 tablaScrollPanel.setEnabled(false);
	    	 elementosTable.setEnabled(false);
	     }
	     
	     public void activarTabla(){
	    	 
	    	 tablaScrollPanel.setEnabled(true);
	    	 elementosTable.setEnabled(true);
	     }
	     
	     public boolean isEmptyTabla(){
	    	 return modelo.getDataVector().isEmpty();
	     }
	     
	 	public JTable getElementosTable() {
			return elementosTable;
		}

		public void setElementosTable(JTable elementosTable) {
			this.elementosTable = elementosTable;
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


