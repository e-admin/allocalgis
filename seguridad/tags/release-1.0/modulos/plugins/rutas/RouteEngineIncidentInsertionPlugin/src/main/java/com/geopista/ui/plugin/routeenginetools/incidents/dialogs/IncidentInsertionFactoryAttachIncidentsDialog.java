package com.geopista.ui.plugin.routeenginetools.incidents.dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import org.uva.geotools.graph.structure.Edge;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.Network;
import org.uva.routeserver.managers.SpatialAllInMemoryExternalSourceMemoryManager;
import org.uva.routeserver.street.Incident;

import com.geopista.app.AppContext;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.ui.components.DateField;
import com.geopista.ui.plugin.routeenginetools.incidents.dialogs.tables.IncidentDateCellRenderer;
import com.geopista.ui.plugin.routeenginetools.incidents.dialogs.tables.IncidentDescriptionCellRenderer;
import com.geopista.ui.plugin.routeenginetools.incidents.dialogs.tables.IncidentTableModel;
import com.geopista.ui.plugin.routeenginetools.incidents.dialogs.tables.IncidentTypeCellRenderer;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.localgis.route.graph.io.LocalGISRouteReaderWriter;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISIncident;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

public class IncidentInsertionFactoryAttachIncidentsDialog extends JPanel implements WizardPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<LocalGISIncident> incidentList;
	// objetos generales
	private WizardContext wizardContext;
	
	private Network network;
	private ILocalGISEdge edgeToModify;
	// objetos visuales
	
	private JTable jTableSelectedIncidents;
	private DateField jDateStart;
	private DateField jDateEnd;
	private JButton jButtonAttach;
	private JButton jButtonDetach;
	private JButton jButtonEdit;
	private JComboBox jComboBoxIncidentType;
	private JTextField jTextFieldDescription;
	private JPanel jPanelInfo;
	private JPanel jPanelData;
	private JPanel jPanelTable;
	private JPanel jPanelIncidentData;
	private JLabel jLabelDescription;
	private JLabel jLabelDateStart;
	private JLabel jLabelDateEnd;
	private JLabel jLabelIncidentType;
	
    
    public IncidentInsertionFactoryAttachIncidentsDialog(ArrayList<LocalGISIncident> incidents,Network network,ILocalGISEdge edgeToModify) {
    	incidentList = incidents;
    	this.network = network;
    	this.edgeToModify= edgeToModify;
    	initialize();
    }

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
    	
    	this.setLayout(new GridBagLayout());
    	this.setSize(new Dimension(600, 550));
    	this.setPreferredSize(new Dimension(600, 550));
    	       
    	
    	this.add(getJPanelInfo(), 
				new GridBagConstraints(0,0,1,1, 1, 0.05,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));

		this.add(getJPanelDatos(), 
				new GridBagConstraints(0,1,1,1, 1, 0.95,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));
    		
    }
    private Component getJPanelDatos() {
    	
    	// Construcción de objetos iniciales
    	jTableSelectedIncidents = new JTable();
    	buildIncidentsTable();
    	
    	
    	
    	
    	if (jPanelData == null){
    		
    		jLabelDescription = new JLabel(I18N.get("networkIncidents","routeengine.incidents.factorydialog.descriptionlabel"));
    		jLabelDateStart = new JLabel(I18N.get("networkIncidents","routeengine.incidents.factorydialog.startdatelabel"));
    		jLabelDateEnd = new JLabel(I18N.get("networkIncidents","routeengine.incidents.factorydialog.enddatelabel"));
    		jLabelIncidentType = new JLabel(I18N.get("networkIncidents","routeengine.incidents.factorydialog.incidenttypelabel"));
    		
    		jPanelData   = new JPanel();
    		jPanelTable   = new JPanel();
    		jPanelIncidentData = new JPanel();
    		//Boton de agregar
    		jButtonAttach = new JButton();
    		jButtonAttach.setText(I18N.get("networkIncidents","routeengine.incidents.factorydialog.addbuttonlabel"));
    		jButtonAttach.setEnabled(true);
    		jButtonAttach.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    attachIncident();
                }
            });
    		//Boton de quitar
    		jButtonDetach = new JButton();
            jButtonDetach.setText(I18N.get("networkIncidents","routeengine.incidents.factorydialog.deletebuttonlabel"));
            jButtonDetach.setEnabled(true);
            jButtonDetach.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                	detachIncident();
                }
            });
            //Tipo de incidente
            jComboBoxIncidentType = new JComboBox();
            //Descripcion de incidente
        	jTextFieldDescription = new JTextField();
        	//jTextFieldDescription.set
            //Fecha de inicio
        	jDateStart = new DateField((java.util.Date) null, 0);
            //Fecha de fin
        	jDateEnd = new DateField((java.util.Date) null, 0);
            
    		jPanelData.setLayout(new GridBagLayout());
    		
    		jPanelData.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("networkIncidents","routeengine.incidents.borderPanelData"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));
    		
    		
    		jPanelTable.setLayout(new GridBagLayout());
    		jPanelIncidentData.setLayout(new GridBagLayout());
    		
    		jPanelTable.add(buildIncidentsTable(), 
    				new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.CENTER,
    						GridBagConstraints.BOTH, new Insets(0,0,0,0),180,0));
            
    		jPanelTable.add(jPanelIncidentData, 
    				new GridBagConstraints(1,0,1,1, 1, 1,GridBagConstraints.CENTER,
    						GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
//            
//    		
//    		
//            
//    		jPanelIncidentData.add(jComboBoxIncidentType, 
//    				new GridBagConstraints(1,0,1,1, 0.1, 0.1,GridBagConstraints.CENTER,
//    						GridBagConstraints.CENTER, new Insets(0,0,0,0),0,0));
//    		jPanelIncidentData.add(jLabelIncidentType, 
//    				new GridBagConstraints(0,0,1,1, 0.1, 0.1,GridBagConstraints.CENTER,
//    						GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));
//           //HORIZONTAL
//    		jPanelIncidentData.add(jTextFieldDescription, 
//    				new GridBagConstraints(1,1,1,1, 0.1, 0.1,GridBagConstraints.CENTER,
//    						GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),0,0));
//    		jPanelIncidentData.add(jLabelDescription, 
//    				new GridBagConstraints(0,1,1,1, 0.1, 0.1,GridBagConstraints.CENTER,
//    						GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));
//    		
//    		jPanelIncidentData.add(jDateStart, 
//    				new GridBagConstraints(1,2,1,1, 0.1, 0.1,GridBagConstraints.CENTER,
//    						GridBagConstraints.CENTER, new Insets(0,0,0,0),0,0));
//    		
//    		jPanelIncidentData.add(jLabelDateStart,
//    				new GridBagConstraints(0,2,1,1, 0.1, 0.1,GridBagConstraints.CENTER,
//    						GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));
//    		
//    		jPanelIncidentData.add(jDateEnd, 
//    				new GridBagConstraints(1,3,1,1, 0.1, 0.1,GridBagConstraints.CENTER,
//    						GridBagConstraints.CENTER, new Insets(0,0,0,0),0,0));
//    		jPanelIncidentData.add(jLabelDateEnd, 
//    				new GridBagConstraints(0,3,1,1, 0.1, 0.1,GridBagConstraints.CENTER,
//    						GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));
    		jPanelIncidentData.add(jButtonAttach, 
    				new GridBagConstraints(0,0,1,1, 0.1, 0.1,GridBagConstraints.CENTER,
    						GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));
    		jPanelIncidentData.add(jButtonDetach, 
    				new GridBagConstraints(0,1,1,1, 0.1, 0.1,GridBagConstraints.CENTER,
    						GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));
    		jPanelIncidentData.add(this.getJButtonEdit(), 
    				new GridBagConstraints(0,2,1,1, 0.1, 0.1,GridBagConstraints.CENTER,
    						GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));
    		
    		jPanelIncidentData.add(new JLabel(), 
    				new GridBagConstraints(0,3,1,1, 0.1, 0.1,GridBagConstraints.CENTER,
    						GridBagConstraints.NONE, new Insets(0,0,0,0),0,150));
    		

            jPanelData.add(jPanelTable,
    				new GridBagConstraints(0,0,1,1, 0.8, 0.8,GridBagConstraints.CENTER,
    						GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
    		
    	}
    	return jPanelData;
	}
    private JButton getJButtonEdit() {
		// TODO Auto-generated method stub
    	if (jButtonEdit == null){
    		jButtonEdit = new JButton (I18N.get("networkIncidents","routeengine.incidents.factorydialog.editbuttonlabel"));
    		jButtonEdit.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                	editIncident();
                }
            });
    	}
		return jButtonEdit;
	}
    
	

	private void attachIncident() {
//    	LocalGISIncident incident = new LocalGISIncident();
//    	if(jTextFieldDescription.getText() == "" || jDateStart.getDate() == null || jDateEnd.getDate() == null)
//    		return;
//    	if(jComboBoxIncidentType.getSelectedItem().equals("Cerrado a Vehiculos"))
//    		incident.setIncidentType(LocalGISIncident.PATH_CLOSED_TO_VEHICLES);
//    	else
//    		incident.setIncidentType(LocalGISIncident.PATH_DISABLED);
//    	incident.setDescription(jTextFieldDescription.getText());
//    	incident.setDateStart(jDateStart.getDate());
//    	incident.setDateEnd(jDateEnd.getDate());
//    	incidentList.add(incident);
//	    updateModelIncidents(jTableSelectedIncidents);
//	    jComboBoxIncidentType.setSelectedIndex(0);
//	    jTextFieldDescription.setText("");
//	    jDateStart.cleanup();
//	    jDateEnd.cleanup();
	    //this.wizardContext.inputChanged();
		BaiscIncidentDialog dialog = new BaiscIncidentDialog(AppContext.getApplicationContext().getMainFrame(),"Nuevo Incidente", null);
		if (dialog.wasOKPressed()){
			incidentList.add(dialog.getIncident());
			updateModelIncidents(jTableSelectedIncidents);
		}
		
	}
	
	private void editIncident() {
		// TODO Auto-generated method stub
		if(jTableSelectedIncidents.getSelectedRow() < 0) return;

	    int[] idSelectedRows = jTableSelectedIncidents.getSelectedRows();
	    
	    for (int i = 0; i< idSelectedRows.length; i++){
	    	LocalGISIncident incident = (LocalGISIncident)jTableSelectedIncidents.getValueAt(idSelectedRows[i], IncidentTableModel.ROW_INCIDENT_TYPE);
	    	BaiscIncidentDialog dialog = new BaiscIncidentDialog(AppContext.getApplicationContext().getMainFrame(),"Nuevo Incidente", incident);
	    	if (dialog.wasOKPressed()){
				updateModelIncidents(jTableSelectedIncidents);
			    this.wizardContext.inputChanged();
			}
	    }
	    
	}
    
    private void detachIncident() {
    	if(jTableSelectedIncidents.getSelectedRow() < 0) return;

	    int[] idSelectedRows = jTableSelectedIncidents.getSelectedRows();
	    
	    for (int i = 0; i< idSelectedRows.length; i++){
	    	LocalGISIncident incident = (LocalGISIncident)jTableSelectedIncidents.getValueAt(idSelectedRows[i], IncidentTableModel.ROW_INCIDENT_TYPE);
	    	incidentList.remove(incident);
	    }
	    updateModelIncidents(jTableSelectedIncidents);
	    this.wizardContext.inputChanged();
	}
	private Component getJPanelInfo() {
		if (jPanelInfo == null){

			jPanelInfo    = new JPanel();
			jPanelInfo.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("networkIncidents","routeengine.incidents.informationpaneltitle"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));

			JTextArea jTextAreaInfo = new JTextArea(I18N.get("networkIncidents","routeengine.incidents.informationdescriptionlabel"));
			jTextAreaInfo.setEnabled(false);
			jTextAreaInfo.setFont(new JLabel().getFont());
			jTextAreaInfo.setOpaque(false);
			jTextAreaInfo.setDisabledTextColor(Color.black);
			jTextAreaInfo.setEditable(false);
			jTextAreaInfo.setWrapStyleWord(true);
			jPanelInfo.add(jTextAreaInfo);
		}
		return jPanelInfo;
	}

	private JScrollPane buildIncidentsTable() {
		jTableSelectedIncidents.setRowSelectionAllowed(true);
		jTableSelectedIncidents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);        
        JScrollPane jScrollPane = new JScrollPane(jTableSelectedIncidents);
        String text = I18N.get("networkIncidents","routeengine.incidents.incidentlistlabel");
        jScrollPane.setBorder(new TitledBorder(text));
        updateModelIncidents(jTableSelectedIncidents);
		return jScrollPane;
		
	}

	private void updateModelIncidents(JTable table) {
		
		IncidentTableModel tableModel = new IncidentTableModel();
		tableModel.setModelData(incidentList);
        TableSorted sorter = new TableSorted(tableModel);
        sorter.setTableHeader(table.getTableHeader());
        table.setModel(sorter);
        //table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //table.getColumnModel().setColumnSelectionAllowed(true);
        table.getColumnModel().getColumn(IncidentTableModel.ROW_INCIDENT_TYPE).setCellRenderer(new IncidentTypeCellRenderer());
        table.getColumnModel().getColumn(IncidentTableModel.ROW_INCIDENT_TYPE).setPreferredWidth(70);
        table.getColumnModel().getColumn(IncidentTableModel.ROW_INCIDENT_DESCRIPTION).setCellRenderer(new IncidentDescriptionCellRenderer());
        table.getColumnModel().getColumn(IncidentTableModel.ROW_INCIDENT_DESCRIPTION).setPreferredWidth(60);
        table.getColumnModel().getColumn(IncidentTableModel.ROW_DATE_START).setCellRenderer(new IncidentDateCellRenderer());
        table.getColumnModel().getColumn(IncidentTableModel.ROW_DATE_START).setPreferredWidth(30);
        table.getColumnModel().getColumn(IncidentTableModel.ROW_DATE_END).setCellRenderer(new IncidentDateCellRenderer());
        table.getColumnModel().getColumn(IncidentTableModel.ROW_DATE_END).setPreferredWidth(30);
        
        table.setEnabled(true);
		
	}

	/**
     * Called when the user presses Next on this panel's previous panel
     */
    public void enteredFromLeft(Map dataMap)
    { 
        
    }
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {   
    	
    	/*
		
		LocalGISIncident incident = new LocalGISIncident();
		incident.setIncidentType(LocalGISIncident.PATH_CLOSED_TO_VEHICLES);
		incident.setDescription("Desc");
		incident.setDateStart(new Date(System.currentTimeMillis()));
		incident.setDateEnd(new Date(System.currentTimeMillis()));*/
    	Set<Incident> incidents = edgeToModify.getIncidents();
    	incidents.clear();
    	Iterator it = incidentList.iterator();
    	while(it.hasNext()){
    		if(edgeToModify instanceof ILocalGISEdge)
    			((ILocalGISEdge)edgeToModify).putIncident((Incident)it.next());
    	}
		
    	
		
		//Si es basica, ya está agregada. Modificar en la tabla. Ya no existe.
		//edgeFeature.setAttribute("incidents", edgeToModify.getIncidents());
		if(network.getGraph() instanceof DynamicGraph && ((DynamicGraph)network.getGraph()).getMemoryManager() instanceof SpatialAllInMemoryExternalSourceMemoryManager){
			LocalGISRouteReaderWriter rw = (LocalGISRouteReaderWriter)((SpatialAllInMemoryExternalSourceMemoryManager)((DynamicGraph)network.getGraph()).getMemoryManager()).getStore();
			RouteConnectionFactory rcf = new GeopistaRouteConnectionFactoryImpl();
			Connection conn = null;
			try{
				conn = rcf.getConnection();
				rw.writeIncidents((Edge)edgeToModify, conn);
			}finally{
				ConnectionUtilities.closeConnection(conn);
			}
		}
		//sino
		/*if(network.getGraph() instanceof DynamicGraph){
			// Es un tipo dinamico. Se actualiza automaticamente por medio 
			DynamicGraph graph = (DynamicGraph)network.getGraph();
			BasicGraph bGraph = new BasicGraph();
			ArrayList modifiedEdges = new ArrayList();
			modifiedEdges.add(edgeToModify);
			ArrayList modifiedNodes = new ArrayList();
			if(edgeToModify instanceof LocalGISDynamicEdge ){
				modifiedNodes.add(((LocalGISDynamicEdge)edgeToModify).getNodeA());
				modifiedNodes.add(((LocalGISDynamicEdge)edgeToModify).getNodeB());
			}else{
				modifiedNodes.add(((LocalGISStreetDynamicEdge)edgeToModify).getNodeA());
				modifiedNodes.add(((LocalGISStreetDynamicEdge)edgeToModify).getNodeB());
			}
			bGraph.setEdges(modifiedEdges);
			bGraph.setNodes(modifiedNodes);
			if(graph.getMemoryManager() instanceof BasicExternalSourceMemoryManager 
//					|| graph.getMemoryManager() instanceof SpatialAllInMemoryExternalSourceMemoryManager
				){
				//if(((SpatialAllInMemoryExternalSourceMemoryManager)graph.getMemoryManager()).getStore() instanceof LocalGISRouteReaderWriter){
					((BasicExternalSourceMemoryManager)graph.getMemoryManager()).appendGraph(bGraph);
				//}
			}
		}*/
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
      return "Incidencias";
    }
    public String getInstructions()
    {
    	return "Instrucciones";
    }
    /**
     * Este metodo valida y activa o no el boton de next
     */
    public boolean isInputValid()
    {
    	return true;
    }
    public void setWizardContext(WizardContext wd)
    {
        wizardContext =wd;
    }
    public String getID()
    {
      return "insertIncidents";
    }
    public void setNextID(String nextID)
    {
       
    }
    public String getNextID()
    {
      return null;
    }
    public void exiting()
    {
        System.out.println("Saliendo");
    }
}
