package com.geopista.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.plugin.Constantes;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedForm;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureUtil;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToSelectedItemsPlugIn;
/*Muestra un diálogo que admite paneles "ExtendedForm" 
*  incluye los formularios extendidos para ampliar el interfaz
* de usuario. Si hay másd de un panel de formulario  se presenta un JTabbedPane. En
* caso contrario se muestra un panel simple.
* En ambos casos se decora con un banner lateral si se especifica un icono decorativo
* @see setSideBarDescription()
* */
public abstract class ExtendedPanelsDialog extends JDialog implements FeatureDialogHome
{

    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
    																.getLog(ExtendedPanelsDialog.class);
    private Feature _feature = null;
    protected JPanel _imagePanel = new JPanel();
    protected JLabel _imageLabel = new JLabel();
    private JPanel _mainPanel = new JPanel();
    protected JTextPane _descriptionTextArea;
    private JPanel _strutPanel = new JPanel();
    private OKCancelPanel _okCancelPanel;
    private JPanel _outerMainPanel = new JPanel();
    private JTabbedPane tabs;;

    public JTabbedPane getTabs()
    {
        if (tabs==null)
        {
            tabs = new JTabbedPane();
        }
    return tabs;
    }

    public void setTabs(JTabbedPane tabs)
    {
    this.tabs = tabs;
    }

    private Vector extendedForms = null;  //  @jve:decl-index=0:
    private static AppContext aplicacion = (AppContext) AppContext
    															.getApplicationContext();
    private JPanel contentPanel = null;
    private JButton zoomFeatureButton = null;
    private JButton flashFeatureButton = null;
    protected ILayerViewPanel layerViewPanel;
    public ExtendedPanelsDialog()
    {
    super();

    _imagePanel.setVisible(true);
    setSideBarDescription("Description");
    _imageLabel.setText("Image");
    }
    public ExtendedPanelsDialog(Frame frame, String title, boolean modal)
    {
    this(frame, title, modal, null, null);
    }
    public ExtendedPanelsDialog(Frame frame, String title, boolean modal,
            Feature feature)
    {
    this(frame, title, modal, feature, null);
    }

    public ExtendedPanelsDialog(Frame frame, String title, boolean modal,
            Feature feature, ILayerViewPanel lyvPanel)
    {

    this(frame, title, modal, feature, lyvPanel, null);

    }
   
    public ExtendedPanelsDialog(Frame frame, String title, boolean modal,
            Feature feature, ILayerViewPanel lyvPanel, Layer capaActual)
    {
    super(frame, title, modal);
    
    setFeature(feature);
    if (lyvPanel != null) setLayerViewPanel(lyvPanel);
   
    _imagePanel.setVisible(false);
    if (capaActual != null)
        {
        setSideBarDescription(capaActual.getName());
        
        // _imageLabel.setText(capaActual.getName());
        }
    }
    public boolean wasOKPressed()
    {
    return _okCancelPanel.wasOKPressed();
    }

    public void setFeature(Feature feature)
    {
    this._feature = feature;
    }

    /**
     * Construye el diálogo utilizando paneles decorados para agrupar los campos
     * según su tabla de procedencia
     */
    public void buildDialog()
    {
   
    this.addComponentListener(new ComponentListener()
    	{
    
    		public void componentHidden(ComponentEvent arg0)
    		{
    		// TODO Auto-generated method stub
    
    		}
    
    		public void componentMoved(ComponentEvent arg0)
    		{
    		// TODO Auto-generated method stub
    
    		}
    
    		public void componentResized(ComponentEvent arg0)
    		{
    		// TODO Auto-generated method stub
    
    		}
    
    		public void componentShown(ComponentEvent arg0)
    		{
    		pack();
    		pack();
    		GUIUtil.centreOnScreen(ExtendedPanelsDialog.this);
    		// fd.move(Math.max(0,fd.getX()),Math.max(0,fd.getY()));
    
    		}
    
    	});
    try
    	{
    	jbInit();
    	}
    catch (Exception e)
    	{
    	// TODO Auto-generated catch block
    	logger.error("buildDialog()", e);
    	}
    
    
    }

    public void setSideBarImage(Icon icon)
    {
        _imagePanel.setVisible(icon!=null);
        if (icon==null)return;
    // Add imageLabel only if #setSideBarImage is called. Otherwise the margin
    // above the description will be too tall. [Jon Aquino]
    _imagePanel.add(_imageLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
    		GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
    		new Insets(10, 10, 0, 10), 0, 0));
    
    _imageLabel.setIcon(icon);
    }
