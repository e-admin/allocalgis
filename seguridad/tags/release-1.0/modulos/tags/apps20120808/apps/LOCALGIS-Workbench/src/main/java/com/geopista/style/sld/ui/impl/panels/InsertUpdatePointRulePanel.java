/*
 * InsertUpdatePointRulePanel.java
 *
 * Created on 2 de agosto de 2004, 10:17
 */
package com.geopista.style.sld.ui.impl.panels;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.prefs.Preferences;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.deegree.graphics.sld.ExternalGraphic;
import org.deegree.graphics.sld.Fill;
import org.deegree.graphics.sld.Graphic;
import org.deegree.graphics.sld.Mark;
import org.deegree.graphics.sld.ParameterValueType;
import org.deegree.graphics.sld.PointSymbolizer;
import org.deegree.graphics.sld.Rule;
import org.deegree.graphics.sld.Stroke;
import org.deegree.graphics.sld.Symbolizer;
import org.deegree.services.wfs.filterencoding.Expression;
import org.deegree.services.wfs.filterencoding.Filter;
import org.deegree.services.wfs.filterencoding.FilterEvaluationException;
import org.deegree_impl.graphics.sld.Graphic_Impl;
import org.deegree_impl.graphics.sld.Stroke_Impl;
import org.deegree_impl.services.wfs.filterencoding.ArithmeticExpression;
import org.deegree_impl.services.wfs.filterencoding.ExpressionDefines;
import org.deegree_impl.services.wfs.filterencoding.Literal;
import org.deegree_impl.services.wfs.filterencoding.PropertyName;

import com.geopista.app.AppContext;
import com.geopista.style.sld.model.impl.FeatureAttribute;
import com.geopista.style.sld.ui.impl.FilterGraphicFiles;
import com.geopista.style.sld.ui.impl.GraphicFormatManager;
import com.geopista.style.sld.ui.impl.MarkGraphicRenderer;
import com.geopista.style.sld.ui.impl.Pattern;
import com.geopista.style.sld.ui.impl.PatternRenderer;
import com.geopista.style.sld.ui.impl.Texture;
import com.geopista.style.sld.ui.impl.UIUtils;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

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
public class InsertUpdatePointRulePanel extends AbstractPanel implements ActionForward {

    /**
	 * 
	 */
	private static final String	ICON_LIB_PREFIX	= "iconlib/";
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	final static String GRAPHIC_PANEL = "Gráfico externo";
	final static String MARK_PANEL = "Símbolo predefinido";
	private DefaultComboBoxModel _rotationCmbModel;
	private static final String[] _rotationValues = {"0.0","45.0","90.0","135.0","180.0","225.0","270.0","315.0","360.0"};
	private DefaultComboBoxModel _strokeWidthCmbModel;
	private static final String[] _widthValues = {"1.0","2.0","3.0","4.0","5.0"};
	private DefaultComboBoxModel _arithmeticOperatorsModel;
	private static final String[] _operatorValues = {"","suma","resta","multiplicación","división"};
	private DefaultComboBoxModel _propertyNameCmbModel;
	private DefaultComboBoxModel _arithmeticOperatorsModel2;
	private DefaultComboBoxModel _propertyNameCmbModel2;
	private JComboBox	iconsComboBox;
			
