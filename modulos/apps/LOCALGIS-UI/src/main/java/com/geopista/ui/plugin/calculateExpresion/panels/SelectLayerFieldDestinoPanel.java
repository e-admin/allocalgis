/**
 * SelectLayerFieldDestinoPanel.java
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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.ui.dialogs.DistanceLinkingPanel;
import com.geopista.ui.plugin.calculateExpresion.ws.client.ClienteWSCalculateExpression;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * Panel para la selección de capas y campos de capas
 * @author juacas
 *
 */
public class SelectLayerFieldDestinoPanel extends JPanel
{
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory.getLog(SelectLayerFieldDestinoPanel.class);
	private static final String DESTINO = DistanceLinkingPanel.class.getName()+"DESTINO";
	
	private JComboBox cbTargetLayer;
	private JScrollPane targetFieldScrollPane;
	private JList lstTargetField;
	protected PlugInContext localContext;

	ClienteWSCalculateExpression cwsce = null;
	
	Blackboard bk = new Blackboard();
	
	private String destinoOld = "";
	/**
	 * 
	 */
	public SelectLayerFieldDestinoPanel() {
		
		super();
		cwsce = new ClienteWSCalculateExpression();
		initialize();
	}
	public SelectLayerFieldDestinoPanel(PlugInContext context) {
		super();
		cwsce = new ClienteWSCalculateExpression();
		setContext(context);
		initialize();
	}
	/**
	 * @param context
	 */
	public void setContext(PlugInContext context)
	{
		localContext=context;	
		setBlackboard(this.localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerViewPanel().getBlackboard());
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
	    
	    ArrayList lstTablesNames = cwsce.obtenerTablesNames();	    
	    initializeComboBox(null, cbTargetLayer,lstTablesNames);
	  
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
	private String oldValue=null;

	ArrayList lstOldValue = new ArrayList();
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
						
						selectedFieldName=(String)cbTargetLayer.getSelectedItem() +"."+ 
										(String) lstTargetField.getSelectedValue()	;
						firePropertyChange("FIELDEXPRESIONVALUE","",selectedFieldName);
						lstOldValue.remove(lstOldValue.size()-1);

						if(lstOldValue.isEmpty() || lstOldValue.size()==0){
							lstOldValue.add(" ");
							firePropertyChange("FIELDVALUE","",lstOldValue.get(lstOldValue.size()-1));
						}
						else{
							firePropertyChange("FIELDVALUE","",(String)lstOldValue.get(lstOldValue.size()-1));
							String campo = (String)lstOldValue.get(lstOldValue.size()-1);
							lstOldValue = new ArrayList();
							lstOldValue.add(campo);
						}
						
					}
					else{
						String campo = (String)cbTargetLayer.getSelectedItem() +"."+(String) lstTargetField.getSelectedValue();
						lstOldValue.add(campo);
						firePropertyChange("FIELDVALUE","",campo);

					}
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
				
				ArrayList lstColumnsNames = cwsce.obtenerColumnsNames((String)cbTargetLayer.getSelectedItem());
			
				Vector vect=new Vector();
				for (int i =0;i<lstColumnsNames.size();i++)
				{
					vect.addElement((String)lstColumnsNames.get(i));
				}
				
				lstTargetField.setListData(vect);
			
				
				
				
				
				
				
				
				
				
//				Layer targetLayer = (Layer) ((JComboBox) e.getSource())
//						.getSelectedItem();
//				if (targetLayer==null)
//					updateLayerList();
//				
//				selectedLayer = (Layer) ((JComboBox) e.getSource()).getSelectedItem();
//				firePropertyChange("LAYERSELECTED", null, targetLayer);
//				updateFieldList(lstTargetField, targetLayer,
//						getFromBlackboard(ATTRIBUTE_ID_ON_TARGET_DESTINO));
			}
		
		});
		return cbTargetLayer;
	}

	/**
	 * 
	 */
//	public void updateLayerList()
//	{
////		 si no hay ninguna capa seleccionada pero en el workbench
//		// si la hay toma esta como la seleccionada.
//		try
//		{
//			Layerable[] candidateTargetLayerables;
//			Layer targetLayer=null;
//			if (localContext != null)
//			{
//				candidateTargetLayerables = localContext.getActiveTaskComponent()
//						.getLayerNamePanel().getSelectedLayers();
//		
//				for (int i = 0; i < candidateTargetLayerables.length; i++)
//				{
//					if (candidateTargetLayerables[i] instanceof Layer)
//					{
//						targetLayer = (Layer) candidateTargetLayerables[i];
//						break;
//					}
//
//				}
//				if (targetLayer==null)
//					return;
//				GeopistaUtil.initializeLayerComboBox(null, cbTargetLayer, targetLayer,
//				"to-do", localContext.getActiveTaskComponent()
//						.getLayerManager().getLayers());
//				cbTargetLayer.setSelectedItem(targetLayer);
//			}
//		} catch (NullPointerException e)
//		{
//		// Causado por una ventana que no corresponda a un mapa
//			if (logger.isDebugEnabled())
//				{
//				logger.debug("updateLayerList() - No es un mapa");
//				}
//		}
//	}
	
//	private Object getFromBlackboard(String key)
//	{
//		Blackboard bk;
//		if (localContext!=null)
//		bk=localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerViewPanel().getBlackboard();
//		else
//		bk=this.bk;
//		
//	return bk.get(key);
//		
//	}
//	private void putToBlackboard(String key, Object value)
//	{
//		Blackboard bk;
//		if (localContext!=null)
//			bk=localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerViewPanel().getBlackboard();
//		else
//			bk=this.bk;
//		
//		bk.put(key,value);
//		
//	}
//	private void updateFieldList(JComponent cbTargetField2, Object item, Object selected)
//	{
//		if (item==null)return;
//		
//		FeatureSchema fsch=((Layer)item).getFeatureCollectionWrapper().getFeatureSchema();
//		Vector vect=new Vector();
//		for (int i =0;i<fsch.getAttributeCount();i++)
//			{
//		//	if (i!=fsch.getGeometryIndex())
//				vect.addElement(fsch.getAttributeName(i));
//			}
//		if (cbTargetField2 instanceof JComboBox)
//		{
//		((JComboBox)cbTargetField2).setModel(new DefaultComboBoxModel(vect));
//		((JComboBox)cbTargetField2).setSelectedItem(selected);
//		}
//		else if (cbTargetField2 instanceof JList)
//		{
//			((JList)cbTargetField2).setListData(vect);
//			((JList)cbTargetField2).setSelectedValue(selected,true);
//		}
//	}
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
	
	public static JComboBox initializeComboBox(String name, JComboBox comboBox, Collection coll)
	{

		Vector vL = new Vector (coll);
		comboBox.setModel(new DefaultComboBoxModel(vL));
		if(!vL.isEmpty())
			comboBox.setSelectedIndex(0);
		
		if (name != null)
			comboBox.setName(name);
		return comboBox;
	}
	
	private void putToBlackboard(String key, Object value)
	{
		bk.put(key,value);
	}
	
	private void setBlackboard(Blackboard blackboard)
	{
		this.bk = blackboard;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