/*
 * 
 */
    public void setSideBarDescription(String description)
    {
    JTextPane text = getDescriptionTextArea();
   
    text.setVisible(description!=null);
    if (description==null) return;
 
    text.setText(description);
    text.setBorder(BorderFactory.createLineBorder(Color.RED,5));
    Dimension d1 = text.getPreferredSize();
    Dimension d2 = text.getSize();
   
    text.setFont(new Font("Arial", Font.PLAIN, 8));
    text.setSize(d2.width, 24);
 
    this.doLayout();
    }

    void jbInit() throws Exception
    {
    
  
  
    get_okCancelPanel().addActionListener(new java.awt.event.ActionListener()
    	{
    		public void actionPerformed(ActionEvent e)
    		{
    		okCancelPanel_actionPerformed(e);
    		}
    	});
    this.addComponentListener(new java.awt.event.ComponentAdapter()
    	{
    		public void componentShown(ComponentEvent e)
    		{
    		this_componentShown(e);
    		}
    	});
    getTabs().addChangeListener(new javax.swing.event.ChangeListener()
    	{
    
    		private FeatureExtendedPanel	oldpanel	= null;
    
    		public void stateChanged(ChangeEvent e)
    		{
    		if (oldpanel != null) oldpanel.exit();
    		// Notifica a los paneles de los cambios de pestañas
    		Component comp=getTabs().getSelectedComponent();
    		FeatureExtendedPanel panel=null;
            if(comp instanceof JScrollPane)
                comp=((JScrollPane) comp).getViewport().getComponent(0);
    		if (comp instanceof FeatureExtendedPanel)
    			panel = (FeatureExtendedPanel) comp;
    		if (panel != null) panel.enter();
    		oldpanel = panel;
    		}
    	});
    
    _outerMainPanel.setLayout(new GridBagLayout());
    _outerMainPanel.setAlignmentX((float) 0.7);
    this.setResizable(true);
    this.getContentPane().setLayout(new BorderLayout());
   
    
    try
    	{
    	getDescriptionTextArea().setOpaque(false);
    	_descriptionTextArea.setPreferredSize(new Dimension(100, 100));
    	_descriptionTextArea.setOpaque(false);
    
    	_descriptionTextArea.setEnabled(false);
    	_descriptionTextArea.setEditable(false);
    	_descriptionTextArea.setContentType("text/html");
    	if (getFeature() instanceof GeopistaFeature
    			&& ((GeopistaFeature) getFeature()).getLayer() != null) _descriptionTextArea
    			.setText(((GeopistaFeature) getFeature()).getLayer().getName());
    
    	}
    catch (RuntimeException e1)
    	{
    	// Problemas para instanciar el kit text/html en el Plugin
    	logger.error("jbInit()", e1);
    	if (logger.isDebugEnabled())
    		{
    		logger.debug("jbInit() - La clase a instanciar para html es:"
    				+ _descriptionTextArea
    						.getEditorKitForContentType("text/html"));
    		}
    	}
    
 
    
    //Interfaz con Pestañas
   
    _strutPanel.add(getZoomFeatureButton(), null); // Generated
    _strutPanel.add(getFlashFeatureButton(), null); // Generated
  
   
   
  
    _mainPanel.setLayout(new GridBagLayout());
   // _mainPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
    _mainPanel.add(_strutPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 
    		GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
   	_mainPanel.add(getTabs(), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));
    
   	installForms();
    
   	// Muestra la pestana primera
   	getTabs().setSelectedIndex(0);
    	
    _descriptionTextArea.setFont(_imageLabel.getFont());
    _descriptionTextArea.setDisabledTextColor(_imageLabel.getForeground());
    
    _imagePanel.setBorder(BorderFactory.createEtchedBorder());
    _imagePanel.setLayout(new GridBagLayout());
    _imagePanel.setPreferredSize(new Dimension(100, 100)); 	    
    _imagePanel.add(getDescriptionTextArea(),new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
    	    		GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
    	    		new Insets(10, 10, 0, 10), 0, 0));
    
   
    _outerMainPanel.add(_imagePanel, new GridBagConstraints(0, 0, 1, 1, 0.0,
    		0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
    		new Insets(0, 0, 0, 0), 0, 0));
    _outerMainPanel.add(_mainPanel, new GridBagConstraints(1, 0, 1, 1, 1.0,
    		1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
    		new Insets(0, 0, 0, 0), 0, 0));
