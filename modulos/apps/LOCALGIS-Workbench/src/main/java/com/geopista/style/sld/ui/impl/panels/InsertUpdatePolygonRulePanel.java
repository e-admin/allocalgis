/**
 * InsertUpdatePolygonRulePanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * InsertUpdatePolygonRulePanel.java
 *
 * Created on 29 de julio de 2004, 12:19
 */
package com.geopista.style.sld.ui.impl.panels;

import java.awt.Color;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.deegree.graphics.sld.ExternalGraphic;
import org.deegree.graphics.sld.Fill;
import org.deegree.graphics.sld.GraphicFill;
import org.deegree.graphics.sld.PolygonSymbolizer;
import org.deegree.graphics.sld.Rule;
import org.deegree.graphics.sld.Stroke;
import org.deegree.graphics.sld.Symbolizer;
import org.deegree.services.wfs.filterencoding.Expression;
import org.deegree.services.wfs.filterencoding.Filter;
import org.deegree.services.wfs.filterencoding.FilterEvaluationException;
import org.deegree_impl.graphics.sld.Stroke_Impl;
import org.deegree_impl.services.wfs.filterencoding.ArithmeticExpression;
import org.deegree_impl.services.wfs.filterencoding.ExpressionDefines;
import org.deegree_impl.services.wfs.filterencoding.Literal;
import org.deegree_impl.services.wfs.filterencoding.PropertyName;

import com.geopista.app.AppContext;
import com.geopista.style.sld.model.impl.FeatureAttribute;
import com.geopista.style.sld.ui.impl.PaintRenderer;
import com.geopista.style.sld.ui.impl.Pattern;
import com.geopista.style.sld.ui.impl.PatternRenderer;
import com.geopista.style.sld.ui.impl.Texture;
import com.geopista.style.sld.ui.impl.UIUtils;
import com.geopista.ui.images.IconLoader;

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
public class InsertUpdatePolygonRulePanel extends AbstractPanel implements ActionForward {

    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private DefaultComboBoxModel _strokeWidthCmbModel;
	private static final String[] _widthValues = {"1.0","2.0","3.0","4.0","5.0"};
	private DefaultComboBoxModel _textureSizeCmbModel;
	private static final String[] _percentageValues = {"25","50","75","100"};
	private DefaultComboBoxModel _arithmeticOperatorsModel;
	private static final String[] _operatorValues = {"","suma","resta","multiplicación","división"};
	private DefaultComboBoxModel _propertyNameCmbModel;

