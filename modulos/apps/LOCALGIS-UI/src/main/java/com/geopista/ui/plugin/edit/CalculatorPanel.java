/**
 * CalculatorPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.edit;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.feature.SchemaValidator;
import com.geopista.feature.ValidationError;
import com.geopista.model.GeopistaLayerManager;
import com.geopista.ui.components.FeatureExpressionPanel;
import com.geopista.ui.dialogs.DistanceLinkingPanel;
import com.geopista.ui.dialogs.SelectLayerFieldPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
/**
 * CalculatorPanel
 * Clase que implementa una calculadora de expresiones matemáticas que permite
 * establecer el valor de los diferentes atributos de las features seleccionadas
 * o de todas las features de una capa.
 */
public class CalculatorPanel extends JPanel
{
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory
												.getLog(CalculatorPanel.class);

  ApplicationContext appContext=AppContext.getApplicationContext();
	private static final String SELECTEDTARGETLAYER = DistanceLinkingPanel.class.getName()+"_targetLayer";
	private static final String EXPRESION_FORMULA = DistanceLinkingPanel.class.getName()+"_expresionFormula";;
	private static final String ATTRIBUTE_ID_ON_TARGET = DistanceLinkingPanel.class.getName()+"ATTRIBUTE_ID_ON_TARGET"; 
	public static Collection debuglayers=new Vector(); // for debug only
	
