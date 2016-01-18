/**
 * 
 */
package com.geopista.ui.plugin.geometrytoareapoint;

import java.awt.BorderLayout;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;


/**
 * @author seilagamo
 *
 */
public class GeometryToAreaPointDialog extends JDialog {
    
    private PlugInContext context = null;
    private JPanel jContentPane = null;
    private GeometryToAreaPointPanel geometryToAreaPointPanel = null;
    private OKCancelPanel _okCancelPanel = null;
    private boolean tieneEntidadesSeleccionadas = false;
    
    /**
     * This is the default constructor
     */
    public GeometryToAreaPointDialog(PlugInContext context, boolean tieneEntidadesSeleccionadas) {

        super();
        this.context = context;
        this.tieneEntidadesSeleccionadas = tieneEntidadesSeleccionadas;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        initialize();
        Locale loc = Locale.getDefault();
        ResourceBundle bundle = ResourceBundle.getBundle(
                "com.geopista.ui.plugin.geometryToAreaPoint.languages.GeometryToAreaPointi18n", loc,
                this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("GeometryToAreaPoint", bundle);
    }
    
    private GeometryToAreaPointPanel getGeometryToAreaPointPanel(PlugInContext context, boolean tieneEntidadesSeleccionadas) {
        if (geometryToAreaPointPanel == null) {
            geometryToAreaPointPanel = new GeometryToAreaPointPanel(context, tieneEntidadesSeleccionadas);
        }
        return geometryToAreaPointPanel;
    }
    
    private void initialize() {

        this.setSize(420, 225);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle(I18N.get("GeometryToAreaPoint", "GeometryToAreaPoint")); 
        this.setModal(true);
        this.setResizable(false);
        this.setContentPane(getJContentPane());
        this.setVisible(true);
    }
    
    private JPanel getJContentPane() {

        if (jContentPane == null) {
            jContentPane = new javax.swing.JPanel();
            jContentPane.setLayout(new java.awt.BorderLayout());
            jContentPane.add(getGeometryToAreaPointPanel(context, tieneEntidadesSeleccionadas), BorderLayout.CENTER);
            jContentPane.add(getOkCancelPanel(), java.awt.BorderLayout.SOUTH);
        }
        return jContentPane;
    }
    
    private OKCancelPanel getOkCancelPanel() {
        if (_okCancelPanel == null) {
            _okCancelPanel = new OKCancelPanel();
            _okCancelPanel.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                	
                	if (!_okCancelPanel.wasOKPressed()) {
                	    setVisible(false);
                	    return;
                    } else {
                        String errorMessage = getGeometryToAreaPointPanel(context,
                                tieneEntidadesSeleccionadas).validateInput();
                        if (errorMessage != null) {
                            JOptionPane.showMessageDialog(GeometryToAreaPointDialog.this,
                                    errorMessage, "GEOPISTA", JOptionPane.ERROR_MESSAGE);
                            _okCancelPanel.setOKPressed(false);
                            return;
                        }
                    }
                    setVisible(false);
                }
            });
        }
        return _okCancelPanel;
    }
    
    public boolean wasOKPressed() {
        return _okCancelPanel.wasOKPressed();
    }

}
