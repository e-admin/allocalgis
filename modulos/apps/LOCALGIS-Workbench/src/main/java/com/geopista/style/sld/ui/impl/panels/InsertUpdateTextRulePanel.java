/**
 * InsertUpdateTextRulePanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * InsertUpdateTextRulePanel.java
 *
 * Created on 2 de agosto de 2004, 13:09
 */
package com.geopista.style.sld.ui.impl.panels;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import org.deegree.graphics.sld.Font;
import org.deegree.graphics.sld.LabelPlacement;
import org.deegree.graphics.sld.LinePlacement;
import org.deegree.graphics.sld.ParameterValueType;
import org.deegree.graphics.sld.PointPlacement;
import org.deegree.graphics.sld.Rule;
import org.deegree.graphics.sld.Symbolizer;
import org.deegree.graphics.sld.TextSymbolizer;
import org.deegree.services.wfs.filterencoding.Filter;
import org.deegree.services.wfs.filterencoding.FilterEvaluationException;
import org.deegree_impl.services.wfs.filterencoding.PropertyName;

import com.geopista.app.AppContext;
import com.geopista.style.sld.model.impl.FeatureAttribute;
import com.geopista.style.sld.ui.impl.UIUtils;

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
public class InsertUpdateTextRulePanel extends AbstractPanel implements ActionForward {

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
	private static final String[] _rotationValues = {"0.0","15.0","30.0","45.0","60.0","90.0","120","135.0","150.0","165.0","180.0","225.0","270.0","315.0","330.0","345.0","360.0"};
	private DefaultComboBoxModel _displacementXCmbModel;
	private static final String[] _displacementValues = {"0.0","1.0","2.0","3.0","4.0","5.0","10.0","20.0","30.0","40.0","50.0"};
	private DefaultComboBoxModel _displacementYCmbModel;
	private DefaultComboBoxModel _perpendicularOffsetCmbModel;
	private static final String[] _perpendicularOffsetValues = {"-10.0","-5.0","-2.0","-1.0","0.0","1.0","2.0","5.0","10.0"};

