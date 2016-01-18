package com.geopista.ui.plugin.importer.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.geopista.feature.CodeBookDomain;
import com.geopista.feature.CodedEntryDomain;
import com.geopista.feature.Domain;
import com.geopista.ui.plugin.importer.ImporterPlugIn;
import com.geopista.ui.plugin.importer.beans.ConstantesImporter;
import com.geopista.ui.plugin.importer.beans.NodeToRealRelation;
import com.geopista.ui.plugin.importer.beans.NodoDominio;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class DomainAssociationPanel extends JPanel {

	private JPanel associationPanel = null;
	private JButton removeAssociationJButton = null;
	private JButton addAssociationJButton = null;
	private JPanel realValuesPanel = null;
	private JPanel domainValuesPanel = null;
	private JScrollPane relationsJScroll = null;
	private JList relationsList = null;
	private JScrollPane domainValuesJScrollPane = null;
	private JList domainValuesList = null;
	private JScrollPane realValuesJScrollPane = null;
	private JList realValuesList = null;
	
	private boolean okPressed = false;

	private boolean isDomainSelected = false;
	private boolean isRealValueSelected = false;
	private PlugInContext localContext;
	public static Blackboard bk=new Blackboard();

	private static final String SELECTEDSOURCELAYER = ImporterPlugIn.class.getName()+"_sourceLayer";
	private static final String ATTRIBUTE_ID_ON_TARGET = ImporterPlugIn.class.getName()+"ATTRIBUTE_ID_ON_TARGET";
	private static final String VOID_STRING_TEXT = I18N.get("ImporterPlugIn","ImporterPlugIn.DomainAssociationPanel.cadenavacia");
	private Hashtable<Object, NodoDominio> hsRelations = new Hashtable<Object, NodoDominio>();

	private DefaultListModel relationsListModel = null;
	private DefaultComboBoxModel realValuesListModel = null;
	private NodoDominio selectedNodo=null;
	private Object selectedRealValue=null;
	private NodeToRealRelation relation = null;

	
	public DomainAssociationPanel (PlugInContext context, Domain domain, String locale){			

		super();
		initialize();
		this.localContext = context;
		loadDomain (domain, locale);
		loadRealValues ();

	}	

	private void loadRealValues() {

		String attName = (String) getFromBlackboard(ATTRIBUTE_ID_ON_TARGET);
		if (attName!=null)
		{
			Object[] nodes = getRealValues(attName);			
			
			Comparator valuesComparator = new Comparator(){
	            public int compare(Object o1, Object o2) {
	            	String l1 = (String) o1;
	            	String l2 = (String) o2;
	            	
	                Collator myCollator=Collator.getInstance(new Locale("es_ES"));
	                myCollator.setStrength(Collator.PRIMARY);
	                return myCollator.compare(l1, l2);                
	            }
	        };
	        
	        Arrays.sort(nodes, valuesComparator);
			
			realValuesListModel = new DefaultComboBoxModel(nodes);
			realValuesList.setModel(realValuesListModel);
			realValuesList.setCellRenderer(new ListCellRenderer() 
			{
				JLabel label=new JLabel();

				public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
				{
					String valor = value.toString();
					if (valor.trim().length()==0)
						valor = VOID_STRING_TEXT;
					label.setText(valor);
					label.setOpaque(true);
					label.setBackground(isSelected|| cellHasFocus ? Color.black : Color.white);
					label.setForeground(isSelected|| cellHasFocus ? Color.white : Color.black);

					return label;
				}
			});

			realValuesList.setSelectedIndex(-1);
		}
	}

	private Object[] getRealValues(String attName) {
		HashSet<String> hsRealValues = new HashSet<String>();

		Layer layer = (Layer) getFromBlackboard(SELECTEDSOURCELAYER);
		Collection features = layer.getFeatureCollectionWrapper().getFeatures();		
		Iterator coleccionIter = features.iterator();

		while (coleccionIter.hasNext())
		{
			Feature actualFeature = (Feature) coleccionIter.next();
			
			String campo = actualFeature.getString(attName).toString().trim();
			//String campo = actualFeature.getString(attName).toString();
			
			//La cadena puede estar vacía porque sean espacios en blanco. Para contemplar estos
			//valores por defecto, se introduce un espacio " ", de modo que todas las cadenas vacías
			//" ", "   ", "      ", se traducirán como " " (tener esto en cuenta en MapFunction)
			if (campo.length()==0)
				campo = " ";
			
			hsRealValues.add(campo);
		}

		return (Object[])hsRealValues.toArray();		
	}
/*
	private void loadDomain(int idDomain, final String locale) {
		ArrayList<NodoDominio> lstNodes = getDomainValues(idDomain, locale);

		domainValuesList.setModel(new DefaultComboBoxModel(lstNodes.toArray()));
		domainValuesList.setCellRenderer(new ListCellRenderer() 
		{
			JLabel label=new JLabel();

			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
			{
				NodoDominio elemento=(NodoDominio)value;
				label.setText(elemento.getDescripcion() + " (" + elemento.getPatron() + ")");
				label.setOpaque(true);
				label.setBackground(isSelected|| cellHasFocus ? Color.black : Color.white);
				label.setForeground(isSelected|| cellHasFocus ? Color.white : Color.black);

				return label;
			}
		});
		domainValuesList.setSelectedIndex(-1);
	}
	*/
	
	private void loadDomain(Domain domain, final String locale) {
		ArrayList<NodoDominio> lstNodes = new ArrayList<NodoDominio>();
		Iterator itNodos = domain.getChildren().iterator();
		
		while (itNodos.hasNext())
		{
			NodoDominio nododominio = new NodoDominio();
			
			Object node = itNodos.next();
			if (node instanceof CodedEntryDomain)
			{
				CodedEntryDomain elemento = (CodedEntryDomain)node;
				nododominio.setDescripcion(elemento.getDescription());
				nododominio.setPatron(elemento.getPattern());
			}
			else if (node instanceof CodeBookDomain)
			{
				CodeBookDomain elemento = (CodeBookDomain)node;
				nododominio.setDescripcion(elemento.getDescription());
				nododominio.setPatron(elemento.getPattern());
			}
			
			lstNodes.add(nododominio);			
		}		
		
		
		Comparator domainComparator = new Comparator(){
            public int compare(Object o1, Object o2) {
            	NodoDominio l1 = (NodoDominio) o1;
            	NodoDominio l2 = (NodoDominio) o2;
            	String desc1="";
                String desc2="";
                if (l1!=null && l1.getDescripcion()!=null)
                	desc1 = l1.getDescripcion();
                if (l2!=null && l2.getDescripcion()!=null)
                	desc2 = l2.getDescripcion();                
                
                Collator myCollator=Collator.getInstance(new Locale("es_ES"));
                myCollator.setStrength(Collator.PRIMARY);
                return myCollator.compare(desc1, desc2);                
            }
        };
        
        Collections.sort(lstNodes, domainComparator);
		
		
		domainValuesList.setModel(new DefaultComboBoxModel(lstNodes.toArray()));
		domainValuesList.setCellRenderer(new ListCellRenderer() 
		{
			JLabel label=new JLabel();

			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
			{
				NodoDominio elemento=(NodoDominio)value;
				label.setText(elemento.getDescripcion() + " (" + elemento.getPatron() + ")");
				label.setOpaque(true);
				label.setBackground(isSelected|| cellHasFocus ? Color.black : Color.white);
				label.setForeground(isSelected|| cellHasFocus ? Color.white : Color.black);

				return label;
			}
		});
		domainValuesList.setSelectedIndex(-1);
	}
	

	private void addToAssociationList(NodoDominio nodo, Object valorReal) {

		relationsListModel.addElement(new NodeToRealRelation(nodo, valorReal));
		realValuesListModel.removeElement(valorReal);
		isRealValueSelected = false;
		selectedRealValue = null;
		addAssociationJButton.setEnabled(false);
		relationsList.setCellRenderer(new ListCellRenderer() 
		{
			JLabel label=new JLabel();

			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
			{
				NodeToRealRelation elemento=(NodeToRealRelation)value;
				String elementoVal = (String)elemento.getRealValue();
				if (elementoVal.trim().length()==0)
					elementoVal = VOID_STRING_TEXT;
				label.setText(elementoVal + " -> " + elemento.getNodoDominio().getDescripcion() + " (" + elemento.getNodoDominio().getPatron()+")");
				label.setOpaque(true);
				label.setBackground(isSelected|| cellHasFocus ? Color.black : Color.white);
				label.setForeground(isSelected|| cellHasFocus ? Color.white : Color.black);

				return label;
			}
		});

		relationsList.setSelectedIndex(relationsListModel.getSize()-1);
		removeAssociationJButton.setEnabled(true);
	}	

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {		

		this.setSize(600, 600);
		hsRelations.clear();
		relationsListModel = new DefaultListModel();
		putToBlackboard(ATTRIBUTE_ID_ON_TARGET, null);

		this.setPreferredSize(new Dimension(600,600));
		this.setMinimumSize(this.getPreferredSize());
		this.setMaximumSize(this.getPreferredSize());

		this.setLayout(new GridBagLayout());
		this.add(getDomainValuesPanel(),new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(0, 5, 0, 0),0,0));
		this.add(getRealValuesPanel(),new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(0, 5, 0, 0),0,0));
		this.add(getAssociationPanel(),new GridBagConstraints(0, 1, 2, 1, 1, 1, GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(5, 5, 0, 0),0,0));
	
	}
	

	private JPanel getAssociationPanel() 
	{
		if (associationPanel == null)
		{
			associationPanel = new JPanel();
			associationPanel.setLayout(new GridBagLayout());

			associationPanel.setBorder(BorderFactory.createTitledBorder(I18N.get("ImporterPlugIn","ImporterPlugIn.DomainAssociationPanel.listaasociaciones.titulo")));
			
			associationPanel.add(getRelationsJScrollPane(), new GridBagConstraints(0, 1, 1, 2, 1, 1, GridBagConstraints.NORTHWEST,
					GridBagConstraints.BOTH, new Insets(0, 5, 0, 0),0,0));

			associationPanel.add(getAddAssociationJButton(), new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
					GridBagConstraints.NONE, new Insets(0, 5, 0, 0),0,0));

			associationPanel.add(getRemoveAssociationJButton(), new GridBagConstraints(1, 2, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
					GridBagConstraints.NONE, new Insets(0, 5, 0, 0),0,0));
		}

		return associationPanel;
	}

	private JButton getRemoveAssociationJButton() 
	{
		if (removeAssociationJButton == null)
		{
			removeAssociationJButton = new JButton();
			removeAssociationJButton.setEnabled(false);
			removeAssociationJButton.setText("-");
			removeAssociationJButton.setPreferredSize(new Dimension (40, 30));
			removeAssociationJButton.setMaximumSize(removeAssociationJButton.getPreferredSize());
			removeAssociationJButton.setMinimumSize(removeAssociationJButton.getPreferredSize());
			removeAssociationJButton.addMouseListener(new java.awt.event.MouseAdapter() { 
				public void mouseClicked(java.awt.event.MouseEvent e) {  					
					if (relation !=null)
					{
						hsRelations.remove(relation.getRealValue());
						removeFromAssociationList(relation);
					}
				}
			});
		}

		return removeAssociationJButton;
	}

	private void removeFromAssociationList(NodeToRealRelation relation) {

		relationsListModel.removeElement(relation);

		realValuesListModel.addElement(relation.getRealValue());
		relationsList.setSelectedIndex(relationsListModel.getSize()-1);
		removeAssociationJButton.setEnabled(relationsListModel.getSize()!=0);			

	}

	private JButton getAddAssociationJButton() 
	{
		if (addAssociationJButton == null)
		{
			addAssociationJButton = new JButton();		
			addAssociationJButton.setEnabled(false);
			addAssociationJButton.setText("+");
			addAssociationJButton.setPreferredSize(new Dimension (40, 30));
			addAssociationJButton.setMaximumSize(addAssociationJButton.getPreferredSize());
			addAssociationJButton.setMinimumSize(addAssociationJButton.getPreferredSize());
			addAssociationJButton.addMouseListener(new java.awt.event.MouseAdapter() { 
				public void mouseClicked(java.awt.event.MouseEvent e) {   
					hsRelations.put(selectedRealValue, selectedNodo);
					addToAssociationList(selectedNodo, selectedRealValue);
				}
			});
		}

		return addAssociationJButton;
	}

	private JScrollPane getRelationsJScrollPane() 
	{		
		if (relationsJScroll == null)
		{
			relationsJScroll = new JScrollPane();	
			relationsJScroll.setMinimumSize(new Dimension (300, 180));
			relationsJScroll.setViewportView(getRelationsList());
		}

		return relationsJScroll;
	}

	private JList getRelationsList() {
		if (relationsList == null) {
			relationsList = new JList(relationsListModel);
			relationsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			
			ListSelectionModel rowSM = relationsList.getSelectionModel();
			rowSM.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					ListSelectionModel lsm = (ListSelectionModel)e.getSource();
					if (!lsm.isSelectionEmpty()) {
						relation = (NodeToRealRelation)relationsList.getSelectedValue();	                                   
					}
					else
					{
						relation = null;	                
					}
					
					if (relation!=null)
						removeAssociationJButton.setEnabled(true);
				}
			});   

		}
		return relationsList;
	}

	private JPanel getRealValuesPanel() 
	{
		if (realValuesPanel == null)
		{
			realValuesPanel = new JPanel();		
			realValuesPanel.setLayout(new GridBagLayout());
			realValuesPanel.setBorder(BorderFactory.createTitledBorder(I18N.get("ImporterPlugIn","ImporterPlugIn.DomainAssociationPanel.listareales.titulo")));			
			realValuesPanel.add(getRealValuesJScrollPane(), new GridBagConstraints(0, 1, 1, 2, 1, 1, GridBagConstraints.NORTHWEST,
					GridBagConstraints.BOTH, new Insets(5, 5, 5, 5),0,0));
		}

		return realValuesPanel;
	}

	private JPanel getDomainValuesPanel() 
	{
		if (domainValuesPanel == null)
		{
			domainValuesPanel = new JPanel();		
			domainValuesPanel.setBorder(BorderFactory.createTitledBorder(I18N.get("ImporterPlugIn","ImporterPlugIn.DomainAssociationPanel.listaposibles.titulo")));			
			domainValuesPanel.setLayout(new GridBagLayout());
			domainValuesPanel.add(getDomainValuesJScrollPane(), new GridBagConstraints(0, 1, 1, 2, 1, 1, GridBagConstraints.NORTHWEST,
					GridBagConstraints.BOTH, new Insets(5, 5, 5, 5),0,0));			

		}

		return domainValuesPanel;
	}	
	
	private JScrollPane getDomainValuesJScrollPane() {
		if (domainValuesJScrollPane == null)
		{
			domainValuesJScrollPane = new JScrollPane();
			domainValuesJScrollPane.setMinimumSize(new Dimension (150, 150));
			domainValuesJScrollPane.setViewportView(getDomainValuesList());
		}

		return domainValuesJScrollPane;
	}

	private JList getDomainValuesList() {
		if (domainValuesList == null) {
			domainValuesList = new JList();
			domainValuesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			
			ListSelectionModel rowSM = domainValuesList.getSelectionModel();
			rowSM.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					ListSelectionModel lsm = (ListSelectionModel)e.getSource();
					if (!lsm.isSelectionEmpty()) {
						selectedNodo = (NodoDominio)domainValuesList.getSelectedValue();	                                   
					}
					else
					{
						selectedNodo = null;
					}
					
					if (selectedNodo!=null)
					{
						isDomainSelected = true;
						addAssociationJButton.setEnabled(isDomainSelected && isRealValueSelected);		
					}	
					
				}
			});   
		}
		return domainValuesList;
	}

	private ArrayList<NodoDominio> getDomainValues(int idDomain, String locale)
	{
		ArrayList<NodoDominio> lstDomainValues = new ArrayList<NodoDominio>();
		try
		{
			lstDomainValues = (ArrayList<NodoDominio>) ConstantesImporter.clienteImporter.getDomainValues(idDomain, locale);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return lstDomainValues;
	}

	private JScrollPane getRealValuesJScrollPane() {
		if (realValuesJScrollPane == null)
		{
			realValuesJScrollPane  = new JScrollPane();
			realValuesJScrollPane.setMinimumSize(new Dimension (150, 150));
			realValuesJScrollPane.setViewportView(getRealValuesList());
		}

		return realValuesJScrollPane;
	}

	private JList getRealValuesList() {
		if (realValuesList == null) {
			realValuesList  = new JList();
			realValuesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);			

			ListSelectionModel rowSM = realValuesList.getSelectionModel();
			rowSM.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					ListSelectionModel lsm = (ListSelectionModel)e.getSource();
					if (!lsm.isSelectionEmpty()) {	                	
						selectedRealValue = realValuesList.getSelectedValue();	                                   
					}
					else
					{
						selectedRealValue = null;
					}
					
					if (selectedRealValue!=null)
					{
						isRealValueSelected = true;
						addAssociationJButton.setEnabled(isDomainSelected && isRealValueSelected);	
					}		
				}
			});    

		}
		return realValuesList;
	}

	private Object getFromBlackboard(String key)
	{
		Blackboard bk;
		if (localContext!=null)
			bk=localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerViewPanel().getBlackboard();
		else
			bk=this.bk;

		return bk.get(key);
	}
	
	public Hashtable getHtRelations()
	{
		if (okPressed)
			return hsRelations;
		else
			return null;
	}
	
	public boolean isAllDataRelated() {
		return realValuesListModel.getSize()==0;		
	}

	public void okPressed()
    {
        okPressed = true;        
    }

	public String validateInput()
	{
		return null;
	}
	
	private void putToBlackboard(String key, Object value)
	{
		Blackboard bk;
		if (localContext!=null)
			bk=localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerViewPanel().getBlackboard();
		else
			bk=this.bk;

		bk.put(key,value);
	}
}
