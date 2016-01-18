package com.geopista.ui.plugin.importer.panels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.geopista.feature.Attribute;
import com.geopista.feature.CodeBookDomain;
import com.geopista.feature.Domain;
import com.geopista.feature.TreeDomain;
import com.geopista.ui.components.FeatureExpressionPanel;

import com.geopista.ui.plugin.importer.FeatureTransformer;
import com.geopista.ui.plugin.importer.ImporterPlugIn;
import com.geopista.ui.plugin.importer.beans.ConstantesImporter;
import com.geopista.ui.plugin.importer.dialogs.DomainAssociationDialog;
import com.geopista.util.expression.FeatureExpresionParser;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class MapFieldPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final String SELECTEDSOURCELAYER = ImporterPlugIn.class.getName()+"_sourceLayer";
	private static final String ATTRIBUTE_ID_ON_TARGET = ImporterPlugIn.class.getName()+"ATTRIBUTE_ID_ON_TARGET";
	public static Blackboard bk=new Blackboard();

	private FeatureExpressionPanel featureExpressionPanel = null;
	private JButton copyFieldName = null;
	private Attribute att;
	private FeatureTransformer featureTrans;

	private ActionEventSupport eventAdapter = new  ActionEventSupport();  //  @jve:decl-index=0:
	private PropertyChangeSupport eventPropertyAdapter=new PropertyChangeSupport(this);
	private JCheckBox isKeyCheckBox = null;
	private PlugInContext context = null;
	private DomainAssociationDialog domainBrowserDialog;
	
	private Hashtable htRelations = null;
	private boolean isDomain = false;
	private boolean isMandatory = false;

	/**
	 * This is the default constructor
	 */
	public MapFieldPanel(com.geopista.feature.Attribute att, PlugInContext context) {
		this(att, context, false);
	}

	public MapFieldPanel(com.geopista.feature.Attribute att, PlugInContext context, boolean isMandatory) {
		super();
		setContext(context);
		initialize();		
		//this.featureTrans=featureTrans;
		this.isMandatory = isMandatory;
		setAttribute(att);
	}


	public MapFieldPanel(PlugInContext context) {
		super();
		setContext(context);
		initialize();
	}

	public void setAttribute(com.geopista.feature.Attribute att) {
		this.att=att;
		String txt = att.getName();
		if (isMandatory)
			txt = annadirAsterisco(txt);
		featureExpressionPanel.setLabelText(txt);
	}

	public void setSchema (FeatureSchema sch)
	{
		if (sch != null)
			featureExpressionPanel.getExpParser().setSchema(sch);
	}

	public void setFeatures(Collection features)
	{
		if (features!=null)
			featureExpressionPanel.getExpParser().setFeatures(features);
	}

	public void setFeature(BasicFeature feature)
	{
		if (feature!=null)
			featureExpressionPanel.getExpParser().setFeature(feature);
	}

	public String getText(){
		String value = null;
		if(featureExpressionPanel!= null)
			return featureExpressionPanel.getText();
		return value;
	}
	public void setContext (PlugInContext context)
	{
		this.context = context;
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {		

		this.setSize(200, 30);
		this.setMinimumSize(new Dimension(200,25));
		this.setLayout(new GridBagLayout());
		//this.add(getIsKeyCheckBox(),new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST,
		//		GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0),0,0));
		this.add(getFeatureExpressionPanel(),new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0),0,0));
		this.add(getCopyFieldName(),new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(0, 5, 0, 0),0,0));
	}

	/**
	 * This method initializes featureExpressionPanel	
	 * 	
	 * @return com.geopista.ui.components.FeatureExpressionPanel	
	 */
	private FeatureExpressionPanel getFeatureExpressionPanel() {
		if (featureExpressionPanel == null) {

			featureExpressionPanel = new FeatureExpressionPanel(false);
			featureExpressionPanel.setContext(context);
			featureExpressionPanel.addPropertyChangeListener(new PropertyChangeListener(){

				public void propertyChange(PropertyChangeEvent evt) {
					eventPropertyAdapter.firePropertyChange(evt);
				}});

		}
		return featureExpressionPanel;
	}

	/**
	 * This method initializes copyFieldName	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCopyFieldName() {
		if (copyFieldName == null) {
			copyFieldName = new JButton();
			copyFieldName.setName("setFieldName");
			copyFieldName.setFont(new Font("Arial", 0, 8));
			copyFieldName.setText("<<");
			copyFieldName.setPreferredSize(new Dimension(50, 30));
			copyFieldName.setMaximumSize(copyFieldName.getPreferredSize());
			copyFieldName.setMinimumSize(copyFieldName.getPreferredSize());
			copyFieldName.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					// Muestra la ventana con los valores posibles del
					// dominio si se trata de un atributo con dominio

					if(getFromBlackboard(SELECTEDSOURCELAYER)!=null && 
							getFromBlackboard(ATTRIBUTE_ID_ON_TARGET)!=null &&
							(MapFieldPanel.this.att.getColumn().getDomain() instanceof CodeBookDomain
									|| MapFieldPanel.this.att.getColumn().getDomain() instanceof TreeDomain))
					{
						getDomainBrowserDialog(MapFieldPanel.this.att.getColumn().getDomain());												
					}					

					//Relanza el evento
					e.setSource(MapFieldPanel.this);
					eventAdapter.forwardActionEvent(e);
				}
			});
		}
		return copyFieldName;
	}

	protected DomainAssociationDialog getDomainBrowserDialog(Domain domain) {	

		domainBrowserDialog = new DomainAssociationDialog(context, domain, ConstantesImporter.Locale);
		domainBrowserDialog.setVisible(true); 
		isDomain = true;
		htRelations = domainBrowserDialog.getListRelations();
		return domainBrowserDialog;

	}

	public void setFeatureTransformer(FeatureTransformer featureTransformer) {
		this.featureTrans=featureTransformer;
	}

	public void appendText(String text) {
		getFeatureExpressionPanel().pasteText(text);
	}

	public void addActionListener(ActionListener listener) {
		eventAdapter.addActionListener(listener);
	}

	public void removeActionListener(ActionListener listener) {
		eventAdapter.removeActionListener(listener);
	}
	public boolean getIsKey()
	{
		return getIsKeyCheckBox().isSelected();
	}
	/**
	 * This method initializes isKeyCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getIsKeyCheckBox() {
		if (isKeyCheckBox == null) {
			isKeyCheckBox = new JCheckBox();
			isKeyCheckBox.setSelectedIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/Key.gif")));  // Generated
			isKeyCheckBox.setIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/YellowCircle.gif")));  // Generated
			isKeyCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					eventPropertyAdapter.firePropertyChange("isKey", !getIsKey(), getIsKey());
				}
			});

		}
		return isKeyCheckBox;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		eventPropertyAdapter.addPropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		eventPropertyAdapter.addPropertyChangeListener(propertyName,
				listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		eventPropertyAdapter.removePropertyChangeListener(listener);
	}

	public Object getExpParser(int j) {
		Object value = null;
		FeatureExpresionParser parser = featureExpressionPanel.getExpParser();
		if (parser.getLastFeature()!=null)
			value = parser.getValuesAsObjects().get(j);

		return value;		
	}

	public Vector getParserValues()
	{
		Vector values = null;
		FeatureExpresionParser parser = featureExpressionPanel.getExpParser();
		if (parser.getLastFeature()!=null)
			values = parser.getValuesAsObjects();

		return values;		
	}


	public Hashtable getHtRelations()
	{
		return htRelations;
	}

	public boolean isDomain()
	{
		return isDomain;
	}
	private Object getFromBlackboard(String key)
	{
		Blackboard bk;
		if (context!=null)
			bk=context.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerViewPanel().getBlackboard();
		else
			bk=this.bk;

		return bk.get(key);

	}

	public String getAttributeName (){
		String name = null;
		if (att!=null)
			name= att.getName();

		return name;
	}
	public String getAttributeType(){
		String type = null;
		if (att!=null)
			type = att.getType();
		return type;
	}


	private static String annadirAsterisco(String s){
		String asterisco= "<html><p>" + s + " "+  "<FONT COLOR=\"#FF0000\"><b>*</b></FONT></p></html>";
		return asterisco;
	}

}
