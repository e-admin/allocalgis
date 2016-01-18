/*
 * BienesJPanel.java
 *
 * Created on 7 de julio de 2006, 9:39
 */

package com.geopista.app.eiel.panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.log4j.Logger;

import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.models.AbastecimientoAutonomoEIELTableModel;
import com.geopista.app.utilidades.TableSorted;

public class ListaDatosEIELJPanel extends javax.swing.JPanel {
	
    Logger logger= Logger.getLogger(ListaDatosEIELJPanel.class);

    private TableModel listaDatosEIELJTableModel;
    private ArrayList actionListeners= new ArrayList();
    private int selectedRow= -1;
    private TableSorted tableSorted = null;    
    private JScrollPane listaDatosEIELJScrollPane = null;
    private JTable listaDatosEIELJTable = null;

	private JScrollPane jScrollPaneListaDatos = null;

	private JTable jTableListaDatos = null;
	private TableModel tableListaDatosModel = null;
	private String nameModel = null;
	private String nameTableModel = null;

    /**
     * Método que genera el panel del listado de bienes asociadas a un tipo de bien
     * @param locale
     */
    public ListaDatosEIELJPanel() {
    	
    	    initComponents();
    }
    
    public ListaDatosEIELJPanel(String nameModel) {
    	this.nameTableModel = nameModel;
    	initComponents();
    }

    
    private void init(){
    	
    	
    }
    
    public void removeJScrollPanestaDatos(){
    	
    	jScrollPaneListaDatos = null;
    	jTableListaDatos = null;
    }

    /**
     * Método llamado por el constructor para inicializar el formulario
     */
    
    public JScrollPane getJScrollPaneListaDatos(){

    	if (jScrollPaneListaDatos == null){

    		jScrollPaneListaDatos   = new JScrollPane();
    		//jScrollPaneListaDatos.setPreferredSize(new Dimension(800,800));
    		jScrollPaneListaDatos.setViewportView(getJTableListaDatos());
    		jScrollPaneListaDatos.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);


    	}
    	return jScrollPaneListaDatos;
    }
    
    public JTable getJTableListaDatos()
    {
    	if (jTableListaDatos  == null)
    	{
    		jTableListaDatos   = new JTable();
    		//jTableListaDatos.setPreferredSize(new Dimension(800,800));
    		
    		if (nameTableModel  != null){
    			
    			try {

    				tableListaDatosModel  = (TableModel) Class.forName(this.nameTableModel).newInstance();

    				TableSorted tblSorted= new TableSorted(tableListaDatosModel);
    				tblSorted.setTableHeader(jTableListaDatos.getTableHeader());
    				
 
    				
    				jTableListaDatos.setModel(tblSorted);
    				jTableListaDatos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    				jTableListaDatos.setCellSelectionEnabled(false);
    				jTableListaDatos.setColumnSelectionAllowed(false);
    				jTableListaDatos.setRowSelectionAllowed(true);
    				jTableListaDatos.getTableHeader().setReorderingAllowed(false);
    				
       				
    				//final TableRowSorter<TableModel> sorter =    new TableRowSorter<TableModel>(tableListaDatosModel);
    				//sorter.setRowFilter(RowFilter.regexFilter("2",1));
    				
    				
    				((TableSorted)jTableListaDatos.getModel()).getTableHeader().addFocusListener(new java.awt.event.FocusAdapter() {
    		            public void focusGained(java.awt.event.FocusEvent evt) {
    		            	 //reColorearBloqueo();
                        	//System.out.println("PASO");
    		            	for (Iterator <ActionListener>i = actionListeners.iterator(); i.hasNext();) {
                                ActionListener l = (ActionListener) i.next();
                                l.actionPerformed(new ActionEvent(this, 0, ConstantesLocalGISEIEL.SORT));
                            }
    		            }
    		            
    		        });
    				
    				((TableSorted)jTableListaDatos.getModel()).getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
    		            public void mouseClicked(java.awt.event.MouseEvent evt) {
    		            	 //reColorearBloqueo();
                        	//System.out.println("PASO");
    		            	//bienesJTableMouseReleased();
                        	for (Iterator <ActionListener>i = actionListeners.iterator(); i.hasNext();) {
                                ActionListener l = (ActionListener) i.next();
                                l.actionPerformed(new ActionEvent(this, 0, ConstantesLocalGISEIEL.SORT));
                            }
    		            }
    		        });
    				
    				jTableListaDatos.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseReleased(java.awt.event.MouseEvent evt) {
                        	//clearSelection(versionJTable);
                            bienesJTableMouseReleased();
                        }
                        public void mouseClicked(java.awt.event.MouseEvent evt){
                        	if(evt.getClickCount() == 2) {
                        		//getBienSeleccionado();
                        		for (Iterator <ActionListener>i = actionListeners.iterator(); i.hasNext();) {
                                    ActionListener l = (ActionListener) i.next();
                                    l.actionPerformed(new ActionEvent(this, 0, ConstantesLocalGISEIEL.EVENT_DOUBLE_CLICK));
                                }
                            }
                        }
                    });

    			} catch (InstantiationException e) {
    				e.printStackTrace();
    			} catch (IllegalAccessException e) {
    				e.printStackTrace();
    			} catch (ClassNotFoundException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    	return jTableListaDatos;
    }
    
    public void setNameTableModel(String nameTableModel){
    	this.nameTableModel = nameTableModel;
    }
    
    
    public TableModel obtenerTableModel(String patronSelected){
    	
		return listaDatosEIELJTableModel;
    	
    	
    }
    
    private void initComponents() {
    	
        
    	
    	this.setLayout(new GridBagLayout());
    	
    	this.add(getJScrollPaneListaDatos(), 
        		new GridBagConstraints(0,0,1,1,1, 1,GridBagConstraints.CENTER,
                        GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));

        /** Re-Ordenacion de la tabla - repintado del bloqueo */        

        /*getJTableListaDatos().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bienesJTableMouseReleased();
            }
        });*/

    }

    /**
     * Método que hace un columna de la tabla no visible
     */
    private void setInvisible(int column){
        /** columna hidden no visible */
        TableColumn col= listaDatosEIELJTable.getColumnModel().getColumn(column);
        col.setResizable(false);
        col.setWidth(0);
        col.setMaxWidth(0);
        col.setMinWidth(0);
        col.setPreferredWidth(0);
    }

       

    private void bienesJTableMouseReleased() {
        fireActionPerformed();
    }



    /**
     * Método que desmarca la fila seleccionada en la tabla
     */
    public void clearSelection(){
        listaDatosEIELJTable.clearSelection();
    }
    
    

    public void addActionListener(ActionListener l) {
        this.actionListeners.add(l);
    }

    public void removeActionListener(ActionListener l) {
        this.actionListeners.remove(l);
    }

    private void fireActionPerformed() {
        for (Iterator i = actionListeners.iterator(); i.hasNext();) {
            ActionListener l = (ActionListener) i.next();
            l.actionPerformed(new ActionEvent(this, 0, null));
        }
    }




    

}
