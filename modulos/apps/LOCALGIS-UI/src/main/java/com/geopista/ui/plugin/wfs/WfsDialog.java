/**
 * WfsDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.wfs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkBench;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.feature.Table;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.TaskFrame;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


/**
 * En esta clase el usuario selecciona puede hacer consultas a servicios de nomenclátor y gazeteer. Para ello
 * puede introducir un criterio de búsqueda y seleccionar el servicio deseado. Con las features obtenidas se
 * puede acceder a detalle de la información, centrar o crear la feature.
 */
public class WfsDialog  extends JDialog {

    static AppContext appContext = (AppContext) AppContext.getApplicationContext();
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(WfsDialog.class);

    BorderLayout borderLayout1 = new BorderLayout();
    Border border1;
    JPanel buttonPanel = new JPanel();
    JButton okButton = new JButton();
    JButton anadirButton = new JButton();
    JButton modificarButton = new JButton();
    JButton eliminarButton = new JButton();
    private JLabel jLabelNombre = new JLabel();
    public JList jResultado;
    private JLabel jInfoResultado;
    public JTextField jTextNombre = new JTextField();
    public JList wfsList;
    private JLabel jLabelMne = new JLabel();
    public JRadioButton jRadioSinGeo = new JRadioButton();
    public JRadioButton jRadioConGeo = new JRadioButton();
    public JRadioButton radioMne = new JRadioButton();
    public JRadioButton radioG = new JRadioButton();
    private JLabel jLabelResultado = new JLabel();
    private JLabel jLabelResultados = new JLabel();
    private JLabel jLabelInfoResultado = new JLabel();
    private JButton botonCentrar = new JButton();
    private JButton botonCrearFeature = new JButton();
    public boolean acceso=true;
    public List listaIds = new ArrayList();
	public Vector vResult = new Vector();
	public List listaGeometrias;
	public static String FICHERO_FEATURES = "features.xml";
	public PlugInContext context;
	public Layer capaCreacion = null;
	private ListadoWfs wfs;
	public WfsMne wfsmne = new WfsMne(this);
	public WfsG wfsg = new WfsG(this);
	public TaskMonitorDialog progressDialog;
	
    // Cambiado por el interfaz general del Component raíz del framework. [Juan Pablo]
    private WfsDialog(Frame frame, String title, boolean modal, PlugInContext context) {
        super(frame, title, modal);
        this.context = context;
        frame.addWindowListener(new WindowAdapter() {
                 public void windowClosing(WindowEvent evt) {
                   wfsmne.borrarFichero();
               }
            });
        Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.wfs.languages.WfsDialogi18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("WfsDialog",bundle2);
        try {
            jbInit();
        } catch (Exception ex) {
            Assert.shouldNeverReachHere(ex.getMessage());
        }
    }
    

	public static WfsDialog instance(WorkBench workbench, PlugInContext context) {
        return instance(workbench.getBlackboard(), (WorkbenchGuiComponent) workbench.getGuiComponent(), context);
    }

    public static WfsDialog instance(Blackboard blackboard, WorkbenchGuiComponent frame, PlugInContext context) {
        if (blackboard.get(WfsDialog.class +" - INSTANCE") == null) {
            return (WfsDialog) blackboard.get(
            		WfsDialog.class +" - INSTANCE",
                        new WfsDialog(AppContext.getApplicationContext().getMainFrame(), I18N.get("WfsDialog","DatosGeograficos"), false,context));    
        }
        return (WfsDialog) blackboard.get(WfsDialog.class +" - INSTANCE");        
    }