	public void configure(Request request) {
		
		Session session = FrontControllerFactory.getSession();

		HashMap features = (HashMap) session.getAttribute("FeatureAttributes");
		
		_pointOrLineCmbModel = new DefaultComboBoxModel();
		_pointOrLineCmbModel.addElement(POINT_PANEL);
		_pointOrLineCmbModel.addElement(LINE_PANEL);
		pointOrLineCmb.setModel(_pointOrLineCmbModel);

		_labelCmbModel = new DefaultComboBoxModel();
		Iterator featureNameIterator = features.keySet().iterator();
		while (featureNameIterator.hasNext()) {
			String featureName = (String) featureNameIterator.next();
			Iterator attributeIterator = ((List)features.get(featureName)).iterator();			
			while(attributeIterator.hasNext()) {
				_labelCmbModel.addElement((FeatureAttribute)attributeIterator.next());  
			}			
		}
		labelCmb.setModel(_labelCmbModel);

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
		
		_perpendicularOffsetCmbModel = new DefaultComboBoxModel(_perpendicularOffsetValues);	
		perpendicularOffsetCmb.setModel(_perpendicularOffsetCmbModel);	
		_displacementXCmbModel = new DefaultComboBoxModel(_displacementValues);	
		displacementXCmb.setModel(_displacementXCmbModel);	
		_displacementYCmbModel = new DefaultComboBoxModel(_displacementValues);	
		displacementYCmb.setModel(_displacementYCmbModel);	
		_rotationCmbModel = new DefaultComboBoxModel(_rotationValues);	
		rotationCmb.setModel(_rotationCmbModel);	
		
		ruleNameTxt.setText((String)session.getAttribute("RuleName"));
		Rule aRule = (Rule)session.getAttribute("Rule");
		filterChk.setSelected(false);
		if (session.getAttribute("RuleFilter") != null) {
			Filter filter = (Filter)session.getAttribute("RuleFilter");
			filterTxt.setText(UIUtils.createStringFromFilter(filter));
			filterChk.setSelected(true);	
		} 

		Symbolizer[] symbolizers = aRule.getSymbolizers();
		if (symbolizers.length >= 1) {
			if (symbolizers.length > 1) {
				JOptionPane.showMessageDialog(null, "Los simbolizadores adicionales de la regla van a ser descartados\nEste interfaz no puede modificar este estilo\nLa definición va a ser alterada");
			}
			Symbolizer symbolizer = symbolizers[0];
			if (symbolizer instanceof TextSymbolizer) {
				ParameterValueType theLabel = ((TextSymbolizer)symbolizer).getLabel();
				if (theLabel != null) {
					Object[] labelComponents = theLabel.getComponents();
					if ((labelComponents.length == 1) && (labelComponents[0] instanceof PropertyName)) {
						String attribute = ((PropertyName)labelComponents[0]).getValue();
						FeatureAttribute fa = getFeatureAttributeSelected(attribute,labelCmb);
						if (fa != null) {
							_labelCmbModel.setSelectedItem(fa);
						}
					}					
					else {
						// TODO: Information is going to be lost
						JOptionPane.showMessageDialog(null, "El simbolizador contiene multiples componentes para la definición de la etiqueta\nEste interfaz no puede modificar este estilo\nLa definición va a ser alterada");
						for (int i = 0; i<labelComponents.length; i++) {
							if (labelComponents[i] instanceof PropertyName) {
								String attribute = ((PropertyName)labelComponents[i]).getValue();
								FeatureAttribute fa = (FeatureAttribute)features.get(attribute);
								_labelCmbModel.setSelectedItem(fa);
								break;							
							}
						}
					}
				}

				Font theFont = ((TextSymbolizer)symbolizer).getFont(); 
				if (theFont != null) {
					try {
						fontColorBtn.setBackground(theFont.getColor(null));
						textFontFamilyCmb.setSelectedIndex(_fontFamilyModel.getIndexOf(theFont.getFamily(null)));
						switch (theFont.getStyle(null)) {
							case Font.STYLE_NORMAL:
								textFontStyleCmb.setSelectedIndex(_fontStyleModel.getIndexOf("normal"));
								break;
							case Font.STYLE_ITALIC: // Deegree no distingue entre oblique e italic
								textFontStyleCmb.setSelectedIndex(_fontStyleModel.getIndexOf("italic"));
								break;
						}
						switch (theFont.getWeight(null)) {
							case Font.WEIGHT_NORMAL:
								textFontWeightCmb.setSelectedIndex(_fontWeightModel.getIndexOf("normal"));
								break;
							case Font.WEIGHT_BOLD: // Deegree no distingue entre oblique e italic
								textFontWeightCmb.setSelectedIndex(_fontWeightModel.getIndexOf("bold"));
								break;
						}
						textFontSizeCmb.setSelectedIndex(_fontSizeModel.getIndexOf(""+theFont.getSize(null)));
					}
					catch (FilterEvaluationException e){
						JOptionPane.showMessageDialog(null, "Esta regla no puede ser modificada con este interfaz\nError en la evaluación del filtro");
						setDefaultFontValues();
					}
				}
				else {
					setDefaultFontValues();
				}
				
				LabelPlacement labelPlacement = ((TextSymbolizer)symbolizer).getLabelPlacement();
				if (labelPlacement != null) {
					if (labelPlacement.getLinePlacement() != null) {
						configure(labelPlacement.getLinePlacement());						
					}
					else if (labelPlacement.getPointPlacement() != null) {
						configure(labelPlacement.getPointPlacement());
					}
					else {
						//TODO: No known label placement, therefore, default values
						setDefaultLabelPlacementValues();
					}
				}
				else {
					//TODO: No label placement, therefore, default values
					setDefaultLabelPlacementValues();
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "El simbolizador de la regla no es un TextSymbolizer\nEste interfaz no puede modificar este estilo\nLa definición va a ser alterada");
				setDefaultValues();
			} 
		}
		else {
			// Regla sin simbolizador. Valores por defecto
			setDefaultValues();		
		}
	}

	private FeatureAttribute getFeatureAttributeSelected(String attribute,JComboBox combo) {
		
		FeatureAttribute faSelected = null;
		int count = combo.getItemCount();
		boolean found = false;
		int i = 0;
		while ((i<count)&&!found) {
			FeatureAttribute fa = (FeatureAttribute)combo.getItemAt(i);
			if (fa.getName().equals(attribute)) {
				faSelected = fa;
				found = true;
			}
			else {
				i++;
			}
		}
		return faSelected;
	}

	private void setDefaultValues() {
		setDefaultLabelPlacementValues();
		setDefaultFontValues();
	}

