/**
 * FeatureFrame.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.NoninvertibleTransformException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.ValidationError;
import com.geopista.ui.autoforms.FeatureFieldPanel;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedForm;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureUtil;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToSelectedItemsPlugIn;

/**
 * Genera un diálogo automático con los atributos de una Feature
 * y adicionalmente incluye unos formularios extendidos para ampliar
 * el interfaz de usuario.
 * Si hay un formulario extendido se presenta un JTabbedPane. En caso contrario
 * se muestra un panel simple.
 * 
 * En ambos casos se decora con un banner lateral.
 * 
 * @see setExtendedForm
 * @author juacas
 *
 */
public class FeatureFrame extends JFrame implements FeatureDialogHome {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(FeatureDialog.class);

	private Feature _feature=null;
	private int maxRows=25;
	 private JPanel _imagePanel = new JPanel();
	 private JLabel _imageLabel = new JLabel();
	 private JPanel _mainPanel = new JPanel();
	 private FeatureFieldPanel _fieldPanel;
	
	 private JTextPane _descriptionTextArea = new JTextPane();
	 private JPanel _strutPanel = new JPanel();
	 private OKCancelPanel _okCancelPanel;  //  @jve:decl-index=0:visual-constraint="32,175"
	 private JPanel _outerMainPanel = new JPanel();
	 private JTabbedPane tabs= new JTabbedPane();
	 private final static int SIDEBAR_WIDTH = 150;
	 private FeatureExtendedForm extendedForm=null;
	 
	 private static AppContext aplicacion = (AppContext) AppContext
     .getApplicationContext();
	private JPanel contentPanel = null;  //  @jve:decl-index=0:visual-constraint="74,122"
	private JPanel _okCancelPanel1 = null;  //  @jve:decl-index=0:visual-constraint="88,134"
	private JButton zoomFeatureButton = null;
	private JButton flashFeatureButton = null;

	protected LayerViewPanel	layerViewPanel;

	
	/**
	 * 
	 */
	public FeatureFrame() {
		super();
		
		 _imagePanel.setVisible(false);
	     _descriptionTextArea.setText("");
	     _imageLabel.setText("");
	}

  public boolean wasOKPressed()
  {
    return _okCancelPanel.wasOKPressed();
  }
  public FeatureFrame(String title, Feature feature) {
  this(title,feature,null);
  }
	public FeatureFrame(String title, Feature feature, LayerViewPanel lyvPanel) {
	
		this(title,feature,lyvPanel, null);
	 
	}
	public FeatureFrame(String title, Feature feature, LayerViewPanel lyvPanel, Layer capaActual)
	{
	super(title);
	_fieldPanel=new FeatureFieldPanel(feature);
	setFeature(feature);
	if (lyvPanel!=null)
		setLayerViewPanel(lyvPanel);
	 _imagePanel.setVisible(false);
	 if (capaActual!=null)
		 {
     _descriptionTextArea.setText(capaActual.getName());
     //_imageLabel.setText(capaActual.getName());
		 }
	}

	public void setFeature(Feature feature)
	{
		this._feature=feature;
	}
	
	/**
	 * Construye el diálogo utilizando paneles decorados
	 * para agrupar los campos según su tabla de procedencia
	 *
	 */
public void buildDialog()
	{
	if (_feature==null) return;
	  this.addComponentListener(new ComponentListener(){

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
		  GUIUtil.centreOnScreen(FeatureFrame.this);
		  //fd.move(Math.max(0,fd.getX()),Math.max(0,fd.getY()));
		
		}
	  
	  });
	try {
		jbInit();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		logger.error("buildDialog()", e);
	}
	_fieldPanel.buildDialogByGroupingTables();
	
	}

    //obtenemos un clone de la feature con los atributos modificados
    //
    public Feature getModifiedFeature()
    {
      return _fieldPanel.getModifiedFeature();
    }
  