//  _outerMainPanel.setLayout(new FlowLayout());
//  _outerMainPanel.add(_imagePanel);
//  _outerMainPanel.add(_mainPanel);
    this.setContentPane(getContentPanel()); // Generated
   
   
    
    }

	/**
	 * @param vExtraForms
	 */
	private void installForms()
	{
	try
	{
//	 Obtenemos paneles suplementarios
    Vector vExtraForms = (Vector) ((AppContext) AppContext
    		.getApplicationContext()).getBlackboard().get(
    				Constantes.IDENT_EXTENDED_FORM);
	
    // añade los extendedForms
	if (extendedForms != null) for (Enumeration e = extendedForms
			.elements(); e.hasMoreElements();)
		{
			FeatureExtendedForm extendedForm = (FeatureExtendedForm) e
					.nextElement();
			try
				{
				extendedForm.initialize(this);
				}
			catch (Exception e1)
				{
				// Notifica la excepción y continua añadiendo los
				// ExtendedForm
				logger.error(
						"jbInit(): Un Extended form ha fallado la inicialización:"
								+ extendedForm, e1);
				JLabel formFailed = new JLabel(aplicacion
						.getI18nString("FalloEnInicializacionExtendedForm"));
				getTabs().addTab(aplicacion.getI18nString("FailedExtendedForm"),
						formFailed);
				}
			}
   
	// añado los extras
	if (vExtraForms != null) for (Enumeration e = vExtraForms
			.elements(); e.hasMoreElements();)
		((FeatureExtendedForm) e.nextElement()).initialize(this);
	}
	catch (Exception e)
		{
		logger.error("jbInit()", e);
		}
	}

    abstract void okCancelPanel_actionPerformed(ActionEvent e);
    

    protected boolean isInputValid()
    {
      Vector ext=getExtendedForms();  
      for (Iterator iter = ext.iterator(); iter.hasNext();)
    {
        FeatureExtendedForm element = (FeatureExtendedForm) iter.next();
        if (element.checkPanels()==false) return false;
        
    }
      
      Vector vExtraForms = (Vector) ((AppContext) AppContext
            .getApplicationContext()).getBlackboard().get(
            Constantes.IDENT_EXTENDED_FORM);
      
      if(vExtraForms!=null)
      {
      
          for (Iterator iter = vExtraForms.iterator(); iter.hasNext();)
          {
              FeatureExtendedForm element = (FeatureExtendedForm) iter.next();
              if (element.checkPanels()==false) return false;
              
          }
      }
      
      return true;
    }

    void this_componentShown(ComponentEvent e)
    {
    _okCancelPanel.setOKPressed(false);
    }

    public void setLock()
    {
    
    Vector vExtraForms = (Vector) ((AppContext) AppContext
    		.getApplicationContext()).getBlackboard().get(
    		Constantes.IDENT_EXTENDED_FORM);
    if (vExtraForms != null)
    	{
    	for (Enumeration e = vExtraForms.elements(); e.hasMoreElements();)
    		((FeatureExtendedForm) e.nextElement()).disableAll();
    	}
    }

    public void setVisible(boolean visible)
    {
    // Workaround for Java bug 4446522 " JTextArea.getPreferredSize()
    // incorrect when line wrapping is used": call #pack twice [Jon Aquino]
    pack();
    pack();
    GUIUtil.centreOnWindow(ExtendedPanelsDialog.this);
    super.setVisible(visible);
    this.requestFocus();
    }

    public void addPanel(FeatureExtendedPanel form)
    {
   addPanel(form,getTabs().getTabCount()); //añade al final
    }

    public void addPanel(FeatureExtendedPanel form, int pos)
    {
    JScrollPane scr=new JScrollPane((JPanel) form);
    scr.setPreferredSize(new Dimension(800,600));
    scr.setBorder(BorderFactory.createEmptyBorder());
    getTabs().add(scr,pos);
    getTabs().setTitleAt(pos, ((JComponent)form).getName());
    }

    public void setPanelEnabled(Class clase, boolean act)
    {
    	for (int i = 0; i < getTabs().getComponentCount(); i++)
    	{
    		if (getTabs().getComponentAt(i) instanceof JScrollPane){
    			JScrollPane scrollPane = (JScrollPane)getTabs().getComponentAt(i);
    			for (int j = 0; j < scrollPane.getComponentCount(); j++){
    				if (scrollPane.getComponent(j) instanceof JViewport){
    					JViewport viewport = (JViewport)scrollPane.getComponent(j);
    					for (int k = 0; k < viewport.getComponentCount(); k++){
    						if (viewport.getComponent(k).getClass().equals(clase)) 
    							getTabs().setEnabledAt(i, act);

    						//if (getTabs().getComponentAt(i).getClass().equals(clase)) getTabs().setEnabledAt(
    						//		i, act);
    					}
    				}
    			}
    		}
    	}
    }

    public void setPanelEnabled(int pos, boolean act)
    {
    
    getTabs().setEnabledAt(pos, act);
    }

    public void removePanel(FeatureExtendedPanel form)
    {
    getTabs().remove((JPanel) form);
    
    }

    /**
     * @return Returns the extendedForm.
     */
    public Vector getExtendedForms()
    {
    return extendedForms;
    }

    /**
     * @param extendedForm
     *            The extendedForm to set.
     */
    public void addExtendedForm(FeatureExtendedForm extendedForm)
    {
    if (extendedForms == null) extendedForms = new Vector();
    this.extendedForms.add(extendedForm);
    }

    public void setExtendedForm(String classname)
    {
    String[] clases = classname.split(",");
    for (int i = 0; i < clases.length; i++)
    	{
    	FeatureExtendedForm featureExtendedForm;
    	try
    		{
    		featureExtendedForm = (FeatureExtendedForm) Class
    				.forName(clases[i]).newInstance();
    		addExtendedForm(featureExtendedForm);
    		}
    	catch (InstantiationException e)
    		{
    		logger.error("setExtendedForm(String)", e);
    		}
    	catch (IllegalAccessException e)
    		{
    		logger.error("setExtendedForm(String)", e);
    		}
    	catch (ClassNotFoundException e)
    		{
    		logger.error("setExtendedForm(String)", e);
    		}
    	}
    }

    public Feature getFeature()
    {
    return _feature;
    }

    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getContentPanel()
    {
    if (contentPanel == null)
    	{
    	contentPanel = new JPanel();
    	contentPanel.setLayout(new BorderLayout()); // Generated
    	contentPanel.add(get_okCancelPanel(), java.awt.BorderLayout.SOUTH); // Generated
    	contentPanel.add(_outerMainPanel, java.awt.BorderLayout.CENTER); // Generated
    	}
    return contentPanel;
    }

    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private OKCancelPanel get_okCancelPanel()
    {
    if (_okCancelPanel == null)
    	{
    	_okCancelPanel = new OKCancelPanel();
    	}
    return _okCancelPanel;
    }

    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getZoomFeatureButton()
    {
    if (zoomFeatureButton == null)
    	{
    	zoomFeatureButton = new JButton();
    	if (layerViewPanel == null) zoomFeatureButton.setEnabled(false);
    	zoomFeatureButton.setIcon(com.geopista.ui.images.IconLoader
    			.icon("SmallMagnify.gif"));
    	zoomFeatureButton.setToolTipText(aplicacion
    			.getI18nString("FieldDialog.ZoomToCurrentFeature"));
    	zoomFeatureButton.addActionListener(new java.awt.event.ActionListener()
    		{
    			public void actionPerformed(java.awt.event.ActionEvent e)
    			{
    
    			zoom(_feature);
    			}
    		});
    	}
    return zoomFeatureButton;
    }

    private void flashFeature(Feature feature)
    {
    if (layerViewPanel == null) return;
    ArrayList col = new ArrayList();
    col.add(feature);
    try
    	{
    	new ZoomToSelectedItemsPlugIn().flash(FeatureUtil.toGeometries(col),
    			(LayerViewPanel) layerViewPanel);
    	}
    catch (NoninvertibleTransformException ex)
    	{
    	logger.debug("flashFeature(feature = " + feature
    			+ ") - Error al hacer flash a la feature.");
    	}
    }

    private void zoom(Feature feature)
    {
    if (layerViewPanel == null) return;
    ArrayList col = new ArrayList();
    col.add(feature);
    try
    	{
    	new ZoomToSelectedItemsPlugIn().zoom(FeatureUtil.toGeometries(col),
    			(LayerViewPanel) layerViewPanel);
    	}
    catch (NoninvertibleTransformException ex)
    	{
    	logger.debug("zoomFeature(feature = " + feature
    			+ ") - Error al hacer zoom a la feature.");
    	}
    }

    /**
     * This method initializes jButton1
     * 
     * @return javax.swing.JButton
     */
    private JButton getFlashFeatureButton()
    {
    if (flashFeatureButton == null)
    	{
    	flashFeatureButton = new JButton();
    	if (layerViewPanel == null) flashFeatureButton.setEnabled(false);
    	flashFeatureButton.setIcon(IconLoader.icon("Flashlight.gif"));
    	flashFeatureButton.setToolTipText(aplicacion
    			.getI18nString("FieldDialog.FlashCurrentFeature"));
    	flashFeatureButton
    			.addActionListener(new java.awt.event.ActionListener()
    				{
    					public void actionPerformed(java.awt.event.ActionEvent e)
    					{
    
    					flashFeature(_feature);
    					}
    				});
    
    	}
    return flashFeatureButton;
    }

    public void setLayerViewPanel(ILayerViewPanel layerViewPanel)
    {
    this.layerViewPanel = layerViewPanel;
    }

    /**
     * @return the _descriptionTextArea
     */
    public JTextPane getDescriptionTextArea()
    {
        if (_descriptionTextArea==null)
            {
            _descriptionTextArea = new JTextPane();
            _descriptionTextArea.setPreferredSize(new Dimension(40, 22));
            _descriptionTextArea.setVisible(false);
            }
            return _descriptionTextArea;
            
            
    }

	public JPanel getStrutPanel()
	{
	return _strutPanel;
	}

}