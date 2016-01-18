/*
 * InsertUpdateThematicPolygonRulePanel.java
 *
 * Created on 30 de julio de 2004, 10:50
 */
package com.geopista.style.sld.ui.impl.panels;

import java.awt.Color;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import org.deegree.graphics.sld.ExternalGraphic;
import org.deegree.services.wfs.filterencoding.Expression;
import org.deegree_impl.graphics.sld.StyleFactory;
import org.deegree_impl.services.wfs.filterencoding.ArithmeticExpression;
import org.deegree_impl.services.wfs.filterencoding.ExpressionDefines;
import org.deegree_impl.services.wfs.filterencoding.Literal;
import org.deegree_impl.services.wfs.filterencoding.PropertyName;

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
import com.geopista.style.sld.ui.impl.PaintRenderer;
import com.geopista.style.sld.ui.impl.Pattern;
import com.geopista.style.sld.ui.impl.PatternRenderer;
import com.geopista.style.sld.ui.impl.Texture;
import com.geopista.style.sld.ui.impl.ThematicTableModel;
import com.geopista.style.sld.ui.impl.UIUtils;
import com.geopista.ui.images.IconLoader;
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
public class InsertUpdateThematicPolygonRulePanel extends AbstractPanel implements ActionForward {

    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private DefaultComboBoxModel _strokeWidthCmbModel;
	private static final String[] _widthValues = {"1.0","2.0","3.0","4.0","5.0"};
	private DefaultComboBoxModel _textureSizeCmbModel;
	private static final String[] _percentageValues = {"25","50","75","100"};
	private DefaultComboBoxModel _arithmeticOperatorsModel;
	private static final String[] _operatorValues = {"", "suma","resta","multiplicación","división"};
	private DefaultComboBoxModel _propertyNameCmbModel;

	private GeopistaLayer layer;
	
