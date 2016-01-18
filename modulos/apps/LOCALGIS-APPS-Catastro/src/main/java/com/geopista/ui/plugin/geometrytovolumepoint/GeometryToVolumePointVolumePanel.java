/**
 * 
 */
package com.geopista.ui.plugin.geometrytovolumepoint;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.intercambio.edicion.utils.EstructuraDBListCellRenderer;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.ui.plugin.geometrytovolumepoint.intercambio.util.EdicionOperations;
import com.geopista.ui.plugin.geometrytovolumepoint.model.beans.TipoSubparcela;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;



/**
 * @author seilagamo
 *
 */
public class GeometryToVolumePointVolumePanel extends JPanel {
    
    private static AppContext app = (AppContext) AppContext.getApplicationContext();
    private PlugInContext context = null;
    
    private JLabel jLabelTipoSubparcela = null;
    private JComboBox jComboBoxTipoSubparcela = null;
    private JLabel jLabelTipoCultivo = null;
    private ComboBoxEstructuras jComboBoxTipoCultivo = null;
    private JLabel jLabelCodigoAtributo = null;
    private JTextField jTextCodigoAtributo = null;
    private JCheckBox jCheckAtributoTodosElementos = null;
    private JLabel jLabelFeature = null;
    private Feature feature = null;

    public final static String SELECTEDTIPOSUBPARCELA = GeometryToVolumePointVolumePanel.class
            .getName() + "_tipoSubparcela";
    public final static String SELECTEDTIPOCULTIVO = GeometryToVolumePointVolumePanel.class
            .getName() + "_tipoCultivo";
    public final static String TEXTCODIGOATRIBUTO = GeometryToVolumePointVolumePanel.class
            .getName() + "_codigoAtributo";
    public final static String CHECKTODOSELEMENTOS = GeometryToVolumePointVolumePanel.class
            .getName() + "_todosElementos";
    
    public GeometryToVolumePointVolumePanel(PlugInContext context) {
        super();
        this.context = context;
        initialize();
    }
    
    public GeometryToVolumePointVolumePanel(PlugInContext context, Feature feature) {
        super();
        this.context = context;
        this.feature = feature;
        initialize();
    }
    
