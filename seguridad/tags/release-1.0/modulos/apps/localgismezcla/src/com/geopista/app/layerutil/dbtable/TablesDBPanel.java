/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 *
 */


package com.geopista.app.layerutil.dbtable;


/**
 * Panel que permite realizar operaciones de manipulacion sobre las tablas
 * de GeoPISTA
 * 
 * @author cotesa
 *
 */
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.geopista.app.AppContext;
import com.geopista.app.layerutil.datosexternos.DataSourceTables;
import com.geopista.app.layerutil.exception.DataException;
import com.geopista.app.layerutil.schema.column.ColumnRow;
import com.geopista.app.layerutil.schema.column.TableColumnsModel;
import com.geopista.feature.Column;
import com.geopista.feature.Table;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;



public class TablesDBPanel extends JPanel implements FeatureExtendedPanel, TreeSelectionListener
{    
    private JPanel jPanelTablas = null;
    private JPanel jPanelColumnas = null;
    private JButton btnAnadirColumna = null;
    private JScrollPane jScrollPane = null;
    private JButton btnEditarColumna = null;
    private JButton btnEliminarColumna = null;
    private JButton btnCrearColumnasSistema = null;
    private JPanelDBTables jPanelTablasBD = null;
    private JButton btnSalir = null;
    private JButton btnAnadirTabla = null;
    private JButton btnEditarTabla = null;
    private JButton btnEliminarTabla = null;
    private JTree treeTablas = new JTree();
    private JTable tblColumnas = null;   
    private TableColumnsModel tablemodel= new TableColumnsModel();
    
    private Table selectedTable = null;
    private DefaultMutableTreeNode selectedNode = null; 
    private int indexSelectedColumnRow =-1;
    private ColumnRow selectedColumnRow = new ColumnRow();
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
    String string1 = I18N.get("GestorCapas","general.si"); 
    String string2 = I18N.get("GestorCapas","general.no"); 
    Object[] options = {string1, string2};
    
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    
    public static final String COL_NUMERIC = "NUMERIC";
    public static final String COL_VARCHAR = "VARCHAR";
    public static final String COL_DATE = "DATE";
    public static final String COL_GEOMETRY = "GEOMETRY";
    public static final String COL_CHAR = "CHAR";
    public static final String COL_INTEGER = "INTEGER";
    public static final String COL_INT4 = "INT4";
    public static final String COL_BYTEA = "BYTEA";
    public static final String COL_TIMESTAMP = "TIMESTAMP";
    public static final String COL_BOOLEAN = "BOOLEAN";
    
