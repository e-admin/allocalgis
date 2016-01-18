/*
 * InsertUpdateThematicPointRulePanel.java
 *
 * Created on 2 de agosto de 2004, 18:11
 */
package com.geopista.style.sld.ui.impl.panels;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import org.deegree.services.wfs.filterencoding.Expression;
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
import com.geopista.style.sld.ui.impl.FilterGraphicFiles;
import com.geopista.style.sld.ui.impl.GraphicFormatManager;
import com.geopista.style.sld.ui.impl.JComboEditorRenderTableDomain;
import com.geopista.style.sld.ui.impl.MarkGraphicRenderer;
import com.geopista.style.sld.ui.impl.Pattern;
import com.geopista.style.sld.ui.impl.PatternRenderer;
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
 * @author Enxenio S.L.
 */
public class InsertUpdateThematicPointRulePanel extends AbstractPanel implements
		ActionForward {

	private static AppContext aplicacion = (AppContext) AppContext
			.getApplicationContext();

	final static String GRAPHIC_PANEL = "Gráfico externo";

	final static String MARK_PANEL = "Símbolo predefinido";

	private DefaultComboBoxModel _rotationCmbModel;

	private static final String[] _rotationValues = { "0.0", "45.0", "90.0",
			"135.0", "180.0", "225.0", "270.0", "315.0", "360.0" };

	private DefaultComboBoxModel _strokeWidthCmbModel;

	private static final String[] _widthValues = { "1.0", "2.0", "3.0", "4.0",
			"5.0" };

	private DefaultComboBoxModel _arithmeticOperatorsModel;

	private static final String[] _operatorValues = { "", "suma", "resta",
			"multiplicación", "división" };

	private DefaultComboBoxModel _propertyNameCmbModel;

	private DefaultComboBoxModel _arithmeticOperatorsModel2;

	private DefaultComboBoxModel _propertyNameCmbModel2;

	private DefaultComboBoxModel _markOrGraphicCmbModel;

	private GeopistaLayer layer;

	public void configure(Request request) {

		Session session = FrontControllerFactory.getSession();

		buttlineLbl.setIcon(IconLoader
				.icon("/com/geopista/ui/images/linecapbuttsmall.jpg"));
		roundlineLbl.setIcon(IconLoader
				.icon("/com/geopista/ui/images/linecaproundsmall.jpg"));
		squarelineLbl.setIcon(IconLoader
				.icon("/com/geopista/ui/images/linecapsquaresmall.jpg"));
		mitreLineJoinLbl.setIcon(IconLoader
				.icon("/com/geopista/ui/images/linejoinmitresmall.jpg"));
		roundLineJoinLbl.setIcon(IconLoader
				.icon("/com/geopista/ui/images/linejoinroundsmall.jpg"));
		bevelineJoinLbl.setIcon(IconLoader
				.icon("/com/geopista/ui/images/linejoinbevelsmall.jpg"));

		_arithmeticOperatorsModel = new DefaultComboBoxModel(_operatorValues);
		arithmeticExprCmb.setModel(_arithmeticOperatorsModel);
		_propertyNameCmbModel = new DefaultComboBoxModel();
		_arithmeticOperatorsModel2 = new DefaultComboBoxModel(_operatorValues);
		arithmeticExprCmb1.setModel(_arithmeticOperatorsModel2);
		_propertyNameCmbModel2 = new DefaultComboBoxModel();

		_propertyCmbModel = new DefaultComboBoxModel();
		// TODO: Esto no está terminado
		final HashMap features = (HashMap) session
				.getAttribute("FeatureAttributes");
		Iterator featureNameIterator = features.keySet().iterator();
		_propertyNameCmbModel.addElement("");
		_propertyNameCmbModel2.addElement("");
		while (featureNameIterator.hasNext()) {
			String featureName = (String) featureNameIterator.next();
			Iterator attributeIterator = ((List) features.get(featureName))
					.iterator();
			while (attributeIterator.hasNext()) {
				FeatureAttribute attribute = (FeatureAttribute) attributeIterator
						.next();
				_propertyCmbModel.addElement(attribute);
				if ((attribute.getType().equals("DOUBLE"))
						|| (attribute.getType().equals("INTEGER"))) {
					_propertyNameCmbModel.addElement(attribute);
					_propertyNameCmbModel2.addElement(attribute);
				}
			}
		}
		propertyNameCmb.setModel(_propertyCmbModel);
		attributesCmb.setModel(_propertyNameCmbModel);
		attributesCmb1.setModel(_propertyNameCmbModel2);

		this.layer = (GeopistaLayer) session.getAttribute("Layer");
		propertyNameCmb.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (layer.getFeatureCollectionWrapper().getFeatureSchema() instanceof GeopistaSchema) 
				{
					setCellEditors(layer, true);
				}
			}
		});

		_wellKnownNameCmbModel = new DefaultComboBoxModel();
		_wellKnownNameCmbModel.addElement("square");
		_wellKnownNameCmbModel.addElement("circle");
		_wellKnownNameCmbModel.addElement("triangle");
		_wellKnownNameCmbModel.addElement("cross");
		_wellKnownNameCmbModel.addElement("x");
		wellKnownNameCmb.setModel(_wellKnownNameCmbModel);
		wellKnownNameCmb.setRenderer(new MarkGraphicRenderer());

		_rotationCmbModel = new DefaultComboBoxModel(_rotationValues);
		rotationCmb.setModel(_rotationCmbModel);
		_strokeWidthCmbModel = new DefaultComboBoxModel(_widthValues);
		strokeWidthCmb.setModel(_strokeWidthCmbModel);

		Dictionary labelTable = new Hashtable();
		labelTable.put(new Integer(0), new JLabel("0.0"));
		labelTable.put(new Integer(10), new JLabel("0.1"));
		labelTable.put(new Integer(20), new JLabel("0.2"));
		labelTable.put(new Integer(30), new JLabel("0.3"));
		labelTable.put(new Integer(40), new JLabel("0.4"));
		labelTable.put(new Integer(50), new JLabel("0.5"));
		labelTable.put(new Integer(60), new JLabel("0.6"));
		labelTable.put(new Integer(70), new JLabel("0.7"));
		labelTable.put(new Integer(80), new JLabel("0.8"));
		labelTable.put(new Integer(90), new JLabel("0.9"));
		labelTable.put(new Integer(100), new JLabel("1.0"));
		opacitySld.setLabelTable(labelTable);
		strokeOpacitySld.setLabelTable(labelTable);
		fillOpacitySld.setLabelTable(labelTable);

		Pattern aPattern;
		_patternCmbModel = new DefaultComboBoxModel();
		_patterns = new HashMap();
		Iterator patternIterator = Pattern.createPatterns().iterator();
		while (patternIterator.hasNext()) {
			aPattern = (Pattern) patternIterator.next();
			_patterns.put(aPattern.toString(), aPattern);
			_patternCmbModel.addElement(aPattern);
		}
		dashArrayCmb.setModel(_patternCmbModel);
		dashArrayCmb.setRenderer(new PatternRenderer());

		_markOrGraphicCmbModel = new DefaultComboBoxModel();
		_markOrGraphicCmbModel.addElement(GRAPHIC_PANEL);
		_markOrGraphicCmbModel.addElement(MARK_PANEL);
		markOrGraphicCmb.setModel(_markOrGraphicCmbModel);

		_tableModel = new ThematicTableModel();
		valueListTable.setModel(_tableModel);
		valueListTable.setDefaultRenderer(Color.class, new ColorRenderer(true));
		valueListTable.setDefaultEditor(Color.class, new ColorEditor());
		ListSelectionModel rowSM = valueListTable.getSelectionModel();
		setDefaultWidthValues();
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

	public boolean windowClosing() {
		Request theRequest = FrontControllerFactory.createRequest();
		FrontController fc = FrontControllerFactory.getFrontController();
		Action theAction = fc.getAction("GetBack");
		ActionForward theActionForward = theAction.doExecute(theRequest);
		_container.forward(theActionForward, theRequest);
		return false;
	}

	public String getTitle() {
		return aplicacion.getI18nString("ThematicPointRule");
	}

	/** Creates new form InsertUpdateThematicPointRulePanel */
	public InsertUpdateThematicPointRulePanel() {
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
        opacityLbl = new javax.swing.JLabel();
        opacitySld = new javax.swing.JSlider();
        markOrGraphicPanel = new javax.swing.JPanel();
        markPanel = new javax.swing.JPanel();
        wellKnownNameLbl = new javax.swing.JLabel();
        wellKnownNameCmb = new javax.swing.JComboBox();
        strokeColorLbl = new javax.swing.JLabel();
        strokeColorBtn = new javax.swing.JButton();
        strokeOpacityLbl = new javax.swing.JLabel();
        strokeOpacitySld = new javax.swing.JSlider();
        fillOpacityLbl = new javax.swing.JLabel();
        fillOpacitySld = new javax.swing.JSlider();
        dashArrayLbl = new javax.swing.JLabel();
        dashArrayCmb = new javax.swing.JComboBox();
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
        strokeWidthLbl = new javax.swing.JLabel();
        strokeWidthCmb = new javax.swing.JComboBox();
        arithmeticExprCmb1 = new javax.swing.JComboBox();
        attributesCmb1 = new javax.swing.JComboBox();
        graphicPanel = new javax.swing.JPanel();
        externalGraphicURLLbl = new javax.swing.JLabel();
        externalGraphicURLTxt = new javax.swing.JTextField();
        externalGraphicURLBtn = new javax.swing.JButton();
        externalGraphicFormatLbl = new javax.swing.JLabel();
        externalGraphicFormatTxt = new javax.swing.JTextField();
        markOrGraphicLbl = new javax.swing.JLabel();
        markOrGraphicCmb = new javax.swing.JComboBox();
        rotationLbl = new javax.swing.JLabel();
        sizeLbl = new javax.swing.JLabel();
        attributesCmb = new javax.swing.JComboBox();
        arithmeticExprCmb = new javax.swing.JComboBox();
        sizeTxt = new javax.swing.JTextField();
        rotationCmb = new javax.swing.JComboBox();
        buttonPanel = new javax.swing.JPanel();
        okBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        setMinimumSize(new java.awt.Dimension(349, 600));
        setPreferredSize(new java.awt.Dimension(349, 600));
        thematicPanel.setLayout(new java.awt.GridBagLayout());

        rangeThematicLbl.setText("Por rangos: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
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
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        thematicPanel.add(rangeThematicChk, gridBagConstraints);

        propertyNameLbl.setText("Propiedad: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        thematicPanel.add(propertyNameLbl, gridBagConstraints);

        propertyNameCmb.setPreferredSize(new java.awt.Dimension(27, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        thematicPanel.add(propertyNameCmb, gridBagConstraints);

        valuePanel.setLayout(new java.awt.BorderLayout());

        valuePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Valores: "));
        valuePanel.setMaximumSize(new java.awt.Dimension(600, 142));
        valueListTableScroll.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        valueListTableScroll.setMaximumSize(new java.awt.Dimension(32767, 122));
        valueListTableScroll.setPreferredSize(new java.awt.Dimension(100, 50));
        valueListTableScroll.setViewportView(valueListTable);

        valuePanel.add(valueListTableScroll, java.awt.BorderLayout.CENTER);

        valueButtonsPanel.setLayout(new java.awt.GridBagLayout());

        valueButtonsPanel.setMaximumSize(new java.awt.Dimension(500, 112));
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
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
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
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
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
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
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
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        valueButtonsPanel.add(rampColorBtn, gridBagConstraints);

        valuePanel.add(valueButtonsPanel, java.awt.BorderLayout.EAST);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        thematicPanel.add(valuePanel, gridBagConstraints);

        stylePanel.setLayout(new java.awt.GridBagLayout());

        stylePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Estilo: "));
        stylePanel.setMaximumSize(new java.awt.Dimension(349, 371));
        stylePanel.setMinimumSize(new java.awt.Dimension(349, 371));
        opacityLbl.setText("Opacidad: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 0);
        stylePanel.add(opacityLbl, gridBagConstraints);

        opacitySld.setMajorTickSpacing(10);
        opacitySld.setMinorTickSpacing(5);
        opacitySld.setPaintLabels(true);
        opacitySld.setPaintTicks(true);
        opacitySld.setValue(100);
        opacitySld.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                opacitySldFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        stylePanel.add(opacitySld, gridBagConstraints);

        markOrGraphicPanel.setLayout(new java.awt.CardLayout());

        markPanel.setLayout(new java.awt.GridBagLayout());

        wellKnownNameLbl.setText("Nombre: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 2, 0);
        markPanel.add(wellKnownNameLbl, gridBagConstraints);

        wellKnownNameCmb.setPreferredSize(new java.awt.Dimension(27, 18));
        wellKnownNameCmb.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                wellKnownNameCmbFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 2, 0);
        markPanel.add(wellKnownNameCmb, gridBagConstraints);

        strokeColorLbl.setText("Color l\u00ednea: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        markPanel.add(strokeColorLbl, gridBagConstraints);

        strokeColorBtn.setMinimumSize(new java.awt.Dimension(32, 19));
        strokeColorBtn.setPreferredSize(new java.awt.Dimension(32, 19));
        strokeColorBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                strokeColorBtnActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 0);
        markPanel.add(strokeColorBtn, gridBagConstraints);

        strokeOpacityLbl.setText("Opacidad l\u00ednea: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        markPanel.add(strokeOpacityLbl, gridBagConstraints);

        strokeOpacitySld.setMajorTickSpacing(10);
        strokeOpacitySld.setMinorTickSpacing(5);
        strokeOpacitySld.setPaintLabels(true);
        strokeOpacitySld.setPaintTicks(true);
        strokeOpacitySld.setValue(100);
        strokeOpacitySld.setMaximumSize(new java.awt.Dimension(128, 40));
        strokeOpacitySld.setMinimumSize(new java.awt.Dimension(36, 40));
        strokeOpacitySld.setPreferredSize(new java.awt.Dimension(128, 40));
        strokeOpacitySld.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                strokeOpacitySldFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 5);
        markPanel.add(strokeOpacitySld, gridBagConstraints);

        fillOpacityLbl.setText("Opacidad relleno: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        markPanel.add(fillOpacityLbl, gridBagConstraints);

        fillOpacitySld.setMajorTickSpacing(10);
        fillOpacitySld.setMinorTickSpacing(5);
        fillOpacitySld.setPaintLabels(true);
        fillOpacitySld.setPaintTicks(true);
        fillOpacitySld.setValue(100);
        fillOpacitySld.setMaximumSize(new java.awt.Dimension(128, 40));
        fillOpacitySld.setMinimumSize(new java.awt.Dimension(36, 40));
        fillOpacitySld.setPreferredSize(new java.awt.Dimension(128, 40));
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
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 5);
        markPanel.add(fillOpacitySld, gridBagConstraints);

        dashArrayLbl.setText("Patr\u00f3n l\u00ednea: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        markPanel.add(dashArrayLbl, gridBagConstraints);

        dashArrayCmb.setPreferredSize(new java.awt.Dimension(27, 18));
        dashArrayCmb.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                dashArrayCmbFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        markPanel.add(dashArrayCmb, gridBagConstraints);

        strokeLineCapPanel.setLayout(new java.awt.GridBagLayout());

        strokeLineCapPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Redondez en nodos: "));
        strokeLineCapPanel.setMinimumSize(new java.awt.Dimension(211, 50));
        strokeLineCapPanel.setPreferredSize(new java.awt.Dimension(211, 50));
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        strokeLineCapPanel.add(squareLineCapRBtn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        strokeLineCapPanel.add(buttlineLbl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        strokeLineCapPanel.add(roundlineLbl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        strokeLineCapPanel.add(squarelineLbl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        markPanel.add(strokeLineCapPanel, gridBagConstraints);

        strokeLineJoinPanel.setLayout(new java.awt.GridBagLayout());

        strokeLineJoinPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Redondez en v\u00e9rtices: "));
        strokeLineJoinPanel.setMinimumSize(new java.awt.Dimension(259, 50));
        strokeLineJoinPanel.setPreferredSize(new java.awt.Dimension(259, 50));
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        strokeLineJoinPanel.add(bevelLineJoinRBtn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        strokeLineJoinPanel.add(mitreLineJoinLbl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        strokeLineJoinPanel.add(roundLineJoinLbl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        strokeLineJoinPanel.add(bevelineJoinLbl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        markPanel.add(strokeLineJoinPanel, gridBagConstraints);

        strokeWidthLbl.setText("Ancho l\u00ednea: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        markPanel.add(strokeWidthLbl, gridBagConstraints);

        strokeWidthCmb.setEditable(true);
        strokeWidthCmb.setMinimumSize(new java.awt.Dimension(40, 18));
        strokeWidthCmb.setPreferredSize(new java.awt.Dimension(40, 18));
        strokeWidthCmb.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                strokeWidthCmbFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 5);
        markPanel.add(strokeWidthCmb, gridBagConstraints);

        arithmeticExprCmb1.setMinimumSize(new java.awt.Dimension(60, 18));
        arithmeticExprCmb1.setPreferredSize(new java.awt.Dimension(60, 18));
        arithmeticExprCmb1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                arithmeticExprCmb1FocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 5);
        markPanel.add(arithmeticExprCmb1, gridBagConstraints);

        attributesCmb1.setMinimumSize(new java.awt.Dimension(70, 18));
        attributesCmb1.setPreferredSize(new java.awt.Dimension(70, 18));
        attributesCmb1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                attributesCmb1FocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 3.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        markPanel.add(attributesCmb1, gridBagConstraints);

        markOrGraphicPanel.add(markPanel, "MARK_PANEL");

        graphicPanel.setLayout(new java.awt.GridBagLayout());

        externalGraphicURLLbl.setText("URL: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        graphicPanel.add(externalGraphicURLLbl, gridBagConstraints);

        externalGraphicURLTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                externalGraphicURLTxtFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        graphicPanel.add(externalGraphicURLTxt, gridBagConstraints);

        externalGraphicURLBtn.setText("...");
        externalGraphicURLBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                externalGraphicURLBtnActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        graphicPanel.add(externalGraphicURLBtn, gridBagConstraints);

        externalGraphicFormatLbl.setText("Formato: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        graphicPanel.add(externalGraphicFormatLbl, gridBagConstraints);

        externalGraphicFormatTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                externalGraphicFormatTxtFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        graphicPanel.add(externalGraphicFormatTxt, gridBagConstraints);

        markOrGraphicPanel.add(graphicPanel, "GRAPHIC_PANEL");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        stylePanel.add(markOrGraphicPanel, gridBagConstraints);

        markOrGraphicLbl.setText("Tipo: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 2, 0);
        stylePanel.add(markOrGraphicLbl, gridBagConstraints);

        markOrGraphicCmb.setPreferredSize(new java.awt.Dimension(27, 18));
        markOrGraphicCmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                markOrGraphicCmbActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 5.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 2, 5);
        stylePanel.add(markOrGraphicCmb, gridBagConstraints);

        rotationLbl.setText("Rotaci\u00f3n: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        stylePanel.add(rotationLbl, gridBagConstraints);

        sizeLbl.setText("Tama\u00f1o:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        stylePanel.add(sizeLbl, gridBagConstraints);

        attributesCmb.setMinimumSize(new java.awt.Dimension(70, 18));
        attributesCmb.setPreferredSize(new java.awt.Dimension(70, 18));
        attributesCmb.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                attributesCmbFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 0);
        stylePanel.add(attributesCmb, gridBagConstraints);

        arithmeticExprCmb.setMinimumSize(new java.awt.Dimension(60, 18));
        arithmeticExprCmb.setPreferredSize(new java.awt.Dimension(60, 18));
        arithmeticExprCmb.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                arithmeticExprCmbFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 0);
        stylePanel.add(arithmeticExprCmb, gridBagConstraints);

        sizeTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                sizeTxtFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        stylePanel.add(sizeTxt, gridBagConstraints);

        rotationCmb.setEditable(true);
        rotationCmb.setMinimumSize(new java.awt.Dimension(45, 18));
        rotationCmb.setPreferredSize(new java.awt.Dimension(45, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        stylePanel.add(rotationCmb, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
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

    }// </editor-fold>                        

	
	private void attributesCmb1FocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_attributesCmb1FocusLost
		ThematicTableModel tm = (ThematicTableModel) valueListTable.getModel();
		for (int i = 0; i < _currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);
			if (!attributesCmb1.getSelectedItem().equals("")
					&& !arithmeticExprCmb1.getSelectedItem().equals("")) {
				PropertyName propertyName = new PropertyName(
						((FeatureAttribute) attributesCmb1.getSelectedItem())
								.getName());
				Literal literal = new Literal((String) strokeWidthCmb
						.getSelectedItem());
				String operator = (String) arithmeticExprCmb1.getSelectedItem();
				int operatorID = 0;
				if (operator.equals("suma")) {
					operatorID = ExpressionDefines.ADD;
				} else if (operator.equals("resta")) {
					operatorID = ExpressionDefines.SUB;
				} else if (operator.equals("multiplicación")) {
					operatorID = ExpressionDefines.MUL;
				} else if (operator.equals("división")) {
					operatorID = ExpressionDefines.DIV;
				}
				ArithmeticExpression arithmeticExpression = new ArithmeticExpression(
						operatorID, propertyName, literal);
				aStyle.put("Width", arithmeticExpression);
			} else {
				Literal literal = new Literal((String) strokeWidthCmb
						.getSelectedItem());
				aStyle.put("Width", literal);
			}
		}
	}// GEN-LAST:event_attributesCmb1FocusLost

	private void arithmeticExprCmb1FocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_arithmeticExprCmb1FocusLost
		ThematicTableModel tm = (ThematicTableModel) valueListTable.getModel();
		for (int i = 0; i < _currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);
			if (!attributesCmb1.getSelectedItem().equals("")
					&& !arithmeticExprCmb1.getSelectedItem().equals("")) {
				PropertyName propertyName = new PropertyName(
						((FeatureAttribute) attributesCmb1.getSelectedItem())
								.getName());
				Literal literal = new Literal((String) strokeWidthCmb
						.getSelectedItem());
				String operator = (String) arithmeticExprCmb1.getSelectedItem();
				int operatorID = 0;
				if (operator.equals("suma")) {
					operatorID = ExpressionDefines.ADD;
				} else if (operator.equals("resta")) {
					operatorID = ExpressionDefines.SUB;
				} else if (operator.equals("multiplicación")) {
					operatorID = ExpressionDefines.MUL;
				} else if (operator.equals("división")) {
					operatorID = ExpressionDefines.DIV;
				}
				ArithmeticExpression arithmeticExpression = new ArithmeticExpression(
						operatorID, propertyName, literal);
				aStyle.put("Width", arithmeticExpression);
			} else {
				Literal literal = new Literal((String) strokeWidthCmb
						.getSelectedItem());
				aStyle.put("Width", literal);
			}
		}
	}// GEN-LAST:event_arithmeticExprCmb1FocusLost

	private void strokeWidthCmbFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_strokeWidthCmbFocusLost
		ThematicTableModel tm = (ThematicTableModel) valueListTable.getModel();
		for (int i = 0; i < _currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);
			if (!attributesCmb1.getSelectedItem().equals("")
					&& !arithmeticExprCmb1.getSelectedItem().equals("")) {
				PropertyName propertyName = new PropertyName(
						((FeatureAttribute) attributesCmb1.getSelectedItem())
								.getName());
				Literal literal = new Literal((String) strokeWidthCmb
						.getSelectedItem());
				String operator = (String) arithmeticExprCmb1.getSelectedItem();
				int operatorID = 0;
				if (operator.equals("suma")) {
					operatorID = ExpressionDefines.ADD;
				} else if (operator.equals("resta")) {
					operatorID = ExpressionDefines.SUB;
				} else if (operator.equals("multiplicación")) {
					operatorID = ExpressionDefines.MUL;
				} else if (operator.equals("división")) {
					operatorID = ExpressionDefines.DIV;
				}
				ArithmeticExpression arithmeticExpression = new ArithmeticExpression(
						operatorID, propertyName, literal);
				aStyle.put("Width", arithmeticExpression);
			} else {
				Literal literal = new Literal((String) strokeWidthCmb
						.getSelectedItem());
				aStyle.put("Width", literal);
			}
		}
	}// GEN-LAST:event_strokeWidthCmbFocusLost

	private void sizeTxtFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_sizeTxtFocusLost
		ThematicTableModel tm = (ThematicTableModel) valueListTable.getModel();
		for (int i = 0; i < _currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);
			if (!attributesCmb.getSelectedItem().equals("")
					&& !arithmeticExprCmb.getSelectedItem().equals("")) {
				PropertyName propertyName = new PropertyName(
						((FeatureAttribute) attributesCmb.getSelectedItem())
								.getName());
				Literal literal = new Literal(sizeTxt.getText());
				String operator = (String) arithmeticExprCmb.getSelectedItem();
				int operatorID = 0;
				if (operator.equals("suma")) {
					operatorID = ExpressionDefines.ADD;
				} else if (operator.equals("resta")) {
					operatorID = ExpressionDefines.SUB;
				} else if (operator.equals("multiplicación")) {
					operatorID = ExpressionDefines.MUL;
				} else if (operator.equals("división")) {
					operatorID = ExpressionDefines.DIV;
				}
				ArithmeticExpression arithmeticExpression = new ArithmeticExpression(
						operatorID, propertyName, literal);
				aStyle.put("Size", arithmeticExpression);
			} else {
				Literal literal = new Literal(sizeTxt.getText());
				aStyle.put("Size", literal);
			}
		}
	}// GEN-LAST:event_sizeTxtFocusLost

	private void arithmeticExprCmbFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_arithmeticExprCmbFocusLost
		ThematicTableModel tm = (ThematicTableModel) valueListTable.getModel();
		for (int i = 0; i < _currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);
			if (!attributesCmb.getSelectedItem().equals("")
					&& !arithmeticExprCmb.getSelectedItem().equals("")) {
				PropertyName propertyName = new PropertyName(
						((FeatureAttribute) attributesCmb.getSelectedItem())
								.getName());
				Literal literal = new Literal(sizeTxt.getText());
				String operator = (String) arithmeticExprCmb.getSelectedItem();
				int operatorID = 0;
				if (operator.equals("suma")) {
					operatorID = ExpressionDefines.ADD;
				} else if (operator.equals("resta")) {
					operatorID = ExpressionDefines.SUB;
				} else if (operator.equals("multiplicación")) {
					operatorID = ExpressionDefines.MUL;
				} else if (operator.equals("división")) {
					operatorID = ExpressionDefines.DIV;
				}
				ArithmeticExpression arithmeticExpression = new ArithmeticExpression(
						operatorID, propertyName, literal);
				aStyle.put("Size", arithmeticExpression);
			} else {
				Literal literal = new Literal(sizeTxt.getText());
				aStyle.put("Size", literal);
			}
		}
	}// GEN-LAST:event_arithmeticExprCmbFocusLost

	private void attributesCmbFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_attributesCmbFocusLost
		ThematicTableModel tm = (ThematicTableModel) valueListTable.getModel();
		for (int i = 0; i < _currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);
			if (!attributesCmb.getSelectedItem().equals("")
					&& !arithmeticExprCmb.getSelectedItem().equals("")) {
				PropertyName propertyName = new PropertyName(
						((FeatureAttribute) attributesCmb.getSelectedItem())
								.getName());
				Literal literal = new Literal(sizeTxt.getText());
				String operator = (String) arithmeticExprCmb.getSelectedItem();
				int operatorID = 0;
				if (operator.equals("suma")) {
					operatorID = ExpressionDefines.ADD;
				} else if (operator.equals("resta")) {
					operatorID = ExpressionDefines.SUB;
				} else if (operator.equals("multiplicación")) {
					operatorID = ExpressionDefines.MUL;
				} else if (operator.equals("división")) {
					operatorID = ExpressionDefines.DIV;
				}
				ArithmeticExpression arithmeticExpression = new ArithmeticExpression(
						operatorID, propertyName, literal);
				aStyle.put("Size", arithmeticExpression);
			} else {
				Literal literal = new Literal(sizeTxt.getText());
				aStyle.put("Size", literal);
			}
		}
	}// GEN-LAST:event_attributesCmbFocusLost

	private void externalGraphicFormatTxtFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_externalGraphicFormatTxtFocusLost
		ThematicTableModel tm = (ThematicTableModel) valueListTable.getModel();
		for (int i = 0; i < _currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);
			aStyle.put("Format", externalGraphicFormatTxt.getText());
		}

	}// GEN-LAST:event_externalGraphicFormatTxtFocusLost

	private void externalGraphicURLTxtFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_externalGraphicURLTxtFocusLost
		ThematicTableModel tm = (ThematicTableModel) valueListTable.getModel();
		for (int i = 0; i < _currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);
			aStyle.put("Url", externalGraphicURLTxt.getText());
		}

	}// GEN-LAST:event_externalGraphicURLTxtFocusLost

	private void externalGraphicURLBtnActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_externalGraphicURLBtnActionPerformed
		JFileChooser chooser = new JFileChooser();
		FilterGraphicFiles fileFilter = new FilterGraphicFiles();
		chooser.setFileFilter(fileFilter);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				externalGraphicURLTxt.setText(chooser.getSelectedFile().toURL()
						.toString());
				String graphicURL = chooser.getSelectedFile().toURL()
						.toString();
				GraphicFormatManager graphicFormatManager = new GraphicFormatManager();
				String graphicFormat = graphicFormatManager
						.getFormat(graphicURL);
				externalGraphicFormatTxt.setText(graphicFormat);
				ThematicTableModel tm = (ThematicTableModel) valueListTable
						.getModel();
				for (int i = 0; i < _currentSelection.length; i++) {
					HashMap aStyle = tm.getStyle(_currentSelection[i]);
					aStyle.put("Url", graphicURL);
					aStyle.put("Format", graphicFormat);
				}
			} catch (MalformedURLException e) {
				// TODO: DO something
			}
		}
	}// GEN-LAST:event_externalGraphicURLBtnActionPerformed

	private void markOrGraphicCmbActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_markOrGraphicCmbActionPerformed
		if (_markOrGraphicCmbModel.getSelectedItem().equals(MARK_PANEL)) {
			CardLayout cl = (CardLayout) markOrGraphicPanel.getLayout();
			cl.show(markOrGraphicPanel, "MARK_PANEL");
			ThematicTableModel tm = (ThematicTableModel) valueListTable
					.getModel();
			for (int i = 0; i < _currentSelection.length; i++) {
				HashMap aStyle = tm.getStyle(_currentSelection[i]);
				aStyle.put("SymbolizerType", "PointMark");
			}

		} else {
			CardLayout cl = (CardLayout) markOrGraphicPanel.getLayout();
			cl.show(markOrGraphicPanel, "GRAPHIC_PANEL");
			ThematicTableModel tm = (ThematicTableModel) valueListTable
					.getModel();
			for (int i = 0; i < _currentSelection.length; i++) {
				HashMap aStyle = tm.getStyle(_currentSelection[i]);
				aStyle.put("SymbolizerType", "PointGraphic");
			}
		}
	}// GEN-LAST:event_markOrGraphicCmbActionPerformed

	private void bevelLineJoinRBtnFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_bevelLineJoinRBtnFocusLost
		ThematicTableModel tm = (ThematicTableModel) valueListTable.getModel();
		for (int i = 0; i < _currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);
			if (mitreLineJoinRBtn.isSelected())
				aStyle.put("LineJoin", "bevel");
		}
	}// GEN-LAST:event_bevelLineJoinRBtnFocusLost

	private void roundLineJoinRBtnFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_roundLineJoinRBtnFocusLost
		ThematicTableModel tm = (ThematicTableModel) valueListTable.getModel();
		for (int i = 0; i < _currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);
			if (mitreLineJoinRBtn.isSelected())
				aStyle.put("LineJoin", "round");
		}
	}// GEN-LAST:event_roundLineJoinRBtnFocusLost

	private void mitreLineJoinRBtnFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_mitreLineJoinRBtnFocusLost
		ThematicTableModel tm = (ThematicTableModel) valueListTable.getModel();
		for (int i = 0; i < _currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);
			if (mitreLineJoinRBtn.isSelected())
				aStyle.put("LineJoin", "mitre");
		}
	}// GEN-LAST:event_mitreLineJoinRBtnFocusLost

	private void squareLineCapRBtnFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_squareLineCapRBtnFocusLost
		ThematicTableModel tm = (ThematicTableModel) valueListTable.getModel();
		for (int i = 0; i < _currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);
			if (buttLineCapRBtn.isSelected())
				aStyle.put("LineCap", "square");
		}
	}// GEN-LAST:event_squareLineCapRBtnFocusLost

	private void roundLineCapRBtnFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_roundLineCapRBtnFocusLost
		ThematicTableModel tm = (ThematicTableModel) valueListTable.getModel();
		for (int i = 0; i < _currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);
			if (buttLineCapRBtn.isSelected())
				aStyle.put("LineCap", "round");
		}
	}// GEN-LAST:event_roundLineCapRBtnFocusLost

	private void buttLineCapRBtnFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_buttLineCapRBtnFocusLost
		ThematicTableModel tm = (ThematicTableModel) valueListTable.getModel();
		for (int i = 0; i < _currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);
			if (buttLineCapRBtn.isSelected())
				aStyle.put("LineCap", "butt");
		}
	}// GEN-LAST:event_buttLineCapRBtnFocusLost

	private void dashArrayCmbFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_dashArrayCmbFocusLost
		ThematicTableModel tm = (ThematicTableModel) valueListTable.getModel();
		for (int i = 0; i < _currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);
			if (dashArrayCmb.getSelectedIndex() != 0) {
				aStyle.put("DashArray", ((Pattern) _patternCmbModel
						.getSelectedItem()).getDashArray());
			} else {
				aStyle.put("DashArray", null);
			}
		}
	}// GEN-LAST:event_dashArrayCmbFocusLost

	private void fillOpacitySldFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_fillOpacitySldFocusLost
		ThematicTableModel tm = (ThematicTableModel) valueListTable.getModel();
		for (int i = 0; i < _currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);
			aStyle.put("OpacityFill", new Double(
					fillOpacitySld.getValue() / 100.0));
		}
	}// GEN-LAST:event_fillOpacitySldFocusLost

	private void strokeOpacitySldFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_strokeOpacitySldFocusLost
		ThematicTableModel tm = (ThematicTableModel) valueListTable.getModel();
		for (int i = 0; i < _currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);
			aStyle.put("OpacityStroke", new Double(
					strokeOpacitySld.getValue() / 100.0));
		}
	}// GEN-LAST:event_strokeOpacitySldFocusLost

	private void wellKnownNameCmbFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_wellKnownNameCmbFocusLost
		ThematicTableModel tm = (ThematicTableModel) valueListTable.getModel();
		for (int i = 0; i < _currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);
			aStyle.put("WellKnownName", _wellKnownNameCmbModel
					.getSelectedItem());
		}
	}// GEN-LAST:event_wellKnownNameCmbFocusLost

	private void opacitySldFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_opacitySldFocusLost
		ThematicTableModel tm = (ThematicTableModel) valueListTable.getModel();
		for (int i = 0; i < _currentSelection.length; i++) {
			HashMap aStyle = tm.getStyle(_currentSelection[i]);
			aStyle.put("Opacity", new Double(
					strokeOpacitySld.getValue() / 100.0));
		}
	}// GEN-LAST:event_opacitySldFocusLost

	private void allValuesBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_allValuesBtnActionPerformed
		Session session = FrontControllerFactory.getSession();
		Layer layer = (Layer) session.getAttribute("Layer");

		SortedSet values = UIUtils.getNonNullAttributeValues(layer,
				((FeatureAttribute) _propertyCmbModel.getSelectedItem())
						.getName());
		if (rangeThematicChk.isSelected()) {
			createRangeThematicTableModel(values);
		} else {
			createUniqueThematicTableModel(values);
		}
	}// GEN-LAST:event_allValuesBtnActionPerformed

	private void createUniqueThematicTableModel(SortedSet values) {
		int result = JOptionPane
				.showConfirmDialog(this,
						"Esta opción eliminará todas las clases, y creará "
								+ values.size()
								+ " nuevas clases.\n ¿Desea continuar?",
						"Confirmación", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {
			_tableModel = new ThematicTableModel();
			_tableModel.setRangeModel(false);

			Iterator valueIterator = values.iterator();
			while (valueIterator.hasNext()) {
				Color fillColor = new Color((int) (Math.random() * 255),
						(int) (Math.random() * 255),
						(int) (Math.random() * 255));
				Object minValue = valueIterator.next();
				_tableModel.addRow(new Object[] { fillColor,
						minValue.toString(), minValue.toString(),
						createStyleFromStylePanel(fillColor) });
			}
			valueListTable.setModel(_tableModel);
			if (layer.getFeatureCollectionWrapper().getFeatureSchema() instanceof GeopistaSchema) 
			{
				setCellEditors(layer, false);
			}
		}
	}

	private void createRangeThematicTableModel(SortedSet values) {

		String inputValue = JOptionPane
				.showInputDialog("Introduzca el número de clases que se van a crear");
		if (inputValue != null) {
			int numberOfClasses;
			try {
				numberOfClasses = new Integer(inputValue).intValue();
				if (numberOfClasses < 1) {
					JOptionPane.showMessageDialog(null,
							"El número de clases debe ser mayor o igual que 1");
				} else {
					Object[] classificationMethods = ClassifierManager
							.getMethodNames(values.first());
					String method = (String) JOptionPane.showInputDialog(this,
							"Introduzca el método de clasificación:\n",
							"Método de clasificación",
							JOptionPane.PLAIN_MESSAGE, null,
							classificationMethods, classificationMethods[0]);
					if (method != null) {
						int result = JOptionPane
								.showConfirmDialog(
										this,
										"Se eliminarán todas las clases, y se crearán "
												+ numberOfClasses
												+ " nuevas clases utilizando el método de "
												+ method
												+ ".\n ¿Desea continuar?",
										"Confirmación",
										JOptionPane.YES_NO_OPTION,
										JOptionPane.QUESTION_MESSAGE);
						if (result == JOptionPane.YES_OPTION) {
							_tableModel = new ThematicTableModel();
							_tableModel.setRangeModel(true);
							Classifier classifier = ClassifierManager
									.getClassifier(method);
							if (classifier != null) {
								Set classifiedValues = classifier.classify(
										values, numberOfClasses);
								Iterator valueIterator = classifiedValues
										.iterator();
								while (valueIterator.hasNext()) {
									Color fillColor = new Color((int) (Math
											.random() * 255), (int) (Math
											.random() * 255), (int) (Math
											.random() * 255));
									Object minValue = valueIterator.next();
									_tableModel
											.addRow(new Object[] {
													fillColor,
													minValue.toString(),
													minValue.toString(),
													createStyleFromStylePanel(fillColor) });
								}
								for (int i = 0; i < (_tableModel.getRowCount() - 1); i++) {
									_tableModel.setValueAt(_tableModel
											.getValueAt(i + 1, 1), i, 2);
								}
								_tableModel.setValueAt(
										values.last().toString(), _tableModel
												.getRowCount() - 1, 2);
								valueListTable.setModel(_tableModel);
							}
						}
					}
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null,
						"El valor introducido no es válido");
			}
		}
	}

	private void rangeThematicChkActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_rangeThematicChkActionPerformed
		if (rangeThematicChk.isSelected()) {
			_tableModel.setRangeModel(true);
		} else {
			_tableModel.setRangeModel(false);
		}
	}// GEN-LAST:event_rangeThematicChkActionPerformed

	private void newValueBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_newValueBtnActionPerformed
		Color fillColor = new Color((int) (Math.random() * 255), (int) (Math
				.random() * 255), (int) (Math.random() * 255));
		_tableModel.addRow(new Object[] { fillColor, "", "",
				createStyleFromStylePanel(fillColor) });
		this.valueListTable.addNotify();
	}// GEN-LAST:event_newValueBtnActionPerformed

	private void removeValueBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_removeValueBtnActionPerformed
		int[] selectedRows;

		selectedRows = valueListTable.getSelectedRows();
		for (int i = selectedRows.length - 1; i >= 0; i--) {
			_tableModel.removeRow(selectedRows[i]);
		}
	}// GEN-LAST:event_removeValueBtnActionPerformed

	private void rampColorBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_rampColorBtnActionPerformed
		int rowCount = _tableModel.getRowCount();
		if (rowCount > 0) {
			Color firstColor = (Color) _tableModel.getValueAt(0, 0);
			Color lastColor = (Color) _tableModel.getValueAt(rowCount - 1, 0);
			int firstRed = firstColor.getRed();
			int firstGreen = firstColor.getGreen();
			int firstBlue = firstColor.getBlue();
			int lastRed = lastColor.getRed();
			int lastGreen = lastColor.getGreen();
			int lastBlue = lastColor.getBlue();
			int stepRed = (lastRed - firstRed) / (rowCount - 1);
			int stepGreen = (lastGreen - firstGreen) / (rowCount - 1);
			int stepBlue = (lastBlue - firstBlue) / (rowCount - 1);

			for (int i = 0; i < rowCount; i++) {
				_tableModel.setValueAt(new Color(firstRed + i * stepRed,
						firstGreen + i * stepGreen, firstBlue + i * stepBlue),
						i, 0);
			}
		}
	}// GEN-LAST:event_rampColorBtnActionPerformed

	private void strokeColorBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_strokeColorBtnActionPerformed
		Color newColor = JColorChooser.showDialog(this, "Color de linea",
				strokeColorBtn.getBackground());
		if (newColor != null) {
			strokeColorBtn.setBackground(newColor);
			ThematicTableModel tm = (ThematicTableModel) valueListTable
					.getModel();
			for (int i = 0; i < _currentSelection.length; i++) {
				HashMap aStyle = tm.getStyle(_currentSelection[i]);
				aStyle.put("ColorStroke", new Integer(strokeColorBtn
						.getBackground().getRGB()));
			}
		}
	}// GEN-LAST:event_strokeColorBtnActionPerformed

	private void okBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_okBtnActionPerformed
		if (checkValues()) {
			if (_tableModel.isCorrect()) {
				if (rangeThematicChk.isSelected()) {
					createThematicRange();
				} else {
					createThematicUnique();
				}
			} else {
				JOptionPane.showMessageDialog(this,
						"Las clases del temático no son correctas");
			}
		}
	}// GEN-LAST:event_okBtnActionPerformed

	private boolean checkValues() {

		boolean valuesAreCorrect = true;
		StringBuffer errorMessage = new StringBuffer();
		Session session = FrontControllerFactory.getSession();
		if (!sizeTxt.getText().equals("")) {
			try {
				double size = Double.parseDouble(sizeTxt.getText());
				if (size <= 0) {
					errorMessage
							.append("El tamaño del gráfico debe ser mayor que cero\n");
					valuesAreCorrect = false;
				}
			} catch (NumberFormatException e) {
				errorMessage
						.append("El tamaño del gráfico debe ser numérico\n");
				valuesAreCorrect = false;
			}
		}
		if (!((String) strokeWidthCmb.getSelectedItem()).equals("")) {
			try {
				double stroke = Double.parseDouble((String) strokeWidthCmb
						.getSelectedItem());
				if (stroke <= 0) {
					errorMessage
							.append("El ancho de linea debe ser mayor que cero\n");
					valuesAreCorrect = false;
				}
			} catch (NumberFormatException e) {
				errorMessage.append("El ancho de linea debe ser numérico\n");
				valuesAreCorrect = false;
			}
		}
		if (!((String) rotationCmb.getSelectedItem()).equals("")) {
			try {
				Double.parseDouble((String) rotationCmb.getSelectedItem());
			} catch (NumberFormatException e) {
				errorMessage
						.append("La rotación de un gráfico debe ser numérico\n");
				valuesAreCorrect = false;
			}
		}
		FeatureAttribute attribute = (FeatureAttribute) _propertyCmbModel
				.getSelectedItem();
		String attributeType = attribute.getType();
		if (rangeThematicChk.isSelected()) {
			for (int i = 0; i < _tableModel.getRowCount(); i++) {
				String attributeValue1 = _tableModel.getValueAt(i, 1)
						.toString();
				String attributeValue2 = _tableModel.getValueAt(i, 2)
						.toString();
				if (attributeValue1.equals("")) {
					errorMessage
							.append("Hay que darle un valor al rango mínimo de la regla "
									+ i + "\n");
					valuesAreCorrect = false;
				} else if (!checkAttribute(attributeValue1, attributeType)) {
					errorMessage
							.append("El valor dado al rango mínimo no corresponde con su tipo en la condición "
									+ i + "\n");
					valuesAreCorrect = false;
				}
				if (attributeValue2.equals("")) {
					errorMessage
							.append("Hay que darle un valor al rango máximo de la regla "
									+ i + "\n");
					valuesAreCorrect = false;
				} else if (!checkAttribute(attributeValue2, attributeType)) {
					errorMessage
							.append("El valor dado al rango máximo no corresponde con su tipo en la condición "
									+ i + "\n");
					valuesAreCorrect = false;
				}
				try {
					double minValue = Double.parseDouble(attributeValue1);
					double maxValue = Double.parseDouble(attributeValue2);
					if (minValue > maxValue) {
						errorMessage
								.append("El rango no está bien definido en la regla "
										+ i
										+ ", debido a que el limite inferior es mayor que el superior\n");
						valuesAreCorrect = false;
					}
				} catch (NumberFormatException e) {

				}
			}
		} else {
			for (int i = 0; i < _tableModel.getRowCount(); i++) {
				String attributeValue1 = _tableModel.getValueAt(i, 1)
						.toString();
				if (!checkAttribute(attributeValue1, attributeType)) {
					errorMessage
							.append("El valor dado al atributo no corresponde con su tipo en la condición "
									+ i + "\n");
					valuesAreCorrect = false;
				}
			}
		}

		if (!valuesAreCorrect) {
			JOptionPane.showMessageDialog(null,
					"Los siguientes valores son incorrectos:\n"
							+ errorMessage.toString());
		}
		return valuesAreCorrect;
	}

	private boolean checkAttribute(String attributeValue, String attributeType) {
		boolean correctValue = true;
		if ((attributeType.equals("DOUBLE"))
				|| (attributeType.equals("INTEGER"))) {
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
		theRequest.setAttribute("PropertyName",
				((FeatureAttribute) _propertyCmbModel.getSelectedItem())
						.getName());
		for (int i = 0; i < _tableModel.getRowCount(); i++) {
			HashMap style = _tableModel.getStyle(i);
			Color currentColor = (Color) _tableModel.getValueAt(i, 0);
			if (currentColor != null) {
				int fillColor = ((Integer) style.get("ColorFill")).intValue();
				if (currentColor.getRGB() != fillColor) {
					style.put("ColorFill", new Integer(currentColor.getRGB()));
				}
			}
			styleList.add(style);
			aRange = new ArrayList();
			aRange.add(_tableModel.getValueAt(i, 1).toString());
			aRange.add(_tableModel.getValueAt(i, 2).toString());
			rangeList.add(aRange);
		}
		String mainRuleName = JOptionPane.showInputDialog("Introduzca el nombre para la regla creada");
		theRequest.setAttribute("mainRuleName", mainRuleName);
		theRequest.setAttribute("RangeList", rangeList);
		theRequest.setAttribute("StyleList", styleList);
		FrontController fc = FrontControllerFactory.getFrontController();
		Action theAction = fc.getAction("CreateThematicRangeRule");
		ActionForward theActionForward = theAction.doExecute(theRequest);
		_container.forward(theActionForward, theRequest);
	}

	private void createThematicUnique() {
		Request theRequest = FrontControllerFactory.createRequest();
		List valueList = new ArrayList();
		List styleList = new ArrayList();
		theRequest.setAttribute("PropertyName",
				((FeatureAttribute) _propertyCmbModel.getSelectedItem())
						.getName());
		for (int i = 0; i < _tableModel.getRowCount(); i++) {
			HashMap style = _tableModel.getStyle(i);
			Color currentColor = (Color) _tableModel.getValueAt(i, 0);
			if (currentColor != null) {
				int fillColor = ((Integer) style.get("ColorFill")).intValue();
				if (currentColor.getRGB() != fillColor) {
					style.put("ColorFill", new Integer(currentColor.getRGB()));
				}
			}
			styleList.add(style);
			valueList.add(_tableModel.getValueAt(i, 1).toString());
		}
		String mainRuleName = JOptionPane.showInputDialog("Introduzca el nombre para la regla creada");
		theRequest.setAttribute("mainRuleName", mainRuleName);
		theRequest.setAttribute("ValueList", valueList);
		theRequest.setAttribute("StyleList", styleList);
		FrontController fc = FrontControllerFactory.getFrontController();
		Action theAction = fc.getAction("CreateThematicRule");
		ActionForward theActionForward = theAction.doExecute(theRequest);
		_container.forward(theActionForward, theRequest);
	}

	private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cancelBtnActionPerformed
		Request theRequest = FrontControllerFactory.createRequest();
		FrontController fc = FrontControllerFactory.getFrontController();
		Action theAction = fc.getAction("GetBack");
		ActionForward theActionForward = theAction.doExecute(theRequest);
		_container.forward(theActionForward, theRequest);
	}// GEN-LAST:event_cancelBtnActionPerformed

	private void valueListTableSelectionChanged(ListSelectionEvent e) {
		// Ignore extra messages.
		if (e.getValueIsAdjusting())
			return;

		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		if (lsm.isSelectionEmpty()) {
			// no rows are selected
			_currentSelection = valueListTable.getSelectedRows();
		} else {
			int selectedRow = lsm.getMinSelectionIndex();
			ThematicTableModel tm = (ThematicTableModel) valueListTable
					.getModel();
			Color fillColor = (Color) tm.getValueAt(selectedRow, 0);
			HashMap style = (HashMap) tm.getStyle(selectedRow);
			_currentSelection = valueListTable.getSelectedRows();
			fillStylePanel(style);
			// selectedRow is selected
		}
	}

	private HashMap defaultStyle(Color fillColor) {
		HashMap style = new HashMap();

		style.put("SymbolizerType", "PointMark");
		style.put("ColorFill", new Integer(fillColor.getRGB()));
		style.put("WellKnownName", "square");
		style.put("Size", new Literal("8"));
		style.put("Opacity", new Double(1));
		style.put("Rotation", new Double(0));
		style.put("ColorStroke", new Integer(0));
		style.put("Width", new Literal("1"));
		style.put("OpacityStroke", new Double(1));
		style.put("OpacityFill", new Double(1));
		style.put("DashArray", null);
		style.put("LineCap", "round");
		style.put("LineJoin", "round");
		return style;
	}

	private HashMap createStyleFromStylePanel(Color fillColor) {
		HashMap style = new HashMap();
		if (!(attributesCmb.getSelectedItem().equals(""))
				&& !(arithmeticExprCmb.getSelectedItem().equals(""))) {
			PropertyName propertyName = new PropertyName(
					((FeatureAttribute) attributesCmb.getSelectedItem())
							.getName());
			Literal literal = new Literal(sizeTxt.getText());
			String operator = (String) arithmeticExprCmb.getSelectedItem();
			int operatorID = 0;
			if (operator.equals("suma")) {
				operatorID = ExpressionDefines.ADD;
			} else if (operator.equals("resta")) {
				operatorID = ExpressionDefines.SUB;
			} else if (operator.equals("multiplicación")) {
				operatorID = ExpressionDefines.MUL;
			} else if (operator.equals("división")) {
				operatorID = ExpressionDefines.DIV;
			}
			ArithmeticExpression arithmeticExpression = new ArithmeticExpression(
					operatorID, propertyName, literal);
			style.put("Size", arithmeticExpression);
		} else {
			Literal literal = new Literal(sizeTxt.getText());
			style.put("Size", literal);
		}
		style.put("Opacity", new Double(opacitySld.getValue() / 100.0));
		style.put("Rotation",
				new Double((String) rotationCmb.getSelectedItem()));
		if (((String) markOrGraphicCmb.getSelectedItem()).equals(MARK_PANEL)) {
			style.put("SymbolizerType", "PointMark");
			style.put("ColorFill", new Integer(fillColor.getRGB()));
			style
					.put("WellKnownName", _wellKnownNameCmbModel
							.getSelectedItem());
			style.put("ColorStroke", new Integer(strokeColorBtn.getBackground()
					.getRGB()));
			if (!(attributesCmb1.getSelectedItem().equals(""))
					&& !(arithmeticExprCmb1.getSelectedItem().equals(""))) {
				PropertyName propertyName = new PropertyName(
						((FeatureAttribute) attributesCmb1.getSelectedItem())
								.getName());
				Literal literal = new Literal((String) strokeWidthCmb
						.getSelectedItem());
				String operator = (String) arithmeticExprCmb1.getSelectedItem();
				int operatorID = 0;
				if (operator.equals("suma")) {
					operatorID = ExpressionDefines.ADD;
				} else if (operator.equals("resta")) {
					operatorID = ExpressionDefines.SUB;
				} else if (operator.equals("multiplicación")) {
					operatorID = ExpressionDefines.MUL;
				} else if (operator.equals("división")) {
					operatorID = ExpressionDefines.DIV;
				}
				ArithmeticExpression arithmeticExpression = new ArithmeticExpression(
						operatorID, propertyName, literal);
				style.put("Width", arithmeticExpression);
			} else {
				Literal literal = new Literal((String) strokeWidthCmb
						.getSelectedItem());
				style.put("Width", literal);
			}
			style.put("OpacityStroke", new Double(
					strokeOpacitySld.getValue() / 100.0));
			style.put("OpacityFill", new Double(
					fillOpacitySld.getValue() / 100.0));
			if (dashArrayCmb.getSelectedIndex() != 0) {
				style.put("DashArray", ((Pattern) _patternCmbModel
						.getSelectedItem()).getDashArray());
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
		} else if (((String) markOrGraphicCmb.getSelectedItem())
				.equals(GRAPHIC_PANEL)) {
			style.put("SymbolizerType", "PointGraphic");
			style.put("Url", externalGraphicURLTxt.getText());
			style.put("Format", externalGraphicFormatTxt.getText());
		}
		return style;
	}

	private void fillStylePanel(HashMap style) {
		Pattern aPattern;

		Expression expressionSize = (Expression) style.get("Size");
		configureSize(expressionSize);

		opacitySld.setValue((int) (((Double) style.get("Opacity"))
				.doubleValue() * 100));
		rotationCmb
				.setSelectedItem(((Double) style.get("Rotation")).toString());
		String symbolizerType = (String) style.get("SymbolizerType");
		if (symbolizerType.equals("PointMark")) {
			_markOrGraphicCmbModel.setSelectedItem(MARK_PANEL);
			CardLayout cl = (CardLayout) markOrGraphicPanel.getLayout();
			cl.show(markOrGraphicPanel, "MARK_PANEL");
			_wellKnownNameCmbModel.setSelectedItem((String) style
					.get("WellKnownName"));
			strokeColorBtn.setBackground(new Color(((Integer) style
					.get("ColorStroke")).intValue()));
			Expression expressionWidth = (Expression) style.get("Width");
			configureWidth(expressionWidth);
			strokeOpacitySld.setValue((int) (((Double) style
					.get("OpacityStroke")).doubleValue() * 100));
			String lineCap = (String) style.get("LineCap");
			if (lineCap.equals("butt")) {
				buttLineCapRBtn.setSelected(true);
				roundLineCapRBtn.setSelected(false);
				squareLineCapRBtn.setSelected(false);
			} else if (lineCap.equals("round")) {
				buttLineCapRBtn.setSelected(false);
				roundLineCapRBtn.setSelected(true);
				squareLineCapRBtn.setSelected(false);
			} else if (lineCap.equals("square")) {
				buttLineCapRBtn.setSelected(false);
				roundLineCapRBtn.setSelected(false);
				squareLineCapRBtn.setSelected(true);
			} else {
				buttLineCapRBtn.setSelected(false);
				roundLineCapRBtn.setSelected(true);
				squareLineCapRBtn.setSelected(false);
			}
			String lineJoin = (String) style.get("LineJoin");
			if (lineJoin.equals("bevel")) {
				bevelLineJoinRBtn.setSelected(true);
				mitreLineJoinRBtn.setSelected(false);
				roundLineJoinRBtn.setSelected(false);
			} else if (lineJoin.equals("mitre")) {
				bevelLineJoinRBtn.setSelected(false);
				mitreLineJoinRBtn.setSelected(true);
				roundLineJoinRBtn.setSelected(false);
			} else if (lineJoin.equals("round")) {
				bevelLineJoinRBtn.setSelected(false);
				mitreLineJoinRBtn.setSelected(false);
				roundLineJoinRBtn.setSelected(true);
			} else {
				bevelLineJoinRBtn.setSelected(false);
				mitreLineJoinRBtn.setSelected(false);
				roundLineJoinRBtn.setSelected(true);
			}
			float[] dashArray = (float[]) style.get("DashArray");
			if (dashArray != null) {
				aPattern = (Pattern) _patterns.get(Pattern.toString(dashArray));
				if (aPattern != null) {
					_patternCmbModel.setSelectedItem(aPattern);
				} else {
					// El patrón de línea no es uno de los predefinidos, ponemos
					// uno por defecto
					aPattern = (Pattern) _patterns.get(Pattern
							.toString(new float[] {}));
					_patternCmbModel.setSelectedItem(aPattern);
				}
			} else {
				aPattern = (Pattern) _patterns.get(Pattern
						.toString(new float[] {}));
				_patternCmbModel.setSelectedItem(aPattern);
			}
			fillOpacitySld.setValue((int) (((Double) style.get("OpacityFill"))
					.doubleValue() * 100));
		} else if (symbolizerType.equals("PointGraphic")) {
			_markOrGraphicCmbModel.setSelectedItem(GRAPHIC_PANEL);
			CardLayout cl = (CardLayout) markOrGraphicPanel.getLayout();
			cl.show(markOrGraphicPanel, "GRAPHIC_PANEL");
			externalGraphicURLTxt.setText((String) style.get("Url"));
			externalGraphicFormatTxt.setText((String) style.get("Format"));
		}
	}

	private void configureWidth(Expression expression) {

		if (expression instanceof ArithmeticExpression) {
			ArithmeticExpression arithmeticExpression = (ArithmeticExpression) expression;
			Expression expr1 = arithmeticExpression.getFirstExpression();
			String propertyName = ((PropertyName) expr1).getValue();
			FeatureAttribute fa = getFeatureAttributeSelected(propertyName,
					attributesCmb1);
			attributesCmb1.setSelectedItem(fa);
			int operatorID = arithmeticExpression.getExpressionId();
			switch (operatorID) {
			case ExpressionDefines.ADD: {
				arithmeticExprCmb1.setSelectedItem("suma");
				break;
			}
			case ExpressionDefines.SUB: {
				arithmeticExprCmb1.setSelectedItem("resta");
				break;
			}
			case ExpressionDefines.MUL: {
				arithmeticExprCmb1.setSelectedItem("multiplicación");
				break;
			}
			case ExpressionDefines.DIV: {
				arithmeticExprCmb1.setSelectedItem("división");
				break;
			}
			}
			Expression expr2 = arithmeticExpression.getSecondExpression();
			strokeWidthCmb.setSelectedItem(((Literal) expr2).getValue());
		} else if (expression instanceof Literal) {
			Literal literal = (Literal) expression;
			strokeWidthCmb.setSelectedItem(literal.getValue());
			arithmeticExprCmb1.setSelectedItem("");
			attributesCmb1.setSelectedItem("");
		}
	}

	private void configureSize(Expression expression) {

		if (expression instanceof ArithmeticExpression) {
			ArithmeticExpression arithmeticExpression = (ArithmeticExpression) expression;
			Expression expr1 = arithmeticExpression.getFirstExpression();
			String propertyName = ((PropertyName) expr1).getValue();
			FeatureAttribute fa = getFeatureAttributeSelected(propertyName,
					attributesCmb);
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
			case ExpressionDefines.DIV: {
				arithmeticExprCmb.setSelectedItem("división");
				break;
			}
			}
			Expression expr2 = arithmeticExpression.getSecondExpression();
			sizeTxt.setText(((Literal) expr2).getValue());
		} else if (expression instanceof Literal) {
			Literal literal = (Literal) expression;
			sizeTxt.setText(literal.getValue());
			arithmeticExprCmb.setSelectedItem("");
			attributesCmb.setSelectedItem("");
		}
	}

	private FeatureAttribute getFeatureAttributeSelected(String attribute,
			javax.swing.JComboBox combo) {

		FeatureAttribute faSelected = null;
		int count = combo.getItemCount();
		boolean found = false;
		int i = 0;
		while ((i < count) && !found) {
			Object object = combo.getItemAt(i);
			if (object instanceof FeatureAttribute) {
				FeatureAttribute fa = (FeatureAttribute) object;
				if (fa.getName().equals(attribute)) {
					faSelected = fa;
					found = true;
				} else {
					i++;
				}
			} else {
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

	private int[] _currentSelection = {};

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton allValuesBtn;

	private javax.swing.JComboBox arithmeticExprCmb;

	private javax.swing.JComboBox arithmeticExprCmb1;

	private javax.swing.JComboBox attributesCmb;

	private javax.swing.JComboBox attributesCmb1;

	private javax.swing.JRadioButton bevelLineJoinRBtn;

	private javax.swing.JLabel bevelineJoinLbl;

	private javax.swing.JRadioButton buttLineCapRBtn;

	private javax.swing.JLabel buttlineLbl;

	private javax.swing.JPanel buttonPanel;

	private javax.swing.JButton cancelBtn;

	private javax.swing.JComboBox dashArrayCmb;

	private javax.swing.JLabel dashArrayLbl;

	private javax.swing.JLabel externalGraphicFormatLbl;

	private javax.swing.JTextField externalGraphicFormatTxt;

	private javax.swing.JButton externalGraphicURLBtn;

	private javax.swing.JLabel externalGraphicURLLbl;

	private javax.swing.JTextField externalGraphicURLTxt;

	private javax.swing.JLabel fillOpacityLbl;

	private javax.swing.JSlider fillOpacitySld;

	private javax.swing.JPanel graphicPanel;

	private javax.swing.JComboBox markOrGraphicCmb;

	private javax.swing.JLabel markOrGraphicLbl;

	private javax.swing.JPanel markOrGraphicPanel;

	private javax.swing.JPanel markPanel;

	private javax.swing.JLabel mitreLineJoinLbl;

	private javax.swing.JRadioButton mitreLineJoinRBtn;

	private javax.swing.JButton newValueBtn;

	private javax.swing.JButton okBtn;

	private javax.swing.JLabel opacityLbl;

	private javax.swing.JSlider opacitySld;

	private javax.swing.JComboBox propertyNameCmb;

	private javax.swing.JLabel propertyNameLbl;

	private javax.swing.JButton rampColorBtn;

	private javax.swing.JCheckBox rangeThematicChk;

	private javax.swing.JLabel rangeThematicLbl;

	private javax.swing.JButton removeValueBtn;

	private javax.swing.JComboBox rotationCmb;

	private javax.swing.JLabel rotationLbl;

	private javax.swing.JRadioButton roundLineCapRBtn;

	private javax.swing.JLabel roundLineJoinLbl;

	private javax.swing.JRadioButton roundLineJoinRBtn;

	private javax.swing.JLabel roundlineLbl;

	private javax.swing.JLabel sizeLbl;

	private javax.swing.JTextField sizeTxt;

	private javax.swing.JRadioButton squareLineCapRBtn;

	private javax.swing.JLabel squarelineLbl;

	private javax.swing.JButton strokeColorBtn;

	private javax.swing.JLabel strokeColorLbl;

	private javax.swing.ButtonGroup strokeLineCapGroup; // @jve:decl-index=0:

	private javax.swing.JPanel strokeLineCapPanel;

	private javax.swing.ButtonGroup strokeLineJoinGroup;

	private javax.swing.JPanel strokeLineJoinPanel;

	private javax.swing.JLabel strokeOpacityLbl;

	private javax.swing.JSlider strokeOpacitySld;

	private javax.swing.JComboBox strokeWidthCmb;

	private javax.swing.JLabel strokeWidthLbl;

	private javax.swing.JPanel stylePanel;

	private javax.swing.JPanel thematicPanel;

	private javax.swing.JPanel valueButtonsPanel;

	private javax.swing.JTable valueListTable;

	private javax.swing.JScrollPane valueListTableScroll;

	private javax.swing.JPanel valuePanel;

	private javax.swing.JComboBox wellKnownNameCmb;

	private javax.swing.JLabel wellKnownNameLbl;

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

		MySchema = (GeopistaSchema) layer.getFeatureCollectionWrapper()
				.getFeatureSchema();
		MySchema.setGeopistalayer((IGeopistaLayer) layer);

		JTable table = this.valueListTable;
		attName = (String) (this.propertyNameCmb.getSelectedItem().toString());

		if (bDeleteTable) {
			_tableModel = new ThematicTableModel();
			if (rangeThematicChk.isSelected()) {
				_tableModel.setRangeModel(true);
			} else {
				_tableModel.setRangeModel(false);
			}
			table.setModel(_tableModel);
		}

		if (MySchema instanceof GeopistaSchema
				&& MySchema.getColumnByAttribute(attName).getDomain() != null) {
			domain = MySchema.getColumnByAttribute(attName).getDomain();
			domainType = domain.getType();

			if (domainType == Domain.CODEDENTRY
					|| domainType == Domain.CODEBOOK
					|| domainType == Domain.TREE) {
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
					table.getColumnModel().getColumn(1).setCellRenderer(
							elRender);
					table.repaint();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {

				// Poner Model como default
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