	private void setDefaultFontValues() {
		fontColorBtn.setBackground(Color.BLACK);
		if (_fontFamilyModel.getSize() > 0)  
			textFontFamilyCmb.setSelectedIndex(0);
		else 
			textFontFamilyCmb.setSelectedIndex(-1);
		textFontStyleCmb.setSelectedIndex(_fontStyleModel.getIndexOf("normal"));
		textFontWeightCmb.setSelectedIndex(_fontWeightModel.getIndexOf("normal"));
		textFontSizeCmb.setSelectedIndex(0);
	}

	private void setDefaultLabelPlacementValues() {
		CardLayout cl = (CardLayout)pointOrLinePanel.getLayout();
		cl.show(pointOrLinePanel, "POINT_PANEL");
		_pointOrLineCmbModel.setSelectedItem(POINT_PANEL);
		_anchorXCmbModel.setSelectedItem(ANCHOR_POINT_X_LIST[0]);
		_anchorYCmbModel.setSelectedItem(ANCHOR_POINT_Y_LIST[0]);
		displacementXCmb.setSelectedItem("0.0");
		displacementYCmb.setSelectedItem("0.0");
		rotationCmb.setSelectedItem("0.0");
	}
	
	private void configure(LinePlacement linePlacement) {
		
		CardLayout cl = (CardLayout)pointOrLinePanel.getLayout();
		cl.show(pointOrLinePanel, "LINE_PANEL");
		_pointOrLineCmbModel.setSelectedItem(LINE_PANEL);
		try {
			perpendicularOffsetCmb.setSelectedItem(new Double(linePlacement.getPerpendicularOffset(null)).toString());
		}
		catch (FilterEvaluationException e){
			JOptionPane.showMessageDialog(null, "Esta regla no puede ser modificada con este interfaz\nError en la evaluación del filtro");
			perpendicularOffsetCmb.setSelectedItem("");
		}
	}

	private void configure(PointPlacement pointPlacement) {

		CardLayout cl = (CardLayout)pointOrLinePanel.getLayout();
		cl.show(pointOrLinePanel, "POINT_PANEL");
		_pointOrLineCmbModel.setSelectedItem(POINT_PANEL);
		try {
			double[] anchorPoint = pointPlacement.getAnchorPoint(null);
			if (anchorPoint[0] == 0) {
				_anchorXCmbModel.setSelectedItem(ANCHOR_POINT_X_LIST[0]);
			}
			else if (anchorPoint[0] == 0.5){
				_anchorXCmbModel.setSelectedItem(ANCHOR_POINT_X_LIST[1]);
			}
			else if (anchorPoint[0] == 1){
				_anchorXCmbModel.setSelectedItem(ANCHOR_POINT_X_LIST[2]);
			}
			else {
				JOptionPane.showMessageDialog(null, "La información adicional del ANCHORPOINT.X va a ser descartada\nError en la evaluación del filtro");
				_anchorXCmbModel.setSelectedItem(ANCHOR_POINT_X_LIST[0]);
			}
			if (anchorPoint[1] == 0) {
				_anchorYCmbModel.setSelectedItem(ANCHOR_POINT_Y_LIST[0]);
			}
			else if (anchorPoint[1] == 0.5){
				_anchorYCmbModel.setSelectedItem(ANCHOR_POINT_Y_LIST[1]);
			}
			else if (anchorPoint[1] == 1){
				_anchorYCmbModel.setSelectedItem(ANCHOR_POINT_Y_LIST[2]);
			}
			else {
				JOptionPane.showMessageDialog(null, "La información adicional del ANCHORPOINT.Y va a ser descartada\nError en la evaluación del filtro");
				_anchorYCmbModel.setSelectedItem(ANCHOR_POINT_Y_LIST[0]);
			}
			double[] displacement = pointPlacement.getDisplacement(null);
			displacementXCmb.setSelectedItem(new Double(displacement[0]).toString());
			displacementYCmb.setSelectedItem(new Double(displacement[1]).toString());
			rotationCmb.setSelectedItem(new Double(pointPlacement.getRotation(null)).toString());
		}
		catch (FilterEvaluationException e){
			JOptionPane.showMessageDialog(null, "Esta regla no puede ser modificada con este interfaz\nError en la evaluación del filtro");
			setDefaultLabelPlacementValues();
		}
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
			return aplicacion.getI18nString("TextRule");		
	}
    
