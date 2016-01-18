package com.geopista.ui.dialogs.mobile;


import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.geopista.app.AppContext;
import com.geopista.model.GeopistaMap;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.ui.dialogs.beans.ExtractionProject;
import com.geopista.ui.dialogs.global.Constants;
import com.geopista.ui.plugin.mobile.MobileAssignCellsPlugin;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;


public class MobileAssignCellsPanel01  extends JPanel implements WizardPanel
{
    private Blackboard blackboard  = Constants.APLICACION.getBlackboard();
    private String localId = null;
    private WizardContext wizardContext;
    private PlugInContext context;
    /**
     * @return null to turn the Next button into a Finish button
     */
    private String nextID = null;
	private JScrollPane jspanelProjectList;
	private JPanel jpanelExtractInfo;
	private JList jlistProjects;
	private List<Object> extractProjects; //proyectos de extracción sobre el mapa
	private JPanel jProjectIdPanel;
	private JPanel jProjectFechaPanel;
	private JPanel jProjectCeldasXPanel;
	private JPanel jProjectCeldasYPanel;
	private JPanel jProjectPosEsquinaXPanel;
	private JPanel jProjectPosEsquinaYPanel;

	public static final String SELECTED_EXTRACT_PROJECT = "SELECTED_EXTRACT_PROJECT";

    public MobileAssignCellsPanel01 (String id, String nextId, PlugInContext context)
    {
        this.nextID = nextId;
        this.localId = id;
        this.context = context; 
    }
    
	  public void enteredFromLeft(Map dataMap)
	  {
	      if(!Constants.APLICACION.isLogged())
	      {
	    	  Constants.APLICACION.login();
	      }
	      if(!Constants.APLICACION.isLogged())
	      {
	          wizardContext.cancelWizard();
	          return;
	      }
	      
	      try
	      {
	           jbInit();
	      }
	      catch(Exception e)
	      {
	          this.add(new JLabel(e.getMessage()));
	      }
	  }
    