	public void configure(Request request) {
		
		Session session = FrontControllerFactory.getSession();

		buttlineLbl.setIcon(IconLoader.icon("/com/geopista/ui/images/linecapbutt.JPG"));
		roundlineLbl.setIcon(IconLoader.icon("/com/geopista/ui/images/linecapround2.JPG"));
		squarelineLbl.setIcon(IconLoader.icon("/com/geopista/ui/images/linecapsquare2.JPG"));
		mitreLineJoinLbl.setIcon(IconLoader.icon("/com/geopista/ui/images/linejoinmitre2.JPG"));
		roundLineJoinLbl.setIcon(IconLoader.icon("/com/geopista/ui/images/linejoinround2.JPG"));
		bevelineJoinLbl.setIcon(IconLoader.icon("/com/geopista/ui/images/linejoinbevel2.JPG"));

		Pattern aPattern;
		_patternCmbModel = new DefaultComboBoxModel();
		_patterns = new HashMap();
		Iterator patternIterator = Pattern.createPatterns().iterator(); 
		while (patternIterator.hasNext()) {
			aPattern = (Pattern)patternIterator.next();
			_patterns.put(aPattern.toString(), aPattern);
			_patternCmbModel.addElement(aPattern);
		}

		_strokeWidthCmbModel = new DefaultComboBoxModel(_widthValues);	
		strokeWidthCmb.setModel(_strokeWidthCmbModel);	

		dashArrayCmb.setModel(_patternCmbModel);
		dashArrayCmb.setRenderer(new PatternRenderer());
		//Create the label table
		Dictionary labelTable = new Hashtable();
		labelTable.put( new Integer( 0 ), new JLabel("0.0") );
		labelTable.put( new Integer( 10 ), new JLabel("0.1") );
		labelTable.put( new Integer( 20 ), new JLabel("0.2") );
		labelTable.put( new Integer( 30 ), new JLabel("0.3") );
		labelTable.put( new Integer( 40 ), new JLabel("0.4") );
		labelTable.put( new Integer( 50 ), new JLabel("0.5") );
		labelTable.put( new Integer( 60 ), new JLabel("0.6") );
		labelTable.put( new Integer( 70 ), new JLabel("0.7") );
		labelTable.put( new Integer( 80 ), new JLabel("0.8") );
		labelTable.put( new Integer( 90 ), new JLabel("0.9") );
		labelTable.put( new Integer( 100 ), new JLabel("1.0") );
		strokeOpacitySld.setLabelTable(labelTable);
		fillOpacitySld.setLabelTable(labelTable);
			
		_fillTextureCmbModel = new DefaultComboBoxModel(Texture.createTextures());
		fillTextureCmb.setModel(_fillTextureCmbModel);
		fillTextureCmb.setRenderer(new PaintRenderer());
		_textureSizeCmbModel = new DefaultComboBoxModel(_percentageValues);
		textureSizeCmb.setModel(_textureSizeCmbModel);
		textureSizeCmb.setSelectedItem("100");
		
		ruleNameTxt.setText((String)session.getAttribute("RuleName"));
		Rule aRule = (Rule)session.getAttribute("Rule");
		filterChk.setSelected(false);
		if (session.getAttribute("RuleFilter") != null) {
			Filter filter = (Filter)session.getAttribute("RuleFilter");
			filterTxt.setText(UIUtils.createStringFromFilter(filter));
			filterChk.setSelected(true);	
		} 
		_arithmeticOperatorsModel = new DefaultComboBoxModel(_operatorValues);	
		arithmeticExprCmb.setModel(_arithmeticOperatorsModel);	
		_propertyNameCmbModel = new DefaultComboBoxModel();
		HashMap featureAttributesMap = (HashMap)session.getAttribute("FeatureAttributes");
		Set featureNameSet = featureAttributesMap.keySet();
		Iterator featureNameIterator = featureNameSet.iterator();
		String featureName = (String)featureNameIterator.next();
		List attributeList = (List)featureAttributesMap.get(featureName);
		Iterator attributeIterator = attributeList.iterator();
		_propertyNameCmbModel.addElement(""); 	
		while (attributeIterator.hasNext()) {
			FeatureAttribute attribute = (FeatureAttribute)attributeIterator.next();
			if ((attribute.getType().equals("DOUBLE"))||(attribute.getType().equals("INTEGER"))) {
				_propertyNameCmbModel.addElement(attribute);
			} 	
		}
		attributesCmb.setModel(_propertyNameCmbModel);

		Symbolizer[] symbolizers = aRule.getSymbolizers();
		if (symbolizers.length >= 1) {
			if (symbolizers.length > 1) {
				JOptionPane.showMessageDialog(null, "Los simbolizadores adicionales de la regla van a ser descartados\nEste interfaz no puede modificar este estilo\nLa definición va a ser alterada");
			}
			Symbolizer symbolizer = symbolizers[0];
			if (symbolizer instanceof PolygonSymbolizer) {
				Stroke stroke = ((PolygonSymbolizer)symbolizer).getStroke();
				if (stroke != null) {
					try {
						strokeColorBtn.setBackground(stroke.getStroke(null));
						Stroke_Impl strokeImpl = (Stroke_Impl)stroke;
						Expression widthAsExpression = strokeImpl.getWidthAsExpression(null);
						if (widthAsExpression != null) { 
							configureWidth(widthAsExpression,attributeList);
						}
						else {
							strokeWidthCmb.setSelectedItem(new Double(stroke.getWidth(null)).toString());	
						}
						strokeOpacitySld.setValue((int)(stroke.getOpacity(null) * 100));
						switch (stroke.getLineCap(null)) {
							case Stroke.LC_BUTT:
								buttLineCapRBtn.setSelected(true);
								break;
							case Stroke.LC_ROUND:
								roundLineCapRBtn.setSelected(true);
								break;
							case Stroke.LC_SQUARE:
								squareLineCapRBtn.setSelected(true);
								break;
						}
						switch (stroke.getLineJoin(null)) {
							case Stroke.LJ_BEVEL:
								bevelLineJoinRBtn.setSelected(true);
								break;
							case Stroke.LJ_MITRE:
								mitreLineJoinRBtn.setSelected(true);
								break;
							case Stroke.LJ_ROUND:
								roundLineJoinRBtn.setSelected(true);
								break;
						}
						float[] dashArray = stroke.getDashArray(null); 
						if (dashArray != null) {
							aPattern = (Pattern)_patterns.get(Pattern.toString(dashArray));
							if (aPattern != null) {
								_patternCmbModel.setSelectedItem(aPattern);
							}
							else {
								// El patrón de línea no es uno de los predefinidos, ponemos uno por defecto
								aPattern = (Pattern)_patterns.get(Pattern.toString(new float[]{}));
								_patternCmbModel.setSelectedItem(aPattern);
							}
						}
						else {
							aPattern = (Pattern)_patterns.get(Pattern.toString(new float[]{}));
							_patternCmbModel.setSelectedItem(aPattern);
						}
					}
					catch (FilterEvaluationException e){
						JOptionPane.showMessageDialog(null, "Esta regla no puede ser modificada con este interfaz\nError en la evaluación del filtro");
						setDefaultStrokeValues();
					}
				}
				else {
					// Regla sin stroke. Valores por defecto
					setDefaultStrokeValues();
				}
				Fill fill = ((PolygonSymbolizer)symbolizer).getFill();
				if (fill != null) {
					try {
						fillColorBtn.setBackground(fill.getFill(null));
						fillOpacitySld.setValue((int)(fill.getOpacity(null) * 100));
						GraphicFill graphicFill = fill.getGraphicFill(); 
						if (graphicFill != null) {
							Object[] marksAndExternalGraphics = graphicFill.getGraphic().getMarksAndExtGraphics();
							if ((marksAndExternalGraphics != null) && (marksAndExternalGraphics.length >= 1)) {								
								Object aMarkOrAGraphic = marksAndExternalGraphics[0];
								if (aMarkOrAGraphic instanceof ExternalGraphic) {
									ExternalGraphic anExternalGraphic = (ExternalGraphic)aMarkOrAGraphic;									
									_fillTextureCmbModel.setSelectedItem(new Texture(anExternalGraphic.getOnlineResource().toString(), anExternalGraphic.getFormat(), new TexturePaint(anExternalGraphic.getAsImage(), new Rectangle2D.Double(0, 0, anExternalGraphic.getAsImage().getWidth(), anExternalGraphic.getAsImage().getHeight())), anExternalGraphic.getAsImage().getWidth(), anExternalGraphic.getAsImage().getHeight()));
								}
							}				
						}
					}
					catch (FilterEvaluationException e){
						JOptionPane.showMessageDialog(null, "Esta regla no puede ser modificada con este interfaz\nError en la evaluación del filtro");
						setDefaultFillValues();
					}
				}
				else {
					// Regla sin fill. Valores por defecto
					setDefaultFillValues();
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "El simbolizador de la regla no es un PolygonSymbolizer\nEste interfaz no puede modificar este estilo\nLa definición va a ser alterada");
				setDefaultValues();
			} 
		}
		else {
			// Regla sin simbolizador. Valores por defecto
			setDefaultValues();
		}
	}