    private void initialize() {
        Locale loc = I18N.getLocaleAsObject();
        ResourceBundle bundle = ResourceBundle.getBundle(
                "com.geopista.ui.plugin.geometryToVolumePoint.languages.GeometryToVolumePointi18n", loc,
                this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("GeometryToVolumePoint", bundle);

        this.setLayout(new GridBagLayout());
        
        if (feature != null) {
            jLabelFeature = new JLabel(I18N.get("GeometryToVolumePoint", "geometryToVolumePoint.label.feature"));
            jLabelFeature.setText(jLabelFeature.getText() + " " + feature.getID());
            this.add(jLabelFeature, new GridBagConstraints(0, 0, 2, 1, 0.1, 0.1,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
                    0, 0));
        }
        jLabelTipoSubparcela = new JLabel(I18N.get("GeometryToVolumePoint",
                "geometryToVolumePoint.label.tipoSubparcela"), JLabel.CENTER);
        this.add(jLabelTipoSubparcela, new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
                0, 0));
        this.add(getJComboBoxTipoSubparcela(), new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
                0, 0));
        jLabelTipoCultivo = new JLabel(I18N.get("GeometryToVolumePoint",
                "geometryToVolumePoint.label.tipoCultivo"), JLabel.CENTER);
        this.add(jLabelTipoCultivo, new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
                0, 0));
        this.add(getJComboBoxTipoCultivo(), new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
                0, 0));
        jLabelCodigoAtributo = new JLabel(I18N.get("GeometryToVolumePoint",
                "geometryToVolumePoint.label.codigoAtributo"), JLabel.CENTER);
        this.add(jLabelCodigoAtributo, new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
                0, 0));
        this.add(getJTextCodigoAtributo(), new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
                0, 0));
        if (feature == null) {
            this.add(getJCheckAtributoTodosElementos(), new GridBagConstraints(0, 4, 2, 1, 0.1, 0.1,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
                    0, 0));
        }
        initComboBoxes();
        initListeners();
        putToBlackboard(TEXTCODIGOATRIBUTO, "");
    }
    
    private void initComboBoxes() {
        // Inicializa los desplegables
        if (getFromBlackboard("ListaTipoSubparcela") == null) {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerTiposSubparcela();
            putToBlackboard("ListaTipoSubparcela", lst);
            EdicionUtils.cargarLista(getJComboBoxTipoSubparcela(), lst);
        } else {
            EdicionUtils.cargarLista(getJComboBoxTipoSubparcela(),
                    (ArrayList) getFromBlackboard("ListaTipoSubparcela"));
        }        
        putToBlackboard(SELECTEDTIPOSUBPARCELA, jComboBoxTipoSubparcela.getSelectedItem());
    }
    
    /**
     * Añade acciones para cuando se modifica alguno de los combos
     */
    private void initListeners(){
        jComboBoxTipoSubparcela.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jTextCodigoAtributo.setText(getCodigoAtributo());      
                putToBlackboard(SELECTEDTIPOSUBPARCELA, ((JComboBox) e.getSource())
                        .getSelectedItem());
            }
        });  
        
        jComboBoxTipoCultivo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jTextCodigoAtributo.setText(aniadeCodigoCultivo());
                putToBlackboard(SELECTEDTIPOCULTIVO, ((JComboBox) e.getSource())
                        .getSelectedItem());
            }
        });  
    }
    
    /**
     * This method initializes jComboBoxTipoSubparcela
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getJComboBoxTipoSubparcela() {
        if (jComboBoxTipoSubparcela == null) {
            jComboBoxTipoSubparcela = new JComboBox();
            jComboBoxTipoSubparcela.setRenderer(new EstructuraDBListCellRenderer());
            jComboBoxTipoSubparcela.setKeySelectionManager(new ComboBoxTipoSubparcelaSelectionManager());
        }
        return jComboBoxTipoSubparcela;
    }

    private JComboBox getJComboBoxTipoCultivo() {
        if (jComboBoxTipoCultivo == null) {
            Estructuras.cargarEstructura("Tipo de cultivo");
            jComboBoxTipoCultivo = new ComboBoxEstructuras(Estructuras.getListaTipos(), null,
                    AppContext.getApplicationContext().getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,
                            "es_ES"), false);
            putToBlackboard(SELECTEDTIPOCULTIVO, jComboBoxTipoCultivo.getSelectedItem());
        }
        return jComboBoxTipoCultivo;
    }
    
    private JTextField getJTextCodigoAtributo() {
        if (jTextCodigoAtributo == null) {
            jTextCodigoAtributo = new JTextField("", 20);
            jTextCodigoAtributo.addFocusListener(new FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    putToBlackboard(TEXTCODIGOATRIBUTO, jTextCodigoAtributo.getText());
                }
            });
        }
        return jTextCodigoAtributo;
    }
    
    private JCheckBox getJCheckAtributoTodosElementos() {
        if (jCheckAtributoTodosElementos == null) {
            jCheckAtributoTodosElementos = new JCheckBox(I18N.get("GeometryToVolumePoint", "geometryToVolumePoint.check.atributoTodosElementos"), true);
            putToBlackboard(CHECKTODOSELEMENTOS, jCheckAtributoTodosElementos.isSelected());
            jCheckAtributoTodosElementos.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    putToBlackboard(CHECKTODOSELEMENTOS, jCheckAtributoTodosElementos.isSelected());
                }
            });
        }
        return jCheckAtributoTodosElementos;
    }
    
    
    /**
     * Calcula el código de atributo
     * @return
     */
    private String getCodigoAtributo() {
        String codigoAtributo = jTextCodigoAtributo.getText();

        if (jComboBoxTipoSubparcela != null) {

            TipoSubparcela tipoSubparcela = (TipoSubparcela)jComboBoxTipoSubparcela.getSelectedItem();
            if (codigoAtributo.equals("")) {
                codigoAtributo = codigoAtributo + tipoSubparcela.getPatron();
            } else {
                codigoAtributo = codigoAtributo + "+" + tipoSubparcela.getPatron();
            }            
        }

        if (feature == null) {
            putToBlackboard(TEXTCODIGOATRIBUTO, codigoAtributo);
        } else {
            putToBlackboard(Integer.toString(feature.getID()), codigoAtributo);
        }
        return codigoAtributo;
    }
    
    
    private String aniadeCodigoCultivo() {
        String codigoAtributo = jTextCodigoAtributo.getText();
        if (jComboBoxTipoCultivo != null && jComboBoxTipoCultivo.getSelectedPatron() != null) {                        
            if (codigoAtributo.equals("")) {
                codigoAtributo = codigoAtributo + jComboBoxTipoCultivo.getSelectedPatron();
            } else {
                codigoAtributo = codigoAtributo + "+" + jComboBoxTipoCultivo.getSelectedPatron();
            }            
        }
        if (feature == null) {
            putToBlackboard(TEXTCODIGOATRIBUTO, codigoAtributo);
        } else {
            putToBlackboard(Integer.toString(feature.getID()), codigoAtributo);
        }
        return codigoAtributo;
    }
    
    // Métodos del Blackboard

    private Object getFromBlackboard(String key) {
        try {
            Blackboard bk = context.getWorkbenchGuiComponent().getActiveTaskComponent()
                    .getLayerViewPanel().getBlackboard();
            return bk.get(key);
        } catch (NullPointerException e) {// ocurre cuando la ventana activa no es de GIS
            return null;
        }
    }

    private void putToBlackboard(String key, Object value) {
        try {
            Blackboard bk = context.getWorkbenchGuiComponent().getActiveTaskComponent()
                    .getLayerViewPanel().getBlackboard();
            bk.put(key, value);
        } catch (NullPointerException e) {// ocurre cuando la ventana activa no es de GIS
            return;
        }
    }
}