	private JComboBox cbTargetLayer;
	protected JList lstTargetField;
	private PlugInContext localContext;
	private FeatureExpressionPanel featureExpressionPanel;
	private JButton btnSetField;
	private JScrollPane targetFieldScrollPane;
	private JTable reportTable = null;
	private JScrollPane jScrollPane1 = null;
	private DefaultTableModel defaultTableModel = null;   //  @jve:decl-index=0:
	/**
	 * This is the default constructor
	 */
	public CalculatorPanel() {
		super();
		initialize();
    updateTable();
	}
	public CalculatorPanel(PlugInContext context)
	{
		super();
		this.localContext=context;
    initialize();
    updateTable();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {
		java.awt.GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		this.setSize(300,200);
		gridBagConstraints16.gridx = 0;
		gridBagConstraints16.gridy = 4;
		gridBagConstraints16.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.gridy = 2;
		gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints11.gridwidth = 2;
		gridBagConstraints14.gridx = 1;
		gridBagConstraints14.gridy = 0;
		gridBagConstraints14.weightx = 1.0;
		gridBagConstraints14.weighty = 1.0;
		gridBagConstraints14.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints14.gridheight = 2;
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.gridy = 4;
		getFeatureExpressionPanel().setLabelText("");
		this.add(getFeatureExpressionPanel(), gridBagConstraints11);
		this.add(getJScrollPane1(), gridBagConstraints14);
		this.add(getJPanel(), gridBagConstraints16);
		this.add(getJCheckBox(), gridBagConstraints1);
    this.setName(appContext.getI18nString("lblTituloGeopistaFeatureCalculatePlugIn"));
    gridBagConstraints17.gridx = 0;
    gridBagConstraints17.gridy = 1;
    gridBagConstraints17.fill = java.awt.GridBagConstraints.BOTH;
    this.add(getSelectLayerFieldPanel(), gridBagConstraints17);
		
	}
	
	public static Blackboard bk=new Blackboard();// for testing only
	private JButton closeButton = null;
	private JPanel jPanel = null;
	private JCheckBox jCheckBox = null;
	protected boolean showOnlySelected;

	
	private SelectLayerFieldPanel selectLayerFieldPanel = null;
	private Object getFromBlackboard(String key)
	{
		Blackboard bk;
		if (localContext!=null)
		bk=localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerViewPanel().getBlackboard();
		else
		bk=this.bk;
		
	return bk.get(key);
		
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
	/**
	 * This method initializes targetFieldScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	
	/**
	 * This method initializes featureExpressionPanel	
	 * 	
	 * @return com.geopista.ui.FeatureExpressionPanel	
	 */    
	private FeatureExpressionPanel getFeatureExpressionPanel() {
		if (featureExpressionPanel == null) {
			featureExpressionPanel = new FeatureExpressionPanel();
			featureExpressionPanel.setPreferredSize(new java.awt.Dimension(100,56));
			featureExpressionPanel.addPropertyChangeListener(new java.beans.PropertyChangeListener() { 
				public void propertyChange(java.beans.PropertyChangeEvent e) { 
					if ((e.getPropertyName().equals("ExpressionValid"))
							|| 
						e.getPropertyName().equals("ExpressionInvalid"))
					{ 
						updateTable();	 
					} 
				}
			});
			
		}
    
		return featureExpressionPanel;
	}
	/**
	 * 
	 */
	protected void updateTable()
	{
		// obtiene los campos de las Features y sus nuevos valores
		String attName = (String) getFromBlackboard(ATTRIBUTE_ID_ON_TARGET);
		Layer layer = (Layer) getFromBlackboard(SELECTEDTARGETLAYER);

		if (localContext != null && layer == null)
		{
		Layerable[] layerables = localContext.getActiveTaskComponent()
				.getLayerNamePanel().getSelectedLayers();
		// busca las de tipo Layer
		for (int i = 0; i < layerables.length; i++)
		{
		if (layerables[i] instanceof Layer)
		{
		layer = (Layer) layerables[i];
		break;
		}
		}
		if (layer == null) return;
		
		putToBlackboard(SELECTEDTARGETLAYER, layer);

		}
		Collection features = getAffectedFeatures(layer);
		featureExpressionPanel.getExpParser().setFeatures(features);
		Vector values = featureExpressionPanel.getExpParser()
				.getValuesAsObjects();
		Vector names = new Vector();
		Vector rows = new Vector();

		if (attName == null)
		{
		names.add(appContext.getI18nString("lblNuevoValor"));
		}
		else
		{
		names.add(attName + " " + appContext.getI18nString("lblNuevoValor"));
		}

		Iterator feat = features.iterator();
		Iterator val = values.iterator();
		// construye los nombres
		FeatureSchema sch = layer.getFeatureCollectionWrapper()
				.getFeatureSchema();
		for (int i = 0; i < sch.getAttributeCount(); i++)
		{
		if (i != sch.getGeometryIndex()) names.add(sch.getAttributeName(i));
		}
		// Construye las filas de la tabla
		while (feat.hasNext())
		{
		Vector row = new Vector();
		Feature feature = (Feature) feat.next();
		if (featureExpressionPanel.getExpParser().getErrorInfo() == null) row
				.add(val.next());
		else row.add(appContext.getI18nString("lblValorPorDefecto"));
		for (int i = 0; i < sch.getAttributeCount(); i++)
		{
		if (i != sch.getGeometryIndex()) row.add(feature.getAttribute(i));
		}
		rows.add(row);
		}

		reportTable.setModel(new DefaultTableModel(rows, names));

	}
	/**
	 * @return
	 */
	private Collection getAffectedFeatures(Layer layer)
	{
		if (showOnlySelected && localContext!=null)
		{
			return localContext.getActiveTaskComponent().getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems(layer);
		}
		else
			if (layer==null)
				return  new Vector();
			else
			return layer.getFeatureCollectionWrapper().getFeatures();
	}
	
	/**
	 * This method initializes jButton3	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBtnSetField() {
		if (btnSetField == null) {
			btnSetField = new JButton();
			btnSetField.setIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/SmallDown.gif")));
			btnSetField.setEnabled(false);
			btnSetField.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			btnSetField.setToolTipText("TransferLinkingData");
			btnSetField.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					setFields();
					updateTable();
				}
			});
		}
		return btnSetField;
	}
	/**
	 * 
	 */
	protected void setFields()
	{

		// obtiene los campos de las Features y sus nuevos valores
	final String attName=(String) getFromBlackboard(ATTRIBUTE_ID_ON_TARGET);
	final Layer layer = (Layer) getFromBlackboard(SELECTEDTARGETLAYER);
	final Collection features=getAffectedFeatures(layer);
	
  if (featureExpressionPanel.getExpParser().getErrorInfo()!=null)
  {
    JOptionPane.showMessageDialog(null,appContext.getI18nString("ExpresionNoValida"));
    return;
  }
	featureExpressionPanel.getExpParser().setFeatures(features);
	final Vector values=featureExpressionPanel.getExpParser().getValuesAsObjects();
	final Vector oldvalues=new Vector();
	Iterator feat = features.iterator();
final SchemaValidator validator=new SchemaValidator(null);

	// Backup de los valores antiguos
	while (feat.hasNext())
		{
		Feature feature=((Feature)feat.next());
		oldvalues.add(feature.getAttribute(attName));
		}

	UndoableCommand cmd= new UndoableCommand(attName)
		{
		
		public void execute()
			{

				Iterator feat = features.iterator();
				Iterator val = values.iterator();
				
				while (feat.hasNext())
					{
					Feature feature=((Feature)feat.next());
					Feature clone=(Feature) feature.clone();
					/**
					 * Intenta evitar la validación automática en cadena
					 * que no se puede interrumpir.
					 */
					Object newVal=val.next();
					clone.setAttribute(attName,newVal);
					boolean pass = validator.validateFeature(clone);
					if (!pass)
						{
						ValidationError err=(ValidationError) validator.getErrorListIterator().next();
						
						String errMsg=I18N.getMessage("CalculatorPanel.ErrorMsg",
								new Object[]{err.attName,err.message,newVal});
						int eleccion= JOptionPane.showConfirmDialog(CalculatorPanel.this,
								errMsg,
								"Error en el esquema",
								JOptionPane.OK_CANCEL_OPTION);
						if (eleccion==JOptionPane.CANCEL_OPTION)
							return;
;						}
					feature.setAttribute(attName, newVal);
					}
				
			}

			public void unexecute()
			{

				Iterator feat = features.iterator();
				Iterator oldval= oldvalues.iterator();
				while (feat.hasNext())
					{
					Feature feature=((Feature)feat.next());
					feature.setAttribute(attName, oldval.next());
					}
				
			}
			
		};
		
		cmd.execute();
	}
	
	
	/**
	 * This method initializes reportTable	
	 * 	
	 * @return javax.swing.JTable	
	 */    
	private JTable getReportTable() {
		if (reportTable == null) {
			reportTable = new JTable();
			reportTable.setRowSelectionAllowed(false);
			reportTable.setAutoResizeMode(0);
			
		}
		return reportTable;
	}
	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getReportTable());
      jScrollPane1.setHorizontalScrollBarPolicy(jScrollPane1.HORIZONTAL_SCROLLBAR_ALWAYS);
			jScrollPane1.setPreferredSize(new java.awt.Dimension(800,600));
		}
		return jScrollPane1;
	}
	/**
	 * This method initializes defaultTableModel	
	 * 	
	 * @return javax.swing.table.DefaultTableModel	
	 */    
	private DefaultTableModel getDefaultTableModel() {
		if (defaultTableModel == null) {
			defaultTableModel = new DefaultTableModel();
			defaultTableModel.setColumnCount(1);
		}
		return defaultTableModel;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getCloseButton() {
		if (closeButton == null) {
			closeButton = new JButton();
			closeButton.setActionCommand("Close");
			closeButton.setSelected(true);
			closeButton.setIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/Delete.gif")));
			closeButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					if (getParent()!=null)
						getParent().setVisible(false);
           
					}
			});
      
      
      

      
		}
		return closeButton;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.add(getBtnSetField(), null);
			jPanel.add(getCloseButton(), null);
      
		}
		return jPanel;
	}
	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */    
	private JCheckBox getJCheckBox() {
		if (jCheckBox == null) {
			jCheckBox = new JCheckBox();
			jCheckBox.setText(appContext.getI18nString("lblSelected"));
			jCheckBox.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					showOnlySelected=((JCheckBox)e.getSource()).isSelected();
          updateTable();
				}
			});
			
		}
		return jCheckBox;
	}
   	
	/**
	 * This method initializes selectLayerFieldPanel	
	 * 	
	 * @return 
	 */    
	private JPanel getSelectLayerFieldPanel() {
		if (selectLayerFieldPanel == null) {
			selectLayerFieldPanel = new SelectLayerFieldPanel(localContext);
//			selectLayerFieldPanel.setContext(localContext);
			selectLayerFieldPanel.addPropertyChangeListener(
					new PropertyChangeListener()
					{

					public void propertyChange(PropertyChangeEvent e)
							{ 
								if (logger.isDebugEnabled())
									{
									logger
											.debug("propertyChange(PropertyChangeEvent)"
													+ e.getPropertyName());
									}
								if (e.getPropertyName().equals("LAYERSELECTED"))
								{
									Layer targetLayer = (Layer) e.getNewValue();
									putToBlackboard(SELECTEDTARGETLAYER,targetLayer);
									if (targetLayer!=null)
										featureExpressionPanel.setFeatures(targetLayer.getFeatureCollectionWrapper().getFeatures());
									
								}
								else
								if (e.getPropertyName().equals("FIELDVALUE"))
								{
									String attName=(String) e.getNewValue();
									putToBlackboard(ATTRIBUTE_ID_ON_TARGET, attName);
									btnSetField.setEnabled(attName!=null);
									updateTable();
									getFeatureExpressionPanel().setLabelText(attName+"=");
								}
										
						}	
						
					});
			selectLayerFieldPanel.addActionListener( new ActionListener() { 
								public void actionPerformed(ActionEvent e)
				{
					SelectLayerFieldPanel pan=(SelectLayerFieldPanel) e.getSource();
					featureExpressionPanel.pasteText(pan.getSelectedFieldName());
					featureExpressionPanel.requestFocus();
					
				}
			});
		
		}
		return selectLayerFieldPanel;
	}
	public static void main(String[] args)
	{
		JDialog d=new JDialog();
		
		FeatureSchema sch=new FeatureSchema();
		sch.addAttribute("GEOMETRY",AttributeType.GEOMETRY);
		sch.addAttribute("entero",AttributeType.INTEGER);
		sch.addAttribute("Cadena",AttributeType.STRING);
		
		Feature feat1= new BasicFeature(sch);
		feat1.setAttribute("GEOMETRY", null);
		feat1.setAttribute("entero",new Integer(23));
		feat1.setAttribute("Cadena","valorcadena");
		
		Feature feat2= new BasicFeature(sch);
		feat2.setAttribute("GEOMETRY", null);
		feat2.setAttribute("entero",new Integer(23));
		feat2.setAttribute("Cadena","valorcadena");
		
		Feature feat3= new BasicFeature(sch);
		feat3.setAttribute("GEOMETRY", null);
		feat3.setAttribute("entero",new Integer(23));
		feat3.setAttribute("Cadena","valorcadena");
		Vector features= new Vector();
		features.add(feat1);
		features.add(feat2);
		features.add(feat3);
		
		Layer layer=new Layer("prueba",Color.RED,new FeatureDataset(features, sch),new GeopistaLayerManager());
		CalculatorPanel.debuglayers.add(layer);
		CalculatorPanel.bk.put(SELECTEDTARGETLAYER,layer);
		SelectLayerFieldPanel.debuglayers.add(layer);
		SelectLayerFieldPanel.bk.put(SELECTEDTARGETLAYER,layer);
		CalculatorPanel cpanel=new CalculatorPanel();
		
		
		d.getContentPane().add(cpanel);
		d.setSize(400,400);
		d.show();
	}
 }