	private void setDefaultValues() {
		setDefaultStrokeValues();
		setDefaultFillValues();
		setDefaultWidthValues();
	}

	private void setDefaultStrokeValues() {
		strokeColorBtn.setBackground(Color.BLACK);
		strokeWidthCmb.setSelectedItem("1.0");
		strokeOpacitySld.setValue(100);
		roundLineCapRBtn.setSelected(true);
		roundLineJoinRBtn.setSelected(true);
		Pattern aPattern = (Pattern)_patterns.get(Pattern.toString(new float[]{}));
		_patternCmbModel.setSelectedItem(aPattern);		
	}

	private void setDefaultFillValues() {
		fillColorBtn.setBackground(Color.WHITE);
		fillOpacitySld.setValue(100);
	}

	private void setDefaultWidthValues() {
		
		attributesCmb.setSelectedItem("");
		arithmeticExprCmb.setSelectedItem("");
	}

	private void configureWidth(Expression expression,List attributeList) {
		
		if (expression instanceof ArithmeticExpression) {
			ArithmeticExpression arithmeticExpression = (ArithmeticExpression)expression;
			Expression expr1 = arithmeticExpression.getFirstExpression();
			String propertyName = ((PropertyName)expr1).getValue();
			Iterator attributeIterator = attributeList.iterator();
			boolean found = false;
			while ((attributeIterator.hasNext())&&!found) {
				FeatureAttribute fa = (FeatureAttribute)attributeIterator.next();
				if (fa.getName().equals(propertyName)) {
					found = true;
					_propertyNameCmbModel.setSelectedItem(fa);
				}
			}
			int operatorID = arithmeticExpression.getExpressionId();
			switch (operatorID) {
				case ExpressionDefines.ADD: {
					arithmeticExprCmb.setSelectedItem("suma");
					break;		
				}
				case ExpressionDefines.SUB: {
					arithmeticExprCmb.setSelectedItem("resta");
					break;
				}
				case ExpressionDefines.MUL: {
					arithmeticExprCmb.setSelectedItem("multiplicación");
					break;
				}
				case ExpressionDefines.DIV : {
					arithmeticExprCmb.setSelectedItem("división");
					break;
				}
			}
			Expression expr2 = arithmeticExpression.getSecondExpression();
			strokeWidthCmb.setSelectedItem(((Literal)expr2).getValue());
		}
		else if (expression instanceof Literal) {
			Literal literal = (Literal)expression;
			strokeWidthCmb.setSelectedItem(literal.getValue());
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
			return aplicacion.getI18nString("PolygonRule");		
	}
    
    /** Creates new form InsertUpdatePolygonRulePanel */
    public InsertUpdatePolygonRulePanel() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */

                          
    // <editor-fold defaultstate="collapsed" desc=" Código Generado  ">                          
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        strokeLineCapGroup = new javax.swing.ButtonGroup();
        strokeLineJoinGroup = new javax.swing.ButtonGroup();
        rulePanel = new javax.swing.JPanel();
        JPanel nomPanel = new javax.swing.JPanel();
        ruleNameLbl = new javax.swing.JLabel();
        ruleNameTxt = new javax.swing.JTextField();
        stylePanel = new javax.swing.JPanel();
        strokeColorLbl = new javax.swing.JLabel();
        strokeColorBtn = new javax.swing.JButton();
        strokeOpacityLbl = new javax.swing.JLabel();
        strokeOpacitySld = new javax.swing.JSlider();
        dashArrayLbl = new javax.swing.JLabel();
        dashArrayCmb = new javax.swing.JComboBox();
        fillOpacityLbl = new javax.swing.JLabel();
        fillOpacitySld = new javax.swing.JSlider();
        fillColorLbl = new javax.swing.JLabel();
        fillColorBtn = new javax.swing.JButton();
        fillTextureLbl = new javax.swing.JLabel();
        fillTextureCmb = new javax.swing.JComboBox();
        strokeLineCapPanel = new javax.swing.JPanel();
        buttLineCapRBtn = new javax.swing.JRadioButton();
        roundLineCapRBtn = new javax.swing.JRadioButton();
        squareLineCapRBtn = new javax.swing.JRadioButton();
        buttlineLbl = new javax.swing.JLabel();
        roundlineLbl = new javax.swing.JLabel();
        squarelineLbl = new javax.swing.JLabel();
        strokeLineJoinPanel = new javax.swing.JPanel();
        mitreLineJoinRBtn = new javax.swing.JRadioButton();
        roundLineJoinRBtn = new javax.swing.JRadioButton();
        bevelLineJoinRBtn = new javax.swing.JRadioButton();
        mitreLineJoinLbl = new javax.swing.JLabel();
        roundLineJoinLbl = new javax.swing.JLabel();
        bevelineJoinLbl = new javax.swing.JLabel();
        textureSizeLbl = new javax.swing.JLabel();
        textureSizeCmb = new javax.swing.JComboBox();
        strokeWidthLbl = new javax.swing.JLabel();
        strokeWidthCmb = new javax.swing.JComboBox();
        arithmeticExprCmb = new javax.swing.JComboBox();
        attributesCmb = new javax.swing.JComboBox();
        filterPanel = new javax.swing.JPanel();
        filterChk = new javax.swing.JCheckBox();
        filterBtn = new javax.swing.JButton();
        filterTxt = new javax.swing.JTextField();
        buttonPanel = new javax.swing.JPanel();
        okBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        setMinimumSize(new java.awt.Dimension(431, 550));
        setPreferredSize(new java.awt.Dimension(400, 550));
        rulePanel.setLayout(new java.awt.GridBagLayout());

        rulePanel.setMinimumSize(new java.awt.Dimension(431, 500));
        rulePanel.setPreferredSize(new java.awt.Dimension(435, 500));
        nomPanel.setLayout(new java.awt.GridBagLayout());

        ruleNameLbl.setText("Nombre: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        nomPanel.add(ruleNameLbl, gridBagConstraints);

        ruleNameTxt.setMaximumSize(new java.awt.Dimension(2147483647, 19));
        ruleNameTxt.setMinimumSize(new java.awt.Dimension(300, 19));
        ruleNameTxt.setPreferredSize(new java.awt.Dimension(300, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        nomPanel.add(ruleNameTxt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weighty = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 6);
        rulePanel.add(nomPanel, gridBagConstraints);

        stylePanel.setLayout(new java.awt.GridBagLayout());

        stylePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Estilo: "));
        stylePanel.setMinimumSize(new java.awt.Dimension(431, 400));
        stylePanel.setPreferredSize(new java.awt.Dimension(435, 400));
        strokeColorLbl.setText("Color l\u00ednea: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        stylePanel.add(strokeColorLbl, gridBagConstraints);

        strokeColorBtn.setMinimumSize(new java.awt.Dimension(32, 19));
        strokeColorBtn.setPreferredSize(new java.awt.Dimension(32, 19));
        strokeColorBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                strokeColorBtnActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        stylePanel.add(strokeColorBtn, gridBagConstraints);

        strokeOpacityLbl.setText("Opacidad l\u00ednea: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        stylePanel.add(strokeOpacityLbl, gridBagConstraints);

        strokeOpacitySld.setMajorTickSpacing(10);
        strokeOpacitySld.setMinorTickSpacing(5);
        strokeOpacitySld.setPaintLabels(true);
        strokeOpacitySld.setPaintTicks(true);
        strokeOpacitySld.setValue(100);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        stylePanel.add(strokeOpacitySld, gridBagConstraints);

        dashArrayLbl.setText("Patr\u00f3n l\u00ednea: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        stylePanel.add(dashArrayLbl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        stylePanel.add(dashArrayCmb, gridBagConstraints);

        fillOpacityLbl.setText("Opacidad relleno: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        stylePanel.add(fillOpacityLbl, gridBagConstraints);

        fillOpacitySld.setMajorTickSpacing(10);
        fillOpacitySld.setMinorTickSpacing(5);
        fillOpacitySld.setPaintLabels(true);
        fillOpacitySld.setPaintTicks(true);
        fillOpacitySld.setValue(100);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        stylePanel.add(fillOpacitySld, gridBagConstraints);

        fillColorLbl.setText("Color relleno: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        stylePanel.add(fillColorLbl, gridBagConstraints);

        fillColorBtn.setMinimumSize(new java.awt.Dimension(32, 19));
        fillColorBtn.setPreferredSize(new java.awt.Dimension(32, 19));
        fillColorBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fillColorBtnActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        stylePanel.add(fillColorBtn, gridBagConstraints);

        fillTextureLbl.setText("Textura: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        stylePanel.add(fillTextureLbl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        stylePanel.add(fillTextureCmb, gridBagConstraints);

        strokeLineCapPanel.setLayout(new java.awt.GridBagLayout());

        strokeLineCapPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Redondez en nodos: "));
        strokeLineCapGroup.add(buttLineCapRBtn);
        buttLineCapRBtn.setText("Recto");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        strokeLineCapPanel.add(buttLineCapRBtn, gridBagConstraints);

        strokeLineCapGroup.add(roundLineCapRBtn);
        roundLineCapRBtn.setText("Redondo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        strokeLineCapPanel.add(roundLineCapRBtn, gridBagConstraints);

        strokeLineCapGroup.add(squareLineCapRBtn);
        squareLineCapRBtn.setText("Cuadrado");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        strokeLineCapPanel.add(squareLineCapRBtn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        strokeLineCapPanel.add(buttlineLbl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        strokeLineCapPanel.add(roundlineLbl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        strokeLineCapPanel.add(squarelineLbl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        stylePanel.add(strokeLineCapPanel, gridBagConstraints);

        strokeLineJoinPanel.setLayout(new java.awt.GridBagLayout());

        strokeLineJoinPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Redondez en v\u00e9rtices: "));
        strokeLineJoinGroup.add(mitreLineJoinRBtn);
        mitreLineJoinRBtn.setText("Angulado");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        strokeLineJoinPanel.add(mitreLineJoinRBtn, gridBagConstraints);

        strokeLineJoinGroup.add(roundLineJoinRBtn);
        roundLineJoinRBtn.setText("Redondeado");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        strokeLineJoinPanel.add(roundLineJoinRBtn, gridBagConstraints);

        strokeLineJoinGroup.add(bevelLineJoinRBtn);
        bevelLineJoinRBtn.setText("Achaflanado");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        strokeLineJoinPanel.add(bevelLineJoinRBtn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        strokeLineJoinPanel.add(mitreLineJoinLbl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        strokeLineJoinPanel.add(roundLineJoinLbl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        strokeLineJoinPanel.add(bevelineJoinLbl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        stylePanel.add(strokeLineJoinPanel, gridBagConstraints);

        textureSizeLbl.setText("Tama\u00f1o textura:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        stylePanel.add(textureSizeLbl, gridBagConstraints);

        textureSizeCmb.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 200);
        stylePanel.add(textureSizeCmb, gridBagConstraints);

        strokeWidthLbl.setText("Ancho: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        stylePanel.add(strokeWidthLbl, gridBagConstraints);

        strokeWidthCmb.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        stylePanel.add(strokeWidthCmb, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        stylePanel.add(arithmeticExprCmb, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        stylePanel.add(attributesCmb, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        rulePanel.add(stylePanel, gridBagConstraints);

        filterPanel.setLayout(new java.awt.GridBagLayout());

        filterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtro: "));
        filterPanel.setMinimumSize(new java.awt.Dimension(125, 60));
        filterPanel.setPreferredSize(new java.awt.Dimension(125, 60));
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
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 5);
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

    }// </editor-fold>//GEN-END:initComponents

    
    
    private void filterBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterBtnActionPerformed

		if (filterChk.isSelected()) {

			Request theRequest = FrontControllerFactory.createRequest();
			theRequest.setAttribute("PageInvocator","InsertUpdatePolygonRule");
			FrontController fc =  FrontControllerFactory.getFrontController();
			Action theAction = fc.getAction("UpdateRuleFilter"); 
			ActionForward theActionForward = theAction.doExecute(theRequest);
			_container.forward(theActionForward, theRequest);			

		}

    }//GEN-LAST:event_filterBtnActionPerformed

    private void filterChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterChkActionPerformed

		if (filterChk.isSelected()) {
			Request theRequest = FrontControllerFactory.createRequest();
			theRequest.setAttribute("PageInvocator","InsertUpdatePolygonRule");
			FrontController fc =  FrontControllerFactory.getFrontController();
			Action theAction = fc.getAction("InsertRuleFilter"); 
			ActionForward theActionForward = theAction.doExecute(theRequest);
			_container.forward(theActionForward, theRequest);			
		}
		else {
			Request theRequest = FrontControllerFactory.createRequest();
			theRequest.setAttribute("PageInvocator","InsertUpdatePolygonRule");
			FrontController fc =  FrontControllerFactory.getFrontController();
			Action theAction = fc.getAction("DeleteRuleFilter"); 
			ActionForward theActionForward = theAction.doExecute(theRequest);
			_container.forward(theActionForward, theRequest);			

		}

    }//GEN-LAST:event_filterChkActionPerformed

    private void strokeColorBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_strokeColorBtnActionPerformed
		Color newColor = JColorChooser.showDialog(
							 this,
							 "Color de linea",
							 strokeColorBtn.getBackground());        
		if (newColor != null) {
			strokeColorBtn.setBackground(newColor);
		}
    }//GEN-LAST:event_strokeColorBtnActionPerformed

    private void fillColorBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fillColorBtnActionPerformed
		Color newColor = JColorChooser.showDialog(
							 this,
							 "Color de relleno",
							 fillColorBtn.getBackground());        
		if (newColor != null) {
			fillColorBtn.setBackground(newColor);
		}
    }//GEN-LAST:event_fillColorBtnActionPerformed

    private void okBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okBtnActionPerformed
		if (checkValues()) {
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
			style.put("SymbolizerType", "Polygon");
			style.put("ColorStroke", new Integer(strokeColorBtn.getBackground().getRGB()));
			if (!(attributesCmb.getSelectedItem().equals(""))&&!(arithmeticExprCmb.getSelectedItem().equals(""))) {
				PropertyName propertyName = new PropertyName(((FeatureAttribute)attributesCmb.getSelectedItem()).getName());
				Literal literal = new Literal((String)strokeWidthCmb.getSelectedItem());
				String operator = (String)arithmeticExprCmb.getSelectedItem();
				int operatorID = 0;
				if (operator.equals("suma")) {
					operatorID = ExpressionDefines.ADD;
				}
				else if (operator.equals("resta")) {
					operatorID = ExpressionDefines.SUB;
				}
				else if (operator.equals("multiplicación")) {
					operatorID = ExpressionDefines.MUL;
				}
				else if (operator.equals("división")) {
					operatorID = ExpressionDefines.DIV;
				}
				ArithmeticExpression arithmeticExpression = new ArithmeticExpression(operatorID,propertyName,literal);
				style.put("Width", arithmeticExpression);
			}
			else {
				Literal literal = new Literal((String)strokeWidthCmb.getSelectedItem());
				style.put("Width",literal);
			}
			style.put("OpacityStroke", new Double(strokeOpacitySld.getValue() / 100.0));
			if (dashArrayCmb.getSelectedIndex() != 0) {
				style.put("DashArray", ((Pattern)_patternCmbModel.getSelectedItem()).getDashArray());
			}
			if (buttLineCapRBtn.isSelected())
				style.put("LineCap", "butt");
			else if (roundLineCapRBtn.isSelected())
				style.put("LineCap", "round");
			else if (squareLineCapRBtn.isSelected())
				style.put("LineCap", "square");		
			if (bevelLineJoinRBtn.isSelected())
				style.put("LineJoin", "bevel");
			else if (mitreLineJoinRBtn.isSelected())
				style.put("LineJoin", "mitre");
			else if (roundLineJoinRBtn.isSelected())
				style.put("LineJoin", "round");
			style.put("ColorFill", new Integer(fillColorBtn.getBackground().getRGB()));
			style.put("OpacityFill", new Double(fillOpacitySld.getValue() / 100.0));
			Texture texture = (Texture)_fillTextureCmbModel.getSelectedItem();
			if (texture.getPaint() != null) {
				try {
					Double.parseDouble((String)textureSizeCmb.getSelectedItem());			
				}
				catch (NumberFormatException e) {
					e.printStackTrace();
				}
				double percentage = new Double((String)textureSizeCmb.getSelectedItem()).doubleValue();
				int width = texture.getWidth();
				double newSizeAsDouble = (width*percentage)/100;
				int newSize = new Double(newSizeAsDouble).intValue();
				style.put("ExternalGraphicSize",new Integer(newSize));
				style.put("ExternalGraphicURL", texture.getURL());
				style.put("ExternalGraphicFormat", texture.getFormat());
			}
			theRequest.setAttribute("Style", style);
			FrontController fc =  FrontControllerFactory.getFrontController();
			Action theAction = fc.getAction("CreateCustomRule"); 
			ActionForward theActionForward = theAction.doExecute(theRequest);
			_container.forward(theActionForward, theRequest);
		}
    }//GEN-LAST:event_okBtnActionPerformed

	private boolean checkValues() {
		
		boolean valuesAreCorrect = true;
		StringBuffer errorMessage = new StringBuffer();
		Session session = FrontControllerFactory.getSession();
		if (!((String)strokeWidthCmb.getSelectedItem()).equals("")) {
			try {
				double stroke = Double.parseDouble((String)strokeWidthCmb.getSelectedItem());
				if (stroke <= 0) {
					errorMessage.append("El ancho de linea debe ser mayor que cero\n");
					valuesAreCorrect = false;		
				}							
			}
			catch (NumberFormatException e) {
				errorMessage.append("El ancho de linea debe ser numérico\n");
				valuesAreCorrect = false;
			}
		}
		if (!((String)textureSizeCmb.getSelectedItem()).equals("")) {
			try {
				double size = Double.parseDouble((String)textureSizeCmb.getSelectedItem());
				if (size <= 0) {
					errorMessage.append("El tamaño de textura debe ser mayor o igual que cero\n");
					valuesAreCorrect = false;		
				}							
			}
			catch (NumberFormatException e) {
				errorMessage.append("El tamaño de textura debe ser numérico\n");
				valuesAreCorrect = false;
			}
		}
		if (!valuesAreCorrect) {
			JOptionPane.showMessageDialog(null, "Los siguientes valores son incorrectos:\n" + errorMessage.toString());
		}
		return valuesAreCorrect;
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
	private DefaultComboBoxModel _patternCmbModel;
	private HashMap _patterns;
	private DefaultComboBoxModel _fillTextureCmbModel;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox arithmeticExprCmb;
    private javax.swing.JComboBox attributesCmb;
    private javax.swing.JRadioButton bevelLineJoinRBtn;
    private javax.swing.JLabel bevelineJoinLbl;
    private javax.swing.JRadioButton buttLineCapRBtn;
    private javax.swing.JLabel buttlineLbl;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JComboBox dashArrayCmb;
    private javax.swing.JLabel dashArrayLbl;
    private javax.swing.JButton fillColorBtn;
    private javax.swing.JLabel fillColorLbl;
    private javax.swing.JLabel fillOpacityLbl;
    private javax.swing.JSlider fillOpacitySld;
    private javax.swing.JComboBox fillTextureCmb;
    private javax.swing.JLabel fillTextureLbl;
    private javax.swing.JButton filterBtn;
    private javax.swing.JCheckBox filterChk;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JTextField filterTxt;
    private javax.swing.JLabel mitreLineJoinLbl;
    private javax.swing.JRadioButton mitreLineJoinRBtn;
    private javax.swing.JButton okBtn;
    private javax.swing.JRadioButton roundLineCapRBtn;
    private javax.swing.JLabel roundLineJoinLbl;
    private javax.swing.JRadioButton roundLineJoinRBtn;
    private javax.swing.JLabel roundlineLbl;
    private javax.swing.JLabel ruleNameLbl;
    private javax.swing.JTextField ruleNameTxt;
    private javax.swing.JPanel rulePanel;
    private javax.swing.JRadioButton squareLineCapRBtn;
    private javax.swing.JLabel squarelineLbl;
    private javax.swing.JButton strokeColorBtn;
    private javax.swing.JLabel strokeColorLbl;
    private javax.swing.ButtonGroup strokeLineCapGroup;
    private javax.swing.JPanel strokeLineCapPanel;
    private javax.swing.ButtonGroup strokeLineJoinGroup;
    private javax.swing.JPanel strokeLineJoinPanel;
    private javax.swing.JLabel strokeOpacityLbl;
    private javax.swing.JSlider strokeOpacitySld;
    private javax.swing.JComboBox strokeWidthCmb;
    private javax.swing.JLabel strokeWidthLbl;
    private javax.swing.JPanel stylePanel;
    private javax.swing.JComboBox textureSizeCmb;
    private javax.swing.JLabel textureSizeLbl;
    // End of variables declaration//GEN-END:variables
    
}