	public void configure(Request request) {
		
		Session session = FrontControllerFactory.getSession();

		buttlineLbl.setIcon(IconLoader.icon("/com/geopista/ui/images/linecapbuttsmall.jpg"));
		roundlineLbl.setIcon(IconLoader.icon("/com/geopista/ui/images/linecaproundsmall.jpg"));
		squarelineLbl.setIcon(IconLoader.icon("/com/geopista/ui/images/linecapsquaresmall.jpg"));
		mitreLineJoinLbl.setIcon(IconLoader.icon("/com/geopista/ui/images/linejoinmitresmall.jpg"));
		roundLineJoinLbl.setIcon(IconLoader.icon("/com/geopista/ui/images/linejoinroundsmall.jpg"));
		bevelineJoinLbl.setIcon(IconLoader.icon("/com/geopista/ui/images/linejoinbevelsmall.jpg"));

		_markOrGraphicCmbModel = new DefaultComboBoxModel();
		_markOrGraphicCmbModel.addElement(GRAPHIC_PANEL);
		_markOrGraphicCmbModel.addElement(MARK_PANEL);
		markOrGraphicCmb.setModel(_markOrGraphicCmbModel);

		_wellKnownNameCmbModel = new DefaultComboBoxModel();
		_wellKnownNameCmbModel.addElement("square");
		_wellKnownNameCmbModel.addElement("circle");
		_wellKnownNameCmbModel.addElement("triangle");
		_wellKnownNameCmbModel.addElement("cross");
		_wellKnownNameCmbModel.addElement("x");
		wellKnownNameCmb.setModel(_wellKnownNameCmbModel);
		wellKnownNameCmb.setRenderer(new MarkGraphicRenderer());

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
		opacitySld.setLabelTable(labelTable);
		strokeOpacitySld.setLabelTable(labelTable);
		fillOpacitySld.setLabelTable(labelTable);
		
		_rotationCmbModel = new DefaultComboBoxModel(_rotationValues);	
		rotationCmb.setModel(_rotationCmbModel);	
		_strokeWidthCmbModel = new DefaultComboBoxModel(_widthValues);	
		strokeWidthCmb.setModel(_strokeWidthCmbModel);	
		_arithmeticOperatorsModel = new DefaultComboBoxModel(_operatorValues);	
		arithmeticExprCmb.setModel(_arithmeticOperatorsModel);	
		_propertyNameCmbModel = new DefaultComboBoxModel();
		_arithmeticOperatorsModel2 = new DefaultComboBoxModel(_operatorValues);	
		arithmeticExprCmb1.setModel(_arithmeticOperatorsModel2);	
		_propertyNameCmbModel2 = new DefaultComboBoxModel();

		ruleNameTxt.setText((String)session.getAttribute("RuleName"));
		Rule aRule = (Rule)session.getAttribute("Rule");
		filterChk.setSelected(false);
		if (session.getAttribute("RuleFilter") != null) {
			Filter filter = (Filter)session.getAttribute("RuleFilter");
			filterTxt.setText(UIUtils.createStringFromFilter(filter));
			filterChk.setSelected(true);	
		} 
		HashMap featureAttributesMap = (HashMap)session.getAttribute("FeatureAttributes");
		Set featureNameSet = featureAttributesMap.keySet();
		Iterator featureNameIterator = featureNameSet.iterator();
		String featureName = (String)featureNameIterator.next();
		List attributeList = (List)featureAttributesMap.get(featureName);
		Iterator attributeIterator = attributeList.iterator();
		_propertyNameCmbModel.addElement("");
		_propertyNameCmbModel2.addElement("");
		while (attributeIterator.hasNext()) {
			FeatureAttribute attribute = (FeatureAttribute)attributeIterator.next();
			if ((attribute.getType().equals("DOUBLE"))||(attribute.getType().equals("INTEGER"))) {
				_propertyNameCmbModel.addElement(attribute);
				_propertyNameCmbModel2.addElement(attribute); 	
			}
		}
		attributesCmb.setModel(_propertyNameCmbModel);
		attributesCmb1.setModel(_propertyNameCmbModel2);

		Symbolizer[] symbolizers = aRule.getSymbolizers();
		if (symbolizers.length >= 1) {
			if (symbolizers.length > 1) {
				JOptionPane.showMessageDialog(null, "Los simbolizadores adicionales de la regla van a ser descartados\nEste interfaz no puede modificar este estilo\nLa definición va a ser alterada");
			}
			Symbolizer symbolizer = symbolizers[0];
			if (symbolizer instanceof PointSymbolizer) {
				Graphic graphic = ((PointSymbolizer)symbolizer).getGraphic();
				try {
					Graphic_Impl graphicImpl = (Graphic_Impl)graphic;
					ParameterValueType size = graphicImpl.getSize();
					Object[] components = size.getComponents();
					configureSize(components[0],attributeList);
					opacitySld.setValue((int)(graphic.getOpacity(null) * 100));
					rotationCmb.setSelectedItem(new Double(graphic.getRotation(null)).toString());
				}
				catch (FilterEvaluationException e){
					JOptionPane.showMessageDialog(null, "Error en la evaluación del filtro de la regla\nEste interfaz no puede modificar este estilo\nLa definición va a ser alterada");
					setDefaultValues();
				}
				Object[] marksAndGraphics = graphic.getMarksAndExtGraphics();
				if (marksAndGraphics != null) {
					if (marksAndGraphics.length >= 1) {
						if (marksAndGraphics.length > 1) {
							JOptionPane.showMessageDialog(null, "Los símbolos adicionales de la definición van a ser descartados\nEste interfaz no puede modificar este estilo\nLa definición va a ser alterada");
						}
						Object aMarkOrAGraphic = marksAndGraphics[0];
						if (aMarkOrAGraphic instanceof ExternalGraphic) { 
							configure((ExternalGraphic)aMarkOrAGraphic);
						}
						else {
							configure((Mark)aMarkOrAGraphic,attributeList);
						}
					}
					else {
						// Regla sin símbolos. Valores por defecto
						setDefaultValues();
						setDefaultSizeValues();
					}
				}				
			}
			else {
				JOptionPane.showMessageDialog(null, "El simbolizador de la regla no es un PointSymbolizer\nEste interfaz no puede modificar este estilo\nLa definición va a ser alterada");
				setDefaultValues();
			} 
		}
		else {
			// Regla sin simbolizador. Valores por defecto
			setDefaultValues();
			setDefaultSizeValues();
		}
	}