	public void configure(Request request) {
		
		Session session = FrontControllerFactory.getSession();

		buttlineLbl.setIcon(IconLoader.icon("/com/geopista/ui/images/linecapbutt.JPG"));
		roundlineLbl.setIcon(IconLoader.icon("/com/geopista/ui/images/linecapround2.JPG"));
		squarelineLbl.setIcon(IconLoader.icon("/com/geopista/ui/images/linecapsquare2.JPG"));
		mitreLineJoinLbl.setIcon(IconLoader.icon("/com/geopista/ui/images/linejoinmitre2.JPG"));
		roundLineJoinLbl.setIcon(IconLoader.icon("/com/geopista/ui/images/linejoinround2.JPG"));
		bevelineJoinLbl.setIcon(IconLoader.icon("/com/geopista/ui/images/linejoinbevel2.JPG"));

		_arithmeticOperatorsModel = new DefaultComboBoxModel(_operatorValues);	
		arithmeticExprCmb.setModel(_arithmeticOperatorsModel);	
		_propertyNameCmbModel = new DefaultComboBoxModel();

		_propertyCmbModel = new DefaultComboBoxModel();
		//TODO: Esto no está terminado
		HashMap features = (HashMap) session.getAttribute("FeatureAttributes");
		Iterator featureNameIterator = features.keySet().iterator();
		_propertyNameCmbModel.addElement("");
		while (featureNameIterator.hasNext()) {
			String featureName = (String) featureNameIterator.next();
			Iterator attributeIterator = ((List)features.get(featureName)).iterator();			
			while(attributeIterator.hasNext()) {
				FeatureAttribute attribute = (FeatureAttribute)attributeIterator.next();
				_propertyCmbModel.addElement(attribute); 
				if ((attribute.getType().equals("DOUBLE"))||(attribute.getType().equals("INTEGER"))) { 
					_propertyNameCmbModel.addElement(attribute);
				}
			}			
		}
		propertyNameCmb.setModel(_propertyCmbModel);
		attributesCmb.setModel(_propertyNameCmbModel);
		
		this.layer = (GeopistaLayer) session.getAttribute("Layer");
		propertyNameCmb.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (layer.getFeatureCollectionWrapper().getFeatureSchema() instanceof GeopistaSchema) 
				{
					setCellEditors(layer, true);
				}
			}
		});
		
		_strokeWidthCmbModel = new DefaultComboBoxModel(_widthValues);	
		strokeWidthCmb.setModel(_strokeWidthCmbModel);	
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
		
		Pattern aPattern;
		_patternCmbModel = new DefaultComboBoxModel();
		_patterns = new HashMap();
		Iterator patternIterator = Pattern.createPatterns().iterator(); 
		while (patternIterator.hasNext()) {
			aPattern = (Pattern)patternIterator.next();
			_patterns.put(aPattern.toString(), aPattern);
			_patternCmbModel.addElement(aPattern);
		}
		dashArrayCmb.setModel(_patternCmbModel);
		dashArrayCmb.setRenderer(new PatternRenderer());
		
		_fillTextureCmbModel = new DefaultComboBoxModel(Texture.createTextures());
		fillTextureCmb.setModel(_fillTextureCmbModel);
		fillTextureCmb.setRenderer(new PaintRenderer());
		_textureSizeCmbModel = new DefaultComboBoxModel(_percentageValues);
		textureSizeCmb.setModel(_textureSizeCmbModel);
		textureSizeCmb.setSelectedItem("100");
		
		_tableModel = new ThematicTableModel();
		valueListTable.setModel(_tableModel);
		valueListTable.setDefaultRenderer(Color.class, new ColorRenderer(true));
		valueListTable.setDefaultEditor(Color.class, new ColorEditor());
		setDefaultWidthValues();
		
		ListSelectionModel rowSM = valueListTable.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener() {
			
			public void valueChanged(ListSelectionEvent e) {
				valueListTableSelectionChanged(e);
			}
		});
		HashMap aDefaultStyle = defaultStyle(Color.WHITE);
		fillStylePanel(aDefaultStyle);
	}

	private void setDefaultWidthValues() {
		
		attributesCmb.setSelectedItem("");
		arithmeticExprCmb.setSelectedItem("");
	}

	private void fillStylePanel(HashMap style) {
		Pattern aPattern;

		strokeColorBtn.setBackground(new Color(((Integer)style.get("ColorStroke")).intValue()));
		Expression expression = (Expression)style.get("Width");
		configureWidth(expression);
		strokeOpacitySld.setValue((int)(((Double)style.get("OpacityStroke")).doubleValue() * 100));
		if (style.get("LineCap").equals("butt")) {
			buttLineCapRBtn.setSelected(true);
			roundLineCapRBtn.setSelected(false);
			squareLineCapRBtn.setSelected(false);
		}
		else if (style.get("LineCap").equals("round")) {
			buttLineCapRBtn.setSelected(false);
			roundLineCapRBtn.setSelected(true);
			squareLineCapRBtn.setSelected(false);
		}
		else if (style.get("LineCap").equals("square")) {
			buttLineCapRBtn.setSelected(false);
			roundLineCapRBtn.setSelected(false);
			squareLineCapRBtn.setSelected(true);
		}
		if (style.get("LineJoin").equals("bevel")) {
			bevelLineJoinRBtn.setSelected(true);
			mitreLineJoinRBtn.setSelected(false);
			roundLineJoinRBtn.setSelected(false);
		}
		else if (style.get("LineJoin").equals("mitre")) {
			bevelLineJoinRBtn.setSelected(false);
			mitreLineJoinRBtn.setSelected(true);
			roundLineJoinRBtn.setSelected(false);
		}
		else if (style.get("LineJoin").equals("round")) {
			bevelLineJoinRBtn.setSelected(false);
			mitreLineJoinRBtn.setSelected(false);
			roundLineJoinRBtn.setSelected(true);
		}
		float[] dashArray = (float[])style.get("DashArray");
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
		fillOpacitySld.setValue((int)(((Double)style.get("OpacityFill")).doubleValue() * 100));
		String externalGraphicURL = (String)style.get("ExternalGraphicURL");
		String externalGraphicFormat = (String)style.get("ExternalGraphicFormat");
		if ((externalGraphicURL != null)&&(externalGraphicFormat != null)) {
			try {
				ExternalGraphic anExternalGraphic = StyleFactory.createExternalGraphic(externalGraphicURL,externalGraphicFormat);
				_fillTextureCmbModel.setSelectedItem(new Texture(anExternalGraphic.getOnlineResource().toString(), anExternalGraphic.getFormat(), new TexturePaint(anExternalGraphic.getAsImage(), new Rectangle2D.Double(0, 0, anExternalGraphic.getAsImage().getWidth(), anExternalGraphic.getAsImage().getHeight())), anExternalGraphic.getAsImage().getWidth(), anExternalGraphic.getAsImage().getHeight()));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		else {
			fillTextureCmb.setSelectedIndex(0);
		}
	}

	private void configureWidth(Expression expression) {
		
		if (expression instanceof ArithmeticExpression) {
			ArithmeticExpression arithmeticExpression = (ArithmeticExpression)expression;
			Expression expr1 = arithmeticExpression.getFirstExpression();
			String propertyName = ((PropertyName)expr1).getValue();
			FeatureAttribute fa = getFeatureAttributeSelected(propertyName,attributesCmb);
			attributesCmb.setSelectedItem(fa);
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
			attributesCmb.setSelectedItem("");
			arithmeticExprCmb.setSelectedItem("");
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
			return aplicacion.getI18nString("ThematicPolygonRule");		
	}
   
    /** Creates new form InsertUpdateThematicPolygonRulePanel */
    public InsertUpdateThematicPolygonRulePanel() {
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
        strokeColorLbl = new javax.swing.JLabel();
        strokeColorBtn = new javax.swing.JButton();
        strokeOpacityLbl = new javax.swing.JLabel();
        strokeOpacitySld = new javax.swing.JSlider();
        dashArrayLbl = new javax.swing.JLabel();
        dashArrayCmb = new javax.swing.JComboBox();
        fillOpacityLbl = new javax.swing.JLabel();
        fillOpacitySld = new javax.swing.JSlider();
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
        buttonPanel = new javax.swing.JPanel();
        okBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        thematicPanel.setLayout(new java.awt.GridBagLayout());

        rangeThematicLbl.setText("Por rangos: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        thematicPanel.add(rangeThematicLbl, gridBagConstraints);

        rangeThematicChk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rangeThematicChkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        thematicPanel.add(rangeThematicChk, gridBagConstraints);

        propertyNameLbl.setText("Propiedad: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        thematicPanel.add(propertyNameLbl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        thematicPanel.add(propertyNameCmb, gridBagConstraints);

        valuePanel.setLayout(new java.awt.BorderLayout());

        valuePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Valores: "));
        valuePanel.setMaximumSize(new java.awt.Dimension(2147483647, 140));
        valuePanel.setMinimumSize(new java.awt.Dimension(112, 140));
        valuePanel.setPreferredSize(new java.awt.Dimension(202, 140));
        valueListTableScroll.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        valueListTableScroll.setMaximumSize(new java.awt.Dimension(32767, 160));
        valueListTableScroll.setMinimumSize(new java.awt.Dimension(21, 120));
        valueListTableScroll.setPreferredSize(new java.awt.Dimension(111, 140));
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
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
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
        gridBagConstraints.gridy = 2;
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
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        valueButtonsPanel.add(rampColorBtn, gridBagConstraints);

        valuePanel.add(valueButtonsPanel, java.awt.BorderLayout.EAST);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        thematicPanel.add(valuePanel, gridBagConstraints);

        stylePanel.setLayout(new java.awt.GridBagLayout());

        stylePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Estilo: "));
        strokeColorLbl.setText("Color l\u00ednea: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        stylePanel.add(strokeColorLbl, gridBagConstraints);

        strokeColorBtn.setMinimumSize(new java.awt.Dimension(32, 19));
        strokeColorBtn.setPreferredSize(new java.awt.Dimension(32, 19));
        strokeColorBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                strokeColorBtnActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        stylePanel.add(strokeColorBtn, gridBagConstraints);

        strokeOpacityLbl.setText("Opacidad l\u00ednea: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        stylePanel.add(strokeOpacityLbl, gridBagConstraints);

        strokeOpacitySld.setMajorTickSpacing(10);
        strokeOpacitySld.setMinorTickSpacing(5);
        strokeOpacitySld.setPaintLabels(true);
        strokeOpacitySld.setPaintTicks(true);
        strokeOpacitySld.setValue(100);
        strokeOpacitySld.setMaximumSize(new java.awt.Dimension(32767, 40));
        strokeOpacitySld.setMinimumSize(new java.awt.Dimension(36, 40));
        strokeOpacitySld.setPreferredSize(new java.awt.Dimension(200, 40));
        strokeOpacitySld.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                strokeOpacitySldFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        stylePanel.add(strokeOpacitySld, gridBagConstraints);

        dashArrayLbl.setText("Patr\u00f3n l\u00ednea: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        stylePanel.add(dashArrayLbl, gridBagConstraints);

        dashArrayCmb.setMaximumSize(new java.awt.Dimension(32767, 18));
        dashArrayCmb.setPreferredSize(new java.awt.Dimension(27, 18));
        dashArrayCmb.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                dashArrayCmbFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        stylePanel.add(dashArrayCmb, gridBagConstraints);

        fillOpacityLbl.setText("Opacidad relleno: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        stylePanel.add(fillOpacityLbl, gridBagConstraints);

        fillOpacitySld.setMajorTickSpacing(10);
        fillOpacitySld.setMinorTickSpacing(5);
        fillOpacitySld.setPaintLabels(true);
        fillOpacitySld.setPaintTicks(true);
        fillOpacitySld.setValue(100);
        fillOpacitySld.setMaximumSize(new java.awt.Dimension(32767, 40));
        fillOpacitySld.setMinimumSize(new java.awt.Dimension(36, 40));
        fillOpacitySld.setPreferredSize(new java.awt.Dimension(200, 40));
        fillOpacitySld.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fillOpacitySldFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        stylePanel.add(fillOpacitySld, gridBagConstraints);

        fillTextureLbl.setText("Textura: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        stylePanel.add(fillTextureLbl, gridBagConstraints);

        fillTextureCmb.setMaximumSize(new java.awt.Dimension(32767, 18));
        fillTextureCmb.setPreferredSize(new java.awt.Dimension(27, 18));
        fillTextureCmb.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fillTextureCmbFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        stylePanel.add(fillTextureCmb, gridBagConstraints);

        strokeLineCapPanel.setLayout(new java.awt.GridBagLayout());

        strokeLineCapPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Redondez en nodos: "));
        strokeLineCapGroup.add(buttLineCapRBtn);
        buttLineCapRBtn.setText("Recto");
        buttLineCapRBtn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                buttLineCapRBtnFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        strokeLineCapPanel.add(buttLineCapRBtn, gridBagConstraints);

        strokeLineCapGroup.add(roundLineCapRBtn);
        roundLineCapRBtn.setText("Redondo");
        roundLineCapRBtn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                roundLineCapRBtnFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        strokeLineCapPanel.add(roundLineCapRBtn, gridBagConstraints);

        strokeLineCapGroup.add(squareLineCapRBtn);
        squareLineCapRBtn.setText("Cuadrado");
        squareLineCapRBtn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                squareLineCapRBtnFocusLost(evt);
            }
        });

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
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        stylePanel.add(strokeLineCapPanel, gridBagConstraints);

        strokeLineJoinPanel.setLayout(new java.awt.GridBagLayout());

        strokeLineJoinPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Redondez en v\u00e9rtices: "));
        strokeLineJoinGroup.add(mitreLineJoinRBtn);
        mitreLineJoinRBtn.setText("Angulado");
        mitreLineJoinRBtn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                mitreLineJoinRBtnFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        strokeLineJoinPanel.add(mitreLineJoinRBtn, gridBagConstraints);

        strokeLineJoinGroup.add(roundLineJoinRBtn);
        roundLineJoinRBtn.setText("Redondeado");
        roundLineJoinRBtn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                roundLineJoinRBtnFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        strokeLineJoinPanel.add(roundLineJoinRBtn, gridBagConstraints);

        strokeLineJoinGroup.add(bevelLineJoinRBtn);
        bevelLineJoinRBtn.setText("Achaflanado");
        bevelLineJoinRBtn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                bevelLineJoinRBtnFocusLost(evt);
            }
        });

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
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        stylePanel.add(strokeLineJoinPanel, gridBagConstraints);

        textureSizeLbl.setText("Tama\u00f1o textura:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        stylePanel.add(textureSizeLbl, gridBagConstraints);

        textureSizeCmb.setEditable(true);
        textureSizeCmb.setMaximumSize(new java.awt.Dimension(32767, 18));
        textureSizeCmb.setPreferredSize(new java.awt.Dimension(122, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        stylePanel.add(textureSizeCmb, gridBagConstraints);

        strokeWidthLbl.setText("Ancho: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        stylePanel.add(strokeWidthLbl, gridBagConstraints);

        strokeWidthCmb.setEditable(true);
        strokeWidthCmb.setMaximumSize(new java.awt.Dimension(32767, 18));
        strokeWidthCmb.setPreferredSize(new java.awt.Dimension(122, 18));
        strokeWidthCmb.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                strokeWidthCmbFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        stylePanel.add(strokeWidthCmb, gridBagConstraints);

        arithmeticExprCmb.setMaximumSize(new java.awt.Dimension(32767, 18));
        arithmeticExprCmb.setPreferredSize(new java.awt.Dimension(27, 18));
        arithmeticExprCmb.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                arithmeticExprCmbFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        stylePanel.add(arithmeticExprCmb, gridBagConstraints);

        attributesCmb.setMaximumSize(new java.awt.Dimension(32767, 18));
        attributesCmb.setPreferredSize(new java.awt.Dimension(27, 18));
        attributesCmb.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                attributesCmbFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        stylePanel.add(attributesCmb, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        thematicPanel.add(stylePanel, gridBagConstraints);

        add(thematicPanel, java.awt.BorderLayout.CENTER);

        buttonPanel.setLayout(new java.awt.GridBagLayout());

        okBtn.setText("Aceptar");
        okBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okBtnActionPerformed(evt);
            }
        });

        buttonPanel.add(okBtn, new java.awt.GridBagConstraints());

        cancelBtn.setText("Cancelar");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        buttonPanel.add(cancelBtn, new java.awt.GridBagConstraints());

        add(buttonPanel, java.awt.BorderLayout.SOUTH);

    }// </editor-fold>                        

    
    private void attributesCmbFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_attributesCmbFocusLost
		ThematicTableModel tm = (ThematicTableModel)valueListTable.getModel();
		for (int i=0; i<_currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);  
			if (!attributesCmb.getSelectedItem().equals("") && !arithmeticExprCmb.getSelectedItem().equals("")) {
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
				aStyle.put("Width", arithmeticExpression);
			}
			else {
				Literal literal = new Literal((String)strokeWidthCmb.getSelectedItem());
				aStyle.put("Width",literal);
			}
		}
    }//GEN-LAST:event_attributesCmbFocusLost

    private void arithmeticExprCmbFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_arithmeticExprCmbFocusLost
		ThematicTableModel tm = (ThematicTableModel)valueListTable.getModel();
		for (int i=0; i<_currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);  
			if (!attributesCmb.getSelectedItem().equals("") && !arithmeticExprCmb.getSelectedItem().equals("")) {
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
				aStyle.put("Width", arithmeticExpression);
			}
			else {
				Literal literal = new Literal((String)strokeWidthCmb.getSelectedItem());
				aStyle.put("Width",literal);
			}
		}
    }//GEN-LAST:event_arithmeticExprCmbFocusLost

    private void strokeWidthCmbFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_strokeWidthCmbFocusLost
		ThematicTableModel tm = (ThematicTableModel)valueListTable.getModel();
		for (int i=0; i<_currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);  
			if (!attributesCmb.getSelectedItem().equals("") && !arithmeticExprCmb.getSelectedItem().equals("")) {
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
				aStyle.put("Width", arithmeticExpression);
			}
			else {
				Literal literal = new Literal((String)strokeWidthCmb.getSelectedItem());
				aStyle.put("Width",literal);
			}
		}
    }//GEN-LAST:event_strokeWidthCmbFocusLost

    private void mitreLineJoinRBtnFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_mitreLineJoinRBtnFocusLost
		ThematicTableModel tm = (ThematicTableModel)valueListTable.getModel();
		for (int i=0; i<_currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);  
			if (mitreLineJoinRBtn.isSelected())
				aStyle.put("LineJoin", "mitre");		
		}
    }//GEN-LAST:event_mitreLineJoinRBtnFocusLost

    private void roundLineJoinRBtnFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_roundLineJoinRBtnFocusLost
		ThematicTableModel tm = (ThematicTableModel)valueListTable.getModel();
		for (int i=0; i<_currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);  
			if (roundLineJoinRBtn.isSelected())
				aStyle.put("LineJoin", "round");		
		}
    }//GEN-LAST:event_roundLineJoinRBtnFocusLost

    private void bevelLineJoinRBtnFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bevelLineJoinRBtnFocusLost
		ThematicTableModel tm = (ThematicTableModel)valueListTable.getModel();
		for (int i=0; i<_currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);  
			if (bevelLineJoinRBtn.isSelected())
				aStyle.put("LineJoin", "bevel");		
		}
    }//GEN-LAST:event_bevelLineJoinRBtnFocusLost

    private void buttLineCapRBtnFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buttLineCapRBtnFocusLost
		ThematicTableModel tm = (ThematicTableModel)valueListTable.getModel();
		for (int i=0; i<_currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);  
			if (buttLineCapRBtn.isSelected())
				aStyle.put("LineCap", "butt");		
		}
    }//GEN-LAST:event_buttLineCapRBtnFocusLost

    private void roundLineCapRBtnFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_roundLineCapRBtnFocusLost
		ThematicTableModel tm = (ThematicTableModel)valueListTable.getModel();
		for (int i=0; i<_currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);  
			if (roundLineCapRBtn.isSelected())
				aStyle.put("LineCap", "round");		
		}
    }//GEN-LAST:event_roundLineCapRBtnFocusLost

    private void squareLineCapRBtnFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_squareLineCapRBtnFocusLost
		ThematicTableModel tm = (ThematicTableModel)valueListTable.getModel();
		for (int i=0; i<_currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);  
			if (squareLineCapRBtn.isSelected())
				aStyle.put("LineCap", "square");		
		}
    }//GEN-LAST:event_squareLineCapRBtnFocusLost

    private void fillTextureCmbFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fillTextureCmbFocusLost
		ThematicTableModel tm = (ThematicTableModel)valueListTable.getModel();
		Texture texture = (Texture)_fillTextureCmbModel.getSelectedItem();
		for (int i=0; i<_currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);  
			if (texture.getPaint() != null) {
				aStyle.put("ExternalGraphicURL", texture.getURL());
				aStyle.put("ExternalGraphicFormat", texture.getFormat());
				double percentage = new Double((String)textureSizeCmb.getSelectedItem()).doubleValue();
				int width = texture.getWidth();
				double newSizeAsDouble = (width*percentage)/100;
				int newSize = new Double(newSizeAsDouble).intValue();
				aStyle.put("ExternalGraphicSize",new Integer(newSize));
			}
		}
    }//GEN-LAST:event_fillTextureCmbFocusLost

    private void fillOpacitySldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fillOpacitySldFocusLost
		ThematicTableModel tm = (ThematicTableModel)valueListTable.getModel();
		for (int i=0; i<_currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);  
			aStyle.put("OpacityFill", new Double(fillOpacitySld.getValue() / 100.0));
		}
    }//GEN-LAST:event_fillOpacitySldFocusLost

    private void strokeOpacitySldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_strokeOpacitySldFocusLost
		ThematicTableModel tm = (ThematicTableModel)valueListTable.getModel();
		for (int i=0; i<_currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);  
			aStyle.put("OpacityStroke", new Double(strokeOpacitySld.getValue() / 100.0));
		}
    }//GEN-LAST:event_strokeOpacitySldFocusLost

    private void dashArrayCmbFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dashArrayCmbFocusLost
		ThematicTableModel tm = (ThematicTableModel)valueListTable.getModel();
		for (int i=0; i<_currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);  
			if (dashArrayCmb.getSelectedIndex() != 0) {
				aStyle.put("DashArray", ((Pattern)_patternCmbModel.getSelectedItem()).getDashArray());
			}
			else {
				aStyle.put("DashArray", null);
			}
		}
    }//GEN-LAST:event_dashArrayCmbFocusLost

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

	private void strokeColorBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_strokeColorBtnActionPerformed
		Color newColor = JColorChooser.showDialog(
												 this,
												 "Color de linea",
												 strokeColorBtn.getBackground());        
		if (newColor != null) {
			strokeColorBtn.setBackground(newColor);
			ThematicTableModel tm = (ThematicTableModel)valueListTable.getModel();
			for (int i=0; i<_currentSelection.length; i++) {
				HashMap aStyle = tm.getStyle(_currentSelection[i]);  
				aStyle.put("ColorStroke", new Integer(strokeColorBtn.getBackground().getRGB()));
			}
		}
	}//GEN-LAST:event_strokeColorBtnActionPerformed

	private void rangeThematicChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rangeThematicChkActionPerformed
		if (rangeThematicChk.isSelected()) {
			_tableModel.setRangeModel(true);
		}
		else {
			_tableModel.setRangeModel(false);
		}
	}//GEN-LAST:event_rangeThematicChkActionPerformed

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
				int fillColor = ((Integer)style.get("ColorFill")).intValue();
				if (currentColor.getRGB() != fillColor) {
					style.put("ColorFill",new Integer(currentColor.getRGB()));
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
				int fillColor = ((Integer)style.get("ColorFill")).intValue();
				if (currentColor.getRGB() != fillColor) {
					style.put("ColorFill",new Integer(currentColor.getRGB()));
				}
			}
			styleList.add(style);
			valueList.add(_tableModel.getValueAt(i,1));
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
		
		style.put("SymbolizerType", "Polygon");
		style.put("ColorStroke", new Integer(0));
		style.put("ColorFill", new Integer(fillColor.getRGB()));
		style.put("Width", new Literal("1"));
		style.put("OpacityStroke", new Double(1));
		style.put("OpacityFill", new Double(1));
		style.put("DashArray", null);
		style.put("LineCap", "round");
		style.put("LineJoin", "round");
		style.put("ExternalGraphicURL", null);
		style.put("ExternalGraphicFormat", null);
		style.put("ExternalGraphicSize",null);
		return style; 
	}

	private HashMap createStyleFromStylePanel(Color fillColor) {
		HashMap style = new HashMap();
		style.put("SymbolizerType", "Polygon");
		style.put("ColorStroke", new Integer(strokeColorBtn.getBackground().getRGB()));
		style.put("ColorFill", new Integer(fillColor.getRGB()));
		if (!attributesCmb.getSelectedItem().equals("") && !arithmeticExprCmb.getSelectedItem().equals("")) {
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
		style.put("OpacityFill", new Double(fillOpacitySld.getValue() / 100.0));
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
	private DefaultComboBoxModel _fillTextureCmbModel;
	private int[] _currentSelection = {};
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton allValuesBtn;
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
    private javax.swing.JLabel fillOpacityLbl;
    private javax.swing.JSlider fillOpacitySld;
    private javax.swing.JComboBox fillTextureCmb;
    private javax.swing.JLabel fillTextureLbl;
    private javax.swing.JLabel mitreLineJoinLbl;
    private javax.swing.JRadioButton mitreLineJoinRBtn;
    private javax.swing.JButton newValueBtn;
    private javax.swing.JButton okBtn;
    private javax.swing.JComboBox propertyNameCmb;
    private javax.swing.JLabel propertyNameLbl;
    private javax.swing.JButton rampColorBtn;
    private javax.swing.JCheckBox rangeThematicChk;
    private javax.swing.JLabel rangeThematicLbl;
    private javax.swing.JButton removeValueBtn;
    private javax.swing.JRadioButton roundLineCapRBtn;
    private javax.swing.JLabel roundLineJoinLbl;
    private javax.swing.JRadioButton roundLineJoinRBtn;
    private javax.swing.JLabel roundlineLbl;
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