public void setSideBarImage(Icon icon) {
    //Add imageLabel only if #setSideBarImage is called. Otherwise the margin
    //above the description will be too tall. [Jon Aquino]
    _imagePanel.add(
        _imageLabel,
        new GridBagConstraints(
            0,
            0,
            1,
            1,
            0.0,
            0.0,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.HORIZONTAL,
            new Insets(10, 10, 0, 10),
            0,
            0));
    _imagePanel.setVisible(true);
    _imageLabel.setIcon(icon);
}
public void setSideBarDescription(String description) {
    _imagePanel.setVisible(true);
    _descriptionTextArea.setText(description);
    Dimension d= _descriptionTextArea.getPreferredScrollableViewportSize();
    Dimension d1=_descriptionTextArea.getPreferredSize();
    Dimension d2=_descriptionTextArea.getSize();
    _descriptionTextArea.setFont(new Font("Arial",Font.PLAIN,8));
    _descriptionTextArea.setSize(d2.width, d1.height);
    this.doLayout();
}
//Experience suggests that one should avoid using weights when using the
//GridBagLayout. I find that nonzero weights can cause layout bugs that are
//hard to track down. [Jon Aquino]
	void jbInit() throws Exception {
		
		_descriptionTextArea.setOpaque(false);
		get_okCancelPanel().addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okCancelPanel_actionPerformed(e);
			}
		});
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				this_componentShown(e);
			}
		});
		tabs.addChangeListener(new javax.swing.event.ChangeListener() {
			
			private FeatureExtendedPanel oldpanel=null;
			public void stateChanged(ChangeEvent e)
			{
				if (oldpanel!=null) oldpanel.exit();
				// Notifica a los paneles de los cambios de pestañas
				FeatureExtendedPanel panel=(FeatureExtendedPanel) tabs.getSelectedComponent();
				if (panel!=null) panel.enter();
        oldpanel=panel;
			}
		});
		
		_outerMainPanel.setLayout(new GridBagLayout());
		_outerMainPanel.setAlignmentX((float) 0.7);
		this.setResizable(true);
		this.getContentPane().setLayout(new BorderLayout());
		_imagePanel.setBorder(BorderFactory.createEtchedBorder());
		_imagePanel.setLayout(new GridBagLayout());
		_mainPanel.setLayout(new GridBagLayout());

		try
			{

			_descriptionTextArea.setPreferredSize(new Dimension(100,100));
			_descriptionTextArea.setOpaque(false);

				_descriptionTextArea.setEnabled(false);
				_descriptionTextArea.setEditable(false);
				_descriptionTextArea.setContentType("text/html");
				if (getFeature() instanceof GeopistaFeature && ((GeopistaFeature)getFeature()).getLayer()!=null)
					_descriptionTextArea.setText(((GeopistaFeature)getFeature()).getLayer().getName());
				
				
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
		
		//descriptionTextArea.setLineWrap(true);
		//descriptionTextArea.setWrapStyleWord(true);
//		_strutPanel.setMaximumSize(new Dimension(SIDEBAR_WIDTH, 21));
//		_strutPanel.setMinimumSize(new Dimension(SIDEBAR_WIDTH, 21));
//		_strutPanel.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 21));

		
		
//		 Si hay diferentes formularios utilizamos un tabbed pane
		int attributesCount = _feature.getSchema().getAttributeCount();
		boolean showFieldPanel = true;
		if(attributesCount<1||(attributesCount==1&&_feature.getSchema().getAttributeType(0)==AttributeType.GEOMETRY))
		{
		    showFieldPanel = false;
		}
		if (extendedForm!=null)
			{
			_mainPanel.add(tabs,new GridBagConstraints(1, 0, 1, 2, 1.0, 1.0,
					GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
					new Insets(0, 0, 0, 0), 0, 0));
			 try
		      {
		        extendedForm.initialize(this);
		      }catch(Exception e)
		      {
				logger.error("jbInit()", e);
		      }
		      
			// añade el panel de la feature
			if(showFieldPanel)
			    addPanel(_fieldPanel);
			else
			{
			    JLabel attibutesNotFound = new JLabel(aplicacion.getI18nString("LaFeatureNoContieneNingunAtributo"));
			    tabs.addTab(aplicacion.getI18nString("AtributosNoEncontrados"),attibutesNotFound);
			}
			
			// Añade otros paneles del FeatureExtendedForm
     
      // Muestra la pestana primera
      tabs.setSelectedIndex(0);
    
			}
		else
		{ // Interfaz de un panel sencillo.
		    if(showFieldPanel)
		    {
			_mainPanel.add(_fieldPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
			GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
			new Insets(0, 0, 0, 0), 0, 0));
		    }
			else
			{
			    JLabel attibutesNotFound = new JLabel(aplicacion.getI18nString("LaFeatureNoContieneNingunAtributo"));
			    _mainPanel.add(attibutesNotFound, new GridBagConstraints(1, 0, 1, 2, 1.0, 1.0,
						GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
						new Insets(10, 10, 10, 10), 0, 0));
			}
		}
		
