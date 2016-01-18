package com.geopista.app.alptolocalgis.panels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.geopista.app.AppContext;
import com.geopista.app.alptolocalgis.beans.ConstantesAlp;
import com.geopista.app.alptolocalgis.beans.OperacionAlp;
import com.geopista.app.alptolocalgis.panels.model.TableOperacionesModel;
import com.geopista.app.alptolocalgis.utils.WfsMne;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.SchemaValidator;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.catastro.Via;
import com.geopista.protocol.contaminantes.NumeroPolicia;
import com.geopista.ui.plugin.GeopistaValidatePlugin;
import com.geopista.ui.plugin.wfs.CoordinateConversion;
import com.geopista.ui.plugin.wfs.GeopistaBBOX;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.java2xml.XML2Java;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class EditingInfoPanel extends JPanel implements FeatureExtendedPanel
{        
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static AppContext app =(AppContext) AppContext.getApplicationContext();
    private Blackboard blackboard = app.getBlackboard();
	private JPanel jPanelOperaciones = null;
	private JScrollPane jScrollPaneOperaciones = null;
	private JTable jTableOperaciones = null;
	private TableOperacionesModel tableOperacionesModel = null;
	private JPanel jPanelBotoneraOperaciones = null;
	private JButton jButtonSincronizar = null;
	private JButton jButtonEliminar = null;
	private JPanel jPanelDatos;
	private JScrollPane jScrollPaneDatos = null;
	private JPanel jPanelDatosBusqueda = null;
	private JPanel jPanelBusqueda = null;
	private JPanel jPanelBotoneraBusqueda = null;
	private JButton jButtonCentrar = null;
	private ButtonGroup jBottonGroupModoBusqueda = null;
	private JRadioButton jRadioButtonAutomatica = null;
	private JRadioButton jRadioButtonManual = null;
	private JLabel jLabelBusquedaAutomatica = null;
	private JLabel jLabelBusquedaManual = null;
	private JPanel jPanelResultadosBusqueda = null;
	private JScrollPane jScrollPaneResultados = null;
	private JList jListResultados = null;
	private JScrollPane jScrollPaneInfoResultados = null;
	private JLabel jLabelNombreVia = null;
	private JButton jButtonBuscar = null;
	private JPanel jPanelRadioButtons = null;
	private JPanel jPanelBusquedaManual = null;
	private JLabel jLabelTipoServicio = null;
	private ComboBoxEstructuras jComboBoxServicios = null;
	private ComboBoxEstructuras jComboBoxTipoEntidad = null;
	private JLabel jLabelTipoOperacion = null;
	private JPanel jPanelTipoServicio = null;
	private JLabel jLabelNombrePortal = null;
	private JTextField jTextFieldNombreVia = null;
	private JTextField jTextFieldNombrePortal = null;	
	private JLabel jLabelInfoResultado = null;
	private ArrayList lstIds = null;
	private ArrayList lstIdsIntermedios = null;
	private WfsMne wfsMne = new WfsMne();
	private JTabbedPane jTabbedPaneBusquedas = null;
	private ButtonGroup jBottonGroupLikeOrLiteral = null;
	private JRadioButton jRadioButtonLike = null;
	private JRadioButton jRadioButtonLiteral = null;
	private JLabel jLabelLike = null;
	private JLabel jLabelLiteral = null;
	private JCheckBox jCheckBoxResGeom = null;
        
    public EditingInfoPanel()
    {
        super();
        initialize();       
        
    }           
    
    private void initialize()
    {
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.alptolocalgis.languages.AlpToLocalGISi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("AlpToLocalGIS",bundle);
        
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(848, 500));
        this.setPreferredSize(new java.awt.Dimension(450,500));
        
        this.setBorder(BorderFactory.createTitledBorder
                (null, I18N.get("AlpToLocalGIS", "alptolocalgis.editinginfopanel.title"), 
                		TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 13))); 
       
        this.add(getJPanelOperaciones(), 
        		new GridBagConstraints(0,0,1,1,0.1, 0.08,GridBagConstraints.NORTH,
                        GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));
        
        this.add(getJTabbedPaneBusquedas(), 
        		new GridBagConstraints(0,1,1,1,0.1, 0.2,GridBagConstraints.NORTH,
                        GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
        
        ArrayList lstOperaciones = null;
        lstOperaciones = recopilaOperacionesBD();
        loadTableOperaciones(lstOperaciones);
        
        jLabelTipoServicio.setEnabled(false);
        getJComboBoxServicios().setEnabled(false);
        getJButtonBuscar().setEnabled(false);
        getJCheckBoxResGeom().setSelected(true);
        EdicionUtils.enablePanel(getJPanelBusquedaManual(), false);
        getJButtonSincronizar().setEnabled(false);
        getJButtonEliminar().setEnabled(false);
        
    }
    
    private ArrayList recopilaOperacionesBD()
    {
    	ArrayList lstOperaciones = new ArrayList();
        try
        {
        	lstOperaciones = (ArrayList) ConstantesAlp.clienteAlp.getOperacionesAlp();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return lstOperaciones;
    }
    
    
    private boolean removeOperacionBD(Integer idOperacion)
    {    	
        try
        {
        	ConstantesAlp.clienteAlp.removeOperacionAlp(idOperacion);
        	return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    private ArrayList recopilaOperacionesBD(String filtro)
    {
    	ArrayList lstOperaciones = new ArrayList();
        try
        {
        	lstOperaciones = (ArrayList) ConstantesAlp.clienteAlp.getOperacionesAlp(filtro);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return lstOperaciones;
    }
    
    private JPanel getJPanelBusqueda(){
    	
    	if (jPanelBusqueda  == null){
    		
    		jPanelBusqueda = new JPanel();
    		jPanelBusqueda.setLayout(new GridBagLayout());
    		jPanelBusqueda.setBorder(BorderFactory.createTitledBorder
                  (null, I18N.get("AlpToLocalGIS", "alptolocalgis.editinginfo.search.title"), 
                  		TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
    		
    		jPanelBusqueda.add(getJPanelDatosBusqueda(), 
    				new GridBagConstraints(0,0,1,1,0.1, 0.1,GridBagConstraints.WEST,
    						GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),0,0));
    		jPanelBusqueda.add(getJPanelResultadosBusqueda(), 
    				new GridBagConstraints(0,1,1,1,1.0, 1.0,GridBagConstraints.NORTH,
    						GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
    		jPanelBusqueda.add(getJPanelBotoneraBusqueda(), 
    				new GridBagConstraints(0,2,1,1,0.1, 0.1,GridBagConstraints.SOUTH,
    						GridBagConstraints.CENTER, new Insets(0,0,0,0),0,0));
    		
    	}
    	return jPanelBusqueda;
    }
    
    private JPanel getJPanelBotoneraBusqueda(){
    	
    	if(jPanelBotoneraBusqueda  == null){
    		
    		jPanelBotoneraBusqueda = new JPanel();
    		jPanelBotoneraBusqueda.setLayout(new GridBagLayout());
    		jPanelBotoneraBusqueda.add(getJButtonCentrar(), 
                    new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(5, 0, 5, 0), 0, 0));
    		
    	}
    	return jPanelBotoneraBusqueda;
    }
    
    private JButton getJButtonCentrar(){
    	
    	if (jButtonCentrar == null)
        {
    		jButtonCentrar = new JButton();
    		jButtonCentrar.setText(I18N.get("AlpToLocalGIS", 
    				"alptolocalgis.editinginfo.search.center")); 
    		jButtonCentrar.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    //Operaciones de sincronizar features
                	int index = jListResultados.getSelectedIndex();
                    if (index > -1){
                    	centrarMapa(index);
                    }
                }
                    }); 
    		
    		jButtonCentrar.setName("center");
        }
        return jButtonCentrar;
    }

    
    private JPanel getJPanelDatosBusqueda(){
    	
    	if (jPanelDatosBusqueda  == null){
    		
    		jPanelDatosBusqueda = new JPanel();
    		jPanelDatosBusqueda.setLayout(new GridBagLayout());
    		jPanelDatosBusqueda.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("AlpToLocalGIS", "alptolocalgis.editinginfopanel.search.searched"), 
                    		TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 11)));
    		    		
    		jPanelDatosBusqueda.add(getJPanelRadioButtons(), 
    				new GridBagConstraints(0,0,1,1,1, 1,GridBagConstraints.CENTER,
    						GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
    		
    		jPanelDatosBusqueda.add(getJPanelTipoServicio(), 
    				new GridBagConstraints(1,0,1,1,1, 1,GridBagConstraints.CENTER,
    						GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
    		
    		jPanelDatosBusqueda.add(getJPanelBusquedaManual(), 
    				new GridBagConstraints(0,1,2,1,1, 1,GridBagConstraints.WEST,
    						GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));    		
    		
    	}
    	return jPanelDatosBusqueda;    		
    }
    
    private JPanel getJPanelBusquedaManual(){
    	
    	if (jPanelBusquedaManual == null){
    		
    		jPanelBusquedaManual  = new JPanel();
    		jPanelBusquedaManual.setLayout(new GridBagLayout());
    		
    		jLabelNombreVia  = new JLabel("", JLabel.CENTER);
    		jLabelNombreVia.setText(I18N.get("AlpToLocalGIS", 
    				"alptolocalgis.editinginfo.search.name.street"));
    		
    		jLabelNombrePortal   = new JLabel("", JLabel.CENTER);
    		jLabelNombrePortal.setText(I18N.get("AlpToLocalGIS", 
    				"alptolocalgis.editinginfo.search.name.number"));
        		
    		jPanelBusquedaManual.add(jLabelNombreVia, 
    				new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.CENTER,
    						GridBagConstraints.NONE, new Insets(5,0,5,0),0,0));
    		jPanelBusquedaManual.add(getJTextFieldNombreVia(), 
    				new GridBagConstraints(1,0,1,1,1,1,GridBagConstraints.CENTER,
    						GridBagConstraints.HORIZONTAL, new Insets(5,0,5,0),0,0));     		
    		jPanelBusquedaManual.add(jLabelNombrePortal, 
    				new GridBagConstraints(2,0,1,1,1,1,GridBagConstraints.CENTER,
    						GridBagConstraints.NONE, new Insets(5,0,5,0),0,0));
    		jPanelBusquedaManual.add(getJTextFieldNombrePortal(), 
    				new GridBagConstraints(3,0,1,1,1,1,GridBagConstraints.CENTER,
    						GridBagConstraints.HORIZONTAL, new Insets(5,0,5,0),0,0));
    		jPanelBusquedaManual.add(getJButtonBuscar(), 
    				new GridBagConstraints(6,0,1,1,1,1,GridBagConstraints.CENTER,
    						GridBagConstraints.NONE, new Insets(5,0,5,0),0,0));
    	}
    	return jPanelBusquedaManual;
    }
    
    private ComboBoxEstructuras getJComboBoxServicios(){
    	
    	if (jComboBoxServicios == null){
    	
    		Estructuras.cargarEstructura("Servicios");
    		jComboBoxServicios = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, app.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
            
    		jComboBoxServicios.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					JComboBox cb = (JComboBox)e.getSource();
			        String servicio = cb.getSelectedItem().toString();
			        
			        if (servicio != null && servicio.equals(ConstantesAlp.VIA)){
			        	
			        	EdicionUtils.enablePanel(getJPanelBusquedaManual(), true);	
			        	getJTextFieldNombrePortal().setEnabled(false);
			        	getJTextFieldNombrePortal().setText("");
			        	getJTextFieldNombrePortal().setBackground(Color.LIGHT_GRAY) ;
			        	
			        }
			        else if (servicio != null && servicio.equals(ConstantesAlp.NUMBER)){
			        	
			        	EdicionUtils.enablePanel(getJPanelBusquedaManual(), true);
			        	
			        }
			        else{
			        	
			        	getJTextFieldNombreVia().setText("");
			        	getJTextFieldNombrePortal().setText("");
			        	EdicionUtils.enablePanel(getJPanelBusquedaManual(), false);			        	
			        }					
				}    			
    		});
    	}    	
    	return jComboBoxServicios;
    }
        
    private ComboBoxEstructuras getJComboBoxTipoOperaciones(){
    	
    	if (jComboBoxTipoEntidad  == null){
    	
    		Estructuras.cargarEstructura("Tipos de Operacion ALP");
    		jComboBoxTipoEntidad = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, app.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
            
    		jComboBoxTipoEntidad.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					ArrayList lstOperaciones = null;
					JComboBox cb = (JComboBox)e.getSource();
					String tipoOperacion = ((ComboBoxEstructuras)cb).getSelectedPatron();
			        
			        if (tipoOperacion != null && !tipoOperacion.equals("")){
			        	
			        	removeTableOperaciones();
			        	lstOperaciones = recopilaOperacionesBD(tipoOperacion);
			            loadTableOperaciones(lstOperaciones);
			            getJTableOperaciones().updateUI();
			            getJTableOperaciones().clearSelection();
			        	
			        }
			        else{
			        	
			        	removeTableOperaciones();
			        	lstOperaciones = recopilaOperacionesBD();
			            loadTableOperaciones(lstOperaciones);
			            getJTableOperaciones().updateUI();
			            getJTableOperaciones().clearSelection();
			        	
			        }
					
				}    			
    		});
    	}    	
    	return jComboBoxTipoEntidad;
    }
    
    private JPanel getJPanelTipoServicio(){
    	
    	if (jPanelTipoServicio  == null){
    		
    		jPanelTipoServicio = new JPanel();
    		jPanelTipoServicio.setLayout(new GridBagLayout());
    		
    		jLabelTipoServicio = new JLabel("", JLabel.CENTER);
    		jLabelTipoServicio.setText(I18N.get("AlpToLocalGIS", 
					"alptolocalgis.editinginfo.search.services"));
    		
    		jPanelTipoServicio.add(getJCheckBoxResGeom(),
    				new GridBagConstraints(0,0,2,1,0.1, 0.1,GridBagConstraints.CENTER,
    						GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
    		jPanelTipoServicio.add(jLabelTipoServicio,
    				new GridBagConstraints(0,1,1,1,0.1, 0.1,GridBagConstraints.CENTER,
    						GridBagConstraints.NONE, new Insets(5,5,5,5),0,0));
    		jPanelTipoServicio.add(getJComboBoxServicios(),
    				new GridBagConstraints(1,1,1,1,0.1, 0.1,GridBagConstraints.WEST,
    						GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
    		    		
    	}
    	return jPanelTipoServicio;
    }
    
    private JCheckBox getJCheckBoxResGeom() {
		if (jCheckBoxResGeom == null) {
			jCheckBoxResGeom  = new JCheckBox();
			jCheckBoxResGeom.setText(I18N.get("AlpToLocalGIS", 
					"alptolocalgis.editinginfo.search.resgeom"));
			
			jCheckBoxResGeom.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					
					if (EditingInfoPanel.this.jRadioButtonAutomatica.isSelected()){
						
						if (jLabelTipoServicio!= null){
							jLabelTipoServicio.setEnabled(false);
						}
				        getJComboBoxServicios().setEnabled(false);
				        getJButtonBuscar().setEnabled(false);
				        EdicionUtils.enablePanel(getJPanelBusquedaManual(), false);
				        
				        getJLabelInfoResultado().setText("");
				        getJTextFieldNombreVia().setText("");
				        getJTextFieldNombrePortal().setText("");
				        if (jListResultados != null){
				        	jListResultados.setListData(new Vector());
				        }
				        
						//Realizar búsqueda automática
				        OperacionAlp operacionAlp = (OperacionAlp)blackboard.get("operacion");
				        if (operacionAlp != null){
				        	
				        	if (operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_ADD_VIA) ||
                    				operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_DEL_VIA) ||
                    				operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_MOD_VIA)){
                    			
                    			buscarResultados(ConstantesAlp.VIA, new Boolean(false));
                    		}
                    		else if(operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_ADD_NUMERO_POLICIA) ||
                    				operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_DEL_NUMERO_POLICIA) ||
                    				operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_MOD_NUMERO_POLICIA)){
                    			
                    			buscarResultados(ConstantesAlp.NUMBER, new Boolean(false));
                    		}
				        }
				        
					}					
				}
	  			
    		});
		}
		return jCheckBoxResGeom;
	}
    
    private JPanel getJPanelRadioButtons(){
    	
    	if (jPanelRadioButtons  == null){
    		
    		jPanelRadioButtons = new JPanel();
    		jPanelRadioButtons.setLayout(new GridBagLayout());
    		
    		jPanelRadioButtons.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
    		
    		jBottonGroupLikeOrLiteral   = new ButtonGroup();
    		jRadioButtonLike  = new JRadioButton();
    		jRadioButtonLiteral  = new JRadioButton();
    		jBottonGroupLikeOrLiteral.add(jRadioButtonLike);
    		jBottonGroupLikeOrLiteral.add(jRadioButtonLiteral);
    		jBottonGroupLikeOrLiteral.setSelected(jRadioButtonLike.getModel(), true);
    		
    		jRadioButtonLike.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					
					if (EditingInfoPanel.this.jRadioButtonAutomatica.isSelected()){
						
						if (jLabelTipoServicio!= null){
							jLabelTipoServicio.setEnabled(false);
						}
				        getJComboBoxServicios().setEnabled(false);
				        getJButtonBuscar().setEnabled(false);
				        EdicionUtils.enablePanel(getJPanelBusquedaManual(), false);
				        
				        getJLabelInfoResultado().setText("");
				        getJTextFieldNombreVia().setText("");
				        getJTextFieldNombrePortal().setText("");
				        if (jListResultados != null){
				        	jListResultados.setListData(new Vector());
				        }
				        
						//Realizar búsqueda automática
				        OperacionAlp operacionAlp = (OperacionAlp)blackboard.get("operacion");
				        if (operacionAlp != null){
				        	
				        	if (operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_ADD_VIA) ||
                    				operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_DEL_VIA) ||
                    				operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_MOD_VIA)){
                    			
                    			buscarResultados(ConstantesAlp.VIA, new Boolean(false));
                    		}
                    		else if(operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_ADD_NUMERO_POLICIA) ||
                    				operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_DEL_NUMERO_POLICIA) ||
                    				operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_MOD_NUMERO_POLICIA)){
                    			
                    			buscarResultados(ConstantesAlp.NUMBER, new Boolean(false));
                    		}
				        }
				        
					}					
				}
  			
    		});
    		
    		jRadioButtonLiteral.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					
					if (EditingInfoPanel.this.jRadioButtonAutomatica.isSelected()){
						
						if (jLabelTipoServicio!= null){
							jLabelTipoServicio.setEnabled(false);
						}
				        getJComboBoxServicios().setEnabled(false);
				        getJButtonBuscar().setEnabled(false);
				        EdicionUtils.enablePanel(getJPanelBusquedaManual(), false);
				        
				        getJLabelInfoResultado().setText("");
				        getJTextFieldNombreVia().setText("");
				        getJTextFieldNombrePortal().setText("");
				        if (jListResultados != null){
				        	jListResultados.setListData(new Vector());
				        }
				        
						//Realizar búsqueda automática
				        OperacionAlp operacionAlp = (OperacionAlp)blackboard.get("operacion");
				        if (operacionAlp != null){
				        	
				        	if (operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_ADD_VIA) ||
                    				operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_DEL_VIA) ||
                    				operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_MOD_VIA)){
                    			
                    			buscarResultados(ConstantesAlp.VIA, new Boolean(false));
                    		}
                    		else if(operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_ADD_NUMERO_POLICIA) ||
                    				operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_DEL_NUMERO_POLICIA) ||
                    				operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_MOD_NUMERO_POLICIA)){
                    			
                    			buscarResultados(ConstantesAlp.NUMBER, new Boolean(false));
                    		}
				        }
				        
					}					
				}
  			
    		});
    		
    		jBottonGroupModoBusqueda  = new ButtonGroup();
    		jRadioButtonAutomatica  = new JRadioButton();
    		jRadioButtonAutomatica.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					
					if (EditingInfoPanel.this.jRadioButtonAutomatica.isSelected()){
						
						if (jLabelTipoServicio!= null){
							jLabelTipoServicio.setEnabled(false);
						}
				        getJComboBoxServicios().setEnabled(false);
				        getJButtonBuscar().setEnabled(false);
				        EdicionUtils.enablePanel(getJPanelBusquedaManual(), false);
				        
				        getJLabelInfoResultado().setText("");
				        getJTextFieldNombreVia().setText("");
				        getJTextFieldNombrePortal().setText("");
				        getJComboBoxServicios().setSelectedIndex(0);
				        if (jListResultados != null){
				        	jListResultados.setListData(new Vector());
				        }
				        
						//Realizar búsqueda automática
				        OperacionAlp operacionAlp = (OperacionAlp)blackboard.get("operacion");
				        if (operacionAlp != null){
				        	
				        	if (operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_ADD_VIA) ||
                    				operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_DEL_VIA) ||
                    				operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_MOD_VIA)){
                    			
                    			buscarResultados(ConstantesAlp.VIA, new Boolean(false));
                    		}
                    		else if(operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_ADD_NUMERO_POLICIA) ||
                    				operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_DEL_NUMERO_POLICIA) ||
                    				operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_MOD_NUMERO_POLICIA)){
                    			
                    			buscarResultados(ConstantesAlp.NUMBER, new Boolean(false));
                    		}
				        }
				        
					}					
				}
  			
    		});
    		
    		jRadioButtonManual = new JRadioButton();
    		jRadioButtonManual.addChangeListener(new javax.swing.event.ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					
					if (jRadioButtonManual.isSelected()){
						
						if (jLabelTipoServicio!= null){
							jLabelTipoServicio.setEnabled(true);
						}
				        getJComboBoxServicios().setEnabled(true);			        
					}					
				}    			
    		});
    		
    		jBottonGroupModoBusqueda.add(jRadioButtonAutomatica);
    		jBottonGroupModoBusqueda.add(jRadioButtonManual);
    		jBottonGroupModoBusqueda.setSelected(jRadioButtonAutomatica.getModel(), true);
    		
    		jLabelBusquedaAutomatica = new JLabel("", JLabel.CENTER); 
    		jLabelBusquedaAutomatica.setText(I18N.get("AlpToLocalGIS", 
    				"alptolocalgis.editinginfo.search.automatic")); 
    		jLabelBusquedaManual  = new JLabel("", JLabel.CENTER); 
    		jLabelBusquedaManual.setText(I18N.get("AlpToLocalGIS", 
    				"alptolocalgis.editinginfo.search.manual"));
    		
    		jLabelLike = new JLabel("", JLabel.CENTER); 
    		jLabelLike.setText(I18N.get("AlpToLocalGIS", 
    				"alptolocalgis.editinginfo.search.like")); 
    		jLabelLiteral   = new JLabel("", JLabel.CENTER); 
    		jLabelLiteral.setText(I18N.get("AlpToLocalGIS", 
    				"alptolocalgis.editinginfo.search.literal"));
    		
    		jPanelRadioButtons.add(jRadioButtonAutomatica, 
    				new GridBagConstraints(0,0,1,1,1, 1,GridBagConstraints.WEST,
    						GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));
    		jPanelRadioButtons.add(jLabelBusquedaAutomatica, 
    				new GridBagConstraints(1,0,1,1,1, 1,GridBagConstraints.WEST,
    						GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));  
    		jPanelRadioButtons.add(jRadioButtonManual, 
    				new GridBagConstraints(2,0,1,1,1, 1,GridBagConstraints.WEST,
    						GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));
    		jPanelRadioButtons.add(jLabelBusquedaManual, 
    				new GridBagConstraints(3,0,1,1,1, 1,GridBagConstraints.WEST,
    						GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));
    		
    		jPanelRadioButtons.add(jRadioButtonLike, 
    				new GridBagConstraints(0,1,1,1,1, 1,GridBagConstraints.WEST,
    						GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));
    		jPanelRadioButtons.add(jLabelLike, 
    				new GridBagConstraints(1,1,1,1,1, 1,GridBagConstraints.WEST,
    						GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));  
    		jPanelRadioButtons.add(jRadioButtonLiteral, 
    				new GridBagConstraints(2,1,1,1,1, 1,GridBagConstraints.WEST,
    						GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));
    		jPanelRadioButtons.add(jLabelLiteral, 
    				new GridBagConstraints(3,1,1,1,1, 1,GridBagConstraints.WEST,
    						GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));
    		
    	}
    	return jPanelRadioButtons;
    }
    
    private JTextField getJTextFieldNombreVia(){
    	
    	if(jTextFieldNombreVia == null){
    		
    		jTextFieldNombreVia  = new JTextField(12);
    		jTextFieldNombreVia.setName("via");
    	}
    	return jTextFieldNombreVia;
    }
    
    private JTextField getJTextFieldNombrePortal(){
    	
    	if(jTextFieldNombrePortal == null){
    		
    		jTextFieldNombrePortal  = new JTextField(6);
    		jTextFieldNombrePortal.setName("portal");
    	}
    	return jTextFieldNombrePortal;
    }
    
    private JButton getJButtonBuscar(){

    	if (jButtonBuscar == null){

    		jButtonBuscar = new JButton();
    		jButtonBuscar.setText(I18N.get("AlpToLocalGIS", 
    		"alptolocalgis.editinginfo.search.search")); 
    		jButtonBuscar.addActionListener(new java.awt.event.ActionListener()
    		{
    			public void actionPerformed(java.awt.event.ActionEvent e)
    			{
    				//Operaciones de buscar features    				
    				String errorMessage = validarServicioSeleccionado();
    			    
    			    if (errorMessage != null) {
    			        JOptionPane.showMessageDialog(null,errorMessage,
    			            AppContext.getMessage("GeopistaName"),JOptionPane.WARNING_MESSAGE);
    			            					
    			        return;
    			    }
    			    
    			    errorMessage = busquedaManual();
    			    if (errorMessage != null) {
    			        JOptionPane.showMessageDialog(null,errorMessage,
    			            AppContext.getMessage("GeopistaName"),JOptionPane.WARNING_MESSAGE);
    			            					
    			        return;
    			    }
    		    	
    			}
    		}); 
    		
    		jButtonBuscar.setName("search");

    	}
    	return jButtonBuscar;
    }
    
    private JPanel getJPanelResultadosBusqueda(){
    	
    	if (jPanelResultadosBusqueda  == null){
    		
    		jPanelResultadosBusqueda = new JPanel();
    		jPanelResultadosBusqueda.setLayout(new GridBagLayout());
    		jPanelResultadosBusqueda.setBorder(BorderFactory.createTitledBorder
            (null, I18N.get("AlpToLocalGIS", "alptolocalgis.editingpanel.search.result.title"), 
            		TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 11)));
    		
    		jScrollPaneResultados  = new JScrollPane();
    		
    		jListResultados  = new JList();
    		jListResultados.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e){
                	jListResultados.setSelectionModel(((JList)e.getSource()).getSelectionModel());
                    int index = jListResultados.getSelectedIndex();
                    if (index > -1){
                    	createInformation(index, (String) jListResultados.getSelectedValue());
                    }
                }
            });   
    		
    		jScrollPaneInfoResultados   = new JScrollPane();
    		jScrollPaneInfoResultados.getViewport().setView(getJLabelInfoResultado());
    		jScrollPaneInfoResultados.getViewport().setBackground(Color.WHITE);
    		
    		jScrollPaneResultados.setSize(new Dimension(100, 200));
    		jScrollPaneResultados.setPreferredSize(new Dimension(100, 200));
    		jScrollPaneResultados.setMaximumSize(jScrollPaneResultados.getPreferredSize());
    		jScrollPaneResultados.setMinimumSize(jScrollPaneResultados.getPreferredSize());
    		jScrollPaneResultados.getViewport().setView(jListResultados);
    		
    		jScrollPaneInfoResultados.setSize(new Dimension(200, 200));
    		jScrollPaneInfoResultados.setPreferredSize(new Dimension(200, 200));
    		jScrollPaneInfoResultados.setMaximumSize(jScrollPaneInfoResultados.getPreferredSize());
    		jScrollPaneInfoResultados.setMinimumSize(jScrollPaneInfoResultados.getPreferredSize());
		
    		jPanelResultadosBusqueda.add(jScrollPaneResultados, 
    				new GridBagConstraints(0,0,1,1,0.5,1,GridBagConstraints.NORTH,
    						GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));
    		jPanelResultadosBusqueda.add(jScrollPaneInfoResultados, 
    				new GridBagConstraints(1,0,1,1,0.5,1,GridBagConstraints.NORTH,
    						GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));
    	}
    	return jPanelResultadosBusqueda;
    }
    
    private JPanel getJPanelDatos(){
    	
    	if (jPanelDatos == null){
    		
    		jPanelDatos = new JPanel();
    		jScrollPaneDatos = new JScrollPane();
    		
    		jPanelDatos.setLayout(new GridBagLayout());
    		jPanelDatos.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("AlpToLocalGIS", "alptolocalgis.editingpanel.info.title"), 
                    		TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
               		
    		jPanelDatos.add(jScrollPaneDatos, 
    				new GridBagConstraints(0,0,1,1,1, 1,GridBagConstraints.NORTH,
    						GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));
    		
    	}
    	return jPanelDatos;
    }
    private JPanel getJPanelOperaciones(){
    	
    	if (jPanelOperaciones  == null){
    		
    		jPanelOperaciones = new JPanel(new GridBagLayout());
    		jPanelOperaciones.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("AlpToLocalGIS", "alptolocalgis.editingpanel.operations.title"),
                    		TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
    		jPanelOperaciones.setPreferredSize(new Dimension(10, 10));
    		jPanelOperaciones.setMaximumSize(jPanelOperaciones.getPreferredSize());
    		jPanelOperaciones.setMinimumSize(jPanelOperaciones.getPreferredSize());
		    		
    		jPanelOperaciones.setLayout(new GridBagLayout());
    		
    		jLabelTipoOperacion   = new JLabel("", JLabel.CENTER);
    		jLabelTipoOperacion.setText(I18N.get("AlpToLocalGIS", 
    				"alptolocalgis.editinginfo.operations.type"));
    		
    		jPanelOperaciones.add(jLabelTipoOperacion, 
            		new GridBagConstraints(0,0,1,1,1, 0.1,GridBagConstraints.CENTER,
                            GridBagConstraints.NONE, new Insets(0,0,5,0),0,0));
    		
    		jPanelOperaciones.add(getJComboBoxTipoOperaciones(), 
            		new GridBagConstraints(1,0,1,1,1, 0.1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(0,0,5,0),0,0));
         
    		jPanelOperaciones.add(getJScrollPaneOperaciones(), 
            		new GridBagConstraints(0,1,2,1,1, 0.2,GridBagConstraints.NORTH,
                            GridBagConstraints.BOTH, new Insets(0,5,5,5),0,0));
         
    		jPanelOperaciones.add(getJPanelBotoneraOperaciones(), 
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                    		GridBagConstraints.NONE, new Insets (0,0,0,0), 0,0));
    	}
    	return jPanelOperaciones;
    }

    private JPanel getJPanelBotoneraOperaciones(){
    	
    	if (jPanelBotoneraOperaciones  == null)
        {
    		jPanelBotoneraOperaciones = new JPanel(new GridBagLayout());
            
    		jPanelBotoneraOperaciones.add(getJButtonSincronizar(), 
                    new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(5, 0, 5, 0), 0, 0));
            
    		jPanelBotoneraOperaciones.add(getJButtonEliminar(), 
                    new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(5, 0, 5, 0), 0, 0));                 
        }
        return jPanelBotoneraOperaciones;
    }
    
    public JButton getJButtonEliminar(){
    	
    	if (jButtonEliminar   == null)
        {
    		jButtonEliminar = new JButton();
    		jButtonEliminar.setText(I18N.get("AlpToLocalGIS", 
    				"alptolocalgis.editinginfo.operations.delete")); 
    		jButtonEliminar.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    //Operaciones de sincronizar features
                	if (JOptionPane.showConfirmDialog(
            				null,
            				I18N.get("AlpToLocalGIS","alptolocalgis.editinginfo.operations.removeoperationquestion"),
            				I18N.get("AlpToLocalGIS","alptolocalgis.editinginfo.operations.removeoperation"),
            				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                    {
                		OperacionAlp operacion = (OperacionAlp)blackboard.get("operacion");
                		if (operacion != null){
                    		removeOperacionBD(operacion.getIdOperacion());                    		
                    		getJComboBoxTipoOperaciones().setSelectedItem(jComboBoxTipoEntidad.getSelectedItem());
                		}
                    }
                }
                    }); 
    		
    		jButtonEliminar.setName("delete");
        }
        return jButtonEliminar;
    }
        
    public JButton getJButtonSincronizar(){

    	if (jButtonSincronizar  == null)
    	{
    		jButtonSincronizar = new JButton();
    		jButtonSincronizar.setText(I18N.get("AlpToLocalGIS", 
    		"alptolocalgis.editinginfo.operations.synchronize")); 
    		jButtonSincronizar.addActionListener(new java.awt.event.ActionListener()
    		{
    			public void actionPerformed(java.awt.event.ActionEvent e)
    			{    				
    					sincronizar(); 
    			}
    		}); 

    		jButtonSincronizar.setName("synchronize");
    	}
    	return jButtonSincronizar;
    }
    
    private JScrollPane getJScrollPaneOperaciones(){
    	
    	if (jScrollPaneOperaciones == null){    		
    		
    		jScrollPaneOperaciones = new JScrollPane();
    		jScrollPaneOperaciones.setViewportView(getJTableOperaciones());
    	    
    	}
    	return jScrollPaneOperaciones;
    }
    
    private JTable getJTableOperaciones()
    {
        if (jTableOperaciones   == null)
        {
        	jTableOperaciones = new JTable();

            tableOperacionesModel  = new TableOperacionesModel();
            
            TableSorted tblSorted= new TableSorted(tableOperacionesModel);
            
            tblSorted.setTableHeader(jTableOperaciones.getTableHeader());
            jTableOperaciones.setModel(tblSorted);
            jTableOperaciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTableOperaciones.setCellSelectionEnabled(false);
            jTableOperaciones.setColumnSelectionAllowed(false);
            jTableOperaciones.setRowSelectionAllowed(true);            
            
            ((ListSelectionModel) jTableOperaciones.getSelectionModel()).
            addListSelectionListener(new ListSelectionListener(){
            	public void valueChanged(ListSelectionEvent e)
            	{     
            		LoadDatosPanel loadDatosPanel = new LoadDatosPanel();
            		ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                    if (lsm.isSelectionEmpty()) {
                    	jScrollPaneDatos.setViewportView(loadDatosPanel.getLoadDatosPanel(null));  
                    	getJPanelDatos().updateUI();
                    	disableButtons();
                    } 
                    else 
                    {                                       	
                        int indexSelectedColumnRow = lsm.getMinSelectionIndex();  
                        int index = ((TableSorted)jTableOperaciones.getModel()).modelIndex(indexSelectedColumnRow);
                        if ((tableOperacionesModel.getValueAt(index)) instanceof OperacionAlp){

                        	OperacionAlp operacionAlp = (OperacionAlp)(tableOperacionesModel.getValueAt(index));
                        	
                        	blackboard.put("operacion", operacionAlp);
                        	
                        	jScrollPaneDatos.setViewportView(loadDatosPanel.getLoadDatosPanel(operacionAlp));  
                        	getJPanelDatos().updateUI();
                        	
                        	if (jRadioButtonAutomatica.isSelected()){  
                        		
                        		if (operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_ADD_VIA) ||
                        				operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_DEL_VIA) ||
                        				operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_MOD_VIA)){
                        			
                        			buscarResultados(ConstantesAlp.VIA, new Boolean(false));
                        		}
                        		else if(operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_ADD_NUMERO_POLICIA) ||
                        				operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_DEL_NUMERO_POLICIA) ||
                        				operacionAlp.getTipoOperacion().equals(ConstantesAlp.ACTION_MOD_NUMERO_POLICIA)){
                        			
                        			buscarResultados(ConstantesAlp.NUMBER, new Boolean(false));
                        		}
                            }
                        	
                        	//Habilita los botones de Eliminar y Sincronizar si el usuario tiene permisos
                        	applySecurityPolicy();
							
                        }    
                        
                    }	

            	}
            });
            
            ArrayList lst = new ArrayList();
            lst.add(new OperacionAlp());
            ((TableOperacionesModel)((TableSorted)jTableOperaciones.getModel()).
                    getTableModel()).setData(lst);
            
        }
        return jTableOperaciones;
    }
    
    public void loadTableOperaciones(ArrayList lstOperaciones)
    {
        ((TableOperacionesModel)((TableSorted)getJTableOperaciones().getModel()).
                getTableModel()).setData(lstOperaciones); 
    }
    
    public void removeTableOperaciones()
    {
    	((TableOperacionesModel)((TableSorted)getJTableOperaciones().getModel()).getTableModel()).setData(new ArrayList());
    	((TableSorted)getJTableOperaciones().getModel()).sortingStatusChanged();    	
    	
    }
    
	public void enter() {
		// TODO Auto-generated method stub
		
	}

	public void exit() {
		// TODO Auto-generated method stub
		
	}
    
	
	
	public String createUrl(String servicio, String parametro){
		
		String nombre = "";
		StringBuffer url = new StringBuffer();
		StringBuffer cadenaBBOX = new StringBuffer();

		if (servicio != null && !servicio.equals("") && parametro != null){    	

			String urlBase = AppContext.getApplicationContext().getString("geopista.conexion.servidor.wfsmne");
			String idMunicipio = AppContext.getApplicationContext().getString("geopista.DefaultCityId");

			url = url.append(urlBase+"?");
			nombre = "app:";
			nombre += servicio;

			url = url.append("REQUEST=GetFeature&version=1.1.0&SERVICE=WFS&TYPENAME=");
			url = url.append(nombre+"&NAMESPACE=xmlns(app=http://www.deegree.org/app)");

			GeopistaEditor editor = GraphicEditorPanel.getEditor();

			if (editor != null){

				if (!parametro.equals("")){

					url = url.append("&FILTER=%3CFilter%20xmlns:gml='http://www.opengis.net/gml'%3E");

					if (getJCheckBoxResGeom().isSelected()){
						url = url.append("%3CAnd%3E");
					}

					url = url.append("%3CPropertyIsLike%20wildCard='*'%20singleChar='_'%20escapeChar='%7C'%3E%3CPropertyName%3EnombreEntidad/nombre%3C/PropertyName%3E%3CLiteral%3E");
					url = url.append(removeSpaces(parametro)+"%3C/Literal%3E%3C/PropertyIsLike%3E");

					if (getJCheckBoxResGeom().isSelected()){
						GeopistaBBOX g1 = new GeopistaBBOX();
						g1.setXmin(editor.getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates().getMinX());
						g1.setYmin(editor.getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates().getMinY());
						g1.setXmax(editor.getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates().getMaxX());
						g1.setYmax(editor.getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates().getMaxY());
						try {
							transformCoordinatesToGeographic(g1);
						} catch (Exception e) {
							e.printStackTrace();
						}
						cadenaBBOX = cadenaBBOX.append("%3CBBOX%3E%3CPropertyName%3EposicionEspacial_coordinates%3C/PropertyName%3E");
						//cadenaBBOX = cadenaBBOX.append("%3Cgml:Envelope%20srsName='EPSG:"+editor.getLayerManager().getCoordinateSystem().getEPSGCode()+"'%3E%3Cgml:lowerCorner%3E");
						cadenaBBOX = cadenaBBOX.append("%3Cgml:Envelope%20srsName='EPSG:4230'%3E%3Cgml:lowerCorner%3E");
						cadenaBBOX = cadenaBBOX.append(g1.getXmin()+"%20"+g1.getYmin());
						cadenaBBOX = cadenaBBOX.append("%3C/gml:lowerCorner%3E%3Cgml:upperCorner%3E"+g1.getXmax()+"%20");
						cadenaBBOX = cadenaBBOX.append(g1.getYmax()+"%3C/gml:upperCorner%3E%3C/gml:Envelope%3E%3C/BBOX%3E");
						url = url.append(cadenaBBOX);

						url = url.append("%3CPropertyIsLike%20wildCard='*'%20singleChar='_'%20escapeChar='|'%3E" +
								"%3CPropertyName>entidadLocal/municipio%3C/PropertyName%3E%3CLiteral%3E");
								
						url = url.append(idMunicipio);
						url = url.append("%3C/Literal%3E%3C/PropertyIsLike%3E");
						url = url.append("%3C/And%3E");
					}
					url = url.append("%3C/Filter%3E");
				}
			}
		}

		return url.toString();
    }
	
	private void transformCoordinatesToGeographic(GeopistaBBOX g1) throws Exception {
		int sridGeographic = 4230;
		CoordinateSystem geographicCoord = CoordinateSystemRegistry.instance(AppContext.getApplicationContext().getBlackboard()).get(sridGeographic);
		CoordinateSystem mapCoord = GraphicEditorPanel.getEditor().getLayerManager().getCoordinateSystem();
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

	public String createUrlFid(String servicio, String primerParametro, String segundoParametro){
		
		String nombre = "";
		StringBuffer url = new StringBuffer();
		StringBuffer cadenaBBOX = new StringBuffer();

		if (servicio != null && !servicio.equals("") && primerParametro != null && 
				segundoParametro != null){    	

			String urlBase = AppContext.getApplicationContext().getString("geopista.conexion.servidor.wfsmne");
			String idMunicipio = AppContext.getApplicationContext().getString("geopista.DefaultCityId");

			url = url.append(urlBase+"?");
			nombre = "app:";
			nombre += servicio;

			url = url.append("REQUEST=GetFeature&version=1.1.0&SERVICE=WFS&TYPENAME=");
			url = url.append(nombre+"&NAMESPACE=xmlns(app=http://www.deegree.org/app)");

			GeopistaEditor editor = GraphicEditorPanel.getEditor();

			if (editor != null){

				if (!primerParametro.equals("") && !segundoParametro.equals("")){

					url = url.append("&FILTER=%3CFilter%20xmlns:gml='http://www.opengis.net/gml'%3E");

					url = url.append("%3CAnd%3E");

					url = url.append("%3CPropertyIsLike%20wildCard='*'%20singleChar='_'%20escapeChar='%7C'%3E%3CPropertyName%3EnombreEntidad/nombre%3C/PropertyName%3E%3CLiteral%3E");
					url = url.append(removeSpaces(primerParametro)+"%3C/Literal%3E%3C/PropertyIsLike%3E");

					url = url.append("%3CPropertyIsLike%20wildCard='*'%20singleChar='_'%20escapeChar='%7C'%3E%3CPropertyName%3EentidadRelacionada/idEntidad%3C/PropertyName%3E%3CLiteral%3E");
					url = url.append(removeSpaces(segundoParametro)+"%3C/Literal%3E%3C/PropertyIsLike%3E");
					
					if (getJCheckBoxResGeom().isSelected()){
						GeopistaBBOX g1 = new GeopistaBBOX();
						g1.setXmin(editor.getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates().getMinX());
						g1.setYmin(editor.getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates().getMinY());
						g1.setXmax(editor.getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates().getMaxX());
						g1.setYmax(editor.getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates().getMaxY());
						try {
							transformCoordinatesToGeographic(g1);
						} catch (Exception e) {
							e.printStackTrace();
						}
						cadenaBBOX = cadenaBBOX.append("%3CBBOX%3E%3CPropertyName%3EposicionEspacial_coordinates%3C/PropertyName%3E");
						//cadenaBBOX = cadenaBBOX.append("%3Cgml:Envelope%20srsName='EPSG:"+editor.getLayerManager().getCoordinateSystem().getEPSGCode()+"'%3E%3Cgml:lowerCorner%3E");
						cadenaBBOX = cadenaBBOX.append("%3Cgml:Envelope%20srsName='EPSG:4230'%3E%3Cgml:lowerCorner%3E");
						cadenaBBOX = cadenaBBOX.append(g1.getXmin()+"%20"+g1.getYmin());
						cadenaBBOX = cadenaBBOX.append("%3C/gml:lowerCorner%3E%3Cgml:upperCorner%3E"+g1.getXmax()+"%20");
						cadenaBBOX = cadenaBBOX.append(g1.getYmax()+"%3C/gml:upperCorner%3E%3C/gml:Envelope%3E%3C/BBOX%3E");
						url = url.append(cadenaBBOX);
					}
					
					url = url.append("%3CPropertyIsLike%20wildCard='*'%20singleChar='_'%20escapeChar='|'%3E" +
					"%3CPropertyName>entidadLocal/municipio%3C/PropertyName%3E%3CLiteral%3E");
					
					url = url.append(idMunicipio);
					url = url.append("%3C/Literal%3E%3C/PropertyIsLike%3E");
					
					url = url.append("%3C/And%3E");
					url = url.append("%3C/Filter%3E");
				}
			}
		}

		return url.toString();
    }
	
	public void buscarResultados(String servicio, Boolean manual){
		
		if (servicio.equals(ConstantesAlp.VIA)){
			
			if (!manual.booleanValue()){
				
				Via via = (Via)blackboard.get("ElementoSeleccionado");
				
				if ( via != null){
					
					if (jRadioButtonLike!= null && jRadioButtonLike.isSelected()){
						buscarResultados(servicio, "*" + via.getNombreViaIne() + "*",null,null);
					}
					else{
						buscarResultados(servicio, via.getNombreViaIne(),null,null);
					}
				}
				
			}
			else{
				
			    String errorMessage = validarServicioSeleccionado();
			    
			    if (errorMessage != null) {
			        JOptionPane.showMessageDialog(null,errorMessage,
			            AppContext.getMessage("GeopistaName"),JOptionPane.WARNING_MESSAGE);
			        
			        wfsMne.getLstIds().clear(); 
					wfsMne.getLstGeometrias().clear();
					wfsMne.getLstResultados().clear();
					getJLabelInfoResultado().setText("");
					
			        return;
			    }
			    
			    errorMessage = busquedaManual();
			    if (errorMessage != null) {
			        JOptionPane.showMessageDialog(null,errorMessage,
			            AppContext.getMessage("GeopistaName"),JOptionPane.WARNING_MESSAGE);
			        
			        wfsMne.getLstIds().clear(); 
					wfsMne.getLstGeometrias().clear();
					wfsMne.getLstResultados().clear();
					getJLabelInfoResultado().setText("");
					
			        return;
			    }
			}
		}
		else if (servicio.equals(ConstantesAlp.NUMBER)){
			
			if (!manual.booleanValue()){
				
				NumeroPolicia numeroPolicia = (NumeroPolicia)blackboard.get("ElementoSeleccionado");
				
				if ( numeroPolicia != null){
					
					if (numeroPolicia.getNombrevia() == null || numeroPolicia.getNombrevia().length()<3){
						if (jRadioButtonLike!= null && jRadioButtonLike.isSelected()){
							buscarResultados(servicio, "*" + numeroPolicia.getNumero() + "*",null,null);
						}
						else{
							buscarResultados(servicio, numeroPolicia.getNumero(),null,null);
						}
					}
					else{
						if (jRadioButtonLike!= null && jRadioButtonLike.isSelected()){
							buscarResultados(ConstantesAlp.VIA, "*" + numeroPolicia.getNombrevia() + "*", servicio, "*" + numeroPolicia.getNumero() + "*");
						}
						else{
							buscarResultados(ConstantesAlp.VIA, numeroPolicia.getNombrevia(), servicio, numeroPolicia.getNumero());
						}						
					}
				}				
			}			
		}
	}
	
	public void centrarMapa(final int indice) 
	{
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(),
				null);
		progressDialog.setTitle(I18N.get("AlpToLocalGIS",
		"alptolocalgis.editinginfo.progress.search"));
		progressDialog.report(I18N.get("AlpToLocalGIS",
		"alptolocalgis.editinginfo.progress.searching"));
		progressDialog.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {    			
				new Thread(new Runnable(){    				
					public void run()
					{
						try
						{    						
							wfsMne.centreMap(indice, progressDialog);
						}
						catch(Exception e)
						{
							e.printStackTrace();
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
     * Genero los resultados según los criterios de entrada
     */
    public void buscarResultados(final String primerServicio, final String primerCriterio, final String segundoServicio, final String segundoCriterio) 
    {
     	
    	final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(),
    			null);
    	progressDialog.setTitle(I18N.get("AlpToLocalGIS",
    			"alptolocalgis.editinginfo.progress.search"));
    	progressDialog.report(I18N.get("AlpToLocalGIS",
    			"alptolocalgis.editinginfo.progress.searching"));
    	progressDialog.addComponentListener(new ComponentAdapter() {
    		public void componentShown(ComponentEvent e) {    			
    			new Thread(new Runnable(){    				
					public void run()
    				{
    					try
    					{    						
    						if ((segundoServicio == null) || 
    								(segundoCriterio == null)){
    							
    							wfsMne.getLstIds().clear(); 
    							wfsMne.getLstGeometrias().clear();
    							wfsMne.getLstResultados().clear();
    							getJLabelInfoResultado().setText("");
    							String url = null;             		    
    							url = createUrl(primerServicio, primerCriterio);        		    
    							wfsMne.searchResults(url,progressDialog);
    							lstIds = wfsMne.getLstIds();
    							Vector vectorResultados = new Vector();
    							for (Iterator itResul = wfsMne.getLstResultados().iterator();itResul.hasNext();){
    								vectorResultados.add(itResul.next());
    							}
    							jListResultados.setListData(vectorResultados);
    						}
    						else if ((segundoServicio != null) && 
    								(segundoCriterio != null)){
    							
    							wfsMne.getLstIds().clear(); 
    							wfsMne.getLstGeometrias().clear();
    							wfsMne.getLstResultados().clear();
    							getJLabelInfoResultado().setText("");
    							String url = null;     
    							
    							url = createUrl(primerServicio, primerCriterio);        		    
    							if (wfsMne.searchResults(url,progressDialog)){
    								lstIdsIntermedios = (ArrayList) wfsMne.getLstIds().clone();

    								Vector vectorResultados = new Vector();    	
    								lstIds = new ArrayList();
    								wfsMne.getLstIds().clear(); 
    								wfsMne.getLstGeometrias().clear();
    								wfsMne.getLstResultados().clear();
    								String[] vectorURL = new String [lstIdsIntermedios.size()];//    							
    								for (int indice = 0; indice<lstIdsIntermedios.size(); indice++){    								
    									url = createUrlFid(segundoServicio, segundoCriterio, (String)lstIdsIntermedios.get(indice));
    									vectorURL[indice] = url;    								
    								}
    								
    								if (lstIdsIntermedios.size()>0){
    									wfsMne.searchResults(vectorURL,progressDialog);
    									lstIds = wfsMne.getLstIds();
    									for (Iterator itResul = wfsMne.getLstResultados().iterator();itResul.hasNext();){
    										String resultado = (String)itResul.next();
    										vectorResultados.add(resultado);
    									}
    								}

        							jListResultados.setListData(vectorResultados);
    							}
    							else{
    								   	
    								lstIds = new ArrayList();
    								wfsMne.getLstIds().clear(); 
    								wfsMne.getLstGeometrias().clear();
    								wfsMne.getLstResultados().clear();
    							}
    						}

    					}catch(Exception e)
    					{
    						e.printStackTrace();
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
    
    private JLabel getJLabelInfoResultado(){
    	
    	if (jLabelInfoResultado == null){
    	
    		jLabelInfoResultado = new JLabel("",JLabel.CENTER);
    	}
    	return jLabelInfoResultado;
    }
    
    public void createInformation(int index, String resultadoSeleccionado){
    	    	
    	String fidEntidad = null;
    	if (lstIds != null){
    		fidEntidad = (String)lstIds.get(index);
    	}
    	getJLabelInfoResultado().setText(wfsMne.createInformation(fidEntidad, index, resultadoSeleccionado));
        
    }
    
    /**
     * This method initializes jTabbedPaneExpediente
     *
     * @return javax.swing.JTabbedPane
     */
    private JTabbedPane getJTabbedPaneBusquedas()
    {
        if (jTabbedPaneBusquedas == null)
        {
        	jTabbedPaneBusquedas  = new JTabbedPane();
        	jTabbedPaneBusquedas.setPreferredSize(new Dimension(30, 30));
        	jTabbedPaneBusquedas.setMaximumSize(jTabbedPaneBusquedas.getPreferredSize());
        	jTabbedPaneBusquedas.setMinimumSize(jTabbedPaneBusquedas.getPreferredSize());
		            	
        	jTabbedPaneBusquedas.addChangeListener(new javax.swing.event.ChangeListener() {

                public void stateChanged(ChangeEvent e)
                {
                	//Acciones a realizar al pincher en las pestañas
                }
            });

        	
        	jTabbedPaneBusquedas.addTab(I18N.get("AlpToLocalGIS","alptolocalgis.editinginfo.tabbedpane.street.title"),
                    null, getJPanelBusqueda(), null);
        	
        	jTabbedPaneBusquedas.addTab(I18N.get("AlpToLocalGIS","alptolocalgis.editinginfo.tabbedpane.info.title"),
                    null, getJPanelDatos(), null);
        	            
            //Deshabilita todas las pestañas al cargar el árbol
        	jTabbedPaneBusquedas.setSelectedComponent(getJPanelBusqueda());
        	jTabbedPaneBusquedas.setEnabledAt(jTabbedPaneBusquedas.indexOfComponent(getJPanelDatos()), true);
            
        }
        return jTabbedPaneBusquedas;
    }

    private String validarServicioSeleccionado(){
    	
    	String mensaje = null;
    	if (getJComboBoxServicios().isEnabled()){
	    	if (getJComboBoxServicios().isEnabled() && (getJComboBoxServicios().getSelectedItem() == null ||
	    			getJComboBoxServicios().getSelectedItem().toString()== null ||
	    			getJComboBoxServicios().getSelectedItem().toString().equals(""))){
	    		
	    		mensaje = I18N.get("AlpToLocalGIS", 
				"alptolocalgis.message.error.notselectedservice");
	    	}
    	}
    	else{
    		mensaje = I18N.get("AlpToLocalGIS", 
			"alptolocalgis.message.error.notenableservice");
    	}
    	
    	return mensaje;
    }
    
    private String busquedaManual(){
    	
    	String mensaje = null;
    	String servicioSeleccionado = null;

    	if (getJComboBoxServicios().isEnabled()){

    		if (getJComboBoxServicios().getSelectedItem() != null && 
    				getJComboBoxServicios().getSelectedItem().toString()!= null){

    			servicioSeleccionado = getJComboBoxServicios().getSelectedItem().toString();

    			if ((servicioSeleccionado.equals(ConstantesAlp.VIA))){
    				if((getJTextFieldNombreVia().getText().equals("") ||
    						getJTextFieldNombreVia().getText().length()<2)){

    					mensaje = I18N.get("AlpToLocalGIS", 
    					"alptolocalgis.message.error.errorparamettervia");
    				}
    				else{

    					if (jRadioButtonLike!= null && jRadioButtonLike.isSelected()){
    						buscarResultados(ConstantesAlp.VIA, "*" + getJTextFieldNombreVia().getText() + "*", null, null);
						}
						else{
							buscarResultados(ConstantesAlp.VIA, getJTextFieldNombreVia().getText(), null, null);
						}    					
    				}
    			}
    			else if ((servicioSeleccionado.equals(ConstantesAlp.NUMBER))){

    				if((getJTextFieldNombreVia().getText().equals("") ||
    						getJTextFieldNombreVia().getText().length()<2)){

    					if (jRadioButtonLike!= null && jRadioButtonLike.isSelected()){
    						buscarResultados(ConstantesAlp.NUMBER, "*" + getJTextFieldNombrePortal().getText() + "*", null, null);
						}
						else{
							buscarResultados(ConstantesAlp.NUMBER, getJTextFieldNombrePortal().getText(), null, null);
						}    					
    				}
    				else{

    					if (jRadioButtonLike!= null && jRadioButtonLike.isSelected()){
    						buscarResultados(ConstantesAlp.VIA, "*" + getJTextFieldNombreVia().getText() + "*", ConstantesAlp.NUMBER, "*" + getJTextFieldNombrePortal().getText() + "*");
						}
						else{
							buscarResultados(ConstantesAlp.VIA, getJTextFieldNombreVia().getText(), ConstantesAlp.NUMBER, getJTextFieldNombrePortal().getText());
						}      					
    				}
    			}
    		}
    	}

    	return mensaje;
    }
    
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
    
    public boolean applySecurityPolicy()
    {
		try
        {
            if (ConstantesAlp.principal == null || ConstantesAlp.permisos == null)
            {
            	return false;
			}
			            
            if (ConstantesAlp.permisos.containsKey("LocalGis.ALP.Synchronize"))
            {
               getJButtonEliminar().setEnabled(true);
               getJButtonSincronizar().setEnabled(true);
            }
           
            return true;
		}
        catch (Exception ex)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
			return false;
		}

	}
    
    private void disableButtons() {
		
    	getJButtonEliminar().setEnabled(false);
    	getJButtonSincronizar().setEnabled(false);
    	
	}
    
    private void sincronizar(){
    	
    	final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(),
				null);
		progressDialog.setTitle(I18N.get("AlpToLocalGIS",
		"alptolocalgis.editinginfo.progress.synchronize"));
		progressDialog.report(I18N.get("AlpToLocalGIS",
		"alptolocalgis.editinginfo.progress.synchronizing"));
		progressDialog.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {    			
				new Thread(new Runnable(){    				
					public void run()
					{
						try
						{    		   
							OperacionAlp operacion = (OperacionAlp)blackboard.get("operacion");
							
							if (operacion != null){
								
								GeopistaEditor editor = GraphicEditorPanel.getEditor();
								
								if (operacion.getTipoOperacion().equals(ConstantesAlp.ACTION_ADD_VIA)){
									
									Layer layer = editor.getLayerManager().getLayer("vias");									
									ArrayList lstFeatures = (ArrayList) editor.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems();

									if (lstFeatures != null && lstFeatures.size()==1){

										Feature selectedFeature = (Feature) lstFeatures.get(0);
										if (selectedFeature instanceof GeopistaFeature){
											((GeopistaFeature)selectedFeature).setNew(true);
											if (!((GeopistaFeature)selectedFeature).getLayer().getSystemId().equals("vias") &&
													!((GeopistaFeature)selectedFeature).getLayer().getSystemId().equals("tramosvia") && 
													!((GeopistaFeature)selectedFeature).getLayer().getSystemId().equals("numeros_policia")){

												if (sincronizarVia(layer, selectedFeature, operacion, progressDialog)){
													GraphicEditorPanel.getEditor().getLayerViewPanel().getSelectionManager().clear();
													Envelope envelope = GraphicEditorPanel.getEditor().getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates();
													GraphicEditorPanel.getEditor().getLayerViewPanel().getViewport().zoom(envelope);
													removeOperacionBD(operacion.getIdOperacion());
													getJComboBoxTipoOperaciones().setSelectedItem(getJComboBoxTipoOperaciones().getSelectedItem());

												}
											}
											else{
												//Mensaje pidiendo ques e seleccione una única feature
												JOptionPane.showMessageDialog(null,I18N.get("AlpToLocalGIS",
												"alptolocalgis.editinginfo.synchronize.warning.errorlayer"),
												AppContext.getMessage("GeopistaName"),JOptionPane.ERROR_MESSAGE);
											}
										}										
									}
									else{
										//Mensaje pidiendo ques e seleccione una única feature
										JOptionPane.showMessageDialog(null,I18N.get("AlpToLocalGIS",
										"alptolocalgis.editinginfo.synchronize.warning.numberfeatures"),
										AppContext.getMessage("GeopistaName"),JOptionPane.ERROR_MESSAGE);
									}
								}
								else if (operacion.getTipoOperacion().equals(ConstantesAlp.ACTION_ADD_NUMERO_POLICIA)){
									
									Layer layer = editor.getLayerManager().getLayer("numeros_policia");	
									ArrayList lstFeatures = (ArrayList) editor.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems();

									if (lstFeatures != null && lstFeatures.size()==1){

										Feature selectedFeature = (Feature) lstFeatures.get(0);
										if (selectedFeature instanceof GeopistaFeature){
											((GeopistaFeature)selectedFeature).setNew(true);
											if (!((GeopistaFeature)selectedFeature).getLayer().getSystemId().equals("vias") &&
													!((GeopistaFeature)selectedFeature).getLayer().getSystemId().equals("tramosvia") && 
													!((GeopistaFeature)selectedFeature).getLayer().getSystemId().equals("numeros_policia")){

												if (sincronizarNumeroPolicia(layer, selectedFeature, operacion, progressDialog)){
													GraphicEditorPanel.getEditor().getLayerViewPanel().getSelectionManager().clear();
													Envelope envelope = GraphicEditorPanel.getEditor().getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates();
													GraphicEditorPanel.getEditor().getLayerViewPanel().getViewport().zoom(envelope);
													removeOperacionBD(operacion.getIdOperacion());
													getJComboBoxTipoOperaciones().setSelectedItem(getJComboBoxTipoOperaciones().getSelectedItem());
												}
											}
											else{
												//Mensaje pidiendo ques e seleccione una única feature
												JOptionPane.showMessageDialog(null,I18N.get("AlpToLocalGIS",
												"alptolocalgis.editinginfo.synchronize.warning.errorlayer"),
												AppContext.getMessage("GeopistaName"),JOptionPane.ERROR_MESSAGE);
											}
										}
									}
									else{
										//Mensaje pidiendo ques e seleccione una única feature
										JOptionPane.showMessageDialog(null,I18N.get("AlpToLocalGIS",
										"alptolocalgis.editinginfo.synchronize.warning.numberfeatures"),
										AppContext.getMessage("GeopistaName"),JOptionPane.ERROR_MESSAGE);
									}

								}
								else if (operacion.getTipoOperacion().equals(ConstantesAlp.ACTION_MOD_VIA)){

									Layer layer = editor.getLayerManager().getLayer("vias");									
									ArrayList lstFeatures = (ArrayList) editor.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems();

									if (lstFeatures != null && lstFeatures.size()==1){

										Feature selectedFeature = (Feature) lstFeatures.get(0);
																				
										if (selectedFeature instanceof GeopistaFeature && 
												((GeopistaFeature)selectedFeature).getLayer().getSystemId().equals("vias")){											
											if (sincronizarVia(layer, selectedFeature, operacion, progressDialog)){
												GraphicEditorPanel.getEditor().getLayerViewPanel().getSelectionManager().clear();
												Envelope envelope = GraphicEditorPanel.getEditor().getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates();
						    					GraphicEditorPanel.getEditor().getLayerViewPanel().getViewport().zoom(envelope);
												removeOperacionBD(operacion.getIdOperacion());											
												getJComboBoxTipoOperaciones().setSelectedItem(getJComboBoxTipoOperaciones().getSelectedItem());
											}
										}
										else{
											JOptionPane.showMessageDialog(null,I18N.get("AlpToLocalGIS",
											"alptolocalgis.editinginfo.synchronize.warning.errorlayer"),
											AppContext.getMessage("GeopistaName"),JOptionPane.ERROR_MESSAGE);
										}										
									}
									else{
										//Mensaje pidiendo ques e seleccione una única feature
										JOptionPane.showMessageDialog(null,I18N.get("AlpToLocalGIS",
										"alptolocalgis.editinginfo.synchronize.warning.numberfeatures"),
										AppContext.getMessage("GeopistaName"),JOptionPane.ERROR_MESSAGE);
									}
								}
								else if (operacion.getTipoOperacion().equals(ConstantesAlp.ACTION_MOD_NUMERO_POLICIA)){

									Layer layer = editor.getLayerManager().getLayer("numeros_policia");									
									ArrayList lstFeatures = (ArrayList) editor.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems();

									if (lstFeatures != null && lstFeatures.size()==1){

										Feature selectedFeature = (Feature) lstFeatures.get(0);
										
										if (selectedFeature instanceof GeopistaFeature && 
												((GeopistaFeature)selectedFeature).getLayer().getSystemId().equals("numeros_policia")){											
											if (sincronizarNumeroPolicia(layer, selectedFeature, operacion, progressDialog)){
												GraphicEditorPanel.getEditor().getLayerViewPanel().getSelectionManager().clear();
												Envelope envelope = GraphicEditorPanel.getEditor().getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates();
						    					GraphicEditorPanel.getEditor().getLayerViewPanel().getViewport().zoom(envelope);
												removeOperacionBD(operacion.getIdOperacion());											
												getJComboBoxTipoOperaciones().setSelectedItem(getJComboBoxTipoOperaciones().getSelectedItem());
											}
										}
										else{
											JOptionPane.showMessageDialog(null,I18N.get("AlpToLocalGIS",
											"alptolocalgis.editinginfo.synchronize.warning.errorlayer"),
											AppContext.getMessage("GeopistaName"),JOptionPane.ERROR_MESSAGE);
										}										
									}
									else{
										//Mensaje pidiendo ques e seleccione una única feature
										JOptionPane.showMessageDialog(null,I18N.get("AlpToLocalGIS",
										"alptolocalgis.editinginfo.synchronize.warning.numberfeatures"),
										AppContext.getMessage("GeopistaName"),JOptionPane.ERROR_MESSAGE);
									}
								}
								else if (operacion.getTipoOperacion().equals(ConstantesAlp.ACTION_DEL_VIA)){

									Layer layer = editor.getLayerManager().getLayer("vias");									
									ArrayList lstFeatures = (ArrayList) editor.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems();

									if (lstFeatures != null && lstFeatures.size()==1){

										Feature selectedFeature = (Feature) lstFeatures.get(0);
										if (selectedFeature instanceof GeopistaFeature){
											((GeopistaFeature)selectedFeature).setDeleted(true);
										}
										
										if (selectedFeature instanceof GeopistaFeature && 
												((GeopistaFeature)selectedFeature).getLayer().getSystemId().equals("vias")){											
											if (sincronizarVia(layer, selectedFeature, operacion, progressDialog)){
												GraphicEditorPanel.getEditor().getLayerViewPanel().getSelectionManager().clear();
												Envelope envelope = GraphicEditorPanel.getEditor().getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates();
						    					GraphicEditorPanel.getEditor().getLayerViewPanel().getViewport().zoom(envelope);
												removeOperacionBD(operacion.getIdOperacion());
												getJComboBoxTipoOperaciones().setSelectedItem(getJComboBoxTipoOperaciones().getSelectedItem());
											}
											
										}
										else{
											JOptionPane.showMessageDialog(null,I18N.get("AlpToLocalGIS",
											"alptolocalgis.editinginfo.synchronize.warning.errorlayer"),
											AppContext.getMessage("GeopistaName"),JOptionPane.ERROR_MESSAGE);
										}										
									}
									else{
										//Mensaje pidiendo ques e seleccione una única feature
										JOptionPane.showMessageDialog(null,I18N.get("AlpToLocalGIS",
										"alptolocalgis.editinginfo.synchronize.warning.numberfeatures"),
										AppContext.getMessage("GeopistaName"),JOptionPane.ERROR_MESSAGE);
									}
								}
								else if (operacion.getTipoOperacion().equals(ConstantesAlp.ACTION_DEL_NUMERO_POLICIA)){

									Layer layer = editor.getLayerManager().getLayer("numeros_policia");									
									ArrayList lstFeatures = (ArrayList) editor.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems();

									if (lstFeatures != null && lstFeatures.size()==1){

										Feature selectedFeature = (Feature) lstFeatures.get(0);
										if (selectedFeature instanceof GeopistaFeature){
											((GeopistaFeature)selectedFeature).setDeleted(true);
										}
										
										if (selectedFeature instanceof GeopistaFeature && 
												((GeopistaFeature)selectedFeature).getLayer().getSystemId().equals("numeros_policia")){											
											if (sincronizarNumeroPolicia(layer, selectedFeature, operacion, progressDialog)){
												GraphicEditorPanel.getEditor().getLayerViewPanel().getSelectionManager().clear();
												Envelope envelope = GraphicEditorPanel.getEditor().getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates();
						    					GraphicEditorPanel.getEditor().getLayerViewPanel().getViewport().zoom(envelope);
												removeOperacionBD(operacion.getIdOperacion());											
												getJComboBoxTipoOperaciones().setSelectedItem(getJComboBoxTipoOperaciones().getSelectedItem());
											}
										}
										else{
											JOptionPane.showMessageDialog(null,I18N.get("AlpToLocalGIS",
											"alptolocalgis.editinginfo.synchronize.warning.errorlayer"),
											AppContext.getMessage("GeopistaName"),JOptionPane.ERROR_MESSAGE);
										}										
									}
									else{
										//Mensaje pidiendo ques e seleccione una única feature
										JOptionPane.showMessageDialog(null,I18N.get("AlpToLocalGIS",
										"alptolocalgis.editinginfo.synchronize.warning.numberfeatures"),
										AppContext.getMessage("GeopistaName"),JOptionPane.ERROR_MESSAGE);
									}
								}
							}
						}
						catch(Exception e)
						{
							e.printStackTrace();
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
        GraphicEditorPanel.getEditor().getLayerViewPanel().repaint();
    }
    
    private boolean sincronizarVia(Layer layer, Feature selectedFeature, OperacionAlp operacion, TaskMonitorDialog progressDialog){
    	
    	try {
    		
    		GeopistaSchema featureSchema = (GeopistaSchema) layer.getFeatureCollectionWrapper().getFeatureSchema();

//    		String idVia = featureSchema.getAttributeByColumn("id");
    		String idAlp = featureSchema.getAttributeByColumn("idalp");
    		String tipoViaINE = featureSchema.getAttributeByColumn("tipoviaine");
    		String nombreViaINE = featureSchema.getAttributeByColumn("nombreviaine");
    		String nombreViaCortoINE = featureSchema.getAttributeByColumn("nombreviacortoine");
    		String codigoINE = featureSchema.getAttributeByColumn("codigoine");
    		String nombreCatastro = featureSchema.getAttributeByColumn("nombrecatastro");
    		String codigoCatastro = featureSchema.getAttributeByColumn("codigocatastro");
    		String fechaGrabacionAyto = featureSchema.getAttributeByColumn("fechagrabacionayto");
    		String fechaGrabacionCierre = featureSchema.getAttributeByColumn("fechagrabacioncierre");
    		String fechaEjecucion = featureSchema.getAttributeByColumn("fechaejecucion");									

    		Feature feature = null;
    		if (operacion.getTipoOperacion().equals(ConstantesAlp.ACTION_ADD_VIA)){
    			feature = new BasicFeature(layer.getFeatureCollectionWrapper().getFeatureSchema());
    			feature.setGeometry(selectedFeature.getGeometry());
    		}
    		else{
    			feature = selectedFeature; 
    		}

    		feature.setSchema(layer.getFeatureCollectionWrapper().getFeatureSchema());

    		XML2Java converter = new XML2Java();   
    		converter.removeCustomConverter(java.util.Date.class);
    		converter.addCustomConverter(Date.class, Via.getDateCustomConverter());
    		Via via = (Via)converter.read(operacion.getXml(),Via.class);

//    		feature.setAttribute(idVia, via.getId());
    		feature.setAttribute(idAlp, via.getIdalp());
    		feature.setAttribute(tipoViaINE, via.getTipoViaIne());
    		feature.setAttribute(nombreViaINE, via.getNombreViaIne());
    		feature.setAttribute(nombreViaCortoINE, via.getNombreViaCortoIne());
    		feature.setAttribute(codigoINE, via.getCodigoIne());
    		feature.setAttribute(nombreCatastro, via.getNombreCatastro());
    		feature.setAttribute(codigoCatastro, via.getCodigoCatastro());
    		feature.setAttribute(fechaGrabacionAyto, via.getFechaGrabacionAyto());
    		feature.setAttribute(fechaGrabacionCierre, via.getFechaGrabacionCierre());
    		feature.setAttribute(fechaEjecucion, via.getFechaEjecucion());

    		((GeopistaFeature) feature).setLayer((GeopistaLayer)layer);
    		
    		if (operacion.getTipoOperacion().equals(ConstantesAlp.ACTION_DEL_VIA)){
    			((GeopistaFeature) feature).setDeleted(true);
    		}
    		
    		if (!sincronizarFeature(layer, feature, progressDialog, operacion)){
    			return false;
    		}
    		else{
    			return true;
    		}

    	} catch (Exception e) {
    		e.printStackTrace();
    		JOptionPane.showMessageDialog(null,I18N.get("AlpToLocalGIS",
			"alptolocalgis.editinginfo.synchronize.error"),
			AppContext.getMessage("GeopistaName"),JOptionPane.ERROR_MESSAGE);
    		return false;
    	} 

    }
    
    private boolean sincronizarNumeroPolicia(Layer layer, Feature selectedFeature, OperacionAlp operacion, TaskMonitorDialog progressDialog){
    	
    	try {
    		
    		GeopistaSchema featureSchema = (GeopistaSchema) layer.getFeatureCollectionWrapper().getFeatureSchema();

    		String idNumeroPolicia = featureSchema.getAttributeByColumn("id");
    		String idAlp = featureSchema.getAttributeByColumn("idalp");
    		String idVia = featureSchema.getAttributeByColumn("id_via");
    		String calificador = featureSchema.getAttributeByColumn("calificador");
    		String numero = featureSchema.getAttributeByColumn("numero");
    		String fechaEjecucion = featureSchema.getAttributeByColumn("fechaejecucion");									

    		Feature feature = null;
    		if (operacion.getTipoOperacion().equals(ConstantesAlp.ACTION_ADD_NUMERO_POLICIA)){
    			feature = new BasicFeature(layer.getFeatureCollectionWrapper().getFeatureSchema());
    			feature.setGeometry(selectedFeature.getGeometry());
    		}
    		else{
    			feature = selectedFeature; 
    		}
    		
    		feature.setSchema(layer.getFeatureCollectionWrapper().getFeatureSchema());

    		XML2Java converter = new XML2Java();   
    		converter.removeCustomConverter(java.util.Date.class);
    		converter.addCustomConverter(Date.class, NumeroPolicia.getDateCustomConverter());
    		NumeroPolicia numeroPolicia = (NumeroPolicia)converter.read(operacion.getXml(),NumeroPolicia.class);

    		feature.setAttribute(idNumeroPolicia, numeroPolicia.getId());
    		feature.setAttribute(idAlp, numeroPolicia.getIdalp());
    		feature.setAttribute(idVia, numeroPolicia.getId_via());
    		feature.setAttribute(calificador,numeroPolicia.getCalificador());
    		feature.setAttribute(numero, numeroPolicia.getNumero());
    		feature.setAttribute(fechaEjecucion,numeroPolicia.getFechaEjecucion());

    		((GeopistaFeature) feature).setLayer((GeopistaLayer)layer);
    		
    		if (operacion.getTipoOperacion().equals(ConstantesAlp.ACTION_DEL_NUMERO_POLICIA)){
    			((GeopistaFeature)feature).setDeleted(true);
    		}

    		if (!sincronizarFeature(layer, feature, progressDialog, operacion)){
    			return false;
    		}
    		else{
    			return true;
    		}

    	} catch (Exception e) {
    		e.printStackTrace();
    		JOptionPane.showMessageDialog(null,I18N.get("AlpToLocalGIS",
			"alptolocalgis.editinginfo.synchronize.error"),
			AppContext.getMessage("GeopistaName"),JOptionPane.ERROR_MESSAGE);
    		return false;
    	} 

    }
    
    private boolean sincronizarFeature(Layer layer, Feature feature, TaskMonitorDialog progressDialog, OperacionAlp operacion) throws Exception{
    	
		boolean doExecute = false;

		SchemaValidator validator= new SchemaValidator(null);

		//Coprobación de si los datos cumplen con los dominios

		if(!(validator.validateFeature(feature))){

			boolean resultAttributes = GeopistaValidatePlugin.showFeatureDialog(
					feature, ((GeopistaFeature) feature).getLayer());
			if (resultAttributes == true)
			{
				doExecute=true;	
			}
			else{
				doExecute = false;
			}
		}
		else{
			doExecute = true;
		}
		if(doExecute){


			FeatureCollection featureCollection = layer.getFeatureCollectionWrapper().getUltimateWrappee();
			layer.getLayerManager().setFiringEvents(true);
			if (operacion.getTipoOperacion().equals(ConstantesAlp.ACTION_ADD_VIA) || 
					operacion.getTipoOperacion().equals(ConstantesAlp.ACTION_ADD_NUMERO_POLICIA)){				
				featureCollection.add(feature);
			}
			
			GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) layer
			.getDataSourceQuery().getDataSource();
			Map driverProperties = geopistaServerDataSource.getProperties();
			Object lastResfreshValue = driverProperties.get(GeopistaConnection.REFRESH_INSERT_FEATURES);
			Object lastValidateAbstractValue = driverProperties.get(GeopistaConnection.VALIDATE_ABSTRACT_FEATURE);
			try
			{
				driverProperties.put(GeopistaConnection.REFRESH_INSERT_FEATURES,new Boolean(true));
				driverProperties.put(GeopistaConnection.VALIDATE_ABSTRACT_FEATURE,new Boolean(false));
				geopistaServerDataSource.getConnection()
				.executeUpdate(layer.getDataSourceQuery().getQuery(),
						featureCollection, progressDialog);
				
				if (operacion.getTipoOperacion().equals(ConstantesAlp.ACTION_DEL_VIA) || 
						operacion.getTipoOperacion().equals(ConstantesAlp.ACTION_DEL_NUMERO_POLICIA)){
					featureCollection.remove(feature);					
				}

				return true;

			}
			catch(Exception ex){
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null,I18N.get("AlpToLocalGIS",
						"alptolocalgis.editinginfo.synchronize.error"),
						AppContext.getMessage("GeopistaName"),JOptionPane.ERROR_MESSAGE);
				return false;
			}
			finally
			{
				if(lastResfreshValue!=null)
				{
					driverProperties.put(GeopistaConnection.REFRESH_INSERT_FEATURES,lastResfreshValue);
				}
				else
				{
					driverProperties.remove(GeopistaConnection.REFRESH_INSERT_FEATURES);
				}
				if (lastValidateAbstractValue != null){
					driverProperties.put(GeopistaConnection.VALIDATE_ABSTRACT_FEATURE,lastResfreshValue);
				}
				else{
					driverProperties.remove(GeopistaConnection.VALIDATE_ABSTRACT_FEATURE);
				}
			} 
		}
    	
		return false;
    }
    
}  //  @jve:decl-index=0:visual-constraint="50,-16"
