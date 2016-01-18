/**
 * SelectLayerFieldOrigenPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.calculateExpresion.panels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.ui.dialogs.DistanceLinkingPanel;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * Panel para la selección de capas y campos de capas
 * @author juacas
 *
 */
public class SelectLayerFieldOrigenPanel extends JPanel
{
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory
												.getLog(SelectLayerFieldOrigenPanel.class);

	private static final String SELECTEDTARGETLAYERDESTINO = DistanceLinkingPanel.class.getName()+"_targetLayerDestino";
	private static final String SELECTEDTARGETLAYERORIGEN = DistanceLinkingPanel.class.getName()+"_targetLayerOrigen";
	private static final String EXPRESION_FORMULA = DistanceLinkingPanel.class.getName()+"_expresionFormula";;
	private static final String ATTRIBUTE_ID_ON_TARGET_ORIGEN = DistanceLinkingPanel.class.getName()+"ATTRIBUTE_ID_ON_TARGET_ORIGEN"; 
	
	private JComboBox cbTargetLayer;
	private JScrollPane targetFieldScrollPane;
	private JList lstTargetField;
	protected PlugInContext localContext;
	public static Collection debuglayers=new Vector(); // for testing only
	public static Blackboard bk=new Blackboard();// for testing only
	/**
	 * 
	 */
	public SelectLayerFieldOrigenPanel() {
		super();
		initialize();
	}
	public SelectLayerFieldOrigenPanel(PlugInContext context) {
		super();
		setContext(context);
		initialize();
	}
	/**
	 * @param context
	 */
	public void setContext(PlugInContext context)
	{
		localContext=context;	
	}
	/**
	 * 
	 *
	 */
	private void initialize()
	{
	    this.setLayout(new BorderLayout());
	    this.add(getTargetFieldScrollPane(), java.awt.BorderLayout.CENTER);
	    this.add(getCbTargetLayer(), java.awt.BorderLayout.NORTH);
	    GeopistaFunctionUtils.initializeLayerComboBox(null, cbTargetLayer,
	            (Layer) getFromBlackboard(
	                    SELECTEDTARGETLAYERORIGEN), "to-do", 
	                    localContext==null?debuglayers:localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerManager().getLayers());
	    cbTargetLayer.setSelectedIndex(0);
	}
	private JScrollPane getTargetFieldScrollPane() {
		if (targetFieldScrollPane == null) {
			targetFieldScrollPane = new JScrollPane();
			targetFieldScrollPane.setViewportView(getLstTargetField());
			targetFieldScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			targetFieldScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			targetFieldScrollPane.setPreferredSize(new java.awt.Dimension(800,600));
		}
		return targetFieldScrollPane;
	}
	private String selectedFieldName=null;
	public static final int FIELD_ACTIONED = 1;
	protected Layer selectedLayer=null;
	private JList getLstTargetField() {
		if (lstTargetField == null) {
			lstTargetField = new JList();
			
			
			lstTargetField.setVisibleRowCount(3);
			
			lstTargetField.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			lstTargetField.addMouseListener(new java.awt.event.MouseAdapter() { 
				public void mouseClicked(java.awt.event.MouseEvent e) { 
					
					if (e.getClickCount()==2)
						{
						
						 JDialog d=new JDialog();
					      
						 Layer layerOrigen = (Layer) getFromBlackboard(SELECTEDTARGETLAYERORIGEN);
						 Layer layerDestino = (Layer) getFromBlackboard(SELECTEDTARGETLAYERDESTINO);
						  //Creamos el panel Calculate y le pasamos el contexto 
					      SelectedRelationExpresionPanel selectedRelationExpresionPanel = new SelectedRelationExpresionPanel(localContext,layerOrigen ,layerDestino, d);
					      selectedRelationExpresionPanel.setVisible(true);
					      
							
					      d.getContentPane().add(selectedRelationExpresionPanel);
					      d.setSize(400,400);
					      d.setModal(true);
					      d.setTitle(AppContext.getApplicationContext().getI18nString("lblTituloGeopistaFeatureCalculatePlugIn"));
					      d.show();
 
						
						selectedFieldName= selectedLayer.getName() + "." + (String) lstTargetField.getSelectedValue();
						fire(SelectLayerFieldOrigenPanel.this,SelectLayerFieldOrigenPanel.FIELD_ACTIONED,"FieldActioned");
						}
				}
			});
			
			lstTargetField.addListSelectionListener(new ListSelectionListener() { 
				private String oldValue=null;
				public void valueChanged(ListSelectionEvent e)
				{
//					firePropertyChange("FIELDVALUE",oldValue,(String) ((JList)e.getSource()).getSelectedValue());
//					selectedFieldName=oldValue=(String) lstTargetField.getSelectedValue();
				}		
			});
		}
		return lstTargetField;
	}
	private JComboBox getCbTargetLayer() {
		if (cbTargetLayer == null) {
			cbTargetLayer = new JComboBox();
		}
    
		cbTargetLayer.addActionListener(new java.awt.event.ActionListener() 
				{ 
			public void actionPerformed(java.awt.event.ActionEvent e)
			{
				
				Layer targetLayer = (Layer) ((JComboBox) e.getSource())
						.getSelectedItem();
				if (targetLayer==null)
					updateLayerList();
				
				selectedLayer = (Layer) ((JComboBox) e.getSource()).getSelectedItem();
				firePropertyChange("LAYERSELECTED", null, targetLayer);
				updateFieldList(lstTargetField, targetLayer,
						getFromBlackboard(ATTRIBUTE_ID_ON_TARGET_ORIGEN));
			}
		
		});
		return cbTargetLayer;
	}