    /** Creates new form InsertUpdateTextRulePanel */
    public InsertUpdateTextRulePanel() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        rulePanel = new javax.swing.JPanel();
        ruleNameLbl = new javax.swing.JLabel();
        ruleNameTxt = new javax.swing.JTextField();
        stylePanel = new javax.swing.JPanel();
        labelLbl = new javax.swing.JLabel();
        labelCmb = new javax.swing.JComboBox();
        fontColorLbl = new javax.swing.JLabel();
        fontColorBtn = new javax.swing.JButton();
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
        filterPanel = new javax.swing.JPanel();
        filterChk = new javax.swing.JCheckBox();
        filterBtn = new javax.swing.JButton();
        filterTxt = new javax.swing.JTextField();
        buttonPanel = new javax.swing.JPanel();
        okBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        setPreferredSize(new java.awt.Dimension(400, 500));
        rulePanel.setLayout(new java.awt.GridBagLayout());

        ruleNameLbl.setText("Nombre: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        rulePanel.add(ruleNameLbl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        rulePanel.add(ruleNameTxt, gridBagConstraints);

        stylePanel.setLayout(new java.awt.GridBagLayout());

        stylePanel.setBorder(new javax.swing.border.TitledBorder("Estilo: "));
        labelLbl.setText("Etiqueta: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        stylePanel.add(labelLbl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        stylePanel.add(labelCmb, gridBagConstraints);

        fontColorLbl.setText("Color: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        stylePanel.add(fontColorLbl, gridBagConstraints);

        fontColorBtn.setMinimumSize(new java.awt.Dimension(32, 19));
        fontColorBtn.setPreferredSize(new java.awt.Dimension(32, 19));
        fontColorBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fontColorBtnActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        stylePanel.add(fontColorBtn, gridBagConstraints);

        textFontPanel.setLayout(new java.awt.GridBagLayout());

        textFontPanel.setBorder(new javax.swing.border.TitledBorder("Fuente: "));
        textFontFamilyLbl.setText("Familia: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        textFontPanel.add(textFontFamilyLbl, gridBagConstraints);

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
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        rulePanel.add(stylePanel, gridBagConstraints);

        filterPanel.setLayout(new java.awt.GridBagLayout());

        filterPanel.setBorder(new javax.swing.border.TitledBorder("Filtro: "));
        filterChk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterChkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        filterPanel.add(filterChk, gridBagConstraints);

        filterBtn.setText("Filtro");
        filterBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterBtnActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        filterPanel.add(filterBtn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        filterPanel.add(filterTxt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        rulePanel.add(filterPanel, gridBagConstraints);

        add(rulePanel, java.awt.BorderLayout.CENTER);

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

    private void filterBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterBtnActionPerformed

		if (filterChk.isSelected()) {

			Request theRequest = FrontControllerFactory.createRequest();
			theRequest.setAttribute("PageInvocator","InsertUpdateTextRule");
			FrontController fc =  FrontControllerFactory.getFrontController();
			Action theAction = fc.getAction("UpdateRuleFilter"); 
			ActionForward theActionForward = theAction.doExecute(theRequest);
			_container.forward(theActionForward, theRequest);			

		}

    }//GEN-LAST:event_filterBtnActionPerformed

    private void filterChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterChkActionPerformed
		if (filterChk.isSelected()) {
			Request theRequest = FrontControllerFactory.createRequest();
			theRequest.setAttribute("PageInvocator","InsertUpdateTextRule");
			FrontController fc =  FrontControllerFactory.getFrontController();
			Action theAction = fc.getAction("InsertRuleFilter"); 
			ActionForward theActionForward = theAction.doExecute(theRequest);
			_container.forward(theActionForward, theRequest);			
		}
		else {
			Request theRequest = FrontControllerFactory.createRequest();
			theRequest.setAttribute("PageInvocator","InsertUpdateTextRule");
			FrontController fc =  FrontControllerFactory.getFrontController();
			Action theAction = fc.getAction("DeleteRuleFilter"); 
			ActionForward theActionForward = theAction.doExecute(theRequest);
			_container.forward(theActionForward, theRequest);			

		}

    }//GEN-LAST:event_filterChkActionPerformed

	private void fontColorBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fontColorBtnActionPerformed
		Color newColor = JColorChooser.showDialog(
							 this,
							 "Color de linea",
							 fontColorBtn.getBackground());        
		if (newColor != null) {
			fontColorBtn.setBackground(newColor);
		}
	}//GEN-LAST:event_fontColorBtnActionPerformed

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
			if (_pointOrLineCmbModel.getSelectedItem().equals(POINT_PANEL)) {
				createPoint();
			}
			else {
				createLine();
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
		if (!valuesAreCorrect) {
			JOptionPane.showMessageDialog(null, "Los siguientes valores son incorrectos:\n" + errorMessage.toString());
		}
		return valuesAreCorrect;
	}

	private void createPoint() {
		
		Request theRequest = FrontControllerFactory.createRequest();
		
		//Esto no es muy operativo aqui. La operacion al final la hemos vuelto a hacer el SLDStyleImpl.java en writeSLDFile
		String nombre=ruleNameTxt.getText();
		try{
			nombre=nombre.replaceAll("<", "&lt;");	
			nombre=nombre.replaceAll(">", "&gt;");	
		}
		catch (Exception e){
			
		}
		theRequest.setAttribute("RuleName", nombre);
		HashMap style = new HashMap();
		style.put("SymbolizerType", "TextPoint");
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
		style.put("ColorFont", new Integer(fontColorBtn.getBackground().getRGB()));
		style.put("AttributeName", ((FeatureAttribute)_labelCmbModel.getSelectedItem()).getName());
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
		theRequest.setAttribute("Style", style);
		FrontController fc =  FrontControllerFactory.getFrontController();
		Action theAction = fc.getAction("CreateCustomRule");
		ActionForward theActionForward = theAction.doExecute(theRequest);
		_container.forward(theActionForward, theRequest);
	}

	private void createLine() {
		Request theRequest = FrontControllerFactory.createRequest();
		
		String nombre=ruleNameTxt.getText();
		try{
			nombre=nombre.replaceAll("<", "&lt;");	
			nombre=nombre.replaceAll(">", "&gt;");	
		}
		catch (Exception e){
			
		}
		theRequest.setAttribute("RuleName", nombre);
		HashMap style = new HashMap();
		style.put("SymbolizerType", "TextLine");
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
		style.put("ColorFont", new Integer(fontColorBtn.getBackground().getRGB()));
		style.put("AttributeName", ((FeatureAttribute)_labelCmbModel.getSelectedItem()).getName());
		if (!((String)perpendicularOffsetCmb.getSelectedItem()).equals(""))
			style.put("PerpendicularOffset", new Double((String)perpendicularOffsetCmb.getSelectedItem()));
		theRequest.setAttribute("Style", style);
		FrontController fc =  FrontControllerFactory.getFrontController();
		Action theAction = fc.getAction("CreateCustomRule");
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


	private DefaultComboBoxModel _operatorCmbModel;
	private HashMap _operators;
	private DefaultComboBoxModel _propertyCmbModel;
	private DefaultComboBoxModel _labelCmbModel;
	private DefaultComboBoxModel _pointOrLineCmbModel;
	private DefaultComboBoxModel _fontFamilyModel;
	private DefaultComboBoxModel _fontStyleModel;
	private DefaultComboBoxModel _fontWeightModel;
	private DefaultComboBoxModel _fontSizeModel;
	private DefaultComboBoxModel _anchorXCmbModel;
	private DefaultComboBoxModel _anchorYCmbModel;
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    private javax.swing.JButton filterBtn;
    private javax.swing.JCheckBox filterChk;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JTextField filterTxt;
    private javax.swing.JButton fontColorBtn;
    private javax.swing.JLabel fontColorLbl;
    private javax.swing.JComboBox labelCmb;
    private javax.swing.JLabel labelLbl;
    private javax.swing.JPanel linePanel;
    private javax.swing.JButton okBtn;
    private javax.swing.JComboBox perpendicularOffsetCmb;
    private javax.swing.JLabel perpendicularOffsetLbl;
    private javax.swing.JComboBox pointOrLineCmb;
    private javax.swing.JLabel pointOrLineLbl;
    private javax.swing.JPanel pointOrLinePanel;
    private javax.swing.JPanel pointPanel;
    private javax.swing.JComboBox rotationCmb;
    private javax.swing.JLabel rotationLbl;
    private javax.swing.JLabel ruleNameLbl;
    private javax.swing.JTextField ruleNameTxt;
    private javax.swing.JPanel rulePanel;
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
    // End of variables declaration//GEN-END:variables
    
}
