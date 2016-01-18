/*
 * InsertUpdateThematicTextRulePanel.java
 *
 * Created on 2 de agosto de 2004, 18:45
 */
package com.geopista.style.sld.ui.impl.panels;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import com.geopista.app.AppContext;
import com.geopista.feature.Domain;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.TreeDomain;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.IGeopistaLayer;
import com.geopista.style.sld.classifier.Classifier;
import com.geopista.style.sld.classifier.ClassifierManager;
import com.geopista.style.sld.model.impl.FeatureAttribute;
import com.geopista.style.sld.ui.impl.ColorEditor;
import com.geopista.style.sld.ui.impl.ColorRenderer;
import com.geopista.style.sld.ui.impl.ComboEditor;
import com.geopista.style.sld.ui.impl.ComboRender;
import com.geopista.style.sld.ui.impl.JComboEditorRenderTableDomain;
import com.geopista.style.sld.ui.impl.ThematicTableModel;
import com.geopista.style.sld.ui.impl.UIUtils;
import com.vividsolutions.jump.workbench.model.Layer;

import es.enxenio.util.controller.Action;
import es.enxenio.util.controller.ActionForward;
import es.enxenio.util.controller.FrontController;
import es.enxenio.util.controller.FrontControllerFactory;
import es.enxenio.util.controller.Request;
import es.enxenio.util.controller.Session;
import es.enxenio.util.ui.impl.AbstractPanel;

/**
 *
 * @author  Enxenio S.L.
 */
public class InsertUpdateThematicTextRulePanel extends AbstractPanel implements ActionForward {

    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private static final String POINT_PANEL = "En un punto";
	private static final String LINE_PANEL = "En una línea";

	private static final String[] FONT_SIZE_LIST = {
		"4", "6", "8", "9", "10", "11", "12", "14", "16", "18", "20", 
		"22", "24", "26", "28", "36", "48", "72",
	};

	private static final String[] ANCHOR_POINT_X_LIST = {"Izquierda", "Centro", "Derecha"};

	private static final String[] ANCHOR_POINT_Y_LIST = {"Arriba", "Centro", "Abajo"};

	private DefaultComboBoxModel _rotationCmbModel;
	private static final String[] _rotationValues = {"0.0","45.0","90.0","135.0","180.0","225.0","270.0","315.0","360.0"};
	private DefaultComboBoxModel _displacementXCmbModel;
	private static final String[] _displacementValues = {"0.0","1.0","2.0","3.0","4.0","5.0"};
	private DefaultComboBoxModel _displacementYCmbModel;
	private DefaultComboBoxModel _perpendicularOffsetCmbModel;
	private static final String[] _perpendicularOffsetValues = {"-2.0","-1.0","0.0","1.0","2.0"};

	private GeopistaLayer layer;
	
