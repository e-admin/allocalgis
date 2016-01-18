/*
 * * The GEOPISTA project is a set of tools and applications to manage
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
 * Created on 08-oct-2004 by juacas
 *
 * 
 */
package com.geopista.ui.plugin.importer.panels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.importer.ImporterPlugIn;
import com.geopista.util.GeopistaFunctionUtils;

import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * Panel para la selección de capas y actualizacion de campos de capas
 * @author cotesa
 *
 */
public class SelectLayerPanel extends JPanel
{
	/**
	 * Logger for this class
	 */
	private static final Log logger	= LogFactory.getLog(SelectLayerPanel.class);

	private static final String SELECTEDTARGETLAYER = ImporterPlugIn.class.getName()+"_targetLayer";
	
	private JComboBox cbTargetLayer;
	protected PlugInContext localContext;
	public static Collection debuglayers=new Vector(); // for testing only
	
	public static Blackboard bk=new Blackboard();// for testing only
	
	private String selectedFieldName=null;
	public static final int FIELD_ACTIONED = 1;
	protected Layer selectedLayer=null;
	private ArrayList actionListeners = new ArrayList();

	public static final int SYSTEM_LAYER = 0;
	public static final int LOCAL_LAYER = 1;
	public static Collection systemLayerList=new Vector();
	public static Collection localLayerList=new Vector(); 
	
	private int layerType;
	
	public SelectLayerPanel() {
		super();
		initialize();
	}
	
	public SelectLayerPanel(PlugInContext context, int layerType) {
		super();
		this.layerType = layerType;
		setContext(context);
		initialize();
	}
	
	public void setContext(PlugInContext context)
	{
		localContext=context;	
	}
		
	private void initialize()
	{
		putToBlackboard(ImporterPlugIn.SELECTEDTARGETLAYER, null);
		putToBlackboard(ImporterPlugIn.SELECTEDSOURCELAYER, null);
		getLayerList();
		this.setLayout(new BorderLayout());
		//this.add(getTargetFieldScrollPane(), java.awt.BorderLayout.CENTER);
		this.add(getCbTargetLayer(), java.awt.BorderLayout.NORTH);
		
		if (layerType == SYSTEM_LAYER)
			GeopistaFunctionUtils.initializeCollatorSortedLayerComboBox(null, cbTargetLayer,
				null, "to-do", 
						systemLayerList);
		else
			GeopistaFunctionUtils.initializeCollatorSortedLayerComboBox(null, cbTargetLayer,
					null, "to-do", 
							localLayerList);
		
		cbTargetLayer.setSelectedIndex(-1);
	}
	private void getLayerList() {
				
		Collection allLayers = localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerManager().getLayers();
		Vector capas = new Vector(allLayers);		
		
		systemLayerList.clear();
		localLayerList.clear();
		
		//systemLayerList.add(new Layer());
		//localLayerList.add(new Layer());
		for (int i =0; i< capas.size(); i++)
		{
			GeopistaLayer capa = (GeopistaLayer)capas.get(i);
			if (!capa.isLocal() && layerType==SYSTEM_LAYER)
				systemLayerList.add(capa);
			else if (capa.isLocal() && layerType==LOCAL_LAYER)
				localLayerList.add(capa);			
		}		
	}
	
	private JComboBox getCbTargetLayer() {
		if (cbTargetLayer == null) {
			cbTargetLayer = new JComboBox();
		}

		cbTargetLayer.addActionListener(new java.awt.event.ActionListener() 
		{ 
			public void actionPerformed(java.awt.event.ActionEvent e)
			{
				Layer targetLayer = (Layer) ((JComboBox) e.getSource()).getSelectedItem();
				if (targetLayer==null)
					updateLayerList();

				selectedLayer = (Layer) ((JComboBox) e.getSource()).getSelectedItem();
				firePropertyChange("LAYERSELECTED_"+layerType, null, targetLayer);
				//updateFieldList(lstTargetField, targetLayer, getFromBlackboard(ATTRIBUTE_ID_ON_TARGET));
			}

		});
		return cbTargetLayer;
	}

	public void updateLayerList()
	{
		// si no hay ninguna capa seleccionada pero en el workbench 
		// sí la hay, toma esta como la seleccionada.
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
				
				
				Collection allLayers = localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerManager().getLayers();
				Vector capas = new Vector(allLayers);
				Vector capasLista = new Vector();
				for (int i =0; i< capas.size(); i++)
				{
					GeopistaLayer capa = (GeopistaLayer)capas.get(i);
					if (!capa.isLocal() && layerType==SYSTEM_LAYER)
						capasLista.add(capa);
					else if (capa.isLocal() && layerType==LOCAL_LAYER)
						capasLista.add(capa);			
				}	
				
				//GeopistaUtil.initializeCollatorSortedLayerComboBox(null, cbTargetLayer, targetLayer, "to-do", capasLista);
				GeopistaFunctionUtils.initializeCollatorSortedLayerComboBox(null, cbTargetLayer, null, "to-do", capasLista);
				//cbTargetLayer.setSelectedItem(targetLayer);
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
	/*
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
	*/
	
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
	
	public void setSelectedIndex(int index)
	{
		cbTargetLayer.setSelectedIndex(index);
	}
	
}  