    void jbInit() throws Exception {
    	TitledBorder border1 = BorderFactory.createTitledBorder("Servicio WFS");
    	TitledBorder border2 = BorderFactory.createTitledBorder(I18N.get("WfsDialog","ResultadoBusqueda"));
    	java.awt.GridBagConstraints gridBagConstraints;
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        JPanel jPanel1 = new JPanel();
        JPanel jPanel2 = new JPanel();
        jPanel1.setBorder(border1);
        jPanel2.setBorder(border2);
        getContentPane().setLayout(new java.awt.GridBagLayout());
        jPanel1.setLayout(new java.awt.GridBagLayout());
        jPanel2.setLayout(new java.awt.GridBagLayout());
        
        wfs = new ListadoWfs(this);
        jLabelNombre.setText(I18N.get("WfsDialog","nombre")+":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabelNombre, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jTextNombre, gridBagConstraints);
        jLabelMne.setText(I18N.get("WfsDialog","ListaWfs")+":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 11);
        jPanel1.add(jLabelMne, gridBagConstraints);
        JScrollPane scrollPane = new JScrollPane();
        wfsList = new JList();
        wfsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        wfsList.setListData(this.wfsmne.listaMnes.toArray());
        scrollPane.getViewport().setView(wfsList);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 75;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        scrollPane.setPreferredSize(new Dimension(150,150));
        jPanel1.add(scrollPane, gridBagConstraints);
        ButtonGroup botones2 = new ButtonGroup();
        radioMne.setText("WFS-MNE");
        radioMne.setSelected(true);
        radioMne.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
            	if (radioMne.isSelected()){
            		wfsList.setListData(wfsmne.listaMnes.toArray());
            	}
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel1.add(radioMne, gridBagConstraints);

        radioG.setText("WFS-G");
        radioG.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
            	if (radioG.isSelected()){
            		wfsList.setListData(wfsg.listaWfsG.toArray());
            	}
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        jPanel1.add(radioG, gridBagConstraints);
        botones2.add(radioMne);
        botones2.add(radioG);
        jRadioSinGeo.setText(I18N.get("WfsDialog","SinGeo"));
        jRadioSinGeo.setSelected(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(14, 0, 0, 0);
        jPanel1.add(jRadioSinGeo, gridBagConstraints);
        jRadioConGeo.setText(I18N.get("WfsDialog","ConGeo"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 0, 0, 0);
        jPanel1.add(jRadioConGeo, gridBagConstraints);
        ButtonGroup botones = new ButtonGroup();
        botones.add(jRadioSinGeo);
        botones.add(jRadioConGeo);
        wfs.addWfsRegistry();
        anadirButton.setText(I18N.get("WfsDialog","AnadirWfs"));
        anadirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
            	wfs.addWfs();
            }
        });
        modificarButton.setText(I18N.get("WfsDialog","ModificarWfs"));
        modificarButton.setEnabled(false);
        modificarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
            	wfs.modifyWfs();
            }
        });

        eliminarButton.setText(I18N.get("WfsDialog","Eliminar"));
    	eliminarButton.setEnabled(false);
        eliminarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
            	wfs.eliminateWfs();
            }
        });
        okButton.setText(I18N.get("WfsDialog","Buscar"));
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
            	searchResults();
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 22, 0, 22);
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(anadirButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 22, 0, 22);
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(modificarButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 22, 25, 22);
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(eliminarButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 22, 0, 22);
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(okButton, gridBagConstraints);

        jLabelResultados.setText(I18N.get("WfsDialog","Resultados")+":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 0, 9, 0);
        jPanel2.add(jLabelResultados, gridBagConstraints);
        jLabelInfoResultado.setText(I18N.get("WfsDialog","InfoResultado")+":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 3);
        jPanel2.add(jLabelInfoResultado, gridBagConstraints);
        jResultado = new JList();
        jResultado.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        wfsList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
            	wfsList.setSelectionModel(((JList)e.getSource()).getSelectionModel());
                int index = wfsList.getSelectedIndex();
                if (index>2 || radioG.isSelected()){ //No se pueden eliminar los Mne que vienen por defecto
                	eliminarButton.setEnabled(true);
                	modificarButton.setEnabled(true);
                }else{
                	eliminarButton.setEnabled(false);
                	modificarButton.setEnabled(false);
                }
           }
        });   
        jResultado.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e){
            	jResultado.setSelectionModel(((JList)e.getSource()).getSelectionModel());
                int index = jResultado.getSelectedIndex();
                if (index > -1)
                	createInformation(index);
            }
        });   

        JScrollPane scrollPane2 = new JScrollPane();
        scrollPane2.getViewport().setView(jResultado);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        scrollPane2.setPreferredSize(new Dimension(150,150));
        jPanel2.add(scrollPane2, gridBagConstraints);
        jInfoResultado = new JLabel("");
        JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.getViewport().setView(jInfoResultado);
        scrollPane1.getViewport().setBackground(Color.WHITE);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 3.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        scrollPane1.setPreferredSize(new Dimension(150,150));
        jPanel2.add(scrollPane1, gridBagConstraints);
        botonCentrar.setText(I18N.get("WfsDialog","CentrarFeature"));
        botonCentrar.setPreferredSize(new Dimension(120,20));
        botonCentrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                centreMap();
            }
        });
        botonCrearFeature.setText(I18N.get("WfsDialog","CrearFeature"));
        botonCrearFeature.setPreferredSize(new Dimension(120,20));
        botonCrearFeature.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
            	selectionLayer();
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(19, 1, 19, 15);
        jPanel2.add(botonCentrar, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(15, 15, 15, 15);
        jPanel2.add(botonCrearFeature, gridBagConstraints);
        
        jPanel1.setPreferredSize(new Dimension(450,250));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        getContentPane().add(jPanel1, gridBagConstraints);

        jPanel2.setPreferredSize(new Dimension(450,280));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(9, 7, 9, 7);
        getContentPane().add(jPanel2, gridBagConstraints);
        this.setSize(500,600);
        this.setResizable(false);
        pack();
     }

    /**
     * Se validan los criterios que ha introducido el usuario
     * @return
     */
    private String validateInput() {
    	if (wfsList.getSelectionModel().isSelectionEmpty())
    		return I18N.get("WfsDialog","SeleccionWfs");
         return null;
    }
    
    /**
     * Genero los resultados según los criterios de entrada
     */
    public void searchResults() 
    {
        //Primero validamos los criterios introducidos por el usuario
	    String errorMessage = validateInput();
	    if (errorMessage != null) {
	        JOptionPane.showMessageDialog(WfsDialog.this,errorMessage,
	            AppContext.getMessage("GeopistaName"),JOptionPane.ERROR_MESSAGE);
	        return;
	    }
    	if (this.jRadioConGeo.isSelected()){
	    	if (this.context.getLayerManager() == null){
	        	JOptionPane.showMessageDialog(this, I18N.get("WfsDialog","AbrirMapa"),
	        			AppContext.getMessage("GeopistaName"), JOptionPane.ERROR_MESSAGE);
	        	return;
	    	}
    	}
    	progressDialog = new TaskMonitorDialog(appContext.getMainFrame(),
                context.getErrorHandler());
        progressDialog.setTitle(appContext.getI18nString("Buscar"));
        progressDialog.report(appContext.getI18nString("Buscando"));
        progressDialog.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                //Wait for the dialog to appear before starting the task. Otherwise
                //the task might possibly finish before the dialog appeared and the
                //dialog would never close. [Jon Aquino]
                new Thread(new Runnable(){
                    public void run()
                    {
                        try
                        {
                		    jInfoResultado.setText("");
                		    jResultado.removeAll();
                		    Object entidades[] = wfsList.getSelectedValues();
                		    int n = entidades.length;
                		    String[] url = new String [n];
                		    listaIds.clear();
                		    for (int i=0;i<n;i++){
                		    	url[i] = createUrl((String)entidades[i]);
                		    }
                    	    if (radioMne.isSelected()){
                    		    wfsmne.searchResults(url);
                    	    }else{
                    		    wfsg.searchResults(url);
                    	    }
                        }catch(Exception e)
                        {
                        }finally
                        {
                            progressDialog.setVisible(false);
                        } 
                    }
                }).start();
            }
        });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
    }
    
    
    public void createInformation(int index){
        if (this.radioMne.isSelected()){
            String fidEntidad = (String)this.listaIds.get(index);
        	this.jInfoResultado.setText(this.wfsmne.createInformation(fidEntidad, index));
        }else
        	this.jInfoResultado.setText(this.wfsg.createInformation(index));
    }
    
    /**
     * Muestra las capas que tiene el usuario disponibles en ese momento para que elija
     * en la que desea crear la feature
     */
     public void selectionLayer(){
 	    JDialog dialogLayers = new JDialog(this,I18N.get("WfsDialog","SeleccionCapa"),true);
 	    dialogLayers.getContentPane().setLayout(null);
 	    JLabel jlabel = new JLabel(I18N.get("WfsDialog","MensajeSeleccionCapa"));
 	    jlabel.setBounds(20,20,280,20);
 	    dialogLayers.getContentPane().add(jlabel);
 	    JList jListLayers = new JList(this.context.getLayerManager().getLayers().toArray());
 	    jListLayers.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
 	    JScrollPane jScroll = new JScrollPane(jListLayers);
 	    jScroll.setBounds(50,70,120,100);
 	    dialogLayers.getContentPane().add(jScroll);
 	    JButton jButton = new JButton(appContext.getI18nString("btnAceptar"));
 	    jButton.setBounds(60,200,100,20);
 	    dialogLayers.getContentPane().add(jButton);
 	    jListLayers.addListSelectionListener(new ListSelectionListener() {
 	        public void valueChanged(ListSelectionEvent e) {
 	            int index = ((JList)e.getSource()).getSelectedIndex();
 	            capaCreacion =  (Layer)context.getLayerManager().getLayers().get(index);
 	        }
 	    });   
 	    jButton.addActionListener(new ActionListener() {
 	        public void actionPerformed(ActionEvent e){
 	            ((JDialog)((JButton)e.getSource()).getParent().getParent().getParent().getParent()).dispose();
 	            createFeature();
 	        }
 	    });
 	    GUIUtil.centreOnWindow(dialogLayers);
 	    dialogLayers.setSize(new Dimension(320,300));
 	    dialogLayers.setVisible(true);
     }
    
    /**
     * Dibuja una la geometría de una feature en el mapa activo
     */
    private void createFeature(){
    	//Compruebo que tengo abierto un mapa
    	if (context.getLayerManager() == null){
        	JOptionPane.showMessageDialog(this, I18N.get("WfsDialog","AbrirMapa"),
        			AppContext.getMessage("GeopistaName"), JOptionPane.ERROR_MESSAGE);
        	return;
    	}
    	//Primero compruebo que he seleccionado una feature
    	int index = jResultado.getSelectedIndex();
    	if (index == -1){
        	JOptionPane.showMessageDialog(this, I18N.get("WfsDialog","SeleccioneFeature"),
        			AppContext.getMessage("GeopistaName"), JOptionPane.ERROR_MESSAGE);
        	return;
    	}
    	progressDialog = new TaskMonitorDialog(appContext.getMainFrame(),
                context.getErrorHandler());
        progressDialog.setTitle(I18N.get("WfsDialog","CreateFeature"));
        progressDialog.report(I18N.get("WfsDialog","CreatingFeature"));
        progressDialog.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                //Wait for the dialog to appear before starting the task. Otherwise
                //the task might possibly finish before the dialog appeared and the
                //dialog would never close. [Jon Aquino]
                new Thread(new Runnable(){
                    public void run()
                    {
                        try
                        {
                    	    if (radioMne.isSelected())
                    		    wfsmne.createFeature();
                    		else
                    			wfsg.createFeature();
                        }catch(Exception e)
                        {
                        }finally
                        {
                            progressDialog.setVisible(false);
                        } 
                    }
                }).start();
            }
        });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
    	
    }
    
    /**
     * Centra una geometría en el mapa activo de acuerdo a su centroide
     */
    private void centreMap(){
    	if (isOperationPossible()){
	    	progressDialog = new TaskMonitorDialog(appContext.getMainFrame(),
	                context.getErrorHandler());
	        progressDialog.setTitle(I18N.get("WfsDialog","CentreMap"));
	        progressDialog.report(I18N.get("WfsDialog","CentringMap"));
	        progressDialog.addComponentListener(new ComponentAdapter() {
	            public void componentShown(ComponentEvent e) {
	                //Wait for the dialog to appear before starting the task. Otherwise
	                //the task might possibly finish before the dialog appeared and the
	                //dialog would never close. [Jon Aquino]
	                new Thread(new Runnable(){
	                    public void run()
	                    {
	                        try
	                        {
	                    	    if (radioMne.isSelected())
	                    		    wfsmne.centreMap();
	                    		else
	                    			wfsg.centreMap();
	                        }catch(Exception e)
	                        {
	                        }finally
	                        {
	                            progressDialog.setVisible(false);
	                        } 
	                    }
	                }).start();
	            }
	        });
	        GUIUtil.centreOnWindow(progressDialog);
	        progressDialog.setVisible(true);
    	}
    }

    /**
     * Se comprueba si la geometría es adecuada para insertarla en la capa seleccionada
     */
    public boolean testGeometry(int tipo,Geometry geometry){
        switch (tipo){
	        case Table.MULTIPOLYGON:
	        	if (geometry.getClass() != MultiPolygon.class)
	            {
	            }
	            break;
	
	        case Table.MULTILINESTRING:
	            if (geometry.getClass() != MultiLineString.class)
	            {
	            	return false;
	            }
	            break;
	
	        case Table.MULTIPOINT:
	            if (geometry.getClass() != MultiPoint.class)
	            {
	            	return false;
	            }
	            break;
	
	        case Table.POINT:
	            if (geometry.getClass() != Point.class)
	            {
	            	return false;
	            }
	            break;
	
	        case Table.LINESTRING:
	            if (geometry.getClass() != LineString.class)
	            {
	            	return false;
	            }
	            break;
	
	        case Table.POLYGON:
	            if (geometry.getClass() != Polygon.class)
	            {
	            	return false;
	            }
	            break;
	
	        default:
	            return true;

        }
        return true;
    	
    }
    /**
     * Construye la url sobre la que se hará la consulta 
     */
    public String createUrl(String entidad){
    	String nombre = "";
    	StringBuffer url = new StringBuffer();
    	StringBuffer cadenaBBOX = new StringBuffer();
    	List listaServicios;
    	List listaUrls;
    	if (this.radioMne.isSelected()){
    		listaServicios = wfsmne.listaMnes;
    		listaUrls = wfsmne.listaURLs;
    	}else{
    		listaServicios = wfsg.listaWfsG;
    		listaUrls = wfsg.listaURLs;
    	}
    	int n = listaServicios.size();
    	for (int i=0;i<n;i++){
    		if (entidad.equals(listaServicios.get(i))){
    	    	url = url.append(listaUrls.get(i)+"?");
    	    	if (entidad.equals("Via") || entidad.equals("Portal"))
    	    		nombre = "app:";
    	    	nombre += listaServicios.get(i);
    	    	if (entidad.equals("Idee"))
    	    		nombre = "Entidad";
    	    	break;
    		}
    	}
    	url = url.append("REQUEST=GetFeature&version=1.1.0&SERVICE=WFS&TYPENAME=");
    	url = url.append(nombre+"&NAMESPACE=xmlns(app=http://www.deegree.org/app)");
    	if (!this.jTextNombre.getText().equals("")||this.jRadioConGeo.isSelected()){
    		url = url.append("&FILTER=%3CFilter%20xmlns:gml='http://www.opengis.net/gml'%3E");
        	if (!this.jTextNombre.getText().equals("")&&this.jRadioConGeo.isSelected())
        		url = url.append("%3CAnd%3E");
	    	if (!this.jTextNombre.getText().equals("")){
	    		url = url.append("%3CPropertyIsLike%20wildCard='*'%20singleChar='_'%20escapeChar='%7C'%3E%3CPropertyName%3EnombreEntidad/nombre%3C/PropertyName%3E%3CLiteral%3E");
	    		url = url.append(removeSpaces(this.jTextNombre.getText())+"%3C/Literal%3E%3C/PropertyIsLike%3E");
	    	}
	    	if (this.jRadioConGeo.isSelected()){
	    		GeopistaBBOX g1 = new GeopistaBBOX();
	    		g1.setXmin(((TaskFrame)context.getActiveInternalFrame()).getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates().getMinX());
	    		g1.setYmin(((TaskFrame)context.getActiveInternalFrame()).getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates().getMinY());
	    		g1.setXmax(((TaskFrame)context.getActiveInternalFrame()).getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates().getMaxX());
	    		g1.setYmax(((TaskFrame)context.getActiveInternalFrame()).getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates().getMaxY());
	    		try{
	    			transformCoordinatesToGeographic(g1);
	    		}catch(Exception e){
	    			e.printStackTrace();
	    		}
	    		cadenaBBOX = cadenaBBOX.append("%3CBBOX%3E%3CPropertyName%3EposicionEspacial_coordinates%3C/PropertyName%3E");
	    		cadenaBBOX = cadenaBBOX.append("%3Cgml:Envelope%20srsName='EPSG:"+this.context.getLayerManager().getCoordinateSystem().getEPSGCode()+"'%3E%3Cgml:lowerCorner%3E");
	    		cadenaBBOX = cadenaBBOX.append(g1.getXmin()+"%20"+g1.getYmin());
	    		cadenaBBOX = cadenaBBOX.append("%3C/gml:lowerCorner%3E%3Cgml:upperCorner%3E"+g1.getXmax()+"%20");
	    		cadenaBBOX = cadenaBBOX.append(g1.getYmax()+"%3C/gml:upperCorner%3E%3C/gml:Envelope%3E%3C/BBOX%3E");
	        	url = url.append(cadenaBBOX);
	    	}
        	if (!this.jTextNombre.getText().equals("")&&this.jRadioConGeo.isSelected())
        		url = url.append("%3C/And%3E");
	    	url = url.append("%3C/Filter%3E");
    	}
    	return url.toString();
    }
    private void transformCoordinatesToGeographic(GeopistaBBOX g1) throws Exception {
		int sridGeographic = 4230;
		CoordinateSystem geographicCoord = CoordinateSystemRegistry.instance(AppContext.getApplicationContext().getBlackboard()).get(sridGeographic);
		CoordinateSystem mapCoord = this.context.getLayerManager().getCoordinateSystem();
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(), mapCoord.getEPSGCode());
        if (mapCoord.getEPSGCode() != 4230){
        	Point pointXMinYMin = factory.createPoint(new Coordinate(g1.getXmin(),g1.getYmin()));
        	Point pointXMaxYMax = factory.createPoint(new Coordinate(g1.getXmax(),g1.getYmax()));
        	pointXMinYMin = (Point)CoordinateConversion.instance().reproject(pointXMinYMin,mapCoord, geographicCoord);
        	pointXMinYMin.setSRID(geographicCoord.getEPSGCode());
        	pointXMaxYMax = (Point)CoordinateConversion.instance().reproject(pointXMaxYMax,mapCoord, geographicCoord);
        	pointXMaxYMax.setSRID(geographicCoord.getEPSGCode());
        	g1.setXmin(pointXMinYMin.getX());
        	g1.setYmin(pointXMinYMin.getY());
        	g1.setXmax(pointXMaxYMax.getX());
        	g1.setYmax(pointXMaxYMax.getY());
        }
        
		
	}
    /**
     * Compruebo que tenga seleccionada una capa con un sistema de coordenadas definido para realizar
     * determinadas operaciones
     */
    private boolean isOperationPossible(){
    	//Compruebo que tengo abierto un mapa
    	if (context.getLayerManager() == null){
        	JOptionPane.showMessageDialog(this, I18N.get("WfsDialog","AbrirMapa"),
        			AppContext.getMessage("GeopistaName"), JOptionPane.ERROR_MESSAGE);
        	return false;
    	}
    	//Primero compruebo que he seleccionado una feature
    	int index = jResultado.getSelectedIndex();
    	if (index == -1){
        	JOptionPane.showMessageDialog(this, I18N.get("WfsDialog","SeleccioneFeature"),
        			AppContext.getMessage("GeopistaName"), JOptionPane.ERROR_MESSAGE);
        	return false;
    	}
    	CoordinateSystem destino = context.getLayerManager().getCoordinateSystem();
    	if (destino.getEPSGCode() == 0){
        	JOptionPane.showMessageDialog(this, I18N.get("WfsDialog","SelSistCoordMapa"),
        			AppContext.getMessage("GeopistaName"), JOptionPane.ERROR_MESSAGE);
        	return false;
    	}
/*    	//Compruebo que se ha seleccionado una capa
    	int n = this.context.getSelectedLayers().length;
    	if (n == 0){
        	JOptionPane.showMessageDialog(this, I18N.get("WfsDialog","SelCapa"),
        			AppContext.getMessage("GeopistaName"), JOptionPane.ERROR_MESSAGE);
        	return;
    	}
    	//Se comprueba que alguna capa visible tenga fijado el sistema de coordenadas
    	int i;
    	for (i=0;i<n;i++){
            Layer layer = this.context.getSelectedLayer(0);    		
        	if (layer.isVisible() == true){
		        CoordinateSystem destino = layer.getFeatureCollectionWrapper().getFeatureSchema().getCoordinateSystem();
		        if (destino.getEPSGCode() == 0){
		        	JOptionPane.showMessageDialog(this, I18N.get("WfsDialog","SelSistCoord"),
		        			AppContext.getMessage("GeopistaName"), JOptionPane.ERROR_MESSAGE);
		        	return;
		        }else{
		        	break;
		        }
        	}
    	}
    	//Este es el caso en el que no tenemos ninguna capa visible
    	if (i == n){
        	JOptionPane.showMessageDialog(this, I18N.get("WfsDialog","SelCapa"),
        			AppContext.getMessage("GeopistaName"), JOptionPane.ERROR_MESSAGE);
        	return;
    	}*/
    	return true;
    }
    /*
     * Sustituye los espacios en blanco de una cadena por %20
     */
    private String removeSpaces(String s) {
    	StringBuffer st = new StringBuffer();
    	int n = s.length();
    	for (int i=0;i<n;i++){
    		char car = s.charAt(i);
    		if (car ==' ')
    			st.append("%20");
    		else
    			st.append(car);
    	}
    	return st.toString();
    }


    
  }