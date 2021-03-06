/**
 * ImageDialog.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedPanel;
import com.geopista.app.AppContext;
import com.geopista.app.image.ImageExtendedForm;
import com.geopista.ui.images.IconLoader;
import com.geopista.feature.GeopistaFeature;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToSelectedItemsPlugIn;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureUtil;

import javax.swing.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Vector;
import java.util.ArrayList;
import java.awt.*;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;

/**
 * Creado por SATEC.
 * User: angeles
 * Date: 08-may-2006
 * Time: 11:46:08
 */
public class ImageDialog  extends JDialog implements FeatureDialogHome {
        private static final Log logger = LogFactory.getLog(FeatureDialog.class);
        private Vector features=null; //Esto hay que cambiarlo por una colleccion de features
        private JPanel _imagePanel = new JPanel();
        private JLabel _imageLabel = new JLabel();
        private JPanel _mainPanel = new JPanel();
        private ImageExtendedForm imageExtendedForm;

        private JTextPane _descriptionTextArea = new JTextPane();
    	private JPanel _infoFeatures = new JPanel();
        private JPanel _strutPanel = new JPanel();
        private OKCancelPanel _okCancelPanel;
        private JPanel _outerMainPanel = new JPanel();

        private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
        private JPanel contentPanel = null;  //  @jve:decl-index=0:visual-constraint="74,122"
        private JButton zoomFeatureButton = null;
        private JButton flashFeatureButton = null;
    	private JComboBoxFeatures jComboBoxFeatures=null;

        protected ILayerViewPanel	layerViewPanel;

        /**
         *  Constructor de la clase
         */
        public ImageDialog() {
            super();
            _imagePanel.setVisible(false);
            _descriptionTextArea.setText("");
            _imageLabel.setText("");
        }
        /**
         * Indica si se pulso aceptar o cancelar
         * @return
         */
        public boolean wasOKPressed()
        {
            return _okCancelPanel.wasOKPressed();
        }
        public ImageDialog(Frame frame, String title, boolean modal, Vector features, ILayerViewPanel lyvPanel)
        {
            super(frame, title, modal);
            setFeatures(features);
            if (lyvPanel!=null)
                setLayerViewPanel(lyvPanel);
            _imagePanel.setVisible(false);
           // if (capaActual!=null)
           //     _descriptionTextArea.setText(capaActual.getName());
        }

        public void setFeatures(Vector features)
        {
            this.features=features;
        }

        /**
         * Construye el di�logo utilizando paneles decorados
         * para agrupar los campos seg�n su tabla de procedencia
         *
         */
        public void buildDialog()
        {
            if (features==null) return;
            this.addComponentListener(new ComponentListener(){

            public void componentHidden(ComponentEvent arg0){}

            public void componentMoved(ComponentEvent arg0){}

            public void componentResized(ComponentEvent arg0){}

            public void componentShown(ComponentEvent arg0)
            {
                pack();
                pack();
                GUIUtil.centreOnScreen(com.geopista.ui.dialogs.ImageDialog.this);
            }
          });
            try {
                jbInit();
            } catch (Exception e) {
                logger.error("buildDialog()", e);
            }
        }