    public static final String TAB_GEOMETRY = "GEOMETRY";
    public static final String TAB_POINT = "POINT";
    public static final String TAB_LINESTRING = "LINESTRING";
    public static final String TAB_POLYGON = "POLYGON";
    public static final String TAB_COLLECTION = "COLLECTION";
    public static final String TAB_MULTIPOINT = "MULTIPOINT";
    public static final String TAB_MULTILINESTRING = "MULTILINESTRING";
    public static final String TAB_MULTIPOLYGON = "MULTIPOLYGON";
  
    
    /**
     * This is the default constructor
     */
    public TablesDBPanel()
    {
        super();
        initialize();
        
        try
        {
            // Iniciamos la ayuda
        	String helpHS = "help/catastro/gestordecapas/GestorCapasHelp_es.hs";
            ClassLoader c1 = this.getClass().getClassLoader();
            URL hsURL = HelpSet.findHelpSet(c1, helpHS);
            HelpSet hs = new HelpSet(null, hsURL);
            HelpBroker hb = hs.createHelpBroker();
            // fin de la ayuda
            hb.enableHelpKey(this,"Pestania1Tablas", hs);
        } 
        catch (Exception excp)
        {
            excp.printStackTrace();
        }
    }
    
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize()
    {
        this.setSize(937, 607);
        this.setLayout(null);
        this.add(getPanelTablas(), null);
        this.add(getJPanelColumnas(), null);
        this.add(getBtnAnadirColumna(), null);
        this.add(getBtnEditarColumna(), null);
        this.add(getBtnEliminarColumna(), null);
        this.add(getBtnCrearColumnasSistema(), null);
        this.add(getBtnSalir(), null);
        
        treeTablas = jPanelTablasBD.getTree();
        treeTablas.addTreeSelectionListener(this);
        treeTablas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                //Detecta el doble click
                if ( evt.getClickCount() == 2
                        && selectedNode.getUserObject() instanceof Table) 
                { 
                    btnEditarTabla_actionPerformed();
                } 
            }
        });
        treeTablas.addKeyListener(new KeyListener(){
            
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) 
                {                    
                    btnEliminarTabla_actionPerformed();                  
                }  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) 
                {                    
                    btnEditarTabla_actionPerformed();
                }  
            }
            
            public void keyReleased(KeyEvent e)
            {   
            }
            
            public void keyTyped(KeyEvent e)
            {   
            }
            
        });
        
        
    }
    
    /**
     * This method initializes jPanelTablas	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getPanelTablas()
    {
        if (jPanelTablas == null)
        {
            jPanelTablas = new JPanel();
            jPanelTablas.setBounds(new java.awt.Rectangle(15,31,270,540));
            jPanelTablas.setBorder(BorderFactory.createTitledBorder(I18N.get("GestorCapas","tablasBD.titulo")));
            jPanelTablas.add(getJPanelTablasBD(), null);
            jPanelTablas.setLayout(null);
            jPanelTablas.add(getBtnAnadirTabla(), null);
            jPanelTablas.add(getBtnEditarTabla(), null);
            jPanelTablas.add(getBtnEliminarTabla(), null);
            
        }
        return jPanelTablas;
    }
    
    /**
     * This method initializes jPanelTablasBD   
     *  
     * @return javax.swing.JTree    
     */
    private JPanelDBTables getJPanelTablasBD()
    {
        if (jPanelTablasBD == null)
        {
            jPanelTablasBD = new JPanelDBTables(TreeSelectionModel.SINGLE_TREE_SELECTION);
            //jPanelTablasBD.setBorder(BorderFactory.createTitledBorder(I18N.get("GestorCapas","layers.paneltablas.titulo")));
            jPanelTablasBD.setBounds(new Rectangle(14,24,247,395));            
        }
        return jPanelTablasBD;
        
    }        
    
    /**
     * This method initializes jPanelColumnas	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelColumnas()
    {
        if (jPanelColumnas == null)
        {
            jPanelColumnas = new JPanel();
            jPanelColumnas.setBounds(new java.awt.Rectangle(300,30,361,540));
            jPanelColumnas.setLayout(null);
            jPanelColumnas.add(getJScrollPane(), null);
            jPanelColumnas.setBorder(BorderFactory.createTitledBorder(I18N.get("GestorCapas","tablasBD.panelColumnas.titulo")));
            
        }
        return jPanelColumnas;
    }
    
    /**
     * This method initializes btnAnadirColumna	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getBtnAnadirColumna()
    {
        if (btnAnadirColumna == null)
        {
            btnAnadirColumna = new JButton();
            btnAnadirColumna.setBounds(new java.awt.Rectangle(690,46,130,30));
            btnAnadirColumna.setText(I18N.get("GestorCapas","general.boton.anadir"));
            btnAnadirColumna.setEnabled(false);
            btnAnadirColumna.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    //Llamar al metodo correspondiente
                    btnAnadirColumna_actionPerformed(e);
                }               
                    });
        }
        return btnAnadirColumna;
    }
    
    /**
     * Accion realizada al pulsar el boton de Annadir Columna
     * @param e
     */
    protected void btnAnadirColumna_actionPerformed(ActionEvent e)
    {        
        JDialogDBColumn dlgBDColumn = new JDialogDBColumn();        
        dlgBDColumn.setLocationRelativeTo(this);
        dlgBDColumn.show();
        
        ColumnRow newColRow=dlgBDColumn.getColumnRow();
        if (newColRow!=null)
        {            
            newColRow.getColumnaBD().setTableName(selectedTable.getDescription());
            TablesDBOperations operaciones = new TablesDBOperations();
            try
            {
                if (operaciones.crearColumnaBD(selectedTable, newColRow))
                {   
                    ColumnRow datosTotal[]= anadirColumna(((TableColumnsModel)tblColumnas.getModel()).getData(), newColRow);
                    
                    ((TableColumnsModel)tblColumnas.getModel()).setData(datosTotal);
                    this.remove(jPanelColumnas);
                    this.add(getJPanelColumnas());
                    
                    getJPanelColumnas().updateUI();
                    getJScrollPane().updateUI();
                    this.updateUI();
                    this.repaint();
                    
                    Identificadores.put("TablasModificadas", true);
                    Identificadores.put("LayersActualizada", false);
                    Identificadores.put("TablasDominiosActualizada", false);
                }
            } catch (DataException e1)
            {                
                e1.printStackTrace();
            }
        }
        
    }
    /**
     * Annade una columna a la lista de columnas 
     * @param datos Lista de columnas existentes
     * @param colRowToAdd Columna a annadir
     * @return Lista de columnas resultante
     */
    private ColumnRow[] anadirColumna(ColumnRow[] datos, ColumnRow colRowToAdd)
    {
        List list = new LinkedList(Arrays.asList(datos));
        list.add(colRowToAdd);
        ColumnRow[] destino = (ColumnRow[])list.toArray(new ColumnRow[list.size()]);        
        return destino;
    }
    
    /**
     * This method initializes jScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPane()
    {
        if (jScrollPane == null)
        {
            jScrollPane = new JScrollPane();
            jScrollPane.setBounds(new java.awt.Rectangle(18,17,326,510));
            jScrollPane.setViewportView(getJTableColumnas());
        }
        return jScrollPane;
    }
    
    /**
     * This method initializes tblColumnas	
     * 	
     * @return javax.swing.JTable	
     */
    private JTable getJTableColumnas()
    {        
        if (tblColumnas == null)
        {
            tablemodel= new TableColumnsModel(); 
            tblColumnas = new JTable(tablemodel);
        }
        
        tblColumnas.getColumnModel().getColumn(0).setCellRenderer(new ColumnsDBCellRenderer());
        tblColumnas.getColumnModel().getColumn(1).setCellRenderer(new ColumnsDBCellRenderer());
        //tblColumnas.getColumnModel().getColumn(1).setCellEditor(new AttributeEditor());
        tblColumnas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);   
        tblColumnas.clearSelection();
        
        tblColumnas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                //Detecta el doble click
                if (evt.getClickCount() == 2) 
                { 
                    btnEditarColumna_actionPerformed();
                    jScrollPane.updateUI();
                } 
            }
        });
        
        tblColumnas.addKeyListener(new KeyListener(){
            
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) 
                {                    
                    btnEliminarColumna_actionPerformed();                  
                }  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) 
                {                    
                    btnEditarColumna_actionPerformed();                  
                }  
            }
            
            public void keyReleased(KeyEvent e)
            {   
            }
            
            public void keyTyped(KeyEvent e)
            {   
            }
            
        });
           
        
        ListSelectionModel rowSM = tblColumnas.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()) {                    
                    btnEditarColumna.setEnabled(false);
                    btnEliminarColumna.setEnabled(false);   
                    btnCrearColumnasSistema.setEnabled(false);
                } 
                else 
                {
                    indexSelectedColumnRow = lsm.getMinSelectionIndex();
                    selectedColumnRow = tablemodel.getValueAt(indexSelectedColumnRow);
                    btnEditarColumna.setEnabled(true);
                    btnEliminarColumna.setEnabled(true);    
                    
                    if (selectedColumnRow.getColumnaSistema().getIdColumn()==0)
                        btnCrearColumnasSistema.setEnabled(true);
                    else
                        btnCrearColumnasSistema.setEnabled(false);
                }
            }
        });   
        
        return tblColumnas;
    }
    
    /**
     * This method initializes btnEditarColumna	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getBtnEditarColumna()
    {
        if (btnEditarColumna == null)
        {
            btnEditarColumna = new JButton();
            btnEditarColumna.setBounds(new java.awt.Rectangle(690,83,130,30));
            btnEditarColumna.setText(I18N.get("GestorCapas","general.boton.editar"));
            btnEditarColumna.setEnabled(false);
            btnEditarColumna.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    //Llamar al metodo correspondiente
                    btnEditarColumna_actionPerformed();
                }
                    });
        }
        return btnEditarColumna;
    }
    
    /**
     * Accion realizada al pulsar el boton de editar columna
     * @param e
     */
    protected void btnEditarColumna_actionPerformed()
    {
        
        JDialogDBColumn dlgBDColumn = new JDialogDBColumn(aplicacion.getMainFrame(),
                true, selectedColumnRow, true);
        
        dlgBDColumn.setLocationRelativeTo(this);
        dlgBDColumn.show();
        
        
        ColumnRow modifiedColRow=dlgBDColumn.getColumnRow();
        if (modifiedColRow!=null)
        {
            Table tablaBackup = new Table(selectedTable.getName(), selectedTable.getDescription());
            tablaBackup.setIdTabla(selectedTable.getIdTabla());
            tablaBackup.setGeometryType(selectedTable.getGeometryType());
            tablaBackup.setColumns(new HashMap(selectedTable.getColumns()));            
            ColumnRow [] colBackup = (ColumnRow [])((TableColumnsModel)tblColumnas.getModel()).getData().clone();
            
            TablesDBOperations operaciones = new TablesDBOperations();
            try
            {
                if(operaciones.modificarColumnaBD (selectedTable, modifiedColRow, selectedColumnRow))
                {
                    //se modifica la referencia de la columna dentro del table 
                    //int index = operaciones.findColumnPosition(selectedTable, modifiedColRow);
                    //selectedTable.getColumns().put(new Integer(index),modifiedColRow);                    
                    
                    ColumnRow datosTotal[]= modificarColumna(((TableColumnsModel)tblColumnas.getModel()).getData(), selectedColumnRow, modifiedColRow);
                    
                    ((TableColumnsModel)tblColumnas.getModel()).setData(datosTotal);
                    this.remove(jPanelColumnas);
                    this.add(getJPanelColumnas());
                    
                    ListSelectionModel rowSM = tblColumnas.getSelectionModel();
                    if (indexSelectedColumnRow ==0) {
                        rowSM.setSelectionInterval(0,0);
                        
                        if (tblColumnas.getModel().getRowCount()!=0)
                            selectedColumnRow = tablemodel.getValueAt(0);
                        else
                            selectedColumnRow = null;
                    }
                    
                    else if (indexSelectedColumnRow == tblColumnas.getModel().getRowCount())
                    {
                        rowSM.setSelectionInterval(indexSelectedColumnRow-1, indexSelectedColumnRow-1);
                        selectedColumnRow = tablemodel.getValueAt(indexSelectedColumnRow-1);
                    }
                    
                    else{
                        rowSM.setSelectionInterval(indexSelectedColumnRow, indexSelectedColumnRow);        
                        selectedColumnRow = tablemodel.getValueAt(indexSelectedColumnRow);
                    }
                    
                    if (tblColumnas.getModel().getRowCount()==0)
                        btnEliminarColumna.setEnabled(false);                
                    
                    if(selectedColumnRow.getColumnaSistema().getName()==null)
                        btnCrearColumnasSistema.setEnabled(true);
                    else 
                        btnCrearColumnasSistema.setEnabled(false);
                    
                    this.repaint();
                    getJPanelColumnas().updateUI();
                    getJScrollPane().updateUI();
                    this.updateUI();
                    
                    Identificadores.put("TablasModificadas", true);
                    Identificadores.put("LayersActualizada", false);
                    Identificadores.put("TablasDominiosActualizada", false);
                    
                }
            } catch (DataException e)
            {
                e.printStackTrace();
                
                //Restaurar valores originales
                selectedTable = tablaBackup;
                ((TableColumnsModel)tblColumnas.getModel()).setData(colBackup);
                this.remove(jPanelColumnas);
                this.add(getJPanelColumnas());
                
            }            
        }
    }
    
    /**
     * Modifica una columna del conjunto de columnas 
     * @param data Lista de columnas existentes
     * @param selectedColRow ColumnRow con la columna a modificar
     * @param modifiedColRow ColumnRow con la columna modificada
     * @return ColumnRow[] con el conjunto final de columnas
     */
    private ColumnRow[] modificarColumna(ColumnRow[] data, ColumnRow selectedColRow, ColumnRow modifiedColRow)
    {        
        List list = new LinkedList(Arrays.asList(data));
        int i = list.lastIndexOf(selectedColRow);
        list.remove(selectedColRow);
        list.add(i, modifiedColRow);
        ColumnRow[] destino = (ColumnRow[])list.toArray(new ColumnRow[list.size()]);        
        return destino;    
    }
    
    /**
     * This method initializes btnEliminarColumna	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getBtnEliminarColumna()
    {
        if (btnEliminarColumna == null)
        {
            btnEliminarColumna = new JButton();
            btnEliminarColumna.setBounds(new java.awt.Rectangle(690,121,130,30));
            btnEliminarColumna.setText(I18N.get("GestorCapas","general.boton.eliminar"));
            btnEliminarColumna.setEnabled(false);
            btnEliminarColumna.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    //Llamar al metodo correspondiente
                    btnEliminarColumna_actionPerformed();
                }
                    });
        }
        return btnEliminarColumna;
    }
    
    /**
     * Accion realizada al pulsar el boton de eliminar columna
     */
    protected void btnEliminarColumna_actionPerformed()
    {
        int n = JOptionPane.showOptionDialog(this,
                I18N.get("GestorCapas","tablasBD.borrar.columna"),
                I18N.get("GestorCapas","general.advertencia"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
        if (n==JOptionPane.NO_OPTION) return;
        
        TablesDBOperations operaciones = new TablesDBOperations();
        try
        {               
            if(operaciones.eliminarColumnaBD (selectedTable, selectedColumnRow ))
            {     
                //se elimina la referencia de la columna dentro del table para evitar problemas
                //en caso de edicion 
                int index = operaciones.findColumnPosition(selectedTable, selectedColumnRow);
                selectedTable.getColumns().remove(new Integer(index));
                
                //Se elimina la columna del modelo para la visualizacion
                ColumnRow datosTotal[]= eliminarColumna(((TableColumnsModel)tblColumnas.getModel()).getData(), selectedColumnRow);
                ((TableColumnsModel)tblColumnas.getModel()).setData(datosTotal);
                this.remove(jPanelColumnas);
                this.add(getJPanelColumnas());                
                
                ListSelectionModel rowSM = tblColumnas.getSelectionModel();
                if (indexSelectedColumnRow ==0) {
                    rowSM.setSelectionInterval(0,0);
                    
                    if (tblColumnas.getModel().getRowCount()!=0)
                        selectedColumnRow = tablemodel.getValueAt(0);
                    else
                        selectedColumnRow = null;
                }
                
                else if (indexSelectedColumnRow == tblColumnas.getModel().getRowCount())
                {
                    rowSM.setSelectionInterval(indexSelectedColumnRow-1, indexSelectedColumnRow-1);
                    selectedColumnRow = tablemodel.getValueAt(indexSelectedColumnRow);
                }
                
                else{
                    rowSM.setSelectionInterval(indexSelectedColumnRow, indexSelectedColumnRow);        
                    selectedColumnRow = tablemodel.getValueAt(indexSelectedColumnRow);
                }
                
                if (tblColumnas.getModel().getRowCount()==0)
                    btnEliminarColumna.setEnabled(false);
                
                tblColumnas.updateUI();
                getJPanelColumnas().updateUI();
                getJScrollPane().updateUI();
                this.updateUI();
                this.repaint();
                
                Identificadores.put("TablasModificadas", true);
                Identificadores.put("ColumnasBorradas",true);
                
                Identificadores.put("LayersActualizada", false);
                Identificadores.put("TablasDominiosActualizada", false);
            }
        } catch (DataException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }        
    }
    
    /**
     * Elimina una columna del conjunto de columnas
     * @param datos Lista de columnas existentes
     * @param colRowToDelete Columna que se desea eliminar
     * @return Lista de columnas resultante
     */
    private ColumnRow[] eliminarColumna(ColumnRow[] datos, ColumnRow colRowToDelete)
    {
        List list = new LinkedList(Arrays.asList(datos));
        list.remove(colRowToDelete);
        ColumnRow[] destino = (ColumnRow[])list.toArray(new ColumnRow[list.size()]);        
        return destino;  
    }
    
    /**
     * This method initializes btnCrearColumnasSistema   
     *  
     * @return javax.swing.JButton  
     */
    private JButton getBtnCrearColumnasSistema()
    {
        if (btnCrearColumnasSistema == null)
        {
            btnCrearColumnasSistema = new JButton();
            btnCrearColumnasSistema.setBounds(new java.awt.Rectangle(690,159,130,30));
            btnCrearColumnasSistema.setText(I18N.get("GestorCapas","tablasBD.boton.crearColumnaSistema"));
            btnCrearColumnasSistema.setEnabled(false);
            btnCrearColumnasSistema.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    //Llamar al metodo correspondiente
                    btnCrearColumnasSistema_actionPerformed(e);
                }
                    });
        }
        return btnCrearColumnasSistema;
    }
        
    /**
     * Accion realizada al pulsar el boton de Crear columna de sistema
     * @param e
     */
    protected void btnCrearColumnasSistema_actionPerformed(ActionEvent e)
    {
        TablesDBOperations operaciones = new TablesDBOperations();
        try
        {
            if(operaciones.crearColumnaSistema (selectedTable, selectedColumnRow )>=0)
            {   
                ((TableColumnsModel)tblColumnas.getModel()).setValueAt(selectedColumnRow.getColumnaSistema(), 
                        tblColumnas.getSelectedRow(), 1);           
                
                this.remove(jPanelColumnas);
                this.add(getJPanelColumnas());
                
                getJPanelColumnas().updateUI();
                getJScrollPane().updateUI();
                this.updateUI();
                
                this.repaint();  
                btnCrearColumnasSistema.setEnabled(false);           
                Identificadores.put("TablasModificadas", true);
                Identificadores.put("LayersActualizada", false);
                Identificadores.put("TablasDominiosActualizada", false);           
            }
        } catch (DataException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }        
    }
    
    /**
     * This method initializes btnSalir 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getBtnSalir()
    {
        if (btnSalir == null)
        {
            btnSalir = new JButton();
            btnSalir.setBounds(new Rectangle(875,555,100,25));
            btnSalir.setText(I18N.get("GestorCapas","general.boton.salir"));
            btnSalir.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    jButtonSalirActionPerformed(); 
                }
                    });
        }
        return btnSalir;
    }
    
    
    /**
     * This method initializes btnAnadirTabla	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getBtnAnadirTabla()
    {
        if (btnAnadirTabla == null)
        {
            btnAnadirTabla = new JButton();
            btnAnadirTabla.setBounds(new java.awt.Rectangle(60,432,130,30));
            btnAnadirTabla.setText(I18N.get("GestorCapas","general.boton.anadir"));  
            btnAnadirTabla.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    //Llamar al metodo correspondiente
                    btnAnadirTabla_actionPerformed(e);
                }
                    });
        }
        return btnAnadirTabla;
    }
    
    /**
     * Accion realizada al pulsar el boton de Annadir tabla
     * @param e
     */
    protected void btnAnadirTabla_actionPerformed(ActionEvent e)
    {
        
        JDialogDBTable dlgBDTable = new JDialogDBTable();       
        dlgBDTable.setLocationRelativeTo(this);
        dlgBDTable.show();
        
        Table newTable=dlgBDTable.getTable();
        if (newTable!=null)
        {            
            TablesDBOperations operaciones = new TablesDBOperations();
            try
            {
            	if (!operaciones.existeTabla(newTable.getDescription())){
                	if(operaciones.crearTablaBD (newTable))
	                {        
	                    //Crea por defecto la columna id_municipio, id y GEOMETRY
	                    ColumnRow idColRow  = new ColumnRow();
	                    ColumnDB idDB = new ColumnDB();
	                    idDB.setName("id");
	                    idDB.setLength(8);
	                    idDB.setType(COL_NUMERIC);
	                    idDB.setDefaultValue("");
	                    idDB.setDescription("");
	                    idDB.setNotNull(true);
	                    idDB.setUnique(true);
	                    idColRow.setColumnaBD(idDB);
	                    Column idColSis = new Column("id", "id", null);
	                    idColRow.setColumnaSistema(idColSis);
	                    operaciones.crearColumnaBD(newTable, idColRow);
	                   
	                    ColumnRow idMunicipioColRow  = new ColumnRow();
	                    ColumnDB idMunicipioDB = new ColumnDB();
	                    idMunicipioDB.setName("id_municipio");
	                    idMunicipioDB.setLength(5);
	                    idMunicipioDB.setType(COL_NUMERIC);
	                    idMunicipioDB.setDefaultValue("");
	                    idMunicipioDB.setNotNull(true);
	                    idMunicipioDB.setDescription("");
	                    idMunicipioColRow.setColumnaBD(idMunicipioDB);
	                    Column idMunicipioColSis = new Column("id_municipio", "id_municipio", null);
	                    idMunicipioColRow.setColumnaSistema(idMunicipioColSis);
	                    operaciones.crearColumnaBD(newTable, idMunicipioColRow);
	                    
	                    if (!(newTable.getGeometryType()<0))
	                    {
	                        ColumnRow idGeometryColRow  = new ColumnRow();
	                        ColumnDB idGeometryDB = new ColumnDB();
	                        idGeometryDB.setName("GEOMETRY");
	                        idGeometryDB.setType(COL_GEOMETRY);
	                        idGeometryDB.setDefaultValue("");
	                        idGeometryDB.setDescription("");
	                        idGeometryColRow.setColumnaBD(idGeometryDB);
	                        Column idGeometryColSis = new Column("GEOMETRY", "GEOMETRY", null);
	                        idGeometryColRow.setColumnaSistema(idGeometryColSis);
	                        operaciones.crearColumnaBD(newTable, idGeometryColRow);
	                    }
	                                       
	                    
	                    //Actualiza el arbol
	                    DefaultMutableTreeNode node = jPanelTablasBD.addObject(new DefaultMutableTreeNode(newTable));                
	                	((DefaultTreeModel)treeTablas.getModel()).reload();  
	                	treeTablas.setSelectionPath(new TreePath(node.getPath()));
	                    
	                    Identificadores.put("TablasModificadas", true);
	                    Identificadores.put("LayersActualizada", false);
	                    Identificadores.put("TablasDominiosActualizada", false);
	                }
            	}else{
                    JOptionPane.showMessageDialog(
                            TablesDBPanel.this,
                            "La tabla ya existe",
                            "LocalGIS",
                            JOptionPane.INFORMATION_MESSAGE);
            	}
            } catch (DataException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }              
    }    
    
    /**
     * This method initializes btnEditarTabla	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getBtnEditarTabla()
    {
        if (btnEditarTabla == null)
        {
            btnEditarTabla = new JButton();
            btnEditarTabla.setBounds(new java.awt.Rectangle(60,467,130,30));
            btnEditarTabla.setText(I18N.get("GestorCapas","general.boton.editar"));
            btnEditarTabla.setEnabled(false);
            btnEditarTabla.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    //Llamar al metodo correspondiente
                    btnEditarTabla_actionPerformed();
                }
                    });
        }
        return btnEditarTabla;
    }
    
    /**
     * Accion realizada al pulsar el boton de editar tabla
     * @param e
     */
    protected void btnEditarTabla_actionPerformed()
    {   
        JDialogDBTable dlgBDTable = new JDialogDBTable(aplicacion.getMainFrame(),
                true, selectedTable, true);
        
        dlgBDTable.setLocationRelativeTo(this);
        dlgBDTable.show();
        
        
        Table modifiedTable=dlgBDTable.getTable();
       
        if (modifiedTable!=null)
        {
            modifiedTable.setIdTabla(selectedTable.getIdTabla());
            modifiedTable.setName(selectedTable.getName());
           
            TablesDBOperations operaciones = new TablesDBOperations();
            try
            {
                if(operaciones.modificarTablaBD (selectedTable, modifiedTable))
                {
                    jPanelTablasBD.removeObject(selectedNode); 
                    DefaultMutableTreeNode modTable =new DefaultMutableTreeNode(modifiedTable); 
                    jPanelTablasBD.addObject(modTable);   
                    
                    ((DefaultTreeModel)treeTablas.getModel()).reload();
                    treeTablas.setSelectionPath(new TreePath(modTable.getPath()));
                    
                    Identificadores.put("TablasModificadas", true);
                    Identificadores.put("LayersActualizada", false);
                    Identificadores.put("TablasDominiosActualizada", false);
                }
            } catch (DataException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else
        {
            treeTablas.setSelectionPath(new TreePath(selectedNode.getPath()));            
        }
    }
    
    /**
     * This method initializes btnEliminarTabla	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getBtnEliminarTabla()
    {
        if (btnEliminarTabla == null)
        {
            btnEliminarTabla = new JButton();
            btnEliminarTabla.setBounds(new java.awt.Rectangle(60,502,130,30));
            btnEliminarTabla.setText(I18N.get("GestorCapas","general.boton.eliminar"));
            btnEliminarTabla.setEnabled(false);
            btnEliminarTabla.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    //Llamar al metodo correspondiente
                    btnEliminarTabla_actionPerformed();
                }
                    });
        }
        return btnEliminarTabla;
    }
    
    
    /**
     * Accion realizada al pulsar el boton de eliminar tabla
     * @param e
     */
    protected void btnEliminarTabla_actionPerformed()
    {
        int n = JOptionPane.showOptionDialog(this,
                I18N.get("GestorCapas","tablasBD.borrar.tabla"),
                I18N.get("GestorCapas","general.advertencia"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
        if (n==JOptionPane.NO_OPTION) return;
        
        TablesDBOperations operaciones = new TablesDBOperations();
        Object nodeInfo = selectedNode.getUserObject();
        if (nodeInfo !=null && nodeInfo instanceof Table)
        {   
            try
            {   
            	Table tabla = (Table)nodeInfo;
            	
            	DataSourceTables dataSourceTables = new DataSourceTables();
            	dataSourceTables.getDataSourceTables(tabla.getName());
            	int id = dataSourceTables.getId_datasource_tables();
            	if (id != -1)
            		dataSourceTables.eliminarDataSourceTables(id);
            	
                if (operaciones.eliminarTablaBD (tabla)){           
                    DefaultMutableTreeNode selectedNodeAfterDeleting = selectedNode.getNextSibling();
                    if (selectedNodeAfterDeleting==null)
                        selectedNodeAfterDeleting = selectedNode.getPreviousSibling();
                    if (selectedNodeAfterDeleting==null)
                        selectedNodeAfterDeleting = selectedNode.getPreviousNode();
                    
                    ((DefaultTreeModel)treeTablas.getModel()).removeNodeFromParent(selectedNode);
                    treeTablas.setSelectionPath(new TreePath(selectedNodeAfterDeleting.getPath()));
                    Identificadores.put("ColumnasBorradas", true);
                    
                    Identificadores.put("TablasModificadas", true);
                    Identificadores.put("LayersActualizada", false);
                    Identificadores.put("TablasDominiosActualizada", false);                    
                }
                else{
                	JOptionPane.showMessageDialog(
                            TablesDBPanel.this,
                            I18N.get("GestorCapas","tablasBD.borrar.tabla.existe"),
                            I18N.get("GestorCapas","tablasBD.borrar.tabla.localgis"),
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (DataException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
    
    /**
     * Accion realizada al pulsar el boton de salir
     *
     */
    private void jButtonSalirActionPerformed()
    {
        int n = JOptionPane.showOptionDialog(this,
                I18N.get("GestorCapas","general.salir.mensaje"),
                "",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
        if (n==JOptionPane.NO_OPTION) return;
        
        aplicacion.getMainFrame().dispose();
        System.exit(0);    
        
    }
    
    /**
     * Acciones realizadas al entrar en el panel
     */
    public void enter()
    {              
        try
        {
        	//NUEVO
        	 if (((Boolean)Identificadores.get("TablasModificadas")).booleanValue()){
             	Identificadores.put("TablasModificadas", false); 
             	
             	jPanelTablas.remove(jPanelTablasBD);
             	this.remove(jPanelTablas);
             	this.remove(jPanelColumnas);
             	treeTablas = null;
             	jPanelTablasBD = null;
             	jPanelTablas = null;
             	jPanelColumnas = null;
             	
             	Identificadores.remove("TablasBD");
             	
             	initialize();
            }
        	//FIN NUEVO
            // Iniciamos la ayuda
        	String helpHS = "help/catastro/gestordecapas/GestorCapasHelp_es.hs";
            ClassLoader c1 = this.getClass().getClassLoader();
            URL hsURL = HelpSet.findHelpSet(c1, helpHS);
            HelpSet hs = new HelpSet(null, hsURL);
            HelpBroker hb = hs.createHelpBroker();
            // fin de la ayuda
            hb.enableHelpKey(this,"Pestania1Tablas", hs);           
        } 
        catch (Exception excp)
        {
            excp.printStackTrace();
        }
    }
    /**
     * Acciones realizadas al salir del panel
     */
    public void exit()
    {      
        
    }
    
    /**
     * Acciones realizadas al producirse algun cambio en la seleccion 
     * en el arbol de tablas
     */
    public void valueChanged(TreeSelectionEvent e)
    {
        btnCrearColumnasSistema.setEnabled(false);
        
        if (e==null || !(e.getSource() instanceof JTree)) return;
        JTree arbol= (JTree)e.getSource();
        
        TreePath[] paths= arbol.getSelectionPaths();
        
        if (paths!=null){
            
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)paths[0].getLastPathComponent();
            selectedNode = node;
            
            Object nodeInfo = node.getUserObject();
            if (nodeInfo instanceof Table)
            {
                this.remove(jPanelColumnas);
                
                btnAnadirColumna.setEnabled(true);
                btnEliminarTabla.setEnabled(true);
                btnEditarTabla.setEnabled(true);
                
                selectedTable = (Table)nodeInfo;
                
                //Cargar columnas de BD y de sistema en la lista
                TablesDBOperations operaciones = new TablesDBOperations();
                HashMap htColBD  = new HashMap();
                HashMap htColSis = new HashMap();
                try
                {
                    htColBD = operaciones.obtenerListaColumnasBD(((Table)nodeInfo).getDescription());
                    htColSis = operaciones.obtenerListaColumnasSistema(((Table)nodeInfo).getIdTabla());
                } catch (DataException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }               
                
                selectedTable.setColumns(htColBD);
                
                ((TableColumnsModel)tblColumnas.getModel()).setData(htColBD, htColSis);
                tblColumnas.clearSelection();
                
                this.add(getJPanelColumnas(), null);
                
                getJPanelColumnas().updateUI();
                getJScrollPane().updateUI();
                this.updateUI();                
                
                this.repaint();                
            }
            else                
            {
                this.remove(jPanelColumnas);
                jScrollPane = null;
                jPanelColumnas = null;
                tblColumnas = null;
                this.add(getJPanelColumnas());
                btnAnadirColumna.setEnabled(true);
                btnEliminarTabla.setEnabled(false);
                btnEditarTabla.setEnabled(false);
            }            
        }    
    }    
    
    /**
     * Actualiza el Panel de Tablas de Base de Datos
     */
    public void updateJPanelTablasBD(Table newTable){
    	jPanelTablasBD.addObject(new DefaultMutableTreeNode(newTable)); 
    }
    
}