//		this.getContentPane().add(_okCancelPanel, BorderLayout.SOUTH);
//		this.getContentPane().add(_outerMainPanel, BorderLayout.CENTER);
		
		
		_imagePanel.add(_descriptionTextArea, new GridBagConstraints(0, 1, 1, 1,
				0.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
		_imagePanel.add(_strutPanel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		_outerMainPanel.add(_mainPanel, new GridBagConstraints(1, 0, 1, 1, 1.0,
				1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		_outerMainPanel.add(_imagePanel, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		_descriptionTextArea.setFont(_imageLabel.getFont());
		_descriptionTextArea.setDisabledTextColor(_imageLabel.getForeground());
		
		this.setContentPane(getContentPanel());  // Generated
			this.setSize(342, 222);  // Generated
			
			_strutPanel.add(getZoomFeatureButton(), null);  // Generated
			_strutPanel.add(getFlashFeatureButton(), null);  // Generated
		
	}
void okCancelPanel_actionPerformed(ActionEvent e) {
	boolean allOK=true;
    if (!_okCancelPanel.wasOKPressed() || (allOK=isInputValid()))
        setVisible(false);
    if (!allOK)
    {
    	Iterator errores = _fieldPanel.getValidator().getErrorListIterator();
    	StringBuffer msg = new StringBuffer();//_descriptionTextArea.getText());
    	while (errores.hasNext())
    	{
                ValidationError err = (ValidationError) errores.next();
                msg.append("\r\n");
                // TODO: MessageFormat
                msg.append(
                        MessageFormat.format(
                                AppContext.getApplicationContext().getI18nString("FeatureDialog.FormattedErrorReportMessage"),
                                new Object[] { err.attName, AppContext.getApplicationContext().getI18nString(err.message) }));
                
        }
    	// substituted by Icon and ToolTip for each field [JP]
    	//_descriptionTextArea.setText(msg.toString());
    }
        return;
    
  //  reportValidationError(firstValidationErrorMessage());
}
protected boolean isInputValid() {
	return _fieldPanel.checkPanel(true);
}
void this_componentShown(ComponentEvent e) {
    _okCancelPanel.setOKPressed(false);
}
public void setLock()
{
_fieldPanel.disableAll();
}
public void setVisible(boolean visible) {
    //Workaround for Java bug  4446522 " JTextArea.getPreferredSize()
    //incorrect when line wrapping is used": call #pack twice [Jon Aquino]
    pack();
    pack();
    //GUIUtil.centreOnWindow(FeatureFrame.this);
    super.setVisible(visible);
    this.requestFocus();
}

/* (non-Javadoc)
 * @see com.geopista.util.FeatureDialogHome#addPanel(javax.swing.JPanel)
 */
public void addPanel(FeatureExtendedPanel form)
{
	tabs.add((JPanel)form);
	
}
public void addPanel(FeatureExtendedPanel form,int pos)
{
	tabs.add((JPanel)form,pos);
	
}
public void setPanelEnabled (int pos, boolean act){
    tabs.setEnabledAt(pos, act);
}
/* (non-Javadoc)
 * @see com.geopista.util.FeatureDialogHome#removePanel(javax.swing.JPanel)
 */
public void removePanel(FeatureExtendedPanel form)
{
	tabs.remove((JPanel)form);	
}

	/**
	 * @return Returns the extendedForm.
	 */
	public FeatureExtendedForm getExtendedForm()
	{
		return extendedForm;
	}
	/**
	 * @param extendedForm The extendedForm to set.
	 */
	public void setExtendedForm(FeatureExtendedForm extendedForm)
	{
		this.extendedForm = extendedForm;
	}
	public void setExtendedForm(String classname)
	{
		 FeatureExtendedForm featureExtendedForm;
		try
		{
		featureExtendedForm = (FeatureExtendedForm) Class.forName(classname).newInstance();
		setExtendedForm(featureExtendedForm);
		} catch (InstantiationException e)
		{
		
		logger.error("setExtendedForm(String)", e);
		} catch (IllegalAccessException e)
		{
		
		logger.error("setExtendedForm(String)", e);
		} catch (ClassNotFoundException e)
		{
		
		logger.error("setExtendedForm(String)", e);
		}
		
	}

	/* (non-Javadoc)
	 * @see com.geopista.util.FeatureDialogHome#getFeature()
	 */
	public Feature getFeature()
	{
		return _feature;
	}

	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			contentPanel = new JPanel();
			contentPanel.setLayout(new BorderLayout());  // Generated
			contentPanel.add(get_okCancelPanel(), java.awt.BorderLayout.SOUTH);  // Generated
			contentPanel.add(_outerMainPanel, java.awt.BorderLayout.CENTER);  // Generated
		}
		return contentPanel;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private OKCancelPanel get_okCancelPanel() {
		if (_okCancelPanel == null) {
			_okCancelPanel = new OKCancelPanel();
		}
		return _okCancelPanel;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getZoomFeatureButton() {
		if (zoomFeatureButton == null) {
			zoomFeatureButton = new JButton();
			if (layerViewPanel==null)zoomFeatureButton.setVisible(false);
			zoomFeatureButton.setIcon( com.geopista.ui.images.IconLoader.icon("SmallMagnify.gif"));
			zoomFeatureButton.setToolTipText(aplicacion.getI18nString("FieldDialog.ZoomToCurrentFeature"));
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
				layerViewPanel);
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
				layerViewPanel);
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
	private JButton getFlashFeatureButton() {
		if (flashFeatureButton == null) {
			flashFeatureButton = new JButton();
			if (layerViewPanel==null) flashFeatureButton.setVisible(false);
			flashFeatureButton.setIcon( IconLoader.icon("Flashlight.gif"));
			flashFeatureButton.setToolTipText(aplicacion.getI18nString("FieldDialog.FlashCurrentFeature"));
			flashFeatureButton.addActionListener(new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent e)
					{
					
					flashFeature(_feature);
					}
				});
	
		}
		return flashFeatureButton;
	}
	public void setLayerViewPanel(LayerViewPanel layerViewPanel)
	{
	this.layerViewPanel = layerViewPanel;
	}
    }  //  @jve:decl-index=0:visual-constraint="10,91"