	private void setDefaultValues() {
		CardLayout cl = (CardLayout)markOrGraphicPanel.getLayout();
		cl.show(markOrGraphicPanel, "MARK_PANEL");
		_markOrGraphicCmbModel.setSelectedItem(MARK_PANEL);
		_wellKnownNameCmbModel.setSelectedItem("square");
		sizeTxt.setText("5.0");
		opacitySld.setValue(100);
		rotationCmb.setSelectedItem("0.0");
		setDefaultStrokeValues();
		setDefaultFillValues();
	}
	
	private void setDefaultSizeValues() {
		
		attributesCmb.setSelectedItem("");
		arithmeticExprCmb.setSelectedItem("");
		attributesCmb1.setSelectedItem("");
		arithmeticExprCmb1.setSelectedItem("");
		
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

	private void configureSize(Object object,List attributeList) {
		
		if (object instanceof ArithmeticExpression) {
			ArithmeticExpression arithmeticExpression = (ArithmeticExpression)object;
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
			sizeTxt.setText(((Literal)expr2).getValue());
		}
		else if (object instanceof Literal) {
			Literal literal = (Literal)object;
			sizeTxt.setText(literal.getValue());
		}
		else if (object instanceof String) {
			String size = (String)object;
			sizeTxt.setText(size);
		}
	}
	
	private void configure(Mark aMark,List attributeList) {
		Pattern aPattern;
		CardLayout cl = (CardLayout)markOrGraphicPanel.getLayout();
		cl.show(markOrGraphicPanel, "MARK_PANEL");
		_markOrGraphicCmbModel.setSelectedItem(MARK_PANEL);
		_wellKnownNameCmbModel.setSelectedItem(aMark.getWellKnownName());
		Stroke stroke = aMark.getStroke();
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

				strokeOpacitySld.setValue((int)(stroke.getOpacity(null)*100));
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
		Fill fill = aMark.getFill();
		if (fill != null) {
			try {
				fillColorBtn.setBackground(fill.getFill(null));
				fillOpacitySld.setValue((int)(fill.getOpacity(null)*100));
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
					_propertyNameCmbModel2.setSelectedItem(fa);
				}
			}
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
				case ExpressionDefines.DIV : {
					arithmeticExprCmb1.setSelectedItem("división");
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

	private void configure(ExternalGraphic anExternalGraphic) {
		CardLayout cl = (CardLayout)markOrGraphicPanel.getLayout();
		cl.show(markOrGraphicPanel, "GRAPHIC_PANEL");
		_markOrGraphicCmbModel.setSelectedItem(GRAPHIC_PANEL);
		externalGraphicURLTxt.setText(anExternalGraphic.getOnlineResource().toString());
		externalGraphicFormatTxt.setText(anExternalGraphic.getFormat());
		// Intenta preseleccionar el icono en el JList
		String path= anExternalGraphic.getOnlineResource().getPath();
		String value = aplicacion.getUserPreference(Texture.TEXTURES_DIRECTORY_PARAMETER, "",false);
		// obtiene directorio de texturas en caso contrario el actual de la aplicación
		File file = new File(new File(value),path);
        iconsList.setSelectedValue(file,true);
        
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
			return aplicacion.getI18nString("PointRule");		
	}
    
    /** Creates new form InsertUpdatePointRulePanel */
    public InsertUpdatePointRulePanel() {
        initComponents();
        // Test change by JP
        GridBagConstraints gridBagConstraints=new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        graphicPanel.add(getIconLibCombo(), gridBagConstraints);
        
        gridBagConstraints=new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        graphicPanel.add(new JLabel("Icono Predefinido"), gridBagConstraints);
    }
    
    
    /**
	 * @return
	 */
	private JComponent getIconLibCombo()
	{
	if (scroll == null)
		{
		String GEOPISTA_PACKAGE = "/com/geopista/app";
		Preferences pref = Preferences.userRoot().node(GEOPISTA_PACKAGE);
		String value = pref.get(Texture.TEXTURES_DIRECTORY_PARAMETER, ".");
		File dir = new File(value + File.separator+ ICON_LIB_PREFIX);
		if (!dir.isDirectory() || !dir.exists())
			{
			System.out.println("No encuentro el directorio:"+dir.getAbsolutePath());
			String [] mesg=new String[]{"Instale librería de símbolos."};
			iconsList = new JList(mesg);
			scroll = new JScrollPane();
			scroll.setSize(60,100);
			scroll.getViewport().add(iconsList);
			iconsList.setEnabled(false);
			}
		else
			{
		    
		    java.io.FileFilter directories = new java.io.FileFilter(){
		        public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return false;
                    }
                    
                   String extension = GeopistaFunctionUtils.getExtension(f);
                   if(extension==null) extension="";
                   if((extension.toUpperCase().equalsIgnoreCase("jpg")) || (extension.toUpperCase().equalsIgnoreCase("gif"))
                       || (extension.toUpperCase().equalsIgnoreCase("jpeg")) || (extension.toUpperCase().equalsIgnoreCase("png")))     
                           
                   {
                       return true;
                   }
                   else
                   {
                       return false;
                   }

                       
                }

                public String getDescription()
                {
                    // TODO Auto-generated method stub
                    return null;
                }

		    };
		    
			File[] iconos = dir.listFiles(directories);
//			iconsComboBox = new JComboBox(iconos);
			iconsList = new JList(iconos);
			iconsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			scroll = new JScrollPane();
			scroll.setSize(60,100);
			scroll.getViewport().add(iconsList);
			
			ListCellRenderer renderer=new ListCellRenderer()
			{
			JLabel	labl= new JLabel();;
			JPanel pan=new JPanel();
			
			public Component getListCellRendererComponent(JList arg0,
					Object arg1, int arg2, boolean isSelected, 
					boolean hasFocus)
			{
			
			ImageIcon imgIcon=new ImageIcon(((File) arg1)
					.getAbsolutePath());
			labl
			.setIcon(GUIUtil.toSmallIcon(imgIcon));
			
			
			String filename = ((File) arg1).getName();
			labl.setText(filename.substring(0, filename.length() - 4));
			pan.setLayout(new BorderLayout());
			 pan.setBackground(isSelected || hasFocus ? Color.blue : Color.white);
	         labl.setForeground(isSelected ? Color.white : Color.black);
	         pan.add(labl,BorderLayout.CENTER);
			return pan;
			}
			};
			
			
			iconsList.setCellRenderer(renderer);
//			iconsComboBox.setRenderer(renderer);
			
			}
		
		iconsList.addListSelectionListener(new ListSelectionListener()
			{
			public void valueChanged(ListSelectionEvent arg0)
				{
				String graphicURL = ((File)iconsList.getSelectedValue()).getName();
				
				externalGraphicURLTxt.setText("file:"+ICON_LIB_PREFIX+graphicURL);
				GraphicFormatManager graphicFormatManager = new GraphicFormatManager();
				String graphicFormat = graphicFormatManager.getFormat(graphicURL);
				externalGraphicFormatTxt.setText(graphicFormat);
				
				}
		
			});
		
		}
	return scroll;
	}

	/** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Código Generado  ">                          
    // <editor-fold defaultstate="collapsed" desc=" Código Generado  ">                          
	   private void initComponents() {
	        java.awt.GridBagConstraints gridBagConstraints;

	        strokeLineCapGroup = new javax.swing.ButtonGroup();
	        strokeLineJoinGroup = new javax.swing.ButtonGroup();
	        rulePanel = new javax.swing.JPanel();
	        ruleNameLbl = new javax.swing.JLabel();
	        ruleNameTxt = new javax.swing.JTextField();
	        stylePanel = new javax.swing.JPanel();
	        markOrGraphicLbl = new javax.swing.JLabel();
	        markOrGraphicCmb = new javax.swing.JComboBox();
	        opacityLbl = new javax.swing.JLabel();
	        opacitySld = new javax.swing.JSlider();
	        rotationLbl = new javax.swing.JLabel();
	        markOrGraphicPanel = new javax.swing.JPanel();
	        markPanel = new javax.swing.JPanel();
	        wellKnownNameLbl = new javax.swing.JLabel();
	        wellKnownNameCmb = new javax.swing.JComboBox();
	        strokeColorLbl = new javax.swing.JLabel();
	        strokeColorBtn = new javax.swing.JButton();
	        fillColorLbl = new javax.swing.JLabel();
	        fillColorBtn = new javax.swing.JButton();
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
	        rotationCmb = new javax.swing.JComboBox();
	        attributesCmb = new javax.swing.JComboBox();
	        arithmeticExprCmb = new javax.swing.JComboBox();
	        sizeTxt = new javax.swing.JTextField();
	        sizeLbl = new javax.swing.JLabel();
	        filterPanel = new javax.swing.JPanel();
	        filterChk = new javax.swing.JCheckBox();
	        filterBtn = new javax.swing.JButton();
	        filterTxt = new javax.swing.JTextField();
	        buttonPanel = new javax.swing.JPanel();
	        okBtn = new javax.swing.JButton();
	        cancelBtn = new javax.swing.JButton();

	        setLayout(new java.awt.BorderLayout());

	        setPreferredSize(new java.awt.Dimension(400, 601));
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

	        stylePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Estilo: "));
	        markOrGraphicLbl.setText("Tipo: ");
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 0;
	        gridBagConstraints.gridy = 0;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
	        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 0);
	        stylePanel.add(markOrGraphicLbl, gridBagConstraints);

	        markOrGraphicCmb.setMinimumSize(new java.awt.Dimension(100, 18));
	        markOrGraphicCmb.setPreferredSize(new java.awt.Dimension(100, 18));
	        markOrGraphicCmb.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                markOrGraphicCmbActionPerformed(evt);
	            }
	        });

	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 1;
	        gridBagConstraints.gridy = 0;
	        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	        gridBagConstraints.weightx = 2.0;
	        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 0);
	        stylePanel.add(markOrGraphicCmb, gridBagConstraints);

	        opacityLbl.setText("Opacidad: ");
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 0;
	        gridBagConstraints.gridy = 2;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
	        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
	        stylePanel.add(opacityLbl, gridBagConstraints);

	        opacitySld.setMajorTickSpacing(10);
	        opacitySld.setMinorTickSpacing(5);
	        opacitySld.setPaintLabels(true);
	        opacitySld.setPaintTicks(true);
	        opacitySld.setValue(100);
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 1;
	        gridBagConstraints.gridy = 2;
	        gridBagConstraints.gridwidth = 3;
	        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	        gridBagConstraints.weightx = 1.0;
	        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 5);
	        stylePanel.add(opacitySld, gridBagConstraints);

	        rotationLbl.setText("Rotaci\u00f3n: ");
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 2;
	        gridBagConstraints.gridy = 0;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
	        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 0);
	        stylePanel.add(rotationLbl, gridBagConstraints);

	        markOrGraphicPanel.setLayout(new java.awt.CardLayout());

	        markPanel.setLayout(new java.awt.GridBagLayout());

	        markPanel.setMaximumSize(new java.awt.Dimension(2147483647, 314));
	        markPanel.setMinimumSize(new java.awt.Dimension(329, 314));
	        markPanel.setPreferredSize(new java.awt.Dimension(329, 314));
	        wellKnownNameLbl.setText("Nombre: ");
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 0;
	        gridBagConstraints.gridy = 0;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
	        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
	        markPanel.add(wellKnownNameLbl, gridBagConstraints);

	        wellKnownNameCmb.setMaximumSize(new java.awt.Dimension(32767, 18));
	        wellKnownNameCmb.setPreferredSize(new java.awt.Dimension(27, 18));
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 1;
	        gridBagConstraints.gridy = 0;
	        gridBagConstraints.gridwidth = 3;
	        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	        gridBagConstraints.weightx = 1.0;
	        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
	        markPanel.add(wellKnownNameCmb, gridBagConstraints);

	        strokeColorLbl.setText("Color l\u00ednea: ");
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 0;
	        gridBagConstraints.gridy = 3;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
	        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
	        markPanel.add(strokeColorLbl, gridBagConstraints);

	        strokeColorBtn.setMinimumSize(new java.awt.Dimension(32, 19));
	        strokeColorBtn.setPreferredSize(new java.awt.Dimension(32, 19));
	        strokeColorBtn.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                strokeColorBtnActionPerformed(evt);
	            }
	        });

	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 1;
	        gridBagConstraints.gridy = 3;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	        gridBagConstraints.weightx = 1.0;
	        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
	        markPanel.add(strokeColorBtn, gridBagConstraints);

	        fillColorLbl.setText("Color relleno: ");
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 2;
	        gridBagConstraints.gridy = 3;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
	        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
	        markPanel.add(fillColorLbl, gridBagConstraints);

	        fillColorBtn.setMinimumSize(new java.awt.Dimension(32, 19));
	        fillColorBtn.setPreferredSize(new java.awt.Dimension(32, 19));
	        fillColorBtn.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                fillColorBtnActionPerformed(evt);
	            }
	        });

	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 3;
	        gridBagConstraints.gridy = 3;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	        gridBagConstraints.weightx = 1.0;
	        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
	        markPanel.add(fillColorBtn, gridBagConstraints);

	        strokeOpacityLbl.setText("Opacidad l\u00ednea: ");
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 0;
	        gridBagConstraints.gridy = 4;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
	        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
	        markPanel.add(strokeOpacityLbl, gridBagConstraints);

	        strokeOpacitySld.setMajorTickSpacing(10);
	        strokeOpacitySld.setMinorTickSpacing(5);
	        strokeOpacitySld.setPaintLabels(true);
	        strokeOpacitySld.setPaintTicks(true);
	        strokeOpacitySld.setValue(100);
	        strokeOpacitySld.setMaximumSize(new java.awt.Dimension(32767, 40));
	        strokeOpacitySld.setMinimumSize(new java.awt.Dimension(36, 40));
	        strokeOpacitySld.setPreferredSize(new java.awt.Dimension(200, 40));
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 1;
	        gridBagConstraints.gridy = 4;
	        gridBagConstraints.gridwidth = 3;
	        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	        gridBagConstraints.weightx = 1.0;
	        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
	        markPanel.add(strokeOpacitySld, gridBagConstraints);

	        fillOpacityLbl.setText("Opacidad relleno: ");
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 0;
	        gridBagConstraints.gridy = 5;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
	        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
	        markPanel.add(fillOpacityLbl, gridBagConstraints);

	        fillOpacitySld.setMajorTickSpacing(10);
	        fillOpacitySld.setMinorTickSpacing(5);
	        fillOpacitySld.setPaintLabels(true);
	        fillOpacitySld.setPaintTicks(true);
	        fillOpacitySld.setValue(100);
	        fillOpacitySld.setMaximumSize(new java.awt.Dimension(32767, 40));
	        fillOpacitySld.setMinimumSize(new java.awt.Dimension(36, 40));
	        fillOpacitySld.setPreferredSize(new java.awt.Dimension(200, 40));
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 1;
	        gridBagConstraints.gridy = 5;
	        gridBagConstraints.gridwidth = 3;
	        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	        gridBagConstraints.weightx = 1.0;
	        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
	        markPanel.add(fillOpacitySld, gridBagConstraints);

	        dashArrayLbl.setText("Patr\u00f3n l\u00ednea: ");
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 0;
	        gridBagConstraints.gridy = 2;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
	        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
	        markPanel.add(dashArrayLbl, gridBagConstraints);

	        dashArrayCmb.setMaximumSize(new java.awt.Dimension(32767, 18));
	        dashArrayCmb.setPreferredSize(new java.awt.Dimension(27, 18));
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 1;
	        gridBagConstraints.gridy = 2;
	        gridBagConstraints.gridwidth = 3;
	        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	        gridBagConstraints.weightx = 1.0;
	        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
	        markPanel.add(dashArrayCmb, gridBagConstraints);

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
	        gridBagConstraints.gridy = 6;
	        gridBagConstraints.gridwidth = 4;
	        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
	        markPanel.add(strokeLineCapPanel, gridBagConstraints);

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
	        gridBagConstraints.gridy = 7;
	        gridBagConstraints.gridwidth = 4;
	        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
	        markPanel.add(strokeLineJoinPanel, gridBagConstraints);

	        strokeWidthLbl.setText("Ancho: ");
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 0;
	        gridBagConstraints.gridy = 1;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
	        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
	        markPanel.add(strokeWidthLbl, gridBagConstraints);

	        strokeWidthCmb.setEditable(true);
	        strokeWidthCmb.setMaximumSize(new java.awt.Dimension(32767, 18));
	        strokeWidthCmb.setMinimumSize(new java.awt.Dimension(40, 18));
	        strokeWidthCmb.setPreferredSize(new java.awt.Dimension(40, 18));
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 1;
	        gridBagConstraints.gridy = 1;
	        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	        gridBagConstraints.weightx = 1.0;
	        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
	        markPanel.add(strokeWidthCmb, gridBagConstraints);

	        arithmeticExprCmb1.setMaximumSize(new java.awt.Dimension(32767, 18));
	        arithmeticExprCmb1.setMinimumSize(new java.awt.Dimension(60, 18));
	        arithmeticExprCmb1.setPreferredSize(new java.awt.Dimension(60, 18));
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 2;
	        gridBagConstraints.gridy = 1;
	        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	        gridBagConstraints.weightx = 2.0;
	        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
	        markPanel.add(arithmeticExprCmb1, gridBagConstraints);

	        attributesCmb1.setMaximumSize(new java.awt.Dimension(32767, 18));
	        attributesCmb1.setMinimumSize(new java.awt.Dimension(70, 18));
	        attributesCmb1.setPreferredSize(new java.awt.Dimension(70, 18));
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 3;
	        gridBagConstraints.gridy = 1;
	        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	        gridBagConstraints.weightx = 3.0;
	        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
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
	        gridBagConstraints.gridwidth = 4;
	        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
	        gridBagConstraints.weighty = 1.0;
	        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 5);
	        stylePanel.add(markOrGraphicPanel, gridBagConstraints);

	        rotationCmb.setEditable(true);
	        rotationCmb.setMaximumSize(new java.awt.Dimension(100, 32767));
	        rotationCmb.setMinimumSize(new java.awt.Dimension(50, 18));
	        rotationCmb.setPreferredSize(new java.awt.Dimension(50, 18));
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 3;
	        gridBagConstraints.gridy = 0;
	        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 0);
	        stylePanel.add(rotationCmb, gridBagConstraints);

	        attributesCmb.setPreferredSize(new java.awt.Dimension(27, 18));
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 1;
	        gridBagConstraints.gridy = 1;
	        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	        gridBagConstraints.weightx = 2.0;
	        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 0);
	        stylePanel.add(attributesCmb, gridBagConstraints);

	        arithmeticExprCmb.setPreferredSize(new java.awt.Dimension(27, 18));
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 2;
	        gridBagConstraints.gridy = 1;
	        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	        gridBagConstraints.weightx = 1.0;
	        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 0);
	        stylePanel.add(arithmeticExprCmb, gridBagConstraints);

	        sizeTxt.setMinimumSize(new java.awt.Dimension(11, 18));
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 3;
	        gridBagConstraints.gridy = 1;
	        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
	        stylePanel.add(sizeTxt, gridBagConstraints);

	        sizeLbl.setText("Tama\u00f1o:");
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.gridx = 0;
	        gridBagConstraints.gridy = 1;
	        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
	        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
	        stylePanel.add(sizeLbl, gridBagConstraints);

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
	        filterPanel.setMaximumSize(new java.awt.Dimension(2147483647, 63));
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

	    }// </editor-fold>                        

	
    private void filterBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterBtnActionPerformed
		if (filterChk.isSelected()) {

			Request theRequest = FrontControllerFactory.createRequest();
			theRequest.setAttribute("PageInvocator","InsertUpdatePointRule");
			FrontController fc =  FrontControllerFactory.getFrontController();
			Action theAction = fc.getAction("UpdateRuleFilter"); 
			ActionForward theActionForward = theAction.doExecute(theRequest);
			_container.forward(theActionForward, theRequest);			

		}

    }//GEN-LAST:event_filterBtnActionPerformed

    private void filterChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterChkActionPerformed
		if (filterChk.isSelected()) {
			Request theRequest = FrontControllerFactory.createRequest();
			theRequest.setAttribute("PageInvocator","InsertUpdatePointRule");
			FrontController fc =  FrontControllerFactory.getFrontController();
			Action theAction = fc.getAction("InsertRuleFilter"); 
			ActionForward theActionForward = theAction.doExecute(theRequest);
			_container.forward(theActionForward, theRequest);			
		}
		else {
			Request theRequest = FrontControllerFactory.createRequest();
			theRequest.setAttribute("PageInvocator","InsertUpdatePointRule");
			FrontController fc =  FrontControllerFactory.getFrontController();
			Action theAction = fc.getAction("DeleteRuleFilter"); 
			ActionForward theActionForward = theAction.doExecute(theRequest);
			_container.forward(theActionForward, theRequest);			

		}

    }//GEN-LAST:event_filterChkActionPerformed

    private void externalGraphicURLBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_externalGraphicURLBtnActionPerformed
		JFileChooser chooser = new JFileChooser();
		FilterGraphicFiles fileFilter = new FilterGraphicFiles();
		chooser.setFileFilter(fileFilter);
		chooser.setCurrentDirectory(new File(""));
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				externalGraphicURLTxt.setText(chooser.getSelectedFile().toURL().toString());
				String graphicURL = chooser.getSelectedFile().toURL().toString();
				GraphicFormatManager graphicFormatManager = new GraphicFormatManager();
				String graphicFormat = graphicFormatManager.getFormat(graphicURL);
				externalGraphicFormatTxt.setText(graphicFormat);
				iconsList.clearSelection();
			}
			catch (MalformedURLException e) {
				//TODO: DO something
			}
		}
    }//GEN-LAST:event_externalGraphicURLBtnActionPerformed

    private void markOrGraphicCmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_markOrGraphicCmbActionPerformed
    	if (_markOrGraphicCmbModel.getSelectedItem().equals(MARK_PANEL)) {
			CardLayout cl = (CardLayout)markOrGraphicPanel.getLayout();
			cl.show(markOrGraphicPanel, "MARK_PANEL");
    	}
    	else {
			CardLayout cl = (CardLayout)markOrGraphicPanel.getLayout();
			cl.show(markOrGraphicPanel, "GRAPHIC_PANEL");
    	}
    }//GEN-LAST:event_markOrGraphicCmbActionPerformed

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
			if (_markOrGraphicCmbModel.getSelectedItem().equals(MARK_PANEL)) {
				createMark();
			}
			else {
				createGraphic();
			}
		}
    }//GEN-LAST:event_okBtnActionPerformed

	private boolean checkValues() {
		
		boolean valuesAreCorrect = true;
		StringBuffer errorMessage = new StringBuffer();
		Session session = FrontControllerFactory.getSession();
		if (!sizeTxt.getText().equals("")) {
			try {
				double size = Double.parseDouble(sizeTxt.getText());	
				if (size <= 0) {
					errorMessage.append("El tamaño del gráfico debe ser mayor que cero\n");
					valuesAreCorrect = false;		
				}
			}
			catch (NumberFormatException e) {
				errorMessage.append("El tamaño del gráfico debe ser numérico\n");
				valuesAreCorrect = false;
			}
		}
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
		if (!((String)rotationCmb.getSelectedItem()).equals("")) {
			try {
				Double.parseDouble((String)rotationCmb.getSelectedItem());
			}
			catch (NumberFormatException e) {
				errorMessage.append("La rotación de un gráfico debe ser numérico\n");
				valuesAreCorrect = false;
			}
		}
		if (!valuesAreCorrect) {
			JOptionPane.showMessageDialog(null, "Los siguientes valores son incorrectos:\n" + errorMessage.toString());
		}
		return valuesAreCorrect;
	}

	private void createMark() {
		Request theRequest = FrontControllerFactory.createRequest();
		theRequest.setAttribute("RuleName", ruleNameTxt.getText());
		HashMap style = new HashMap();
		style.put("SymbolizerType", "PointMark");
		style.put("WellKnownName", _wellKnownNameCmbModel.getSelectedItem());
		if (!(attributesCmb.getSelectedItem().equals(""))&&!(arithmeticExprCmb.getSelectedItem().equals(""))) {
			PropertyName propertyName = new PropertyName(((FeatureAttribute)attributesCmb.getSelectedItem()).getName());
			Literal literal = new Literal(sizeTxt.getText());
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
			style.put("Size", arithmeticExpression);
		}
		else {
			Literal literal = new Literal(sizeTxt.getText());
			style.put("Size",literal);
		}
		style.put("Opacity", new Double(opacitySld.getValue() / 100.0));
		style.put("Rotation", new Double((String)rotationCmb.getSelectedItem()));
		style.put("ColorStroke", new Integer(strokeColorBtn.getBackground().getRGB()));
		if (!(attributesCmb1.getSelectedItem().equals(""))&&!(arithmeticExprCmb1.getSelectedItem().equals(""))) {
			PropertyName propertyName = new PropertyName(((FeatureAttribute)attributesCmb1.getSelectedItem()).getName());
			Literal literal = new Literal((String)strokeWidthCmb.getSelectedItem());
			String operator = (String)arithmeticExprCmb1.getSelectedItem();
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
		theRequest.setAttribute("Style", style);
		FrontController fc =  FrontControllerFactory.getFrontController();
		Action theAction = fc.getAction("CreateCustomRule");
		ActionForward theActionForward = theAction.doExecute(theRequest);
		_container.forward(theActionForward, theRequest);
	}

	private void createGraphic() {
		Request theRequest = FrontControllerFactory.createRequest();
		theRequest.setAttribute("RuleName", ruleNameTxt.getText());
		HashMap style = new HashMap();
		style.put("SymbolizerType", "PointGraphic");
		if (!(attributesCmb.getSelectedItem().equals(""))&&!(arithmeticExprCmb.getSelectedItem().equals(""))) {
			PropertyName propertyName = new PropertyName(((FeatureAttribute)attributesCmb.getSelectedItem()).getName());
			Literal literal = new Literal(sizeTxt.getText());
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
			style.put("Size", arithmeticExpression);
		}
		else {
			Literal literal = new Literal(sizeTxt.getText());
			style.put("Size",literal);
		}
		style.put("Opacity", new Double(opacitySld.getValue() / 100.0));
		style.put("Rotation", new Double((String)rotationCmb.getSelectedItem()));
		style.put("Url", externalGraphicURLTxt.getText());
		style.put("Format", externalGraphicFormatTxt.getText());
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
	private DefaultComboBoxModel _patternCmbModel;
	private HashMap _patterns;
	private DefaultComboBoxModel _markOrGraphicCmbModel;
	private DefaultComboBoxModel _wellKnownNameCmbModel;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    private javax.swing.JButton fillColorBtn;
    private javax.swing.JLabel fillColorLbl;
    private javax.swing.JLabel fillOpacityLbl;
    private javax.swing.JSlider fillOpacitySld;
    private javax.swing.JButton filterBtn;
    private javax.swing.JCheckBox filterChk;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JTextField filterTxt;
    private javax.swing.JPanel graphicPanel;
    private javax.swing.JComboBox markOrGraphicCmb;
    private javax.swing.JLabel markOrGraphicLbl;
    private javax.swing.JPanel markOrGraphicPanel;
    private javax.swing.JPanel markPanel;
    private javax.swing.JLabel mitreLineJoinLbl;
    private javax.swing.JRadioButton mitreLineJoinRBtn;
    private javax.swing.JButton okBtn;
    private javax.swing.JLabel opacityLbl;
    private javax.swing.JSlider opacitySld;
    private javax.swing.JComboBox rotationCmb;
    private javax.swing.JLabel rotationLbl;
    private javax.swing.JRadioButton roundLineCapRBtn;
    private javax.swing.JLabel roundLineJoinLbl;
    private javax.swing.JRadioButton roundLineJoinRBtn;
    private javax.swing.JLabel roundlineLbl;
    private javax.swing.JLabel ruleNameLbl;
    private javax.swing.JTextField ruleNameTxt;
    private javax.swing.JPanel rulePanel;
    private javax.swing.JLabel sizeLbl;
    private javax.swing.JTextField sizeTxt;
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
    private javax.swing.JComboBox wellKnownNameCmb;
    private javax.swing.JLabel wellKnownNameLbl;
    // End of variables declaration//GEN-END:variables
	private JList	iconsList;
	private JScrollPane	scroll;
    
}
