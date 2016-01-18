/*
 * * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations. Copyright (C) 2004 INZAMAC-SATEC
 * UTE This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version. This program is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU
 * General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA. For more information, contact: www.geopista.com Created on
 * 02-jun-2004 by juacas
 */
package com.geopista.ui.dialogs;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.Iterator;

import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.feature.ValidationError;
import com.geopista.ui.autoforms.FeatureFieldPanel;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;

/**
 * Genera un diálogo automático con los atributos de una Feature además de los ExtendedForms que se especifiquen
 * 
 * @author juacas
 */
public class FeatureDialog extends ExtendedPanelsDialog 
{
	FeatureFieldPanel	_fieldPanel				= null;

	/**
	 * 
	 */
	
 
      /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
                                                                    .getLog(FeatureDialog.class);
    private static AppContext aplicacion = (AppContext) AppContext
    .getApplicationContext();
	public FeatureDialog(Frame frame, String title, boolean modal,
			Feature feature, ILayerViewPanel lyvPanel, Layer capaActual)
	{
        
	super(frame, title, modal,feature,lyvPanel,capaActual);
    _fieldPanel = new FeatureFieldPanel(feature);
    
    if(capaActual!=null)      
        this.setTitle(capaActual.getName()+":"+capaActual.getDescription());
   
    this.addExtendedForm(_fieldPanel);
	}

	public FeatureDialog(JFrame mainFrame, String string, boolean b, Feature currentFeature)
        {
            this(mainFrame, string, b, currentFeature,null,null);// TODO Auto-generated constructor stub
        }

    // obtenemos un clone de la feature con los atributos modificados
	//
	public Feature getModifiedFeature()
	{
	return _fieldPanel.getModifiedFeature();
	}

	/**
	 * @return Returns the _fieldPanel.
	 */
	public FeatureFieldPanel get_fieldPanel()
	{
	return _fieldPanel;
	}

	/**
	 * @param panel
	 *            The _fieldPanel to set.
	 */
	public void set_fieldPanel(FeatureFieldPanel panel)
	{
	_fieldPanel = panel;
	}

    /* (non-Javadoc)
     * @see com.geopista.ui.dialogs.ExtendedPanelsDialog#buildDialog()
     */
    public void buildDialog()
    {
      
      super.buildDialog();
      _fieldPanel.buildDialogByGroupingTables();
      _fieldPanel.checkPanel(true);
    }

    /* (non-Javadoc)
     * @see com.geopista.ui.dialogs.ExtendedPanelsDialog#jbInit()
     */
//    void jbInit() throws Exception
//    {
//
//       super.jbInit(); 
//       
//        try
//            {
//        
//            _descriptionTextArea.setPreferredSize(new Dimension(100, 100));
//            _descriptionTextArea.setOpaque(false);
//        
//            _descriptionTextArea.setEnabled(false);
//            _descriptionTextArea.setEditable(false);
//            _descriptionTextArea.setContentType("text/html");
//            if (getFeature() instanceof GeopistaFeature
//                    && ((GeopistaFeature) getFeature()).getLayer() != null) _descriptionTextArea
//                    .setText(((GeopistaFeature) getFeature()).getLayer().getName());
//        
//            }
//        catch (RuntimeException e1)
//            {
//            // Problemas para instanciar el kit text/html en el Plugin
//            logger.error("jbInit()", e1);
//            if (logger.isDebugEnabled())
//                {
//                logger.debug("jbInit() - La clase a instanciar para html es:"
//                        + _descriptionTextArea
//                                .getEditorKitForContentType("text/html"));
//                }
//            }
//        
//        // descriptionTextArea.setLineWrap(true);
//        // descriptionTextArea.setWrapStyleWord(true);
//        // _strutPanel.setMaximumSize(new Dimension(SIDEBAR_WIDTH, 21));
//        // _strutPanel.setMinimumSize(new Dimension(SIDEBAR_WIDTH, 21));
//        // _strutPanel.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 21));
//        
//        // Si hay diferentes formularios utilizamos un tabbed pane
//        int attributesCount = getFeature().getSchema().getAttributeCount();
//        boolean showFieldPanel = true;
//        if (attributesCount < 1
//                || (attributesCount == 1 && getFeature().getSchema()
//                        .getAttributeType(0) == AttributeType.GEOMETRY))
//            {
//            showFieldPanel = false;
//            }
//      
//            // añado el tab de atributos
//            if (showFieldPanel)
//                {
//                
//                addPanel(_fieldPanel,0);
//                }
//            else
//                {
//                JLabel attibutesNotFound = new JLabel(aplicacion
//                        .getI18nString("LaFeatureNoContieneNingunAtributo"));
//                getTabs().addTab(aplicacion.getI18nString("AtributosNoEncontrados"),
//                        attibutesNotFound);
//                }
//          
//        
//            // Muestra la pestana primera
//            getTabs().setSelectedIndex(0);
//        
//            
////        else
////            { // Interfaz de un panel sencillo.
////            if (showFieldPanel)
////                {
////                _mainPanel.add(_fieldPanel,
////                        new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
////                                GridBagConstraints.NORTHWEST,
////                                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0,
////                                        0), 0, 0));
////                }
////            else
////                {
////                JLabel attibutesNotFound = new JLabel(aplicacion
////                        .getI18nString("LaFeatureNoContieneNingunAtributo"));
////                _mainPanel.add(attibutesNotFound, new GridBagConstraints(1, 0, 1,
////                        2, 1.0, 1.0, GridBagConstraints.NORTHWEST,
////                        GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10),
////                        0, 0));
////                }
////            }
//        
//        // this.getContentPane().add(_okCancelPanel, BorderLayout.SOUTH);
//        // this.getContentPane().add(_outerMainPanel, BorderLayout.CENTER);
//        
//       
//        
//    }
    void okCancelPanel_actionPerformed(ActionEvent e)
    {
    boolean allOK = true;
    if (!wasOKPressed() || (allOK = isInputValid())) setVisible(false);
    if (!allOK)
        {
        Iterator errores = _fieldPanel.getValidator().getErrorListIterator();
        StringBuffer msg = new StringBuffer();// _descriptionTextArea.getText());
        while (errores.hasNext())
            {
            ValidationError err = (ValidationError) errores.next();
            msg.append("\r\n");
            // TODO: MessageFormat
            msg.append(MessageFormat.format(
                    AppContext.getApplicationContext().getI18nString(
                            "FeatureDialog.FormattedErrorReportMessage"),
                    new Object[]{
                            err.attName,
                            AppContext.getApplicationContext().getI18nString(
                                    err.message)}));
    
            }
        // substituted by Icon and ToolTip for each field [JP]
        // _descriptionTextArea.setText(msg.toString());
        }
    return;
    
    // reportValidationError(firstValidationErrorMessage());
    }
  
    public void setLock()
    {
    _fieldPanel.disableAll();
   super.setLock();
    }
} // @jve:decl-index=0:visual-constraint="10,91"