    private void jbInit() throws Exception
    {
    	this.removeAll();
        
    	this.setLayout(new GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.weighty = 1.0;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints1.ipadx = -21;
        gridBagConstraints1.ipady = 141;
        java.awt.GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints2.ipadx = -21;
        gridBagConstraints2.ipady = 141;
        gridBagConstraints2.insets = new java.awt.Insets(5,5,5,5);
        this.add(getExtractProjectsList(), gridBagConstraints1);
        this.add(getExtractProjectsInfo(), gridBagConstraints2);
    }

    /**
     * Panel con la lista de proyectos de extracción
     * @return
     * @throws Exception 
     */
	private JScrollPane getExtractProjectsList() throws Exception {
		if(jspanelProjectList==null){
			jspanelProjectList = new JScrollPane();
		    //obtenemos los proyectos de extracción para el mapa abierto
			final String sUrlPrefix = Constants.APLICACION.getString("geopista.conexion.servidor");
		    AdministradorCartografiaClient administradorCartografiaClient = new AdministradorCartografiaClient(
		    	 sUrlPrefix + "AdministradorCartografiaServlet");
		    GeopistaMap geoMap = (GeopistaMap) context.getTask();

		    try {
		    	//TODO SATEC corregir problema id municipio
		    	extractProjects = administradorCartografiaClient.getExtractProjects(Integer.parseInt(geoMap.getSystemId()));
		    	
		    }catch (Exception e) {
		    	throw new Exception("No existen proyectos de extraccion.", e);
			}
		    if(extractProjects==null || extractProjects.size()==0){
		    	throw new Exception("No existen proyectos de extraccion.");
		    }
		    //*********************************
		    //añadimos a la lista los proyectos
		    //*********************************
		    jlistProjects = addExtractionProjectsList(extractProjects);

		    //*********************************
		    //listener para mostrar info del proyecto seleccionado en la parte de abajo de la ventana
		    //*********************************
		    jlistProjects.addListSelectionListener(new ListSelectionListener() {
		        public void valueChanged(ListSelectionEvent evt) {
		          //obtenemos el proyecto seleccionado
		          ExtractionProject extProject = (ExtractionProject) extractProjects.get(jlistProjects.getSelectedIndex());
		          ((JTextField)jProjectIdPanel.getComponent(1)).setText(extProject.getIdProyecto());
		          ((JTextField)jProjectFechaPanel.getComponent(1)).setText(extProject.getFechaExtraccionFormateada());
		          ((JTextField)jProjectPosEsquinaXPanel.getComponent(1)).setText(String.valueOf(extProject.getPosEsquinaX()));
		          ((JTextField)jProjectPosEsquinaYPanel.getComponent(1)).setText(String.valueOf(extProject.getPosEsquinaY()));
		          ((JTextField)jProjectCeldasXPanel.getComponent(1)).setText(String.valueOf(extProject.getCeldasX()));
		          ((JTextField)jProjectCeldasYPanel.getComponent(1)).setText(String.valueOf(extProject.getCeldasY()));
		          //creamos la cuadrícula y mostramos el mapa
		          
		          MobileAssignCellsPanel02.crearCentrarCuadricula(context, 
		        		  extProject.getAnchoCeldas(), extProject.getAltoCeldas(), 
		        		  extProject.getCeldasX(), extProject.getCeldasY(), 
		        		  new Coordinate(extProject.getPosEsquinaX(), extProject.getPosEsquinaY()));
		          
		          wizardContext.inputChanged(); //indicamos que ya se puede habilitar el boton
		        }
		    });
		    
		    jspanelProjectList.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		    jspanelProjectList.setViewportView(jlistProjects);	    
		}
	    return jspanelProjectList;
	}
	
	/**
	 * Crea una lista para añadir los proyectos de extracción
	 * @param extractProjects
	 * @return
	 */
    private JList addExtractionProjectsList(List<Object> extractProjects) {
    	jlistProjects = new JList();
		DefaultListModel mod = new DefaultListModel();
		jlistProjects.setModel(mod);
	    ExtractionProject curProject = null;
	    if(extractProjects==null){return jlistProjects;}
	    for (int i = 0; i < extractProjects.size(); i++) {
		    curProject = (ExtractionProject) extractProjects.get(i);
		    mod.addElement(curProject);
	    }
	    return jlistProjects;
	}

    /**
     * Panel de información de los proyectos seleccionados
     * @return
     */
	private JPanel getExtractProjectsInfo() {


        if(jpanelExtractInfo==null){
    		jpanelExtractInfo = new JPanel();
    		jpanelExtractInfo.setLayout(new GridLayout(3,2));
	    	jpanelExtractInfo.setBorder(BorderFactory.createTitledBorder(I18N.get(MobileAssignCellsPlugin.PluginMobileExtracti18n,MobilePluginI18NResource.MobileAssignCellsPanel01_infoProyect)));
	    	//creamos y añadimos elementos al panel
	    	//id
	    	jProjectIdPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    	jProjectIdPanel.add(new JLabel(I18N.get(MobileAssignCellsPlugin.PluginMobileExtracti18n,MobilePluginI18NResource.MobileAssignCellsPanel01_id)));
	    	JTextField jtextProjectId = new JTextField(10);
	    	jtextProjectId.setEditable(false);
	    	jProjectIdPanel.add(jtextProjectId);
	    	//fecha
	    	jProjectFechaPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    	jProjectFechaPanel.add(new JLabel(I18N.get(MobileAssignCellsPlugin.PluginMobileExtracti18n,MobilePluginI18NResource.MobileAssignCellsPanel01_fecha)));
	    	JTextField jtextProjectFecha = new JTextField(13);
	    	jtextProjectFecha.setEditable(false);
	    	jProjectFechaPanel.add(jtextProjectFecha);
	    	//posEsquinaX
	    	jProjectPosEsquinaXPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    	jProjectPosEsquinaXPanel.add(new JLabel(I18N.get(MobileAssignCellsPlugin.PluginMobileExtracti18n,MobilePluginI18NResource.MobileAssignCellsPanel01_esqX)));
	    	JTextField jtextProjectPosEsquinaX = new JTextField(12);
	    	jtextProjectPosEsquinaX.setEditable(false);
	    	jProjectPosEsquinaXPanel.add(jtextProjectPosEsquinaX);
	    	//posEsquinaY
	    	jProjectPosEsquinaYPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    	jProjectPosEsquinaYPanel.add(new JLabel(I18N.get(MobileAssignCellsPlugin.PluginMobileExtracti18n,MobilePluginI18NResource.MobileAssignCellsPanel01_esqy)));
	    	JTextField jtextProjectPosEsquinaY = new JTextField(12);
	    	jtextProjectPosEsquinaY.setEditable(false);
	    	jProjectPosEsquinaYPanel.add(jtextProjectPosEsquinaY);
	    	//celdas X
	    	jProjectCeldasXPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    	jProjectCeldasXPanel.add(new JLabel(I18N.get(MobileAssignCellsPlugin.PluginMobileExtracti18n,MobilePluginI18NResource.MobileAssignCellsPanel01_celdX)));
	    	JTextField jtextProjectCeldasX = new JTextField(5);
	    	jtextProjectCeldasX.setEditable(false);
	    	jProjectCeldasXPanel.add(jtextProjectCeldasX);
	    	//celdas Y
	    	jProjectCeldasYPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    	jProjectCeldasYPanel.add(new JLabel(I18N.get(MobileAssignCellsPlugin.PluginMobileExtracti18n,MobilePluginI18NResource.MobileAssignCellsPanel01_celdY)));
	    	JTextField jtextProjectCeldasY = new JTextField(5);
	    	jtextProjectCeldasY.setEditable(false);
	    	jProjectCeldasYPanel.add(jtextProjectCeldasY);
	    	//añadiendo paneles
	    	jpanelExtractInfo.add(jProjectIdPanel);
	    	jpanelExtractInfo.add(jProjectFechaPanel);
	    	jpanelExtractInfo.add(jProjectPosEsquinaXPanel);
	    	jpanelExtractInfo.add(jProjectCeldasXPanel);
	    	jpanelExtractInfo.add(jProjectPosEsquinaYPanel);
	    	jpanelExtractInfo.add(jProjectCeldasYPanel);
    	}
		return jpanelExtractInfo;
	}

	/**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {    	
    	//añadimos al clipboard el proyecto seleccionado 
    	ExtractionProject extProject = (ExtractionProject) extractProjects.get(jlistProjects.getSelectedIndex());
    	blackboard.put(SELECTED_EXTRACT_PROJECT, extProject);
    }
    
    /**
     * Tip: Delegate to an InputChangedFirer.
     * @param listener a party to notify when the input changes (usually the
     * WizardDialog, which needs to know when to update the enabled state of
     * the buttons.
     */
    public void add(InputChangedListener listener)
    {
        
    }
    
    public void remove(InputChangedListener listener)
    {
        
    }
    
    public String getTitle()
    {
        return this.getName();
    }
    
    public String getID()
    {
        return localId;
    }
    
    public String getInstructions()
    {
        return I18N.get(MobileAssignCellsPlugin.PluginMobileExtracti18n,MobilePluginI18NResource.MobileAssignCellsPanel01_selecProy);
    }
    
    public boolean isInputValid()
    {        
       if(jlistProjects==null || jlistProjects.getSelectedIndex()<0){
    	   return false;
       }
       return true;
    }
    
    public void setWizardContext(WizardContext wd)
    {
        wizardContext = wd;
    }

    public void setNextID(String nextID)
    {
        this.nextID=nextID;
    }
    public String getNextID()
    {
        return nextID;
    }
       
    
    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
   
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"  

