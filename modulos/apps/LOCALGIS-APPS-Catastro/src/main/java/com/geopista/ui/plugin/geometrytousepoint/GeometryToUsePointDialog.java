/**
 * 
 */
package com.geopista.ui.plugin.geometrytousepoint;

import java.awt.BorderLayout;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

/**
 * @author seilagamo
 * 
 */
public class GeometryToUsePointDialog extends JDialog {

    private PlugInContext context = null;
    private JPanel jContentPane = null;
    private GeometryToUsePointLayerPanel geometryToUsePointLayerPanel = null;
    private GeometryToUsePointUsePanel geometryToUsePointUsePanel = null;
    private OKCancelPanel _okCancelPanel = null;
    private boolean evaluandoFeatures = false;

    /**
     * This is the default constructor
     */
    public GeometryToUsePointDialog(PlugInContext context) {

        super();
        this.context = context;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        initialize();
        Locale loc = Locale.getDefault();
        ResourceBundle bundle = ResourceBundle.getBundle(
                "com.geopista.ui.plugin.geometryToUsePoint.languages.GeometryToUsePointi18n", loc,
                this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("GeometryToUsePoint", bundle);
    }

    private GeometryToUsePointLayerPanel getGeometryToUsePointLayerPanel(PlugInContext context) {
        if (geometryToUsePointLayerPanel == null) {
            geometryToUsePointLayerPanel = new GeometryToUsePointLayerPanel(context);
        }
        return geometryToUsePointLayerPanel;
    }
    
    private GeometryToUsePointUsePanel getGeometryToUsePointUsePanel(PlugInContext context) {
        if (geometryToUsePointUsePanel == null) {
            geometryToUsePointUsePanel = new GeometryToUsePointUsePanel(context);
        }
        return geometryToUsePointUsePanel;
    }
    
    private GeometryToUsePointUsePanel getGeometryToUsePointUsePanel(PlugInContext context, Feature feature) {
        if (geometryToUsePointUsePanel == null) {
            geometryToUsePointUsePanel = new GeometryToUsePointUsePanel(context, feature);
        }
        return geometryToUsePointUsePanel;
    }

    private void initialize() {

        this.setSize(420, 225);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle(I18N.get("GeometryToUsePoint", "geometryToUsePoint.panel.title")); 
        this.setModal(true);
        this.setResizable(false);
        this.setContentPane(getJContentPane());
        this.setVisible(true);
    }

    private JPanel getJContentPane() {

        if (jContentPane == null) {
            jContentPane = new javax.swing.JPanel();
            jContentPane.setLayout(new java.awt.BorderLayout());
            jContentPane.add(getGeometryToUsePointLayerPanel(context), BorderLayout.CENTER);
            jContentPane.add(getOkCancelPanel(), java.awt.BorderLayout.SOUTH);
        }
        return jContentPane;
    }
    


    private OKCancelPanel getOkCancelPanel() {
        if (_okCancelPanel == null) {
            _okCancelPanel = new OKCancelPanel();
            _okCancelPanel.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    
                    if (_okCancelPanel.wasOKPressed()) {    
                        
                        if (geometryToUsePointUsePanel == null) {
                            String errorMessage = getGeometryToUsePointLayerPanel(context)
                                    .validateInput();
                            if (errorMessage != null) {
                                JOptionPane.showMessageDialog(GeometryToUsePointDialog.this,
                                        errorMessage, "GEOPISTA", JOptionPane.ERROR_MESSAGE);
                                _okCancelPanel.setOKPressed(false);
                                return;
                            }
                            _okCancelPanel.setOKPressed(false);
                            jContentPane.remove(geometryToUsePointLayerPanel);
                            jContentPane.add(getGeometryToUsePointUsePanel(context), BorderLayout.CENTER);
                            jContentPane.revalidate();
                        } else {
                            setVisible(false);
                            if (!evaluandoFeatures) { 
                                boolean todosElementos = context.getWorkbenchGuiComponent()
                                        .getActiveTaskComponent().getLayerViewPanel().getBlackboard()
                                        .get(GeometryToUsePointUsePanel.CHECKTODOSELEMENTOS, true);
                                if (!todosElementos && !evaluandoFeatures) { //Para que sólo entre una vez
                                    evaluandoFeatures = true;
                                    //Averiguamos si había elementos seleccionados en la capa
                                    Layer layerOrigen = (Layer) context.getWorkbenchGuiComponent()
                                            .getActiveTaskComponent().getLayerViewPanel()
                                            .getBlackboard()
                                            .get(GeometryToUsePointLayerPanel.SELECTEDSOURCELAYER);

                                    boolean tieneEntidadesSeleccionadas = false;
    
                                    Collection featuresSeleccionadas = context.getWorkbenchContext().getLayerViewPanel()
                                            .getSelectionManager().getFeaturesWithSelectedItems(layerOrigen);

                                    Iterator featuresSeleccionadasIter = featuresSeleccionadas.iterator();
                                    if (featuresSeleccionadasIter.hasNext()) { // Esto es que tiene al
                                                                               // menos una feature seleccionada
                                        tieneEntidadesSeleccionadas = true;
                                    }

                                    if (!tieneEntidadesSeleccionadas) {
                                        featuresSeleccionadas = layerOrigen.getFeatureCollectionWrapper().getFeatures();
                                    }
                                    
                                    for (Iterator i = featuresSeleccionadas.iterator(); i.hasNext();) {
                                        Feature f = (Feature) i.next();                                    
                                        jContentPane.remove(geometryToUsePointUsePanel);
                                        geometryToUsePointUsePanel = null;
                                        jContentPane.add(getGeometryToUsePointUsePanel(context, f), BorderLayout.CENTER);
                                        jContentPane.revalidate();
                                        setVisible(true);                                        
                                    }                                
                                } else {
                                    remove(geometryToUsePointUsePanel);
                                    setVisible(false);
                                }
                                return;
                            } 
                        }
                    } else {
                        setVisible(false);
                        return;
                    }

                }
            });
        }
        return _okCancelPanel;
    }
    
    public boolean wasOKPressed() {
        return _okCancelPanel.wasOKPressed();
    }

}
