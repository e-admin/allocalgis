/**
 * BienesJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * BienesJPanel.java
 *
 * Created on 7 de julio de 2006, 9:39
 */

package com.geopista.app.inventario.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.InventarioTableModel;
import com.geopista.app.inventario.renderer.ColorTableVersionesCellRenderer;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.BienRevertible;
import com.geopista.protocol.inventario.ConfigParameters;
import com.geopista.protocol.inventario.Const;
import com.geopista.protocol.inventario.CreditoDerechoBean;
import com.geopista.protocol.inventario.Lote;
import com.geopista.protocol.inventario.NumInventario;
import com.geopista.protocol.inventario.StringComparator;
import com.geopista.util.config.UserPreferenceStore;


public class BienesJPanel extends javax.swing.JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	
	private static final int BIENES_COLUMNA_REVISION_EXPIRADA=6;
	private static final int BIENES_REVERTIBLES_COLUMNA_REVISION_EXPIRADA=6;

	Logger logger= Logger.getLogger(BienesJPanel.class);

    private AppContext aplicacion;
    private String locale;
    private InventarioTableModel bienesJTableModel;
    //private InventarioTableModel bienesBorradosJTableModel;
    private InventarioTableModel versionesJTableModel;
    private InventarioTableModel bienesRevertiblesJTableModel;
    private InventarioTableModel versionesBRJTableModel;
    private InventarioTableModel lotesJTableModel;
    public static final String DOBLE_CLICK="DOBLE_CLICK";
    public static final String CHANGE_TAB="CHANGE_TAB";
    
    private ArrayList <ActionListener> actionListeners= new ArrayList<ActionListener>();
    private int selectedRow= -1;
    private static int MAX_REGISTROS=500;
    
    private TableSorted tableSorted;
    //private TableSorted tableBorradosSorted;
    private TableSorted tableSortedVersion;
    private TableSorted tableSortedBienesRevertibles;
    private TableSorted tableSortedVersionBR;
    private TableSorted tableSortedLotes;
    
    private JScrollPane bienesJScrollPane;
    //private JScrollPane bienesBorradosJScrollPane;
    private JScrollPane versionesBienesJPanel;
    private JScrollPane bienesRevertiblesJScrollPanel;
    private JScrollPane versionesBRBienesJPanel;
    private JScrollPane lotesJScrollPanel;
    
    private javax.swing.JTable bienesJTable;
    //private javax.swing.JTable bienesBorradosJTable;
    private javax.swing.JTable versionJTable;
    private javax.swing.JTable bienesRevertiblesJTable;
    private javax.swing.JTable versionBRJTable;
    private javax.swing.JTable lotesJTable;
    
    private JPanel contadorBienesJPanel;
	private JLabel contadorBienesJLabel;
	
	private JButton adelanteJButton;
	private JButton atrasJButton;
	
	private boolean showSeleccionar=false;
	JTabbedPane panelPestañasBienes;

	private InventarioJPanel inventarioJPanel;
	

	private int actualOffset=0;
	
	private boolean seleccionTabla;
	
	/**
     * Método que genera el panel del listado de bienes asociadas a un tipo de bien
     * @param locale
     */
    public BienesJPanel( String locale, boolean showVersiones, boolean showSeleccionar) {
        this.locale= locale;
        this.aplicacion= (AppContext) AppContext.getApplicationContext();
        this.showSeleccionar=showSeleccionar;
        initComponents(showVersiones, showSeleccionar);
        renombrarComponentes();
    }

    /**
     * Método que genera el panel del listado de bienes asociadas a un tipo de bien
     * @param locale
     */
    public BienesJPanel( String locale) {
    	 this(locale,true,false);
    }

    /**
     * Método llamado por el constructor para inicializar el formulario
     */
    private void initComponents(boolean showVersiones, boolean showSeleccionar) {
        bienesJScrollPane = new javax.swing.JScrollPane();
        //bienesBorradosJScrollPane = new javax.swing.JScrollPane();
        bienesRevertiblesJScrollPanel = new javax.swing.JScrollPane(); 
        lotesJScrollPanel = new javax.swing.JScrollPane(); 

        contadorBienesJPanel = new JPanel();
        contadorBienesJPanel.setLayout(new GridBagLayout());
        contadorBienesJLabel = new JLabel();
        contadorBienesJLabel.setText("");
        contadorBienesJPanel.add(contadorBienesJLabel, 
        		new GridBagConstraints(15, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,
        				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 10, 0),0,0));
        

        adelanteJButton = new JButton();
        adelanteJButton.setText(">");
        adelanteJButton.setEnabled(false);
        adelanteJButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
				adelanteJButton_actionPerformed();
			}

		});
        
        if (Constantes.MOSTRAR_PAGINACION)
        	contadorBienesJPanel.add(adelanteJButton, 
        		new GridBagConstraints(20, 0, 0, 1, 1, 1, GridBagConstraints.EAST,
        				GridBagConstraints.CENTER, new Insets(2, 2, 5, 0),0,0));

   
        atrasJButton = new JButton();
        atrasJButton.setText("<");
        atrasJButton.setEnabled(false);
        atrasJButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
            	atrasJButton_actionPerformed();
			}

		});
        
        if (Constantes.MOSTRAR_PAGINACION)
        	contadorBienesJPanel.add(atrasJButton, 
        		new GridBagConstraints(10, 0, 1, 1, 1, 1, GridBagConstraints.WEST,
        				GridBagConstraints.CENTER, new Insets(5, 5, 10, 0),0,0));
        
        setLayout(new java.awt.BorderLayout());
        initHeadersJTable(showSeleccionar);
        initHeadersJTableBienRevertible();
        initHeadersJTableLotes();

        getTableBienes(showSeleccionar);
        //getTableBienesBorrados(showSeleccionar);
        getTableBienesRevertibles();
        getTableLotes();
        getTableVersion();
        getTableVersionBR();
        
        panelPestañasBienes = new JTabbedPane();
        panelPestañasBienes.addTab(aplicacion.getI18nString("inventario.bienes.tag1"),bienesJScrollPane);

        //panelPestañasBienes.addTab(aplicacion.getI18nString("inventario.bienes.tag1.1"),bienesBorradosJScrollPane);
        if (showVersiones)
        	panelPestañasBienes.addTab(aplicacion.getI18nString("inventario.bienes.versiones"),versionesBienesJPanel);
        
       
        add(panelPestañasBienes, BorderLayout.CENTER);
        add(contadorBienesJPanel, BorderLayout.SOUTH);

        
        panelPestañasBienes.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                  try{clearSelection(versionBRJTable);}catch(Exception ex){}
                  try{clearSelection(versionJTable);}catch(Exception ex){}
                  try{clearSelection(bienesJTable);}catch(Exception ex){}
                  try{clearSelection(bienesRevertiblesJTable);}catch(Exception ex){}
                  for (Iterator <ActionListener>i = actionListeners.iterator(); i.hasNext();) {
                      ActionListener l = (ActionListener) i.next();
                      l.actionPerformed(new ActionEvent(this, 0, CHANGE_TAB));
                  }
            }
        });

    }
   
    

	/**
     * Inicializa la tabla bienes 
     */
    private void getTableBienes(boolean showSeleccionar){
    	bienesJTable = new BienTableRenderer(BIENES_COLUMNA_REVISION_EXPIRADA); 
        /* Ordenacion de la tabla */
        tableSorted= new TableSorted(bienesJTableModel);
        bienesJTableModel.setTableSorted(tableSorted);
        tableSorted.setTableHeader(bienesJTable.getTableHeader());
        tableSorted.setColumnComparator(NumInventario.class, NumInventario.NUM_INVENTARIO_COMPARATOR);
        
        //La columna 2 y 3 son las fechas de alta y modificacion. La 4 es el nombre del inventario
        try {
			tableSorted.setColumnComparatorByColumn(2, TimeStampComparator.DATE_COMPARATOR);
			tableSorted.setColumnComparatorByColumn(3,TimeStampComparator.DATE_COMPARATOR);
			tableSorted.setColumnComparatorByColumn(4,StringComparator.STRING_COMPARATOR);
		} catch (Exception e) {
		}
        
        /*Tabla bienes*/
        bienesJTable.setModel(tableSorted);
        bienesJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bienesJTable.setCellSelectionEnabled(false);
        bienesJTable.setColumnSelectionAllowed(false);
        bienesJTable.setRowSelectionAllowed(true);
        bienesJTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS/*JTable.AUTO_RESIZE_OFF*/);
        bienesJTable.getTableHeader().setReorderingAllowed(false);
        setInvisible(bienesJTableModel.getColumnCount()-1,bienesJTable);
        setInvisible(bienesJTableModel.getColumnCount()-2,bienesJTable);
        bienesJTableModel.setTable(bienesJTable);
      
        TableColumn col= bienesJTable.getColumnModel().getColumn(0);
        col.setCellEditor(new com.geopista.app.licencias.utilidades.CheckBoxTableEditor(new JCheckBox()));
        col.setCellRenderer(new com.geopista.app.licencias.utilidades.CheckBoxRenderer());
        if (!showSeleccionar){
             /** al ser una check-box dejamos el espacio suficiente impidiendo que la columna se pueda hacer mas grande */
             col.setMinWidth(50);
             col.setMaxWidth(50);
             col.setPreferredWidth(50);
        }else{
            col.setMinWidth(100);
            col.setPreferredWidth(100);
            ((com.geopista.app.licencias.utilidades.CheckBoxRenderer)col.getCellRenderer()).setHorizontalAlignment(JLabel.LEFT);
        }
        	

      
        /** Re-Ordenacion de la tabla - repintado del bloqueo */
        ((TableSorted)bienesJTable.getModel()).getTableHeader().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                reColorearBloqueo();
            }
        });
        ((TableSorted)bienesJTable.getModel()).getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reColorearBloqueo();
            }
        });

        bienesJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
            	clearSelection(versionJTable);
                bienesJTableMouseReleased();
            }
            public void mouseClicked(java.awt.event.MouseEvent evt){
            	if(evt.getClickCount() == 2) {
            		getBienSeleccionado();
            		for (Iterator <ActionListener>i = actionListeners.iterator(); i.hasNext();) {
                        ActionListener l = (ActionListener) i.next();
                        l.actionPerformed(new ActionEvent(this, 0, DOBLE_CLICK));
                    }
                }
            }
        });
        
        bienesJScrollPane.setViewportView(bienesJTable);
        
      
    }
    
    /*private void getTableBienesBorrados(boolean showSeleccionar){
    	bienesBorradosJTable = new BienTableRenderer(BIENES_COLUMNA_REVISION_EXPIRADA); 
        tableBorradosSorted= new TableSorted(bienesBorradosJTableModel);
        bienesBorradosJTableModel.setTableSorted(tableBorradosSorted);
        tableBorradosSorted.setTableHeader(bienesBorradosJTable.getTableHeader());
        tableBorradosSorted.setColumnComparator(NumInventario.class, NumInventario.NUM_INVENTARIO_COMPARATOR);
       
        bienesBorradosJTable.setModel(tableBorradosSorted);
        bienesBorradosJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bienesBorradosJTable.setCellSelectionEnabled(false);
        bienesBorradosJTable.setColumnSelectionAllowed(false);
        bienesBorradosJTable.setRowSelectionAllowed(true);
        bienesBorradosJTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS
        bienesBorradosJTable.getTableHeader().setReorderingAllowed(false);
        setInvisible(bienesBorradosJTableModel.getColumnCount()-1,bienesJTable);
        setInvisible(bienesBorradosJTableModel.getColumnCount()-2,bienesJTable);
        bienesBorradosJTableModel.setTable(bienesBorradosJTable);
      
        TableColumn col= bienesBorradosJTable.getColumnModel().getColumn(0);
        col.setCellEditor(new com.geopista.app.licencias.utilidades.CheckBoxTableEditor(new JCheckBox()));
        col.setCellRenderer(new com.geopista.app.licencias.utilidades.CheckBoxRenderer());
        if (!showSeleccionar){
             //al ser una check-box dejamos el espacio suficiente impidiendo que la columna se pueda hacer mas grande 
             col.setMinWidth(50);
             col.setMaxWidth(50);
             col.setPreferredWidth(50);
        }else{
            col.setMinWidth(100);
            col.setPreferredWidth(100);
            ((com.geopista.app.licencias.utilidades.CheckBoxRenderer)col.getCellRenderer()).setHorizontalAlignment(JLabel.LEFT);
        }
        	

      
        // Re-Ordenacion de la tabla - repintado del bloqueo
        ((TableSorted)bienesBorradosJTable.getModel()).getTableHeader().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                reColorearBloqueo();
            }
        });
        ((TableSorted)bienesBorradosJTable.getModel()).getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reColorearBloqueo();
            }
        });

        bienesBorradosJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
            	clearSelection(versionJTable);
                bienesJTableMouseReleased();
            }
            public void mouseClicked(java.awt.event.MouseEvent evt){
            	if(evt.getClickCount() == 2) {
            		getBienSeleccionado();
            		for (Iterator <ActionListener>i = actionListeners.iterator(); i.hasNext();) {
                        ActionListener l = (ActionListener) i.next();
                        l.actionPerformed(new ActionEvent(this, 0, DOBLE_CLICK));
                    }
                }
            }
        });
        
        bienesBorradosJScrollPane.setViewportView(bienesBorradosJTable);
        
      
    }*/
    
    
    /**
     * Inicializa la tabla bienes revertibles
     */
    private void getTableBienesRevertibles(){
    	bienesRevertiblesJTable = new BienTableRenderer(BIENES_REVERTIBLES_COLUMNA_REVISION_EXPIRADA);
    	 
        tableSortedBienesRevertibles= new TableSorted(bienesRevertiblesJTableModel);
        bienesRevertiblesJTableModel.setTableSorted(tableSortedBienesRevertibles);
        tableSortedBienesRevertibles.setTableHeader(bienesRevertiblesJTable.getTableHeader());
        tableSortedBienesRevertibles.setColumnComparator(NumInventario.class, NumInventario.NUM_INVENTARIO_COMPARATOR);
        
        //La columna 2 y 3 son las fechas de alta y modificacion
        try {
        	tableSortedBienesRevertibles.setColumnComparatorByColumn(2, TimeStampComparator.DATE_COMPARATOR);
        	tableSortedBienesRevertibles.setColumnComparatorByColumn(3,TimeStampComparator.DATE_COMPARATOR);
		} catch (Exception e) {
		}
        
        /*Tabla bienes revertibles*/
        bienesRevertiblesJTable.setModel(tableSortedBienesRevertibles);
        bienesRevertiblesJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bienesRevertiblesJTable.setCellSelectionEnabled(false);
        bienesRevertiblesJTable.setColumnSelectionAllowed(false);
        bienesRevertiblesJTable.setRowSelectionAllowed(true);
        bienesRevertiblesJTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS/*JTable.AUTO_RESIZE_OFF*/);
        bienesRevertiblesJTable.getTableHeader().setReorderingAllowed(false);
        setInvisible(bienesRevertiblesJTableModel.getColumnCount()-1,bienesRevertiblesJTable);
        setInvisible(bienesRevertiblesJTableModel.getColumnCount()-2,bienesRevertiblesJTable);
        
        bienesRevertiblesJTableModel.setTable(bienesRevertiblesJTable);

        // Annadimos el editor CheckBox de registro marcado como borrado en BD en la primera columna de la tabla
        TableColumn col= bienesRevertiblesJTable.getColumnModel().getColumn(0);
        col.setCellEditor(new com.geopista.app.licencias.utilidades.CheckBoxTableEditor(new JCheckBox()));
        col.setCellRenderer(new com.geopista.app.licencias.utilidades.CheckBoxRenderer());
        /** al ser una check-box dejamos el espacio suficiente impidiendo que la columna se pueda hacer mas grande */
        col.setMinWidth(50);
        col.setMaxWidth(50);
        col.setPreferredWidth(50);
        
        /** Re-Ordenacion de la tabla - repintado del bloqueo */
        ((TableSorted)bienesRevertiblesJTable.getModel()).getTableHeader().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                reColorearBloqueoBR();
            }
        });
        ((TableSorted)bienesRevertiblesJTable.getModel()).getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reColorearBloqueoBR();
            }
        });

    	bienesRevertiblesJScrollPanel.setViewportView(bienesRevertiblesJTable);
    	bienesRevertiblesJTable.addMouseListener(new java.awt.event.MouseAdapter() {
             public void mouseReleased(java.awt.event.MouseEvent evt) {
             	fireActionPerformed(Const.SUPERPATRON_REVERTIBLES);
             }
             public void mouseClicked(java.awt.event.MouseEvent evt){
            	 reColorearBloqueoBR();
             	if(evt.getClickCount() == 2) {
             		getBienSeleccionado();
             		for (Iterator <ActionListener>i = actionListeners.iterator(); i.hasNext();) {
                         ActionListener l = (ActionListener) i.next();
                         l.actionPerformed(new ActionEvent(this, 0, DOBLE_CLICK));
                     }
                 }
             }
         });
    }
    
    /**
     * Inicializa la tabla lotes
     */
    private void getTableLotes(){
    	lotesJTable = new JTable();
    	 
        tableSortedLotes= new TableSorted(lotesJTableModel);
        lotesJTableModel.setTableSorted(tableSortedLotes);
        tableSortedLotes.setTableHeader(lotesJTable.getTableHeader());

        /*Tabla lotes*/
        lotesJTable.setModel(tableSortedLotes);
        lotesJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lotesJTable.setCellSelectionEnabled(false);
        lotesJTable.setColumnSelectionAllowed(false);
        lotesJTable.setRowSelectionAllowed(true);
        lotesJTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS/*JTable.AUTO_RESIZE_OFF*/);
        lotesJTable.getTableHeader().setReorderingAllowed(false);
        setInvisible(lotesJTableModel.getColumnCount()-1,lotesJTable);
        lotesJTableModel.setTable(lotesJTable);
        
     // Annadimos el editor CheckBox de registro marcado como borrado en BD en la primera columna de la tabla
        TableColumn col= lotesJTable.getColumnModel().getColumn(0);
        col.setCellEditor(new com.geopista.app.licencias.utilidades.CheckBoxTableEditor(new JCheckBox()));
        col.setCellRenderer(new com.geopista.app.licencias.utilidades.CheckBoxRenderer());
        /** al ser una check-box dejamos el espacio suficiente impidiendo que la columna se pueda hacer mas grande */
        col.setMinWidth(50);
        col.setMaxWidth(50);
        col.setPreferredWidth(50);
    	lotesJScrollPanel.setViewportView(lotesJTable);
    	lotesJTable.addMouseListener(new java.awt.event.MouseAdapter() {
             public void mouseReleased(java.awt.event.MouseEvent evt) {
             	fireActionPerformed(Const.SUPERPATRON_LOTES);
             }
             public void mouseClicked(java.awt.event.MouseEvent evt){
            	 seleccionarLotes();
             	if(evt.getClickCount() == 2) {
             		getBienSeleccionado();
             		for (Iterator <ActionListener>i = actionListeners.iterator(); i.hasNext();) {
                         ActionListener l = (ActionListener) i.next();
                         l.actionPerformed(new ActionEvent(this, 0, DOBLE_CLICK));
                     }
                 }
             }
         });
    }
    /**
     * Método que hace un columna de la tabla no visible
     */
    private void setInvisible(int column, JTable jTable){
        /** columna hidden no visible */
        TableColumn col= jTable.getColumnModel().getColumn(column);
        col.setResizable(false);
        col.setWidth(0);
        col.setMaxWidth(0);
        col.setMinWidth(0);
        col.setPreferredWidth(0);
    }

    /**
     * Método que inicializa las cabeceras de la tabla que muestra la lista de bienes
     */
    private void initHeadersJTable(boolean seleccionable){
		this.bienesJTableModel= new InventarioTableModel(new String[]{(seleccionable?aplicacion.getI18nString("inventario.bienes.columna-1"):aplicacion.getI18nString("inventario.bienes.columna0")),
                                                             aplicacion.getI18nString("inventario.bienes.columna1"),
                                                             aplicacion.getI18nString("inventario.bienes.columna2"),
                                                             aplicacion.getI18nString("inventario.bienes.columna3"),
                                                             aplicacion.getI18nString("inventario.bienes.columna4"),
                                                             aplicacion.getI18nString("inventario.bienes.columna5"),
                                                             //aplicacion.getI18nString("inventario.bienes.columna6"), 
                                                             "HIDDEN", "HIDDEN"}, 
                                                             new boolean[]{seleccionable, false, false, false, false, false, false, false, false}, locale, seleccionable);
		
		/*this.bienesBorradosJTableModel= new InventarioTableModel(new String[]{(seleccionable?aplicacion.getI18nString("inventario.bienes.columna-1"):aplicacion.getI18nString("inventario.bienes.columna0")),
                aplicacion.getI18nString("inventario.bienes.columna1"),
                aplicacion.getI18nString("inventario.bienes.columna2"),
                aplicacion.getI18nString("inventario.bienes.columna3"),
                aplicacion.getI18nString("inventario.bienes.columna4"),
                aplicacion.getI18nString("inventario.bienes.columna5"),
                //aplicacion.getI18nString("inventario.bienes.columna6"), 
                "HIDDEN", "HIDDEN"}, 
                new boolean[]{seleccionable, false, false, false, false, false, false, false, false}, locale, seleccionable);*/

    }

    /**
     * Método que inicializa las cabeceras de la tabla que muestra la lista de bienes
     */
    private void initHeadersJTableBienRevertible(){
    	this.bienesRevertiblesJTableModel= new InventarioTableModel(new String[]{aplicacion.getI18nString("inventario.bienes.columna0"),
    											aplicacion.getI18nString("inventario.bienesrevertibles.numinventario"),
    											aplicacion.getI18nString("inventario.bienesrevertibles.fechaalta"),
    											aplicacion.getI18nString("inventario.bienesrevertibles.fechamodificacion"),
    											aplicacion.getI18nString("inventario.bienesrevertibles.poseedor"),
    											aplicacion.getI18nString("inventario.bienesrevertibles.tituloposesion"), "HIDDEN", "HIDDEN"}, 
                                                                 new boolean[]{false, false, false, false, false, false, false, false, false,false}, locale,false);
    }
    
    /**
     * Método que inicializa las cabeceras de la tabla que muestra la lista de bienes
     */
    private void initHeadersJTableLotes(){
    	this.lotesJTableModel= new InventarioTableModel(new String[]{aplicacion.getI18nString("inventario.bienes.columna0"),
    											aplicacion.getI18nString("inventario.bienes.columna2"),
    											aplicacion.getI18nString("inventario.bienes.columna3"),
    											aplicacion.getI18nString("inventario.lote.nombre"),
    											aplicacion.getI18nString("inventario.lote.seguro"), "HIDDEN"}, 
                                                                 new boolean[]{false,  false, false, false, false, false}, locale,false);
    }
    private void initHeadersJTableVersion(){
    	this.versionesJTableModel= new InventarioTableModel(new String[]{aplicacion.getI18nString("inventario.bienes.columna1"),
                                                                 aplicacion.getI18nString("inventario.bienes.columna2"),
                                                                 aplicacion.getI18nString("inventario.bienes.autor"),
                                                                 aplicacion.getI18nString("inventario.bienes.revision"),"HIDDEN",
    															 "HIDDEN"}, new boolean[]{false, false, false, false, false,false}, locale,false);
    }
    private void initHeadersJTableVersionBR(){
    	this.versionesBRJTableModel= new InventarioTableModel(new String[]{aplicacion.getI18nString("inventario.bienesrevertibles.numinventario"),
                                                                 aplicacion.getI18nString("inventario.bienes.columna2"),
                                                                 aplicacion.getI18nString("inventario.bienes.autor"),
                                                                 aplicacion.getI18nString("inventario.bienes.revision"),"HIDDEN",
    															 "HIDDEN"}, new boolean[]{false, false, false, false, false,false}, locale,false);
    }
    private void bienesJTableMouseReleased() {
        getBienSeleccionado();
        fireActionPerformed(Const.SUPERPATRON_BIENES);
    }

    /**
     * Método que recoge el bien seleccionado de la tabla
     * @return el bien seleccionado de la tabla 
     */
    public Object getBienSeleccionado(){
    	//TODO: devuelve el bien, pero si ha sido modificado, devuelve la version anterior

    	if (panelPestañasBienes.getSelectedIndex()==0){
    		selectedRow= bienesJTable.getSelectedRow();
    		if (selectedRow == -1) return null;
        	bienesJTableModel.setTableSorted(bienesJTableModel.getTableSorted());
        	bienesJTableModel.setRows(bienesJTableModel.getRows());
        	return bienesJTableModel.getObjetAt(selectedRow);
    	}
    	if (panelPestañasBienes.getSelectedIndex()==1){
            selectedRow= this.versionJTable.getSelectedRow();
	        if (selectedRow == -1) 	return null;
	        versionesJTableModel.setTableSorted(versionesJTableModel.getTableSorted());
	        versionesJTableModel.setRows(versionesJTableModel.getRows());
	        return versionesJTableModel.getObjetAt(selectedRow);
	     }
    	
//        selectedRow= bienesJTable.getSelectedRow();
//        if (selectedRow == -1){ 
//            selectedRow= this.versionJTable.getSelectedRow();
//	        if (selectedRow == -1) 
//	        	return null;
//	        else{
//	        	versionesJTableModel.setTableSorted(versionesJTableModel.getTableSorted());
//	        	versionesJTableModel.setRows(versionesJTableModel.getRows());
//	        	return versionesJTableModel.getObjetAt(selectedRow);
//	        }
//        }else{
//        	bienesJTableModel.setTableSorted(bienesJTableModel.getTableSorted());
//        	bienesJTableModel.setRows(bienesJTableModel.getRows());
//        	return bienesJTableModel.getObjetAt(selectedRow);
//        }
        
//    	if (panelPestañasBienes.getSelectedIndex()==0){
//    		selectedRow= bienesJTable.getSelectedRow();
//    		if (selectedRow == -1) return null;
//        	bienesJTableModel.setTableSorted(bienesJTableModel.getTableSorted());
//        	bienesJTableModel.setRows(bienesJTableModel.getRows());
//        	return bienesJTableModel.getObjetAt(selectedRow);
//    	}
//    	/*if (panelPestañasBienes.getSelectedIndex()==1){
//    		selectedRow= bienesBorradosJTable.getSelectedRow();
//    		if (selectedRow == -1) return null;
//        	bienesBorradosJTableModel.setTableSorted(bienesBorradosJTableModel.getTableSorted());
//        	bienesBorradosJTableModel.setRows(bienesBorradosJTableModel.getRows());
//        	return bienesBorradosJTableModel.getObjetAt(selectedRow);
//    	}*/
//    	if (panelPestañasBienes.getSelectedIndex()==1){
//            selectedRow= this.versionJTable.getSelectedRow();
//	        if (selectedRow == -1) 	return null;
//	        versionesJTableModel.setTableSorted(versionesJTableModel.getTableSorted());
//	        versionesJTableModel.setRows(versionesJTableModel.getRows());
//	        return versionesJTableModel.getObjetAt(selectedRow);
//	     }
//        return null;
    
        return null;
    }
    
    /**
     * Método que recoge el bien seleccionado de la tabla
     * @return el bien seleccionado de la tabla 
     */
    public BienRevertible getBienRevertibleSeleccionado(){
    	if (panelPestañasBienes.getSelectedIndex()==0){
    		selectedRow= bienesRevertiblesJTable.getSelectedRow();
    		if (selectedRow == -1)return null; 
    	    return (BienRevertible)bienesRevertiblesJTableModel.getObjetAt(selectedRow);
    	}
    	if (panelPestañasBienes.getSelectedIndex()==1){
            selectedRow= this.versionBRJTable.getSelectedRow();
	        if (selectedRow == -1) 	return null;
	       	versionesBRJTableModel.setTableSorted(versionesBRJTableModel.getTableSorted());
	        versionesBRJTableModel.setRows(versionesBRJTableModel.getRows());
	        return (BienRevertible)versionesBRJTableModel.getObjetAt(selectedRow);
	    }
    	return null;
    }
    
    /**
     * Método que recoge el lote seleccionado de la tabla
     * @return el bien seleccionado de la tabla 
     */
    public Lote getLoteSeleccionado(){
    	int selectedRow= lotesJTable.getSelectedRow();
        if (selectedRow == -1) return null;
        return (Lote)lotesJTableModel.getObjetAt(selectedRow);
    }
    
    /**
     * 
     */
    public void seleccionarBienRevertible(BienRevertible bienRevertible){
    	try{
	    	if (bienRevertible==null || bienRevertible.getId()==null) return;
	    	for (int i=0; i<=tableSortedBienesRevertibles.getRowCount();i++ ){
	    		
	    		String idRow=null;
	    		try{idRow= new String(tableSortedBienesRevertibles.getValueAt(i, tableSortedBienesRevertibles.getColumnCount()-1).toString());}catch(Exception ex){}
	    		if ((bienRevertible.getId()+"_"+bienRevertible.getRevisionActual()).equals(idRow)){
	    			bienesRevertiblesJTable.setRowSelectionInterval(i, i);
	    			return;
	    		}
	    	}
	    	//Sino encuentro el id y la versión busco el id
	    	for (int i=0; i<=tableSortedBienesRevertibles.getRowCount();i++ ){
	    		
	    		String idRow=null;
	    		try{idRow= new String(tableSortedBienesRevertibles.getValueAt(i, tableSortedBienesRevertibles.getColumnCount()-1).toString());}catch(Exception ex){}
	    		if (idRow.startsWith(bienRevertible.getId()+"_")){
	    			bienesRevertiblesJTable.setRowSelectionInterval(i, i);
	    			return;
	    		}
	    	}
    	}catch(Exception ex){}
    }
    /**
     * Selecciona el lote de la tabla
     * @param lote
     */
    public void seleccionarLote(Lote lote){
    	try{
    		if (lote==null || lote.getId_lote()==null) return;
    		for (int i=0; i<=tableSortedLotes.getRowCount();i++ ){
    			Long idRow=null;
    			try{idRow= new Long(tableSortedLotes.getValueAt(i, tableSortedLotes.getColumnCount()-1).toString());}catch(Exception ex){}
    			if (lote.getId_lote().equals(idRow)){
    				lotesJTable.setRowSelectionInterval(i, i);
    			}
    		}
    	}catch(Exception ex){}
    }
    /**
     * Selecciona el bien de la tabla
     * @param lote
     */
    public BienBean seleccionarBien(BienBean bien){
    	try{
    		if (bien==null || bien.getId()==-1) return null;
    		for (int i=0; i<=tableSorted.getRowCount();i++ ){
    			BienBean bienBuscado = (BienBean)bienesJTableModel.getObjetAt(i);
    			if (bienBuscado.getId()==bien.getId()){
    				bienesJTable.setRowSelectionInterval(i, i);
    				return bienBuscado;
    			}
    			/*String idRow=null;
    			try{idRow= tableSorted.getValueAt(i, tableSorted.getColumnCount()-1).toString();}catch(Exception ex){}
    			if (idRow!=null && idRow.indexOf(new Long(bien.getId()).toString()) ==0){
    				bienesJTable.setRowSelectionInterval(i, i);
    			}*/
    		}
    	}catch(Exception ex){}
    	return null;
    }
    /***
     * Devuelve la lista de bienes de la tabla
     * @return
     */
     public Collection<BienBean> getListaBienes(){
    	 if (bienesJTableModel==null || bienesJTableModel.getRows()==null || bienesJTableModel.getRows().size()==0) return null;
    	 Vector <BienBean> bienes = new Vector();
    	 for (Enumeration e=bienesJTableModel.getRows().elements();e.hasMoreElements();)
    		 bienes.add((BienBean)e.nextElement());
    	 return bienes; 
     }
     /***
      * Devuelve la lista de lotes de la tabla
      * @return
      */
      public Collection<Lote> getLotes(){
     	 if (lotesJTableModel==null || lotesJTableModel.getRows()==null || lotesJTableModel.getRows().size()==0) return null;
     	 Vector <Lote> lotes = new Vector();
     	 for (Enumeration e=lotesJTableModel.getRows().elements();e.hasMoreElements();)
     		 lotes.add((Lote)e.nextElement());
     	 return lotes; 
      }
      
      /***
       * Devuelve la lista de bienes Reversiles de la tabla
       * @return
       */
       public Collection<BienRevertible> getBienesReversibles(){
      	 if (bienesRevertiblesJTableModel==null || bienesRevertiblesJTableModel.getRows()==null || bienesRevertiblesJTableModel.getRows().size()==0) return null;
      	 Vector <BienRevertible> bienesRevertibles = new Vector();
      	 for (Enumeration e=bienesRevertiblesJTableModel.getRows().elements();e.hasMoreElements();)
      		bienesRevertibles.add((BienRevertible)e.nextElement());
      	 return bienesRevertibles; 
       }


    /**
     * Método que desmarca la fila seleccionada en la tabla
     */
    public void clearSelection(JTable jTable){
    	jTable.clearSelection();
    }
    public void clearSelection(){
    	bienesJTable.clearSelection();
    }

    /**
     * Método que carga en la tabla una lista de bienes
     * @param c Collection de bienes a cargar
     */
    public void loadListaBienes(Collection c) throws Exception{
    	int numBienes=0;
        Collection cRet= new ArrayList();
        //Collection cRetBorrados= new ArrayList();
        Collection cRetVersion= new ArrayList();
        if (c != null){
	    	Object[] arrayBienes = c.toArray();
	    	Arrays.sort(arrayBienes,new BienComparatorByNumInventario());
	    	int  n = arrayBienes.length;
	    	/*if (arrayBienes.length>MAX_REGISTROS){
	    		JOptionPane.showMessageDialog(this, "Demasiados objetos en la lista se mostraran los "+MAX_REGISTROS+" primeros\n"+aplicacion.getI18nString("inventario.mensajes.tag2"));
				n=MAX_REGISTROS;
	    	}*/
	    
	    	for (int i=0;i<n;i++){
	    		BienBean bien = (BienBean)arrayBienes[i];
	    		//logger.info("BIEN:"+bien.getNumInventario());
	    		if (!bien.isVersionado()){
	    			//if ((bien.isBorrado()) || (bien.isEliminado()))
	    			//	cRetBorrados.add(bien);	    			    		
	    			//else 
	    				cRet.add(bien);
	    			numBienes++;
	    	
	    		}
	    		else{
	    			//logger.info("El bien no esta versionado");
	    		}
	   			cRetVersion.add(bien);
	    	}
        }
        bienesJTableModel.setModelData(cRet);
        //bienesBorradosJTableModel.setModelData(cRetBorrados);
        versionesJTableModel.setModelDataVersion(cRetVersion);
        reColorearBloqueo();
        
        int num = 0;
        if (c!=null)
        	num = numBienes;
        
        contadorBienesJLabel.setText(aplicacion.getI18nString("inventario.bienes.contador")
        		+ " " + num);
        
        
        //Habilitamos los botones de adelante y atras.
        if (numBienes>0){
	        adelanteJButton.setEnabled(true);
	        atrasJButton.setEnabled(true);
        }else{
	        adelanteJButton.setEnabled(false);        	
        }
        
        if (actualOffset==0){
        	atrasJButton.setEnabled(false);
        }
        else{
        	atrasJButton.setEnabled(true);
        }
    }
    
    /**
     * Método que carga en la tabla una lista de bienes
     * @param c Collection de bienes a cargar
     */
    public void loadListaBienesRevertibles(Collection c) throws Exception{
    	int numBienes=0;
        Collection cRet= new ArrayList();
        Collection cRetVersion= new ArrayList();
        if (c != null){
	    	Object[] arrayBienes = c.toArray();
	    	Arrays.sort(arrayBienes,new BienComparatorByNumInventario());
	    	int n = arrayBienes.length;
	    	/*if (arrayBienes.length>MAX_REGISTROS){
	    		JOptionPane.showMessageDialog(this, "Demasiados objetos en la lista se mostraran los "+MAX_REGISTROS+"primeros\n");
				n=MAX_REGISTROS;
	    	}*/
	    	
	    	for (int i=0;i<n;i++){
	    		BienRevertible bien = (BienRevertible)arrayBienes[i];
	    		if (!bien.isVersionado()){
	    			cRet.add(bien);
	    			numBienes++;
	    		}
	   			cRetVersion.add(bien);
	    	}
        }
        bienesRevertiblesJTableModel.setModelData(cRet);
        versionesBRJTableModel.setModelDataVersionBR(cRetVersion);
       // reColorearBloqueo();
        
        int num = 0;
        if (c!=null)
        	num = numBienes;
        
        contadorBienesJLabel.setText(aplicacion.getI18nString("inventario.bienes.contador")
        		+ " " + num);
        

        //Habilitamos los botones de adelante y atras.
        if (numBienes>0){
	        adelanteJButton.setEnabled(true);
	        atrasJButton.setEnabled(true);
        }else{
	        adelanteJButton.setEnabled(false);        	
        }
        
        if (actualOffset==0){
        	atrasJButton.setEnabled(false);
        }
        else{
        	atrasJButton.setEnabled(true);
        }
    
    }
    /**
     * Método que carga en la tabla una lista de lotes
     * @param c Collection de lotes a cargar
     */
    public void loadListaLotes(Collection<Lote> c) throws Exception{
    	int num=0;
    	int numBienes=0;
        Collection<Lote> cRet= new ArrayList<Lote>();
        if (c != null){
	    	Object[] arrayLotes= c.toArray();
	    	Arrays.sort(arrayLotes,new LoteComparator());
	    	int n = arrayLotes.length;
	    	/*if (arrayLotes.length>MAX_REGISTROS){
	    		JOptionPane.showMessageDialog(this, "Demasiados objetos en la lista se mostraran los "+MAX_REGISTROS+"primeros\n");
				n=MAX_REGISTROS;
	    	}*/
	    	
	    	for (int i=0;i<n;i++){
	    		Lote lote = (Lote)arrayLotes[i];
	    			cRet.add(lote);
	    			num++;
	    	}
        }
        lotesJTableModel.setModelData(cRet);

        if (c!=null)
        	num = numBienes;
        
        contadorBienesJLabel.setText(aplicacion.getI18nString("inventario.lote.numero.lotes")
        		+ " " + num);
        

        //Habilitamos los botones de adelante y atras.
        if (numBienes>0){
	        adelanteJButton.setEnabled(true);
	        atrasJButton.setEnabled(true);
        }else{
	        adelanteJButton.setEnabled(false);        	
        }
        
        if (actualOffset==0){
        	atrasJButton.setEnabled(false);
        }
        else{
        	atrasJButton.setEnabled(true);
        }
    }

    public void clearTable(){

    	bienesJTableModel.setModelData(new ArrayList());    	
    	bienesJTableModel.getTableSorted().sortingStatusChanged();
    	
    	bienesRevertiblesJTableModel.setModelData(new ArrayList());    	
    	bienesRevertiblesJTableModel.getTableSorted().sortingStatusChanged();
    	
    	lotesJTableModel.setModelData(new ArrayList());    	
    	lotesJTableModel.getTableSorted().sortingStatusChanged();
    	

    }

    /**
     * Método que añade un bien a la tabla
     * @param obj Bien a cargar
     */
    public void addBienTabla(Object obj){
        bienesJTableModel.annadirRow(obj);
    }

    /**
     * Método que actualiza un bien en la tabla
     * @param obj Bien a actualizar
     */
    public void updateBienTabla(Object obj){
        bienesJTableModel.actualizarRow(selectedRow, obj);
    }

    /**
     * Método que borra un bien de la tabla
     */
    public void deleteBienTabla(String key){
        bienesJTableModel.deleteRow(selectedRow, key);
    }
    
    /**
     * Método que borra un bien de la tabla
     */
    public void deleteBienTabla(BienBean bien){
        bienesJTableModel.deleteRow(selectedRow, bien);
    }
    
    /**
     * Método que borra un bien de la tabla
     */
    public void deleteBienRevertibleTabla(String key){
    	int selectedRow =bienesRevertiblesJTable.getSelectedRow();
        bienesRevertiblesJTableModel.deleteRow(selectedRow, key);
    }
    
    /**
     * Método que borra un lote de la tabla
     */
    public void deleteLoteTabla(String key){
    	int selectedRow =lotesJTable.getSelectedRow();
        lotesJTableModel.deleteRow(selectedRow, key);
    }
   
   
    public void addActionListener(ActionListener l) {
        this.actionListeners.add(l);
    }

    public void removeActionListener(ActionListener l) {
        this.actionListeners.remove(l);
    }

    private void fireActionPerformed(String source) {
        for (Iterator i = actionListeners.iterator(); i.hasNext();) {
            ActionListener l = (ActionListener) i.next();
            l.actionPerformed(new ActionEvent(this, 0, source));
        }
    }

    /**
     * Método que renombra los componentes del panel segun el locale
     */
    public void renombrarComponentes(){
        try{setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("inventario.bienes.tag1")));}catch(Exception e){}
        /** Headers de la tabla de bienes */
        int i =0;
        TableColumn tableColumn =null;
             	
        tableColumn= bienesJTable.getColumnModel().getColumn(i++);
        try{tableColumn.setHeaderValue((showSeleccionar?"Seleccionar":aplicacion.getI18nString("inventario.bienes.columna0")));}catch(Exception e){}
        tableColumn= bienesJTable.getColumnModel().getColumn(i++);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString("inventario.bienes.columna1"));}catch(Exception e){}
        tableColumn= bienesJTable.getColumnModel().getColumn(i++);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString("inventario.bienes.columna2"));}catch(Exception e){}
        tableColumn= bienesJTable.getColumnModel().getColumn(i++);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString("inventario.bienes.columna3"));}catch(Exception e){}
        tableColumn= bienesJTable.getColumnModel().getColumn(i++);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString("inventario.bienes.columna4"));}catch(Exception e){}
        tableColumn= bienesJTable.getColumnModel().getColumn(i++);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString("inventario.bienes.columna5"));}catch(Exception e){}
        //tableColumn= bienesJTable.getColumnModel().getColumn(i++);
        //try{tableColumn.setHeaderValue(aplicacion.getI18nString("inventario.bienes.columna6"));}catch(Exception e){}
        

        tableColumn=bienesRevertiblesJTable.getColumnModel().getColumn(0);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString("inventario.bienes.columna0"));}catch (Exception e){}
        tableColumn=bienesRevertiblesJTable.getColumnModel().getColumn(1);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString("inventario.bienesrevertibles.numinventario"));}catch (Exception e){}
		tableColumn=bienesRevertiblesJTable.getColumnModel().getColumn(2);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString("inventario.bienesrevertibles.fechainicio"));}catch (Exception e){}
        tableColumn=bienesRevertiblesJTable.getColumnModel().getColumn(3);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString("inventario.bienesrevertibles.fechavencimiento"));}catch (Exception e){}
        tableColumn=bienesRevertiblesJTable.getColumnModel().getColumn(4);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString("inventario.bienesrevertibles.poseedor"));}catch (Exception e){}
        tableColumn=bienesRevertiblesJTable.getColumnModel().getColumn(5);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString("inventario.bienesrevertibles.tituloposesion"));}catch (Exception e){}
        
        tableColumn=lotesJTable.getColumnModel().getColumn(0);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString("inventario.bienes.columna0"));}catch (Exception e){}
        tableColumn=lotesJTable.getColumnModel().getColumn(1);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString("inventario.bienes.columna2"));}catch (Exception e){}
		tableColumn=lotesJTable.getColumnModel().getColumn(2);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString("inventario.bienes.columna3"));}catch (Exception e){}
        tableColumn=lotesJTable.getColumnModel().getColumn(3);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString("inventario.lote.nombre"));}catch (Exception e){}
        tableColumn=lotesJTable.getColumnModel().getColumn(4);
        try{tableColumn.setHeaderValue(aplicacion.getI18nString("inventario.lote.seguro"));}catch (Exception e){}
    }

      
    private void reColorearBloqueo(){
          Vector redRows= new Vector();
          for (int i=0; i < tableSorted.getRowCount(); i++){
              BienBean bb= (BienBean)((InventarioTableModel)tableSorted.getTableModel()).getObjetAt(i);
              if (bb != null && bb.getBloqueado()!=null && !bb.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                  redRows.add(new Integer(i));
              }else redRows.add(null);
          }

        /** Re-Pintamos en rojo, los bienes que esten bloqueados */
          /* la columna 0 es un CheckBoxRenderer y no se puede tratar igual */
        for ( int j=1; j < bienesJTableModel.getColumnCount(); j++){
            // Annadimos a cada columna el renderer ColorTableCellRenderer
            TableColumn col= bienesJTable.getColumnModel().getColumn(j);
            com.geopista.app.licencias.utilidades.ColorTableCellRenderer colorTableCellRenderer= new com.geopista.app.licencias.utilidades.ColorTableCellRenderer(redRows);
            col.setCellRenderer(colorTableCellRenderer);
        }

    }
    
    private void reColorearBloqueoBR(){
        Vector redRows= new Vector();
        for (int i=0; i < tableSorted.getRowCount(); i++){
            BienBean bb= (BienBean)((InventarioTableModel)tableSorted.getTableModel()).getObjetAt(i);
            if (bb != null && bb.getBloqueado()!=null && !bb.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                redRows.add(new Integer(i));
            }else redRows.add(null);
        }

      /** Re-Pintamos en rojo, los bienes que esten bloqueados */
        /* la columna 0 es un CheckBoxRenderer y no se puede tratar igual */
      for ( int j=1; j < bienesRevertiblesJTableModel.getColumnCount(); j++){
          // Annadimos a cada columna el renderer ColorTableCellRenderer
          TableColumn col= bienesRevertiblesJTable.getColumnModel().getColumn(j);
          com.geopista.app.licencias.utilidades.ColorTableCellRenderer colorTableCellRenderer= new com.geopista.app.licencias.utilidades.ColorTableCellRenderer(redRows);
          col.setCellRenderer(colorTableCellRenderer);
      }

  }
    
    
    private void seleccionarVersiones(){
      for (int j=0; j < versionJTable.getColumnCount()-1; j++){
          // Annadimos a cada columna el renderer ColorTableCellRenderer
          TableColumn col= versionJTable.getColumnModel().getColumn(j);
          ColorTableVersionesCellRenderer colorTableCellRenderer= new ColorTableVersionesCellRenderer();
          col.setCellRenderer(colorTableCellRenderer);
      }

  }
    
    private void seleccionarRevertibles(){
        for (int j=0; j < versionBRJTable.getColumnCount()-1; j++){
            // Annadimos a cada columna el renderer ColorTableCellRenderer
            TableColumn col= versionBRJTable.getColumnModel().getColumn(j);
            ColorTableVersionesCellRenderer colorTableCellRenderer= new ColorTableVersionesCellRenderer();
            col.setCellRenderer(colorTableCellRenderer);
        }
    }
    

    private void seleccionarLotes(){
        for (int j=1; j < lotesJTable.getColumnCount()-1; j++){
            // Annadimos a cada columna el renderer ColorTableCellRenderer
            TableColumn col= lotesJTable.getColumnModel().getColumn(j);
            ColorTableVersionesCellRenderer colorTableCellRenderer= new ColorTableVersionesCellRenderer();
            col.setCellRenderer(colorTableCellRenderer);
        }
    }


    private void getTableVersion(){
        versionJTable = new BienTableRenderer(4); 

        this.initHeadersJTableVersion();
	    tableSortedVersion= new TableSorted(versionesJTableModel);
	    versionesJTableModel.setTableSorted(tableSortedVersion);
	    tableSortedVersion.setTableHeader(versionJTable.getTableHeader());
	    tableSortedVersion.setColumnComparator(NumInventario.class, NumInventario.NUM_INVENTARIO_COMPARATOR);
	    
		  //La columna 1 es la fecha
        try {
        	tableSortedVersion.setColumnComparatorByColumn(1, TimeStampComparator.DATE_COMPARATOR);
		} catch (Exception e) {
		}
        
	    versionJTable.setModel(tableSortedVersion);
	    versionJTable.setRowSelectionAllowed(true);
	    versionJTable.setCellSelectionEnabled(true);
	    versionJTable.setColumnSelectionAllowed(false);
	    versionJTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS/*JTable.AUTO_RESIZE_OFF*/);
	    versionJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    versionJTable.getTableHeader().setReorderingAllowed(false);
	    setInvisible(versionesJTableModel.getColumnCount()-1,versionJTable);
	    setInvisible(versionesJTableModel.getColumnCount()-2,versionJTable);
	
	    versionesJTableModel.setTable(versionJTable);
	    
        versionesBienesJPanel = new JScrollPane();
        versionesBienesJPanel.setViewportView(versionJTable);
        versionJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
            	clearSelection(bienesJTable);
                bienesJTableMouseReleased();
            }
            public void mouseClicked(java.awt.event.MouseEvent evt){
            	seleccionarVersiones();
            	if(evt.getClickCount() == 2) {
            		getBienSeleccionado();
            		for (Iterator <ActionListener>i = actionListeners.iterator(); i.hasNext();) {
                        ActionListener l = (ActionListener) i.next();
                        l.actionPerformed(new ActionEvent(this, 0, DOBLE_CLICK));
                    }
                }
            }
        });
    }
    
    private void getTableVersionBR(){
        versionBRJTable = new BienTableRenderer(4); 

        this.initHeadersJTableVersionBR();
	    tableSortedVersionBR= new TableSorted(versionesBRJTableModel);
	    versionesBRJTableModel.setTableSorted(tableSortedVersionBR);
	    tableSortedVersionBR.setTableHeader(versionBRJTable.getTableHeader());
	    tableSortedVersionBR.setColumnComparator(NumInventario.class, NumInventario.NUM_INVENTARIO_COMPARATOR);
	  //La columna 2 y 3 son las fechas de alta y modificacion
        try {
        	tableSortedVersionBR.setColumnComparatorByColumn(2, TimeStampComparator.DATE_COMPARATOR);
        	tableSortedVersionBR.setColumnComparatorByColumn(3,TimeStampComparator.DATE_COMPARATOR);
		} catch (Exception e) {
		}
        
		versionBRJTable.setModel(tableSortedVersionBR);
	    versionBRJTable.setRowSelectionAllowed(true);
	    versionBRJTable.setCellSelectionEnabled(true);
	    versionBRJTable.setColumnSelectionAllowed(false);
	    versionBRJTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS/*JTable.AUTO_RESIZE_OFF*/);
	    versionBRJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    versionBRJTable.getTableHeader().setReorderingAllowed(false);
	    setInvisible(versionesBRJTableModel.getColumnCount()-1,versionBRJTable);
	    setInvisible(versionesBRJTableModel.getColumnCount()-2,versionBRJTable);
	
	    versionesBRJTableModel.setTable(versionBRJTable);
	    
        versionesBRBienesJPanel = new JScrollPane();
        versionesBRBienesJPanel.setViewportView(versionBRJTable);
        versionBRJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
            	clearSelection(bienesJTable);
            	getBienRevertibleSeleccionado();
                fireActionPerformed(Const.SUPERPATRON_REVERTIBLES);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt){
            	seleccionarRevertibles();
            	if(evt.getClickCount() == 2) {
            		getBienRevertibleSeleccionado();
            		for (Iterator <ActionListener>i = actionListeners.iterator(); i.hasNext();) {
                        ActionListener l = (ActionListener) i.next();
                        l.actionPerformed(new ActionEvent(this, 0, DOBLE_CLICK));
                    }
                }
            }
        });
    }
    /**
     * Compruebo si el bien está dado de baja o no
     */
    private boolean estaActivoBien(long revisionExpirada){
    	return revisionExpirada == Long.parseLong("9999999999");
    }    
    
    
    class BienComparator implements Comparator {
    	public int compare(Object o1, Object o2) {
    		if (o1 instanceof BienBean && o2 instanceof BienBean){
    			BienBean b1 = (BienBean)o1;
    			BienBean b2 = (BienBean)o2;
    			if (b1.getId()>b2.getId())
    				return 1;
    			else if (b1.getId()==b2.getId())
    				if (b1.getRevisionActual()>b2.getRevisionActual())
    					return 1;
    				else return -1;
    			else
    				return -1;
    		}
    		if (o1 instanceof BienRevertible && o2 instanceof BienRevertible){
    			BienRevertible b1 = (BienRevertible)o1;
    			BienRevertible b2 = (BienRevertible)o2;
    			if (b1.getId()>b2.getId())
    				return 1;
    			else if (b1.getId()==b2.getId())
    				if (b1.getRevisionActual()>b2.getRevisionActual())
    					return 1;
    				else return -1;
    			else
    				return -1;
    		}
    		return -1;
    	}
    }

    class LoteComparator implements Comparator {
    	public int compare(Object o1, Object o2) {
    		if (o1 instanceof Lote && o2 instanceof Lote){
    			Lote b1 = (Lote)o1;
    			Lote b2 = (Lote)o2;
    			if (b1.getId_lote()>b2.getId_lote())
    				return 1;
    			else
    				return -1;
    		}
    		return -1;
    	}
    }

    class BienComparatorByNumInventario implements Comparator {
    	public int compare(Object o1, Object o2) {
    		if (o1 instanceof BienBean && o2 instanceof BienBean){
    			BienBean b1 = (BienBean)o1;
    			BienBean b2 = (BienBean)o2;
    			String numInventario1=b1.getNumInventario();
    			String numInventario2=b2.getNumInventario();
    			
    			if (numInventario1.equals(numInventario2)){
        			Date date1=b1.getFechaVersion();
        			Date date2=b2.getFechaVersion();		

        			 if (date1 == null && date2 == null) {
                         return  0;
                     } else if (date1 == null) {
                         return -1;
                     } else if (date2 == null) {
                         return 1;
                     } 
                     else
                    	 return date1.compareTo(date2);
    			}
    			
            	String cadena1=numInventario1.replaceAll("_", ".");
            	String cadena2=numInventario2.replaceAll("_", ".");
            	
            	/* Solución rapida para que aparezcan ordenados los créditos y derechos (primero los arrendamientos, número de inventario 5.1.,
            	 *  y luego los créditos y derechos, número de inventario 5.)
            	 */
            	if ((b1 instanceof CreditoDerechoBean) && (b2 instanceof CreditoDerechoBean)){
            		cadena1 = numInventario1.replaceAll("5.1.","1.");
            		cadena2 = numInventario2.replaceAll("5.1.","1.");
            		cadena1 = cadena1.replaceAll("5.","2.");
            		cadena2 = cadena2.replaceAll("5.","2.");
            	}
            	
            	StringTokenizer stk1 = new StringTokenizer(cadena1,".");
            	StringTokenizer stk2 = new StringTokenizer(cadena2,".");
            	//StringTokenizer stk1 = new StringTokenizer(o1.toString(),".");
            	//StringTokenizer stk2 = new StringTokenizer(o2.toString(),".");
        		for (;stk1.countTokens()<stk2.countTokens()?stk1.hasMoreTokens():stk2.hasMoreTokens();){
        			String token1 = stk1.nextToken();
        			String token2 = stk2.nextToken();
        			try{
        				long num1=new Long(token1).longValue();
        				long num2=new Long(token2).longValue();
        				//logger.info("NUM1:"+num1+" vs NUM:"+num2);
        				if (num1<num2)return -1;
        				if (num1>num2)return 1;
        			}catch (Exception ex){
        				return token1.compareTo(token2);
        			}
        		}
        		//logger.info("PASAMOS");
                return stk1.countTokens()<stk2.countTokens()?-1:1;    			    	
    		}
    		if (o1 instanceof BienRevertible && o2 instanceof BienRevertible){
    			BienRevertible b1 = (BienRevertible)o1;
    			BienRevertible b2 = (BienRevertible)o2;
    			
    			String numInventario1=b1.getNumInventario();
    			String numInventario2=b2.getNumInventario();
    			
            	String cadena1=numInventario1.replaceAll("_", ".");
            	String cadena2=numInventario2.replaceAll("_", ".");
            	StringTokenizer stk1 = new StringTokenizer(cadena1,".");
            	StringTokenizer stk2 = new StringTokenizer(cadena2,".");
            	//StringTokenizer stk1 = new StringTokenizer(o1.toString(),".");
            	//StringTokenizer stk2 = new StringTokenizer(o2.toString(),".");
        		for (;stk1.countTokens()<stk2.countTokens()?stk1.hasMoreTokens():stk2.hasMoreTokens();){
        			String token1 = stk1.nextToken();
        			String token2 = stk2.nextToken();
        			try{
        				long num1=new Long(token1).longValue();
        				long num2=new Long(token2).longValue();
        				//logger.info("NUM1:"+num1+" vs NUM:"+num2);
        				if (num1<num2)return -1;
        				if (num1>num2)return 1;
        			}catch (Exception ex){
        				return token1.compareTo(token2);
        			}
        		}
        		//logger.info("PASAMOS");
                return stk1.countTokens()<stk2.countTokens()?-1:1;    
    		}
    		return -1;
    	}
    }
    
    class BienTableRenderer extends javax.swing.JTable{
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int columnRevisionExpirada; 
    	
    	BienTableRenderer(int columnRevisionExpirada){   		
    		this.columnRevisionExpirada = columnRevisionExpirada;
    	}
    	
        public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
        		Component returnComp = super.prepareRenderer(renderer, row,column);
        	 	try{
        	 		Object valor = getValueAt(row,columnRevisionExpirada);
        	 		if (valor==null) return returnComp;
        	 		String tipo= valor.toString(); 
        	 		if(estaActivoBien(Long.parseLong(tipo))){ 
        	 			returnComp.setForeground(Color.BLUE);
        	 			//returnComp.setBackground(new Color(49,196,197));
        	 		}else 
        	 			returnComp.setForeground(Color.RED); 
        	 		return returnComp;
        	 	}catch (Exception ex){
        	 		//logger.error("Se ha producido un error en el renderer de los bienes:", ex);
        			returnComp.setForeground(Color.GREEN); 
            	 	return returnComp; 
        	 	}
        	 	
        } 
    	
    }
    public void setPanelesVisibles(boolean panelBien, boolean panelBienRevertible, boolean panelLote){
    		panelPestañasBienes.removeAll();
			if (panelBienRevertible){
    			panelPestañasBienes.addTab(aplicacion.getI18nString("inventario.bienesrevertibles.lista"), bienesRevertiblesJScrollPanel);
    			panelPestañasBienes.addTab(aplicacion.getI18nString("inventario.bienes.versiones"), versionesBRBienesJPanel);
    		}
			if (panelBien){
    			panelPestañasBienes.addTab(aplicacion.getI18nString("inventario.bienes.tag1"),bienesJScrollPane);
    			//panelPestañasBienes.addTab(aplicacion.getI18nString("inventario.bienes.tag1.1"),bienesBorradosJScrollPane);
 		        panelPestañasBienes.addTab(aplicacion.getI18nString("inventario.bienes.versiones"),versionesBienesJPanel);
 		    }
			if (panelLote){
				panelPestañasBienes.addTab(aplicacion.getI18nString("inventario.lote"), lotesJScrollPanel);
		 	}

    }
    /**
     * Devuelve la lista de bienes seleccionados
     */
    public Collection getBienesSeleccionados() {
        int[] selectedRows = bienesJTable.getSelectedRows();
        if(selectedRows.length == 0)
        	return null;
        
        bienesJTableModel.setTableSorted(bienesJTableModel.getTableSorted());
        bienesJTableModel.setRows(bienesJTableModel.getRows());
        BienBean[] bienes = new BienBean[selectedRows.length];
        for(int i = 0;i < selectedRows.length;i++)
        	bienes[i] = (BienBean)bienesJTableModel.getObjetAt(selectedRows[i]);
//        return bienes;
    		return Arrays.asList(bienes);
    }
    
    /***
     * Devuelve la lista de bienes Revertibles seleccionados en la tabla
     * @return
     */
     public Collection<BienRevertible> getBienesReversiblesSeleccionados(){
    	 
    	 int[] selectedRows = bienesRevertiblesJTable.getSelectedRows();
    	 if(selectedRows.length == 0)
    		 return null;
    	 bienesRevertiblesJTableModel.setTableSorted(bienesRevertiblesJTableModel.getTableSorted());
    	 bienesRevertiblesJTableModel.setRows(bienesRevertiblesJTableModel.getRows());
    	 BienRevertible[] bienesRevertibles = new BienRevertible[selectedRows.length];
    	 for(int i = 0;i < selectedRows.length;i++)
    		 bienesRevertibles[i] = (BienRevertible)bienesRevertiblesJTableModel.getObjetAt(selectedRows[i]);
      	return Arrays.asList(bienesRevertibles);
     		
     }
     
    public void addBienes(Collection<BienBean> bienes){
    	for (Iterator<BienBean> it=bienes.iterator();it.hasNext();)
    		bienesJTableModel.annadirRow(it.next());
    }
    

	private void adelanteJButton_actionPerformed() {
		try {
			if (inventarioJPanel!=null){
				String tipoSeleccionado=inventarioJPanel.getTipoBienesPanel().getTipoSeleccionado();
				
				if (tipoSeleccionado.equals(Const.SUPERPATRON_BIENES))
						inventarioJPanel.recargarTablaBienesInventario(actualOffset+ConfigParameters.DEFAULT_LIMIT);
				if (tipoSeleccionado.equals(Const.SUPERPATRON_REVERTIBLES))
					inventarioJPanel.recargarTablaBienesRevertibles(actualOffset+ConfigParameters.DEFAULT_LIMIT);
				//if (tipoSeleccionado.equals(Const.SUPERPTRON_PATRIMONIO_MUNICIPAL_SUELO))
				//	inventarioJPanel.recargarTablaBienesInventario(actualOffset+ConfigParameters.DEFAULT_LIMIT);
				if (tipoSeleccionado.equals(Const.SUPERPATRON_LOTES))
					inventarioJPanel.recargarTablaLotes(actualOffset+ConfigParameters.DEFAULT_LIMIT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void atrasJButton_actionPerformed() {
		try {
			if (inventarioJPanel!=null){
				
				int offset=0;
				if (actualOffset==0)
					offset=0;
				else
					offset=actualOffset-ConfigParameters.DEFAULT_LIMIT;
			
				
				String tipoSeleccionado=inventarioJPanel.getTipoBienesPanel().getTipoSeleccionado();
				
				if (tipoSeleccionado.equals(Const.SUPERPATRON_BIENES))
						inventarioJPanel.recargarTablaBienesInventario(offset);
				if (tipoSeleccionado.equals(Const.SUPERPATRON_REVERTIBLES))
					inventarioJPanel.recargarTablaBienesRevertibles(offset);
				//if (tipoSeleccionado.equals(Const.SUPERPTRON_PATRIMONIO_MUNICIPAL_SUELO))
				//	inventarioJPanel.recargarTablaBienesInventario(offset);
				if (tipoSeleccionado.equals(Const.SUPERPATRON_LOTES))
					inventarioJPanel.recargarTablaLotes(offset);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void setPanelPadre(InventarioJPanel inventarioJPanel) {
		this.inventarioJPanel=inventarioJPanel;
		
	}

	public void setActualOffset(int actualOffset) {
		this.actualOffset = actualOffset;
	}

	public int getActualOffset() {
		return actualOffset;
	}
	
	public boolean isSeleccionTabla() {
		return seleccionTabla;
	}

	public void setSeleccionTabla(boolean seleccionTabla) {
		this.seleccionTabla = seleccionTabla;
	}

    
}
