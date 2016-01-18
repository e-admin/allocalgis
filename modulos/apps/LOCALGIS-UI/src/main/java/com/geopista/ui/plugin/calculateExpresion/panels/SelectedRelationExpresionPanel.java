/**
 * SelectedRelationExpresionPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.calculateExpresion.panels;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collection;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class SelectedRelationExpresionPanel extends JPanel{
	
	ApplicationContext appContext=AppContext.getApplicationContext();
	
	protected PlugInContext localContext;
	private JComboBox origenComboBox;
	private JComboBox destinoComboBox;
	private Layer layerOrigen;
	private Layer layerDestino;
	public static Collection debuglayers=new Vector();
	
	private JLabel origenLabel = null;
	private JLabel destinoLabel = null;
	private JPanel origenPanel = null;
	private JPanel destinoPanel = null;
	
	private JTable origenTable = null;
	private JTable destinoTable = null;
	private JScrollPane jScrollOrigen = null;
	private JScrollPane jScrollDestino = null;
	private DefaultTableModel defaultTableModelOrigen = null; 
	private DefaultTableModel defaultTableModelDestino = null; 
	
	private JButton anadirRelacionButton;
	private JButton aceptarButton;
	private JPanel botonera;
	private JDialog d;

	Vector namesOrigen = new Vector();
	Vector namesDestino = new Vector();
	Vector rowsOrigen = new Vector();
	Vector rowsDestino = new Vector();
	
	//private 
	public SelectedRelationExpresionPanel() {
		super();
		initialize();
	}
	public SelectedRelationExpresionPanel(PlugInContext context,Layer layerOrigen, Layer layerDestino , JDialog d) {
		super();
		this.layerDestino = layerDestino;
		this.layerOrigen = layerOrigen;
		this.d = d;
		setContext(context);
		initialize();
	}
	
	private void initialize()
	{
	    this.setLayout(new BorderLayout());
	    
	    origenLabel = new JLabel();
		origenLabel.setText(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.origen"));
		destinoLabel = new JLabel();
		destinoLabel.setText(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.destino"));
		
		
		java.awt.GridBagConstraints gridBagConstraints00 = new GridBagConstraints();
	    java.awt.GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
	    java.awt.GridBagConstraints gridBagConstraints01 = new GridBagConstraints();
	    java.awt.GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
	    java.awt.GridBagConstraints gridBagConstraints02 = new GridBagConstraints();
	    java.awt.GridBagConstraints gridBagConstraints03 = new GridBagConstraints();
	    java.awt.GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
	    java.awt.GridBagConstraints gridBagConstraints04 = new GridBagConstraints();
	   
	    gridBagConstraints00.gridx = 0;
	    gridBagConstraints00.gridy = 0;
	    gridBagConstraints00.weightx = 0.01;
	    gridBagConstraints00.weighty = 0.01;
	    gridBagConstraints00.fill = java.awt.GridBagConstraints.CENTER;
	    gridBagConstraints00.gridheight = 1;
	    gridBagConstraints00.gridwidth =1;
		
	    gridBagConstraints10.gridx = 1;
	    gridBagConstraints10.gridy = 0;
	    gridBagConstraints10.weightx = 0.01;
	    gridBagConstraints10.weighty = 0.01;
	    gridBagConstraints10.fill = java.awt.GridBagConstraints.CENTER;
	    gridBagConstraints10.gridheight = 1;
	    gridBagConstraints10.gridwidth = 1;
		
	    
	    gridBagConstraints01.gridx = 0;
	    gridBagConstraints01.gridy = 1;
	    gridBagConstraints01.weightx = 0.01;
	    gridBagConstraints01.weighty = 0.01;
	    gridBagConstraints01.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints01.gridheight = 1;
	    gridBagConstraints01.gridwidth = 1;
	    
	    gridBagConstraints11.gridx = 1;
	    gridBagConstraints11.gridy = 1;
	    gridBagConstraints11.weightx =  0.01;
	    gridBagConstraints11.weighty =  0.01;
	    gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints11.gridheight = 1;
	    gridBagConstraints11.gridwidth = 1;
	    
	    gridBagConstraints02.gridx = 0;
	    gridBagConstraints02.gridy = 2;
	    gridBagConstraints02.weightx =  0.01;
	    gridBagConstraints02.weighty =  0.01;
	    gridBagConstraints02.fill = java.awt.GridBagConstraints.NONE;
	    gridBagConstraints02.gridheight = 1;
	    gridBagConstraints02.gridwidth = 2;
	    

	    
	    gridBagConstraints03.gridx = 0;
	    gridBagConstraints03.gridy = 3;
	    gridBagConstraints03.weightx = 1.0;
	    gridBagConstraints03.weighty = 1.0;
	    gridBagConstraints03.fill = java.awt.GridBagConstraints.BOTH;
	    gridBagConstraints03.gridheight = 1;
	    gridBagConstraints03.gridwidth = 1;
	    
	    gridBagConstraints13.gridx = 1;
	    gridBagConstraints13.gridy = 3;
	    gridBagConstraints13.weightx = 1.0;
	    gridBagConstraints13.weighty = 1.0;
	    gridBagConstraints13.fill = java.awt.GridBagConstraints.BOTH;
	    gridBagConstraints13.gridheight = 1;
	    gridBagConstraints13.gridwidth = 1;
	    
	    gridBagConstraints04.gridx = 1;
	    gridBagConstraints04.gridy = 4;
	    gridBagConstraints04.weightx = 1.00;
	    gridBagConstraints04.weighty = 0.01;
	    gridBagConstraints04.fill = java.awt.GridBagConstraints.WEST;
	    gridBagConstraints04.gridheight = 1;
	    gridBagConstraints04.gridwidth = 2;
	    
	    
	    this.setLayout(new GridBagLayout());
	    this.add(origenLabel, gridBagConstraints00);
	    this.add(destinoLabel, gridBagConstraints10);
	    
	   
	    this.add(getOrigenComboBox(), gridBagConstraints01);
	    this.add(getDestinoComboBox(), gridBagConstraints11);
	    
	    this.add(getAnadirRelacionButton(), gridBagConstraints02);
	    
	    this.add(getJScrollOrigen(), gridBagConstraints03);
		this.add(getJScrollDestino(), gridBagConstraints13);
		this.add(getBotonera(), gridBagConstraints04);
		
	
		
	    initializeFeaturesComboBox(null, origenComboBox,
	            layerOrigen, "to-do");
	    origenComboBox.setSelectedIndex(0);
	    
	    
	    initializeFeaturesComboBox(null, destinoComboBox,
	            layerDestino, "to-do");
	    destinoComboBox.setSelectedIndex(0);
	    
	    
	}
	
	
	public JComboBox getOrigenComboBox() {
		
		if (origenComboBox == null) {
			origenComboBox = new JComboBox();
		}
			
		return origenComboBox;
	}
	public void setOrigenComboBox(JComboBox origenComboBox) {
		this.origenComboBox = origenComboBox;
	}
	public JComboBox getDestinoComboBox() {
		if (destinoComboBox == null) {
			destinoComboBox = new JComboBox();
		}
			
		return destinoComboBox;
	}
	public void setDestinoComboBox(JComboBox destinoComboBox) {
		this.destinoComboBox = destinoComboBox;
	}
//	
//	public  Vector getValuesAsObjects(Collection features)
//	{
//		Iterator it=features.iterator();
//		Vector values=new Vector();
//		while (it.hasNext())
//		{
//			setFeature((Feature)it.next());
//			Object value=this.getValueAsObject();
//			if (value==null)
//				values.add(this.getErrorInfo());
//			else
//				values.add(value);
//		}
//		return values;
//	}
//	
	private Collection getAffectedFeatures(Layer layer)
	{
		if ( localContext!=null)
		{
			return localContext.getActiveTaskComponent().getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems(layer);
		}
		else
			if (layer==null)
				return  new Vector();
			else
				return layer.getFeatureCollectionWrapper().getFeatures();
	}
	
	
	public void setContext(PlugInContext context)
	{
		localContext=context;	
	}

	public static JComboBox initializeFeaturesComboBox(String name, JComboBox comboBox, Layer layer, String toolTipText)
	{

		FeatureSchema fsch = layer.getFeatureCollectionWrapper().getFeatureSchema();
		Vector vect=new Vector();
		for (int i =0;i<fsch.getAttributeCount();i++)
		{
			if (i != fsch.getGeometryIndex()){
				vect.addElement(fsch.getAttributeName(i));
			}
		}
		comboBox.setModel(new DefaultComboBoxModel(vect));

		return comboBox;
	}
	
	public JButton getAnadirRelacionButton() {
		if(anadirRelacionButton == null){
			anadirRelacionButton = new JButton();

			anadirRelacionButton.setEnabled(true);
			anadirRelacionButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			anadirRelacionButton.setToolTipText(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.relaccionar.relacionar"));
			anadirRelacionButton.setText(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.relaccionar.relacionar"));
			anadirRelacionButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
		
					Vector rowOrig = new Vector();
					rowOrig.add(getOrigenComboBox().getSelectedItem().toString());
					rowsOrigen.add(rowOrig);
					origenTable.setModel(new DefaultTableModel(rowsOrigen, namesOrigen));
					
					Vector rowDest = new Vector();
					rowDest.add(getDestinoComboBox().getSelectedItem().toString());
					rowsDestino.add(rowDest);
					destinoTable.setModel(new DefaultTableModel(rowsDestino, namesDestino));

				}
			});
			
		}
		return anadirRelacionButton;
	}
	public void setAnadirRelacionButton(JButton anadirRelacionButton) {
		this.anadirRelacionButton = anadirRelacionButton;
	}

	private DefaultTableModel getDefaultTableModelOrigen() {
		if (defaultTableModelOrigen == null) {
			defaultTableModelOrigen = new DefaultTableModel();
			defaultTableModelOrigen.setColumnCount(1);
		}
		return defaultTableModelOrigen;
	}
	
	private DefaultTableModel getDefaultTableModelDestino() {
		if (defaultTableModelDestino == null) {
			defaultTableModelDestino = new DefaultTableModel();
			defaultTableModelDestino.setColumnCount(1);
		}
		return defaultTableModelDestino;
	}

	private JScrollPane getJScrollOrigen() {
		if (jScrollOrigen == null) {
			jScrollOrigen = new JScrollPane();
			jScrollOrigen.setViewportView(getOrigenTable());
			jScrollOrigen.setHorizontalScrollBarPolicy(jScrollOrigen.HORIZONTAL_SCROLLBAR_ALWAYS);
			jScrollOrigen.setPreferredSize(new java.awt.Dimension(800,300));
		}
		return jScrollOrigen;
	}
	
	private JScrollPane getJScrollDestino() {
		if (jScrollDestino == null) {
			jScrollDestino = new JScrollPane();
			jScrollDestino.setViewportView(getDestinoTable());
			jScrollDestino.setHorizontalScrollBarPolicy(jScrollDestino.HORIZONTAL_SCROLLBAR_ALWAYS);
			jScrollDestino.setPreferredSize(new java.awt.Dimension(800,300));
		}
		return jScrollDestino;
	}
	
	private JTable getDestinoTable() {
		if (destinoTable == null) {
			destinoTable = new JTable();
			destinoTable.setRowSelectionAllowed(false);
			destinoTable.setAutoResizeMode(1);

			namesDestino.add("Atributo");
			Vector rows = new Vector();
			destinoTable.setModel(new DefaultTableModel(rows, namesDestino));
			
		}
		return destinoTable;
	}
	
	private JTable getOrigenTable() {
		if (origenTable == null) {
			origenTable = new JTable();
			origenTable.setRowSelectionAllowed(false);
			origenTable.setAutoResizeMode(1);

			namesOrigen.add("Atributo");
			Vector rows = new Vector();
			origenTable.setModel(new DefaultTableModel(rows, namesOrigen));
			
		}
		return origenTable;
	}
	
	public JButton getAceptarButton() {
		if(aceptarButton == null){
		
			aceptarButton = new JButton();
	
			aceptarButton.setEnabled(true);
			aceptarButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			aceptarButton.setToolTipText(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.relaccionar.aceptar"));
			aceptarButton.setText(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.relaccionar.aceptar"));
			
			
			aceptarButton.addActionListener(new java.awt.event.ActionListener() { 
			public void actionPerformed(java.awt.event.ActionEvent e) {    
	
					d.dispose();
					
				}
			});
			
		}
		return aceptarButton;
	}
	public void setAceptarButton(JButton aceptarButton) {
		this.aceptarButton = aceptarButton;
	}
	

	public JPanel getBotonera() {
		if(botonera == null){
			botonera = new JPanel();
			botonera.add(getAceptarButton());

		}
		return botonera;
	}
	public void setBotonera(JPanel botonera) {
		this.botonera = botonera;
	}
}