	public void configure(Request request) {
		
		Session session = FrontControllerFactory.getSession();

		_propertyCmbModel = new DefaultComboBoxModel();
		//TODO: Esto no está terminado
		HashMap features = (HashMap) session.getAttribute("FeatureAttributes");
		Iterator featureNameIterator = features.keySet().iterator();
		while (featureNameIterator.hasNext()) {
			String featureName = (String) featureNameIterator.next();
			Iterator attributeIterator = ((List)features.get(featureName)).iterator();			
			while(attributeIterator.hasNext()) {
				_propertyCmbModel.addElement((FeatureAttribute)attributeIterator.next());  
			}			
		}
		propertyNameCmb.setModel(_propertyCmbModel);
		_pointOrLineCmbModel = new DefaultComboBoxModel();
		_pointOrLineCmbModel.addElement(POINT_PANEL);
		_pointOrLineCmbModel.addElement(LINE_PANEL);
		pointOrLineCmb.setModel(_pointOrLineCmbModel);

		_labelCmbModel = new DefaultComboBoxModel();
		featureNameIterator = features.keySet().iterator();
		while (featureNameIterator.hasNext()) {
			String featureName = (String) featureNameIterator.next();
			Iterator attributeIterator = ((List)features.get(featureName)).iterator();			
			while(attributeIterator.hasNext()) {
				FeatureAttribute featureAttribute = (FeatureAttribute)attributeIterator.next(); 
				_labelCmbModel.addElement(featureAttribute);  
			}			
		}
		
		this.layer = (GeopistaLayer) session.getAttribute("Layer");
		propertyNameCmb.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (layer.getFeatureCollectionWrapper().getFeatureSchema() instanceof GeopistaSchema) 
				{
					setCellEditors(layer, true);
				}
			}
		});
		
		labelCmb.setModel(_labelCmbModel);
		_displacementXCmbModel = new DefaultComboBoxModel(_displacementValues);	
		displacementXCmb.setModel(_displacementXCmbModel);	
		_displacementYCmbModel = new DefaultComboBoxModel(_displacementValues);	
		displacementYCmb.setModel(_displacementYCmbModel);
		_rotationCmbModel = new DefaultComboBoxModel(_rotationValues);	
		rotationCmb.setModel(_rotationCmbModel);		
		_perpendicularOffsetCmbModel = new DefaultComboBoxModel(_perpendicularOffsetValues);	
		perpendicularOffsetCmb.setModel(_perpendicularOffsetCmbModel);	

		_fontFamilyModel = new DefaultComboBoxModel();
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment ();
		String[] fontFamilyNames = env.getAvailableFontFamilyNames();
		for (int i=0; i< fontFamilyNames.length; i++) {
			_fontFamilyModel.addElement(fontFamilyNames[i]);
		}
		textFontFamilyCmb.setModel(_fontFamilyModel);

		_fontStyleModel = new DefaultComboBoxModel();
		_fontStyleModel.addElement("normal");
		_fontStyleModel.addElement("italic");
		textFontStyleCmb.setModel(_fontStyleModel);

		_fontWeightModel = new DefaultComboBoxModel();
		_fontWeightModel.addElement("normal");
		_fontWeightModel.addElement("bold");
		textFontWeightCmb.setModel(_fontWeightModel);

		_fontSizeModel = new DefaultComboBoxModel(FONT_SIZE_LIST);
		textFontSizeCmb.setModel(_fontSizeModel);

		_anchorXCmbModel = new DefaultComboBoxModel(ANCHOR_POINT_X_LIST);
		anchorXCmb.setModel(_anchorXCmbModel);

		_anchorYCmbModel = new DefaultComboBoxModel(ANCHOR_POINT_Y_LIST);
		anchorYCmb.setModel(_anchorYCmbModel);
		
		_tableModel = new ThematicTableModel();
		valueListTable.setModel(_tableModel);
		valueListTable.setDefaultRenderer(Color.class, new ColorRenderer(true));
		valueListTable.setDefaultEditor(Color.class, new ColorEditor());

		ListSelectionModel rowSM = valueListTable.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener() {
			
			public void valueChanged(ListSelectionEvent e) {
				valueListTableSelectionChanged(e);
			}
		});
		HashMap aDefaultStyle = defaultStyle(Color.WHITE);
		fillStylePanel(aDefaultStyle);
	}

	public boolean windowClosing() {
		Request theRequest = FrontControllerFactory.createRequest();
		FrontController fc =  FrontControllerFactory.getFrontController();
		Action theAction = fc.getAction("GetBack"); 
		ActionForward theActionForward = theAction.doExecute(theRequest);
		_container.forward(theActionForward, theRequest);
		return false;
	}

	public String getTitle() {
			return aplicacion.getI18nString("ThematicTextRule");		
	}
    
    /** Creates new form InsertUpdateThematicTextRulePanel */
    public InsertUpdateThematicTextRulePanel() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        thematicPanel = new javax.swing.JPanel();
        rangeThematicLbl = new javax.swing.JLabel();
        rangeThematicChk = new javax.swing.JCheckBox();
        propertyNameLbl = new javax.swing.JLabel();
        propertyNameCmb = new javax.swing.JComboBox();
        valuePanel = new javax.swing.JPanel();
        valueListTableScroll = new javax.swing.JScrollPane();
        valueListTable = new javax.swing.JTable();
        valueButtonsPanel = new javax.swing.JPanel();
        newValueBtn = new javax.swing.JButton();
        removeValueBtn = new javax.swing.JButton();
        allValuesBtn = new javax.swing.JButton();
        rampColorBtn = new javax.swing.JButton();
        stylePanel = new javax.swing.JPanel();
        labelLbl = new javax.swing.JLabel();
        labelCmb = new javax.swing.JComboBox();
        textFontPanel = new javax.swing.JPanel();
        textFontFamilyLbl = new javax.swing.JLabel();
        textFontFamilyCmb = new javax.swing.JComboBox();
        textFontStyleLbl = new javax.swing.JLabel();
        textFontStyleCmb = new javax.swing.JComboBox();
        textFontWeightLbl = new javax.swing.JLabel();
        textFontWeightCmb = new javax.swing.JComboBox();
        textFontSIzeLbl = new javax.swing.JLabel();
        textFontSizeCmb = new javax.swing.JComboBox();
        pointOrLineLbl = new javax.swing.JLabel();
        pointOrLineCmb = new javax.swing.JComboBox();
        pointOrLinePanel = new javax.swing.JPanel();
        pointPanel = new javax.swing.JPanel();
        anchorXLbl = new javax.swing.JLabel();
        anchorXCmb = new javax.swing.JComboBox();
        anchorYLbl = new javax.swing.JLabel();
        anchorYCmb = new javax.swing.JComboBox();
        displacementXLbl = new javax.swing.JLabel();
        displacementYLbl = new javax.swing.JLabel();
        rotationLbl = new javax.swing.JLabel();
        rotationCmb = new javax.swing.JComboBox();
        displacementXCmb = new javax.swing.JComboBox();
        displacementYCmb = new javax.swing.JComboBox();
        linePanel = new javax.swing.JPanel();
        perpendicularOffsetLbl = new javax.swing.JLabel();
        perpendicularOffsetCmb = new javax.swing.JComboBox();
        buttonPanel = new javax.swing.JPanel();
        okBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        setPreferredSize(new java.awt.Dimension(400, 600));
        thematicPanel.setLayout(new java.awt.GridBagLayout());

        rangeThematicLbl.setText("Por rangos: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        thematicPanel.add(rangeThematicLbl, gridBagConstraints);

        rangeThematicChk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rangeThematicChkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        thematicPanel.add(rangeThematicChk, gridBagConstraints);

        propertyNameLbl.setText("Propiedad: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        thematicPanel.add(propertyNameLbl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        thematicPanel.add(propertyNameCmb, gridBagConstraints);

        valuePanel.setLayout(new java.awt.BorderLayout());

        valuePanel.setBorder(new javax.swing.border.TitledBorder("Valores: "));
        valueListTableScroll.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        valueListTableScroll.setViewportView(valueListTable);

        valuePanel.add(valueListTableScroll, java.awt.BorderLayout.CENTER);

        valueButtonsPanel.setLayout(new java.awt.GridBagLayout());

        newValueBtn.setText("Nuevo");
        newValueBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newValueBtnActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        valueButtonsPanel.add(newValueBtn, gridBagConstraints);

        removeValueBtn.setText("Borrar");
        removeValueBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeValueBtnActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        valueButtonsPanel.add(removeValueBtn, gridBagConstraints);

        allValuesBtn.setText("Todos");
        allValuesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allValuesBtnActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        valueButtonsPanel.add(allValuesBtn, gridBagConstraints);

        rampColorBtn.setText("Rampa");
        rampColorBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rampColorBtnActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        valueButtonsPanel.add(rampColorBtn, gridBagConstraints);

        valuePanel.add(valueButtonsPanel, java.awt.BorderLayout.EAST);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        thematicPanel.add(valuePanel, gridBagConstraints);

        stylePanel.setLayout(new java.awt.GridBagLayout());

        stylePanel.setBorder(new javax.swing.border.TitledBorder("Estilo: "));
        labelLbl.setText("Etiqueta: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        stylePanel.add(labelLbl, gridBagConstraints);

        labelCmb.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                labelCmbFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        stylePanel.add(labelCmb, gridBagConstraints);

        textFontPanel.setLayout(new java.awt.GridBagLayout());

        textFontPanel.setBorder(new javax.swing.border.TitledBorder("Fuente: "));
        textFontFamilyLbl.setText("Familia: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        textFontPanel.add(textFontFamilyLbl, gridBagConstraints);

        textFontFamilyCmb.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textFontFamilyCmbFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        textFontPanel.add(textFontFamilyCmb, gridBagConstraints);

        textFontStyleLbl.setText("Estilo: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        textFontPanel.add(textFontStyleLbl, gridBagConstraints);

        textFontStyleCmb.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textFontStyleCmbFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        textFontPanel.add(textFontStyleCmb, gridBagConstraints);

        textFontWeightLbl.setText("Grosor: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        textFontPanel.add(textFontWeightLbl, gridBagConstraints);

        textFontWeightCmb.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textFontWeightCmbFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        textFontPanel.add(textFontWeightCmb, gridBagConstraints);

        textFontSIzeLbl.setText("Tama\u00f1o: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        textFontPanel.add(textFontSIzeLbl, gridBagConstraints);

        textFontSizeCmb.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textFontSizeCmbFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        textFontPanel.add(textFontSizeCmb, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        stylePanel.add(textFontPanel, gridBagConstraints);

        pointOrLineLbl.setText("Tipo: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        stylePanel.add(pointOrLineLbl, gridBagConstraints);

        pointOrLineCmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pointOrLineCmbActionPerformed(evt);
            }
        });
        pointOrLineCmb.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                pointOrLineCmbFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        stylePanel.add(pointOrLineCmb, gridBagConstraints);

        pointOrLinePanel.setLayout(new java.awt.CardLayout());

        pointPanel.setLayout(new java.awt.GridBagLayout());

        anchorXLbl.setText("Alineaci\u00f3n horizontal: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pointPanel.add(anchorXLbl, gridBagConstraints);

        anchorXCmb.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                anchorXCmbFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        pointPanel.add(anchorXCmb, gridBagConstraints);

        anchorYLbl.setText("Alineaci\u00f3n vertical: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pointPanel.add(anchorYLbl, gridBagConstraints);

        anchorYCmb.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                anchorYCmbFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        pointPanel.add(anchorYCmb, gridBagConstraints);

        displacementXLbl.setText("Desplazamiento horizontal: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pointPanel.add(displacementXLbl, gridBagConstraints);

        displacementYLbl.setText("Desplazamiento vertical: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pointPanel.add(displacementYLbl, gridBagConstraints);

        rotationLbl.setText("Rotaci\u00f3n: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pointPanel.add(rotationLbl, gridBagConstraints);

        rotationCmb.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 150);
        pointPanel.add(rotationCmb, gridBagConstraints);

        displacementXCmb.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 150);
        pointPanel.add(displacementXCmb, gridBagConstraints);

        displacementYCmb.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 150);
        pointPanel.add(displacementYCmb, gridBagConstraints);

        pointOrLinePanel.add(pointPanel, "POINT_PANEL");

        linePanel.setLayout(new java.awt.GridBagLayout());

        perpendicularOffsetLbl.setText("Desplazamiento: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        linePanel.add(perpendicularOffsetLbl, gridBagConstraints);

        perpendicularOffsetCmb.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 150);
        linePanel.add(perpendicularOffsetCmb, gridBagConstraints);

        pointOrLinePanel.add(linePanel, "LINE_PANEL");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        stylePanel.add(pointOrLinePanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        thematicPanel.add(stylePanel, gridBagConstraints);

        add(thematicPanel, java.awt.BorderLayout.CENTER);

        okBtn.setText("Aceptar");
        okBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okBtnActionPerformed(evt);
            }
        });

        buttonPanel.add(okBtn);

        cancelBtn.setText("Cancelar");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        buttonPanel.add(cancelBtn);

        add(buttonPanel, java.awt.BorderLayout.SOUTH);

    }//GEN-END:initComponents

    private void pointOrLineCmbFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pointOrLineCmbFocusLost
		ThematicTableModel tm = (ThematicTableModel)valueListTable.getModel();
		for (int i=0; i<_currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);  
			if (_pointOrLineCmbModel.getSelectedItem().equals(LINE_PANEL)) {
				aStyle.put("SymbolizerType", "TextLine");
			}
			else {
				aStyle.put("SymbolizerType", "TextPoint");
			}
		}
    }//GEN-LAST:event_pointOrLineCmbFocusLost

    private void anchorYCmbFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_anchorYCmbFocusLost
		ThematicTableModel tm = (ThematicTableModel)valueListTable.getModel();
		for (int i=0; i<_currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);  
			if (_anchorYCmbModel.getSelectedItem().equals(ANCHOR_POINT_Y_LIST[0])) {
				aStyle.put("AnchorY", new Double(0));
			}
			else if (_anchorYCmbModel.getSelectedItem().equals(ANCHOR_POINT_Y_LIST[1])){
				aStyle.put("AnchorY", new Double(0.5));
			}
			else {
				aStyle.put("AnchorY", new Double(1));
			}
		}
    }//GEN-LAST:event_anchorYCmbFocusLost

    private void anchorXCmbFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_anchorXCmbFocusLost
		ThematicTableModel tm = (ThematicTableModel)valueListTable.getModel();
		for (int i=0; i<_currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);  
			if (_anchorXCmbModel.getSelectedItem().equals(ANCHOR_POINT_X_LIST[0])) {
				aStyle.put("AnchorX", new Double(0));
			}
			else if (_anchorXCmbModel.getSelectedItem().equals(ANCHOR_POINT_X_LIST[1])){
				aStyle.put("AnchorX", new Double(0.5));
			}
			else {
				aStyle.put("AnchorX", new Double(1));
			}
		}
    }//GEN-LAST:event_anchorXCmbFocusLost

    private void textFontSizeCmbFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textFontSizeCmbFocusLost
		ThematicTableModel tm = (ThematicTableModel)valueListTable.getModel();
		for (int i=0; i<_currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);  
			aStyle.put("FontSize", new Double((String)_fontSizeModel.getSelectedItem()));
		}
    }//GEN-LAST:event_textFontSizeCmbFocusLost

    private void textFontWeightCmbFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textFontWeightCmbFocusLost
		ThematicTableModel tm = (ThematicTableModel)valueListTable.getModel();
		for (int i=0; i<_currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);  
			if (_fontWeightModel.getSelectedItem().equals("normal")) {
				aStyle.put("Bold", new Boolean(false));	
			}
			else {
				aStyle.put("Bold", new Boolean(true));
			}
		}
    }//GEN-LAST:event_textFontWeightCmbFocusLost

    private void textFontStyleCmbFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textFontStyleCmbFocusLost
		ThematicTableModel tm = (ThematicTableModel)valueListTable.getModel();
		for (int i=0; i<_currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);  
			if (_fontStyleModel.getSelectedItem().equals("normal")) {
				aStyle.put("Italic", new Boolean(false));	
			}
			else {
				aStyle.put("Italic", new Boolean(true)); 
			}
		}
    }//GEN-LAST:event_textFontStyleCmbFocusLost

    private void textFontFamilyCmbFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textFontFamilyCmbFocusLost
		ThematicTableModel tm = (ThematicTableModel)valueListTable.getModel();
		for (int i=0; i<_currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);  
			aStyle.put("FontFamily", _fontFamilyModel.getSelectedItem());
		}
    }//GEN-LAST:event_textFontFamilyCmbFocusLost

    private void labelCmbFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_labelCmbFocusLost
		ThematicTableModel tm = (ThematicTableModel)valueListTable.getModel();
		for (int i=0; i<_currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);  
			aStyle.put("AttributeName", ((FeatureAttribute)_labelCmbModel.getSelectedItem()).getName());
		}
    }//GEN-LAST:event_labelCmbFocusLost

	private void rangeThematicChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rangeThematicChkActionPerformed
		if (rangeThematicChk.isSelected()) {
			_tableModel.setRangeModel(true);
		}
		else {
			_tableModel.setRangeModel(false);
		}
	}//GEN-LAST:event_rangeThematicChkActionPerformed

	private void createUniqueThematicTableModel(SortedSet values) {
		int result = JOptionPane.showConfirmDialog(
			this,
			"Esta opción eliminará todas las clases, y creará " + values.size() + " nuevas clases.\n ¿Desea continuar?",
			"Confirmación",					
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {
			_tableModel = new ThematicTableModel();
			_tableModel.setRangeModel(false);
			Iterator valueIterator = values.iterator();		
			while (valueIterator.hasNext()) {
				Color fillColor = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
				Object minValue = valueIterator.next(); 
				_tableModel.addRow(new Object[]{fillColor, minValue.toString(), minValue.toString(), createStyleFromStylePanel(fillColor)});
			}
			valueListTable.setModel(_tableModel);
			if (layer.getFeatureCollectionWrapper().getFeatureSchema() instanceof GeopistaSchema) 
			{
				setCellEditors(layer, false);
			}
		}
	}

	private void createRangeThematicTableModel(SortedSet values) {

		String inputValue = JOptionPane.showInputDialog("Introduzca el número de clases que se van a crear");
		if (inputValue != null) {
			int numberOfClasses;
			try {
				numberOfClasses = new Integer(inputValue).intValue();
				if (numberOfClasses < 1) {
					JOptionPane.showMessageDialog(null, "El número de clases debe ser mayor o igual que 1");
				}
				else {
					Object[] classificationMethods = ClassifierManager.getMethodNames(values.first());
					String method = (String)JOptionPane.showInputDialog(this, "Introduzca el método de clasificación:\n", "Método de clasificación", JOptionPane.PLAIN_MESSAGE, null, classificationMethods, classificationMethods[0]);
					if (method != null) {
						int result = JOptionPane.showConfirmDialog(
							this,
							"Se eliminarán todas las clases, y se crearán " + numberOfClasses + " nuevas clases utilizando el método de " + method + ".\n ¿Desea continuar?",
							"Confirmación",					
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
						if (result == JOptionPane.YES_OPTION) {
							_tableModel = new ThematicTableModel();
							_tableModel.setRangeModel(true);
							Classifier classifier = ClassifierManager.getClassifier(method);
							if (classifier != null) {
								Set classifiedValues = classifier.classify(values, numberOfClasses);
								Iterator valueIterator = classifiedValues.iterator();		
								while (valueIterator.hasNext()) {
									Color fillColor = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
									Object minValue = valueIterator.next();
									_tableModel.addRow(new Object[]{fillColor, minValue.toString(), minValue.toString(), createStyleFromStylePanel(fillColor)});			
								}
								for (int i = 0; i < (_tableModel.getRowCount()-1); i++ ) {
									_tableModel.setValueAt(_tableModel.getValueAt(i+1, 1),i, 2);
								}
								_tableModel.setValueAt(values.last().toString(), _tableModel.getRowCount()-1, 2);
								valueListTable.setModel(_tableModel);
							}
						}
					}
				}
			}
			catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "El valor introducido no es válido");			
			}
		}
	}

    private void newValueBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newValueBtnActionPerformed
		Color fillColor = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
		_tableModel.addRow(new Object[]{fillColor, "", "", createStyleFromStylePanel(fillColor)});
		this.valueListTable.addNotify();
    }//GEN-LAST:event_newValueBtnActionPerformed

    private void removeValueBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeValueBtnActionPerformed
		int[] selectedRows;
		
		selectedRows = valueListTable.getSelectedRows();
		for (int i=selectedRows.length-1; i>=0; i--) {
			_tableModel.removeRow(selectedRows[i]);
		}
    }//GEN-LAST:event_removeValueBtnActionPerformed

    private void allValuesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allValuesBtnActionPerformed
		Session session = FrontControllerFactory.getSession();
		Layer layer = (Layer) session.getAttribute("Layer");
		
		SortedSet values = UIUtils.getNonNullAttributeValues(layer, ((FeatureAttribute)_propertyCmbModel.getSelectedItem()).getName());
		if (rangeThematicChk.isSelected()) {
			createRangeThematicTableModel(values);
		}
		else {
			createUniqueThematicTableModel(values);
		}
    }//GEN-LAST:event_allValuesBtnActionPerformed

    private void rampColorBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rampColorBtnActionPerformed
		int rowCount = _tableModel.getRowCount();
		if (rowCount > 0) {
			Color firstColor = (Color)_tableModel.getValueAt(0, 0);
			Color lastColor = (Color)_tableModel.getValueAt(rowCount-1, 0);
			int firstRed = firstColor.getRed();
			int firstGreen = firstColor.getGreen();
			int firstBlue = firstColor.getBlue();
			int lastRed = lastColor.getRed();
			int lastGreen = lastColor.getGreen();
			int lastBlue = lastColor.getBlue();
			int stepRed = (lastRed - firstRed) / (rowCount - 1);
			int stepGreen = (lastGreen - firstGreen) / (rowCount - 1);
			int stepBlue = (lastBlue - firstBlue) / (rowCount - 1);
	
			for (int i=0; i < rowCount; i++) {
				_tableModel.setValueAt(new Color(firstRed + i * stepRed, firstGreen + i * stepGreen, firstBlue + i * stepBlue), i, 0);
			}
		}
    }//GEN-LAST:event_rampColorBtnActionPerformed

    private void pointOrLineCmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pointOrLineCmbActionPerformed
		if (_pointOrLineCmbModel.getSelectedItem().equals(LINE_PANEL)) {
			CardLayout cl = (CardLayout)pointOrLinePanel.getLayout();
			cl.show(pointOrLinePanel, "LINE_PANEL");
		}
		else {
			CardLayout cl = (CardLayout)pointOrLinePanel.getLayout();
			cl.show(pointOrLinePanel, "POINT_PANEL");
		}
    }//GEN-LAST:event_pointOrLineCmbActionPerformed

    private void okBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okBtnActionPerformed
		if (checkValues()) {
			if (_tableModel.isCorrect()) {
				if (rangeThematicChk.isSelected()) {
					createThematicRange();
				}
				else {
					createThematicUnique();
				}
			}
			else {
				JOptionPane.showMessageDialog(this, "Las clases del temático no son correctas");		
			}
		}
    }//GEN-LAST:event_okBtnActionPerformed

	private boolean checkValues() {
		
		boolean valuesAreCorrect = true;
		StringBuffer errorMessage = new StringBuffer();
		Session session = FrontControllerFactory.getSession();
		if (!((String)displacementXCmb.getSelectedItem()).equals("")) {
			try {
				double displacementX = Double.parseDouble((String)displacementXCmb.getSelectedItem());	
			}
			catch (NumberFormatException e) {
				errorMessage.append("El desplazamiento en el eje X del texto debe ser numérico\n");
				valuesAreCorrect = false;
			}
		}
		if (!((String)displacementYCmb.getSelectedItem()).equals("")) {
			try {
				double displacementY = Double.parseDouble((String)displacementYCmb.getSelectedItem());
			}
			catch (NumberFormatException e) {
				errorMessage.append("El desplazamiento en el eje Y del texto debe ser numérico\n");
				valuesAreCorrect = false;
			}
		}
		if (!((String)rotationCmb.getSelectedItem()).equals("")) {
			try {
				Double.parseDouble((String)rotationCmb.getSelectedItem());
			}
			catch (NumberFormatException e) {
				errorMessage.append("La rotación de un gráfico debe ser numérico\n");
				valuesAreCorrect = false;
			}
		}
		if (!((String)perpendicularOffsetCmb.getSelectedItem()).equals("")) {
			try {
				Double.parseDouble((String)perpendicularOffsetCmb.getSelectedItem());
			}
			catch (NumberFormatException e) {
				errorMessage.append("La distancia del texto respecto a una línea debe ser numérico\n");
				valuesAreCorrect = false;
			}
		}
		FeatureAttribute attribute = (FeatureAttribute)_propertyCmbModel.getSelectedItem();
		String attributeType = attribute.getType();
		if (rangeThematicChk.isSelected()) {
			for (int i=0; i < _tableModel.getRowCount(); i++) {
				String attributeValue1 = _tableModel.getValueAt(i,1).toString();
				String attributeValue2 = _tableModel.getValueAt(i,2).toString();
				if (attributeValue1.equals("")) {
					errorMessage.append("Hay que darle un valor al rango mínimo de la regla "+i+"\n");
					valuesAreCorrect = false;					
				}
				else if (!checkAttribute(attributeValue1,attributeType)) {
					errorMessage.append("El valor dado al rango mínimo no corresponde con su tipo en la condición "+i+"\n");
					valuesAreCorrect = false;
				}
				if (attributeValue2.equals("")) {
					errorMessage.append("Hay que darle un valor al rango máximo de la regla "+i+"\n");
					valuesAreCorrect = false;					
				}				
				else if (!checkAttribute(attributeValue2,attributeType)) {
					errorMessage.append("El valor dado al rango máximo no corresponde con su tipo en la condición "+i+"\n");
					valuesAreCorrect = false;
				}				
				try {
					double minValue = Double.parseDouble(attributeValue1);
					double maxValue = Double.parseDouble(attributeValue2);
					if (minValue > maxValue) {
						errorMessage.append("El rango no está bien definido en la regla "+i+", debido a que el limite inferior es mayor que el superior\n");
						valuesAreCorrect = false;						
					}
				} catch (NumberFormatException e) {					
				}
				HashMap style = _tableModel.getStyle(i);
				if (style.get("AttributeName") == null) {
					errorMessage.append("No has elegido un atributo para mostrar en la etiqueta en la regla "+i+"\n");
					valuesAreCorrect = false;						
				}
			}
		} 
		else {
			for (int i=0; i < _tableModel.getRowCount(); i++) {
				String attributeValue1 = _tableModel.getValueAt(i,1).toString();
				if (!checkAttribute(attributeValue1,attributeType)) {
					errorMessage.append("El valor dado al atributo no corresponde con su tipo en la condición "+i+"\n");
					valuesAreCorrect = false;
				}
			}
		}		
		if (!valuesAreCorrect) {
			JOptionPane.showMessageDialog(null, "Los siguientes valores son incorrectos:\n" + errorMessage.toString());
		}
		return valuesAreCorrect;
	}

	private boolean checkAttribute(String attributeValue,String attributeType) {
		boolean correctValue = true;
		if ((attributeType.equals("DOUBLE"))||(attributeType.equals("INTEGER"))) {
			try {
				Double.parseDouble(attributeValue);
			} catch (NumberFormatException e) {
				correctValue = false;
			}
		}
		return correctValue;
	}

	private void createThematicRange() {
		Request theRequest = FrontControllerFactory.createRequest();
		List rangeList = new ArrayList();
		List styleList = new ArrayList();
		List aRange;
		theRequest.setAttribute("PropertyName", ((FeatureAttribute)_propertyCmbModel.getSelectedItem()).getName());
		for (int i=0; i < _tableModel.getRowCount(); i++) {
			HashMap style = _tableModel.getStyle(i);
			Color currentColor = (Color)_tableModel.getValueAt(i,0);
			if (currentColor != null) {
				int fillColor = ((Integer)style.get("ColorFont")).intValue();
				if (currentColor.getRGB() != fillColor) {
					style.put("ColorFont",new Integer(currentColor.getRGB()));
				}
			}
			styleList.add(style);
			aRange = new ArrayList();
			aRange.add(_tableModel.getValueAt(i,1).toString());
			aRange.add(_tableModel.getValueAt(i,2).toString());
			rangeList.add(aRange);
		}
		String mainRuleName = JOptionPane.showInputDialog("Introduzca el nombre para la regla creada");
		theRequest.setAttribute("mainRuleName", mainRuleName);
		theRequest.setAttribute("RangeList", rangeList);
		theRequest.setAttribute("StyleList", styleList);
		FrontController fc =  FrontControllerFactory.getFrontController();
		Action theAction = fc.getAction("CreateThematicRangeRule"); 
		ActionForward theActionForward = theAction.doExecute(theRequest);
		_container.forward(theActionForward, theRequest);
	}
    
	private void createThematicUnique() {
		Request theRequest = FrontControllerFactory.createRequest();
		List valueList = new ArrayList();
		List styleList = new ArrayList();
		theRequest.setAttribute("PropertyName", ((FeatureAttribute)_propertyCmbModel.getSelectedItem()).getName());
		for (int i=0; i < _tableModel.getRowCount(); i++) {
			HashMap style = _tableModel.getStyle(i);
			Color currentColor = (Color)_tableModel.getValueAt(i,0);
			if (currentColor != null) {
				int fillColor = ((Integer)style.get("ColorFont")).intValue();
				if (currentColor.getRGB() != fillColor) {
					style.put("ColorFont",new Integer(currentColor.getRGB()));
				}
			}
			styleList.add(style);
			valueList.add(_tableModel.getValueAt(i,1).toString());
		}
		String mainRuleName = JOptionPane.showInputDialog("Introduzca el nombre para la regla creada");
		theRequest.setAttribute("mainRuleName", mainRuleName);
		theRequest.setAttribute("ValueList", valueList);
		theRequest.setAttribute("StyleList", styleList);
		FrontController fc =  FrontControllerFactory.getFrontController();
		Action theAction = fc.getAction("CreateThematicRule"); 
		ActionForward theActionForward = theAction.doExecute(theRequest);
		_container.forward(theActionForward, theRequest);
	}

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
		Request theRequest = FrontControllerFactory.createRequest();
		FrontController fc =  FrontControllerFactory.getFrontController();
		Action theAction = fc.getAction("GetBack"); 
		ActionForward theActionForward = theAction.doExecute(theRequest);
		_container.forward(theActionForward, theRequest);
    }//GEN-LAST:event_cancelBtnActionPerformed

	private void valueListTableSelectionChanged(ListSelectionEvent e) {
		//Ignore extra messages.
		if (e.getValueIsAdjusting()) return;

		ListSelectionModel lsm = (ListSelectionModel)e.getSource();
		if (lsm.isSelectionEmpty()) {
			//no rows are selected
			_currentSelection = valueListTable.getSelectedRows();
		} else {
			int selectedRow = lsm.getMinSelectionIndex();
			ThematicTableModel tm = (ThematicTableModel)valueListTable.getModel();
			Color fillColor = (Color)tm.getValueAt(selectedRow,0);
			HashMap style = (HashMap)tm.getStyle(selectedRow);
			_currentSelection = valueListTable.getSelectedRows();
			fillStylePanel(style);
			//selectedRow is selected
		}
	}

	private HashMap defaultStyle(Color fillColor) {
		HashMap style = new HashMap();
		
		style.put("SymbolizerType", "TextPoint");				
		style.put("ColorFont", new Integer(fillColor.getRGB()));
		style.put("FontFamily", "Serif");
		style.put("Italic", new Boolean(false));	
		style.put("Bold", new Boolean(false));	
		style.put("FontSize", new Double(10));
		style.put("AttributeName", "");
		style.put("AnchorX", new Double(0.5));
		style.put("AnchorY", new Double(0.5));
		style.put("DisplacementX", new Double(0));
		style.put("DisplacementY", new Double(0));
		style.put("Rotation", new Double(0));
		style.put("PerpendicularOffset", new Double(0));
		return style;
	}

	private void fillStylePanel(HashMap style) {
		_labelCmbModel.setSelectedItem((String)style.get("AttributeName"));
		textFontFamilyCmb.setSelectedIndex(_fontFamilyModel.getIndexOf((String)style.get("FontFamily")));		
		if (((Boolean)style.get("Italic")).booleanValue()) {
			textFontStyleCmb.setSelectedIndex(_fontStyleModel.getIndexOf("italic"));
		}
		else {
			textFontStyleCmb.setSelectedIndex(_fontStyleModel.getIndexOf("normal"));
		}
		if (((Boolean)style.get("Bold")).booleanValue()) {
			textFontWeightCmb.setSelectedIndex(_fontWeightModel.getIndexOf("bold"));
		}
		else {
			textFontWeightCmb.setSelectedIndex(_fontWeightModel.getIndexOf("normal"));
		}
		textFontSizeCmb.setSelectedIndex(_fontSizeModel.getIndexOf(""+(int)((Double)style.get("FontSize")).doubleValue()));
		_pointOrLineCmbModel.setSelectedItem(POINT_PANEL);
		double anchorX = ((Double)style.get("AnchorX")).doubleValue();
		if (anchorX == 0) {
			_anchorXCmbModel.setSelectedItem(ANCHOR_POINT_X_LIST[0]);
		}
		else if (anchorX == 0.5){
			_anchorXCmbModel.setSelectedItem(ANCHOR_POINT_X_LIST[1]);
		}
		else if (anchorX == 1){
			_anchorXCmbModel.setSelectedItem(ANCHOR_POINT_X_LIST[2]);
		}
		else {
			_anchorXCmbModel.setSelectedItem(ANCHOR_POINT_X_LIST[0]);
		}
		double anchorY = ((Double)style.get("AnchorY")).doubleValue();
		if (anchorY == 0) {
			_anchorYCmbModel.setSelectedItem(ANCHOR_POINT_Y_LIST[0]);
		}
		else if (anchorY == 0.5){
			_anchorYCmbModel.setSelectedItem(ANCHOR_POINT_Y_LIST[1]);
		}
		else if (anchorY == 1){
			_anchorYCmbModel.setSelectedItem(ANCHOR_POINT_Y_LIST[2]);
		}
		else {
			_anchorYCmbModel.setSelectedItem(ANCHOR_POINT_Y_LIST[0]);
		}
		displacementXCmb.setSelectedItem(((Double)style.get("DisplacementX")).toString());
		displacementYCmb.setSelectedItem(((Double)style.get("DisplacementY")).toString());
		rotationCmb.setSelectedItem(((Double)style.get("Rotation")).toString());
		perpendicularOffsetCmb.setSelectedItem(((Double)style.get("PerpendicularOffset")).toString());
		if (((String)style.get("SymbolizerType")).equals("TextLine")) {
			CardLayout cl = (CardLayout)pointOrLinePanel.getLayout();
			cl.show(pointOrLinePanel, "LINE_PANEL");
		}
		else {
			CardLayout cl = (CardLayout)pointOrLinePanel.getLayout();
			cl.show(pointOrLinePanel, "POINT_PANEL");
		}
	}

	private HashMap createStyleFromStylePanel(Color fillColor) {
		HashMap style = new HashMap();
		if (_pointOrLineCmbModel.getSelectedItem().equals(POINT_PANEL)) {
			style.put("SymbolizerType", "TextPoint");				
		}
		else {
			style.put("SymbolizerType", "TextLine");
		}
		style.put("ColorFont", new Integer(fillColor.getRGB()));
		style.put("FontFamily", _fontFamilyModel.getSelectedItem());
		if (_fontStyleModel.getSelectedItem().equals("normal")) {
			style.put("Italic", new Boolean(false));	
		}
		else {
			style.put("Italic", new Boolean(true)); 
		}
		if (_fontWeightModel.getSelectedItem().equals("normal")) {
			style.put("Bold", new Boolean(false));	
		}
		else {
			style.put("Bold", new Boolean(true));
		}
		style.put("FontSize", new Double((String)_fontSizeModel.getSelectedItem()));
		if ((_labelCmbModel.getSelectedItem() != null)) {
		if (!_labelCmbModel.getSelectedItem().equals("")) {
			style.put("AttributeName", ((FeatureAttribute)_labelCmbModel.getSelectedItem()).getName());
			}
		else{
			style.put("AttributeName", "");
			}
		}
		if (_anchorXCmbModel.getSelectedItem().equals(ANCHOR_POINT_X_LIST[0])) {
			style.put("AnchorX", new Double(0));
		}
		else if (_anchorXCmbModel.getSelectedItem().equals(ANCHOR_POINT_X_LIST[1])){
			style.put("AnchorX", new Double(0.5));
		}
		else {
			style.put("AnchorX", new Double(1));
		}
		if (_anchorYCmbModel.getSelectedItem().equals(ANCHOR_POINT_Y_LIST[0])) {
			style.put("AnchorY", new Double(0));
		}
		else if (_anchorYCmbModel.getSelectedItem().equals(ANCHOR_POINT_Y_LIST[1])){
			style.put("AnchorY", new Double(0.5));
		}
		else {
			style.put("AnchorY", new Double(1));
		}
		if (!((String)displacementXCmb.getSelectedItem()).equals(""))
			style.put("DisplacementX", new Double((String)displacementXCmb.getSelectedItem()));
		if (!((String)displacementYCmb.getSelectedItem()).equals(""))
			style.put("DisplacementY", new Double((String)displacementYCmb.getSelectedItem()));
		if (!((String)rotationCmb.getSelectedItem()).equals(""))
			style.put("Rotation", new Double((String)rotationCmb.getSelectedItem()));
		if (!((String)perpendicularOffsetCmb.getSelectedItem()).equals(""))
			style.put("PerpendicularOffset", new Double((String)perpendicularOffsetCmb.getSelectedItem()));
		return style;
	}

	private FeatureAttribute getFeatureAttributeSelected(String attribute,javax.swing.JComboBox combo) {
		
		FeatureAttribute faSelected = null;
		int count = combo.getItemCount();
		boolean found = false;
		int i = 0;
		while ((i<count)&&!found) {
			Object object = combo.getItemAt(i);
			if (object instanceof FeatureAttribute) {
				FeatureAttribute fa = (FeatureAttribute)object;
				if (fa.getName().equals(attribute)) {
					faSelected = fa;
					found = true;
				}
				else {
					i++;
				}
			}
			else {
				i++;
			}
		}
		return faSelected;
	}

	private DefaultComboBoxModel _propertyCmbModel;
	private DefaultComboBoxModel _patternCmbModel;
	private HashMap _patterns;
	private ThematicTableModel _tableModel; 
	private DefaultComboBoxModel _wellKnownNameCmbModel;
	private DefaultComboBoxModel _labelCmbModel;
	private DefaultComboBoxModel _pointOrLineCmbModel;
	private DefaultComboBoxModel _fontFamilyModel;
	private DefaultComboBoxModel _fontStyleModel;
	private DefaultComboBoxModel _fontWeightModel;
	private DefaultComboBoxModel _fontSizeModel;
	private DefaultComboBoxModel _anchorXCmbModel;
	private DefaultComboBoxModel _anchorYCmbModel;
	private int[] _currentSelection = {};
     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton allValuesBtn;
    private javax.swing.JComboBox anchorXCmb;
    private javax.swing.JLabel anchorXLbl;
    private javax.swing.JComboBox anchorYCmb;
    private javax.swing.JLabel anchorYLbl;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JComboBox displacementXCmb;
    private javax.swing.JLabel displacementXLbl;
    private javax.swing.JComboBox displacementYCmb;
    private javax.swing.JLabel displacementYLbl;
    private javax.swing.JComboBox labelCmb;
    private javax.swing.JLabel labelLbl;
    private javax.swing.JPanel linePanel;
    private javax.swing.JButton newValueBtn;
    private javax.swing.JButton okBtn;
    private javax.swing.JComboBox perpendicularOffsetCmb;
    private javax.swing.JLabel perpendicularOffsetLbl;
    private javax.swing.JComboBox pointOrLineCmb;
    private javax.swing.JLabel pointOrLineLbl;
    private javax.swing.JPanel pointOrLinePanel;
    private javax.swing.JPanel pointPanel;
    private javax.swing.JComboBox propertyNameCmb;
    private javax.swing.JLabel propertyNameLbl;
    private javax.swing.JButton rampColorBtn;
    private javax.swing.JCheckBox rangeThematicChk;
    private javax.swing.JLabel rangeThematicLbl;
    private javax.swing.JButton removeValueBtn;
    private javax.swing.JComboBox rotationCmb;
    private javax.swing.JLabel rotationLbl;
    private javax.swing.JPanel stylePanel;
    private javax.swing.JComboBox textFontFamilyCmb;
    private javax.swing.JLabel textFontFamilyLbl;
    private javax.swing.JPanel textFontPanel;
    private javax.swing.JLabel textFontSIzeLbl;
    private javax.swing.JComboBox textFontSizeCmb;
    private javax.swing.JComboBox textFontStyleCmb;
    private javax.swing.JLabel textFontStyleLbl;
    private javax.swing.JComboBox textFontWeightCmb;
    private javax.swing.JLabel textFontWeightLbl;
    private javax.swing.JPanel thematicPanel;
    private javax.swing.JPanel valueButtonsPanel;
    private javax.swing.JTable valueListTable;
    private javax.swing.JScrollPane valueListTableScroll;
    private javax.swing.JPanel valuePanel;
    // End of variables declaration//GEN-END:variables
    
	// miguel

	/**
	 * @param layer
	 *            Set specific CellEditors in a table
	 */
	private void setCellEditors(GeopistaLayer layer, boolean bDeleteTable) {
		GeopistaSchema MySchema;
		Domain domain;
		String attName;
		int domainType;
		
		MySchema = (GeopistaSchema) layer.getFeatureCollectionWrapper().getFeatureSchema();
		MySchema.setGeopistalayer((IGeopistaLayer) layer);

		JTable table = this.valueListTable;
		attName = (String) (this.propertyNameCmb.getSelectedItem().toString());
		
		if (bDeleteTable)
		{
			_tableModel = new ThematicTableModel();
			if (rangeThematicChk.isSelected())
			{
				_tableModel.setRangeModel(true);
			}
			else
			{
				_tableModel.setRangeModel(false);
			}
			table.setModel(_tableModel);
		}
		
		if (MySchema instanceof GeopistaSchema && MySchema.getColumnByAttribute(attName).getDomain() != null) {
			domain = MySchema.getColumnByAttribute(attName).getDomain();
			domainType = domain.getType();

			if (domainType == Domain.CODEDENTRY || domainType == Domain.CODEBOOK || domainType == Domain.TREE) {
				// Crear Combo
				try {
					// // Constructs the ComboBox
					JComboEditorRenderTableDomain EditorCombo = new JComboEditorRenderTableDomain(
							attName, (TreeDomain) domain, MySchema);

					ComboEditor elEditor = new ComboEditor(EditorCombo
							.getCombo(), EditorCombo.getValues());
					ComboRender elRender = new ComboRender(EditorCombo
							.getCombo(), EditorCombo.getValues());

					table.getColumnModel().getColumn(1).setCellEditor(elEditor);
					table.getColumnModel().getColumn(1).setCellRenderer(elRender);
					table.repaint();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {

				//Poner Model como default
				table.getColumnModel().getColumn(1).setCellEditor(null);
				table.getColumnModel().getColumn(1).setCellRenderer(null);

				table.getColumnModel().getColumn(1).setCellEditor(
						new DefaultCellEditor(new JTextField()));
				table.getColumnModel().getColumn(1).setCellRenderer(
						new DefaultTableCellRenderer());
			}
		}
	}

	public IGeopistaLayer getLayer() {
		return this.layer;
	}
}
