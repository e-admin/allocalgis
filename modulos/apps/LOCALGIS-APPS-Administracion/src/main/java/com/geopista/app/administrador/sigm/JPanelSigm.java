/**
 * JPanelSigm.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.administrador.sigm;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.acl.AclNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.licencias.utilidades.CheckBoxRenderer;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.global.ServletConstants;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPrincipal;
import com.localgis.client.sigm.SigmClient;
import com.localgis.web.core.model.Procedure;
import com.localgis.web.core.model.ProcedureDefaults;
import com.localgis.web.core.model.ProcedureProperty;
import com.localgis.web.core.model.ProcedurePropertyKey;

public class JPanelSigm extends javax.swing.JPanel {
	
	/**
	 * Logger
	 */
	Logger logger = Logger.getLogger(JPanelSigm.class);
	
	/**
	 * Variables
	 */
	private HashMap<String, Procedure> procedures = null;
	
	private String featureToolbarNameSelected = null;
	
	private TableSorted proceduresSorter;
	private TableSorted procedurePropertiesSorter;
	private ProceduresTableModel proceduresModel;
	private ProcedurePropertiesTableModel procedurePropertiesModel;
	
	private HashMap<ProcedurePropertyKey, ProcedureProperty> procedurePropertiesTemp;
	
	//0=inicial 1=insertar 2=editar 3=eliminar
	private int procedureStatus = 0;
	private int procedurePropertyStatus = 0;

	private ResourceBundle mensajesProcedure;
	
	private JFrame framePadre;
		
	private boolean permisoEdicionCompleta = false;
	private boolean permisoEdicionParcial = false;
	
	private HashMap<Integer,String> featureToolbarName = null;
	
	private Procedure procedureSelected = null;	
	private ProcedureDefaults procedureDefaults = null;
	private HashMap<String, ProcedureProperty> procedureProperties = null;
	private ProcedureProperty procedurePropertySelected = null;
	private ProcedureDefaults auxProcedureDefaults = null;
	private HashMap<String, ProcedureProperty> auxProcedureProperties = null;
	private Procedure auxProcedure = null;
	
	
	public JPanelSigm(ResourceBundle messages, JFrame framePadre) {
		this.framePadre = framePadre;
		initComponents(messages);
	}

	private void initComponents(ResourceBundle messages) {// GEN-BEGIN:initComponents

		mensajesProcedure = messages;
	
		jPanelEditProcedure = new javax.swing.JPanel();
		jPanelProcedures = new javax.swing.JPanel();
		
		jTableProcedures = new javax.swing.JTable();
		jTableProcedureProperties = new javax.swing.JTable();
		
		jButtonInsertProcedure = new javax.swing.JButton();
		jButtonDeleteProcedure = new javax.swing.JButton();
		jButtonDeleteProcedure.setEnabled(false);
		jButtonEditProcedure = new javax.swing.JButton();
		jButtonEditProcedure.setEnabled(false);
		
		jButtonInsertProcedureProperty = new javax.swing.JButton();
		jButtonInsertProcedureProperty.setEnabled(false);
		jButtonEditProcedureProperty = new javax.swing.JButton();
		jButtonEditProcedureProperty.setEnabled(false);
		jButtonDeleteProcedureProperty = new javax.swing.JButton();
		jButtonDeleteProcedureProperty.setEnabled(false);
		
		
		jLabelId = new javax.swing.JLabel();		
		jLabelTableName = new javax.swing.JLabel();
		jLabelLayerName = new javax.swing.JLabel();
		jLabelMapName = new javax.swing.JLabel();
		jLabelProcedureType = new javax.swing.JLabel();
		jLabelFeatureName = new javax.swing.JLabel();
		jLabelStyleProperty = new javax.swing.JLabel();
		jLabelAddressProperty = new javax.swing.JLabel();
		jLabelProperty = new javax.swing.JLabel();
		jLabelName = new javax.swing.JLabel();
		jLabelType = new javax.swing.JLabel();
		jLabelSearch = new javax.swing.JLabel();
		jLabelActive = new javax.swing.JLabel();
		
		jTextFieldFeatureName = new com.geopista.app.utilidades.TextField(32);
		jTextFieldStyleProperty = new com.geopista.app.utilidades.TextField(32);
		jTextFieldAddressProperty = new com.geopista.app.utilidades.TextField(32);
		jTextFieldId = new com.geopista.app.utilidades.TextField(32);
		jTextFieldTableName = new com.geopista.app.utilidades.TextField(32);
		jTextFieldLayerName = new com.geopista.app.utilidades.TextField(32);
		jTextFieldMapName = new com.geopista.app.utilidades.TextField(32);
		jTextFieldProcedureType = new com.geopista.app.utilidades.TextField(32);
		jTextFieldProperty = new com.geopista.app.utilidades.TextField(32);
		jTextFieldName = new com.geopista.app.utilidades.TextField(32);
		jTextFieldType = new com.geopista.app.utilidades.TextField(32);
		
		jCheckBoxSearch = new javax.swing.JCheckBox();
		jCheckBoxActive = new javax.swing.JCheckBox();
		
		jTextFieldBusquedaProcedure = new com.geopista.app.utilidades.TextField(64);
		
		jLabelBusquedaProcedure = new javax.swing.JLabel();
		jLabelFeatureToolBarName = new javax.swing.JLabel();
	
		jButtonCancel = new javax.swing.JButton();
		jButtonAccept = new javax.swing.JButton();
		jButtonCancelProcedureProperty = new javax.swing.JButton();
		jButtonAcceptProcedureProperty = new javax.swing.JButton();

		jButtonProcedureAll = new javax.swing.JButton();

        sigmJTabbedPane = new javax.swing.JTabbedPane();
        jPanelProcedure = new JPanel();
		jPanelProcedureDefaults = new JPanel();
		jPanelProcedureProperties = new JPanel();
        
		proceduresJScrollPane = new javax.swing.JScrollPane();
		procedurePropertiesJScrollPane = new javax.swing.JScrollPane();

		setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		jPanelProcedures
				.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		
		proceduresJScrollPane.setViewportView(jTableProcedures);
	
		jPanelProcedures.add(proceduresJScrollPane,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 550,
						180));

		jButtonInsertProcedure.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				anadirProcedureActionPerformed();
			}
		});

		jPanelProcedures.add(jButtonInsertProcedure,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(575, 75, 75,
						-1));

		jButtonEditProcedure.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				editarProcedure();
			}
		});		
		
		jPanelProcedures.add(jButtonEditProcedure,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(575, 110, 75,
						-1));
		
		jButtonDeleteProcedure
		.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				eliminarProcedureActionListener();
			}
		});		
		
		jPanelProcedures.add(jButtonDeleteProcedure,
		new org.netbeans.lib.awtextra.AbsoluteConstraints(575, 145, 75,
				-1));
		
		add(jPanelProcedures,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 670,
						250));
		
		jPanelEditProcedure
				.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
			
			jPanelProcedure.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
			jPanelProcedureDefaults.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
			jPanelProcedureProperties.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
			
			sigmJTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
	        sigmJTabbedPane.setFont(new java.awt.Font("Arial", 0, 10));	        
			sigmJTabbedPane.addTab(messages.getString("CSigmFrame.sigmJTabbedPane.procedure"), jPanelProcedure);
			sigmJTabbedPane.addTab(messages.getString("CSigmFrame.sigmJTabbedPane.defaults"), jPanelProcedureDefaults);
			sigmJTabbedPane.addTab(messages.getString("CSigmFrame.sigmJTabbedPane.properties"), jPanelProcedureProperties);
		    
		    jPanelEditProcedure.add(sigmJTabbedPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 0, 870,
					365));	
		    
		    
		    jButtonCancel.setEnabled(false);
			jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					cancelarActionPerformed();
				}
			});
			jPanelEditProcedure.add(jButtonCancel,
					new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 370,
							110, -1));
			
			jButtonAccept.setEnabled(false);
			jButtonAccept
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent evt) {
							aceptarActionPerformed();
						}
					});
			jPanelEditProcedure.add(jButtonAccept,
					new org.netbeans.lib.awtextra.AbsoluteConstraints(415, 370,
							110, -1));
		    
		    /*--- Tab - Procedures ---*/		    
		    jPanelProcedure.add(jLabelId,
						new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 38, 100,
								-1));			    
			jPanelProcedure.add(jTextFieldId,
						new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 35, 120,
								-1));
			    
		    jPanelProcedure.add(jLabelTableName,
					new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 63, 100,
							-1));		    
		    jPanelProcedure.add(jTextFieldTableName,
					new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 60, 120,
							-1));						
		    
		    jPanelProcedure.add(jLabelLayerName,
					new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 88, 100,
							-1));		    
		    jPanelProcedure.add(jTextFieldLayerName,
					new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 85, 120,
							-1));
		    
		    jPanelProcedure.add(jLabelMapName,
						new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 113, 100,
								-1));			    
		    jPanelProcedure.add(jTextFieldMapName,
						new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 120,
								-1));
		    
		    jPanelProcedure.add(jLabelProcedureType,
						new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 138, 100,
								-1));			    
		    jPanelProcedure.add(jTextFieldProcedureType,
						new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 135, 120,
								-1));
		    
		    
		    
		    /*--- Tab - Procedure Defaults ---*/
		    jPanelProcedureDefaults.add(jLabelFeatureName,
					new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 38, 100,
							-1));		    
		    jPanelProcedureDefaults.add(jTextFieldFeatureName,
					new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 35, 120,
							-1));
		   
		    jPanelProcedureDefaults.add(jLabelFeatureToolBarName,
						new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 63, 100,
								-1));			    
			jPanelProcedureDefaults.add(getJComboBoxFeatureToolbarName(),
						new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 60, 120,
								-1));
		    
		    jPanelProcedureDefaults.add(jLabelStyleProperty,
					new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 88, 100,
							-1));		    
		    jPanelProcedureDefaults.add(jTextFieldStyleProperty,
					new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 85, 120,
							-1));
		    
		    jPanelProcedureDefaults.add(jLabelAddressProperty,
						new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 113, 100,
								-1));			    
			jPanelProcedureDefaults.add(jTextFieldAddressProperty,
						new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 120,
								-1));
		    
		    
			/*--- Tab - Procedure Properties ---*/

			procedurePropertiesJScrollPane.setViewportView(jTableProcedureProperties);
			jPanelProcedureProperties.add(procedurePropertiesJScrollPane,
							new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 500,
									300));
			
			jButtonInsertProcedureProperty
			.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					insertarProcedureProperty();
				}
			});					
			jPanelProcedureProperties.add(jButtonInsertProcedureProperty,
			new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 35, 75,
					-1));
			
			jButtonEditProcedureProperty
			.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					editarProcedureProperty();
				}
			});					
			jPanelProcedureProperties.add(jButtonEditProcedureProperty,
			new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 35, 75,
					-1));
			
			jButtonDeleteProcedureProperty
			.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					eliminarProcedurePropertyActionListener();
				}
			});					
			jPanelProcedureProperties.add(jButtonDeleteProcedureProperty,
			new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 35, 75,
					-1));			
					
			jPanelProcedureProperties.add(jLabelProperty,
					new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 108, 100,
							-1));		    
			jPanelProcedureProperties.add(jTextFieldProperty,
					new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 105, 170,
							-1));
		   
			jPanelProcedureProperties.add(jLabelName,
						new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 133, 100,
								-1));			    
			jPanelProcedureProperties.add(jTextFieldName,
						new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 130, 170,
								-1));
		    
			jPanelProcedureProperties.add(jLabelType,
					new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 158, 100,
							-1));		    
			jPanelProcedureProperties.add(jTextFieldType,
					new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 155, 170,
							-1));
 
			jPanelProcedureProperties.add(jLabelSearch,
					new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 183, 100,
							-1));		    
			jPanelProcedureProperties.add(jCheckBoxSearch,
					new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 180, 120,
							-1));
			
			jPanelProcedureProperties.add(jLabelActive,
					new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 208, 100,
							-1));		    
			jPanelProcedureProperties.add(jCheckBoxActive,
					new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 205, 120,
							-1));
		
		    
		    jButtonCancelProcedureProperty.setEnabled(false);
		    jButtonCancelProcedureProperty.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					cancelarProcedurePropertyActionPerformed();
				}
			});
		    jPanelProcedureProperties.add(jButtonCancelProcedureProperty,
					new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 250,
							110, -1));
			
			jButtonAcceptProcedureProperty.setEnabled(false);
			jButtonAcceptProcedureProperty
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent evt) {
							aceptarProcedurePropertyActionPerformed();
						}
					});
			jPanelProcedureProperties.add(jButtonAcceptProcedureProperty,
					new org.netbeans.lib.awtextra.AbsoluteConstraints(695, 250,
							110, -1));
		    
			
			
			
			
			
		    
		jTextFieldBusquedaProcedure.addKeyListener(new KeyListener(){

			
			public void keyTyped(KeyEvent e) {
			}

			
			public void keyPressed(KeyEvent e) {				
			}

			
			public void keyReleased(KeyEvent e) {			
				actualizarProceduresModel(jTextFieldBusquedaProcedure.getText());
				disableAll();
			}
		});		
		
		jPanelProcedures.add(jTextFieldBusquedaProcedure,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 25, 200,
						-1));
		
		jButtonProcedureAll.addMouseListener(new MouseListener(){
			
			public void mouseClicked(MouseEvent e) {
				jTextFieldBusquedaProcedure.setText("");				
				disableAll();
				actualizarProceduresModel("");
			}

			
			public void mousePressed(MouseEvent e) {
			}

			
			public void mouseReleased(MouseEvent e) {
			}

			
			public void mouseEntered(MouseEvent e) {
			}

			
			public void mouseExited(MouseEvent e) {
			}
			
		});
		
		jPanelProcedures.add(jButtonProcedureAll,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 24, 80,
						-1));
		
		jTextFieldId.setEditable(false);
		jTextFieldTableName.setEditable(false);
		jTextFieldLayerName.setEditable(false);
		jTextFieldMapName.setEditable(false);
		jTextFieldProcedureType.setEditable(false);		
		
		jTextFieldFeatureName.setEditable(false);
		jTextFieldStyleProperty.setEditable(false);
		jTextFieldAddressProperty.setEditable(false);
		jComboBoxFeatureToolbarName.setEnabled(false);
		
		jTextFieldProperty.setEditable(false);
		jTextFieldName.setEditable(false);
		jTextFieldType.setEditable(false);
		jCheckBoxSearch.setEnabled(false);
		jCheckBoxActive.setEnabled(false);


		add(jPanelEditProcedure,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 255, 880,
						395));

		// Para seleccionar una fila
		ListSelectionModel rowProcedure = jTableProcedures.getSelectionModel();
		rowProcedure.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				seleccionarProcedure(e);
			}
		});
		
		ListSelectionModel rowProcedureProperties = jTableProcedureProperties.getSelectionModel();
		rowProcedureProperties.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				seleccionarProcedureProperty(e);
			}
		});
	}
		
	private JComboBox getJComboBoxFeatureToolbarName(){
		if(jComboBoxFeatureToolbarName == null){
			jComboBoxFeatureToolbarName = new javax.swing.JComboBox();
			featureToolbarName = new HashMap<Integer,String>();
			featureToolbarName.put(0, "");
			featureToolbarName.put(1, "ToolbarWfstPolygonLocalgis");
			featureToolbarName.put(2, "ToolbarWfstPathLocalgis");
			featureToolbarName.put(3, "ToolbarWfstPointLocalgis");
			jComboBoxFeatureToolbarName.addItem(" ");
			jComboBoxFeatureToolbarName.addItem(mensajesProcedure
					.getString("CSigmFrame.jComboBoxFeatureToolbarName.polygon"));
			jComboBoxFeatureToolbarName.addItem(mensajesProcedure
					.getString("CSigmFrame.jComboBoxFeatureToolbarName.path"));
			jComboBoxFeatureToolbarName.addItem(mensajesProcedure
					.getString("CSigmFrame.jComboBoxFeatureToolbarName.point"));
			jComboBoxFeatureToolbarName
					.addActionListener(new java.awt.event.ActionListener() {
		
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							jComboBoxFeatureToolbarNameActionPerformed();
						}
			});
		}
		return jComboBoxFeatureToolbarName;
	}

	public void editable(boolean b) {
		jButtonInsertProcedure.setEnabled(b);
		// jButtonUsuEliminar.setEnabled(b);
	}

	public void changeScreenLang(ResourceBundle messages) {
		mensajesProcedure = messages;
		jPanelProcedures.setBorder(new javax.swing.border.TitledBorder(messages.getString("CSigmFrame.jPanelProcedures")));
		//jPanelEditProcedure.setBorder(new javax.swing.border.TitledBorder(""));

		jButtonInsertProcedure.setText(messages.getString("CSigmFrame.jButtonInsertProcedure"));
		jButtonDeleteProcedure.setText(messages.getString("CSigmFrame.jButtonDeleteProcedure"));
		jButtonEditProcedure.setText(messages.getString("CSigmFrame.jButtonEditProcedure"));
		jButtonInsertProcedureProperty.setText(messages.getString("CSigmFrame.jButtonInsertProcedure"));
		jButtonEditProcedureProperty.setText(messages.getString("CSigmFrame.jButtonEditProcedure"));
		jButtonDeleteProcedureProperty.setText(messages.getString("CSigmFrame.jButtonDeleteProcedure"));
		
		jLabelFeatureName.setText(messages.getString("CSigmFrame.jLabelFeatureName"));
		jLabelFeatureToolBarName.setText(messages.getString("CSigmFrame.jLabelFeatureToolBarName"));
		jLabelStyleProperty.setText(messages.getString("CSigmFrame.jLabelStyleProperty"));
		jLabelAddressProperty.setText(messages.getString("CSigmFrame.jLabelAddressProperty"));
		jLabelId.setText(messages.getString("CSigmFrame.jLabelId"));
		jLabelTableName.setText(messages.getString("CSigmFrame.jLabelTableName"));
		jLabelLayerName.setText(messages.getString("CSigmFrame.jLabelLayerName"));
		jLabelMapName.setText(messages.getString("CSigmFrame.jLabelMapName"));
		jLabelProcedureType.setText(messages.getString("CSigmFrame.jLabelProcedureType"));		
		jLabelProperty.setText(messages.getString("CSigmFrame.jLabelProperty"));
		jLabelName.setText(messages.getString("CSigmFrame.jLabelName"));
		jLabelType.setText(messages.getString("CSigmFrame.jLabelType"));
		jLabelSearch.setText(messages.getString("CSigmFrame.jLabelSearch"));
		jLabelActive.setText(messages.getString("CSigmFrame.jLabelActive"));
		
		
		
		jLabelBusquedaProcedure.setText(messages
				.getString("CSigmFrame.jLabelBusquedaProcedure"));

		jButtonProcedureAll.setText(messages
				.getString("CSigmFrame.jButtonProcedureAll"));

		jButtonCancel.setText(messages
				.getString("CSigmFrame.jButtonCancel"));
		jButtonAccept.setText(messages
				.getString("CSigmFrame.jButtonAccept"));
		
		jButtonCancelProcedureProperty.setText(messages
				.getString("CSigmFrame.jButtonCancel"));
		jButtonAcceptProcedureProperty.setText(messages
				.getString("CSigmFrame.jButtonAccept"));
	
		jButtonEditProcedure.setToolTipText(messages
				.getString("CSigmFrame.jButtonEditProcedure"));
		jButtonAccept.setToolTipText(messages
				.getString("CSigmFrame.jButtonAccept"));
		jButtonInsertProcedure.setToolTipText(messages
				.getString("CSigmFrame.jButtonInsertProcedure"));
		jButtonDeleteProcedure.setToolTipText(messages
				.getString("CSigmFrame.jButtonDeleteProcedure"));
		jButtonDeleteProcedureProperty.setToolTipText(messages
				.getString("CSigmFrame.jButtonDeleteProcedure"));
	}

	private void jComboBoxFeatureToolbarNameActionPerformed() {
		featureToolbarNameSelected = featureToolbarName.get(jComboBoxFeatureToolbarName.getSelectedIndex());
	}

	private void guardarCambios() {
		try {

			auxProcedure = new Procedure();
			auxProcedure.setId(jTextFieldId.getText());
			auxProcedure.setTableName(jTextFieldTableName.getText());
			auxProcedure.setLayerName(jTextFieldLayerName.getText());
			auxProcedure.setMapName(jTextFieldMapName.getText());
			auxProcedure.setProcedureType(jTextFieldProcedureType.getText());

			auxProcedureDefaults = new ProcedureDefaults();
			auxProcedureDefaults.setId(auxProcedure.getId());
			auxProcedureDefaults.setFeatureName(jTextFieldFeatureName.getText());
			auxProcedureDefaults.setStyleProperty(jTextFieldStyleProperty.getText());
			auxProcedureDefaults.setAddressProperty(jTextFieldAddressProperty.getText());
			auxProcedureDefaults.setFeatureToolbarname(featureToolbarNameSelected);
			auxProcedureDefaults.setOnfeatureunselecttext("Seleccione " + jTextFieldFeatureName.getText());
			auxProcedureDefaults.setOnnotfeatureinfotext(jTextFieldFeatureName.getText() + " sin datos");
			auxProcedureDefaults.setOnnotfeaturesearchtext("No se encontró " + jTextFieldFeatureName.getText() + " con estas características");
						
		} catch (Exception e) {
			java.io.StringWriter sw = new java.io.StringWriter();
			java.io.PrintWriter pw = new java.io.PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Error al guardar los cambios:" + sw.toString());
		}

	}
	
	private void guardarCambiosProcedureProperty() {
		try {	
			if(procedurePropertySelected==null){
				procedurePropertySelected = new ProcedureProperty();
			}
			procedurePropertySelected.setId(jTextFieldId.getText());
			procedurePropertySelected.setGrouptitle("");
			procedurePropertySelected.setProperty(jTextFieldProperty.getText());
			procedurePropertySelected.setName(jTextFieldName.getText());
			procedurePropertySelected.setType(jTextFieldType.getText());
			procedurePropertySelected.setSearchactive(jCheckBoxSearch.isSelected());
			procedurePropertySelected.setActive(jCheckBoxActive.isSelected());						
		} catch (Exception e) {
			java.io.StringWriter sw = new java.io.StringWriter();
			java.io.PrintWriter pw = new java.io.PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Error al guardar los cambios:" + sw.toString());
		}

	}
	
	private void disableAll(){
		jTextFieldId.setEditable(false);
		jTextFieldTableName.setEditable(false);
		jTextFieldLayerName.setEditable(false);
		jTextFieldMapName.setEditable(false);
		jTextFieldProcedureType.setEditable(false);		
		
		jTextFieldFeatureName.setEditable(false);
		jTextFieldStyleProperty.setEditable(false);
		jTextFieldAddressProperty.setEditable(false);
		jComboBoxFeatureToolbarName.setEnabled(false);
		
		jTextFieldProperty.setEditable(false);
		jTextFieldName.setEditable(false);
		jTextFieldType.setEditable(false);
		jCheckBoxSearch.setEnabled(false);
		jCheckBoxActive.setEnabled(false);
		
		jButtonInsertProcedure.setEnabled(true);
		jButtonEditProcedure.setEnabled(false);
		jButtonDeleteProcedure.setEnabled(false);
		jButtonInsertProcedureProperty.setEnabled(false);
		jButtonEditProcedureProperty.setEnabled(false);
		jButtonDeleteProcedureProperty.setEnabled(false);	
		
		jButtonAccept.setEnabled(false);
		jButtonCancel.setEnabled(false);	
		jButtonAcceptProcedureProperty.setEnabled(false);
		jButtonCancelProcedureProperty.setEnabled(false);	
		
		mostrarProcedure(null,null,null);
		mostrarProcedureProperty(null);
		
		procedureStatus = 0;
		procedurePropertyStatus = 0;
	}

	public void pintarProcedures(HashMap<String, Procedure> procedures) {
		this.procedures = procedures;
		actualizarProceduresModel();
	}
	
	public void pintarProcedureProperties(HashMap<String, ProcedureProperty> procedureProperties) {
		this.procedureProperties = procedureProperties;
		actualizarProcedurePropertiesModel(procedureProperties);
	}
	
	private void seleccionarProcedure(ListSelectionEvent e) {
		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		if (lsm.isSelectionEmpty()) {
		} else {
			int selectedRow = lsm.getMinSelectionIndex();
			String idProcedure = (String) proceduresSorter.getValueAt(selectedRow,
					0);
			procedureSelected = procedures.get(idProcedure);
			if (procedureSelected == null) {
				JOptionPane optionPane = new JOptionPane(mensajesProcedure
						.getString("CSigmFrame.jPanelProcedure.mensaje.procedurenoencontrado"),
						JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog = optionPane.createDialog(this, "");
				dialog.show();
			}

			else {
				// miramos si el procedure que está en sesión tiene los permisos
				// de editar sus propios datos
				try {
					/*
					GeopistaAcl aclAdministracion = com.geopista.security.SecurityManager
							.getPerfil("Administracion");
					boolean permisoEdicionParcial = aclAdministracion
							.checkPermission(new com.geopista.security.GeopistaPermission(
									com.geopista.security.GeopistaPermission.EDIT_USER_DATA_ADMINITRACION));
					boolean permisoEdicionCompleto = aclAdministracion
							.checkPermission(new com.geopista.security.GeopistaPermission(
									com.geopista.security.GeopistaPermission.EDITAR_ADMINITRACION));
					*/
					boolean permisoEdicionParcial = true;
					boolean permisoEdicionCompleto = true;
					
					procedureDefaults = new SigmClient(Constantes.url + ServletConstants.SIGM_SERVLET).getProcedureDefaults(idProcedure);
					procedureProperties = new SigmClient(Constantes.url + ServletConstants.SIGM_SERVLET).getProcedurePropertiesMap(idProcedure);
					auxProcedureProperties = procedureProperties;
					
					GeopistaPrincipal principal = com.geopista.security.SecurityManager
							.getPrincipal();
					String procedureLogueado = principal.getName();

					if (permisoEdicionCompleto) {
						this.jButtonDeleteProcedure.setEnabled(true);
					} else {
						this.jButtonDeleteProcedure.setEnabled(false);
					}

					if ((permisoEdicionParcial && procedureSelected.getId()
							.equalsIgnoreCase(procedureLogueado))
							|| permisoEdicionCompleto){
						this.jButtonEditProcedure.setEnabled(true);						
					}
					else{
						this.jButtonEditProcedure.setEnabled(false);
					}
					
					jButtonEditProcedureProperty.setEnabled(false);
					jButtonDeleteProcedureProperty.setEnabled(false);	

				} catch (AclNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			mostrarProcedure(procedureSelected, procedureDefaults, procedureProperties);
		}
	}// fin método seleccionarProcedure
	
	private void seleccionarProcedureProperty(ListSelectionEvent e) {
			ListSelectionModel lsm = (ListSelectionModel) e.getSource();
			if (lsm.isSelectionEmpty()) {
			} else {
				int selectedRow = lsm.getMinSelectionIndex();
				String idProcedureProperty = (String) procedurePropertiesSorter.getValueAt(selectedRow,
						0);
					procedurePropertySelected = getSelectedProperty(idProcedureProperty);
					if (procedurePropertySelected == null) {
						JOptionPane optionPane = new JOptionPane(mensajesProcedure
								.getString("CSigmFrame.jPanelProcedure.mensaje.procedurepropertynoencontrado"),
								JOptionPane.INFORMATION_MESSAGE);
						JDialog dialog = optionPane.createDialog(this, "");
						dialog.show();
					}
					else {
						// miramos si el procedure que está en sesión tiene los permisos
						// de editar sus propios datos
						try {
							/*
							GeopistaAcl aclAdministracion = com.geopista.security.SecurityManager
									.getPerfil("Administracion");
							boolean permisoEdicionParcial = aclAdministracion
									.checkPermission(new com.geopista.security.GeopistaPermission(
											com.geopista.security.GeopistaPermission.EDIT_USER_DATA_ADMINITRACION));
							boolean permisoEdicionCompleto = aclAdministracion
									.checkPermission(new com.geopista.security.GeopistaPermission(
											com.geopista.security.GeopistaPermission.EDITAR_ADMINITRACION));
							*/
							boolean permisoEdicionParcial = true;
							boolean permisoEdicionCompleto = true;
							
							GeopistaPrincipal principal = com.geopista.security.SecurityManager
									.getPrincipal();
							String procedureLogueado = principal.getName();

							if (permisoEdicionCompleto) {
								this.jButtonDeleteProcedureProperty.setEnabled(true);
							} else {
								this.jButtonDeleteProcedureProperty.setEnabled(false);
							}

							if ((permisoEdicionParcial && procedureSelected.getId()
									.equalsIgnoreCase(procedureLogueado))
									|| permisoEdicionCompleto){
								this.jButtonEditProcedureProperty.setEnabled(true);
							}
							else{
								this.jButtonEditProcedureProperty.setEnabled(false);
							}
							/*
						} catch (AclNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							*/
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					this.jButtonInsertProcedureProperty.setEnabled(false);
					mostrarProcedureProperty(procedurePropertySelected);
			}
	}
	
	private ProcedureProperty getSelectedProperty(String idProcedureProperty){
		if(procedureProperties != null && procedureProperties.size()>0){
			Iterator<ProcedureProperty> it = procedureProperties.values().iterator();
			while(it.hasNext()){
				ProcedureProperty procedureProperty = it.next();
				if(procedureProperty.getProperty().equals(idProcedureProperty)){
					return procedureProperty;
				}
			}	
		}	
		return null;
	}

	/**
	 * Habilitamos los componentes.
	 * 
	 * @param bEnabled
	 */
	private void switchStatus() {

		com.geopista.security.GeopistaAcl aclAdministracion;
		try {
			/*
			aclAdministracion = com.geopista.security.SecurityManager
					.getPerfil("Administracion");
			permisoEdicionCompleta = aclAdministracion
					.checkPermission(new com.geopista.security.GeopistaPermission(
							com.geopista.security.GeopistaPermission.EDITAR_ADMINITRACION));
			permisoEdicionParcial = aclAdministracion
					.checkPermission(new com.geopista.security.GeopistaPermission(
							"Geopista.Administracion.Edit"));
			*/
			boolean permisoEdicionParcial = true;
			boolean permisoEdicionCompleto = true;
			
			//jTextFieldBusquedaProcedure.setText("");
			
			if(procedureStatus==1){
				jTextFieldId.setEditable(true);
				jTextFieldTableName.setEditable(true);					
				jTextFieldLayerName.setEditable(true);
				jTextFieldMapName.setEditable(true);
				jTextFieldProcedureType.setEditable(true);
								
				jTextFieldFeatureName.setEditable(true);
				jComboBoxFeatureToolbarName.setEnabled(true);
				jTextFieldStyleProperty.setEditable(true);
				jTextFieldAddressProperty.setEditable(true);			
					
				jButtonInsertProcedure.setEnabled(false);	
				jButtonEditProcedure.setEnabled(false);	
				jButtonDeleteProcedure.setEnabled(false);				
				
				jButtonAccept.setEnabled(true);
				jButtonCancel.setEnabled(true);
				
				//jTableProcedures.getSelectionModel().clearSelection();
				
				if(procedurePropertyStatus==1 || procedurePropertyStatus==2){
					jButtonInsertProcedureProperty.setEnabled(false);
					jButtonEditProcedureProperty.setEnabled(false);	
					jButtonDeleteProcedureProperty.setEnabled(false);

					jTextFieldProperty.setEditable(true);
					jTextFieldName.setEditable(true);
					jTextFieldType.setEditable(true);
					jCheckBoxSearch.setEnabled(true);
					jCheckBoxActive.setEnabled(true);
					
					jButtonAcceptProcedureProperty.setEnabled(true);
					jButtonCancelProcedureProperty.setEnabled(true);
				} 				
				else if(procedurePropertyStatus==0){
					jButtonInsertProcedureProperty.setEnabled(true);
					jButtonEditProcedureProperty.setEnabled(false);	
					jButtonDeleteProcedureProperty.setEnabled(false);
					
					jTextFieldProperty.setEditable(false);
					jTextFieldName.setEditable(false);
					jTextFieldType.setEditable(false);
					jCheckBoxSearch.setEnabled(false);
					jCheckBoxActive.setEnabled(false);
					
					jButtonAcceptProcedureProperty.setEnabled(false);
					jButtonCancelProcedureProperty.setEnabled(false);
				}
			} 
			else if(procedureStatus==2){
				jTextFieldId.setEditable(true);
				jTextFieldTableName.setEditable(true);					
				jTextFieldLayerName.setEditable(true);
				jTextFieldMapName.setEditable(true);
				jTextFieldProcedureType.setEditable(true);
									
				jTextFieldFeatureName.setEditable(true);
				jComboBoxFeatureToolbarName.setEnabled(true);
				jTextFieldStyleProperty.setEditable(true);
				jTextFieldAddressProperty.setEditable(true);			
						
				jButtonDeleteProcedure.setEnabled(false);
				jButtonEditProcedure.setEnabled(false);	
					
				jButtonAccept.setEnabled(true);
				jButtonCancel.setEnabled(true);					 
				 
				if(procedurePropertyStatus==1 || procedurePropertyStatus==2){
					jButtonInsertProcedureProperty.setEnabled(false);
					jButtonEditProcedureProperty.setEnabled(false);	
					jButtonDeleteProcedureProperty.setEnabled(false);

					jTextFieldProperty.setEditable(true);
					jTextFieldName.setEditable(true);
					jTextFieldType.setEditable(true);
					jCheckBoxSearch.setEnabled(true);
					jCheckBoxActive.setEnabled(true);
					
					jButtonAcceptProcedureProperty.setEnabled(true);
					jButtonCancelProcedureProperty.setEnabled(true);
				} 				
				else if(procedurePropertyStatus==0){
					jButtonInsertProcedureProperty.setEnabled(true);
					jButtonEditProcedureProperty.setEnabled(false);	
					jButtonDeleteProcedureProperty.setEnabled(false);
					
					jTextFieldProperty.setEditable(false);
					jTextFieldName.setEditable(false);
					jTextFieldType.setEditable(false);
					jCheckBoxSearch.setEnabled(false);
					jCheckBoxActive.setEnabled(false);
					
					jButtonAcceptProcedureProperty.setEnabled(false);
					jButtonCancelProcedureProperty.setEnabled(false);
				}
			}
			else if(procedureStatus==0){
				disableAll();
			}
			
				
			this.invalidate();
			this.validate();
			/*
		} catch (AclNotFoundException e) {
			logger.error(e);
			e.printStackTrace();
			*/
			
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Habilitamos los componentes.
	 * 
	 * @param bEnabled
	 */
//	private void enabledComponents(boolean bEnabled) {
//
//		com.geopista.security.GeopistaAcl aclAdministracion;
//		try {
//			/*
//			aclAdministracion = com.geopista.security.SecurityManager
//					.getPerfil("Administracion");
//			permisoEdicionCompleta = aclAdministracion
//					.checkPermission(new com.geopista.security.GeopistaPermission(
//							com.geopista.security.GeopistaPermission.EDITAR_ADMINITRACION));
//			permisoEdicionParcial = aclAdministracion
//					.checkPermission(new com.geopista.security.GeopistaPermission(
//							"Geopista.Administracion.Edit"));
//			*/
//			boolean permisoEdicionParcial = true;
//			boolean permisoEdicionCompleto = true;
//			
//			jTextFieldBusquedaProcedure.setText("");
//			
//			jButtonAccept.setEnabled(bEnabled);
//			jButtonCancel.setEnabled(bEnabled);
//			
//			if(procedureStatus==1){
//				jTextFieldId.setEditable(bEnabled);
//			}
//			else{
//				jTextFieldId.setEditable(false);
//			}
//			jTextFieldTableName.setEditable(bEnabled);
//			jTextFieldLayerName.setEditable(bEnabled);
//			jTextFieldMapName.setEditable(bEnabled);
//			jTextFieldProcedureType.setEditable(bEnabled);
//						
//			jTextFieldFeatureName.setEditable(bEnabled);
//			jComboBoxFeatureToolbarName.setEnabled(bEnabled);
//			jTextFieldStyleProperty.setEditable(bEnabled);
//			jTextFieldAddressProperty.setEditable(bEnabled);			
//			
//			jButtonDeleteProcedure.setEnabled(!bEnabled);
//			jButtonEditProcedure.setEnabled(!bEnabled);	
//				
//			this.invalidate();
//			this.validate();
//			/*
//		} catch (AclNotFoundException e) {
//			logger.error(e);
//			e.printStackTrace();
//			*/
//			
//		} catch (Exception e) {
//			logger.error(e);
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * Habilitamos los componentes.
	 * 
	 * @param bEnabled
	 */
//	private void enabledProcedurePropertyComponents(boolean bEnabled) {
//
//		com.geopista.security.GeopistaAcl aclAdministracion;
//		try {
//			/*
//			aclAdministracion = com.geopista.security.SecurityManager
//					.getPerfil("Administracion");
//			permisoEdicionCompleta = aclAdministracion
//					.checkPermission(new com.geopista.security.GeopistaPermission(
//							com.geopista.security.GeopistaPermission.EDITAR_ADMINITRACION));
//			permisoEdicionParcial = aclAdministracion
//					.checkPermission(new com.geopista.security.GeopistaPermission(
//							"Geopista.Administracion.Edit"));
//			*/
////			jTextFieldBusquedaProcedure.setText("");
//			
//			jButtonAcceptProcedureProperty.setEnabled(bEnabled);
//			jButtonCancelProcedureProperty.setEnabled(bEnabled);
//			
////			if(modoNuevo){
////				jTextFieldId.setEditable(bEnabled);
////			}
////			else{
////				jTextFieldId.setEditable(false);
////			}
////			jTextFieldTableName.setEditable(bEnabled);
////			jTextFieldLayerName.setEditable(bEnabled);
////			jTextFieldMapName.setEditable(bEnabled);
////			jTextFieldProcedureType.setEditable(bEnabled);
////						
////			jTextFieldFeatureName.setEditable(bEnabled);
////			jComboBoxFeatureToolbarName.setEnabled(bEnabled);
////			jTextFieldStyleProperty.setEditable(bEnabled);
////			jTextFieldAddressProperty.setEditable(bEnabled);
//
//			jTextFieldProperty.setEditable(bEnabled);
//			jTextFieldName.setEditable(bEnabled);
//			jTextFieldType.setEditable(bEnabled);
//			jCheckBoxSearch.setEnabled(bEnabled);
//			jCheckBoxActive.setEnabled(bEnabled);
//			if(procedureStatus==1){
//				jButtonInsertProcedureProperty.setEnabled(true);
//				jButtonEditProcedureProperty.setEnabled(false);	
//				jButtonDeleteProcedureProperty.setEnabled(false);
//			}
//			else{
//				jButtonInsertProcedureProperty.setEnabled(true);
//				jButtonEditProcedureProperty.setEnabled(true);	
//				jButtonDeleteProcedureProperty.setEnabled(true);
//			}
////			jButtonInsertProcedureProperty.setEnabled(!bEnabled);
////			jButtonEditProcedureProperty.setEnabled(!bEnabled);	
////			jButtonDeleteProcedureProperty.setEnabled(!bEnabled);
////			this.invalidate();
////			this.validate();
//			/*
//		} catch (AclNotFoundException e) {
//			logger.error(e);
//			e.printStackTrace();
//			*/
//		} catch (Exception e) {
//			logger.error(e);
//			e.printStackTrace();
//		}
//	}
	
	private void actualizarProceduresModel() {
		actualizarProceduresModel("");
	}

	private void actualizarProceduresModel(String filtro) {
		proceduresModel = new ProceduresTableModel();
		proceduresModel.setModelData(procedures, filtro);
		proceduresSorter = new TableSorted(proceduresModel);
		proceduresSorter.setTableHeader(jTableProcedures.getTableHeader());
		jTableProcedures.setModel(proceduresSorter);
		jTableProcedures.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	proceduresSorter.setSortingStatus(1, 1);    	
    	mostrarProcedure(null, null, null);
	}
	
	private void actualizarProcedurePropertiesModel(HashMap<String, ProcedureProperty> procedurePropertiesMap) {
		procedurePropertiesModel = new ProcedurePropertiesTableModel();
		procedurePropertiesModel.setModelData(procedurePropertiesMap);
		procedurePropertiesSorter = new TableSorted(procedurePropertiesModel);
		procedurePropertiesSorter.setTableHeader(jTableProcedureProperties.getTableHeader());
		jTableProcedureProperties.setModel(procedurePropertiesSorter);
		jTableProcedureProperties.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		procedurePropertiesSorter.setSortingStatus(1, 1);
		CheckBoxRenderer checkBoxRenderer = new CheckBoxRenderer();
		jTableProcedureProperties.getColumnModel().getColumn(3).setCellRenderer(checkBoxRenderer);
		jTableProcedureProperties.getColumnModel().getColumn(4).setCellRenderer(checkBoxRenderer);
		//jTableProcedureProperties.getColumnModel().getColumn(0).setCellRenderer();
		mostrarProcedureProperty(null);
	}

	public void mostrarProcedure(Procedure procedure, ProcedureDefaults procedureDefaults, HashMap<String, ProcedureProperty> procedureProperties) {
		if (procedure != null) {	
			auxProcedure = procedure;						
			jTextFieldId.setText(procedure.getId());
			jTextFieldTableName.setText(procedure.getTableName());
			jTextFieldLayerName.setText(procedure.getLayerName());
			jTextFieldMapName.setText(procedure.getMapName());
			jTextFieldProcedureType.setText(procedure.getProcedureType());
	
			if(procedureDefaults != null){
				auxProcedureDefaults = procedureDefaults;
				jTextFieldFeatureName.setText(procedureDefaults.getFeatureName());
				jTextFieldStyleProperty.setText(procedureDefaults.getStyleProperty());
				jTextFieldAddressProperty.setText(procedureDefaults.getAddressProperty());	
				Iterator<Integer> it = featureToolbarName.keySet().iterator();
				while(it.hasNext()){
					Integer index = it.next();
					if(featureToolbarName.get(index).equals(procedureDefaults.getFeatureToolbarname())){
						jComboBoxFeatureToolbarName.setSelectedIndex(index);
					}
				}			
			}
			else{
				auxProcedureDefaults = new ProcedureDefaults();
				jTextFieldFeatureName.setText("");
				jTextFieldStyleProperty.setText("");
				jTextFieldAddressProperty.setText("");	
				jComboBoxFeatureToolbarName.setSelectedIndex(0);
			}				
			if(procedureProperties != null){
				auxProcedureProperties = procedureProperties;	
				actualizarProcedurePropertiesModel(auxProcedureProperties);
			}
			else{
				auxProcedureProperties = new HashMap<String, ProcedureProperty>();	
				this.procedureProperties = new HashMap<String, ProcedureProperty>();
				jTableProcedureProperties.removeAll();
				actualizarProcedurePropertiesModel(auxProcedureProperties);
			}
		}
		else{
			auxProcedure = procedure;	
			jTextFieldId.setText("");
			jTextFieldTableName.setText("");
			jTextFieldLayerName.setText("");
			jTextFieldMapName.setText("");
			jTextFieldProcedureType.setText("");
			
			auxProcedureDefaults = procedureDefaults;
			jTextFieldFeatureName.setText("");
			jTextFieldStyleProperty.setText("");
			jTextFieldAddressProperty.setText("");
			jComboBoxFeatureToolbarName.setSelectedIndex(0);
			
			auxProcedureProperties = procedureProperties;			
			this.procedureProperties = new HashMap<String, ProcedureProperty>();
			jTableProcedureProperties.removeAll();
			actualizarProcedurePropertiesModel(procedureProperties);			
			jTextFieldProperty.setText("");
			jTextFieldName.setText("");
			jTextFieldType.setText("");
			jCheckBoxSearch.setSelected(false);
			jCheckBoxActive.setSelected(false);
		}
	}
	
	public void mostrarProcedureProperty(ProcedureProperty procedureProperty) {
		if (procedureProperty != null) {
			jTextFieldProperty.setText(procedureProperty.getProperty());
			jTextFieldName.setText(procedureProperty.getName());
			jTextFieldType.setText(procedureProperty.getType());
			jCheckBoxSearch.setSelected(procedureProperty.getSearchactive());
			jCheckBoxActive.setSelected(procedureProperty.getActive());					
		}
		else{		
			auxProcedureProperties = procedureProperties;
		//	auxProcedureProperty = procedureProperties;
			jTableProcedureProperties.removeAll();
			jTextFieldProperty.setText("");
			jTextFieldName.setText("");
			jTextFieldType.setText("");
			jCheckBoxSearch.setSelected(false);
			jCheckBoxActive.setSelected(false);
			
		}
	}

	private void editarProcedure() {
		if (procedureSelected == null) {
			JOptionPane optionPane = new JOptionPane(mensajesProcedure
					.getString("CSigmFrame.jPanelProcedure.mensaje.procedurenoencontrado"),
					JOptionPane.INFORMATION_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "INFO");
			dialog.show();
			return;
		}
		procedureStatus = 2;
		switchStatus();
	}

	private void cancelarActionPerformed() {
		procedureStatus = 0;
		switchStatus();
		jButtonInsertProcedureProperty.setEnabled(false);
		jButtonEditProcedureProperty.setEnabled(false);	
		jButtonDeleteProcedureProperty.setEnabled(false);
		mostrarProcedure(procedureSelected, procedureDefaults, procedureProperties);
	}

	public void aceptarActionPerformed() {
		try {
			guardarCambios();
			String result = null;
			String sMensaje = "";
			try {
				if(auxProcedure!=null && auxProcedure.getId()!=null && !auxProcedure.getId().equals("")){
					SigmClient sigmClient = new SigmClient(Constantes.url + ServletConstants.SIGM_SERVLET);
					if (procedureStatus==1) {
						if(sigmClient.getProcedure(auxProcedure.getId())==null){
							sigmClient.insertProcedure(auxProcedure);
							if(sigmClient.getProcedureDefaults(auxProcedureDefaults.getId())!=null){
								sigmClient.updateProcedureDefaults(auxProcedureDefaults);
							}
							else{
								sigmClient.insertProcedureDefaults(auxProcedureDefaults);
							}
							if(auxProcedureProperties!=null && auxProcedureProperties.size()>0){
								sigmClient.insertProcedureProperties(auxProcedureProperties);
							}
							result = mensajesProcedure.getString("CSigmFrame.jPanelProcedures.mensaje.procedureinsertado");
							sMensaje = mensajesProcedure.getString("CSigmFrame.jPanelProcedures.mensaje.procedureinsertado");
						}
						else{
							result = null;
							sMensaje = mensajesProcedure.getString("CSigmFrame.jPanelProcedures.mensaje.procedureexiste");
						}
					} else {
	//					logger.debug("Actualizando procedure: " + auxProcedure
	//							+ " Grupos: \n" + auxProcedure.printGrupos());
						sigmClient.updateProcedure(auxProcedure);
						if(sigmClient.getProcedureDefaults(auxProcedureDefaults.getId())!=null){
							sigmClient.updateProcedureDefaults(auxProcedureDefaults);
						}
						else{
							sigmClient.insertProcedureDefaults(auxProcedureDefaults);
						}
						if(auxProcedureProperties != null){
							sigmClient.updateProcedureProperties(auxProcedure.getId(), auxProcedureProperties);
						}
//						if(auxProcedureProperties != null && auxProcedureProperties.size()>0){
//							ProcedureProperty pp = auxProcedureProperties.get(0);
//							System.out.println(pp.getProperty());
//							pp.setProperty(pp.getProperty() + "_PRUEBA");
//							sigmClient.updateProcedureProperty(auxProcedureProperties);
//						}
						result = mensajesProcedure.getString("CSigmFrame.jPanelProcedures.mensaje.procedureactualizado");
						sMensaje = mensajesProcedure.getString("CSigmFrame.jPanelProcedures.mensaje.procedureactualizado");
					}
				}else{
					result=null;
					sMensaje = mensajesProcedure.getString("CSigmFrame.jPanelProcedures.mensaje.proceduresinid");
				}
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				logger.error("Exception al grabar en base de datos un procedure: "
								+ sw.toString());
//				result = new CResultadoOperacion(false, e.getMessage());
			}
			if (result!=null) {
				procedureSelected = auxProcedure;
				procedureDefaults = auxProcedureDefaults;
				procedureProperties = auxProcedureProperties;
				procedures.put(procedureSelected.getId(), procedureSelected);
				JOptionPane optionPane = new JOptionPane(sMensaje,
						JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog = optionPane.createDialog(this, "");
				dialog.show();
				procedureStatus = 0;
				switchStatus();
				//modoNuevo = false;
				mostrarProcedure(null, null, null);
				//mostrarProcedure(procedureSelected, procedureDefaults, procedureProperties);
				actualizarProceduresModel();
			} else {
				JOptionPane optionPane = new JOptionPane(sMensaje, JOptionPane.ERROR_MESSAGE);
				JDialog dialog = optionPane.createDialog(this, "ERROR");
				dialog.show();				
			}
		} catch (Exception ex) {
			java.io.StringWriter sw = new java.io.StringWriter();
			java.io.PrintWriter pw = new java.io.PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Excepcion al añadir procedure: " + sw.toString());
			JOptionPane optionPane = new JOptionPane(ex.getMessage(),
					JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "");
			dialog.show();
		}
	}

	private void editarProcedureProperty() {
		if (procedurePropertySelected == null) {
			JOptionPane optionPane = new JOptionPane(mensajesProcedure
					.getString("CSigmFrame.jPanelProcedure.mensaje.procedurepropertynoencontrado"),
					JOptionPane.INFORMATION_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "INFO");
			dialog.show();
			return;
		}
		procedurePropertyStatus = 2;
		switchStatus();
	}
	
	private void cancelarProcedurePropertyActionPerformed() {
		procedurePropertyStatus = 0;
		switchStatus();
		//mostrarProcedure(procedureSelected, procedureDefaults, procedureProperties);
	}

	public void aceptarProcedurePropertyActionPerformed() {
		try {
			guardarCambiosProcedureProperty();
			String result = null;
			String sMensaje = "";
			try {
				if(procedurePropertySelected!=null && procedurePropertySelected.getId()!=null && !procedurePropertySelected.getId().equals("")){
					SigmClient sigmClient = new SigmClient(Constantes.url + ServletConstants.SIGM_SERVLET);
					if (procedurePropertyStatus==1) {					
						if(sigmClient.getProcedure(procedurePropertySelected.getId())==null){
							if(procedurePropertySelected.getProperty()!=null && !procedurePropertySelected.getProperty().equals("")){
							//si es que si
								//mensaje error
							//si es que no
								//añadir a hashmap aux y a tabla
									if(auxProcedureProperties==null){
										auxProcedureProperties = new HashMap<String, ProcedureProperty>();
									}	
									if(auxProcedureProperties.get(procedurePropertySelected.getProperty()) == null){
										auxProcedureProperties.put(procedurePropertySelected.getProperty(), procedurePropertySelected);
										actualizarProcedurePropertiesModel(auxProcedureProperties);
									}
							}
//							sigmClient.insertProcedure(auxProcedure);
//							if(sigmClient.getProcedureDefaults(auxProcedureDefaults.getId())!=null){
//								sigmClient.updateProcedureDefaults(auxProcedureDefaults);
//							}
//							else{
//								sigmClient.insertProcedureDefaults(auxProcedureDefaults);
//							}
//							sigmClient.insertProcedureProperties(auxProcedureProperties);
							result = "";
							sMensaje = "";
//							sMensaje = mensajesProcedure.getString("CSigmFrame.jPanelProcedures.mensaje.procedureinsertado");
						}
						else{
							result = null;
							sMensaje = mensajesProcedure.getString("CSigmFrame.jPanelProcedures.mensaje.procedureexiste");
						}
					} else if(procedurePropertyStatus==2) {
						//ARREGLAR
	//					logger.debug("Actualizando procedure: " + auxProcedure
	//							+ " Grupos: \n" + auxProcedure.printGrupos());
//						sigmClient.updateProcedure(auxProcedure);
//						if(sigmClient.getProcedureDefaults(auxProcedureDefaults.getId())!=null){
//							sigmClient.updateProcedureDefaults(auxProcedureDefaults);
//						}
//						else{
//							sigmClient.insertProcedureDefaults(auxProcedureDefaults);
//						}
//						if(auxProcedureProperties != null && auxProcedureProperties.size()>0){
//							ProcedureProperty pp = auxProcedureProperties.get(0);
//							System.out.println(pp.getProperty());
//							pp.setProperty(pp.getProperty() + "_PRUEBA");
//							sigmClient.updateProcedureProperty(auxProcedureProperties);
//						}
						result = mensajesProcedure.getString("CSigmFrame.jPanelProcedures.mensaje.procedureactualizado");
						sMensaje = mensajesProcedure.getString("CSigmFrame.jPanelProcedures.mensaje.procedureactualizado");
					}
				}else{
					result=null;
					sMensaje = mensajesProcedure.getString("CSigmFrame.jPanelProcedures.mensaje.proceduresinid");
				}
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				logger.error("Exception al grabar en base de datos un procedure: "
								+ sw.toString());
//				result = new CResultadoOperacion(false, e.getMessage());
			}
//			if (result!=null) {
////				procedureSelected = auxProcedure;
////				procedureDefaults = auxProcedureDefaults;
////				procedureProperties = auxProcedureProperties;
////				procedures.put(procedureSelected.getId(), procedureSelected);
//				JOptionPane optionPane = new JOptionPane(sMensaje,
//						JOptionPane.INFORMATION_MESSAGE);
//				JDialog dialog = optionPane.createDialog(this, "");
//				dialog.show();
//				procedureStatus = 0;
//				switchStatus();
////				mostrarProcedure(null, null, null);
//				//mostrarProcedure(procedureSelected, procedureDefaults, procedureProperties);
////				actualizarProceduresModel();
//			} else {
//				JOptionPane optionPane = new JOptionPane(sMensaje, JOptionPane.ERROR_MESSAGE);
//				JDialog dialog = optionPane.createDialog(this, "ERROR");
//				dialog.show();				
//			}
		} catch (Exception ex) {
			java.io.StringWriter sw = new java.io.StringWriter();
			java.io.PrintWriter pw = new java.io.PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Excepcion al añadir procedure: " + sw.toString());
			JOptionPane optionPane = new JOptionPane(ex.getMessage(),
					JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "");
			dialog.show();
		}
	}
	
	private void eliminarProcedureActionListener() {
		if (procedureSelected == null) {
			JOptionPane optionPane = new JOptionPane(mensajesProcedure
					.getString("CSigmFrame.jPanelProcedure.mensaje.procedurenoencontrado"),
					JOptionPane.INFORMATION_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "INFO");
			dialog.show();
			return;
		}
		int n = JOptionPane.showOptionDialog(this, mensajesProcedure
				.getString("CSigmFrame.jPanelProcedure.confirm.eliminarprocedure")
				+ " " + procedureSelected.getId() + "?", "",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				null, null);
		if (n == JOptionPane.NO_OPTION)
			return;
		else
			eliminarProcedure(procedureSelected);
	}
	
	public void eliminarProcedure(Procedure procedureEliminado) {
		String result = null;
		try {
			SigmClient sigmClient = new SigmClient(Constantes.url + ServletConstants.SIGM_SERVLET);
			sigmClient.deleteProcedureProperties(procedureEliminado.getId());
			sigmClient.deleteProcedureDefaults(procedureEliminado.getId());			
			sigmClient.deleteProcedure(procedureEliminado.getId());
			result = mensajesProcedure
					.getString("CSigmFrame.jPanelProcedure.mensaje.eliminarprocedure");
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Exception al eliminar procedure en base de datos: "
					+ sw.toString());
//			result = new CResultadoOperacion(false, e.getMessage());
		}
		if (result!=null) {
			procedures.remove(procedureEliminado.getId());
			procedureSelected = null;
			JOptionPane optionPane = new JOptionPane(result,
					JOptionPane.INFORMATION_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "");
			dialog.show();
			procedureStatus = 0;
			switchStatus();
			mostrarProcedure(procedureSelected, procedureDefaults, procedureProperties);
			actualizarProceduresModel();
		} else {
			JOptionPane optionPane = new JOptionPane(result,
					JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "");
			dialog.show();
		}
	}
	
	public void insertarProcedureProperty() {
		procedureStatus = 1;
		procedurePropertyStatus = 1;
		switchStatus();		
	}

	private void eliminarProcedurePropertyActionListener() {
		if (procedurePropertySelected == null) {
			JOptionPane optionPane = new JOptionPane(mensajesProcedure
					.getString("CSigmFrame.jPanelProcedure.mensaje.procedurepropertynoencontrado"),
					JOptionPane.INFORMATION_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "INFO");
			dialog.show();
			return;
		}
		int n = JOptionPane.showOptionDialog(this, mensajesProcedure
				.getString("CSigmFrame.jPanelProcedure.confirm.eliminarprocedureproperty")
				+ " " + procedurePropertySelected.getProperty() + "?", "",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				null, null);
		if (n == JOptionPane.NO_OPTION)
			return;
		else
			eliminarProcedureProperty(procedurePropertySelected);
	}

	public void eliminarProcedureProperty(ProcedureProperty procedurePropertyEliminado) {
		//procedurePropertyEliminado
		
		//procedureProperties.remove(procedurePropertyEliminado.getProperty());
		auxProcedureProperties.remove(procedurePropertyEliminado.getProperty());
		actualizarProcedurePropertiesModel(procedureProperties);
	}
	
//	public void eliminarProcedureProperty(ProcedureProperty procedurePropertyEliminado) {
//		String result = null;
//		try {
//			SigmClient sigmClient = new SigmClient(Constantes.url + ServletConstants.SIGM_SERVLET);
//			ProcedurePropertyKey procedurePropertyKey = new ProcedurePropertyKey();
//			procedurePropertyKey.setId(procedurePropertyEliminado.getId());
//			procedurePropertyKey.setProperty(procedurePropertyEliminado.getProperty());
//			sigmClient.deleteProcedureProperty(procedurePropertyKey);
//			result = mensajesProcedure
//					.getString("CSigmFrame.jPanelProcedure.mensaje.eliminarprocedureproperty");
//		} catch (Exception e) {
//			StringWriter sw = new StringWriter();
//			PrintWriter pw = new PrintWriter(sw);
//			e.printStackTrace(pw);
//			logger.error("Exception al eliminar el atributo del procedimiento en base de datos: "
//					+ sw.toString());
////			result = new CResultadoOperacion(false, e.getMessage());
//		}
//		if (result!=null) {
//			SigmClient sigmClient = new SigmClient(Constantes.url + ServletConstants.SIGM_SERVLET);
//			try {
//				procedureProperties = sigmClient.getProcedureProperties(procedureSelected.getId());
//			} catch (Exception e) {
//				logger.error(e);
//				e.printStackTrace();
//			}
//			procedurePropertySelected = null;
//			JOptionPane optionPane = new JOptionPane(result,
//					JOptionPane.INFORMATION_MESSAGE);
//			JDialog dialog = optionPane.createDialog(this, "");
//			dialog.show();
//			enabledComponents(false);
//			mostrarProcedureProperty(null);
//			actualizarProcedurePropertiesModel();
//		} else {
//			JOptionPane optionPane = new JOptionPane(result,
//					JOptionPane.ERROR_MESSAGE);
//			JDialog dialog = optionPane.createDialog(this, "");
//			dialog.show();
//		}
//	}

	public void anadirProcedureActionPerformed() {		
		auxProcedure = new Procedure();
		auxProcedureDefaults = new ProcedureDefaults();
		auxProcedureProperties = new HashMap<String, ProcedureProperty>();
		mostrarProcedure(null, null, null);	
		procedureStatus = 1;
		switchStatus();
		jButtonInsertProcedureProperty.setEnabled(true);
	}// fin anadirProcedureActionPerformed

	
	private javax.swing.JButton jButtonInsertProcedure;
	private javax.swing.JButton jButtonEditProcedure;
	private javax.swing.JButton jButtonDeleteProcedure;
	private javax.swing.JButton jButtonProcedureAll;
	private javax.swing.JButton jButtonCancel;
	private javax.swing.JButton jButtonAccept;	
	private javax.swing.JButton jButtonInsertProcedureProperty;
	private javax.swing.JButton jButtonEditProcedureProperty;
	private javax.swing.JButton jButtonDeleteProcedureProperty;
	private javax.swing.JButton jButtonCancelProcedureProperty;
	private javax.swing.JButton jButtonAcceptProcedureProperty;
	
	private javax.swing.JComboBox jComboBoxFeatureToolbarName;
	
	private javax.swing.JLabel jLabelBusquedaProcedure;
	private javax.swing.JLabel jLabelStyleProperty;
	private javax.swing.JLabel jLabelAddressProperty;
	private javax.swing.JLabel jLabelFeatureName;
	private javax.swing.JLabel jLabelId;
	private javax.swing.JLabel jLabelTableName;
	private javax.swing.JLabel jLabelLayerName;
	private javax.swing.JLabel jLabelMapName;
	private javax.swing.JLabel jLabelProcedureType;	
	private javax.swing.JLabel jLabelFeatureToolBarName;
	private javax.swing.JLabel jLabelProperty;
	private javax.swing.JLabel jLabelName;	
	private javax.swing.JLabel jLabelType;
	private javax.swing.JLabel jLabelSearch;	
	private javax.swing.JLabel jLabelActive;
	
	private com.geopista.app.utilidades.TextField jTextFieldBusquedaProcedure;
	private com.geopista.app.utilidades.TextField jTextFieldFeatureName;
	private com.geopista.app.utilidades.TextField jTextFieldStyleProperty;
	private com.geopista.app.utilidades.TextField jTextFieldAddressProperty;
	private com.geopista.app.utilidades.TextField jTextFieldId;
	private com.geopista.app.utilidades.TextField jTextFieldTableName;
	private com.geopista.app.utilidades.TextField jTextFieldLayerName;
	private com.geopista.app.utilidades.TextField jTextFieldMapName;
	private com.geopista.app.utilidades.TextField jTextFieldProcedureType;
	private com.geopista.app.utilidades.TextField jTextFieldProperty;
	private com.geopista.app.utilidades.TextField jTextFieldName;
	private com.geopista.app.utilidades.TextField jTextFieldType;

	private javax.swing.JCheckBox jCheckBoxSearch;
	private javax.swing.JCheckBox jCheckBoxActive;
	
	private javax.swing.JScrollPane proceduresJScrollPane;
	private javax.swing.JScrollPane procedurePropertiesJScrollPane;
	
	private javax.swing.JTable jTableProcedures;
	private javax.swing.JTable jTableProcedureProperties;
	
	private javax.swing.JPanel jPanelProcedures;
	private javax.swing.JPanel jPanelEditProcedure;
	private javax.swing.JPanel jPanelProcedure;
	private javax.swing.JPanel jPanelProcedureDefaults;
	private javax.swing.JPanel jPanelProcedureProperties;
	
	private javax.swing.JTabbedPane sigmJTabbedPane;
	
	
	
}