        public void setSideBarImage(Icon icon) {
            //Add imageLabel only if #setSideBarImage is called. Otherwise the margin
            //above the description will be too tall. [Jon Aquino]
            _imagePanel.add(_imageLabel, new GridBagConstraints(0,0,1,1,0.0,0.0,
                GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10),
                0,0));
            _imagePanel.setVisible(true);
            _imageLabel.setIcon(icon);
        }
        public void setSideBarDescription(String description) {
            _imagePanel.setVisible(true);
            _descriptionTextArea.setText(description);
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

            //_outerMainPanel.setLayout(new GridBagLayout());
            //_outerMainPanel.setAlignmentX((float) 0.7);
            _outerMainPanel.setLayout(new BorderLayout());
            this.setResizable(true);
            this.getContentPane().setLayout(new BorderLayout());
            _imagePanel.setBorder(BorderFactory.createEtchedBorder());
            _imagePanel.setLayout(new GridBagLayout());
            _mainPanel.setLayout(new BorderLayout());
            try	{
                _descriptionTextArea.setPreferredSize(new Dimension(100,100));
                _descriptionTextArea.setOpaque(false);
                _descriptionTextArea.setEnabled(false);
                _descriptionTextArea.setEditable(false);
                _descriptionTextArea.setContentType("text/html");
            }catch (RuntimeException e1){
                // Problemas para instanciar el kit text/html en el Plugin
                logger.error("jbInit()", e1);
                if (logger.isDebugEnabled()){
                    logger.debug("jbInit() - La clase a instanciar para html es:"
                            + _descriptionTextArea.getEditorKitForContentType("text/html"));
                }
            }
            imageExtendedForm= new ImageExtendedForm();
            imageExtendedForm.initialize(this);

            setDescription();

            _imagePanel.add(_descriptionTextArea, new GridBagConstraints(0, 1, 1, 1,
                    0.0, 1.0, GridBagConstraints.NORTHWEST,
                    GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
    		_imagePanel.add(_infoFeatures, new GridBagConstraints(0, 2, 1, 1, 0.0, 1.0,
    				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
    				new Insets(10, 10, 10, 10), 0, 0));
            
            _imagePanel.add(_strutPanel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));
            _outerMainPanel.add(_mainPanel,BorderLayout.CENTER);
            _outerMainPanel.add(_imagePanel, BorderLayout.WEST);

            _descriptionTextArea.setFont(_imageLabel.getFont());
            _descriptionTextArea.setDisabledTextColor(_imageLabel.getForeground());
    		if (features.size()>1)
    			_infoFeatures.add(getFeaturesComboBox(),null);

            this.setContentPane(getContentPanel());  // Generated
            _strutPanel.add(getZoomFeatureButton(), null);  // Generated
            _strutPanel.add(getFlashFeatureButton(), null);  // Generated
        }
        void okCancelPanel_actionPerformed(ActionEvent e) {
            setVisible(false);
            return;
        }

        void this_componentShown(ComponentEvent e) {
            _okCancelPanel.setOKPressed(false);
        }
        public void setLock()
        {
               if (imageExtendedForm!=null)
                    imageExtendedForm.disableAll();
        }
        public void setVisible(boolean visible) {
            pack();
            pack();
            GUIUtil.centreOnWindow(com.geopista.ui.dialogs.ImageDialog.this);
            super.setVisible(visible);
            this.requestFocus();
        }

        /* (non-Javadoc)
        * @see com.geopista.util.FeatureDialogHome#addPanel(javax.swing.JPanel)
        */
        public void addPanel(FeatureExtendedPanel form)
        {
            //_mainPanel.add(form, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
            // GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
            // new Insets(0, 0, 0, 0), 0, 0));
            _mainPanel.add((Component)form, BorderLayout.CENTER);
        }
        public void addPanel(FeatureExtendedPanel form, int pos) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
        public void removePanel(FeatureExtendedPanel form) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
        public void setPanelEnabled(int pos, boolean act) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
        public Feature getFeature() {
            if (imageExtendedForm!=null)
               return imageExtendedForm.getSelectedFeature();
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
        /* (non-Javadoc)
         * @see com.geopista.util.FeatureDialogHome#getFeature()
         */
        public Vector getFeatures()
        {
            return features;
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
                contentPanel.setPreferredSize(new Dimension(800,500));
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
                _okCancelPanel.setCancelVisible(false);
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
                       zoom(imageExtendedForm.getSelectedFeature());
                       setDescription();
                    }
                });
            }
            return zoomFeatureButton;
        }
    	/**
    	 * This method initializes jButton
    	 *
    	 * @return javax.swing.JButton
    	 */
    	private JComboBoxFeatures getFeaturesComboBox() {		
    		if (jComboBoxFeatures == null) {
    			//System.out.println("features."+features.size());
    			jComboBoxFeatures = new JComboBoxFeatures(features.toArray(),null);
    			jComboBoxFeatures.addActionListener(new java.awt.event.ActionListener()
    			{
    				public void actionPerformed(java.awt.event.ActionEvent e)
    				{
    				  boxActionPerformed();
    				}
    			});
    		}
    		return jComboBoxFeatures;
    	}	
        private void boxActionPerformed(){
                //if (jComboBoxFeatures.getSelectedIndex()!=0){
                	//System.out.println("jComboBoxFeatures.getSelectedIndex()"+jComboBoxFeatures.getSelectedIndex());
                	GeopistaFeature feature= (GeopistaFeature)jComboBoxFeatures.getSelectedItem();
                	//if (feature!=null)
                	//	System.out.println("Feature es distinta de null");
                	//else
                	//	System.out.println("Feature es  null");
                	imageExtendedForm.getInfoImagePanel().setFeature(feature);
                	//featuresJComboBox.setText("");
                //}
        }

        public void setDescription()
        {
            try
            {
                if (getFeature() instanceof GeopistaFeature && ((GeopistaFeature)getFeature()).getLayer()!=null)
                                  _descriptionTextArea.setText(((GeopistaFeature)getFeature()).getLayer().getName());
            }catch(Exception e){}
        }
        private void flashFeature(Feature feature)
        {
        if (layerViewPanel == null || feature==null) return;
        ArrayList col = new ArrayList();
        col.add(feature);
        try
            {
                new ZoomToSelectedItemsPlugIn().flash(FeatureUtil.toGeometries(col),
                    (LayerViewPanel)layerViewPanel);
                setDescription();
            }
        catch (NoninvertibleTransformException ex)
            {
            logger.debug("flashFeature(feature = " + feature
                    + ") - Error al hacer flash a la feature.");
            }
        }

        private void zoom(Feature feature)
        {
        if (layerViewPanel == null || feature==null ) return;
        ArrayList col = new ArrayList();
        col.add(feature);
        try
            {
            new ZoomToSelectedItemsPlugIn().zoom(FeatureUtil.toGeometries(col),
                    layerViewPanel);
                   setDescription();
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
                            flashFeature(imageExtendedForm.getSelectedFeature());
                            setDescription();
                        }
                    });

            }
            return flashFeatureButton;
        }
        public void setLayerViewPanel(ILayerViewPanel layerViewPanel)
        {
        this.layerViewPanel = layerViewPanel;
        }




}