	/**
	 * 
	 */
	public void updateLayerList()
	{
//		 si no hay ninguna capa seleccionada pero en el workbench
		// si la hay toma esta como la seleccionada.
		try
		{
			Layerable[] candidateTargetLayerables;
			Layer targetLayer=null;
			if (localContext != null)
			{
				candidateTargetLayerables = localContext.getActiveTaskComponent()
						.getLayerNamePanel().getSelectedLayers();
		
				for (int i = 0; i < candidateTargetLayerables.length; i++)
				{
					if (candidateTargetLayerables[i] instanceof Layer)
					{
						targetLayer = (Layer) candidateTargetLayerables[i];
						break;
					}
		
				}
				if (targetLayer==null)
					return;
				GeopistaFunctionUtils.initializeLayerComboBox(null, cbTargetLayer, targetLayer,
						"to-do", localContext.getActiveTaskComponent()
								.getLayerManager().getLayers());
				cbTargetLayer.setSelectedItem(targetLayer);
			}
		} catch (NullPointerException e)
		{
		// Causado por una ventana que no corresponda a un mapa
			if (logger.isDebugEnabled())
				{
				logger.debug("updateLayerList() - No es un mapa");
				}
		}
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
	private void putToBlackboard(String key, Object value)
	{
		Blackboard bk;
		if (localContext!=null)
			bk=localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerViewPanel().getBlackboard();
		else
			bk=this.bk;
		
		bk.put(key,value);
		
	}
	private void updateFieldList(JComponent cbTargetField2, Object item, Object selected)
	{
		if (item==null)return;
		
		FeatureSchema fsch=((Layer)item).getFeatureCollectionWrapper().getFeatureSchema();
		Vector vect=new Vector();
		for (int i =0;i<fsch.getAttributeCount();i++)
			{
		//	if (i!=fsch.getGeometryIndex())
				vect.addElement(fsch.getAttributeName(i));
			}
		if (cbTargetField2 instanceof JComboBox)
		{
		((JComboBox)cbTargetField2).setModel(new DefaultComboBoxModel(vect));
		((JComboBox)cbTargetField2).setSelectedItem(selected);
		}
		else if (cbTargetField2 instanceof JList)
		{
			((JList)cbTargetField2).setListData(vect);
			((JList)cbTargetField2).setSelectedValue(selected,true);
		}
	}
	   private ArrayList actionListeners = new ArrayList();

	    public void addActionListener(ActionListener listener) {
	        actionListeners.add(listener);
	    }

	    public void remove(ActionListener listener) {
	        actionListeners.remove(listener);
	    }

	    public void fire(Object source, int id, String command) {
	        for (Iterator i = actionListeners.iterator(); i.hasNext();) {
	            ActionListener listener = (ActionListener) i.next();
	            listener.actionPerformed(new ActionEvent(source, id, command));
	        }
	    }
	public String getSelectedFieldName()
	{
		return selectedFieldName;
	}
	public Layer getSelectedLayer()
	{
		return selectedLayer;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
