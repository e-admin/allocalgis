/**
 * TablaFicherosFip.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.dialogs.busquedaFip.panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import com.gestorfip.app.planeamiento.beans.panels.tramite.FipPanelBean;
import com.vividsolutions.jump.I18N;

public class TablaFicherosFip extends JPanel{

	private String etiqueta;
    private JTable fipTable;
    private String[] identificadores;
    private DefaultTableModel modelo;
    private JScrollPane tablaScrollPanel;
    private ArrayList editors;
    private boolean sorted = false;
	
	  public TablaFicherosFip(String label, ArrayList lstFicheros)
	    {
	        etiqueta= label;
	        inicializaTabla();
	        if(lstFicheros!=null && !lstFicheros.isEmpty())
	        {
	            cargaDatosTabla(lstFicheros);
	        }
	    }
	  
	  /**
	     * Inicializa todos los elementos del panel y los coloca en su posicion. Carga la tabla y le asigna los modos
	     * que queremos. Se sobreescribe el TableCellEditor para que devuelva el array de referencias  de la
	     * fila.
	     */
	    private void inicializaTabla()
	    {

	    	fipTable= new JTable(){
	            public TableCellEditor getCellEditor(int row, int column){
	                if (column == 7)
	                    return (TableCellEditor)editors.get(row);
	                else
	                    return super.getCellEditor(row, column);
	            }
	        };

	        editors = new ArrayList();
	        identificadores = new String[3];

	        modelo= new DefaultTableModel() {
	            public boolean isCellEditable(int row, int column) {
	                if(column==7)
	                    return true;
	                else
	                    return false;
	            }
	        };

	        renombrarComponentes();
	        fipTable.setModel(modelo);
	        fipTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        fipTable.setCellSelectionEnabled(false);
			fipTable.setColumnSelectionAllowed(false);
			fipTable.setRowSelectionAllowed(true);
	        fipTable.getTableHeader().setReorderingAllowed(false);
	        fipTable.getTableHeader().setResizingAllowed(true);
	        fipTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

	        // Disable autoCreateColumnsFromModel otherwise all the column customizations
	        // and adjustments will be lost when the model data is sorted
	        fipTable.setAutoCreateColumnsFromModel(false);

	        fipTable.getTableHeader().addMouseListener(new MouseListener(){

	            public void mouseClicked(MouseEvent e) {

	                int columna = fipTable.columnAtPoint(e.getPoint());
	                sorted = !sorted;
	                sortAllRowsBy(modelo, columna, sorted);

	            }

	            public void mouseEntered(MouseEvent e) {}

	            public void mouseExited(MouseEvent e) {}

	            public void mousePressed(MouseEvent e) {}

	            public void mouseReleased(MouseEvent e) {}
	        });


	        tablaScrollPanel= new JScrollPane(fipTable);

	        tablaScrollPanel.setPreferredSize(new Dimension(450,270));

			 this.add(tablaScrollPanel,
			            new GridBagConstraints(0, 0, 1, 1, 1.0, 1,
			                GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST,
			                new Insets(0, 0, 0, 0), 0, 0));

			 setInvisible(0,fipTable);
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
	    public void cargaDatosTabla(ArrayList lstFicheros)
	    {
	        editors.clear();
	        String[][] datos= new String[lstFicheros.size()][3];
	        for(int i=0; i< lstFicheros.size() ;i++)
	        {
	        	FipPanelBean fip = (FipPanelBean)lstFicheros.get(i);
	        	
	        	datos[i][0] = String.valueOf(fip.getId());
	        	datos[i][1] = fip.getMunicipio();
	        	datos[i][2] = fip.getFechaConsolidacion();
	        }
	        
	        modelo.setDataVector(datos,identificadores);
	        fipTable.setModel(modelo);
	    }
	    
	    /**
	     * Renombra las etiquetas, botones, nombre del panel, etc. Clase
	     * necesaria para implementar IMultilingue
	     * */
	    public void renombrarComponentes()
	    {
	    	identificadores[0] = ("");
	    	
	        identificadores[1] = (I18N.get("LocalGISGestorFip",
	        						"gestorFip.abrirfip.tabla.municipioFicheroFip1"));
	        identificadores[2] = (I18N.get("LocalGISGestorFip",
	        						"gestorFip.abrirfip.tabla.fechaFicheroFip1"));
	        
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
	      * Devuelve el id del fip seleccionado
	      *
	      * @return int fip seleccionado
	      */
	     public int getFicheroSeleccionado()
	     {
	    	 int fipSeleccionado = -1;
	    	 if (fipTable.getSelectedRow() == -1){
	    		 fipSeleccionado = -1;
	    	 }
	    	 else{
		         Vector fila =(Vector) modelo.getDataVector().elementAt(fipTable.getSelectedRow()) ;
		       //  fipSeleccionado = (String)fila.elementAt(0);
		         fipSeleccionado = Integer.valueOf(((String)fila.get(0))).intValue();
	    	 }
	         return fipSeleccionado;
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
}

